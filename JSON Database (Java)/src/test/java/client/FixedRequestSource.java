package client;

/**
 * Hands the client a request that a test prepared.
 */
class FixedRequestSource implements RequestSource {
    private final String requestJson;

    FixedRequestSource(String requestJson) {
        this.requestJson = requestJson;
    }

    @Override
    public String readRequest() {
        return requestJson;
    }
}
