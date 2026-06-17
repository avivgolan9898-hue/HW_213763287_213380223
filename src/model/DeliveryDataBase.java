package model;

import admin.Admin;
import admin.RestAdmin;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import restaurant.PremiumRestaurant;
import restaurant.Restaurant;

/**
 * Central in-memory database for the food delivery system.
 * All main project data is stored in collections and kept consistent through
 * this class.
 */
public class DeliveryDataBase {
	private Admin systemAdministrator;
	private ArrayList<RestAdmin> restAdmins;
	private ArrayList<Restaurant> restaurants;
	private ArrayList<Customer> customers;
	private ArrayList<Rider> riders;
	private ArrayList<Order> orders;
	private HashMap<String, ArrayList<Order>> ordersByCustomer;
	private Hashtable<String, ArrayList<Restaurant>> restaurantsByCustomer;
	private HashMap<String, Double> totalPaymentsByCustomer;

	public DeliveryDataBase() {
		this.systemAdministrator = new Admin("System", "admin", "12345");
		this.restAdmins = new ArrayList<RestAdmin>();
		this.restaurants = new ArrayList<Restaurant>();
		this.customers = new ArrayList<Customer>();
		this.riders = new ArrayList<Rider>();
		this.orders = new ArrayList<Order>();
		this.ordersByCustomer = new HashMap<String, ArrayList<Order>>();
		this.restaurantsByCustomer = new Hashtable<String, ArrayList<Restaurant>>();
		this.totalPaymentsByCustomer = new HashMap<String, Double>();
	}

	// -- Getters for all collections and the system administrator --
	public Admin getSystemAdministrator() {
		return systemAdministrator;
	}

	public ArrayList<RestAdmin> getRestAdmins() {
		return new ArrayList<RestAdmin>(restAdmins);
	}

	public ArrayList<Restaurant> getRestaurants() {
		return new ArrayList<Restaurant>(restaurants);
	}

	public ArrayList<Customer> getCustomers() {
		return new ArrayList<Customer>(customers);
	}

	public ArrayList<Rider> getRiders() {
		return new ArrayList<Rider>(riders);
	}

	public ArrayList<Order> getOrders() {
		return new ArrayList<Order>(orders);
	}

	public HashMap<String, ArrayList<Order>> getOrdersByCustomer() {
		return new HashMap<String, ArrayList<Order>>(ordersByCustomer);
	}

	public Hashtable<String, ArrayList<Restaurant>> getRestaurantsByCustomer() {
		return new Hashtable<String, ArrayList<Restaurant>>(restaurantsByCustomer);
	}

	public HashMap<String, Double> getTotalPaymentsByCustomer() {
		return new HashMap<String, Double>(totalPaymentsByCustomer);
	}
	// end of getters
	
	
	// -- Methods to add entities to the database, ensuring uniqueness and consistency --
	public boolean addCustomer(Customer customer) {
		return addUnique(customers, customer);
	}

	public boolean addRestAdmin(RestAdmin restAdmin) {
		if (findAdminByUsername(restAdmin.getUsername()) != null) {
			return false;
		}
		return addUnique(restAdmins, restAdmin);
	}


	public boolean addRestaurant(Restaurant restaurant) {
		return addUnique(restaurants, restaurant);
	}

	public boolean addRider(Rider rider) {
		return addUnique(riders, rider);
	}

	public boolean addOrder(Order order) {
		if (order == null || findOrderByCode(order.getCode()) != null) {
			return false;
		}
		Customer customer = findCustomerByCode(order.getCustomerCode());
		if (customer == null || order.getRestaurant() == null || customer.getBalance() < order.getFinalPrice()) {
			return false;
		}
		orders.add(order);
		addOrderToCustomer(customer.getCode(), order);
		addRestaurantToCustomer(customer.getCode(), order.getRestaurant());
		addPaymentToCustomer(customer.getCode(), order.getFinalPrice());
		customer.setBalance(customer.getBalance() - order.getFinalPrice());
		return true;
	}

	public void addOrderToCustomer(String customerCode, Order order) {
		addToMapList(ordersByCustomer, customerCode, order);
	}
	// end of add methods
	
	

	public ArrayList<Order> getActiveOrdersByRider(String riderCode) {
		ArrayList<Order> activeOrders = new ArrayList<Order>();
		for (Order order : orders) {
			if (order != null && riderCode.equals(order.getRiderCode()) && order.isActiveDeliveryStatus()) {
				activeOrders.add(order);
			}
		}
		return activeOrders;
	}

	public ArrayList<Restaurant> getPremiumRestaurantsByCustomer(Customer customer) {
		ArrayList<Restaurant> premiumRestaurants = new ArrayList<Restaurant>();
		if (customer == null) {
			return premiumRestaurants;
		}
		ArrayList<Restaurant> customerRestaurants = restaurantsByCustomer.get(customer.getCode());
		if (customerRestaurants == null) {
			return premiumRestaurants;
		}
		for (Restaurant restaurant : customerRestaurants) {
			if (restaurant instanceof PremiumRestaurant && !premiumRestaurants.contains(restaurant)) {
				premiumRestaurants.add(restaurant);
			}
		}
		return premiumRestaurants;
	}

	public Customer getCustomerWithMostOrders() {
		Customer bestCustomer = null;
		int mostOrders = -1;
		for (Customer customer : customers) {
			ArrayList<Order> customerOrders = ordersByCustomer.get(customer.getCode());
			int orderCount = customerOrders == null ? 0 : customerOrders.size();
			if (orderCount > mostOrders) {
				mostOrders = orderCount;
				bestCustomer = customer;
			}
		}
		return bestCustomer;
	}

	public Rider getRiderWithMostDeliveries() {
		Rider bestRider = null;
		int mostDeliveries = -1;
		for (Rider rider : riders) {
			int deliveries = 0;
			for (Order order : rider.getAssignedOrders()) {
				if (order != null && order.isDeliveredStatus()) {
					deliveries++;
				}
			}
			if (deliveries > mostDeliveries) {
				mostDeliveries = deliveries;
				bestRider = rider;
			}
		}
		return bestRider;
	}

	public ArrayList<Restaurant> getOpenRestaurantsByCuisine(String cuisine) {
		ArrayList<Restaurant> result = new ArrayList<Restaurant>();
		if (cuisine == null) {
			return result;
		}
		for (Restaurant restaurant : restaurants) {
			if (restaurant != null && restaurant.isOpen() && cuisine.equalsIgnoreCase(restaurant.getType())) {
				result.add(restaurant);
			}
		}
		return result;
	}

	
	// -- Methods to find entities by their unique identifiers (code or username) --
	public Admin findAdminByUsername(String username) {
		if (username == null) {
			return null;
		}
		if (username.equals(systemAdministrator.getUsername())) {
			return systemAdministrator;
		}
		for (RestAdmin admin : restAdmins) {
			if (admin != null && username.equals(admin.getUsername())) {
				return admin;
			}
		}
		return null;
	}

	public Admin findAdminByCode(String code) {
		if (systemAdministrator != null && systemAdministrator.getCode().equals(code)) {
			return systemAdministrator;
		}
		return findByCode(restAdmins, code);
	}

	public Customer findCustomerByCode(String code) {
		return findByCode(customers, code);
	}

	public Restaurant findRestaurantByCode(String code) {
		return findByCode(restaurants, code);
	}

	public Rider findRiderById(String id) {
		return findByCode(riders, id);
	}

	public Order findOrderByCode(String code) {
		return findByCode(orders, code);
	}
	// end of find methods
	

	public ArrayList<Order> getOrdersByCustomerCode(String customerCode) {
		ArrayList<Order> result = ordersByCustomer.get(customerCode);
		if (result == null) {
			return new ArrayList<Order>();
		}
		return new ArrayList<Order>(result);
	}

	public ArrayList<Restaurant> getRestaurantsByCustomerCode(String customerCode) {
		ArrayList<Restaurant> result = restaurantsByCustomer.get(customerCode);
		if (result == null) {
			return new ArrayList<Restaurant>();
		}
		return new ArrayList<Restaurant>(result);
	}

	public boolean assignRestaurantToRestAdmin(String adminCode, String restaurantCode) {
		Admin admin = findAdminByCode(adminCode);
		Restaurant restaurant = findRestaurantByCode(restaurantCode);
		if (!(admin instanceof RestAdmin) || restaurant == null) {
			return false;
		}
		return ((RestAdmin) admin).addManagedRestaurant(restaurant);
	}

	public boolean assignRiderToOrder(String riderId, String orderCode) {
		Rider rider = findRiderById(riderId);
		Order order = findOrderByCode(orderCode);
		if (rider == null || order == null) {
			return false;
		}
		if (!rider.isAvailable() || order.getRiderCode() != null) {
			return false;
		}
		order.setRiderCode(riderId);
		if (!rider.addAssignedOrder(order)) {
			order.setRiderCode(null);
			return false;
		}
		if (!"sent".equals(order.getStatus())) {
			order.setStatus("sent");
		}
		return true;
	}

	public boolean updateOrderStatusByRider(Rider rider, Order order, String status, String deliveredDate) {
		if (rider == null || order == null || status == null || !rider.getId().equals(order.getRiderCode())) {
			return false;
		}
		if ("delivered".equals(status)) {
			order.setDeliveredDate(deliveredDate);
		}
		order.setStatus(status);
		
		if (order.isDeliveredStatus()) {
			rider.setAvailable(true);
		}
		return true;
	}

	private void addRestaurantToCustomer(String customerCode, Restaurant restaurant) {
		addToMapList(restaurantsByCustomer, customerCode, restaurant);
	}

	private void addPaymentToCustomer(String customerCode, double amount) {
		double currentTotal = totalPaymentsByCustomer.containsKey(customerCode) ? totalPaymentsByCustomer.get(customerCode) : 0;
		totalPaymentsByCustomer.put(customerCode, currentTotal + amount);
	}

	/**
	 * Generic method to find an item by its code in a collection of items that
	 * implement HasCodeField.
	 * 
	 * @param <T>   The type of items in the collection, must implement HasCodeField.
	 * @param items  The collection of items to search through.
	 * @param code   The code to search for.
	 * 
	 * @return       The item with the matching code, or null if not found.
	 */
	private <T extends HasCodeField> T findByCode(Collection<T> items, String code) {
		if (items == null || code == null) {
			return null;
		}
		for (T item : items) {
			if (item != null && code.equals(item.getCode())) {
				return item;
			}
		}
		return null;
	}
	
	/**
	 * Generic method to add an item to a collection if it is not null and its code
	 * is unique within the collection.
	 * 
	 * @param <T>   The type of items in the collection, must implement
	 *              HasCodeField.
	 * @param items The collection to add the item to.
	 * @param item  The item to add.
	 * 
	 * @return true if the item was added successfully, false if the item was null
	 *         or an item with the same code already exists in the collection.
	 */
	private <T extends HasCodeField> boolean addUnique(Collection<T> items, T item) {
	    if (item == null || findByCode(items, item.getCode()) != null) {
	        return false;
	    }
	    return items.add(item);
	}
	
	
	/**
	 * Generic method to add a value to a list in a map, creating the list if it
	 * doesn't exist. Ensures that the value is not added multiple times for the
	 * same key.
	 * 
	 * @param <T>   The type of values in the list.
	 * @param map   The map to add the value to.
	 * @param key   The key for the list to add the value to.
	 * @param value The value to add to the list.
	 */
	private <T> void addToMapList(Map<String, ArrayList<T>> map, String key, T value) {
	    if (key == null || value == null) {
	        return;
	    }

	    ArrayList<T> values = map.get(key);
	    if (values == null) {
	        values = new ArrayList<T>();
	        map.put(key, values);
	    }

	    if (!values.contains(value)) {
	        values.add(value);
	    }
	}

}
