import scala.io.Source
import scala.collection.immutable.HashMap
import scala.collection.immutable.Set

object SubstitutionCryptanalysis {
  
  class Dictionary() {
    var dictionary: HashMap[String, Set[String]] = HashMap()
    
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
        dictionary = dictionary + ((key, dictionary(key) + (value)))
      } else {
        dictionary = dictionary + ((key, Set(value)))
      }
    }

    
  }

  def main(args: Array[String]) {
    val filename = "src/resources/words.txt"
    val dictionary = new Dictionary()
      
    for (word <- Source.fromFile(filename).getLines()) {
        dictionary.addWordLetterPattern(word)
    }
    
    println(dictionary.dictionary)
    
    println(dictionary.convertToLetterPattern("aadvark"))
      
    val input = """IAL FTNHPL PDDI DR RDNP WF IUD
                  |2
                  |aH
                  |oD""".stripMargin
      
    val splitInput = input.split("\n")

    splitInput.foreach(println)
      
  }
}