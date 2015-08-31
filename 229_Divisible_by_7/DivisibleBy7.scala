import scala.collection.immutable.HashSet

object DivisibleBy7 {
 
	var resultSet: Seq[Long] = Seq(7, 70, 77, 161, 168, 252, 259, 343, 434, 525, 595, 616,
			686, 700, 707, 770, 777, 861, 868, 952, 959)
      
  var reversedSet: Set[Long] = HashSet()

	var divisibleBy7Set: Set[Long] = Set(-7, 0, 7)

  def isDivisibleBy7(long: Long): Boolean = {
    isDivisibleBy7(long, Seq[Long]())
  }

  private def isDivisibleBy7(long: Long, numbersUsed: Seq[Long]): Boolean = {
    val newNumbersUsed = numbersUsed.+:(long)
    if (divisibleBy7Set.contains(long)) {
      divisibleBy7Set = divisibleBy7Set ++ newNumbersUsed
      true
    } else if (long < 10 && long > -10) {
      false
    } else {
      val lastDigit = long % 10
      val rest = long / 10
      isDivisibleBy7(rest - (lastDigit * 2), newNumbersUsed)
    }
  }
  
  def main(args: Array[String]) {
    divisibleBy7Set = divisibleBy7Set ++ resultSet
    
    var number = 1001L
    while (number < Math.pow(10, 9) + 1) {
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
    
    println("Result: " + resultSet.size)
    println("Result: " + resultSet.sum)
    println("Result: " + resultSet.take(10))
  }
}