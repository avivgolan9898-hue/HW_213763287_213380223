package restaurant;

public class PremiumRestaurant extends Restaurant {
	private double minimumOrderAmount;
	private double additionalCommissionRate; 

	public PremiumRestaurant(String name, String type, double rating, boolean isOpen, double deliveryFee,
			double minimumOrderAmount, double additionalCommissionRate) {
		super(name, type, rating, isOpen, deliveryFee);
		this.minimumOrderAmount = minimumOrderAmount;
		this.additionalCommissionRate = additionalCommissionRate;
	}

	public double getMinimumOrderAmount() {
		return minimumOrderAmount;
	}

	public double getAdditionalCommissionRate() {
		return additionalCommissionRate;
	}

	public void setMinimumOrderAmount(double minimumOrderAmount) {
		if (minimumOrderAmount < 0) {
			System.out.println("Minimum order amount cannot be negative.");
			return;
		}
		this.minimumOrderAmount = minimumOrderAmount;
	}

	public void setAdditionalCommissionRate(double additionalCommissionRate) {
		if (additionalCommissionRate < 0 || additionalCommissionRate > 1) {
			System.out.println("Additional commission rate must be between 0 and 1.");
			return;
		}
		this.additionalCommissionRate = additionalCommissionRate;
	}

	@Override
	public double calculateFinalPrice(double basicPrice) {
		double finalPrice = basicPrice + deliveryFee; 
		
		if (basicPrice < minimumOrderAmount) {
			finalPrice = minimumOrderAmount;
					
		}
		
		return finalPrice + (finalPrice * additionalCommissionRate);
	}

	@Override
	public String toString() {
		String lineSeparator = "\r\n";
		return "Premium Restaurant" + lineSeparator
				+ getDisplayDetails() + lineSeparator
				+ "  Minimum order amount: " + String.format("%.2f", minimumOrderAmount) + lineSeparator
				+ "  Additional commission: " + String.format("%.2f%%", additionalCommissionRate * 100);
	}
}
