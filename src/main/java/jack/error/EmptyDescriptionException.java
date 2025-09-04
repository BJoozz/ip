package jack.error;

public class EmptyDescriptionException extends JackException {
    public EmptyDescriptionException(String what) {
        super("The " + what + " description cannot be empty.");
    }
}
