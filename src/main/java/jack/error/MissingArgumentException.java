package jack.error;

/**
 * Thrown to indicate that a command is missing a required argument.
 * <p>
 * For example, a {@code deadline} command without a {@code /by} date.
 */
public class MissingArgumentException extends JackException {
    /**
     * Creates a new {@code MissingArgumentException}.
     *
     * @param need description of the missing argument
     */
    public MissingArgumentException(String need) {
        super("Missing required part: " + need);
    }
}
