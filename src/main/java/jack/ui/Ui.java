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

    /**
     * Displays the welcome message at program startup.
     */
    public void showWelcome() {
        System.out.println(LINE);
        System.out.println(" Hello! I'm Jack");
        System.out.println(" What can I do for you?");
        System.out.println(LINE);
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
        System.out.println(LINE);
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
        System.out.println(LINE);
        for (String s : lines) {
            System.out.println(" " + s);
        }
        System.out.println(LINE);
    }

    /**
     * Displays the current list of tasks in numbered order.
     *
     * @param tasks task list to display
     */
    public void showList(TaskList tasks) {
        System.out.println(LINE);
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + "." + tasks.get(i));
        }
        System.out.println(LINE);
    }
}
