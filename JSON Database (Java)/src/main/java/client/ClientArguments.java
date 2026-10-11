package client;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import java.util.Optional;
import protocol.RequestType;

/**
 * The options the client was started with.
 */
public class ClientArguments {
    static final String TYPE_OPTION = "-t";
    static final String KEY_OPTION = "-k";
    static final String VALUE_OPTION = "-v";
    static final String INPUT_FILE_OPTION = "-in";

    @Parameter(names = TYPE_OPTION)
    private String type;

    @Parameter(names = KEY_OPTION)
    private String key;

    @Parameter(names = VALUE_OPTION)
    private String value;

    @Parameter(names = INPUT_FILE_OPTION)
    private String inputFileName;

    public static ClientArguments parsedFrom(String... arguments) {
        ClientArguments parsed = new ClientArguments();
        JCommander.newBuilder().addObject(parsed).build().parse(arguments);
        return parsed;
    }

    public Optional<RequestType> requestType() {
        return Optional.ofNullable(type).flatMap(RequestType::forName);
    }

    public Optional<String> key() {
        return Optional.ofNullable(key);
    }

    public Optional<String> value() {
        return Optional.ofNullable(value);
    }

    public Optional<String> inputFileName() {
        return Optional.ofNullable(inputFileName);
    }
}
