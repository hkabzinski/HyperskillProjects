package client;

/**
 * Stands in for the server: remembers the request it was given and answers with a prepared response.
 */
class FakeServerConnection implements ServerConnection {
    private final String response;
    private String receivedRequest;

    FakeServerConnection(String response) {
        this.response = response;
    }

    @Override
    public String exchange(String requestJson) {
        receivedRequest = requestJson;
        return response;
    }

    String receivedRequest() {
        return receivedRequest;
    }
}
