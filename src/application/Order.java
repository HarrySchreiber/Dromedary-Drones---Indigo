package application;
/**
 * An order contains what meal a customer ordered, when they ordered it, and where its going
 * 
 * @author Harry Schreiber, Matthew Lew, Jacob Ramseyer, Nicholas Grube
 *
 */
public class Order implements Comparable<Order> {
	private Meal meal;
	private int timeStamp;
	private Location deliveryPoint;
	private boolean skipped;
	
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
		this.skipped = false;
	}
	
	/**
	 * Copy Constructor
	 * 
	 * @param other The Original Order to be Copied
	 */
	public Order(Order other) {
		this.meal = new Meal(other.meal);
		this.timeStamp = other.timeStamp;
		this.deliveryPoint = other.deliveryPoint;
		this.skipped = false;
	}

	/**
	 * @return If the order has been skipped
	 */
	public boolean isSkipped() {
		return skipped;
	}

	/**
	 * @param skipped If the order has been skipped
	 */
	public void setSkipped(boolean skipped) {
		this.skipped = skipped;
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
		return timeStamp;
	}

	/**
	 * @param timeStamp The minute the order came in
	 */
	public void setTimeStamp(int timeStamp) {
		//TODO: Do we even need setters and if so do we need to do deep copys?
		this.timeStamp = timeStamp;
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

	/**
	 * Logic to compare numbers so we can sort by time
	 */
	@Override
	public int compareTo(Order o) {
		// TODO Auto-generated method stub
		return Integer.valueOf(this.getTimeStamp()).compareTo(o.getTimeStamp());
	}
	
	@Override
	public String toString() {
		return String.valueOf(this.getTimeStamp());
	}
	
}
