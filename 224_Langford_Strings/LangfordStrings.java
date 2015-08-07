import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LangfordStrings {

    public static void main(String[] args) {
        List<String> langfordStrings = new ArrayList<String>();
        int n = 4;
        String string = "";
        for (int i = 0; i < n * 2; i++) {
            string += " ";
        }
        
        langfordStrings = createLangfordStrings(string, 0, n);
        System.out.println(langfordStrings);

        // Bonus
        sortedFirstK(20, 10);
        System.out.println(firstKLangfordStrings);
    }
    
    /*
     * Create Langford strings by filling in pairs of letters, starting with A.
     * For each valid pair of positions (eg. A_A___, _A_A__, etc.), the function
     * is called recursively to fill in the subsequent letters. If there are no
     * valid positions for a certain letter, the algorithm backtracks (returns).
     * Strings that have all their letters filled are valid answers and are 
     * returned.
     */
    public static List<String> createLangfordStrings(String string, int depth, int end) {
        List<String> langfordStrings = new ArrayList<String>();
        /* Return if the string is completely filled with valid letters. */
        if (depth == end) {
            langfordStrings.add(string);
            return langfordStrings;
        }
        
        char currentChar = Character.toChars(65 + depth)[0];
        int distance = depth + 1;

        /*
         * For each letter the valid positions are from 0 to the position that
         * is a distance plus one equal to the index away from the end.
         * Eg. ___A_A  <- last possible position
         *        ^
         *   first of pair
         */
        for (int i = 0; i < string.length() - distance - 1; i++) {
            /*
             * If both letter positions are valid, we add them to the string and 
             * recurse down.
             */
            if (string.charAt(i) == ' ' && string.charAt(i + distance + 1) == ' ') {
                String newString = string;
                newString = replaceAt(newString, i, currentChar);
                newString = replaceAt(newString, i + distance + 1, currentChar);
                langfordStrings.addAll(createLangfordStrings(newString, depth + 1, end));
            }
        }
        
        return langfordStrings;
    }

    /*
     * List to hold first k answers. This is done to check for an early return;
     * once the list has k elements, we can stop.
     */
    static List<String> firstKLangfordStrings = new ArrayList<String>();
    
    public static void sortedFirstK(int n, int k) {
        String string = "";
        for (int i = 0; i < n * 2; i++) {
            string += " ";
        }
        
        boolean[] lettersUsed = new boolean[n];
        createLangfordStringsEarlyReturn(string, 0, k, lettersUsed);
    }

    /*
     * A different algorithm is used here. This time the strings are generated
     * in sorted order, allowing us to end when k strings are generated.
     * The position is the current String position we wish to fill. An array
     * of booleans is used to store which letters have been used in generating
     * a particular string.
     *
     * The algorithm starts at the leftmost position and fills it and its 
     * corresponding pair with the letter 'A' then recurses. The same is done
     * for the rest of the letters. As it recurses, it attempts to fill in 
     * each position with letters in alphabetical order. If a position already
     * contains a character, the algorithm continues. If no valid letters can 
     * be used, the algorithm returns (backtracks).
     */
     public static void createLangfordStringsEarlyReturn(String string, int position, int k, boolean[] lettersUsed) {
        /* 
         * Return if the list is filled with k elements. (We are done because
         * values are generated in alphabetical order)
        */
        if (firstKLangfordStrings.size() == k) {
            return;
        }

        /*
         * If all positions in the string are filled with letters, we can
         * add this to our list of answers.
         */
        if (position == string.length() - 1) {
            firstKLangfordStrings.add(string);
            return;
        }
        
        /* Continue if a position is already filled. */
        if (string.charAt(position) != ' ') {
            createLangfordStringsEarlyReturn(string, position + 1, k, lettersUsed);
        }

        /*
         * Each letter is attempted in order to determine if it and its
         * matching pair is a valid choice for the given position.
         * After its use, a letter is added to the lettersUsed array,
         * ensuring that we add a different letter each time we recurse.
         */
        for (int i = 0; i < string.length() / 2; i++) {
            int distance = i + 1;
            if (!lettersUsed[i] && (position + distance + 1) < string.length() && firstKLangfordStrings.size() != k && string.charAt(position) == ' ' && string.charAt(position + distance + 1) == ' ') {
                char currentChar = Character.toChars(65 + i)[0];
                String newString = string;
                newString = replaceAt(newString, position, currentChar);
                newString = replaceAt(newString, position + distance + 1, currentChar);
                boolean[] newLettersUsed = new boolean[lettersUsed.length];
                System.arraycopy(lettersUsed, 0, newLettersUsed, 0, lettersUsed.length);
                newLettersUsed[i] = true;
                createLangfordStringsEarlyReturn(newString, position + 1, k, newLettersUsed); 
            }
        }
    }
    
    /*
     * Function to replace the character at position i with a new character c.
     */
    public static String replaceAt(String string, int i, char ch) {
        StringBuilder sb = new StringBuilder(string);
        sb.setCharAt(i, ch);
        return sb.toString();
    }
}
