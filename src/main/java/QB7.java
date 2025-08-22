import java.util.Scanner;

public class QB7 {
    private static final String LINE = "____________________________________________________________";
    private static void printBlock(String... lines) {
        System.out.println(LINE);
        for (String s : lines) {
            System.out.println(" " + s);
        }
        System.out.println(LINE);
    }

    public static void main(String[] args) {
        System.out.println(LINE);
        System.out.println(" Hello! I'm QB7");
        System.out.println(" What can I do for you?");
        System.out.println(LINE);
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String input = sc.nextLine();
            if (input.equals("bye")) {
                printBlock("Bye. Hope to see you again soon!");
                break;
            }
            printBlock(input);
        }
    }
}
