package server;

import protocol.RecordPath;
import protocol.Request;
import protocol.Response;

/**
 * Carries out one request against the database and reports the outcome.
 */
public class RequestHandler {
    static final String NO_SUCH_KEY_REASON = "No such key";
    static final String MISSING_VALUE_REASON = "No value";

    private final Database database;
    private final ShutdownSignal shutdownSignal;

    public RequestHandler(Database database, ShutdownSignal shutdownSignal) {
        this.database = database;
        this.shutdownSignal = shutdownSignal;
    }

    public Response handle(Request request) {
        return switch (request.type()) {
            case SET -> writeRecord(request);
            case GET -> readRecord(request);
            case DELETE -> deleteRecord(request);
            case EXIT -> stopServing();
        };
    }

    private Response writeRecord(Request request) {
        if (request.value() == null) {
            return Response.error(MISSING_VALUE_REASON);
        }
        database.write(RecordPath.fromKey(request.key()), request.value());
        return Response.ok();
    }

    private Response readRecord(Request request) {
        return database.read(RecordPath.fromKey(request.key()))
                .map(Response::ok)
                .orElseGet(() -> Response.error(NO_SUCH_KEY_REASON));
    }

    private Response deleteRecord(Request request) {
        if (database.delete(RecordPath.fromKey(request.key()))) {
            return Response.ok();
        }
        return Response.error(NO_SUCH_KEY_REASON);
    }

    private Response stopServing() {
        shutdownSignal.raise();
        return Response.ok();
    }
}
