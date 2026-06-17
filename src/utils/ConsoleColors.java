package utils;

/**
 * This interface defines ANSI escape codes for coloring console output. It
 * includes codes for resetting colors, regular colors (red, green, yellow,
 * blue), and bold variants of red and green.
 */
public interface ConsoleColors {
	// Reset
	String RESET = "\u001B[0m";

	// Regular Colors
	String RED = "\u001B[31m";
	String GREEN = "\u001B[32m";
	String YELLOW = "\u001B[33m";
	String BLUE = "\u001B[34m";

	// Light Colors
	String LIGHT_BLUE = "\u001B[94m";
	String LIGHT_CYAN = "\u001B[96m";

	// Bold
    String BOLD = "\u001B[1m";
	String BOLD_RED = "\u001B[1;31m";
	String BOLD_GREEN = "\u001B[1;32m";
	String BOLD_YELLOW = "\u001B[1;33m";
    String BOLD_BLUE = "\u001B[1;34m";
}
