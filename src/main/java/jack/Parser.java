package jack;

import jack.error.EmptyDescriptionException;
import jack.error.InvalidIndexException;
import jack.error.JackException;
import jack.error.MissingArgumentException;
import jack.model.*;
import jack.ui.Ui;

import java.time.LocalDate;

/**
 * Parses raw user input and executes the corresponding commands.
 * <p>
 * Provides small parsing helpers (splitting tokens, validating indices, parsing dates)
 * and a single entry point that dispatches a command to the application core.
 */
public class Parser {
    private static String[] splitOnce(String input) {
        String t = input.trim(); int sp = t.indexOf(' ');
        if (sp < 0) return new String[]{t.toLowerCase(), ""};
        return new String[]{t.substring(0, sp).toLowerCase(), t.substring(sp + 1)};
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
        try {
            int idx = Integer.parseInt(str.trim());
            if (idx < 1 || idx > size) throw new InvalidIndexException(action);
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
        if (s == null || s.trim().isEmpty()) throw new EmptyDescriptionException(what);
        return s.trim();
    }

    /**
     * Parses a date in ISO-8601 format ({@code yyyy-MM-dd}).
     *
     * @param s date string to parse
     * @return parsed {@link LocalDate}
     * @throws MissingArgumentException if the string is not a valid ISO date
     */

    private static LocalDate parseIsoDate(String s) throws JackException {
        String trimmed = need(s, "deadline date").trim();
        try { return LocalDate.parse(trimmed); }
        catch (java.time.format.DateTimeParseException e) {
            throw new MissingArgumentException("a valid date in yyyy-MM-dd (e.g., 2019-10-15)");
        }
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
        String[] parts = splitOnce(fullCommand);
        String cmd = parts[0], args = parts[1];

        switch (cmd) {
            case "list": ui.showList(tasks); return false;
            case "bye":  ui.showExit(); return true;

            case "mark": {
                int idx = parseIndex(args, "mark", tasks.size());
                tasks.get(idx - 1).markAsDone();
                try { storage.save(tasks.raw()); } catch (Exception ignored) {}
                ui.showBlock("Nice! I've marked this task as done:", "  " + tasks.get(idx - 1));
                return false;
            }
            case "unmark": {
                int idx = parseIndex(args, "unmark", tasks.size());
                tasks.get(idx - 1).markAsNotDone();
                try { storage.save(tasks.raw()); } catch (Exception ignored) {}
                ui.showBlock("OK, I've marked this task as not done yet:", "  " + tasks.get(idx - 1));
                return false;
            }
            case "delete": {
                int idx = parseIndex(args, "delete", tasks.size());
                Task removed = tasks.remove(idx - 1);
                try { storage.save(tasks.raw()); } catch (Exception ignored) {}
                ui.showBlock("Noted. I've removed this task:",
                        "  " + removed,
                        "Now you have " + tasks.size() + " tasks in the list.");
                return false;
            }
            case "todo": {
                if (args.isEmpty()) throw new EmptyDescriptionException("todo");
                Task t = new Todo(need(args, "todo"));
                tasks.add(t);
                try { storage.save(tasks.raw()); } catch (Exception ignored) {}
                ui.showBlock("Got it. I've added this task:", "  " + t,
                        "Now you have " + tasks.size() + " tasks in the list.");
                return false;
            }
            case "deadline": {
                if (args.isEmpty()) throw new EmptyDescriptionException("deadline");
                String[] p = need(args, "deadline").split("\\s+/by\\s+", 2);
                if (p.length < 2) throw new MissingArgumentException("/by <yyyy-MM-dd>");
                Task t = new Deadline(need(p[0], "deadline"), parseIsoDate(p[1]));
                tasks.add(t);
                try { storage.save(tasks.raw()); } catch (Exception ignored) {}
                ui.showBlock("Got it. I've added this task:", "  " + t,
                        "Now you have " + tasks.size() + " tasks in the list.");
                return false;
            }
            case "event": {
                if (args.isEmpty()) throw new EmptyDescriptionException("event");
                String body = need(args, "event");
                String[] p1 = body.split("\\s+/from\\s+", 2);
                if (p1.length < 2) throw new MissingArgumentException("/from <start>");
                String[] p2 = p1[1].split("\\s+/to\\s+", 2);
                if (p2.length < 2) throw new MissingArgumentException("/to <end>");
                Task t = new Event(need(p1[0], "event"), need(p2[0], "/from <start>"), need(p2[1], "/to <end>"));
                tasks.add(t);
                try { storage.save(tasks.raw()); } catch (Exception ignored) {}
                ui.showBlock("Got it. I've added this task:", "  " + t,
                        "Now you have " + tasks.size() + " tasks in the list.");
                return false;
            }

            default:
                if (!fullCommand.trim().isEmpty())
                    throw new JackException("I don't recognise the command: \"" + fullCommand.trim() + "\"");
                return false;
        }
    }
}
