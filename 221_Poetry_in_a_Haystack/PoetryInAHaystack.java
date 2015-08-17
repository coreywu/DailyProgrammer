import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class PoetryInAHaystack {

    /**
     * BigramBuilder provides methods for generating and displaying a bigram
     * frequency table. The bigram frequency table is represented as an
     * adjacency matrix where the indices represent each letter in a pair.
     */
    public static class BigramBuilder {

        /**
         * Returns the bigram frequency table generated from a String input.
         *
         * @param input  The string to parse to find letter pair frequencies
         * @return  The bigram frequency table in the form of an adjacency
         * matrix
         */
        public static double[][] buildBigramFromString(String input) {
            /*
             * bigramMatrix[_1][_2] is the percentage of times that the second
             * letter follows the first. It is the probability of the second
             * letter occurring given the first letter has occurred.
             */
            double[][] bigramMatrix = new double[26][26];

            /*
             * A count of the occurences of each letter. Used to calculate the
             * bigramMatrix after all 2 letter combinations have been examined.
             */
            int[] letterCounts = new int[26];

            addToBigramMatrix(input, bigramMatrix, letterCounts);

            for (int i = 0; i < bigramMatrix.length; i++) {
                if (letterCounts[i] > 0) {
                    for (int j = 0; j < bigramMatrix[0].length; j++) {
                        bigramMatrix[i][j] = bigramMatrix[i][j] / letterCounts[i];
                    }
                } else {
                    for (int j = 0; j < bigramMatrix[0].length; j++) {
                        bigramMatrix[i][j] = 0;
                    }
                }
            }

            return bigramMatrix;
        }

        /**
         * Returns the bigram frequency table generated from a text file.
         *
         * @param filename  The name of the file to parse
         * @return  The bigram frequency table in the form of an adjacency
         * matrix
         */
        public static double[][] buildBigramFromFile(String filename) throws IOException {
            double[][] bigramMatrix = new double[26][26];
            int[] letterCounts = new int[26];

            BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));

            try {
                String line = bufferedReader.readLine();

                while (line !=null) {
                    addToBigramMatrix(line, bigramMatrix, letterCounts);
                    line = bufferedReader.readLine();
                }

            } finally {
                bufferedReader.close();
            }

            /*
             * Find the frequency of each bigram by diving the number of pair
             * occurrences by the number of occurrences of the first letter.
             * For example, we divide the occurrences of 'ab' by the
             * occurrences of 'a' by itself. This gives us the probability that
             * given the letter 'a', we end up with 'b' as the next letter in
             * the pair.
             */
            for (int i = 0; i < bigramMatrix.length; i++) {
                if (letterCounts[i] > 0) {
                    for (int j = 0; j < bigramMatrix[0].length; j++) {
                        bigramMatrix[i][j] = bigramMatrix[i][j] / letterCounts[i];
                    }
                } else {
                    for (int j = 0; j < bigramMatrix[0].length; j++) {
                        bigramMatrix[i][j] = 0;
                    }
                }
            }

            return bigramMatrix;
        }

        /**
         * Count and store the occurrences of individual letters in
         * {@code letterCounts} and pairs of letters in {@code bigramMatrix}.
         *
         * @param line  A string to parse for letters and pairs of letters
         * @param bigramMatrix  The bigram adjacency matrix to update with new
         *                      letter pairs
         * @param letterCounts  The counts of occurrences of each letter
         */
        public static void addToBigramMatrix(String line, double[][] bigramMatrix, int[] letterCounts) {
            String[] splitWords = line.replaceAll("[^a-zA-Z ]", "").toLowerCase().split(" ");

            /*
             * Count and store occurrences of letters in 'letterCounts' and
             * and pairs in 'bigramMatrix'.
             */
            for (String word : splitWords) {
                if (word.length() == 0) {
                    continue;
                }
                int firstIndex = word.charAt(0) - 97;
                letterCounts[firstIndex]++;
                for (int i = 1; i < word.length(); i++) {
                    int secondIndex = word.charAt(i) - 97;
                    letterCounts[secondIndex]++;
                    bigramMatrix[firstIndex][secondIndex]++;
                    firstIndex = secondIndex;
                }
            }
        }

        public static void printBigramMatrix(double[][] bigramMatrix) {
            for (int i = 0; i < bigramMatrix.length; i++) {
                for (int j = 0; j < bigramMatrix[0].length; j++) {
                    System.out.print(bigramMatrix[i][j] + " ");
                }
                System.out.println();
            }
        }

        public static void printSortedBigramPairs(double[][] bigramMatrix) {
        }

        /**
         * Find the similarity score between two matrices. This similarity
         * score is calculated as the total absolute difference between the
         * corresponding values in each matrix. A lower score indicates more
         * similarities in values.
         *
         * @param matrix1  The first matrix to compare
         * @param matrix2  The second matrix to compare
         * @return  the similarity score; the total of all absolute differences
         * in all corresponding values in each matrix
         */
        public static double compareMatrices(double[][] matrix1, double[][] matrix2) {
            double totalDifference = 0;

            for (int i = 0; i < matrix1.length; i++) {
                for (int j = 0; j < matrix1[0].length; j++) {
                    totalDifference += Math.abs(matrix1[i][j] - matrix2[i][j]);
                }
            }

            return totalDifference;
        }

        /**
         * Find the similarity score between two matrices, with one addition:
         * if the first matrix contains a value that is zero and the second
         * matrix's corresponding value is not also zero, we return
         * {@code Double.MAX_VALUE} (the two matrices do not have similar
         * values).
         *
         * This is because letter pairs that were never found in the input are
         * very likely to be invalid letter pairs. If these letter pairs are
         * found in the candidate line, it is very likely that the line
         * contains invalid English words.
         *
         * @param matrix1  The first matrix to compare
         * @param matrix2  The second matrix to compare
         * @return  the similarity score; the total of all absolute differences
         * in all corresponding values in each matrix
         */
        public static double compareMatricesNoZero(double[][] matrix1, double[][] matrix2) {
            double totalDifference = 0;

            for (int i = 0; i < matrix1.length; i++) {
                for (int j = 0; j < matrix1[0].length; j++) {
                    if (matrix1[i][j] == 0 && matrix2[i][j] > 0) {
                        return Double.MAX_VALUE;
                    }
                    totalDifference += Math.abs(matrix1[i][j] - matrix2[i][j]);
                }
            }

            return totalDifference;
        }

    }

    public static void printEnglishLines() throws IOException {
        double[][] fileBigram = BigramBuilder.buildBigramFromFile("TheOddyseyI-III.txt");

        /* Keep track of the 3 most similar lines to our sample */
        double similarityScore1 = Integer.MAX_VALUE;
        double similarityScore2 = Integer.MAX_VALUE;
        double similarityScore3 = Integer.MAX_VALUE;

        String line1 = "";
        String line2 = "";
        String line3 = "";

        BufferedReader bufferedReader = new BufferedReader(new FileReader("challenge.txt"));

        try {
            String line = bufferedReader.readLine();

            while (line !=null) {
                double[][] challengeBigram = BigramBuilder.buildBigramFromString(line);
                /*
                 * `compareMatrices` by itself did not find the third valid
                 * line correctly, so `compareMatricesNoZero` is used instead.
                 * This eliminates lines that correspond very well to our
                 * sample but contain letter pairs that do not exist in our
                 * sample.
                 */
                double similarityScore = BigramBuilder.compareMatricesNoZero(fileBigram, challengeBigram);

                if (similarityScore < similarityScore1) {
                    similarityScore3 = similarityScore2;
                    similarityScore2 = similarityScore1;
                    similarityScore1 = similarityScore;
                    line3 = line2;
                    line2 = line1;
                    line1 = line;
                } else if (similarityScore < similarityScore2) {
                    similarityScore3 = similarityScore2;
                    similarityScore2 = similarityScore;
                    line3 = line2;
                    line2 = line;
                } else if (similarityScore < similarityScore3) {
                    similarityScore3 = similarityScore;
                    line3 = line;
                }

                line = bufferedReader.readLine();
            }

        } finally {
            bufferedReader.close();
        }

        System.out.println(line1);
        System.out.println(line2);
        System.out.println(line3);
    }

    public static void main(String[] args) throws IOException {
        printEnglishLines();
    }
}
