package restaurant;

import model.HasCodeField;

public class Restaurant implements HasCodeField {
	protected final String code;
	protected String name;
	protected String type;
	protected double rating;
	protected boolean isOpen;
	protected double deliveryFee;

	private static int count = 0; // to count the number of restaurants created
	
	/**
	 * Constructor to initialize a Restaurant object with the given parameters. The
	 * restaurant code is automatically generated based on the count of created
	 * restaurants.
	 *
	 * @param name        The name of the restaurant.
	 * @param type        The type of cuisine the restaurant offers.
	 * @param rating      The average rating of the restaurant (0 to 5).
	 * @param isOpen      Whether the restaurant is currently open or closed.
	 * @param deliveryFee The delivery fee charged by the restaurant (non-negative).
	 */
	public Restaurant(String name, String type, double rating, boolean isOpen, double deliveryFee) {
		this.code = generateRestaurantCode();
		this.name = name;
		this.type = type;
		this.rating = rating;
		this.isOpen = isOpen;
		this.deliveryFee = deliveryFee;
	}

	private String generateRestaurantCode() {
		count++;
		return String.format("Re%04d", count);
	}

	@Override
	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public double getRating() {
		return rating;
	}

	public boolean isOpen() {
		return isOpen;
	}

	public double getDeliveryFee() {
		return deliveryFee;
	}

	/**
	 * Polymorphic final price calculation entry point.
	 */
	public double calculateFinalPrice(double basicPrice) {
		return basicPrice + deliveryFee;
	}

	public void setName(String name) {
		if (name == null || name.trim().isEmpty())
			System.out.println("Restaurant name cannot be null or empty.");
		else
			this.name = name;
	}

	public void setType(String type) {
		if (type == null || type.trim().isEmpty())
			System.out.println("Restaurant type cannot be null or empty.");
		else
			this.type = type;
	}

	public void setRating(double rating) {
		if (rating < 0 || rating > 5)
			System.out.println("Rating must be between 0 and 5.");
		else
			this.rating = rating;
	}

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}

	public void setDeliveryFee(double deliveryFee) {
		if (deliveryFee < 0)
			System.out.println("Delivery fee cannot be negative.");
		else
			this.deliveryFee = deliveryFee;
	}

	protected String getDisplayDetails() {
		String lineSeparator = "\r\n";
		return "  Code: " + code + lineSeparator
				+ "  Name: " + name + lineSeparator
				+ "  Cuisine: " + type + lineSeparator
				+ "  Rating: " + String.format("%.1f", rating) + lineSeparator
				+ "  Status: " + (isOpen ? "open" : "closed") + lineSeparator
				+ "  Delivery fee: " + String.format("%.2f", deliveryFee);
	}

	@Override
	public String toString() {
		return "Restaurant" + "\r\n" + getDisplayDetails();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Restaurant other = (Restaurant) obj;
		return code.equals(other.code);
	}
}
