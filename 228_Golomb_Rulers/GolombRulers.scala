import scala.collection.immutable.HashSet
import scala.collection.immutable.SortedSet
import scala.collection.immutable.TreeSet

/**
 * Find optimal Golomb Rulers by iterating through each ruler length and 
 * selecting marking positions using a recursive backtracking algorithm. The
 * algorithm keeps track of which lengths can currently be measured using the
 * current markings. We then check if any lengths created by adding a new 
 * marking cause duplicates in the previous set. If true, we backtrack; if 
 * false, we recurse down.
 */
object GolombRulers {
  
  /**
   * Find and print out the Golomb rulers for a given order. Iterates through
   * all possible ruler lengths until a valid Golomb ruler is generated.
   * 
   * @param order the order that we wish to generate a Golomb ruler for (order
   *              is the number of markings in a Golomb ruler)
   */
  def findGolombRulersForOrder(order: Int):Unit= {
    var result: Set[SortedSet[Int]] = Set()
    var golombRulers: Set[SortedSet[Int]] = Set()

    var rulerLength = -1 
    while(golombRulers.isEmpty) {
    	var markings: SortedSet[Int] = TreeSet(0, rulerLength)
    	var lengths: Set[Int] = HashSet()

      rulerLength += 1
    	if (order == rulerLength) {
    		lengths += order
    	}
      golombRulers = recursiveGolombRulerSearchOnLength(order, rulerLength, markings, lengths)
      result = removeReverseDuplicates(golombRulers, rulerLength)
      
    }

    var stringBuilder: StringBuilder = new StringBuilder()
    for (markings <- result) {
      stringBuilder.append("\n  ")
      for (marking <- markings) {
    	  stringBuilder.append(marking)
    	  stringBuilder.append(" ")
      }
    }
    println(order + "   " + rulerLength + stringBuilder.toString() + "\n")
  }
  
  /**
   * Recursively search for valid markings on a specific ruler length. Keeps a 
   * set of lengths to reference. For each new potential marking, we find all
   * the lengths that adding this marking would create. If any of these lengths
   * already exist in the set, the marking is invalid. When no more new 
   * positions are valid for a given state, we backtrack.
   * 
   * @param order the order of the Golomb ruler we wish to find
   * @param rulerLength the length of the Golomb ruler that we are selecting
   *                    markings for.
   * @param markings the markings that we have set for this partial Golomb
   *                 ruler state
   * @param lengths the set of lengths that we currently can measure using the
   *                markings that currently exist
   * @return a set of Golomb rulers that can be created given our current set
   *         of markings; can be empty
   */
  def recursiveGolombRulerSearchOnLength(order: Int, rulerLength: Int, 
      markings: SortedSet[Int], lengths: Set[Int]): Set[SortedSet[Int]] = {
    // If we have enough markings to satisfy the order, we have an answer
    if (markings.size == order) {
      return HashSet(markings)
    }

    var results: Set[SortedSet[Int]] = HashSet()
    
    // For each new possible position for a marking, we determine if it is 
    // valid (no new lengths can be measured using our existing markings)
    for (position <- 1 until rulerLength) {
    	var newMarkings: SortedSet[Int] = TreeSet()(Ordering.Int) ++ markings
      if (!markings.contains(position)) {
    	  val possibleNewLengths: Option[Set[Int]] = getNewLengths(order, markings, lengths, position)
    	  if (possibleNewLengths.isDefined) {
          newMarkings += position
    		  var newLengths: Set[Int] = new HashSet() ++ lengths ++ possibleNewLengths.get
          
          val result = recursiveGolombRulerSearchOnLength(order, rulerLength, newMarkings, newLengths)
          results ++= result
    		}
      }
    }
    
    results
  }
  
  /**
   * Return the lengths that can be measured with the addition of a marking at
   * the new position, or None if a new length can already be measured using
   * prior markings (which invalidates this position).
   * 
   * @param order the order of our Golomb rulers we wish to generate
   * @param markings the current markings in our partial Golomb ruler
   * @param lengths the set of lengths that can currently be measured using our
   *        existing markings
   * @param position the candidate position for a new marking
   * @return an Option containing the set of new lengths or None if the 
   *         position is invalid
   */
  def getNewLengths(order: Int, markings: Set[Int], lengths: Set[Int], position: Int): Option[Set[Int]] = {
    var newLengths: Set[Int] = new HashSet() ++ lengths
    for (marking <- markings) {
      val length = math.abs(marking - position)
      if (newLengths.contains(length)) {
        return None
      } else {
        	newLengths += length
      }
    }
    Some(newLengths)
  }
  
  /**
   * Remove reflected rulers (eg. for an order of 5, [0, 1, 4, 9, 11] and 
   * [0, 2, 7, 10, 11] are the same rulers reflected. We also sort our results
   * so that we have Golomb rulers with the next mark at the samller of its two
   * possible values (so [0, 1, 4, 9, 11] is returned before instead of 
   * [0, 2, 7, 10, 11]).
   * 
   * @param golombRulers the valid Golomb rulers we have generated. There are
   *                     reflected duplicates among this set.
   * @param rulerLength the length of our Golomb rulers
   * @return the set of Golomb rulers with duplicates removed
   */
  def removeReverseDuplicates(golombRulers: Set[SortedSet[Int]], rulerLength: Int): Set[SortedSet[Int]] = {
    val ord: Ordering[SortedSet[Int]] = new Ordering[SortedSet[Int]] { 
	    def compare(x: SortedSet[Int], y: SortedSet[Int]): Int = {
       val set1 = x.toArray
       val set2 = y.toArray
       for (i <- 0 until x.size) {
         if (set1(1) < set2(i)) return -1
         else if (set1(i) > set2(i)) return 1
       }
       0
      }
    }
    val sortedGolombRulers: SortedSet[SortedSet[Int]] = TreeSet()(ord) ++ golombRulers

    var uniqueGolombRulers: Set[SortedSet[Int]] = HashSet()
    for (markings <- sortedGolombRulers) {
      if (uniqueGolombRulers.size == 0) {
        uniqueGolombRulers += markings
      } else {
    	  var duplicate = false 
        for (uniqueMarkings <- uniqueGolombRulers) {
          if (markings.size == uniqueMarkings.size) {
            var currentDuplicate = true
            for (marking <- markings) {
              if (!uniqueMarkings.contains(rulerLength - marking)) {
                currentDuplicate = false
              }
            }
            if (currentDuplicate) {
              duplicate = true
            }
          }
        }
    	  if (!duplicate) {
    		  uniqueGolombRulers += markings
    	  }
      }
    }
    uniqueGolombRulers
  }
  
  def main(args: Array[String]) {
    findGolombRulersForOrder(3)
    findGolombRulersForOrder(5)
    findGolombRulersForOrder(8)
    findGolombRulersForOrder(7)
    findGolombRulersForOrder(10)
    findGolombRulersForOrder(20)
    findGolombRulersForOrder(26)
  }
}