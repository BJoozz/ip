package jack;

import jack.error.JackException;
import jack.model.Storage;
import jack.model.TaskList;
import jack.ui.Ui;
/**
 * Entry point of the Jack task manager application.
 * <p>
 * Wires together the {@code Storage}, {@code TaskList}, and {@code Ui} components,
 * loads existing tasks from disk, and runs the main event loop.
 */
public class Jack {
    private final Storage storage;

    /** In-memory list of tasks currently managed by the application. */
    private TaskList tasks;
    private final Ui ui;
    /**
     * Creates a new {@code Jack} application instance.
     *
     * @param isGui true if running with a graphical interface, false otherwise
     */
    public Jack(boolean isGui) {
        ui = new Ui(isGui);
        storage = new Storage();
        try {
            tasks = new TaskList(storage.load()); // jack.model.Storage.load() returns ArrayList<jack.model.Task>
        } catch (Exception e) {
            ui.showLoadingError();
            tasks = new TaskList();
        }
    }
    public Jack() {
        this(false);
    }


    /**
     * Creates a new {@code Jack} application instance.
     * <p>
     * This constructor ignores the provided file path and delegates
     * to the default constructor.
     *
     * @param ignoredFilePath unused file path argument
     */
    public Jack(String ignoredFilePath) {
        this();
    }

    /**
     * Runs the main event loop until an exit command is received.
     */
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

    /**
     * Starts the application.
     *
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        new Jack().run();
    }

    /**
     * Processes a single user input and returns the chatbot's response.
     * <p>
     * If the input is an exit command, this method will also request
     * application termination.
     *
     * @param input user input string
     * @return response string from Jack
     */
    public String getResponse(String input) {
        try {
            boolean isExit = Parser.dispatch(input, tasks, ui, storage);
            if (isExit) {
                // Extra safeguard: Platform.exit() closes JavaFX window
                javafx.application.Platform.exit();
            }
            String out = ui.getCaptured();
            return out.isEmpty() ? "(no output)" : out;
        } catch (JackException e) {
            return e.getMessage();
        } catch (Exception e) {
            return "Something went wrong internally: " + e.getClass().getSimpleName();
        }
    }

    /**
     * Generates the welcome message shown at application startup.
     *
     * @return welcome message string
     */
    public String getWelcomeMessage() {
        ui.showWelcome();
        return ui.getCaptured();
    }
}
