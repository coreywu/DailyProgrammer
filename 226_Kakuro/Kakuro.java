import java.util.*;

public class Kakuro {

    public static class Constraint {
        private int sum;
        private List<String> positions;
        private int remainingValues;

        public Constraint(int sum, List<String> positions) {
            this.sum = sum;
            this.positions = positions;
        }

        public Constraint deepCopy() {
            Constraint copy = new Constraint(this.sum, this.positions);
            copy.remainingValues = this.remainingValues;
            return copy;
        }

        public int getSum() {
            return sum;
        }

        public List<String> getPositions() {
            return positions;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Constraint) {
                Constraint that = (Constraint) obj;
                return this.sum == that.sum && this.positions.equals(that.positions) && this.remainingValues == that.remainingValues;
            } else {
                return false;
            }
        }

        @Override
        public String toString() {
            return "sum: " + sum + " positions: " + positions;
        }
    }

    public static class ConstraintMultiMap {
        private Map<String, List<Constraint>> multiMap;

        public ConstraintMultiMap() {
            multiMap = new HashMap<>();
        }

        public ConstraintMultiMap(ConstraintMultiMap constraintMultiMap) {
            this.multiMap = constraintMultiMap.multiMap;
        }

        public void putConstraint(String position, Constraint constraint) {
            List<Constraint> constraints = multiMap.get(position);
            if (!multiMap.containsKey(position)) {
                constraints = new ArrayList<>();
            }
            constraints.add(constraint);

            multiMap.put(position, constraints);
        }

        public List<Constraint> getConstraints(String position) {
            return multiMap.get(position);
        }

        public void updateConstraint(String position, Constraint constraint) {
            List<Constraint> newConstraintList = new ArrayList<>();
            Iterator<Constraint> iterator = multiMap.get(position).iterator();
            while(iterator.hasNext()) {
                Constraint oldConstraint = iterator.next();
                if (oldConstraint.sum == constraint.sum && oldConstraint.positions == constraint.positions) {
                    newConstraintList.add(constraint);
                } else {
                    newConstraintList.add(oldConstraint);
                }
            }

            this.multiMap.put(position, newConstraintList);
        }

        public int size() {
            return multiMap.size();
        }

        public int getDegree(String position) {
            if (multiMap.containsKey(position)) {
                return multiMap.get(position).size();
            } else {
                return 0;
            }
        }

        @Override
        public String toString() {
            return multiMap.toString();
        }
    }

    public static void main(String[] args) {

//        String input = "1 2\n" +
//                       "3 A1 A2";

//        String input = "2 3\n" +
//                       "13 A1 A2 A3\n" +
//                       "8 B1 B2 B3\n" +
//                       "6 A1 B1\n" +
//                       "6 A2 B2\n" +
//                       "9 A3 B3\n";

//        String input = "4 3\n" +
//                       "3 C1 D1\n" +
//                       "10 A2 B2 C2 D2\n" +
//                       "3 A3 B3\n" +
//                       "4 A2 A3\n" +
//                       "3 B2 B3\n" +
//                       "6 C1 C2\n" +
//                       "3 D1 D2";

        String input = "8 8\n"
                     + "21 A1 A2 A3 A4\n"
                     + "3 A7 A8\n"
                     + "11 B1 B2 B3 B4\n"
                     + "6 B6 B7 B8\n"
                     + "3 C2 C3\n"
                     + "4 C5 C6\n"
                     + "4 D1 D2\n"
                     + "15 D4 D5 D6 D7 D8\n"
                     + "15 E1 E2 E3 E4 E5\n"
                     + "4 E7 E8\n"
                     + "3 F3 F4\n"
                     + "4 F6 F7\n"
                     + "7 G1 G2 G3\n"
                     + "15 G5 G6 G7 G8\n"
                     + "4 H1 H2\n"
                     + "21 H5 H6 H7 H8\n"
                     + "3 A1 B1\n"
                     + "8 D1 E1\n"
                     + "4 G1 H1\n"
                     + "16 A2 B2 C2 D2 E2\n"
                     + "3 G2 H2\n"
                     + "7 A3 B3 C3\n"
                     + "7 E3 F3 G3\n"
                     + "14 A4 B4\n"
                     + "6 D4 E4 F4\n"
                     + "7 C5 D5 E5\n"
                     + "17 G5 H5\n"
                     + "6 B6 C6 D6\n"
                     + "6 F6 G6 H6\n"
                     + "4 A7 B7\n"
                     + "21 D7 E7 F7 G7 H7\n"
                     + "3 A8 B8\n"
                     + "4 D8 E8\n"
                     + "4 G8 H8"
                     ;

        ConstraintMultiMap constraintMap = new ConstraintMultiMap();
        List<Constraint> constraintList = new ArrayList<>();

        String[] splitInput = input.split("\n");
        int columns = Integer.parseInt(splitInput[0].split(" ")[0]);
        int rows = Integer.parseInt(splitInput[0].split(" ")[1]);
        int[][] result = new int[rows][columns];

        for (int i = 1; i < splitInput.length; i++) {
            String[] constraintInput = splitInput[i].split(" ");

            List<String> positions = new ArrayList<>();
            for (int j = 1; j < constraintInput.length; j++) {
                positions.add(constraintInput[j]);
            }

            Constraint constraint = new Constraint(Integer.parseInt(constraintInput[0]), positions);
            constraintList.add(constraint);

            for (String position : positions) {
                constraintMap.putConstraint(position, constraint);
            }
        }

        System.out.println(constraintMap);

        backtrackingSearch(constraintList, constraintMap, rows, columns);

    }

//    public static class ConstraintComparator implements Comparator<Constraint> {
//
//        @Override
//        public int compare(Constraint constraint1, Constraint constraint2) {
//            if (constraint1.getRemainingValues() < constraint2.getRemainingValues()) {
//                return -1;
//            } else if (constraint2.getRemainingValues() < constraint1.getRemainingValues()) {
//                return 1;
//            } else {
//                return 0;
//            }
//        }
//    }

    public static int[][] backtrackingSearch(List<Constraint> constraintList, ConstraintMultiMap constraintMap, int rows, int columns) {
        Map<Constraint, Integer> remainingValuesMap = new HashMap<>();
        for (Constraint constraint : constraintList) {
            remainingValuesMap.put(constraint, constraint.getPositions().size());
        }

        Map<String, Integer> constraintValues = new HashMap<>();
        int totalPositions = rows * columns;

        Map<String, Integer> answer = recursiveBacktrackingSearch(constraintValues, remainingValuesMap, constraintMap, totalPositions);
        System.out.println("Answer: " + answer);

        return null;
    }

    public static Map<String, Integer> recursiveBacktrackingSearch(Map<String, Integer> constraintValues,
                                                                   Map<Constraint, Integer> remainingValuesMap,
                                                                   ConstraintMultiMap constraintMap,
                                                                   int totalPositions) {
        System.out.println("ConstraintValuesSize: " + constraintValues.size() + " " + constraintMap.size() + " " + constraintValues);

//        if (constraintValues.size() == totalPositions) {
//            return constraintValues;
//        }

        if (constraintValues.size() == constraintMap.size()) {
            return constraintValues;
        }

//        if (assignmentIsComplete(grid)) {
//            return grid;
//        }

        /* Use the MRV heuristic to determine which constraint to fulfill */
        Constraint mrvConstraint = getMinimumRemainingValuesConstraint(remainingValuesMap);

        System.out.println("MRV CONSTRAINT: " + mrvConstraint + " " + remainingValuesMap.get(mrvConstraint) + " " + constraintValues);

        /* Get the next grid position to fill by using the degree heuristic */
        String position = getUnassignedPosition(constraintValues, mrvConstraint, constraintMap);

        System.out.println("POSITION: " + position);

//        /* Create a priority queue to sort the constraints by lowest to highest remaining values */
//        Queue<Constraint> constraintQueue = new PriorityQueue<>(new ConstraintComparator());
//        constraintQueue.addAll(constraintMap.getConstraints(position));

        Map<String, Integer> newConstraintValues = null;
        ConstraintMultiMap newConstraintMap = new ConstraintMultiMap(constraintMap);

        /* If there is a single value to fulfill, determine if that value is
         * valid (conforms with other constraints) and recurse. If not, return.
         */
        if (remainingValuesMap.get(mrvConstraint) == 1) {
            newConstraintValues = satisfyConstraint(position, constraintValues, mrvConstraint);

//            System.out.println("1 remaining, new constraint values: " + newConstraintValues);

            if (constraintValuesAreValid(newConstraintValues, position, constraintMap)) {

                /* Once a value is chosen for a position, update all constraints
                 * which use that value.
                 */
                Map<Constraint, Integer> newRemainingValuesMap = new HashMap<>(remainingValuesMap);
                for (Constraint constraintToUpdate : constraintMap.getConstraints(position)) {
                    newRemainingValuesMap.put(constraintToUpdate, remainingValuesMap.get(constraintToUpdate) - 1);
                }

                System.out.println("NEW CONSTRAINT MAP: " + newConstraintMap);

//                newMinimumRemainingValues = new PriorityQueue<>(minimumRemainingValues);
//                for (Constraint constraintToUpdate : constraintMap.getConstraints(position)) {
//                    newMinimumRemainingValues.remove(constraintToUpdate);
//                }

                return recursiveBacktrackingSearch(newConstraintValues, newRemainingValuesMap, constraintMap, totalPositions);
            }
        } else {
            newConstraintValues = new HashMap<>(constraintValues);
            for (int i = 1; i <= 9; i++) {
                newConstraintValues.put(position, i);

//                System.out.println("VALID: " + i + " " + constraintValuesAreValid(newConstraintValues, position, constraintMap));

                if (constraintValuesAreValid(newConstraintValues, position, constraintMap)) {
                    /* Once a value is chosen for a position, update all constraints
                    * which use that value.
                    */
                    Map<Constraint, Integer> newRemainingValuesMap = new HashMap<>(remainingValuesMap);
                    for (Constraint constraintToUpdate : constraintMap.getConstraints(position)) {
                        newRemainingValuesMap.put(constraintToUpdate, remainingValuesMap.get(constraintToUpdate) - 1);
                    }
                    System.out.println("NEW CONSTRAINT MAP 2+: " + newConstraintMap);

                    Map<String, Integer> result = recursiveBacktrackingSearch(newConstraintValues, newRemainingValuesMap, constraintMap, totalPositions);
                    if (result != null) {
                        return result;
                    }
                }
            }
        }

        return null;
    }

    public static Constraint getMinimumRemainingValuesConstraint(Map<Constraint, Integer> remainingValuesMap) {
        int currentMRV = Integer.MAX_VALUE;
        Constraint currentConstraint = null;

        for (Constraint constraint : remainingValuesMap.keySet()) {
//            System.out.println("Constraint: " + constraint + " " + remainingValuesMap.get(constraint));
            int constraintRemainingValues = remainingValuesMap.get(constraint);
            if (constraintRemainingValues > 0 && constraintRemainingValues < currentMRV) {
                currentMRV = constraintRemainingValues;
                currentConstraint = constraint;
            }
        }

        return currentConstraint;
    }

    /**
     * Returns a position by first selecting a constraint using the minimum
     * remaining values (MRV) heuristic, then selecting the position using
     * the degree heuristic.
     *
     * Using these two heuristics prunes the search tree. The MRV heuristic
     * selects a variable that is likely to fail early if it is invalid. The
     * degree heuristic attempts to reduce the branching factor on future
     * choices by selecting the position that is involved with the greatest
     * number of constraints.
     *
     * @param minimumRemainingConstraint the constraint that satisfies the MRV
     *                                   heuristic
     * @return the position that is part of the constraint with the minimum
     *         remaining value and has the highest degree of all positions
     *         within that constraint
     */
    public static String getUnassignedPosition(Map<String, Integer> constraintValues, Constraint minimumRemainingConstraint, ConstraintMultiMap constraintMultiMap) {
        String unassignedPositionWithHighestDegree = null;
        int highestDegree = 0;

        for (int i = 0; i < minimumRemainingConstraint.getPositions().size(); i++) {
            String position = minimumRemainingConstraint.getPositions().get(i);
            if (!constraintValues.containsKey(position)) {
                if (constraintMultiMap.getDegree(position) > highestDegree) {
                    highestDegree = constraintMultiMap.getDegree(position);
                    unassignedPositionWithHighestDegree = position;
                }
            }

        }

        return unassignedPositionWithHighestDegree;
    }

    public static Map<String, Integer> satisfyConstraint(String position, Map<String, Integer> constraintValues, Constraint constraint) {
        Map<String, Integer> newConstraintValues = new HashMap<>(constraintValues);
        int currentSum = 0;

        for (String constraintPosition : constraint.getPositions()) {
            if (newConstraintValues.containsKey(constraintPosition)) {
                currentSum += newConstraintValues.get(constraintPosition);
            }
        }

        int difference = constraint.sum - currentSum;
        newConstraintValues.put(position, difference);

        return newConstraintValues;
    }

    public static boolean constraintValuesAreValid(Map<String, Integer> constraintValues, String position, ConstraintMultiMap constraintMultiMap) {
        List<Constraint> constraints = constraintMultiMap.getConstraints(position);

//        System.out.println("VALID constraints check: " + constraints + " " + constraintValues);

        for (Constraint constraint : constraints) {
            int calculatedSum = 0;
            boolean missingConstraints = false;

            for (String constraintPosition : constraint.getPositions()) {
//                System.out.println("Constraint position: " + constraintPosition + " contains key? " + constraintValues.containsKey(constraintPosition) + " null? " + (constraintValues.get(constraintPosition) == null));
                if (constraintValues.containsKey(constraintPosition)) {
                    calculatedSum += constraintValues.get(constraintPosition);
                } else {
                    missingConstraints = true;
                }
            }

            if (calculatedSum > constraint.getSum()) {
                return false;
            } else if (calculatedSum == constraint.getSum() && missingConstraints) {
                return false;
            } else if (!missingConstraints && calculatedSum != constraint.getSum()) {
                return false;
            }
        }

        return true;
    }
}
