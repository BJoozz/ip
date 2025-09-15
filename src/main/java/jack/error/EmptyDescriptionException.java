package jack.error;

/**
 * Thrown to indicate that a command was given without a required description.
 * <p>
 * For example, {@code "todo"} without any following text.
 */
public class EmptyDescriptionException extends JackException {
    /**
     * Creates a new {@code EmptyDescriptionException}.
     *
     * @param detail name of the command or field missing a description
     */
    public EmptyDescriptionException(String detail) {
        super("The " + detail + " description cannot be empty.");
    }
}
