package application;
/**
 * An order contains what meal a customer ordered, when they ordered it, and where its going
 * 
 * @author Harry Schreiber, Matthew Lew, Jacob Ramseyer, Nicholas Grube
 *
 */
public class Order implements Comparable<Order> {
	private int simulationNumber;
	private Meal meal;
	private int timeStampOrder;
	private int timeStampDelivered;
	private Location deliveryPoint;
	private boolean skipped;
	
	/**
	 * Order Constructor
	 *
	 * @param meal The meal the customer ordered
	 * @param timeStamp The minute they ordered the meal
	 * @param deliveryPoint Where the meal order is going
	 */
	public Order(int simulationNumber, Meal meal, int timeStamp, Location deliveryPoint) {
		this.simulationNumber = simulationNumber;
		this.meal = meal;
		this.timeStampOrder = timeStamp;
		this.deliveryPoint = deliveryPoint;
		this.skipped = false;
		this.timeStampDelivered = 0;
	}
	
	/**
	 * Copy Constructor
	 * 
	 * @param other The Original Order to be Copied
	 */
	public Order(Order other) {
		this.simulationNumber = other.simulationNumber;
		this.meal = new Meal(other.meal);
		this.timeStampOrder = other.timeStampOrder;
		this.deliveryPoint = other.deliveryPoint;
		this.skipped = false;
		this.timeStampDelivered = other.timeStampDelivered;
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
	public int getTimeStampOrder() {
		return timeStampOrder;
	}

	/**
	 * @param timeStamp The minute the order came in
	 */
	public void setTimeStampOrder(int timeStamp) {
		//TODO: Do we even need setters and if so do we need to do deep copys?
		this.timeStampOrder = timeStamp;
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
		return Integer.valueOf(this.getTimeStampOrder()).compareTo(o.getTimeStampOrder());
	}
	
	//TODO: Remove after testing
	@Override
	public String toString() {
		return String.valueOf(this.getTimeStampOrder());
	}

	/**
	 * @return The simulation that the order was apart of
	 */
	public int getSimulationNumber() {
		return simulationNumber;
	}

	/**
	 * @param simulationNumber The simulation that the order was apart of
	 */
	public void setSimulationNumber(int simulationNumber) {
		this.simulationNumber = simulationNumber;
	}

	/**
	 * @return The time when the order was delivered
	 */
	public int getTimeStampDelivered() {
		return timeStampDelivered;
	}

	/**
	 * @param timeStampDelivered The time when the order was delivered
	 */
	public void setTimeStampDelivered(int timeStampDelivered) {
		this.timeStampDelivered = timeStampDelivered;
	}
	
}