public class MissingArgumentException extends JackException {
    public MissingArgumentException(String need) {
        super("Missing required part: " + need);
    }
}
