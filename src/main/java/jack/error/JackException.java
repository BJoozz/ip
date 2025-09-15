package jack.error;

/**
 * Base class for all custom exceptions in the Jack application.
 * <p>
 * Serves as a common superclass for user input and parsing errors.
 */
public class JackException extends Exception {
    /**
     * Creates a new {@code JackException} with the specified message.
     *
     * @param msg explanation of the error
     */
    public JackException(String msg) {
        super(msg);
    }
}
