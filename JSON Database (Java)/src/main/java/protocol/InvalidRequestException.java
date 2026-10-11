package protocol;

/**
 * Raised when a request cannot be understood, carrying the reason to report back.
 */
public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException(String reason) {
        super(reason);
    }
}
