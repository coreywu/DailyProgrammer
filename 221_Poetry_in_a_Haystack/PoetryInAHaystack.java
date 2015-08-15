import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class PoetryInAHaystack {

    public static class BigramBuilder {

        public static double[][] buildBigramFromString(String input) {
            /*
             * bigramMatrix[_1][_2] is the percentage of times that the second
             * letter follows the first. It is the probability of the second
             * letter occuring given the first letter has occured.
             */
            double[][] bigramMatrix = new double[26][26];

            /*
             * A count of the occurance of each letter. Used to calculate the
             * bigramMatrix after all 2 letter combinations have been examined.
             */
            int[] letterCounts = new int[26];

            String[] splitWords = input.replaceAll("[^a-zA-Z ]", "").toLowerCase().split(" ");

//            for (String word : splitWords) {
//                System.out.println(word);
//            }

            for (String word : splitWords) {
//				char first = word.charAt(0);
                if (word.length() == 0) {
                    continue;
                }
                int firstIndex = word.charAt(0) - 97;
                letterCounts[firstIndex]++;
                for (int i = 1; i < word.length(); i++) {
//					char second = word.charAt(i);
                    int secondIndex = word.charAt(i) - 97;
                    letterCounts[secondIndex]++;
//					System.out.println("first: " + first + " ascii: " + (first - 97) + " second: " + second + " ascii: " + (second - 97));
                    bigramMatrix[firstIndex][secondIndex]++;
                    firstIndex = secondIndex;
                }
            }

//            for (int i = 0; i < letterCounts.length; i++) {
//                System.out.println((char) (i + 97) + " " + letterCounts[i]);
//            }

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

//            for (int i = 0; i < bigramMatrix.length; i++) {
//                for (int j = 0; j < bigramMatrix[0].length; j++) {
////					System.out.print((char)(i + 97) + " " + (char)(j + 97) + " " + bigramMatrix[i][j]);
//                    System.out.print(bigramMatrix[i][j] + " ");
//                }
//                System.out.println();
//
//            }

            return bigramMatrix;
        }

        public static double[][] buildBigramFromFile(String filename) throws IOException {
            double[][] bigramMatrix = new double[26][26];
            int[] letterCounts = new int[26];

            System.out.println("working directory: " + System.getProperty("user.dir"));

            BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));

            try {
                String line = bufferedReader.readLine();

                /*
                 * For each line of text, split the line into individual words,
                 * then count the occurrances of each letter and pair of
                 * letters.
                 */
                while (line !=null) {
                    String[] splitWords = line.replaceAll("[^a-zA-Z ]", "").toLowerCase().split(" ");

//                    for (String word : splitWords) {
//                        System.out.println(word);
//                    }

                    /*
                     * Count and tore occurrences of letters in 'letterCounts'
                     * and pairs in 'bigramMatrix'.
                     */
                    for (String word : splitWords) {
//				char first = word.charAt(0);
                        if (word.length() == 0) {
                            continue;
                        }
                        int firstIndex = word.charAt(0) - 97;
                        letterCounts[firstIndex]++;
                        for (int i = 1; i < word.length(); i++) {
//					char second = word.charAt(i);
                            int secondIndex = word.charAt(i) - 97;
                            letterCounts[secondIndex]++;
//					System.out.println("first: " + first + " ascii: " + (first - 97) + " second: " + second + " ascii: " + (second - 97));
                            bigramMatrix[firstIndex][secondIndex]++;
                            firstIndex = secondIndex;
                        }
                    }

                    line = bufferedReader.readLine();
                }

            } finally {
                bufferedReader.close();
            }


            for (int i = 0; i < letterCounts.length; i++) {
                System.out.println((char) (i + 97) + " " + letterCounts[i]);
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

            printBigramMatrix(bigramMatrix);

            return bigramMatrix;
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

    public static void main(String[] args) throws IOException {
//    	String input = "I have seen nothing.";

    	String input = "Horatio says 'tis but our fantasy,"
    				 + "And will not let belief take hold of him"
    				 + "Touching this dreaded sight, twice seen of us:"
    				 + "Therefore I have entreated him along"
    				 + "With us to watch the minutes of this night;"
    				 + "That if again this apparition come,"
    				 + "He may approve our eyes and speak to it."
    				 ;

        double[][] inputBigram = BigramBuilder.buildBigramFromString(input);

        double[][] fileBigram = BigramBuilder.buildBigramFromFile("TheOddyseyI-III.txt");


        double value1 = Integer.MAX_VALUE;
        double value2 = Integer.MAX_VALUE;
        double value3 = Integer.MAX_VALUE;
        double value4 = Integer.MAX_VALUE;
        double value5 = Integer.MAX_VALUE;

        String line1 = "";
        String line2 = "";
        String line3 = "";
        String line4 = "";
        String line5 = "";

        BufferedReader bufferedReader = new BufferedReader(new FileReader("challenge.txt"));

        try {
            String line = bufferedReader.readLine();

            while (line !=null) {
                double[][] challengeBigram = BigramBuilder.buildBigramFromString(line);
//                BigramBuilder.printBigramMatrix(challengeBigram);

//                double similarityScore = BigramBuilder.compareMatrices(fileBigram, challengeBigram);
                double similarityScore = BigramBuilder.compareMatricesNoZero(fileBigram, challengeBigram);

                if (similarityScore < value1) {
                    value5 = value4;
                    value4 = value3;
                    value3 = value2;
                    value2 = value1;
                    value1 = similarityScore;
                    line5 = line4;
                    line4 = line3;
                    line3 = line2;
                    line2 = line1;
                    line1 = line;
                } else if (similarityScore < value2) {
                    value5 = value4;
                    value4 = value3;
                    value3 = value2;
                    value2 = similarityScore;
                    line5 = line4;
                    line4 = line3;
                    line3 = line2;
                    line2 = line;
                } else if (similarityScore < value3) {
                    value5 = value4;
                    value4 = value3;
                    value3 = similarityScore;
                    line5 = line4;
                    line4 = line3;
                    line3 = line;
                } else if (similarityScore < value4) {
                    value5 = value4;
                    value4 = similarityScore;
                    line5 = line4;
                    line4 = line;
                } else if (similarityScore < value5) {
                    value5 = similarityScore;
                    line5 = line;
                }

                line = bufferedReader.readLine();
            }

        } finally {
            bufferedReader.close();
        }

        System.out.println(line1);
        System.out.println(line2);
        System.out.println(line3);
        System.out.println(line4);
        System.out.println(line5);
    }

}
