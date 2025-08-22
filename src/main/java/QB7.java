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
        String[] tasks = new String[100];
        int size = 0;

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

            if (input.equals("list")) {
                if (size == 0) {
                    printBlock(); // just the lines if nothing yet (optional)
                } else {
                    System.out.println(LINE);
                    for (int i = 0; i < size; i++) {
                        System.out.println(" " + (i + 1) + ". " + tasks[i]);
                    }
                    System.out.println(LINE);
                }
            } else {
                // add task (assume size <= 100 per spec)
                tasks[size++] = input;
                printBlock("added: " + input);
            }
        }
    }
}
