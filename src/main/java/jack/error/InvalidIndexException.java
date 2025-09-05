package jack.error;

public class InvalidIndexException extends JackException {
    public InvalidIndexException(String action) {
        super("That index is not valid for \"" + action + "\". Use a 1-based index within the list range.");
    }
}
