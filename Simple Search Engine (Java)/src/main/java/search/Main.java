package search;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class Main {
    static final String MISSING_DATA_OPTION_MESSAGE =
            "Please tell me where the data is, for example: --data people.txt";

    public static void main(String[] args) {
        Optional<String> dataFileName = new CommandLineArguments(args).dataFileName();
        if (dataFileName.isEmpty()) {
            System.out.println(MISSING_DATA_OPTION_MESSAGE);
            return;
        }
        TextIO io = new ConsoleIO(System.in, System.out);
        PersonSource source = new PersonFileSource(Path.of(dataFileName.get()));
        searchMenuFor(source, io).run();
    }

    private static SearchMenu searchMenuFor(PersonSource source, TextIO io) {
        WordTokenizer tokenizer = new WordTokenizer();
        List<Person> people = source.readPeople();
        PersonDirectory directory =
                new PersonDirectory(people, new InvertedIndex(people, tokenizer), tokenizer);
        return new SearchMenu(io, directory, new PeoplePresenter(io),
                MatchingStrategyCatalog.withDefaultStrategies());
    }
}
