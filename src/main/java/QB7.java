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
        Task[] tasks = new Task[100];
        int size = 0;

        System.out.println(LINE);
        System.out.println(" Hello! I'm QB7");
        System.out.println(" What can I do for you?");
        System.out.println(LINE);

        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String input = sc.nextLine().trim();

            // Exit
            if (input.equals("bye")) {
                printBlock("Bye. Hope to see you again soon!");
                break;
            }

            // List
            if (input.equals("list")) {
                if (size == 0) {
                    printBlock("Here are the tasks in your list:");
                } else {
                    System.out.println(LINE);
                    System.out.println(" Here are the tasks in your list:");
                    for (int i = 0; i < size; i++) {
                        // match sample: "1.[T][X] read book"
                        System.out.println(" " + (i + 1) + "." + tasks[i].toString());
                    }
                    System.out.println(LINE);
                }
                continue;
            }

            // Mark / Unmark (Level-3)
            if (input.startsWith("mark ")) {
                try {
                    int idx = Integer.parseInt(input.substring(5).trim()); // 1-based
                    if (idx < 1 || idx > size) throw new IndexOutOfBoundsException();
                    tasks[idx - 1].markAsDone();
                    printBlock(
                            "Nice! I've marked this task as done:",
                            "  " + tasks[idx - 1].toString().replaceFirst("^\\[", "[") // keep formatting
                    );
                } catch (Exception e) {
                    printBlock("Invalid index for mark.");
                }
                continue;
            }

            if (input.startsWith("unmark ")) {
                try {
                    int idx = Integer.parseInt(input.substring(7).trim()); // 1-based
                    if (idx < 1 || idx > size) throw new IndexOutOfBoundsException();
                    tasks[idx - 1].markAsNotDone();
                    printBlock(
                            "OK, I've marked this task as not done yet:",
                            "  " + tasks[idx - 1].toString()
                    );
                } catch (Exception e) {
                    printBlock("Invalid index for unmark.");
                }
                continue;
            }

            // Level-4: ToDos, Deadlines, Events
            if (input.startsWith("todo ")) {
                String desc = input.substring(5).trim();
                Task t = new Todo(desc);
                tasks[size++] = t;
                printBlock(
                        "Got it. I've added this task:",
                        "  " + t.toString(),
                        "Now you have " + size + " tasks in the list."
                );
                continue;
            }

            if (input.startsWith("deadline ")) {
                String body = input.substring(9).trim();
                // split around /by
                String[] parts = body.split("\\s+/by\\s+", 2);
                String desc = parts[0];
                String by = parts.length > 1 ? parts[1] : "";
                Task t = new Deadline(desc, by);
                tasks[size++] = t;
                printBlock(
                        "Got it. I've added this task:",
                        "  " + t.toString(),
                        "Now you have " + size + " tasks in the list."
                );
                continue;
            }

            if (input.startsWith("event ")) {
                String body = input.substring(6).trim();
                // split around /from and /to
                String[] p1 = body.split("\\s+/from\\s+", 2);
                String desc = p1[0];
                String from = "", to = "";
                if (p1.length > 1) {
                    String[] p2 = p1[1].split("\\s+/to\\s+", 2);
                    from = p2[0];
                    to = (p2.length > 1) ? p2[1] : "";
                }
                Task t = new Event(desc, from, to);
                tasks[size++] = t;
                printBlock(
                        "Got it. I've added this task:",
                        "  " + t.toString(),
                        "Now you have " + size + " tasks in the list."
                );
                continue;
            }

            // Fallback (optional): If user types plain text (pre-Level-4 style), treat it as a simple Todo
            if (!input.isEmpty()) {
                Task t = new Todo(input);
                tasks[size++] = t;
                printBlock(
                        "Got it. I've added this task:",
                        "  " + t.toString(),
                        "Now you have " + size + " tasks in the list."
                );
            }
        }
    }
}
