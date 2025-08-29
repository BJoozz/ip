public class MissingArgumentException extends QB7Exception {
    public MissingArgumentException(String need) {
        super("Missing required part: " + need);
    }
}
