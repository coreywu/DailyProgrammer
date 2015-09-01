import scala.collection.immutable.HashSet

/**
 * Solution for Divisible by 7 challenge. Calculates integers that are 
 * divisible by 7 and also divisible by 7 when the digits are reversed. 
 * Solution checks all multiples of 7 and adds them if their reverse is also
 * divisible by 7.
 * 
 * Also adds all multiples of 7 used in the divisible by 7 test to a HashSet
 * to keep track of valid multiples of 7.
 */
object DivisibleBy7 {
 
	var resultSet: Seq[Long] = Seq(7, 70, 77, 161, 168, 252, 259, 343, 434, 525, 595, 616,
			686, 700, 707, 770, 777, 861, 868, 952, 959)
      
  var reversedSet: Set[Long] = HashSet()

	var divisibleBy7Set: Set[Long] = Set(-7, 0, 7)

  /**
   * Test for whether the number is divisible by 7 from 
   * https://www.math.hmc.edu/funfacts/ffiles/10005.5.shtml (test #2).
   * Algorithm removes the last digit of the number and subtracts the rest of 
   * the number by the last digit multiplied by 2. If the result is divisible
   * by 7, the original number is also divisible by 7.
   */
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