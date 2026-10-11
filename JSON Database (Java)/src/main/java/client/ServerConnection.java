package client;

/**
 * The link to the database server over which one request and its answer travel.
 */
public interface ServerConnection {
    String exchange(String requestJson);
}
