/*
* AnagramJob
*
* @description: Subclass of AJob.
*
* @author: m.pinna10@studenti.unipi.it
*/

package com.mycompany.anagram;

import com.mycompany.framework.AJob;
import com.mycompany.framework.Pair;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;


public class AnagramJob extends AJob<String, String> {

    private static final int LEN_THRESHOLD = 4; /* minimum word length */
    private static String filename;
    
    
    /**
     * Constructor.
     * 
     * @param filename The filename from which to read the words
     */
    public AnagramJob(String filename) {
        this.filename = filename;
    }
    
    /**
     * Applies the 'characters in alphabetical order' transformation.
     * 
     * @param s The string to transform
     * @return The input string alphabetically ordered
     */
    private String ciao(String s) {
        char charArray[] = s.toLowerCase().toCharArray();
        Arrays.sort(charArray);
        return new String(charArray);
    }
    
    /**
     * Reads the file and returns a stream containing all pairs with format
     * (ciao(w), w), where 'w' must have the following properties:
     *  - length greater or equal than 'LEN_THRESHOLD'
     *  - only alphabetic characters
     * 
     * @return A stream containing all pairs with format (ciao(w), w)
     */
    @Override
    public Stream<Pair<String, String>> execute() {
        try {
            return Files.lines(Paths.get(filename))
                    .flatMap(x -> Arrays.stream(x.split("\\s").clone()))
                    .filter(word -> word.length() > LEN_THRESHOLD 
                            && word.matches("^[a-zA-Z]+$"))
                    .map(x -> new Pair(ciao(x), x.toLowerCase()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Stream.empty();
    }

}
