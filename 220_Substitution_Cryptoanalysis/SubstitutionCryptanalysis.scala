import scala.io.Source
import scala.collection.immutable.HashMap
import scala.collection.immutable.Set
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.Buffer

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
    
    val splitInput = input.split("\n")
    
    val encodedWords: Buffer[String] = splitInput.head.split(" ").toBuffer
    val letterPatternWords: Buffer[String] = encodedWords.map {
        word => dictionary.convertToLetterPattern(word.toLowerCase())
    }
        
    // Map of encoded words (original) to patterns (alphabetical patterns)
    val encodedToPattern: Buffer[(String, String)] = encodedWords.zip(letterPatternWords)
    val sortedLetterPatternWords: Buffer[String] = letterPatternWords.sortBy(word => dictionary.patternCount(word))

//    println("Encoded Words:")
//    encodedWords.foreach(println)
//
//    println("Encoded to Pattern Words 1")
//    encodedToPattern.foreach(println)
    
    // Parse the given letter substitutions
    var substitutions: Map[Char, Char] = HashMap()
    splitInput.drop(2).foreach(pair => substitutions += (pair.charAt(1) -> pair.charAt(0).toUpper))
    
//    println("Substitutions: " + substitutions)
    
    val result: Buffer[Map[Char, Char]] = recursiveSubstitutionEncoding(substitutions, encodedToPattern, sortedLetterPatternWords, dictionary)
    
    if (result.size > 0) {
      println("No encoding can be found")
    } else {
    	// Format output of result(s)
      val substitutionsBuffer = result
    	println(encodedWords.map(word => word.map(letter => substitutions(letter))).mkString(" "));
    }

  }

  def recursiveSubstitutionEncoding(substitutions: Map[Char, Char], 
      encodedToPattern: Buffer[(String, String)], sortedLetterPatternWords: Buffer[String], 
      dictionary: Dictionary): Buffer[Map[Char, Char]] = {
    
    if (encodedToPattern.length == 0) return Buffer(substitutions)
    
    var resultBuffer: Buffer[Map[Char, Char]] = Buffer()

    val usedLetters: Set[Char] = substitutions.values.toSet
    
    // Find all possible letter substitutions given the set of encoded words
    var possibleSubstitutions: Map[Char, Set[Char]] = HashMap()
    
//    println("Encoded Words 2:")
//    encodedWords.foreach(println)

    for (encodedWord: String <- encodedToPattern.map(tuple => tuple._1)) {
//      println("Encoded Word: " + encodedWord)
//      println("Encoded to Pattern: " + getPattern(encodedToPattern, encodedWord))
      var candidateSubstitutions: Map[Char, Set[Char]] = HashMap()
      for (possibleWord: String <- dictionary.dictionary(getPattern(encodedToPattern, encodedWord))) {
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
    
//    println("Possible Substitutions: " + possibleSubstitutions)
    
    // Select the word with the fewest possible matches and recurse
    val firstWordToSubstitute = sortedLetterPatternWords.head
//    println("firstWordToSubstitute: " + firstWordToSubstitute)
    
    for (possibleWord <- dictionary.dictionary(firstWordToSubstitute)) {
      // Check if the word is valid given our information about possible letter
      // substitutions given our set of encoded words.
      var valid = true
      for (i <- 0 until firstWordToSubstitute.length()) {
//        println("Encoded to Pattern" + encodedToPattern)
        val encodedLetter = getEncoded(encodedToPattern, firstWordToSubstitute).charAt(i)
        val possibleLetter = possibleWord.charAt(i).toUpper
//        println("Substitutions: " + substitutions)
//        println("Possible word: " + possibleWord + " encodedword: " + getEncoded(encodedToPattern, firstWordToSubstitute))
        if (!substitutions.contains(encodedLetter) && !possibleSubstitutions(encodedLetter).contains(possibleLetter)) {
          valid = false   
        } 
        if (substitutions.contains(encodedLetter) && !substitutions(encodedLetter).equals(possibleLetter)) {
          valid = false
        }
      }
//      println("PossibleWord: " + possibleWord + " " + valid)
      if (valid) {
        // Add all letter substitutions to our substitution map and recurse
        // down.
    	  var newSubstitutions: Map[Char, Char] = HashMap() ++ substitutions
        for (i <- 0 until firstWordToSubstitute.length()) {
        	val encodedLetter = getEncoded(encodedToPattern, firstWordToSubstitute).charAt(i)
        	val possibleLetter = possibleWord.charAt(i).toUpper
          newSubstitutions += encodedLetter -> possibleLetter
        }
        
        val substitutedEncodedWord = getEncoded(encodedToPattern, firstWordToSubstitute)
        val substitutedPatternWord = firstWordToSubstitute
        
//        val newEncodedWords: Buffer[String] = encodedWords - substitutedEncodedWord
        val newEncodedToPattern: Buffer[(String, String)] = encodedToPattern - ((substitutedEncodedWord, substitutedPatternWord))
//        println("Encoded: " + encodedWords + " new: " + newEncodedWords)
        val newSortedLetterPatternWords: Buffer[String] = sortedLetterPatternWords.tail

        val result = recursiveSubstitutionEncoding(newSubstitutions, newEncodedToPattern, newSortedLetterPatternWords, dictionary)
        if (result.size > 0) {
          return resultBuffer ++= result
        }
      }
    }
    
    Buffer()
  }

  def buildEncodingDictionary(filename: String): Dictionary = {
    val dictionary = new Dictionary()
    for (word <- Source.fromFile(filename).getLines()) {
        dictionary.addWordLetterPattern(word)
    }
    dictionary
  }
  
  def getPattern(encodedToPattern: Buffer[(String, String)], encodedWord: String): String = {
    encodedToPattern.find{_._1.equals(encodedWord)}.get._2
  }

  def getEncoded(encodedToPattern: Buffer[(String, String)], patternWord: String): String = {
    encodedToPattern.find{_._2.equals(patternWord)}.get._1
  }

  def main(args: Array[String]) {
    
    val input1 = """IAL FTNHPL PDDI DR RDNP WF IUD
                   |2
                   |aH
                   |oD""".stripMargin
                  
    
//    val input2 = """LBH'ER ABG PBBXVAT CBEX PUBC FNAQJVPURF
//                   |2
//                   |rE
//                   |wJ""".stripMargin

    val input3 = """ABCDEF
                   |2
                   |aC
                   |zF""".stripMargin

//    val input4 = """WRKZ DG ZRDG D AOX'Z VQVX
//                   |2
//                   |wW
//                   |sG""".stripMargin

    val input5 = """JNOH MALAJJGJ SLNOGQ JSOGX
                   |1
                   |sX""".stripMargin
    
    findSubstitutionEncoding(input1)
//  findSubstitutionEncoding(input2)
//    findSubstitutionEncoding(input3)
//  findSubstitutionEncoding(input4)
//    findSubstitutionEncoding(input5)
  }
}