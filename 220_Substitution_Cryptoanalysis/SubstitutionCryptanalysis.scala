import scala.io.Source
import scala.collection.immutable.HashMap
import scala.collection.immutable.Set
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.Buffer

/**
 * Solve a substitution cipher using heuristics outlined in the Quipqiup
 * algorithm: http://quipqiup.com/howwork.php
 * 
 * Letter patterns to decipher will be referred to as "encoded" words, and
 * sequences of letters in the dictionary will be referred to as "pattern"
 * words (eg. ABACDAEFG is a pattern for encoded word RGRXSRKHM).
 */
object SubstitutionCryptanalysis {
  
  /**
   * Dictionary class which stores a dictionary of patterns to words that are
   * formed from that pattern of letters. For example, the word QUARTZ has a
   * letter pattern of ABCDE, and PARALLEL has ABCBDDED.
   * 
   * Using this dictionary, we can transform our encoded words (eg MALAJJGJ)
   * into patterns (ABCBDDED), then into English words.
   */
  class Dictionary() {
    private var dictionary: Map[String, Set[String]] = HashMap()
    
    /**
     * Add an English word to the dictionary by converting it into a letter
     * pattern then adding to the map.
     * 
     * @param word the English word to add
     */
    def addWordLetterPattern(word: String) = {
      addWord(convertToLetterPattern(word), word)
    }
    
    /**
     * Convert a String to the corresponding letter pattern that it matches.
     * 
     * @param word String to convert into letter pattern
     */
    def convertToLetterPattern(word: String) = {
      var newWord = word
      var currentLetter = 'A'
      for (i <- 0 until word.length()) {
        val letter = newWord.charAt(i)
        if (letter.isLower) {
          newWord = newWord.replace(letter, currentLetter)
          currentLetter = (currentLetter.toInt + 1).toChar
        }
      }
      newWord
    }

    private def addWord(key: String, value: String) = {
      if (dictionary.contains(key)) {
        dictionary = dictionary + (key -> (dictionary(key) + (value)))
      } else {
        dictionary = dictionary + (key -> Set(value))
      }
    }
    
    /**
     * Return the set of English words that correspond to a letter pattern.
     * 
     * @param pattern pattern to return English words for
     * @return a set of English words that correspond to the pattern
     */
    def getPatternSet(pattern: String): Set[String] = {
      dictionary(pattern)
    }

    /**
     * Return the number of English words that correspond to a letter pattern.
     * 
     * @param pattern pattern to return number of English words for
     * @return the number of English words that correspond to the pattern
     */
    def getPatternCount(pattern: String): Int = {
      dictionary(pattern).size
    }
    
  }
  
  /**
   * Print out all valid deciphered lines given an encoded input.
   * 
   * @param input String to decipher
   */
  def findSubstitutionEncoding(input: String): Unit = {
    // Use a word list to create the dictionary that we will reference
    val filename = "src/resources/words.txt"
    val dictionary = buildEncodingDictionary(filename)
    
    // Parse input and create buffers of encoded words (eg. RGRXSRKHM) and 
    // pattern words (eg. RGRXSRKHM)
    val splitInput = input.split("\n")
    val encodedWords: Buffer[String] = splitInput.head.split(" ").toBuffer
    val letterPatternWords: Buffer[String] = encodedWords.map {
        word => dictionary.convertToLetterPattern(word.toLowerCase())
    }
        
    // Create a buffer of tuples which contain the encoded words as the first
    // element and their matching patterns as the second 
    val encodedToPattern: Buffer[(String, String)] = encodedWords.zip(letterPatternWords)
    
    // Create a buffer of pattern words, sorted by smallest pattern set size to
    // largest. Pattern words with smaller pattern word sets are deciphered 
    // first because they are more restrictive; there are likely fewer valid
    // letters for patterns with fewer word lists.
    val sortedLetterPatternWords: Buffer[String] = letterPatternWords.sortBy(word => dictionary.getPatternCount(word))
    
    // Parse the given letter substitutions and add them to the HashMap
    var substitutions: Map[Char, Char] = HashMap()
    splitInput.drop(2).foreach(pair => substitutions += (pair.charAt(1) -> pair.charAt(0).toUpper))
    
    // Recursively search for valid decodings of the sentence
    val resultSubstitutions: Buffer[Map[Char, Char]] = recursiveSubstitutionEncoding(substitutions, encodedToPattern, sortedLetterPatternWords, dictionary)
    
    if (resultSubstitutions.size > 0) {
    	// Format output of result(s)
    	val resultStrings = resultSubstitutions.map(resultSubstitution => encodedWords.map(word => word.map(letter => resultSubstitution(letter))).mkString(" "));
      println("Result: " + resultStrings.mkString("\n"))
    } else {
      println("No encoding can be found.")
    }
  }

  /**
   * Recursively find decodings of words and determine if the required 
   * substitutions are valid, given our previous substitutions.
   * 
   * @param substitutions all currently used substitutions from encoded letters
   *                      to actual English word letters
   * @param encodedToPattern the map of encoded words to pattern words
   * @param sortedLetterPatternWords a sorted list of pattern words, in order
   *                                 of least valid words in pattern set to 
   *                                 most
   * @param dictionary the dictionary that we use to match from letter patterns
   *                   to sets of corresponding English words
   * @return all valid substitutions that can be used to decipher the encoded
   * words into valid English words
   */
  def recursiveSubstitutionEncoding(substitutions: Map[Char, Char], 
      encodedToPattern: Buffer[(String, String)], sortedLetterPatternWords: Buffer[String], 
      dictionary: Dictionary): Buffer[Map[Char, Char]] = {
    
    // Return the substitution as valid if we have deciphered all words
    if (encodedToPattern.length == 0) {
      return Buffer(substitutions)
    }
    
    var resultBuffer: Buffer[Map[Char, Char]] = Buffer()

    // Letters that have been previously used cannot be used for deciphering
    // another encoded letter (eg. if A -> B, C -/> B).
    val usedLetters: Set[Char] = substitutions.values.toSet
    
    // Find all possible letter substitutions given the set of encoded words.
    // This finds all letter substitutions for a letter pattern word then
    // creates a set of valid substitutions for each letter, given that word.
    // These sets are created for each word and a set intersection is performed
    // on matching letters of all words for common substitutions. This prunes
    // the number of candidate substitutions by discovering invalid
    // substitutions early.
    //
    // For example, if the pattern word ABACDAEFGH maps to the words ADAPTABLE,
    // ENERGETIC, EMERGENCY, and NONLINEAR, we know that X -> {A, E, N}. If we 
    // find that from another word, X -> {E, N, Y}, we know that X -> {E, N}
    // because if we select X -> A to decipher the first word, we will never
    // find a valid deciphering of the second word using X -> A.
    var possibleSubstitutions: Map[Char, Set[Char]] = HashMap()
    
    for (encodedWord: String <- encodedToPattern.map(tuple => tuple._1)) {
      // First, create the map of candidate substitutions (substitutions that
      // are valid before we perform set intersection)
      var candidateSubstitutions: Map[Char, Set[Char]] = HashMap()
      for (possibleWord: String <- dictionary.getPatternSet(getPattern(encodedToPattern, encodedWord))) {
        for (i <- 0 until possibleWord.length()) {
          val encodedLetter = encodedWord.charAt(i)
        	val possibleLetter = possibleWord.charAt(i).toUpper
          if (!substitutions.contains(encodedLetter) && !usedLetters.contains(possibleLetter)) {
        		if (!candidateSubstitutions.contains(encodedLetter)) {
        		  candidateSubstitutions += encodedLetter -> Set(possibleLetter)
        	  } else {
        		  candidateSubstitutions += encodedLetter ->
        			  (candidateSubstitutions(encodedLetter) + possibleLetter)
        		}
          }
        }
      }
      // Perform the set intersection step for each letter in our candidate set 
      // (for our new word against our "possible" set (set we have already 
      // performed set intersection on using the previous word).
      if (possibleSubstitutions.isEmpty) {
    	  possibleSubstitutions ++= candidateSubstitutions
      } else {
    	  for (letter: Char <- candidateSubstitutions.keys) {
    		  if (possibleSubstitutions.contains(letter)) {
    			  val newPossibleSubstitutionsForLetter: Set[Char] = 
                possibleSubstitutions(letter) & candidateSubstitutions(letter)
            possibleSubstitutions += letter -> newPossibleSubstitutionsForLetter
    		  } else {
            possibleSubstitutions += letter -> candidateSubstitutions(letter) 
          }
    	  }
      }
    }
    
    // Select the word with the fewest possible matches to decipher first
    val firstWordToSubstitute = sortedLetterPatternWords.head
    
    for (possibleWord <- dictionary.getPatternSet(firstWordToSubstitute)) {
      // Check if the word is valid given our information about possible letter
      // substitutions (generated from the set intersection step)
      var valid = true
      for (i <- 0 until firstWordToSubstitute.length()) {
        val encodedLetter = getEncoded(encodedToPattern, firstWordToSubstitute).charAt(i)
        val possibleLetter = possibleWord.charAt(i).toUpper
        if (!substitutions.contains(encodedLetter) && !possibleSubstitutions(encodedLetter).contains(possibleLetter)) {
          valid = false   
        } 
        if (substitutions.contains(encodedLetter) && !substitutions(encodedLetter).equals(possibleLetter)) {
          valid = false
        }
      }

      if (valid) {
        // Add all letter substitutions used by selecting this word as a valid
        // deciphering and recurse to decipher the next words
    	  var newSubstitutions: Map[Char, Char] = HashMap() ++ substitutions
        for (i <- 0 until firstWordToSubstitute.length()) {
        	val encodedLetter = getEncoded(encodedToPattern, firstWordToSubstitute).charAt(i)
        	val possibleLetter = possibleWord.charAt(i).toUpper
          newSubstitutions += encodedLetter -> possibleLetter
        }
        
        val substitutedEncodedWord = getEncoded(encodedToPattern, firstWordToSubstitute)
        val substitutedPatternWord = firstWordToSubstitute
        
        val newEncodedToPattern: Buffer[(String, String)] = encodedToPattern - ((substitutedEncodedWord, substitutedPatternWord))
        val newSortedLetterPatternWords: Buffer[String] = sortedLetterPatternWords.tail

        val result = recursiveSubstitutionEncoding(newSubstitutions, newEncodedToPattern, newSortedLetterPatternWords, dictionary)
        if (result.size > 0) {
          resultBuffer ++= result
        }
      }
    }
    
    resultBuffer
  }

  /**
   * Builds the dictionary by reading the word list from a file and converting
   * all words into letter patterns.
   * 
   * @param filename filename of the word list to read
   * @return the Dictionary that contains all letter patterns to words in the
   * given word list
   */
  def buildEncodingDictionary(filename: String): Dictionary = {
    val dictionary = new Dictionary()
    for (word <- Source.fromFile(filename).getLines()) {
        dictionary.addWordLetterPattern(word)
    }
    dictionary
  }
  
  /**
   * Get the corresponding pattern of an encoded word.
   * 
   * @param encodedWord the encoded word
   * @return the corresponding pattern word
   */
  def getPattern(encodedToPattern: Buffer[(String, String)], encodedWord: String): String = {
    encodedToPattern.find{_._1.equals(encodedWord)}.get._2
  }

  /**
   * Get the corresponding encoded word of a pattern word.
   * 
   * @param patternWord the pattern word
   * @return the corresponding encoded word
   */
  def getEncoded(encodedToPattern: Buffer[(String, String)], patternWord: String): String = {
    encodedToPattern.find{_._2.equals(patternWord)}.get._1
  }

  def main(args: Array[String]) {
    
    val input1 = """IAL FTNHPL PDDI DR RDNP WF IUD
                   |2
                   |aH
                   |oD""".stripMargin
                  
    
    val input2 = """LBH'ER ABG PBBXVAT CBEX PUBC FNAQJVPURF
                   |2
                   |rE
                   |wJ""".stripMargin

    val input3 = """ABCDEF
                   |2
                   |aC
                   |zF""".stripMargin

    val input4 = """WRKZ DG ZRDG D AOX'Z VQVX
                   |2
                   |wW
                   |sG""".stripMargin

    val input5 = """JNOH MALAJJGJ SLNOGQ JSOGX
                   |1
                   |sX""".stripMargin
    
    findSubstitutionEncoding(input1)
    findSubstitutionEncoding(input2)
    findSubstitutionEncoding(input3)
    findSubstitutionEncoding(input4)
    findSubstitutionEncoding(input5)
  }
}