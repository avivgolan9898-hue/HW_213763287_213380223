package model;

import restaurant.Restaurant;
import utils.ConsoleUI;
import utils.StringValidationUtils;

public class Order implements HasCodeField {
	private final String code;
	private String customerCode;
	private Restaurant restaurant;
	private String restaurantCode;
	private String riderCode;

	private String orderedDate;
	private String deliveredDate;

	private double basicPrice;
	private double finalPrice;
	private String status;

	private static int count = 0;
	private static final String[] VALID_STATUSES = { "pending", "sent", "on_the_way", "delivered" };

	public Order(String customerCode, Restaurant restaurant, String riderCode, String orderedDate, double basicPrice) {
		this.code = generateOrderCode();
		this.customerCode = customerCode;
		this.restaurant = restaurant;
		this.restaurantCode = restaurant.getCode();
		this.riderCode = riderCode;
		this.orderedDate = orderedDate;
		this.basicPrice = basicPrice;
		this.finalPrice = calculateFinalPrice();
		this.status = "pending";
		this.deliveredDate = "0";
	}

	public Order(String customerCode, Restaurant restaurant, String orderedDate, double basicPrice) {
		this(customerCode, restaurant, null, orderedDate, basicPrice);
	}

	private String generateOrderCode() {
		count++;
		return String.format("O%04d", count);
	}

	/**
	 * Calculates the final price of the order based on the restaurant's pricing
	 * rules. If no restaurant is associated, it returns the basic price.
	 */
	private double calculateFinalPrice() {
		if (restaurant == null) {
			return basicPrice;
		}
		return restaurant.calculateFinalPrice(basicPrice);
	}

	@Override
	public String getCode() {
		return code;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public Restaurant getRestaurant() {
		return restaurant;
	}

	public String getRestaurantCode() {
		return restaurantCode;
	}

	public String getRiderCode() {
		return riderCode;
	}

	public String getOrderedDate() {
		return orderedDate;
	}

	public String getDeliverdDate() {
		return deliveredDate;
	}

	public String getDeliveredDate() {
		return deliveredDate;
	}

	public double getBasicPrice() {
		return basicPrice;
	}

	public double getFinalPrice() {
		return finalPrice;
	}

	public String getStatus() {
		return status;
	}

	public void setCustomerCode(String customerCode) {
		if (StringValidationUtils.isNullOrEmpty(customerCode)) {
			ConsoleUI.error("Customer code cannot be null or empty.");
			return;
		}

		if (this.customerCode != null && this.customerCode.equals(customerCode)) {
			ConsoleUI.info("Customer code is already set to " + customerCode + ", no change needed.");
			return;
		}

		this.customerCode = customerCode;
	}

	public void setRestaurant(Restaurant restaurant) {
		if (restaurant == null) {
			ConsoleUI.error("Restaurant cannot be null.");
			return;
		}

		if (this.restaurant != null && this.restaurant.getCode().equals(restaurant.getCode())) {
			ConsoleUI.info("Restaurant is already set to " + restaurant.getName() + ", no change needed.");
			return;
		}

		if (this.restaurant != null) {
			ConsoleUI.info("Changing restaurant from " + this.restaurant.getName() + " to " + restaurant.getName());
		} else {
			ConsoleUI.info("Setting restaurant to " + restaurant.getName());
		}

		this.restaurant = restaurant;
		this.restaurantCode = restaurant.getCode();
		this.finalPrice = calculateFinalPrice();
	}

	public void setRiderCode(String riderCode) {
		this.riderCode = riderCode;
	}

	public void setOrderedDate(String orderedDate) {
		if (StringValidationUtils.isValidOrderDate(orderedDate))
			this.orderedDate = orderedDate;
	}

	public void setDeliveredDate(String deliveredDate) {
		if ("0".equals(deliveredDate)) {
			this.deliveredDate = "0";
			return;
		}
		if (!StringValidationUtils.isValidOrderDate(deliveredDate)) {
			ConsoleUI.error("Invalid delivered date format.");
			return;
		}
		if (StringValidationUtils.isBefore(deliveredDate, orderedDate)) {
			ConsoleUI.error("Delivered date cannot be before ordered date.");
			return;
		}

		this.deliveredDate = deliveredDate;
	}

	public void setBasicPrice(double basicPrice) {
		if (basicPrice < 0) {
			ConsoleUI.error("Basic price cannot be negative.");
			return;
		}
		this.basicPrice = basicPrice;
		this.finalPrice = calculateFinalPrice();
	}

	private boolean isValidStatus(String value) {
		for (String validStatus : VALID_STATUSES) {
			if (validStatus.equals(value)) {
				return true;
			}
		}
		ConsoleUI.error("Invalid status: " + value + ". Valid statuses are: pending, sent, on_the_way, delivered.");
		return false;
	}

	public void setStatus(String status) {
		if (StringValidationUtils.isNullOrEmpty(status) || !isValidStatus(status)) {
			ConsoleUI.error("Status cannot be null, empty, or invalid.");
			return;
		}
		if (isDeliveredStatus(this.status) && !isDeliveredStatus(status)) {
			ConsoleUI.error("Cannot change status from delivered back to another status.");
			return;
		}
		if ("pending".equals(status) && this.status != null && !"pending".equals(this.status)) {
			ConsoleUI.error("Cannot change status back to pending.");
			return;
		}
		if (isActiveDeliveryStatus(status) && StringValidationUtils.isNullOrEmpty(this.riderCode)) {
			ConsoleUI.error("Cannot set status to active delivery without an assigned rider.");
			return;
		}

		if (isDeliveredStatus(status) && "0".equals(this.deliveredDate)) {
			ConsoleUI.error("Cannot set status to delivered without a valid delivered date.");
			return;
		}

		if (status.equals(this.status)) {
			ConsoleUI.info("Status is already " + status + ", no change needed.");
			return;
		}

		ConsoleUI.success("Changing status from " + this.status + " to " + status);

		this.status = status;
	}

	public boolean isActiveDeliveryStatus() {
		return isActiveDeliveryStatus(status);
	}

	public boolean isDeliveredStatus() {
		return isDeliveredStatus(status);
	}

	private boolean isActiveDeliveryStatus(String value) {
		return "sent".equals(value) || "on_the_way".equals(value);
	}

	private boolean isDeliveredStatus(String value) {
		return "delivered".equals(value);
	}

	@Override
	public String toString() {
		String lineSeparator = "\r\n";
		String displayRider = StringValidationUtils.isNullOrEmpty(riderCode) ? "Not assigned" : riderCode;
		String displayDeliveredDate = "0".equals(deliveredDate) ? "Not delivered" : deliveredDate;
		return "Order" + lineSeparator + "  Code: " + code + lineSeparator + "  Customer code: " + customerCode
				+ lineSeparator + "  Restaurant code: " + restaurantCode + lineSeparator + "  Rider code: "
				+ displayRider + lineSeparator + "  Ordered date: " + orderedDate + lineSeparator + "  Delivered date: "
				+ displayDeliveredDate + lineSeparator + "  Basic price: " + String.format("%.2f", basicPrice)
				+ lineSeparator + "  Final price: " + String.format("%.2f", finalPrice) + lineSeparator + "  Status: "
				+ status;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		return code.equals(((Order) obj).code);
	}

	/**
	 * HW3 Part A - Comparator. Sorts orders by final price from highest to lowest.
	 */
	public static class FinalPriceComparator implements java.util.Comparator<Order> {
		@Override
		public int compare(Order o1, Order o2) {
			return Double.compare(o2.getFinalPrice(), o1.getFinalPrice()); // descending
		}
	}
}