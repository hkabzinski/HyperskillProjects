package search;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * Knows the matching strategies the user can choose from and finds them by name.
 */
public class MatchingStrategyCatalog {
    static final String ALL = "ALL";
    static final String ANY = "ANY";
    static final String NONE = "NONE";
    private static final String NAME_SEPARATOR = ", ";

    private final Map<String, MatchingStrategy> strategiesByName;

    public MatchingStrategyCatalog(Map<String, MatchingStrategy> strategiesByName) {
        this.strategiesByName = new LinkedHashMap<>(strategiesByName);
    }

    public static MatchingStrategyCatalog withDefaultStrategies() {
        MatchingStrategy anyWordStrategy = new AnyWordMatchingStrategy();
        Map<String, MatchingStrategy> defaults = new LinkedHashMap<>();
        defaults.put(ALL, new AllWordsMatchingStrategy());
        defaults.put(ANY, anyWordStrategy);
        defaults.put(NONE, new NoWordsMatchingStrategy(anyWordStrategy));
        return new MatchingStrategyCatalog(defaults);
    }

    public Optional<MatchingStrategy> findByName(String name) {
        return Optional.ofNullable(strategiesByName.get(name.trim().toUpperCase(Locale.ROOT)));
    }

    public String availableNames() {
        return String.join(NAME_SEPARATOR, strategiesByName.keySet());
    }
}
