import java.util.Scanner;

public class Ui {
    private static final String LINE = "____________________________________________________________";
    private final Scanner sc = new Scanner(System.in);

    public void showWelcome() {
        System.out.println(LINE);
        System.out.println(" Hello! I'm Jack");
        System.out.println(" What can I do for you?");
        System.out.println(LINE);
    }

    public String readCommand() { return sc.nextLine(); }
    public void showLine() { System.out.println(LINE); }
    public void showLoadingError() { showBlock("Error loading previous tasks."); }
    public void showError(String msg) { showBlock("Uh oh! " + msg); }
    public void showExit() { showBlock("Bye. Hope to see you again soon!"); }

    public void showBlock(String... lines) {
        System.out.println(LINE);
        for (String s : lines) System.out.println(" " + s);
        System.out.println(LINE);
    }

    public void showList(TaskList tasks) {
        System.out.println(LINE);
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + "." + tasks.get(i));
        }
        System.out.println(LINE);
    }
}
