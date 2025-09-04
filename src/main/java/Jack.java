public class Jack {
    private final Storage storage;
    private TaskList tasks;
    private final Ui ui;

    // default: use Storage() and ArrayList<Task>
    public Jack() {
        ui = new Ui();
        storage = new Storage();
        try {
            tasks = new TaskList(storage.load()); // Storage.load() returns ArrayList<Task>
        } catch (Exception e) {
            ui.showLoadingError();
            tasks = new TaskList();
        }
    }

    // keep old signature for compatibility (ignored path)
    public Jack(String ignoredFilePath) { this(); }

    public void run() {
        ui.showWelcome();
        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                // ui.showLine();
                isExit = Parser.dispatch(fullCommand, tasks, ui, storage);
            } catch (JackException e) {
                ui.showError(e.getMessage());
            } catch (Exception e) {
                ui.showError("Something went wrong internally: " + e.getClass().getSimpleName());
            } finally {
                // ui.showLine();
            }
        }
    }

    public static void main(String[] args) {
        new Jack().run();
    }
}
