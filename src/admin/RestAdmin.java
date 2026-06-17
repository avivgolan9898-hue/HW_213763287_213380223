package admin;

import java.util.ArrayList;
import restaurant.Restaurant;
import utils.ConsoleUI;

/**
 * Restaurant administrator who can manage one or more restaurants.
 */
public class RestAdmin extends Admin {
	private ArrayList<Restaurant> managedRestaurants;

	public RestAdmin(String firstName, String username, String password) {
		super(firstName, username, password);
		this.managedRestaurants = new ArrayList<Restaurant>();
	}

	public ArrayList<Restaurant> getManagedRestaurants() {
		return managedRestaurants;
	}

	public int getRestaurantCount() {
		return managedRestaurants.size();
	}

	public boolean addManagedRestaurant(Restaurant restaurant) {
		if (restaurant == null) {
			ConsoleUI.error("Cannot add a null restaurant to the managed list.");
			return false;
		}
		for (Restaurant managedRestaurant : managedRestaurants) {
			if (managedRestaurant != null && managedRestaurant.getCode().equals(restaurant.getCode())) {
				ConsoleUI.error("This restaurant is already managed by this admin.");
				return false;
			}
		}
		managedRestaurants.add(restaurant);
		return true;
	}
	
	/**
	 * Checks if this admin is responsible for managing the restaurant with the
	 * given code.
	 * 
	 * @param restaurantCode The code of the restaurant to check.
	 * @return true if this admin manages the restaurant with the given code, false
	 */
	public boolean isResponsibleForRestaurant(String restaurantCode) {
		if (restaurantCode == null)
			return false;
		for (Restaurant managedRestaurant : managedRestaurants) {
			if (managedRestaurant != null && restaurantCode.equals(managedRestaurant.getCode())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		String lineSeparator = "\r\n";
		return "Restaurant Admin" + lineSeparator
				+ "  Code: " + code + lineSeparator
				+ "  Name: " + firstName + lineSeparator
				+ "  Username: " + username + lineSeparator
				+ "  Managed restaurants: " + getRestaurantCount();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		RestAdmin other = (RestAdmin) obj;
		return this.getUsername().equals(other.getUsername());
	}
}
