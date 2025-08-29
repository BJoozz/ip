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

    private static int parseIndex(String s, String action, int size) throws QB7Exception {
        try {
            int idx = Integer.parseInt(s.trim());
            if (idx < 1 || idx > size) throw new InvalidIndexException(action);
            return idx;
        } catch (NumberFormatException e) {
            throw new InvalidIndexException(action);
        }
    }

    private static String need(String s, String what) throws QB7Exception {
        if (s == null || s.trim().isEmpty()) throw new EmptyDescriptionException(what);
        return s.trim();
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
            String line = sc.nextLine().trim();

            if (line.equals("bye")) {
                printBlock("Bye. Hope to see you again soon!");
                break;
            }

            try {
                // list
                if (line.equals("list")) {
                    System.out.println(LINE);
                    System.out.println(" Here are the tasks in your list:");
                    for (int i = 0; i < size; i++) {
                        System.out.println(" " + (i + 1) + "." + tasks[i]);
                    }
                    System.out.println(LINE);
                    continue;
                }

                // Level-3: mark / unmark (validate index)
                if (line.startsWith("mark ")) {
                    int idx = parseIndex(line.substring(5), "mark", size);
                    tasks[idx - 1].markAsDone();
                    printBlock("Nice! I've marked this task as done:", "  " + tasks[idx - 1]);
                    continue;
                }
                if (line.startsWith("unmark ")) {
                    int idx = parseIndex(line.substring(7), "unmark", size);
                    tasks[idx - 1].markAsNotDone();
                    printBlock("OK, I've marked this task as not done yet:", "  " + tasks[idx - 1]);
                    continue;
                }

                // Level-4: todo
                if (line.equals("todo")) throw new EmptyDescriptionException("todo");
                if (line.startsWith("todo ")) {
                    String desc = need(line.substring(5), "todo");
                    Task t = new Todo(desc);
                    tasks[size++] = t;
                    printBlock("Got it. I've added this task:", "  " + t, "Now you have " + size + " tasks in the list.");
                    continue;
                }

                // Level-4: deadline
                if (line.equals("deadline")) throw new EmptyDescriptionException("deadline");
                if (line.startsWith("deadline ")) {
                    String body = need(line.substring(9), "deadline");
                    String[] parts = body.split("\\s+/by\\s+", 2);
                    if (parts.length < 2) throw new MissingArgumentException("/by <time>");
                    String desc = need(parts[0], "deadline");
                    String by = need(parts[1], "/by <time>");
                    Task t = new Deadline(desc, by);
                    tasks[size++] = t;
                    printBlock("Got it. I've added this task:", "  " + t, "Now you have " + size + " tasks in the list.");
                    continue;
                }

                // Level-4: event
                if (line.equals("event")) throw new EmptyDescriptionException("event");
                if (line.startsWith("event ")) {
                    String body = need(line.substring(6), "event");
                    String[] p1 = body.split("\\s+/from\\s+", 2);
                    if (p1.length < 2) throw new MissingArgumentException("/from <start>");
                    String desc = need(p1[0], "event");
                    String[] p2 = p1[1].split("\\s+/to\\s+", 2);
                    if (p2.length < 2) throw new MissingArgumentException("/to <end>");
                    String from = need(p2[0], "/from <start>");
                    String to = need(p2[1], "/to <end>");
                    Task t = new Event(desc, from, to);
                    tasks[size++] = t;
                    printBlock("Got it. I've added this task:", "  " + t, "Now you have " + size + " tasks in the list.");
                    continue;
                }

                // Unknown command (Level-5 minimal)

            } catch (QB7Exception e) {
                // Friendly, specific error message block
                printBlock("Uh oh! " + e.getMessage());
            } catch (Exception e) {
                // Safety net for unexpected bugs
                printBlock("Something went wrong internally: " + e.getClass().getSimpleName());
            }
        }
    }
}
