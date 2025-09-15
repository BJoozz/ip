package jack.error;

/**
 * Thrown to indicate that a user-specified task index is invalid.
 * <p>
 * This can happen if the input is not a number or the number
 * is outside the valid task list range.
 */
public class InvalidIndexException extends JackException {
    /**
     * Creates a new {@code InvalidIndexException}.
     *
     * @param action name of the action attempted (e.g., {@code "delete"})
     */
    public InvalidIndexException(String action) {
        super("That index is not valid for \"" + action + "\". Use a 1-based index within the list range.");
    }
}
