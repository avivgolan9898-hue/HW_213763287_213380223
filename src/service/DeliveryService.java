package service;

import admin.Admin;
import admin.RestAdmin;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import model.Customer;
import model.DeliveryDataBase;
import model.Order;
import model.Rider;
import restaurant.FastFoodRestaurant;
import restaurant.PremiumRestaurant;
import restaurant.Restaurant;
import utils.ConsoleColors;
import utils.ConsoleUI;
import utils.ScannerUtils;
import utils.StringValidationUtils;

/**
 * Console application layer for the food delivery system.
 */
public class DeliveryService {
	private DeliveryDataBase dataBase;
	private Scanner scanner;

	public DeliveryService() {
		this.dataBase = new DeliveryDataBase();
		this.scanner = new Scanner(System.in);
		initData();
	}

	public void start() {
		ConsoleUI.printWelcomeMessage();
		boolean running = true;
		while (running) {
			ConsoleUI.printMenu("Main Menu", new String[] { "1. System admin login", "2. Restaurant admin login",
					"3. Rider login", "4. Customer login", "0. Exit" });
			int choice = ScannerUtils.readInt(scanner, "Choose option: ");
			switch (choice) {
			case 1:
				systemAdminLogin();
				break;
			case 2:
				restaurantAdminLogin();
				break;
			case 3:
				riderLogin();
				break;
			case 4:
				customerLogin();
				break;
			case 0:
				running = false;
				break;
			default:
				ConsoleUI.invalidChoice();
			}
		}
		ConsoleUI.printExitMessage();
	}

	private void systemAdminLogin() {
		String username = ScannerUtils.readNonEmptyString(scanner, "Username: ");
		String password = ScannerUtils.readNonEmptyString(scanner, "Password: ");
		Admin admin = dataBase.findAdminByUsername(username);
		if (admin == null || !admin.getPassword().equals(password) || admin instanceof RestAdmin) {
			ConsoleUI.error("Invalid system admin login.");
			return;
		}
		ConsoleUI.success("Login successful. Welcome, " + admin.getFirstName() + "!");
		systemAdminMenu();
	}

	private void systemAdminMenu() {
		boolean back = false;
		while (!back) {
			ConsoleUI.printMenu("System Admin Menu",
					new String[] { "1.  Add customer", "2.  Add restaurant admin", "3.  Add restaurant",
							"4.  Add rider", "5.  Assign restaurant admin to restaurant", "6.  Assign rider to order",
							"7.  Show all orders", "8.  Customer with most orders", "9.  Rider with most deliveries",
							"10. Update restaurant open status", "11. Sort & Display [HW3 Part A]",
							"12. System Reports - Wildcards [HW3 Part D]", "0.  Back" });
			int choice = ScannerUtils.readInt(scanner, "Choose option: ");
			switch (choice) {
			case 1:
				addCustomerFlow();
				break;
			case 2:
				addRestAdminFlow();
				break;
			case 3:
				addRestaurantFlow();
				break;
			case 4:
				addRiderFlow();
				break;
			case 5:
				assignRestaurantAdminFlow();
				break;
			case 6:
				assignRiderToOrderFlow();
				break;
			case 7:
				printList(dataBase.getOrders());
				break;
			case 8:
				ConsoleUI.printObject(dataBase.getCustomerWithMostOrders());
				break;
			case 9:
				ConsoleUI.printObject(dataBase.getRiderWithMostDeliveries());
				break;
			case 10:
				updateRestaurantOpenStatusFlow();
				break;
			case 11:
				sortMenuFlow();
				break;
			case 12:
				reportsMenuFlow();
				break;
			case 0:
				back = true;
				break;
			default:
				ConsoleUI.invalidChoice();
			}
		}
	}

	private void restaurantAdminLogin() {
		String username = ScannerUtils.readNonEmptyString(scanner, "Username: ");
		String password = ScannerUtils.readNonEmptyString(scanner, "Password: ");
		Admin admin = dataBase.findAdminByUsername(username);
		if (!(admin instanceof RestAdmin) || !admin.getPassword().equals(password)) {
			ConsoleUI.error("Invalid restaurant admin login.");
			return;
		}
		ConsoleUI.success("Login successful. Welcome, " + admin.getFirstName() + "!");
		restaurantAdminMenu((RestAdmin) admin);
	}

	private void restaurantAdminMenu(RestAdmin restAdmin) {
		boolean back = false;
		while (!back) {
			ConsoleUI.printMenu("Restaurant Admin Menu",
					new String[] { "1. Add customer", "2. Add order", "3. Add rider", "4. Assign rider to order",
							"5. Show orders by restaurant", "6. Show open restaurants by cuisine", "0. Back" });
			int choice = ScannerUtils.readInt(scanner, "Choose option: ");
			switch (choice) {
			case 1:
				addCustomerFlow();
				break;
			case 2:
				addOrderFlow(restAdmin, null);
				break;
			case 3:
				addRiderFlow();
				break;
			case 4:
				assignRiderToOrderFlow();
				break;
			case 5:
				showOrdersByRestaurantFlow(restAdmin);
				break;
			case 6:
				showOpenRestaurantsByCuisineFlow();
				break;
			case 0:
				back = true;
				break;
			default:
				ConsoleUI.invalidChoice();
			}
		}
	}

	private void riderLogin() {
		Rider rider = dataBase.findRiderById(ScannerUtils.readNonEmptyString(scanner, "Rider ID: "));
		if (rider == null) {
			ConsoleUI.error("Rider not found.");
			return;
		}
		ConsoleUI.success("Login successful. Welcome, " + rider.getFirstName() + "!");
		boolean back = false;
		while (!back) {
			ConsoleUI.printMenu("Rider Menu", new String[] { "1. Update order status", "2. Show active order",
					"3. Show order history", "0. Back" });
			int choice = ScannerUtils.readInt(scanner, "Choose option: ");
			switch (choice) {
			case 1:
				updateOrderStatusByRiderFlow(rider);
				break;
			case 2:
				printList(dataBase.getActiveOrdersByRider(rider.getId()));
				break;
			case 3:
				printList(rider.getAssignedOrders());
				break;
			case 0:
				back = true;
				break;
			default:
				ConsoleUI.invalidChoice();
			}
		}
	}

	private void customerLogin() {
		Customer customer = dataBase.findCustomerByCode(ScannerUtils.readNonEmptyString(scanner, "Customer code: "));
		if (customer == null) {
			ConsoleUI.error("Customer not found.");
			return;
		}
		ConsoleUI.success("Login successful. Welcome, " + customer.getFirstName() + "!");
		boolean back = false;
		while (!back) {
			String balance = "current balance: " + customer.getBalance();
			ConsoleUI.printMenu("Customer Menu - " + balance,
					new String[] { "1. Create new order", "2. Show my orders", "3. Update contact details",
							"4. Show restaurants I ordered from", "5. Show premium restaurants I ordered from",
							"6. Add money", "7. Withdraw money", "0. Back" });
			int choice = ScannerUtils.readInt(scanner, "Choose option: ");
			switch (choice) {
			case 1:
				addOrderFlow(null, customer.getCode());
				break;
			case 2:
				printList(dataBase.getOrdersByCustomerCode(customer.getCode()));
				break;
			case 3:
				updateCustomerDetailsFlow(customer);
				break;
			case 4:
				printList(dataBase.getRestaurantsByCustomerCode(customer.getCode()));
				break;
			case 5:
				printList(dataBase.getPremiumRestaurantsByCustomer(customer));
				break;
			case 6:
				depositFlow(customer);
				break;
			case 7:
				withdrawFlow(customer);
				break;
			case 0:
				back = true;
				break;
			default:
				ConsoleUI.invalidChoice();
			}
		}
	}

	private void addCustomerFlow() {
		String firstName = ScannerUtils.readNonEmptyString(scanner, "First name: ");
		if (!StringValidationUtils.isStringOnlyAlphabetic(firstName)) {
			ConsoleUI.error("First name must contain only alphabetic characters.");
			return;
		}

		String lastName = ScannerUtils.readNonEmptyString(scanner, "Last name: ");
		if (!StringValidationUtils.isStringOnlyAlphabetic(lastName)) {
			ConsoleUI.error("Last name must contain only alphabetic characters.");
			return;
		}

		String email = ScannerUtils.readNonEmptyString(scanner, "Email: ");
		if (!StringValidationUtils.isValidEmail(email)) {
			ConsoleUI.error("Invalid email format.");
			return;
		}

		String phone = ScannerUtils.readNonEmptyString(scanner, "Phone: ");
		if (!StringValidationUtils.isValidPhoneNumber(phone)) {
			ConsoleUI.error("Invalid phone number format.");
			return;
		}

		double balance = ScannerUtils.readNonNegativeDouble(scanner, "Balance: ");
		String street = ScannerUtils.readNonEmptyString(scanner, "Street: ");
		if (!StringValidationUtils.isStringOnlyAlphanumericWithSpaces(street, 5)) {
			ConsoleUI.error("Street must contain only alphanumeric characters and spaces.");
			return;
		}

		String city = ScannerUtils.readNonEmptyString(scanner, "City: ");
		if (!StringValidationUtils.isStringOnlyAlphabeticWithSpaces(city, 5)) {
			ConsoleUI.error("City must contain only alphabetic characters and spaces.");
			return;
		}

		String zip = ScannerUtils.readNonEmptyString(scanner, "Zip: ");
		if (!StringValidationUtils.isStringOnlyNumeric(zip)) {
			ConsoleUI.error("Zip code must contain only numeric characters.");
			return;
		}

		Customer customer = new Customer(firstName, lastName, email, phone, balance, street, city, zip);
		if (dataBase.addCustomer(customer)) {
			ConsoleUI.success("Customer created. Code: " + customer.getCode());
		} else {
			ConsoleUI.error("Customer not created.");
		}
	}

	private void addRestAdminFlow() {
		String firstName = ScannerUtils.readNonEmptyString(scanner, "First name: ");
		if (!StringValidationUtils.isStringOnlyAlphabetic(firstName)) {
			ConsoleUI.error("First name must contain only alphabetic characters.");
			return;
		}

		String username = ScannerUtils.readNonEmptyString(scanner, "Username: ");
		if (!StringValidationUtils.isStringOnlyAlphabetic(username)) {
			ConsoleUI.error("Username must contain only alphabetic characters.");
			return;
		}
		if (dataBase.findAdminByUsername(username) != null) {
			ConsoleUI.error("Username already exists.");
			return;
		}

		String password = ScannerUtils.readNonEmptyString(scanner, "Password: ");

		if (!StringValidationUtils.isValidPassword(password)) {
			return;
		}
		RestAdmin restAdmin = new RestAdmin(firstName, username, password);
		if (dataBase.addRestAdmin(restAdmin)) {
			ConsoleUI.success("Restaurant admin created. Code: " + restAdmin.getCode());
		} else {
			ConsoleUI.error("Restaurant admin not created.");
		}
	}

	private void addRestaurantFlow() {
		String kind = ScannerUtils.readNonEmptyString(scanner, "Kind (regular/fast/premium): ").toLowerCase();
		if (!"regular".equals(kind) && !"fast".equals(kind) && !"premium".equals(kind)) {
			ConsoleUI.error("Invalid restaurant kind.");
			return;
		}

		String name = ScannerUtils.readNonEmptyString(scanner, "Name: ");
		if (!StringValidationUtils.isStringOnlyAlphabeticWithSpaces(name, 5)) {
			ConsoleUI.error("Restaurant name must contain only alphabetic characters and spaces.");
			return;
		}

		String cuisine = ScannerUtils.readNonEmptyString(scanner, "Cuisine/type: ");
		if (!StringValidationUtils.isStringOnlyAlphabetic(cuisine)) {
			ConsoleUI.error("Cuisine/type must contain only alphabetic characters.");
			return;
		}

		double rating = ScannerUtils.readDoubleInRange(scanner, "Rating (0-5): ", 0, 5);
		double deliveryFee = ScannerUtils.readNonNegativeDouble(scanner, "Delivery fee: ");
		Restaurant restaurant;
		if ("fast".equals(kind)) {
			int prep = ScannerUtils.readNonNegativeInt(scanner, "Average preparation time: ");
			double extra = ScannerUtils.readNonNegativeDouble(scanner, "Fast delivery extra charge: ");
			restaurant = new FastFoodRestaurant(name, cuisine, rating, true, deliveryFee, prep, extra);
		} else if ("premium".equals(kind)) {
			double minimum = ScannerUtils.readNonNegativeDouble(scanner, "Minimum order amount: ");
			double commission = ScannerUtils.readDoubleInRange(scanner, "Commission percent (0-100): ", 0, 100);
			restaurant = new PremiumRestaurant(name, cuisine, rating, true, deliveryFee, minimum, commission / 100);
		} else if ("regular".equals(kind)) {
			restaurant = new Restaurant(name, cuisine, rating, true, deliveryFee);
		} else {
			ConsoleUI.error("Invalid restaurant kind.");
			return;
		}
		if (dataBase.addRestaurant(restaurant)) {
			ConsoleUI.success("Restaurant created. Code: " + restaurant.getCode());
		} else {
			ConsoleUI.error("Restaurant not created.");
		}
	}

	private void addRiderFlow() {
		String firstName = ScannerUtils.readNonEmptyString(scanner, "First name: ");
		if (!StringValidationUtils.isStringOnlyAlphabetic(firstName)) {
			ConsoleUI.error("First name must contain only alphabetic characters.");
			return;
		}

		String lastName = ScannerUtils.readNonEmptyString(scanner, "Last name: ");
		if (!StringValidationUtils.isStringOnlyAlphabetic(lastName)) {
			ConsoleUI.error("Last name must contain only alphabetic characters.");
			return;
		}

		String phone = ScannerUtils.readNonEmptyString(scanner, "Phone: ");
		if (!StringValidationUtils.isValidPhoneNumber(phone)) {
			ConsoleUI.error("Invalid phone number format.");
			return;
		}

		String vehicle = ScannerUtils.readNonEmptyString(scanner, "Vehicle (car/motorcycle/bicycle): ").toLowerCase();
		if (!isValidVehicle(vehicle)) {
			ConsoleUI.error("Invalid vehicle type. Valid types are: car, motorcycle, bicycle.");
			return;
		}
		Rider rider = new Rider(firstName, lastName, phone, vehicle);
		if (dataBase.addRider(rider)) {
			ConsoleUI.success("Rider created. ID: " + rider.getId());
		} else {
			ConsoleUI.error("Rider not created.");
		}
	}

	private void addOrderFlow(RestAdmin restAdmin, String customerCode) {
		String code = customerCode == null ? ScannerUtils.readNonEmptyString(scanner, "Customer code: ") : customerCode;
		Customer customer = dataBase.findCustomerByCode(code);
		if (customer == null) {
			ConsoleUI.error("Customer not found.");
			return;
		}
		String restaurantCode = ScannerUtils.readNonEmptyString(scanner, "Restaurant code: ");
		Restaurant restaurant = dataBase.findRestaurantByCode(restaurantCode);
		if (restaurant == null || !restaurant.isOpen()) {
			ConsoleUI.error("Restaurant not found or closed.");
			return;
		}
		if (restAdmin != null && !restAdmin.isResponsibleForRestaurant(restaurantCode)) {
			ConsoleUI.error("You are not responsible for this restaurant.");
			return;
		}
		String orderedDate = ScannerUtils.readNonEmptyString(scanner, "Ordered date (DD-MM-YYYY): ");
		if (!StringValidationUtils.isValidOrderDate(orderedDate)) {
			ConsoleUI.error("Order not created.");
			return;
		}
		double basicPrice = ScannerUtils.readNonNegativeDouble(scanner, "Basic price: ");
		double finalPrice = restaurant.calculateFinalPrice(basicPrice);
		if (customer.getBalance() < finalPrice) {
			ConsoleUI.error("Customer does not have enough balance. Needed: " + finalPrice);
			return;
		}
		Order order = new Order(customer.getCode(), restaurant, orderedDate, basicPrice);
		if (dataBase.addOrder(order)) {
			ConsoleUI.success("Order created. Code: " + order.getCode());
			ConsoleUI.info("Final price: " + order.getFinalPrice());
			ConsoleUI.info("Updated balance: " + customer.getBalance());
		} else {
			ConsoleUI.error("Order not created.");
		}
	}

	private void assignRestaurantAdminFlow() {
		String adminCode = ScannerUtils.readNonEmptyString(scanner, "Admin code: ");
		Admin admin = dataBase.findAdminByCode(adminCode);
		if (!(admin instanceof RestAdmin)) {
			ConsoleUI.error("Restaurant admin not found.");
			return;
		}

		String restaurantCode = ScannerUtils.readNonEmptyString(scanner, "Restaurant code: ");
		if (dataBase.findRestaurantByCode(restaurantCode) == null) {
			ConsoleUI.error("Restaurant not found.");
			return;
		}

		if (dataBase.assignRestaurantToRestAdmin(adminCode, restaurantCode)) {
			ConsoleUI.success("Assignment done.");
		} else {
			ConsoleUI.error("Assignment failed.");
		}
	}

	private void assignRiderToOrderFlow() {
		String riderId = ScannerUtils.readNonEmptyString(scanner, "Rider ID: ");
		Rider rider = dataBase.findRiderById(riderId);
		if (rider == null) {
			ConsoleUI.error("Rider not found.");
			return;
		}
		if (!rider.isAvailable()) {
			ConsoleUI.error("Rider is not available.");
			return;
		}

		String orderCode = ScannerUtils.readNonEmptyString(scanner, "Order code: ");
		Order order = dataBase.findOrderByCode(orderCode);
		if (order == null) {
			ConsoleUI.error("Order not found.");
			return;
		}
		if (order.getRiderCode() != null) {
			ConsoleUI.error("Order is already assigned to a rider.");
			return;
		}

		if (dataBase.assignRiderToOrder(riderId, orderCode)) {
			ConsoleUI.success("Rider assigned and order status changed to sent.");
		} else {
			ConsoleUI.error("Assignment failed.");
		}
	}

	private void updateOrderStatusByRiderFlow(Rider rider) {
		String orderCode = ScannerUtils.readNonEmptyString(scanner, "Order code: ");
		Order order = dataBase.findOrderByCode(orderCode);
		if (order == null || !rider.getId().equals(order.getRiderCode())) {
			ConsoleUI.error("Order not found or not assigned to you.");
			return;
		}

		if ("delivered".equals(order.getStatus())) {
			ConsoleUI.info("Order is already delivered, no update needed.");
			return;
		}

		String status = ScannerUtils.readNonEmptyString(scanner, "New status (on_the_way/delivered): ");
		if (!"on_the_way".equals(status) && !"delivered".equals(status)) {
			ConsoleUI.error("Riders may update only to on_the_way or delivered.");
			return;
		}
		String deliveredDate = "0";
		if ("delivered".equals(status)) {
			deliveredDate = ScannerUtils.readNonEmptyString(scanner, "Delivered date (DD-MM-YYYY): ");
			if (!StringValidationUtils.isValidOrderDate(deliveredDate)) {
				ConsoleUI.error("Status update failed.");
				return;
			}
		}
		if (dataBase.updateOrderStatusByRider(rider, order, status, deliveredDate)) {
			ConsoleUI.success("Order status updated.");
		} else {
			ConsoleUI.error("Status update failed.");
		}
	}

	private void updateRestaurantOpenStatusFlow() {
		Restaurant restaurant = dataBase
				.findRestaurantByCode(ScannerUtils.readNonEmptyString(scanner, "Restaurant code: "));
		if (restaurant == null) {
			ConsoleUI.error("Restaurant not found.");
			return;
		}
		int status = ScannerUtils.readInt(scanner, "1 = open, 0 = closed: ");

		if (status != 0 && status != 1) {
			ConsoleUI.error("Invalid status.");
			return;
		}

		restaurant.setOpen(status == 1);
		ConsoleUI.success("Restaurant status updated.");
	}

	private void showOrdersByRestaurantFlow(RestAdmin restAdmin) {
		String restaurantCode = ScannerUtils.readNonEmptyString(scanner, "Restaurant code: ");
		if (!restAdmin.isResponsibleForRestaurant(restaurantCode)) {
			ConsoleUI.error("You are not responsible for this restaurant.");
			return;
		}
		ArrayList<Order> result = new ArrayList<Order>();
		for (Order order : dataBase.getOrders()) {
			if (restaurantCode.equals(order.getRestaurantCode())) {
				result.add(order);
			}
		}
		printList(result);
	}

	private void showOpenRestaurantsByCuisineFlow() {
		String cuisine = ScannerUtils.readNonEmptyString(scanner, "Cuisine/type: ");

		if (!StringValidationUtils.isStringOnlyAlphabetic(cuisine)) {
			ConsoleUI.error("Invalid cuisine/type.");
			return;
		}

		printList(dataBase.getOpenRestaurantsByCuisine(cuisine));
	}

	private void updateCustomerDetailsFlow(Customer customer) {
		boolean back = false;
		while (!back) {
			ConsoleUI.printMenu("Update Customer Details", new String[] { "1. Update street", "2. Update city",
					"3. Update zip code", "4. Update phone", "5. Update email", "0. Back" });
			int choice = ScannerUtils.readInt(scanner, "Choose option: ");
			switch (choice) {
			case 1:
				updateCustomerStreetFlow(customer);
				break;
			case 2:
				updateCustomerCityFlow(customer);
				break;
			case 3:
				updateCustomerZipFlow(customer);
				break;
			case 4:
				updateCustomerPhoneFlow(customer);
				break;
			case 5:
				updateCustomerEmailFlow(customer);
				break;
			case 0:
				back = true;
				break;
			default:
				ConsoleUI.invalidChoice();
			}
		}
	}

	private void updateCustomerStreetFlow(Customer customer) {
		String street = ScannerUtils.readNonEmptyString(scanner, "Street: ");
		if (!StringValidationUtils.isStringOnlyAlphanumericWithSpaces(street, 5)) {
			ConsoleUI.error("Street must contain only alphanumeric characters and spaces.");
			return;
		}
		customer.setStreet(street);
		ConsoleUI.success("Street updated.");
	}

	private void updateCustomerCityFlow(Customer customer) {
		String city = ScannerUtils.readNonEmptyString(scanner, "City: ");
		if (!StringValidationUtils.isStringOnlyAlphabeticWithSpaces(city, 5)) {
			ConsoleUI.error("City must contain only alphabetic characters and spaces.");
			return;
		}
		customer.setCity(city);
		ConsoleUI.success("City updated.");
	}

	private void updateCustomerZipFlow(Customer customer) {
		String zip = ScannerUtils.readNonEmptyString(scanner, "Zip: ");
		if (!StringValidationUtils.isStringOnlyNumeric(zip)) {
			ConsoleUI.error("Zip code must contain only numeric characters.");
			return;
		}
		customer.setZipCode(zip);
		ConsoleUI.success("Zip code updated.");
	}

	private void updateCustomerPhoneFlow(Customer customer) {
		String phone = ScannerUtils.readNonEmptyString(scanner, "Phone: ");
		if (!StringValidationUtils.isValidPhoneNumber(phone)) {
			ConsoleUI.error("Phone was not updated.");
			return;
		}
		customer.setPhoneNumber(phone);
		ConsoleUI.success("Phone updated.");
	}

	private void updateCustomerEmailFlow(Customer customer) {
		String email = ScannerUtils.readNonEmptyString(scanner, "Email: ");
		if (!StringValidationUtils.isValidEmail(email)) {
			ConsoleUI.error("Email was not updated.");
			return;
		}
		customer.setEmail(email);
		ConsoleUI.success("Email updated.");
	}

	private void depositFlow(Customer customer) {
		double amount = ScannerUtils.readNonNegativeDouble(scanner, "Amount to add: ");
		customer.setBalance(customer.getBalance() + amount);
		ConsoleUI.success("Money added. New balance: " + customer.getBalance());
	}

	private void withdrawFlow(Customer customer) {
		double amount = ScannerUtils.readNonNegativeDouble(scanner, "Amount to withdraw: ");
		if (amount > customer.getBalance()) {
			ConsoleUI.error("Cannot withdraw more than the current balance.");
			return;
		}
		customer.setBalance(customer.getBalance() - amount);
		ConsoleUI.success("Money withdrawn. New balance: " + customer.getBalance());
	}

	private boolean isValidPersonData(String firstName, String lastName, String email, String phone) {
		return StringValidationUtils.isStringOnlyAlphabetic(firstName)
				&& StringValidationUtils.isStringOnlyAlphabetic(lastName) && StringValidationUtils.isValidEmail(email)
				&& StringValidationUtils.isValidPhoneNumber(phone);
	}

	private boolean isValidVehicle(String vehicle) {
		for (String validVehicle : Rider.VALID_VEHICLES) {
			if (validVehicle.equals(vehicle)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Utility method to print lists of any type with a common "no data" message for
	 * empty lists.
	 * 
	 * @param items the list of items to print
	 */
	/**
	 * Prints all items in the list. HW3 Part C - Method Reference 1:
	 * System.out::println forEach with System.out::println replaces the for loop.
	 */
	private void printList(ArrayList<?> items) {
		if (items == null || items.isEmpty()) {
			ConsoleUI.info("No data to display.");
			return;
		}
		items.forEach(System.out::println); // Method Reference 1: System.out::println
	}

	/**
	 * HW3 Part A - Sort Menu. Accessible from System Admin menu, option 11. Option
	 * 1: Comparable - Collections.sort calls Customer.compareTo (balance desc).
	 * Option 2: Comparator - Restaurant.RatingComparator (rating desc). Option 3:
	 * Comparator - Order.FinalPriceComparator (final price desc).
	 */
	private void sortMenuFlow() {
		boolean back = false;
		while (!back) {
			ConsoleUI.printMenu("Sort & Display Menu",
					new String[] { "1. Sort customers by balance - highest first [Comparable]",
							"2. Sort restaurants by rating - highest first [Comparator]",
							"3. Sort orders by final price - highest first [Comparator]",
							"4. Sort riders by delivery count [Lambda]", "5. Sort customers by first name [Lambda]",
							"6. Sort orders by date [Lambda]", "0. Back" });
			int choice = ScannerUtils.readInt(scanner, "Choose option: ");
			switch (choice) {
			case 1:
				// Comparable: Collections.sort calls Customer.compareTo automatically
				ArrayList<Customer> sortedCustomers = new ArrayList<>(dataBase.getCustomers());
				Collections.sort(sortedCustomers);
				System.out.println("\n--- Customers sorted by balance (highest first) ---");
				sortedCustomers.forEach(Customer::printSummary); // Method Reference 2: Customer::printSummary
				break;
			case 2:
				// Comparator: Restaurant.RatingComparator passed to Collections.sort
				ArrayList<Restaurant> sortedRestaurants = new ArrayList<>(dataBase.getRestaurants());
				Collections.sort(sortedRestaurants, new Restaurant.RatingComparator());
				System.out.println("\n--- Restaurants sorted by rating (highest first) ---");
				for (Restaurant r : sortedRestaurants)
					System.out.println(r.getName() + " | Rating: " + r.getRating());
				break;
			case 3:
				// Comparator: Order.FinalPriceComparator passed to Collections.sort
				ArrayList<Order> sortedOrders = new ArrayList<>(dataBase.getOrders());
				Collections.sort(sortedOrders, new Order.FinalPriceComparator());
				System.out.println("\n--- Orders sorted by final price (highest first) ---");
				for (Order o : sortedOrders)
					System.out.println(
							"Order " + o.getCode() + " | Final Price: " + String.format("%.2f", o.getFinalPrice()));
				break;
			case 4:
				// Lambda: sort riders by number of assigned orders (deliveries) descending
				ArrayList<Rider> sortedRiders = new ArrayList<>(dataBase.getRiders());
				sortedRiders.sort((r1, r2) -> r2.getAssignedOrders().size() - r1.getAssignedOrders().size());
				System.out.println("\n--- Riders sorted by delivery count (highest first) ---");
				sortedRiders.forEach(Rider::printSummary); // Method Reference 3: Rider::printSummary
				break;
			case 5:
				// Lambda: sort customers by first name alphabetically
				ArrayList<Customer> sortedByName = new ArrayList<>(dataBase.getCustomers());
				sortedByName.sort((c1, c2) -> c1.getFirstName().compareTo(c2.getFirstName()));
				System.out.println("\n--- Customers sorted by first name (A to Z) ---");
				for (Customer cu : sortedByName)
					System.out.println(cu.getFirstName() + " " + cu.getLastName());
				break;
			case 6:
				// Lambda: sort orders by ordered date alphabetically (format DD-MM-YYYY sorts
				// correctly as string)
				ArrayList<Order> sortedByDate = new ArrayList<>(dataBase.getOrders());
				sortedByDate.sort((o1, o2) -> o1.getOrderedDate().compareTo(o2.getOrderedDate()));
				System.out.println("\n--- Orders sorted by date (earliest first) ---");
				for (Order o : sortedByDate)
					System.out.println("Order " + o.getCode() + " | Date: " + o.getOrderedDate() + " | Price: "
							+ String.format("%.2f", o.getFinalPrice()));
				break;
			case 0:
				back = true;
				break;
			default:
				ConsoleUI.invalidChoice();
			}
		}
	}

	/**
	 * HW3 Part D - Wildcards Reports Menu. Accessible from System Admin menu,
	 * option 12. Demonstrates all 4 wildcard functions from SystemReports.
	 */
	private void reportsMenuFlow() {
		boolean back = false;
		while (!back) {
			ConsoleUI.printMenu("System Reports - Wildcards",
					new String[] { "1. Show all restaurants [? extends Restaurant]",
							"2. Show total revenue from all orders [? extends Order]",
							"3. Add fast food restaurant to system [? super FastFoodRestaurant]",
							"4. Find customer with highest balance [Generic getMax]", "0. Back" });
			int choice = ScannerUtils.readInt(scanner, "Choose option: ");
			switch (choice) {
			case 1:
				// Wildcard 1: ? extends Restaurant - works for any restaurant subtype
				SystemReports.printAllRestaurants(dataBase.getRestaurants());
				break;
			case 2:
				// Wildcard 2: ? extends Order - sums final prices of all orders
				double total = SystemReports.getTotalRevenue(dataBase.getOrders());
				System.out.println("\nTotal revenue from all orders: " + String.format("%.2f", total));
				break;
			case 3:
				// Wildcard 3: ? super FastFoodRestaurant - adds to the main restaurant list
				SystemReports.addFastFoodRestaurant(dataBase.getRestaurants(), scanner);
				break;
			case 4:
				// Wildcard 4: Generic getMax with Comparable - finds customer with highest
				// balance
				model.Customer maxCustomer = SystemReports.getMax(dataBase.getCustomers());
				if (maxCustomer != null)
					System.out.println("\nCustomer with highest balance:\n" + maxCustomer);
				else
					System.out.println("No customers in system.");
				break;
			case 0:
				back = true;
				break;
			default:
				ConsoleUI.invalidChoice();
			}
		}
	}

	private void initData() {
		RestAdmin dana = new RestAdmin("Dana", "resta", "abc123");
		RestAdmin yossi = new RestAdmin("Yossi", "restb", "abc123");
		RestAdmin noa = new RestAdmin("Noa", "restc", "abc123");
		dataBase.addRestAdmin(dana);
		dataBase.addRestAdmin(yossi);
		dataBase.addRestAdmin(noa);

		for (int i = 1; i <= 10; i++) {
			dataBase.addCustomer(new Customer("Customer" + i, "User", "c" + i + "@mail.com", "05012345" + i, 500,
					"Street " + i, "City", "1234" + i));
		}

		dataBase.addRestaurant(new Restaurant("Home Food", "regular", 4.3, true, 12));
		dataBase.addRestaurant(new Restaurant("Pasta Place", "italian", 4.0, true, 10));
		dataBase.addRestaurant(new Restaurant("Grill Box", "grill", 4.1, true, 11));
		dataBase.addRestaurant(new FastFoodRestaurant("Burger Express", "fast", 4.5, true, 8, 15, 3));
		dataBase.addRestaurant(new FastFoodRestaurant("Pizza Go", "italian", 4.2, true, 7, 20, 4));
		dataBase.addRestaurant(new FastFoodRestaurant("Taco Now", "mexican", 4.0, true, 7, 10, 2));
		dataBase.addRestaurant(new PremiumRestaurant("Chef Table", "premium", 4.9, true, 15, 120, 0.20));
		dataBase.addRestaurant(new PremiumRestaurant("Sea Prime", "seafood", 4.8, true, 14, 100, 0.18));
		dataBase.addRestaurant(new PremiumRestaurant("Royal Asia", "asian", 4.7, true, 13, 110, 0.16));
		dataBase.addRestaurant(new Restaurant("Healthy Bowl", "vegan", 4.1, true, 9));

		for (int i = 1; i <= 5; i++) {
			dataBase.addRider(new Rider("Rider" + i, "Test", "05099999" + i, "car"));
		}

		ArrayList<Restaurant> restaurants = dataBase.getRestaurants();
		dataBase.assignRestaurantToRestAdmin(dana.getCode(), restaurants.get(0).getCode());
		dataBase.assignRestaurantToRestAdmin(dana.getCode(), restaurants.get(3).getCode());
		dataBase.assignRestaurantToRestAdmin(yossi.getCode(), restaurants.get(1).getCode());
		dataBase.assignRestaurantToRestAdmin(yossi.getCode(), restaurants.get(6).getCode());
		dataBase.assignRestaurantToRestAdmin(noa.getCode(), restaurants.get(2).getCode());
		dataBase.assignRestaurantToRestAdmin(noa.getCode(), restaurants.get(7).getCode());

		for (int i = 0; i < 10; i++) {
			Customer customer = dataBase.getCustomers().get(i);
			Restaurant restaurant = restaurants.get(i % restaurants.size());
			dataBase.addOrder(
					new Order(customer.getCode(), restaurant, String.format("%02d-06-2026", i + 1), 40 + i * 10));
		}
		Customer firstCustomer = dataBase.getCustomers().get(0);
		dataBase.addOrder(new Order(firstCustomer.getCode(), restaurants.get(6), "11-06-2026", 130));
		dataBase.addOrder(new Order(firstCustomer.getCode(), restaurants.get(1), "12-06-2026", 30));

		dataBase.assignRiderToOrder("Ri0001", "O0001");
		dataBase.assignRiderToOrder("Ri0002", "O0002");
		dataBase.assignRiderToOrder("Ri0003", "O0003");
		ConsoleUI.info(
				"Initial data loaded: admin/12345, 3 restaurant admins, 10 customers, 10 restaurants, 5 riders, 12 orders.");
	}
}