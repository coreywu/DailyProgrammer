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

        sortedFirstK(20, 10);
        System.out.println(firstKLangfordStrings);
    }
    
    public static List<String> createLangfordStrings(String string, int depth, int end) {
        List<String> langfordStrings = new ArrayList<String>();
//      System.out.println("STRING: " + string);
        if (depth == end) {
            langfordStrings.add(string);
            return langfordStrings;
        }
        
        char currentChar = Character.toChars(65 + depth)[0];
        int distance = depth + 1;

        for (int i = 0; i < string.length() - distance - 1; i++) {
            if (string.charAt(i) == ' ' && string.charAt(i + distance + 1) == ' ') {
                String newString = string;
                newString = replaceAt(newString, i, currentChar);
                newString = replaceAt(newString, i + distance + 1, currentChar);
//              System.out.println("NEW STRING: " + newString);
//              System.out.println("STRINGS NULL? : " + (langfordStrings == null) + " NEWSTRING: " + (newString == null));
                langfordStrings.addAll(createLangfordStrings(newString, depth + 1, end));
            }
        }
        
        return langfordStrings;
    }

    static List<String> firstKLangfordStrings = new ArrayList<String>();
    
    public static void sortedFirstK(int n, int k) {
        String string = "";
        for (int i = 0; i < n * 2; i++) {
            string += " ";
        }
        
        List<Boolean> lettersUsed = new ArrayList<Boolean>(Collections.nCopies(n, false));

        
        createLangfordStringsEarlyReturn(string, 0, k, lettersUsed);
    }

    // position is the current position that we are looking to fill in the string
    public static void createLangfordStringsEarlyReturn(String string, int position, int k, List<Boolean> lettersUsed) {
        if (firstKLangfordStrings.size() == k) {
            return;
        }

        if (position == string.length() - 1) {
//          System.out.println("POS: " + position + "STRING: " + string);
            firstKLangfordStrings.add(string);
            return;
        }
        
        if (string.charAt(position) != ' ') {
            createLangfordStringsEarlyReturn(string, position + 1, k, lettersUsed);
        }
        
//      System.out.println("STRING: " + string);

        for (int i = 0; i < string.length() / 2; i++) {
            int distance = i + 1;
//          System.out.println("lettersused: " + i + " " + lettersUsed.get(i) + " pos: " + position + "dist: " + distance + " " + ((position + distance + 1) < string.length()));
            if (!lettersUsed.get(i) && (position + distance + 1) < string.length() && firstKLangfordStrings.size() != k && string.charAt(position) == ' ' && string.charAt(position + distance + 1) == ' ') {
                char currentChar = Character.toChars(65 + i)[0];
                String newString = string;
                newString = replaceAt(newString, position, currentChar);
                newString = replaceAt(newString, position + distance + 1, currentChar);
//              System.out.println("NEW STRING: " + newString);
                List<Boolean> newLettersUsed = new ArrayList<Boolean>(lettersUsed);
                newLettersUsed.set(i, true);
                createLangfordStringsEarlyReturn(newString, position + 1, k, newLettersUsed); 
            }
        }
    }
    
    public static String replaceAt(String string, int i, char ch) {
        StringBuilder sb = new StringBuilder(string);
        sb.setCharAt(i, ch);
        return sb.toString();
    }
}

