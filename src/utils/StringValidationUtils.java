package utils;

/**
 * Utility class for validating strings with various criteria
 */
public final class StringValidationUtils {

	private StringValidationUtils() {
		// Prevent object creation
	}

	/**
	 * Checks if a string is null or empty (after trimming).
	 * 
	 * @param str The string to check.
	 * @return true if the string is null or empty, false otherwise.
	 */
	public static boolean isNullOrEmpty(String str) {
		return str == null || str.trim().isEmpty();
	}

	/**
	 * Checks if a string exceeds a specified maximum length.
	 *
	 * @param str       The string to check.
	 * @param maxLength The maximum allowed length.
	 * @return true if the string is not null and exceeds the max length, false
	 *         otherwise.
	 */
	public static boolean isStringTooLong(String str, int maxLength) {
		return str != null && str.length() > maxLength;
	}

	/**
	 * Checks if a string is shorter than a specified minimum length.
	 *
	 * @param str       The string to check.
	 * @param minLength The minimum required length.
	 * @return true if the string is not null and shorter than the min length, false
	 *         otherwise.
	 */
	public static boolean isStringTooShort(String str, int minLength) {
		return str != null && str.length() < minLength;
	}

	/**
	 * Validates if the provided email string is in a valid email format.
	 *
	 * @param email The email string to validate.
	 * @return true if the email is valid, false otherwise.
	 */
	public static boolean isValidEmail(String email) {
		if (isNullOrEmpty(email)) {
			return false;
		}

		int atIndex = email.indexOf('@');

		// Must contain exactly one '@'
		if (atIndex == -1 || atIndex != email.lastIndexOf('@')) {
			return false;
		}

		// '@' cannot be first or last
		if (atIndex == 0 || atIndex == email.length() - 1) {
			return false;
		}

		String localPart = email.substring(0, atIndex);
		String domainPart = email.substring(atIndex + 1);

		// Domain should contain at least one dot
		if (!domainPart.contains(".")) {
			return false;
		}

		// Dot cannot be first or last in domain
		if (domainPart.startsWith(".") || domainPart.endsWith(".")) {
			return false;
		}

		// Check allowed characters
		for (int i = 0; i < localPart.length(); i++) {
			char ch = localPart.charAt(i);

			if (!Character.isLetterOrDigit(ch) && ch != '+' && ch != '_' && ch != '.' && ch != '-') {
				return false;
			}
		}

		for (int i = 0; i < domainPart.length(); i++) {
			char ch = domainPart.charAt(i);

			if (!Character.isLetterOrDigit(ch) && ch != '.' && ch != '-') {
				return false;
			}
		}

		return true;
	}

	/**
	 * Checks if a string contains only numeric characters.
	 * 
	 * @param str The string to check.
	 * @return true if the string is not null, not empty, and contains only digits;
	 *         false otherwise.
	 */
	public static boolean isStringOnlyNumeric(String str) {
		if (isNullOrEmpty(str)) {
			return false;
		}
		for (char c : str.toCharArray()) {
			if (!Character.isDigit(c)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks if a string contains only alphabetic characters.
	 *
	 * @param str The string to check.
	 * @return true if the string is not null, not empty, and contains only letters;
	 *         false otherwise.
	 */
	public static boolean isStringOnlyAlphabetic(String str) {
		if (isNullOrEmpty(str)) {
			return false;
		}
		for (char c : str.toCharArray()) {
			if (!Character.isLetter(c)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks if a string contains only alphanumeric characters (letters and
	 * digits).
	 *
	 * @param str The string to check.
	 * @return true if the string is not null, not empty, and contains only letters
	 *         and digits; false otherwise.
	 */
	public static boolean isStringOnlyAlphanumeric(String str) {
		if (isNullOrEmpty(str)) {
			return false;
		}
		for (char c : str.toCharArray()) {
			if (!Character.isLetterOrDigit(c)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks if a string contains only alphabetic characters and spaces, with a
	 * limit on the number of spaces.
	 *
	 * @param str              The string to check.
	 * @param number_of_spaces The maximum allowed number of spaces in the string.
	 * @return true if the string is not null, not empty, contains only letters and
	 *         spaces, and has at most the specified number of spaces; false
	 *         otherwise.
	 */
	public static boolean isStringOnlyAlphabeticWithSpaces(String str, int number_of_spaces) {
		if (isNullOrEmpty(str)) {
			return false;
		}
		int spaceCount = 0;
		for (char c : str.toCharArray()) {
			if (Character.isSpaceChar(c)) {
				spaceCount++;
			} else if (!Character.isLetter(c)) {
				return false;
			}
		}
		return spaceCount <= number_of_spaces;
	}

	/**
	 * Checks if a string contains only alphanumeric characters and spaces, with a
	 * limit on the number of spaces.
	 *
	 * @param str              The string to check.
	 * @param number_of_spaces The maximum allowed number of spaces in the string.
	 * @return true if the string is not null, not empty, contains only letters,
	 *         digits, and spaces, and has at most the specified number of spaces;
	 *         false otherwise.
	 */
	public static boolean isStringOnlyAlphanumericWithSpaces(String str, int number_of_spaces) {
		if (isNullOrEmpty(str)) {
			return false;
		}
		int spaceCount = 0;
		for (char c : str.toCharArray()) {
			if (Character.isSpaceChar(c)) {
				spaceCount++;
			} else if (!Character.isLetterOrDigit(c)) {
				return false;
			}
		}
		return spaceCount <= number_of_spaces;
	}

	/**
	 * Validates if the provided phone number string is in a valid format. A valid
	 * phone number may contain digits, spaces, dashes, and an optional leading '+'.
	 *
	 * @param phoneNumber The phone number string to validate.
	 * @return true if the phone number is valid, false otherwise.
	 */
	public static boolean isValidPhoneNumber(String phoneNumber) {
		if (isNullOrEmpty(phoneNumber)) {
			return false;
		}
		
		if (phoneNumber.length() < 7 || phoneNumber.length() > 15) {
			ConsoleUI.error("Phone number must be between 7 and 15 characters long.");
			return false; // Basic length check for phone numbers
		}

		int digitCount = 0;

		for (int i = 0; i < phoneNumber.length(); i++) {
			char c = phoneNumber.charAt(i);

			if (Character.isDigit(c)) {
				digitCount++;
			} else if (c == '+') {
				// '+' is allowed only as the first character
				if (i != 0) {
					return false;
				}
			} else if (c == '-' || Character.isSpaceChar(c)) {
				// allowed
			} else {
				return false;
			}
		}

		return digitCount > 0;
	}

	/**
	 * Checks if a given data is in the format: "DD-MM-YYYY".
	 * 
	 * @param str The string to check.
	 * @return true if the string is a valid date in the format "DD-MM-YYYY", false
	 *         otherwise.
	 */
	public static boolean isValidOrderDate(String str) {
		if (isNullOrEmpty(str)) {
			ConsoleUI.error("Date cannot be null or empty.");
			return false;
		}

		String[] parts = str.split("-");
		if (parts.length != 3) {
			ConsoleUI.error("Date must be in the format DD-MM-YYYY.");
			return false;
		}

		String dayPart = parts[0];
		String monthPart = parts[1];
		String yearPart = parts[2];

		if (!isStringOnlyNumeric(dayPart)) {
			ConsoleUI.error("Day must be numeric.");
			return false;
		}

		if (!isStringOnlyNumeric(monthPart)) {
			ConsoleUI.error("Month must be numeric.");
			return false;
		}

		if (!isStringOnlyNumeric(yearPart)) {
			ConsoleUI.error("Year must be numeric.");
			return false;
		}

		int day = Integer.parseInt(dayPart);
		int month = Integer.parseInt(monthPart);
		int year = Integer.parseInt(yearPart);

		// Basic validation for year, month, and day ranges
		if (year < 1900 || year > 2100) {
			ConsoleUI.error("Year must be between 1900 and 2100.");
			return false;
		}

		if (month < 1 || month > 12) {
			ConsoleUI.error("Month must be between 1 and 12.");
			return false;
		}

		int[] daysInMonth = { 31, (isLeapYear(year) ? 29 : 28), 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

		if (day < 1 || day > daysInMonth[month - 1]) {
			ConsoleUI.error("Day must be between 1 and " + daysInMonth[month - 1] + " for month " + month + ".");
			return false;
		}

		return true;
	}

	/**
	 * Helper method to determine if a given year is a leap year.
	 *
	 * @param year The year to check.
	 * @return true if the year is a leap year, false otherwise.
	 */
	private static boolean isLeapYear(int year) {
		return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
	}

	/**
	 * Checks if the delivered date is before the ordered date.
	 *
	 * @param deliveredDate The delivered date in "DD-MM-YYYY" format.
	 * @param orderedDate   The ordered date in "DD-MM-YYYY" format.
	 * @return true if the delivered date is before the ordered date, false
	 *         otherwise.
	 */
	public static boolean isBefore(String deliveredDate, String orderedDate) {
		String[] deliveredParts = deliveredDate.split("-");
		String[] orderedParts = orderedDate.split("-");

		int deliveredDay = Integer.parseInt(deliveredParts[0]);
		int deliveredMonth = Integer.parseInt(deliveredParts[1]);
		int deliveredYear = Integer.parseInt(deliveredParts[2]);

		int orderedDay = Integer.parseInt(orderedParts[0]);
		int orderedMonth = Integer.parseInt(orderedParts[1]);
		int orderedYear = Integer.parseInt(orderedParts[2]);

		if (deliveredYear < orderedYear) {
			return true;
		} else if (deliveredYear > orderedYear) {
			return false;
		}

		if (deliveredMonth < orderedMonth) {
			return true;
		} else if (deliveredMonth > orderedMonth) {
			return false;
		}

		return deliveredDay < orderedDay;
	}
	
	/**
	 * Validates if the provided password string meets the specified criteria: -
	 * Minimum length of 8 characters - Contains at least one uppercase letter -
	 * Contains at least one lowercase letter - Contains at least one digit -
	 * Contains at least one special character (e.g., !@#$%^&*()-+)
	 *
	 * @param password The password string to validate.
	 * @return true if the password is valid, false otherwise.
	 */
	public static boolean isValidPassword(String password) {
		if (isNullOrEmpty(password)) {
			ConsoleUI.error("Password cannot be null or empty.");
			return false;
		}

		if (password.length() < 8) {
			ConsoleUI.error("Password must be at least 8 characters long.");
			return false;
		}

		boolean hasUppercase = false;
		boolean hasLowercase = false;
		boolean hasDigit = false;
		boolean hasSpecialChar = false;

		for (char c : password.toCharArray()) {
			if (Character.isUpperCase(c)) {
				hasUppercase = true;
			} else if (Character.isLowerCase(c)) {
				hasLowercase = true;
			} else if (Character.isDigit(c)) {
				hasDigit = true;
			} else if ("!@#$%^&*()-+".indexOf(c) >= 0) {
				hasSpecialChar = true;
			}
		}

		if (!hasUppercase) {
			ConsoleUI.error("Password must contain at least one uppercase letter.");
			return false;
		}

		if (!hasLowercase) {
			ConsoleUI.error("Password must contain at least one lowercase letter.");
			return false;
		}

		if (!hasDigit) {
			ConsoleUI.error("Password must contain at least one digit.");
			return false;
		}

		if (!hasSpecialChar) {
			ConsoleUI.error("Password must contain at least one special character (!@#$%^&*()-+).");
			return false;
		}

		return true;
	}
}
