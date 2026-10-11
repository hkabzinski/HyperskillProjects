package search;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WordTokenizerTest {
    private static final String MIXED_CASE_LINE = "Erick HARRINGTON harrington@Gmail.com";
    private static final List<String> MIXED_CASE_WORDS =
            List.of("erick", "harrington", "harrington@gmail.com");
    private static final String PADDED_LINE = "   Richard    Roy    roy@gmail.com  ";
    private static final List<String> PADDED_WORDS = List.of("richard", "roy", "roy@gmail.com");
    private static final String SINGLE_WORD_LINE = "Katie";
    private static final List<String> SINGLE_WORD = List.of("katie");
    private static final String ONLY_SPACES = "    ";

    private WordTokenizer tokenizer;

    @BeforeEach
    void createTokenizer() {
        tokenizer = new WordTokenizer();
    }

    @Test
    void wordsIn_mixedCaseLine_returnsLowercasedWords() {
        assertEquals(MIXED_CASE_WORDS, tokenizer.wordsIn(MIXED_CASE_LINE));
    }

    @Test
    void wordsIn_lineWithExtraSpaces_returnsWordsWithoutEmptyOnes() {
        assertEquals(PADDED_WORDS, tokenizer.wordsIn(PADDED_LINE));
    }

    @Test
    void wordsIn_singleWord_returnsThatWord() {
        assertEquals(SINGLE_WORD, tokenizer.wordsIn(SINGLE_WORD_LINE));
    }

    @Test
    void wordsIn_onlySpaces_returnsNoWords() {
        assertEquals(List.of(), tokenizer.wordsIn(ONLY_SPACES));
    }

    @Test
    void wordsIn_emptyText_returnsNoWords() {
        assertEquals(List.of(), tokenizer.wordsIn(""));
    }
}
