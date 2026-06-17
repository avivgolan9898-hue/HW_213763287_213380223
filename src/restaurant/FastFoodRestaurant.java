package restaurant;

public class FastFoodRestaurant extends Restaurant {
	private int averagePreparationTime;
	private double extraCharges;

	public FastFoodRestaurant(String name, String type, double rating, boolean isOpen, double deliveryFee,
			int averagePreparationTime, double extraCharges) {
		super(name, type, rating, isOpen, deliveryFee);
		this.averagePreparationTime = averagePreparationTime;
		this.extraCharges = extraCharges;
	}

	public int getAveragePreparationTime() {
		return averagePreparationTime;
	}

	public double getExtraCharges() {
		return extraCharges;
	}

	public void setAveragePreparationTime(int averagePreparationTime) {
		if (averagePreparationTime < 0) {
			System.out.println("Average preparation time cannot be negative.");
			return;
		}
		this.averagePreparationTime = averagePreparationTime;
	}

	public void setExtraCharges(double extraCharges) {
		if (extraCharges < 0) {
			System.out.println("Extra charges cannot be negative.");
			return;
		}
		this.extraCharges = extraCharges;
	}

	@Override
	public double calculateFinalPrice(double basicPrice) {
		return basicPrice + deliveryFee + extraCharges;
	}

	@Override
	public String toString() {
		String lineSeparator = "\r\n";
		return "Fast Food Restaurant" + lineSeparator
				+ getDisplayDetails() + lineSeparator
				+ "  Average preparation time: " + averagePreparationTime + " minutes" + lineSeparator
				+ "  Fast delivery extra charge: " + String.format("%.2f", extraCharges);
	}
}
