package model;

import utils.StringValidationUtils;

public class Customer implements HasCodeField, Comparable<Customer> {
	private final String code;
	private String firstName;
	private String lastName;
	private String email;
	private String phoneNumber;
	private double balance;

	private String street;
	private String city;
	private String zipCode;

	private static int count = 0; // to count the number of customers created

	public Customer(String firstName, String lastName, String email, String phoneNumber, double balance, String street,
			String city, String zipCode) {
		this.code = generateCustomerCode();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.balance = balance;
		this.street = street;
		this.city = city;
		this.zipCode = zipCode;
	}

	/**
	 * Generates a unique customer code based on the number of customers created.
	 * The code is formatted as "C" followed by a zero-padded number (e.g., C0001,
	 * C0002).
	 * 
	 * @return
	 */
	private String generateCustomerCode() {
		count++;
		return String.format("C%04d", count); // Generates a code like C0001, C0002, etc.
	}

	// Getters

	@Override
	public String getCode() {
		return code;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmail() {
		return email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public double getBalance() {
		return balance;
	}

	public String getStreet() {
		return street;
	}

	public String getCity() {
		return city;
	}

	public String getZipCode() {
		return zipCode;
	}

	public String getAddress() {
		return street + ", " + city + ", " + zipCode;
	}

	// Setters

	public void setFirstName(String firstName) {
		if (StringValidationUtils.isNullOrEmpty(firstName)) {
			System.out.println("First name cannot be null or empty.");
			return;
		}

		if (!StringValidationUtils.isStringOnlyAlphabetic(firstName)) {
			System.out.println("First name must contain only alphabetic characters.");
			return;
		}

		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		if (StringValidationUtils.isNullOrEmpty(lastName)) {
			System.out.println("Last name cannot be null or empty.");
			return;
		}

		if (!StringValidationUtils.isStringOnlyAlphabetic(lastName)) {
			System.out.println("Last name must contain only alphabetic characters.");
			return;
		}

		this.lastName = lastName;
	}

	public void setEmail(String email) {
		if (StringValidationUtils.isNullOrEmpty(email)) {
			System.out.println("Email cannot be null or empty.");
			return;
		}

		if (!StringValidationUtils.isValidEmail(email)) {
			System.out.println("Invalid email format.");
			return;
		}

		this.email = email;
	}

	public void setPhoneNumber(String phoneNumber) {
		if (StringValidationUtils.isNullOrEmpty(phoneNumber)) {
			System.out.println("Phone number cannot be null or empty.");
			return;
		}

		if (!StringValidationUtils.isValidPhoneNumber(phoneNumber)) {
			System.out.println("Invalid phone number format.");
			return;
		}

		this.phoneNumber = phoneNumber;
	}

	public void setBalance(double balance) {
		if (balance < 0) {
			System.out.println("Balance cannot be negative.");
		} else
			this.balance = balance;
	}

	public void setStreet(String street) {
		if (StringValidationUtils.isNullOrEmpty(street)) {
			System.out.println("Street cannot be null or empty.");
			return;
		}

		if (!StringValidationUtils.isStringOnlyAlphanumericWithSpaces(street, 5)) {
			System.out.println("Street must contain only alphanumeric characters and spaces (max 5 spaces).");
			return;
		}

		this.street = street;
	}

	public void setCity(String city) {
		if (StringValidationUtils.isNullOrEmpty(city)) {
			System.out.println("City cannot be null or empty.");
			return;
		}

		if (!StringValidationUtils.isStringOnlyAlphabeticWithSpaces(city, 5)) {
			System.out.println("City must contain only alphabetic characters and " + "spaces (max 5 spaces).");
			return;
		}

		this.city = city;
	}

	public void setZipCode(String zipCode) {
		if (StringValidationUtils.isNullOrEmpty(zipCode)) {
			System.out.println("Zip code cannot be null or empty.");
			return;
		}

		if (!StringValidationUtils.isStringOnlyNumeric(zipCode)) {
			System.out.println("Zip code must contain only numeric characters.");
			return;
		}

		this.zipCode = zipCode;
	}

	@Override
	public String toString() {
		String lineSeparator = "\r\n";
		return "Customer" + lineSeparator + "  Code: " + code + lineSeparator + "  Name: " + firstName + " " + lastName
				+ lineSeparator + "  Email: " + email + lineSeparator + "  Phone: " + phoneNumber + lineSeparator
				+ "  Balance: " + String.format("%.2f", balance) + lineSeparator + "  Address: " + getAddress();
	}

	/**
	 * HW3 Part C - Method Reference 2. Used as Customer::printSummary via forEach
	 * in DeliveryService.sortMenuFlow(). Prints a one-line summary of this
	 * customer.
	 */
	public void printSummary() {
		System.out.println(firstName + " " + lastName + " | Balance: " + String.format("%.2f", balance));
	}

	/**
	 * HW3 Part A - Comparable. Sorts customers by credit balance from highest to
	 * lowest.
	 */
	@Override
	public int compareTo(Customer other) {
		return Double.compare(other.getBalance(), this.getBalance()); // descending
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Customer other = (Customer) obj;
		return code.equals(other.code);
	}
}