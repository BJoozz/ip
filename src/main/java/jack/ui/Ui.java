package jack.ui;

import java.util.Scanner;

import jack.model.TaskList;

/**
 * Handles user interaction for the Jack application.
 * <p>
 * Provides methods to display messages, errors, and task lists,
 * as well as reading user commands from standard input.
 */
public class Ui {
    /** Horizontal line used to delimit output blocks. */
    private static final String LINE = "____________________________________________________________";

    /** Scanner used to read input from standard input. */
    private final Scanner sc = new Scanner(System.in);

    private final boolean captureMode;
    private final StringBuilder buffer;

    /** CLI mode (prints to console). */
    public Ui() {
        this(false);
    }

    /** If captureMode is true, output is captured and retrievable via getCaptured(). */
    public Ui(boolean captureMode) {
        this.captureMode = captureMode;
        this.buffer = captureMode ? new StringBuilder() : null;
    }

    private void raw(String s) {
        if (captureMode) {
            buffer.append(s).append(System.lineSeparator());
        } else {
            System.out.println(s);
        }
    }

    /** Gets captured text (GUI mode). */
    public String getCaptured() {
        String out = buffer.toString().trim();
        buffer.setLength(0); // clear the buffer for next use
        return out;
    }

    /**
     * Displays the welcome message at program startup.
     */
    public void showWelcome() {
        showLine();
        raw(" Hello! I'm Jack");
        raw(" What can I do for you?");
        showLine();
    }

    /**
     * Reads a command line from the user.
     *
     * @return the next line of user input
     */
    public String readCommand() {
        return sc.nextLine();
    }

    /**
     * Prints a horizontal line separator.
     */
    public void showLine() {
        if (!captureMode) {
            System.out.println(LINE);
        }
    }

    /**
     * Displays an error message indicating failure to load previous tasks.
     */
    public void showLoadingError() {
        showBlock("Error loading previous tasks.");
    }

    /**
     * Displays a generic error message with the given details.
     *
     * @param msg error details
     */
    public void showError(String msg) {
        showBlock("Uh oh! " + msg);
    }

    /**
     * Displays the program exit message.
     */
    public void showExit() {
        showBlock("Bye. Hope to see you again soon!");
    }

    /**
     * Displays one or more lines of text as a block wrapped between horizontal lines.
     *
     * @param lines lines of text to display
     */
    public void showBlock(String... lines) {
        showLine();
        for (String s : lines) {
            raw(" " + s);
        }
        showLine();
    }

    /**
     * Displays the current list of tasks in numbered order.
     *
     * @param tasks task list to display
     */
    public void showList(TaskList tasks) {
        showLine();
        raw(" Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            raw(" " + (i + 1) + "." + tasks.get(i));
        }
        showLine();
    }
}
