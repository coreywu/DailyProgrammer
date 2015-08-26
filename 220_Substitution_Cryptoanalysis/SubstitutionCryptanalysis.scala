import scala.io.Source
import scala.collection.immutable.HashMap
import scala.collection.immutable.Set

object SubstitutionCryptanalysis {
  
  class Dictionary() {
    var dictionary: Map[String, Set[String]] = HashMap()
    var patternCount: Map[String, Int] = HashMap()
    
    def addWordLetterPattern(word: String) = {
      addWord(convertToLetterPattern(word), word)
    }
    
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

    def addWord(key: String, value: String) = {
      if (dictionary.contains(key)) {
        dictionary = dictionary + (key -> (dictionary(key) + (value)))
        patternCount = patternCount + (key -> (patternCount(key) + 1))
      } else {
        dictionary = dictionary + (key -> Set(value))
        patternCount = patternCount + (key -> 1)
      }
    }
    
  }
  
  def findSubstitutionEncoding(input: String): Unit = {
    val filename = "src/resources/words.txt"
    val dictionary = buildEncodingDictionary(filename)
    
    println(dictionary.dictionary.take(10))
    println(dictionary.patternCount.take(10))

    val splitInput = input.split("\n")
    
    val encodedWords = splitInput.head.split(" ")
    val letterPatternWords: Array[String] = encodedWords.map {
        word => dictionary.convertToLetterPattern(word.toLowerCase())
    }
        
    // Map of encoded words (original) to patterns (alphabetical patterns)
    val encodedToPattern: Map[String, String] = encodedWords.zip(letterPatternWords).toMap
    val sortedLetterPatternWords = letterPatternWords.sortBy(word => dictionary.patternCount(word))

    println("Encoded to Pattern Words 1")
    encodedToPattern.foreach(println)
    
//    println("Letter Pattern Words 1")
//    letterPatternWords.foreach(println)
//    
//    println("Letter Pattern Words 2")
//    sortedLetterPatternWords.foreach(println)
//
//    println("Letter Pattern Words Values 1")
//    letterPatternWords.map(word => (word, dictionary.patternCount(word))).foreach(println)
//
//    println("Letter Pattern Words Values 2")
//    sortedLetterPatternWords.map(word => (word, dictionary.patternCount(word))).foreach(println)
    
//    for (word: String <- dictionary.dictionary(sortedLetterPatternWords.head)) {
//      if (word.contains)
//    }

    // Parse the given letter substitutions
    var substitutions: Map[Char, Char] = HashMap()
    splitInput.drop(2).foreach(pair => substitutions += (pair.charAt(0).toUpper -> pair.charAt(1)))
    
    println("Substitutions: " + substitutions)

  }

  def recursiveSubstitutionEncoding(substitutions: Map[Char, Char], encodedWords: Array[String], 
      encodedToPattern: Map[String, String], sortedLetterPatternWords: Array[String], 
      dictionary: Dictionary): Option[Map[Char, Char]] = {
    
    if (encodedWords.length == 0) return Some(substitutions)

    // Find all possible letter substitutions given the set of encoded words
    var possibleSubstitutions: Map[Char, Set[Char]] = HashMap()
    
    for (encodedWord: String <- encodedWords) {
      var candidateSubstitutions: Map[Char, Set[Char]] = HashMap()
      for (possibleWord: String <- dictionary.dictionary(encodedToPattern(encodedWord))) {
        for (i <- 0 until possibleWord.length()) {
          val encodedLetter = encodedWord.charAt(i)
          if (!substitutions.contains(encodedLetter)) {
        	  val possibleLetter = possibleWord.charAt(i).toUpper
        		if (!candidateSubstitutions.contains(encodedLetter)) {
        		  candidateSubstitutions += encodedLetter -> Set(possibleLetter)
        	  } else {
        		  candidateSubstitutions += encodedLetter ->
        			  (candidateSubstitutions(encodedLetter) + possibleLetter)
        		}
          }
        }
      }
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
    
    println("Possible Substitutions: " + possibleSubstitutions)
    
    // Select the word with the fewest possible matches and recurse
    val firstWordToSubstitute = sortedLetterPatternWords.head
    println("firstWordToSubstitute: " + firstWordToSubstitute)
    
    for (possibleWord <- dictionary.dictionary(firstWordToSubstitute)) {
      // Check if the word is valid given our information about possible letter
      // substitutions given our set of encoded words.
      var valid = true
      for (i <- 0 until firstWordToSubstitute.length()) {
        val encodedLetter = firstWordToSubstitute.charAt(i)
        val possibleLetter = possibleWord.charAt(i)
        if (!possibleSubstitutions(encodedLetter).contains(possibleLetter)) {
          valid = false   
        }
      }
      if (valid) {
        // Add all letter substitutions to our substitution map and recurse
        // down.
    	  var newSubstitutions: Map[Char, Char] = HashMap() ++ substitutions
        for (i <- 0 until firstWordToSubstitute.length()) {
        	val encodedLetter = firstWordToSubstitute.charAt(i)
        	val possibleLetter = possibleWord.charAt(i)
          newSubstitutions += encodedLetter -> possibleLetter
        }
        
        val newEncodedWords = sortedLetterPatternWords.tail
        val newEncodedToPattern = encodedToPattern - firstWordToSubstitute
        val newSortedLetterPatternWords = sortedLetterPatternWords.tail

        return recursiveSubstitutionEncoding(newSubstitutions, newEncodedWords, newEncodedToPattern, newSortedLetterPatternWords, dictionary)
      }
    }
    
    None
  }

  def buildEncodingDictionary(filename: String): Dictionary = {
    val dictionary = new Dictionary()
    for (word <- Source.fromFile(filename).getLines()) {
        dictionary.addWordLetterPattern(word)
    }
    dictionary
  }

  def main(args: Array[String]) {
    
    val input = """IAL FTNHPL PDDI DR RDNP WF IUD
                  |2
                  |aH
                  |oD""".stripMargin
                  
    println("Result: " + findSubstitutionEncoding(input))
  }
}
