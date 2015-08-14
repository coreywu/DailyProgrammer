import java.util.*;

public class Kakuro {

    /**
     * A data structure which holds the required sum for a set of positions.
     */
    public static class Constraint {
        private int sum;
        private List<String> positions;

        public Constraint(int sum, List<String> positions) {
            this.sum = sum;
            this.positions = positions;
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
                return this.sum == that.sum && this.positions.equals(that.positions);
            } else {
                return false;
            }
        }

        @Override
        public String toString() {
            return "sum: " + sum + " positions: " + positions;
        }
    }

    /**
     * A map of positions (A1, A2, D3, etc.) to all constraints which contain
     * that position.
     */
    public static class ConstraintMultiMap {
        private Map<String, List<Constraint>> multiMap;

        public ConstraintMultiMap() {
            multiMap = new HashMap<>();
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

        public int size() {
            return multiMap.size();
        }

        /**
         * Return the number of constraints which include a specified position.
         * This is used for the degree heuristic.
         * @param position  The specified position
         * @return The number of constraints which include the position
         */
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

        String input1 = "1 2\n"
                      + "3 A1 A2";

        String input2 = "2 3\n"
                      + "13 A1 A2 A3\n"
                      + "8 B1 B2 B3\n"
                      + "6 A1 B1\n"
                      + "6 A2 B2\n"
                      + "9 A3 B3\n";

        String input3 = "4 3\n"
                      + "3 C1 D1\n"
                      + "10 A2 B2 C2 D2\n"
                      + "3 A3 B3\n"
                      + "4 A2 A3\n"
                      + "3 B2 B3\n"
                      + "6 C1 C2\n"
                      + "3 D1 D2";

        String input4 = "8 8\n"
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

        solveKakuro(input1);
        solveKakuro(input2);
        solveKakuro(input3);
        solveKakuro(input4);

    }

    public static void solveKakuro(String input) {
        ConstraintMultiMap constraintMap = new ConstraintMultiMap();
        List<Constraint> constraintList = new ArrayList<>();

        String[] splitInput = input.split("\n");
        int columns = Integer.parseInt(splitInput[0].split(" ")[0]);
        int rows = Integer.parseInt(splitInput[0].split(" ")[1]);
        char[][] result;

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

        result = backtrackingSearch(constraintList, constraintMap, rows, columns);

        System.out.print("  ");
        for (int i = 0; i < columns; i++) {
            System.out.print((char)(i + 65) + " ");
        }
        System.out.println();

        for (int i = 0; i < rows; i++) {
            System.out.print((i + 1) + " ");
            for (int j = 0; j < columns; j++) {
                System.out.print(result[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * Set up the recursive backtracking search for selecting values that
     * fulfill a set of constraints.
     */
    public static char[][] backtrackingSearch(List<Constraint> constraintList,
                                              ConstraintMultiMap constraintMap,
                                              int rows,
                                              int columns) {
        Map<Constraint, Integer> remainingValuesMap = new HashMap<>();
        for (Constraint constraint : constraintList) {
            remainingValuesMap.put(constraint, constraint.getPositions().size());
        }

        Map<String, Integer> constraintValues = new HashMap<>();

        Map<String, Integer> finalValues = recursiveBacktrackingSearch(constraintValues, remainingValuesMap, constraintMap);

        char[][] grid = new char[rows][columns];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                String position = Character.toString((char)(j + 65)) + (i + 1);
                if (finalValues.containsKey(position)) {
                    grid[i][j] = Character.toChars(finalValues.get(position) + 48)[0];
                } else {
                    grid[i][j] = ' ';
                }
            }
        }

        return grid;
    }

    /**
     * Backtracking search which uses the minimum remaining values (MRV or most
     * constrained variable) and degree heuristics to prune the search tree.
     *
     * The search algorithm selects a position and if that position only allows
     * for a single value based on a constraint, it fills that position with
     * the value. The new value is then checked against all constraints that
     * involve it for validity. If the position has more than one currently
     * valid value, it recurses with the position filled with all values from
     * 1 to 9.
     *
     * @param constraintValues  A map of positions to values.
     * @param remainingValuesMap  A map of constraints to the number of
     *                            remaining values in that constraint.
     * @param constraintMap  A map of positions to a list of all constraints
     *                       that involved that position.
     * @return A map of all positions to valid values or null if no valid
     * combination can exist from the current values.
     */
    public static Map<String, Integer> recursiveBacktrackingSearch(Map<String, Integer> constraintValues,
                                                                   Map<Constraint, Integer> remainingValuesMap,
                                                                   ConstraintMultiMap constraintMap) {
        if (constraintValues.size() == constraintMap.size()) {
            return constraintValues;
        }
        Map<String, Integer> newConstraintValues;

        /* Use the MRV heuristic to determine which constraint to fulfill */
        Constraint mrvConstraint = getMinimumRemainingValuesConstraint(remainingValuesMap);

        /* Get the next grid position to fill by using the degree heuristic */
        String position = getUnassignedPosition(constraintValues, mrvConstraint, constraintMap);

        /*
         * If there is a single value to fulfill, determine if that value is
         * valid (conforms with other constraints) and recurse. If not, return.
         */
        if (remainingValuesMap.get(mrvConstraint) == 1) {
            newConstraintValues = satisfyConstraint(position, constraintValues, mrvConstraint);

            if (constraintValuesAreValid(newConstraintValues, position, constraintMap)) {
                /*
                 * Once a value is chosen for a position, update all constraints
                 * which use that value.
                 */
                Map<Constraint, Integer> newRemainingValuesMap = new HashMap<>(remainingValuesMap);
                for (Constraint constraintToUpdate : constraintMap.getConstraints(position)) {
                    newRemainingValuesMap.put(constraintToUpdate, remainingValuesMap.get(constraintToUpdate) - 1);
                }

                return recursiveBacktrackingSearch(newConstraintValues, newRemainingValuesMap, constraintMap);
            }
        } else {
            newConstraintValues = new HashMap<>(constraintValues);
            for (int i = 1; i <= 9; i++) {
                newConstraintValues.put(position, i);

                if (constraintValuesAreValid(newConstraintValues, position, constraintMap)) {
                    /*
                     * Once a value is chosen for a position, update all constraints
                     * which use that value.
                     */
                    Map<Constraint, Integer> newRemainingValuesMap = new HashMap<>(remainingValuesMap);
                    for (Constraint constraintToUpdate : constraintMap.getConstraints(position)) {
                        newRemainingValuesMap.put(constraintToUpdate, remainingValuesMap.get(constraintToUpdate) - 1);
                    }

                    Map<String, Integer> result = recursiveBacktrackingSearch(
                            newConstraintValues, newRemainingValuesMap, constraintMap);
                    if (result != null) {
                        return result;
                    }
                }
            }
        }

        return null;
    }

    /**
     * Returns the Constraint that has the least number of remaining values to
     * fill. Using the minimum remaining values (MRV) heuristic, we can prune
     * the search tree by fulfilling constraints that are nearing completion.
     * These constraints have fewer legal values compared to other
     * possibilities.
     *
     * @param remainingValuesMap  A map of constraints to the number of
     *                            remaining values in that constraint
     * @return the constraint with the minimum remaining values.
     */
    public static Constraint getMinimumRemainingValuesConstraint(Map<Constraint, Integer> remainingValuesMap) {
        int currentMRV = Integer.MAX_VALUE;
        Constraint currentConstraint = null;

        for (Constraint constraint : remainingValuesMap.keySet()) {
            int constraintRemainingValues = remainingValuesMap.get(constraint);
            if (constraintRemainingValues > 0 && constraintRemainingValues < currentMRV) {
                currentMRV = constraintRemainingValues;
                currentConstraint = constraint;
            }
        }

        return currentConstraint;
    }

    /**
     * Returns the position with the highest degree (used in the most
     * number of constraints) from the constraint given. This uses the degree
     * heuristic to reduce the branching factor on future choices. The
     * position that is involved with the greatest number of constraints causes
     * failures to occur earlier.
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
    public static String getUnassignedPosition(Map<String, Integer> constraintValues,
                                               Constraint minimumRemainingConstraint,
                                               ConstraintMultiMap constraintMultiMap) {
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

    /**
     * If a single value is left to be selected for a constraint, we can
     * determine the last value by finding the difference between the sum of
     * current values and desired sum. This value may be greater than 9. This
     * is an invalid value which will be caught in the call to
     * {@link #constraintValuesAreValid(Map, String, ConstraintMultiMap)}.
     *
     * @param position  The unfilled position to select a value for
     * @param constraintValues  A map of currently selected positions with
     *                          their corresponding values
     * @param constraint  The constraint that has a single value left to select
     * @return an updated constraint map with the newly selected position to
     * value mapping
     */
    public static Map<String, Integer> satisfyConstraint(String position,
                                                         Map<String, Integer> constraintValues,
                                                         Constraint constraint) {
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

    /**
     * Returns whether the selection values are valid according to the
     * constraints provided. Only constraints that involve the given position
     * are checked, because no other constraints could have changed.
     *
     * The values are invalid if:
     *   * Any value is greater than 9
     *   * The calculated sum is greater than the constraint sum
     *   * The calculated sum is equal to the constraint sum and there are more
     *     values to be selected in that constraint
     *   * The calculated sum is not equal to the constraint sum and there are
     *     no more remaining values to select
     *
     * Otherwise, the constraints are valid (values have not yet been guaranteed
     * to be correct, but they have not been disqualified either).
     *
     * @param constraintValues  A map of positions to values
     * @param position  The most recently changed position
     * @param constraintMultiMap  A map from position to all constraints that
     *                            involve that position
     * @return a boolean for whether the selected value for the new position
     * is in accordance to all constraints that use it
     */
    public static boolean constraintValuesAreValid(Map<String, Integer> constraintValues,
                                                   String position,
                                                   ConstraintMultiMap constraintMultiMap) {
        List<Constraint> constraints = constraintMultiMap.getConstraints(position);

        for (Constraint constraint : constraints) {
            int calculatedSum = 0;
            boolean missingConstraints = false;

            for (String constraintPosition : constraint.getPositions()) {
                if (constraintValues.containsKey(constraintPosition)) {
                    if (constraintValues.get(constraintPosition) > 9) {
                        return false;
                    }
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
