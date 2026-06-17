package service;

import java.util.ArrayList;

import restaurant.FastFoodRestaurant;
import restaurant.Restaurant;
import model.Order;
import utils.ScannerUtils;
import java.util.Scanner;

/**
 * HW3 Part D - Wildcards. Contains four wildcard-based functions as required.
 */
public class SystemReports {

	/**
	 * HW3 Part D - Wildcard function 1. Uses: ArrayList<? extends Restaurant> Works
	 * with: ArrayList<Restaurant>, ArrayList<FastFoodRestaurant>,
	 * ArrayList<PremiumRestaurant> Displays all restaurants in the given
	 * collection.
	 *
	 * @param restaurants any list of Restaurant or its subclasses
	 */
	public static void printAllRestaurants(ArrayList<? extends Restaurant> restaurants) {
		if (restaurants == null || restaurants.isEmpty()) {
			System.out.println("No restaurants to display.");
			return;
		}
		System.out.println("\n--- Restaurant Report (" + restaurants.size() + " items) ---");
		for (Restaurant r : restaurants) {
			System.out.println(r);
			System.out.println("---");
		}
	}

	/**
	 * HW3 Part D - Wildcard function 2. Uses: ArrayList<? extends Order> Returns
	 * the total sum of final prices of all orders in the collection.
	 *
	 * @param orders any list of Order or its subclasses
	 * @return total sum of final prices
	 */
	public static double getTotalRevenue(ArrayList<? extends Order> orders) {
		double total = 0;
		if (orders == null || orders.isEmpty())
			return 0;
		for (Order o : orders)
			total += o.getFinalPrice();
		return total;
	}

	/**
	 * HW3 Part D - Wildcard function 3. Uses: ArrayList<? super FastFoodRestaurant>
	 * Adds a new FastFoodRestaurant to the given list. The list can be
	 * ArrayList<Restaurant> or ArrayList<Object>.
	 *
	 * @param list    a list that can hold FastFoodRestaurant or its supertype
	 * @param scanner scanner to read input from user
	 */
	public static void addFastFoodRestaurant(ArrayList<? super FastFoodRestaurant> list, Scanner scanner) {
		System.out.println("\n--- Add Fast Food Restaurant (Wildcard) ---");
		String name = ScannerUtils.readNonEmptyString(scanner, "Restaurant name: ");
		String type = ScannerUtils.readNonEmptyString(scanner, "Cuisine type: ");

		double rating = ScannerUtils.readDoubleInRange(scanner, "Rating (0-5): ", 0, 5);

		double deliveryFee = ScannerUtils.readNonNegativeDouble(scanner, "Delivery fee: ");

		int prepTime = ScannerUtils.readInt(scanner, "Average preparation time (minutes): ");

		double extraCharges = ScannerUtils.readNonNegativeDouble(scanner, "Extra charges: ");

		FastFoodRestaurant ffr = new FastFoodRestaurant(name, type, rating, true, deliveryFee, prepTime, extraCharges);
		list.add(ffr);
		System.out.println("Fast food restaurant added successfully: " + ffr.getName());
	}

	/**
	 * HW3 Part D - Wildcard function 4 (Generic). Returns the largest object from a
	 * collection of any class that implements Comparable. Uses bounded type
	 * parameter: <T extends Comparable<T>>
	 *
	 * @param list any list of Comparable objects
	 * @return the maximum element, or null if the list is empty
	 */
	public static <T extends Comparable<T>> T getMax(ArrayList<T> list) {
		if (list == null || list.isEmpty())
			return null;
		T max = list.get(0);
		for (T item : list) {
			if (item.compareTo(max) < 0) // compareTo returns negative if item is GREATER (balance desc)
				max = item;
		}
		return max;
	}
}