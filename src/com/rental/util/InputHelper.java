package com.rental.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * Utility class for safe, validated console input.
 * All methods prompt and re-prompt until valid input is received.
 */
public class InputHelper {

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final DateTimeFormatter DATE_FMT =
        DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private InputHelper() {}

    /** Read a non-blank string from stdin. */
    public static String readString(String prompt) {
        String val = "";
        while (val.isBlank()) {
            System.out.print(prompt);
            val = SCANNER.nextLine().trim();
            if (val.isBlank()) System.out.println("  Input cannot be empty. Try again.");
        }
        return val;
    }

    /** Read an integer within an inclusive [min, max] range. */
    public static int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                int val = Integer.parseInt(SCANNER.nextLine().trim());
                if (val >= min && val <= max) return val;
                System.out.printf("  Please enter a number between %d and %d.%n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("  Invalid number. Try again.");
            }
        }
    }

    /** Read a positive double (price, etc.). */
    public static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                double val = Double.parseDouble(SCANNER.nextLine().trim());
                if (val > 0) return val;
                System.out.println("  Value must be positive.");
            } catch (NumberFormatException e) {
                System.out.println("  Invalid number. Try again.");
            }
        }
    }

    /**
     * Read a LocalDate in yyyy-MM-dd format.
     * Optionally enforces that the date is not before a minimum date.
     */
    public static LocalDate readDate(String prompt, LocalDate minDate) {
        while (true) {
            System.out.print(prompt);
            String raw = SCANNER.nextLine().trim();
            try {
                LocalDate date = LocalDate.parse(raw, DATE_FMT);
                if (minDate != null && date.isBefore(minDate)) {
                    System.out.println("  Date cannot be before " + minDate + ". Try again.");
                    continue;
                }
                return date;
            } catch (DateTimeParseException e) {
                System.out.println("  Invalid date format. Use yyyy-MM-dd (e.g. 2025-07-15).");
            }
        }
    }

    /** Read a raw line (used for optional fields where blank is acceptable). */
    public static String readOptional(String prompt) {
        System.out.print(prompt);
        return SCANNER.nextLine().trim();
    }

    /** Pause until the user presses Enter. */
    public static void pressEnterToContinue() {
        System.out.print("\n  Press ENTER to continue...");
        SCANNER.nextLine();
    }

    public static Scanner getScanner() { return SCANNER; }
}
