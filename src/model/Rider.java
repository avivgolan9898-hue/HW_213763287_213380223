package model;

import java.util.ArrayList;

import utils.StringValidationUtils;

/**
 * Represents a delivery rider and keeps the rider's delivery history.
 */
public class Rider implements HasCodeField {
	private final String id;
	private String firstName;
	private String lastName;
	private String phoneNumber;
	private String vehicle;

	public static final String[] VALID_VEHICLES = { "car", "motorcycle", "bicycle" };
	private boolean isAvailable;
	private ArrayList<Order> assignedOrders;
	private static int count = 0;

	public Rider(String firstName, String lastName, String phoneNumber, String vehicle) {
		this.id = generateRiderId();
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.vehicle = vehicle;
		this.isAvailable = true;
		this.assignedOrders = new ArrayList<Order>();
	}

	private String generateRiderId() {
		count++;
		return String.format("Ri%04d", count);
	}

	public String getId() {
		return id;
	}

	@Override
	public String getCode() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getVehicle() {
		return vehicle;
	}

	public boolean isAvailable() {
		return isAvailable;
	}

	public ArrayList<Order> getAssignedOrders() {
		return assignedOrders;
	}

	public void setFirstName(String firstName) {
		if (StringValidationUtils.isNullOrEmpty(firstName)
				|| !StringValidationUtils.isStringOnlyAlphabetic(firstName)) {
			System.out.println("First name must contain only alphabetic characters.");
			return;
		}
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		if (StringValidationUtils.isNullOrEmpty(lastName) || !StringValidationUtils.isStringOnlyAlphabetic(lastName)) {
			System.out.println("Last name must contain only alphabetic characters.");
			return;
		}
		this.lastName = lastName;
	}

	public void setPhoneNumber(String phoneNumber) {
		if (StringValidationUtils.isNullOrEmpty(phoneNumber)
				|| !StringValidationUtils.isValidPhoneNumber(phoneNumber)) {
			System.out.println("Invalid phone number format.");
			return;
		}
		this.phoneNumber = phoneNumber;
	}

	private boolean isValidVehicle(String vehicle) {
		for (String validVehicle : VALID_VEHICLES) {
			if (validVehicle.equals(vehicle)) {
				return true;
			}
		}
		return false;
	}

	public void setVehicle(String vehicle) {
		if (StringValidationUtils.isNullOrEmpty(vehicle)) {
			System.out.println("Vehicle cannot be empty.");
			return;
		}
		if (!StringValidationUtils.isStringOnlyAlphabetic(vehicle)) {
			System.out.println("Vehicle must contain only alphabetic characters.");
			return;
		}

		String normalizedVehicle = vehicle.toLowerCase();
		if (!isValidVehicle(normalizedVehicle)) {
			System.out.println("Invalid vehicle type. Valid types are: car, motorcycle, bicycle.");
			return;
		}
		this.vehicle = normalizedVehicle;
	}

	public boolean addAssignedOrder(Order order) {
		if (order == null) {
			System.out.println("Order cannot be null, failed to assign order to rider.");
			return false;
		}
		for (Order assignedOrder : assignedOrders) {
			if (assignedOrder != null && assignedOrder.getCode().equals(order.getCode())) {
				System.out.println("Order is already assigned to this rider," + " failed to assign order: "
						+ order.getCode() + " + to rider.");
				return false;
			}
		}
		assignedOrders.add(order);
		isAvailable = false;
		return true;
	}

	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	@Override
	public String toString() {
		String lineSeparator = "\r\n";
		return "Rider" + lineSeparator
				+ "  Code: " + id + lineSeparator
				+ "  Name: " + firstName + " " + lastName + lineSeparator
				+ "  Phone: " + phoneNumber + lineSeparator
				+ "  Vehicle: " + vehicle + lineSeparator
				+ "  Available: " + (isAvailable ? "yes" : "no") + lineSeparator
				+ "  Assigned orders: " + assignedOrders.size();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Rider other = (Rider) obj;
		return id.equals(other.id);
	}
}
