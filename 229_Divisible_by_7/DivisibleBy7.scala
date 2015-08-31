object DivisibleBy7 {
 
	var resultSet: Seq[Long] = Seq(7, 70, 77, 161, 168, 252, 259, 343, 434, 525, 595, 616,
			686, 700, 707, 770, 777, 861, 868, 952, 959)

	var divisibleBy7Set: Set[Long] = Set(-7, 0, 7)

  def isDivisibleBy7(long: Long): Boolean = {
    isDivisibleBy7(long, Seq[Long]())
  }

  private def isDivisibleBy7(long: Long, numbersUsed: Seq[Long]): Boolean = {
    val newNumbersUsed = numbersUsed.+:(long)
    if (divisibleBy7Set.contains(long)) {
      divisibleBy7Set = divisibleBy7Set ++ newNumbersUsed
      true
    } else {
      val lastDigit = long % 10
      val rest = long / 10
      isDivisibleBy7(rest - (lastDigit * 2), newNumbersUsed)
    }
  }
  
  def generateReversableNumbers(digits: Int): Seq[Long] = {
    val digitsToSelect = digits / 2
    var halfNumbers: Seq[String] = Seq()

    for (i <- 1 until 10) {
      halfNumbers = halfNumbers.+:(i.toString())
    }
    
    for (digit <- 1 until digitsToSelect) {
      var newHalfNumbers: Seq[String] = Seq()
      for {
        i <- 0 until 10 
        currentHalfDigit <- halfNumbers
      } {
        newHalfNumbers = newHalfNumbers.+:((currentHalfDigit + i))
      }
      halfNumbers = newHalfNumbers
    }
    var numbers: Seq[Long] = Seq()
    
    if (digits % 2 == 0) {
      numbers = numbers ++ halfNumbers.map(halfNumber => (halfNumber + halfNumber.reverse).toLong)
    } else {
      numbers = numbers ++ halfNumbers.flatMap(halfNumber => {
        var newNumbers: Seq[Long] = Seq()
        for (digit <- 0 until 10) {
        	newNumbers +:= (halfNumber + digit + halfNumber.reverse).toLong
        }
        newNumbers
      })
    }
    
    numbers
  }
  
  def main(args: Array[String]) {
    divisibleBy7Set = divisibleBy7Set ++ resultSet
    
    for (i <- 4 until 12) {
    	generateReversableNumbers(6).foreach { 
    		reversableNumber => {
    			if (isDivisibleBy7(reversableNumber)) resultSet +:= reversableNumber 
    		}
    	}
    }
    
    println("Result: " + resultSet.size)
    println("Result: " + resultSet.sum)
    println("Result: " + resultSet.take(10))
  }
}