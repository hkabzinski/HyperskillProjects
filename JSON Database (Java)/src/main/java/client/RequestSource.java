package client;

/**
 * Where the client takes the request it sends, so it does not care whether the request
 * was typed on the command line or prepared in a file.
 */
public interface RequestSource {
    String readRequest();
}
