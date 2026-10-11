package search;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Splits a line into the words it can be found by, ignoring letter case and extra spaces.
 */
public class WordTokenizer {
    private static final String WHITESPACE = "\s+";

    public List<String> wordsIn(String text) {
        String trimmed = text.trim();
        if (trimmed.isEmpty()) {
            return List.of();
        }
        return Arrays.stream(trimmed.split(WHITESPACE))
                .map(word -> word.toLowerCase(Locale.ROOT))
                .toList();
    }
}
