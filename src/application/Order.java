package application;

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
	 * @param timeStamp The minute the order came in
	 */
	public void setTimeStamp(int timeStamp) {
		//TODO: Do we even need setters and if so do we need to do deep copys?
		this.timeStamp = timeStamp;
	}
	
	public int getTimeStamp() {
		return timeStamp;
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
