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
        boolean[] done = new boolean[100];
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
                        String box = done[i] ? "[X]" : "[ ]";
                        System.out.println(" " + (i + 1) + "." + box + " " + tasks[i]);
                    }
                    System.out.println(LINE);
                }
            } else if (input.startsWith("mark ")) {
                // mark <index>
                try {
                    int idx = Integer.parseInt(input.substring(5).trim()); // 1-based
                    if (idx < 1 || idx > size) throw new IndexOutOfBoundsException();
                    done[idx - 1] = true;
                    printBlock(
                            "Nice! I've marked this task as done:",
                            "  " + "[X] " + tasks[idx - 1]
                    );
                } catch (Exception e) {
                    printBlock("Invalid index for mark.");
                }

            } else if (input.startsWith("unmark ")) {
                // unmark <index>
                try {
                    int idx = Integer.parseInt(input.substring(7).trim()); // 1-based
                    if (idx < 1 || idx > size) throw new IndexOutOfBoundsException();
                    done[idx - 1] = false;
                    printBlock(
                            "OK, I've marked this task as not done yet:",
                            "  " + "[ ] " + tasks[idx - 1]
                    );
                } catch (Exception e) {
                    printBlock("Invalid index for unmark.");
                }
            } else if (!input.isEmpty()) {
                // add task
                tasks[size] = input;
                done[size] = false;
                size++;
                printBlock("added: " + input);
            }
        }
    }
}
