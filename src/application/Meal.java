package application;

import java.util.ArrayList;

/**
 * Contains the food items associated with a meal combo
 * 
 * @author Harry Schreiber, Matthew Lew, Jacob Ramseyer, Nicholas Grube
 *
 */
public class Meal {
	ArrayList<FoodItem> foodItems;
	
	/**
	 * Meal Constructor
	 */
	public Meal() {
		this.foodItems = new ArrayList<FoodItem>();
	}
	
	/**
	 * TODO: Decide if we want to calculate this everytime we need it or just keep a member variable that updates every time we add a food
	 * @return The total weight of the meal
	 */
	public double calculateWeight() {
		//TODO: Actually Populate the method
		return 0;
	}
	
	/**
	 * TODO: If we decide to update the weight member variable just remember to do that here and add the member variable
	 * @param item The food item to be passed to the list
	 */
	public void addFoodItem(FoodItem item) {
		
	}
}
