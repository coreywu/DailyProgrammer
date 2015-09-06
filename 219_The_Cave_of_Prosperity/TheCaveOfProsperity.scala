import scala.collection.immutable.HashSet
import scala.collection.immutable.SortedSet
import scala.collection.immutable.TreeSet

/**
 * Solution for The Cave of Properity problem which uses a dynamic programming
 * algorithm for the knapsack problem. The algorithm creates an array with n
 * columns and w rows, where n is the number of items (plus one) and w is the
 * total capacity of the knapsack (all weights are multiplied by 10^7 to become
 * integers rather than doubles).
 * 
 * Note: Running this will require a lot of heap space.
 */
object TheCaveOfProsperity {
  
  def knapsack(input: String): Unit = {
    val splitInput = input.split("\n")

    val capacity: Int = splitInput(0).replace(".", "").toInt
    val numberOfItems: Int = splitInput(1).toInt
    
    val goldStringArray: Array[String] = Array.fill(numberOfItems)("")
    val goldArray: Array[Int] = Array.fill(numberOfItems)(0)
    
    for (i <- 0 until numberOfItems) {
      goldStringArray(i) = splitInput(i + 2)
      goldArray(i) = splitInput(i + 2).replace(".", "").toInt
    }
    
    // Create valuesArray, the array we will use for calculating the maximum
    // weight we can carry using the first n items with a total capacity of w.
    // Each value is the max of 1: the knapsack with the same capacity and one
    // fewer item; and 2: the weight of the new item with the total weight of
    // a knapsack with one fewer item and a capacity of the remaining space in
    // the current knapsack. These previous values were calculated beforehand,
    // in higher rows of the array.
    val valuesArray: Array[Array[Int]] = Array.ofDim[Int](numberOfItems + 1, capacity + 1)
    
    // The keepArray is the array we use to determing if a certain item will
    // end up being used to add up to the final optimal weight.
    val keepArray: Array[Array[Boolean]] = Array.ofDim[Boolean](numberOfItems + 1, capacity + 1)
    
    for (i <- 1 until numberOfItems + 1) {
      for (w <- 1 until capacity + 1) {
    	  if (w < goldArray(i - 1)) {
    		  valuesArray(i)(w) = valuesArray(i - 1)(w)
    	  } else {
    		  if (w - goldArray(i - 1) >= 0) {
            if (goldArray(i - 1) + valuesArray(i - 1)(w - goldArray(i - 1)) > valuesArray(i - 1)(w)) {
            	valuesArray(i)(w) = goldArray(i - 1) + valuesArray(i - 1)(w - goldArray(i - 1))
              keepArray(i)(w) = true 
            } else {
            	valuesArray(i)(w) = valuesArray(i - 1)(w)
            }
    		  } else {
            if (goldArray(i - 1) > valuesArray(i - 1)(w)) {
            	valuesArray(i)(w) = goldArray(i - 1)
              keepArray(i)(w) = true
            } else {
            	valuesArray(i)(w) = valuesArray(i - 1)(w)
            }
    		  }
        }
      }
    }
    
    // Iterate through the keepArray to determine which items to keep
    var keepSet: Set[String] = Set()
    var currentW = capacity
    for (item <- numberOfItems until 0 by -1) {
      if (currentW >= 0 && keepArray(item)(currentW)) {
        keepSet += goldStringArray(item - 1)
        currentW -= goldArray(item - 1)
      }
    }
    
    val sum = valuesArray(numberOfItems)(capacity)
    println("%1.7f" format (sum.toDouble / 10000000))
    println(keepSet.mkString("\n"))
    println()
  }
  
  def main(args: Array[String]) {
    
    val input = """0.0000005
                  |3
                  |0.0000003
                  |0.0000002
                  |0.0000001""".stripMargin
    
    val input1 = """2.0000000
                   |5
                   |0.3958356
                   |0.4109163
                   |0.5924923
                   |0.6688261
                   |0.8720640""".stripMargin

    val input2 = """4.0000000
                   |10
                   |0.0359785
                   |0.9185395
                   |0.2461690
                   |0.7862738
                   |0.9237070
                   |0.2655587
                   |0.3373235
                   |0.8795087
                   |0.7802254
                   |0.8158674""".stripMargin
                   
    val input3 = """5.5000000
                   |15
                   |0.8650584
                   |0.2952432
                   |0.9213683
                   |0.0854114
                   |0.6849213
                   |0.3929483
                   |0.9295125
                   |0.8269517
                   |0.8900554
                   |0.2346400
                   |0.9676352
                   |0.1983828
                   |0.2847689
                   |0.2777273
                   |0.1182488""".stripMargin
               
                   
    val input4 = """11.0000000
                   |30
                   |0.4499129
                   |0.8949440
                   |0.1205519
                   |0.3417682
                   |0.0038582
                   |0.2044650
                   |0.8572595
                   |0.1766059
                   |0.6546017
                   |0.9038807
                   |0.5654560
                   |0.8803604
                   |0.6624511
                   |0.6366273
                   |0.0238172
                   |0.1820313
                   |0.1988662
                   |0.3852392
                   |0.1149663
                   |0.4820060
                   |0.3035506
                   |0.9905403
                   |0.0408851
                   |0.2536498
                   |0.2009946
                   |0.4845842
                   |0.1896687
                   |0.5589163
                   |0.6838930
                   |0.9383432""".stripMargin
                   
    val input5 = """13.0000000
                   |46
                   |0.5656655
                   |0.3382802
                   |0.3064386
                   |0.8037713
                   |0.8039497
                   |0.8110147
                   |0.5209788
                   |0.2034019
                   |0.9472229
                   |0.1274831
                   |0.0255210
                   |0.4313166
                   |0.8739507
                   |0.3060384
                   |0.9749309
                   |0.0090694
                   |0.9875971
                   |0.0072788
                   |0.8723527
                   |0.1881410
                   |0.4535392
                   |0.4286220
                   |0.9292595
                   |0.1822589
                   |0.4253469
                   |0.4101875
                   |0.1865457
                   |0.6602952
                   |0.7062095
                   |0.2989150
                   |0.6303584
                   |0.3088890
                   |0.3098897
                   |0.4077755
                   |0.1909044
                   |0.6333937
                   |0.1367541
                   |0.5840784
                   |0.6694657
                   |0.6399703
                   |0.8353940
                   |0.4807639
                   |0.4281496
                   |0.5507794
                   |0.3896394
                   |0.3547061""".stripMargin
                   
     knapsack(input) 
     knapsack(input1) 
     knapsack(input2) 
     knapsack(input3) 
     knapsack(input4) 
     knapsack(input5) 
  }
}