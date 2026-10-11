package search;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MatchingStrategyCatalogTest {
    private static final String LOWERCASE_ALL = "all";
    private static final String PADDED_ANY = "  any  ";
    private static final String NONE_IN_CAPITALS = "NONE";
    private static final String UNKNOWN_NAME = "SOME";
    private static final String DEFAULT_NAMES = "ALL, ANY, NONE";
    private static final String CUSTOM_NAME = "EVERY";

    private MatchingStrategyCatalog catalog;

    @BeforeEach
    void createCatalogWithDefaultStrategies() {
        catalog = MatchingStrategyCatalog.withDefaultStrategies();
    }

    @Test
    void findByName_allInLowercase_returnsTheAllWordsStrategy() {
        assertInstanceOf(AllWordsMatchingStrategy.class,
                catalog.findByName(LOWERCASE_ALL).orElseThrow());
    }

    @Test
    void findByName_anySurroundedBySpaces_returnsTheAnyWordStrategy() {
        assertInstanceOf(AnyWordMatchingStrategy.class, catalog.findByName(PADDED_ANY).orElseThrow());
    }

    @Test
    void findByName_none_returnsTheNoWordsStrategy() {
        assertInstanceOf(NoWordsMatchingStrategy.class,
                catalog.findByName(NONE_IN_CAPITALS).orElseThrow());
    }

    @Test
    void findByName_unknownName_returnsNothing() {
        assertTrue(catalog.findByName(UNKNOWN_NAME).isEmpty());
    }

    @Test
    void availableNames_defaultStrategies_listsThemInMenuOrder() {
        assertEquals(DEFAULT_NAMES, catalog.availableNames());
    }

    @Test
    void availableNames_customStrategies_listsOnlyThoseNames() {
        MatchingStrategyCatalog customCatalog =
                new MatchingStrategyCatalog(Map.of(CUSTOM_NAME, new AllWordsMatchingStrategy()));

        assertEquals(CUSTOM_NAME, customCatalog.availableNames());
    }

    @Test
    void findByName_customStrategy_returnsTheInjectedStrategy() {
        MatchingStrategy injected = new AnyWordMatchingStrategy();
        MatchingStrategyCatalog customCatalog = new MatchingStrategyCatalog(Map.of(CUSTOM_NAME, injected));

        assertEquals(injected, customCatalog.findByName(CUSTOM_NAME).orElseThrow());
    }
}
