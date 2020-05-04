package application;

/**
 * A food item stores what the food is and how much it weighs
 * 
 * @author Harry Schreiber, Matthew Lew, Jacob Ramseyer, Nicholas Grube
 *
 */
public class FoodItem {
	private String name;
	private double weight;
	
	/**
	 * FoodItem Constructor
	 * 
	 * @param name Name of the Food Item
	 * @param weight Weight of the Food Item in pounds
	 */
	public FoodItem(String name, double weight) {
		this.name = name;
		this.weight = weight;
	}
	
	public FoodItem(FoodItem other) {
		this.name = other.name;
		this.weight = other.weight;
	}

	/**
	 * @return Name of food item
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name Name of food item
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return Weight of food item in pounds
	 */
	public double getWeight() {
		return weight;
	}

	/**
	 * @param weight Weight of food item in pounds
	 */
	public void setWeight(double weight) {
		this.weight = weight;
	}
	@Override
	public String toString() {
		return name;
	}
}
