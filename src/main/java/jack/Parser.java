package jack;

import jack.util.NaturalDates;
import java.time.Clock;
import java.time.LocalDate;
import java.util.regex.Pattern;

import jack.error.EmptyDescriptionException;
import jack.error.InvalidIndexException;
import jack.error.JackException;
import jack.error.MissingArgumentException;
import jack.model.Deadline;
import jack.model.Event;
import jack.model.Storage;
import jack.model.Task;
import jack.model.TaskList;
import jack.model.Todo;
import jack.ui.Ui;

/**
 * Parses raw user input and executes the corresponding commands.
 * <p>
 * Provides small parsing helpers (splitting tokens, validating indices, parsing dates)
 * and a single entry point that dispatches a command to the application core.
 */
public class Parser {
    private static final Pattern BY_TOKEN   = Pattern.compile("\\s+/by\\s+");
    private static final Pattern FROM_TOKEN = Pattern.compile("\\s+/from\\s+");
    private static final Pattern TO_TOKEN   = Pattern.compile("\\s+/to\\s+");
    private static String[] splitOnce(String input) {
        assert input != null : "splitOnce: input must not be null";
        String t = input.trim();
        int sp = t.indexOf(' ');
        if (sp < 0) {
            return new String[]{t.toLowerCase(), ""};
        }
        String[] out = new String[]{t.substring(0, sp).toLowerCase(), t.substring(sp + 1)};
        assert out[0] != null && out[1] != null : "splitOnce must not yield null parts";
        return out;
    }

    /**
     * Parses a 1-based list index from user input and validates bounds.
     *
     * @param str      the raw index string
     * @param action name of the action for error messages (e.g., {@code "delete"})
     * @param size   current number of tasks in the list
     * @return the parsed 1-based index
     * @throws InvalidIndexException if the index is not a valid number or is out of bounds
     */
    private static int parseIndex(String str, String action, int size) throws JackException {
        assert action != null && !action.isBlank() : "parseIndex: action label must be non-blank";
        assert size >= 0 : "parseIndex: size must be non-negative";
        try {
            int idx = Integer.parseInt(str.trim());
            if (idx < 1 || idx > size) {
                throw new InvalidIndexException(action);
            }
            return idx;
        } catch (NumberFormatException e) {
            throw new InvalidIndexException(action);
        }
    }

    /**
     * Ensures that the given argument is non-null and non-blank.
     *
     * @param s    raw argument string
     * @param what name of the argument for error messages
     * @return trimmed argument string
     * @throws EmptyDescriptionException if the argument is null or blank
     */
    private static String need(String s, String what) throws JackException {
        assert what != null && !what.isBlank() : "need: 'what' label must be provided";
        if (s == null || s.trim().isEmpty()) {
            throw new EmptyDescriptionException(what);
        }
        return s.trim();
    }


    /**
     * Parses a date string, supporting both ISO formats and natural dates.
     *
     * @param s date string to parse
     * @return parsed {@link LocalDate}
     * @throws MissingArgumentException if the string is not a valid date
     */
    private static LocalDate parseNaturalDate(String s) throws JackException {
        final String input = need(s, "deadline date");
        try {
            LocalDate today = LocalDate.now(Clock.systemDefaultZone());
            return NaturalDates.parse(input, today);
        } catch (IllegalArgumentException e) {
            throw new MissingArgumentException(
                    "a valid date (e.g., 2025-10-01, 1 Oct 2025, today, next Mon, in 2 days)"
            );
        }
    }

    private static void persist(TaskList tasks, Storage storage) {
        try {
            storage.save(tasks.raw());
        } catch (Exception ignored) {
            // Intentionally ignored because saving failure is non-critical
        }
    }

    private static void confirmAdd(Ui ui, TaskList tasks, Task t) {
        ui.showBlock("Got it. I've added this task:", "  " + t,
                "Now you have " + tasks.size() + " tasks in the list.");
    }

    /**
     * Parses and executes a single user command.
     *
     * @param fullCommand full input line from the user (e.g., {@code "todo read book"})
     * @param tasks       the in-memory task list to read and modify
     * @param ui          UI helper for producing user-facing messages
     * @param storage     persistent storage used to save task list updates
     * @return {@code true} if the command requests program termination (e.g., {@code "bye"}); {@code false} otherwise
     * @throws JackException if the command is unknown or required arguments are missing/invalid
     */
    public static boolean dispatch(String fullCommand, TaskList tasks, Ui ui, Storage storage) throws JackException {
        assert tasks != null : "dispatch: tasks must not be null";
        assert ui != null : "dispatch: ui must not be null";
        assert storage != null : "dispatch: storage must not be null";
        assert fullCommand != null : "dispatch: fullCommand must not be null";

        String[] parts = splitOnce(fullCommand);
        String cmd = parts[0];
        String args = parts[1];

        switch (cmd) {
        case "list":
            ui.showList(tasks);
            return false;
        case "bye":
            ui.showExit();
            return true;

        case "mark": {
            int idx = parseIndex(args, "mark", tasks.size());
            tasks.get(idx - 1).markAsDone();
            try {
                storage.save(tasks.raw());
            } catch (Exception ignored) {
                // Intentionally ignored because saving failure is non-critical
            }
            ui.showBlock("Nice! I've marked this task as done:", "  " + tasks.get(idx - 1));
            return false;
        }
        case "unmark": {
            int idx = parseIndex(args, "unmark", tasks.size());
            tasks.get(idx - 1).markAsNotDone();
            persist(tasks, storage);
            ui.showBlock("OK, I've marked this task as not done yet:", "  " + tasks.get(idx - 1));
            return false;
        }
        case "delete": {
            int idx = parseIndex(args, "delete", tasks.size());
            Task removed = tasks.remove(idx - 1);
            persist(tasks, storage);
            ui.showBlock("Noted. I've removed this task:",
                    "  " + removed,
                    "Now you have " + tasks.size() + " tasks in the list.");
            return false;
        }
        case "todo": {
            if (args.isEmpty()) {
                throw new EmptyDescriptionException("todo");
            }
            Task t = new Todo(need(args, "todo"));
            tasks.add(t);
            persist(tasks, storage);
            confirmAdd(ui, tasks, t);
            return false;
        }
        case "deadline": {
            if (args.isEmpty()) {
                throw new EmptyDescriptionException("deadline");
            }
            String[] p = BY_TOKEN.split(need(args, "deadline"), 2);
            if (p.length < 2) {
                throw new MissingArgumentException("/by <yyyy-MM-dd>");
            }
            Task t = new Deadline(need(p[0], "deadline"), parseNaturalDate(p[1]));
            tasks.add(t);
            persist(tasks, storage);
            confirmAdd(ui, tasks, t);
            return false;
        }
        case "event": {
            if (args.isEmpty()) {
                throw new EmptyDescriptionException("event");
            }
            String body = need(args, "event");
            final String[] p1 = FROM_TOKEN.split(body, 2);
            if (p1.length < 2) {
                throw new MissingArgumentException("/from <start>");
            }
            final String[] p2 = TO_TOKEN.split(p1[1], 2);
            if (p2.length < 2) {
                throw new MissingArgumentException("/to <end>");
            }
            Task t = new Event(need(p1[0], "event"), need(p2[0], "/from <start>"), need(p2[1], "/to <end>"));
            tasks.add(t);
            persist(tasks, storage);
            confirmAdd(ui, tasks, t);
            return false;
        }
        case "find": {
            String keyword = need(args, "search keyword");
            String result = tasks.findTasks(keyword);
            ui.showBlock(result);
            return false;
        }

        default:
            if (!fullCommand.trim().isEmpty()) {
                throw new JackException("I don't recognise the command: \"" + fullCommand.trim() + "\"");
            }
            return false;
        }
    }
}
