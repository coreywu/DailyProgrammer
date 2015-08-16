import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class PoetryInAHaystack {

    public static class BigramBuilder {

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

            String[] splitWords = input.replaceAll("[^a-zA-Z ]", "").toLowerCase().split(" ");

            /*
             * For each line of text, split the line into individual words,
             * then count the occurrences of each letter and pair of
             * letters.
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

        public static double[][] buildBigramFromFile(String filename) throws IOException {
            double[][] bigramMatrix = new double[26][26];
            int[] letterCounts = new int[26];

            BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));

            try {
                String line = bufferedReader.readLine();

                while (line !=null) {
                    String[] splitWords = line.replaceAll("[^a-zA-Z ]", "").toLowerCase().split(" ");

                    /*
                     * Count and tore occurrences of letters in 'letterCounts'
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

                    line = bufferedReader.readLine();
                }

            } finally {
                bufferedReader.close();
            }

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
         * {@code letterCounts} and pairs of letters in {@code bigramMatrix}
         *
         *
         * @param line
         * @param bigramMatrix
         * @param letterCounts
         */
        public static void addToBigramMatrix(String line, double[][] bigramMatrix, double[] letterCounts) {
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

        public static double compareMatrices(double[][] matrix1, double[][] matrix2) {
            double totalDifference = 0;

            for (int i = 0; i < matrix1.length; i++) {
                for (int j = 0; j < matrix1[0].length; j++) {
                    totalDifference += Math.abs(matrix1[i][j] - matrix2[i][j]);
                }
            }

            return totalDifference;
        }

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
