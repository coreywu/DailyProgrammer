import scala.collection.immutable.HashSet

object DivisibleBy7Graph {
 
	var resultSet: Seq[Long] = Seq(7, 70, 77, 161, 168, 252, 259, 343, 434, 525, 595, 616,
			686, 700, 707, 770, 777, 861, 868, 952, 959)
      
  var reversedSet: Set[Long] = HashSet()

  private val nextNodeArray: Array[Int] = Array(0, 3, 6, 2, 5, 1, 4)
  
  def isDivisibleBy7(number: Long): Boolean = {
    isDivisibleBy7(number.toString(), 0)
  }

  private def isDivisibleBy7(number: String, node: Int): Boolean = {
    val firstDigit = Integer.parseInt(number.head.toString(), 10)
    val newNumber = number.tail
    var newNode = (node + firstDigit) % 7
    if (newNumber.size == 0) {
      return newNode == 0
    } else {
      newNode = nextNodeArray(newNode)
    }
    isDivisibleBy7(newNumber, newNode)
  }
  
  def isReverseDivisibleBy7(number: Long): Boolean = {
    isReverseDivisibleBy7(number.toString(), 0)
  }

  private def isReverseDivisibleBy7(number: String, node: Int): Boolean = {
    val firstDigit = Integer.parseInt(number.charAt(number.length() - 1).toString(), 10)
    val newNumber = number.substring(0, number.length() - 1)
    var newNode = (node + firstDigit) % 7
    if (newNumber.size == 0) {
      return newNode == 0
    } else {
      newNode = nextNodeArray(newNode)
    }
    isReverseDivisibleBy7(newNumber, newNode)
  }
    
  def isReverseDivisibleBy7Efficient(number: Long): Boolean = {
    var currentNumber = number
    var node: Int = 0
    while (currentNumber >= 10) {
      val firstDigit = (currentNumber % 10).toInt
      currentNumber = currentNumber / 10
      node = (node + firstDigit) % 7
      node = nextNodeArray(node)
    }
    node = (node + currentNumber.toInt) % 7
    node == 0
  }
  
  
  def main(args: Array[String]) {
    println(isReverseDivisibleBy7Efficient(14))
    println(isReverseDivisibleBy7Efficient(41))
    println(isReverseDivisibleBy7Efficient(7777777))
    println(isReverseDivisibleBy7Efficient(1111))
    println(isReverseDivisibleBy7Efficient(14984001))
    println(isReverseDivisibleBy7Efficient(34048584342L))
    
    val start = System.nanoTime   

    var number = 1001L
    while (number <= Math.pow(10, 9)) {
      if (reversedSet.contains(number)) {
        resultSet +:= number
      } else {
        if (isReverseDivisibleBy7Efficient(number)) {
          resultSet +:= number
          val reverseNumber = number.toString().reverse.toLong
          reversedSet += reverseNumber
        }
      }
      number += 7
    }
    
    println((System.nanoTime - start) / 1000000000 + " secs")
    
    println("Result size: " + resultSet.size)
    println("Result sum: " + resultSet.sum)
    println("Result sum of digits: " + resultSet.sum.toString().map(_.asDigit).foldLeft(0)(_ + _))
  }
}