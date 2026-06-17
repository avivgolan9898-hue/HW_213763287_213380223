package utils;

/**
 * Utility class for printing styled console messages and menus.
 */
public final class ConsoleUI {

	private static final int MENU_WIDTH = 50;

	private ConsoleUI() {
		// Prevent object creation
	}

	public static void printWelcomeMessage() {
		System.out.println(ConsoleColors.BOLD_GREEN);
		System.out.println("====================================================");
		System.out.println("            Food Delivery System Started       ");
		System.out.println("====================================================");
		System.out.print(ConsoleColors.RESET);
	}

	public static void printExitMessage() {
		System.out.println(ConsoleColors.BOLD_YELLOW + "\nExiting Food Delivery System. Goodbye!" + ConsoleColors.RESET);
	}

	public static void printMenu(String title, String[] options) {
		System.out.println(ConsoleColors.LIGHT_BLUE);
		printTopBorder();
		printCenteredLine(title);
		printMiddleBorder();

		for (String option : options) {
			printOptionLine(option);
		}

		printBottomBorder();
		System.out.print(ConsoleColors.RESET);
	}

	public static void success(String message) {
		System.out.println(ConsoleColors.BOLD_GREEN + "[SUCCESS] " + message + ConsoleColors.RESET);
	}

	public static void error(String message) {
		System.out.println(ConsoleColors.BOLD_RED + "[ERROR] " + message + ConsoleColors.RESET);
	}

	public static void warning(String message) {
		System.out.println(ConsoleColors.BOLD_YELLOW + "[WARNING] " + message + ConsoleColors.RESET);
	}

	public static void info(String message) {
		System.out.println(ConsoleColors.LIGHT_CYAN + "[INFO] " + message + ConsoleColors.RESET);
	}

	public static void invalidChoice() {
		error("Invalid choice, please try again.");
	}

	public static void printObject(Object object) {
		if (object == null) {
			error("No data found.");
		} else {
			System.out.println(object);
		}
	}

	public static void printEmptyLine() {
		System.out.println();
	}

	private static void printTopBorder() {
		System.out.println("|" + "=".repeat(MENU_WIDTH) + "|");
	}

	private static void printMiddleBorder() {
		System.out.println("|" + "=".repeat(MENU_WIDTH) + "|");
	}

	private static void printBottomBorder() {
		System.out.println("|" + "=".repeat(MENU_WIDTH) + "|");
	}

	private static void printCenteredLine(String text) {
		if (text.length() > MENU_WIDTH) {
			text = text.substring(0, MENU_WIDTH);
		}

		int totalPadding = MENU_WIDTH - text.length();
		int leftPadding = totalPadding / 2;
		int rightPadding = totalPadding - leftPadding;

		System.out.println("|" + " ".repeat(leftPadding) + text + " ".repeat(rightPadding) + "|");
	}

	private static void printOptionLine(String text) {
		if (text.length() > MENU_WIDTH - 2) {
			text = text.substring(0, MENU_WIDTH - 5) + "...";
		}

		System.out.printf("|  %-" + (MENU_WIDTH - 2) + "s|%n", text);
	}
}