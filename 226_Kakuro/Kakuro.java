import java.util.*;

public class Kakuro {

    public static class Constraint {
        private int sum;
        private List<String> positions;
        private int remainingValues;

        public Constraint(int sum, List<String> positions) {
            this.sum = sum;
            this.positions = positions;
            this.remainingValues = positions.size();
        }

        public int getSum() {
            return sum;
        }

        public List<String> getPositions() {
            return positions;
        }

        public void reduceRemainingValue() {
            remainingValues--;
        }

        public int getRemainingValues() {
            return remainingValues;
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

        public void putConstraint(String position, Constraint constraint) {
            List<Constraint> constraints = multiMap.get(position);
            if (!multiMap.containsKey(position)) {
                //constraints = multiMap.get(position);
                constraints = new ArrayList<>();
            }
            constraints.add(constraint);

            multiMap.put(position, constraints);
        }

        public List<Constraint> getConstraints(String position) {
            return multiMap.get(position);
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

        String input = "2 3\n" +
                       "13 A1 A2 A3\n" +
                       "8 B1 B2 B3\n" +
                       "6 A1 B1\n" +
                       "6 A2 B2\n" +
                       "9 A3 B3\n";

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

    public static class ConstraintComparator implements Comparator<Constraint> {

        @Override
        public int compare(Constraint constraint1, Constraint constraint2) {
            if (constraint1.getRemainingValues() < constraint2.getRemainingValues()) {
                return -1;
            } else if (constraint2.getRemainingValues() < constraint1.getRemainingValues()) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    public static int[][] backtrackingSearch(List<Constraint> constraintList, ConstraintMultiMap constraintMap, int rows, int columns) {
        Queue<Constraint> minimumRemainingValues = new PriorityQueue<>(new ConstraintComparator());
        minimumRemainingValues.addAll(constraintList);
        Map<String, Integer> constraintValues = new HashMap<>();

        int totalPositions = rows * columns;

        System.out.println("Minimum remaining values: " + minimumRemainingValues);

        recursiveBacktrackingSearch(constraintValues, minimumRemainingValues, constraintMap, totalPositions);
        return null;
    }

    public static Map<String, Integer> recursiveBacktrackingSearch(Map<String, Integer> constraintValues, Queue<Constraint> minimumRemainingValues, ConstraintMultiMap constraintMap, int totalPositions) {
        if (constraintValues.size() == totalPositions) {
            return constraintValues;
        }
//        if (assignmentIsComplete(grid)) {
//            return grid;
//        }

        String position = getUnassignedPosition(minimumRemainingValues.poll(), constraintMap);

        Queue<Constraint> constraintQueue = new PriorityQueue<>(new ConstraintComparator());
        constraintQueue.addAll(constraintMap.getConstraints(position));

        Constraint constraint = constraintQueue.poll();
        constraint.reduceRemainingValue();

        if (constraint.getRemainingValues() == 1) {
            Map<String, Integer> newConstraintValues = satisfyConstraint(position, constraintValues, constraint);
            Queue<Constraint> newMinimumRemainingValues = new PriorityQueue<>(minimumRemainingValues);

            newMinimumRemainingValues.remove(constraint);
            newMinimumRemainingValues.add(constraint);

            if (constraintValuesAreValid(constraintValues, position, constraintMap)) {
                recursiveBacktrackingSearch(newConstraintValues, newMinimumRemainingValues, constraintMap, totalPositions);
            }
        }

//        Iterator<Constraint> constraintIterator = constraintQueue.iterator();
//        while(constraintIterator.hasNext()) {
//
//        }


//        for (int i = 1; i <= 9; i++) {
//
//            if ()
//        }

        System.out.println("UnassignedPosition: " + position);

        return null;
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
    public static String getUnassignedPosition(Constraint minimumRemainingConstraint, ConstraintMultiMap constraintMultiMap) {

        String positionWithHighestDegree = minimumRemainingConstraint.getPositions().get(0);
        if (minimumRemainingConstraint.getPositions().size() == 1) {
            return positionWithHighestDegree;
        }

        int highestDegree = constraintMultiMap.getDegree(minimumRemainingConstraint.getPositions().get(0));

        for (int i = 1; i < minimumRemainingConstraint.getPositions().size(); i++) {
            String position = minimumRemainingConstraint.getPositions().get(i);
            if (constraintMultiMap.getDegree(position) > highestDegree) {
                highestDegree = constraintMultiMap.getDegree(position);
            }

        }

        return positionWithHighestDegree;
    }

    public static Map<String, Integer> satisfyConstraint(String position, Map<String, Integer> constraintValues, Constraint constraint) {
        int currentSum = 0;

        for (String constraintPosition : constraint.getPositions()) {
            if (constraintValues.containsKey(constraintPosition)) {
                currentSum += constraintValues.get(constraintPosition);
            }
        }

        int difference = constraint.sum - currentSum;
        constraintValues.put(position, difference);

        return constraintValues;
    }

    public static boolean constraintValuesAreValid(Map<String, Integer> constraintValues, String position, ConstraintMultiMap constraintMultiMap) {
        List<Constraint> constraints = constraintMultiMap.getConstraints(position);

        for (Constraint constraint : constraints) {
            int calculatedSum = 0;

            for (String constraintPosition : constraint.getPositions()) {
                calculatedSum += constraintValues.get(constraintPosition);
            }

            if (calculatedSum != constraint.getSum()) {
                return false;
            }
        }

        return true;
    }

    public static boolean assignmentIsComplete(int[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 0) {
                    return false;
                }
            }
        }

        return true;
    }

}
