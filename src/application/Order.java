package application;

import java.util.ArrayList;
import java.util.Random;

/**
 * An order contains what meal a customer ordered, when they ordered it, and where its going
 * 
 * @author Harry Schreiber, Matthew Lew, Jacob Ramseyer, Nicholas Grube
 *
 */
public class Order {
	private Meal meal;
	private int timeStamp;
	private Location deliveryPoint;
	//public int orders = 0;
	ArrayList<Integer> times = new ArrayList<Integer>();
	ArrayList<Order> orders = new ArrayList<Order>();
	int max = 60;
	int min = 1;
	
	/**
	 * Order Constructor
	 *
	 * @param meal The meal the customer ordered
	 * @param timeStamp The minute they ordered the meal
	 * @param deliveryPoint Where the meal order is going
	 */
	public Order(Meal meal, int timeStamp, Location deliveryPoint) {
		//TODO: Do we need deep copy constructors for meal and delivery points?
		this.meal = meal;
		this.timeStamp = timeStamp;
		this.deliveryPoint = deliveryPoint;
	}

	/**
	 * @return The meal that was ordered
	 */
	public Meal getMeal() {
		return meal;
	}

	/**
	 * @param meal The meal that was ordered
	 */
	public void setMeal(Meal meal) {
		//TODO: Do we even need setters and if so do we need to do deep copys?
		this.meal = meal;
	}

	/**
	 * @return The minute the order came in
	 */
	public int getTimeStamp() {
		timeStamp = times.get(0);
		times.remove(0);
		return timeStamp;
	}

	/**
	 * @param timeStamp The minute the order came in
	 */
	public void setTimeStamp(int timeStamp) {
		//TODO: Do we even need setters and if so do we need to do deep copys?
		this.timeStamp = timeStamp;
	}
	public static int rand(int min, int max) {
		return new Random().nextInt(max - min +1)+min;
	}
	
	public void orderTime() {
		for(int i = 0; i<4; i++) {
			for(int j = 0; j<15; j++) {
				times.add(rand(min, max) + (60 * i));
			}
		}
	}
	/*
	 * Adds a new order to the arrayList of Orders
	 * @param meal The meal the customer ordered
	 * @param timeStamp The minute they ordered the meal
	 * @param deliveryPoint Where the meal order is going
	 */
	public ArrayList<Order> addOrder(Meal meal, int timeStamp, Location deliveryPoint){
		orders.add(new Order(meal, timeStamp, deliveryPoint));
		return orders;
		
	}

	/**
	 * @return The delivery point where the meal is going
	 */
	public Location getDeliveryPoint() {
		return deliveryPoint;
	}

	/**
	 * @param deliveryPoint The delivery point where the meal is going
	 */
	public void setDeliveryPoint(Location deliveryPoint) {
		//TODO: Do we even need setters and if so do we need to do deep copys?
		this.deliveryPoint = deliveryPoint;
	}
	
	
}
