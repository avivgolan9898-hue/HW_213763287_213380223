package utils;

import java.util.Scanner;

/**
 * Utility class for reading validated input from Scanner. Provides methods to
 * read integers, doubles, and non-empty strings with prompts and validation.
 */
public final class ScannerUtils {
	private ScannerUtils() {
		// Prevent object creation
	}

	/**
	 * Reads an integer from the scanner with a prompt. Continues to prompt until a
	 * valid integer is entered.
	 *
	 * @param scanner The Scanner to read input from.
	 * @param prompt  The prompt message to display to the user.
	 * @return The integer value entered by the user.
	 */
	public static int readInt(Scanner scanner, String prompt) {
		while (true) {
			System.out.print(prompt);
			if (scanner.hasNextInt()) {
				int value = scanner.nextInt();
				scanner.nextLine();
				return value;
			}
			ConsoleUI.warning("Invalid input, try again.");
			scanner.nextLine();
		}
	}

	/**
	 * Reads a non-negative integer from the scanner with a prompt. Continues to
	 * prompt until a valid non-negative integer is entered.
	 *
	 * @param scanner The Scanner to read input from.
	 * @param prompt  The prompt message to display to the user.
	 * @return The non-negative integer value entered by the user.
	 */
	public static int readNonNegativeInt(Scanner scanner, String prompt) {
		while (true) {
			int value = readInt(scanner, prompt);
			if (value >= 0) {
				return value;
			}
			System.out.println("Value must be non-negative, try again .");
		}
	}

	/**
	 * Reads a double from the scanner with a prompt. Continues to prompt until a
	 * valid double is entered.
	 *
	 * @param scanner The Scanner to read input from.
	 * @param prompt  The prompt message to display to the user.
	 * @return The double value entered by the user.
	 */
	public static double readNonNegativeDouble(Scanner scanner, String prompt) {
		while (true) {
			System.out.print(prompt);

			if (scanner.hasNextDouble()) {
				double value = scanner.nextDouble();
				scanner.nextLine();

				if (value >= 0) {
					return value;
				}

				System.out.println("Value must be non-negative.");
			} else {
				System.out.println("Invalid number, Try again.");
				scanner.nextLine();
			}
		}
	}

	/**
	 * Reads a double from the scanner with a prompt and ensures it is within the
	 * specified range. Continues to prompt until a valid double within the range is
	 * entered.
	 *
	 * @param scanner The Scanner to read input from.
	 * @param prompt  The prompt message to display to the user.
	 * @param min     The minimum acceptable value (inclusive).
	 * @param max     The maximum acceptable value (inclusive).
	 * @return The double value entered by the user that is within the specified
	 *         range.
	 */
	public static double readDoubleInRange(Scanner scanner, String prompt, double min, double max) {
		while (true) {
			double value = readNonNegativeDouble(scanner, prompt);
			if (value >= min && value <= max) {
				return value;
			}
			System.out.println("Value must be between " + min + " and " + max + ".");
		}
	}

	/**
	 * Reads a non-empty string from the scanner with a prompt. Continues to prompt
	 * until a non-empty string is entered.
	 * 
	 * @param scanner The Scanner to read input from.
	 * @param prompt  The prompt message to display to the user.
	 * @return
	 */
	public static String readNonEmptyString(Scanner scanner, String prompt) {
		while (true) {
			System.out.print(prompt);
			if (!scanner.hasNextLine()) {
				continue;
			}
			String value = scanner.nextLine();
			if (!StringValidationUtils.isNullOrEmpty(value)) {
				return value.trim();
			}
			System.out.println("Input cannot be empty.");
		}
	}
}
