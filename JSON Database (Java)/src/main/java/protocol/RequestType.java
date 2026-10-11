package protocol;

import com.google.gson.annotations.SerializedName;
import java.util.Arrays;
import java.util.Optional;

/**
 * The operations a client can ask the server to perform, named as they travel over the connection.
 */
public enum RequestType {
    @SerializedName("set")
    SET,
    @SerializedName("get")
    GET,
    @SerializedName("delete")
    DELETE,
    @SerializedName("exit")
    EXIT;

    public static Optional<RequestType> forName(String name) {
        return Arrays.stream(values())
                .filter(type -> type.name().equalsIgnoreCase(name))
                .findFirst();
    }
}
