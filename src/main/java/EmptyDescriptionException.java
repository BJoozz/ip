public class EmptyDescriptionException extends QB7Exception {
    public EmptyDescriptionException(String what) {
        super("The " + what + " description cannot be empty.");
    }
}
