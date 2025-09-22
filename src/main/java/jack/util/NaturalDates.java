package jack.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

/**
 * Utility for parsing lightweight "natural language" date expressions
 * (e.g., "today", "next Mon", "in 2 weeks") into {@link LocalDate}.
 * <p>
 * Supports both natural keywords and a few common date formats, without
 * using any external libraries.
 */public final class NaturalDates {
    private NaturalDates() {}

    /**
     * Supported explicit date formats (in addition to keywords).
     * Examples:
     * - 2025-10-01
     * - 1/10/2025
     * - 1-10-2025
     * - 1 Oct 2025
     * - Oct 1 2025
     */
    private static final DateTimeFormatter[] PATTERNS = new DateTimeFormatter[] {
            DateTimeFormatter.ISO_LOCAL_DATE, // 2025-10-01
            DateTimeFormatter.ofPattern("d/M/uuuu"), // 1/10/2025
            DateTimeFormatter.ofPattern("d-M-uuuu"), // 1-10-2025
            DateTimeFormatter.ofPattern("d MMM uuuu", Locale.ENGLISH), // 1 Oct 2025
            DateTimeFormatter.ofPattern("MMM d uuuu", Locale.ENGLISH) // Oct 1 2025
    };

    /**
     * Parses a natural-language or formatted date string relative to a given "today".
     *
     * <p>Examples:
     * <ul>
     *   <li>"today" → {@code today}</li>
     *   <li>"tomorrow" → {@code today.plusDays(1)}</li>
     *   <li>"yesterday" → {@code today.minusDays(1)}</li>
     *   <li>"in 3 days" → {@code today.plusDays(3)}</li>
     *   <li>"in 2 weeks" → {@code today.plusWeeks(2)}</li>
     *   <li>"next Monday" → next Monday strictly after today</li>
     *   <li>"Mon" → next Monday strictly after today</li>
     *   <li>"2025-10-01" → parsed using ISO format</li>
     * </ul>
     *
     * @param raw   the input string (must not be null or empty)
     * @param today the base date to interpret relative expressions
     * @return parsed {@link LocalDate}
     * @throws IllegalArgumentException if input is null, empty, or unrecognized
     */
    public static LocalDate parse(String raw, LocalDate today) {
        if (raw == null) throw new IllegalArgumentException("date string is null");
        String s = raw.trim().toLowerCase(Locale.ENGLISH);
        if (s.isEmpty()) throw new IllegalArgumentException("date string is empty");

        // Keywords
        switch (s) {
        case "today": return today;
        case "tomorrow": return today.plusDays(1);
        case "yesterday": return today.minusDays(1);
        }

        // "in N days/weeks"
        if (s.startsWith("in ")) {
            String[] parts = s.split("\\s+");
            if (parts.length == 3) {
                int n = Integer.parseInt(parts[1]);
                if (parts[2].startsWith("day")) return today.plusDays(n);
                if (parts[2].startsWith("week")) return today.plusWeeks(n);
            }
        }

        // "next mon", "next monday"
        if (s.startsWith("next ")) {
            DayOfWeek dow = parseDayOfWeek(s.substring(5));
            if (dow != null) return next(dow, today);
        }

        // bare weekday: "mon", "monday" => next occurrence (excluding today)
        DayOfWeek maybeDow = parseDayOfWeek(s);
        if (maybeDow != null) return next(maybeDow, today);

        // Try explicit date patterns
        for (DateTimeFormatter f : PATTERNS) {
            try {
                return LocalDate.parse(raw.trim(), f);
            } catch (DateTimeParseException ignored) {
                // try next
            }
        }

        throw new IllegalArgumentException("Unrecognized date: " + raw);
    }

    private static DayOfWeek parseDayOfWeek(String s) {
        String t = s.trim().toLowerCase(Locale.ENGLISH);
        if (t.length() >= 3) t = t.substring(0, 3);
        switch (t) {
        case "mon": return DayOfWeek.MONDAY;
        case "tue": return DayOfWeek.TUESDAY;
        case "wed": return DayOfWeek.WEDNESDAY;
        case "thu": return DayOfWeek.THURSDAY;
        case "fri": return DayOfWeek.FRIDAY;
        case "sat": return DayOfWeek.SATURDAY;
        case "sun": return DayOfWeek.SUNDAY;
        default: return null;
        }
    }

    /**
     * Returns the next occurrence of the given day of week strictly after {@code from}.
     * <p>
     * If {@code from} is already the given day, returns 7 days later.
     *
     * @param target the desired day of week
     * @param from   the base date
     * @return the next matching date
     */
    private static LocalDate next(DayOfWeek target, LocalDate from) {
        int add = (target.getValue() - from.getDayOfWeek().getValue() + 7) % 7;
        if (add == 0) add = 7; // "next Mon" means next week if today is Mon
        return from.plusDays(add);
    }
}
