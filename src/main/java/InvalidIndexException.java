public class InvalidIndexException extends QB7Exception {
    public InvalidIndexException(String action) {
        super("That index is not valid for \"" + action + "\". Use a 1-based index within the list range.");
    }
}
