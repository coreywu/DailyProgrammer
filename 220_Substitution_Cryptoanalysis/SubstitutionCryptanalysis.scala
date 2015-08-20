import scala.io.Source
import scala.collection.immutable.HashMap
import scala.collection.immutable.Set

object SubstitutionCryptanalysis {
  
  class Dictionary() {
    var dictionary: HashMap[String, Set[String]] = HashMap()
    var patternCount: HashMap[String, Int] = HashMap()
    
    def addWordLetterPattern(word: String) = {
      addWord(convertToLetterPattern(word), word)
    }
    
    def convertToLetterPattern(word: String) = {
      var newWord = word
      var currentLetter = 'A'
      for (letter <- word) {
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

  def main(args: Array[String]) {
    val filename = "src/resources/words.txt"
    val dictionary = new Dictionary()
      
    for (word <- Source.fromFile(filename).getLines()) {
        dictionary.addWordLetterPattern(word)
    }
    
    println(dictionary.dictionary.take(10))
    println(dictionary.patternCount.take(10))
    
    val input = """IAL FTNHPL PDDI DR RDNP WF IUD
                  |2
                  |aH
                  |oD""".stripMargin
      
    val splitInput = input.split("\n")
    
    val encodedWords = splitInput.head.split(" ")
    val letterPatternWords: Array[String] = encodedWords.map(word => dictionary.convertToLetterPattern(word.toLowerCase()))
    val encodedToPattern: Map[String, String] = encodedWords.zip(letterPatternWords).toMap
//    .map{ case (k,v) => (k -> v) }
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
    
    var substitutes: HashMap[Char, Char] = HashMap()
    splitInput.drop(2).foreach(pair => substitutes += (pair.charAt(0).toUpper -> pair.charAt(1)))
    
    println(substitutes)
      
  }
}