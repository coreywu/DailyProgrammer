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
  
  def main(args: Array[String]) {
    
    var number = 1001L
    while (number <= Math.pow(10, 9)) {
      if (reversedSet.contains(number)) {
        resultSet +:= number
      } else {
    	  val reverseNumber = number.toString().reverse.toLong
    		if (isDivisibleBy7(reverseNumber)) {
    		  resultSet +:= number
    		  reversedSet += reverseNumber
    		}
      }
      number += 7
    }
    
    println("Result size: " + resultSet.size)
    println("Result sum: " + resultSet.sum)
    println("Result sum of digits: " + resultSet.sum.toString().map(_.asDigit).foldLeft(0)(_ + _))
  }
}