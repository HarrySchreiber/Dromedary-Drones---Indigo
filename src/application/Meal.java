package application;

import java.util.ArrayList;

/**
 * Contains the food items associated with a meal combo
 * 
 * @author Harry Schreiber, Matthew Lew, Jacob Ramseyer, Nicholas Grube
 *
 */
public class Meal {
	FoodItem burgers = new FoodItem("Burger", 0.375);
	FoodItem fries = new FoodItem("Fries", 0.25);
	FoodItem coke = new FoodItem("coke", 0.875);
	ArrayList<FoodItem> foodItems;
	private double totalWeight;
	private double probability;
	
	/**
	 * Meal Constructor
	 */
	public Meal(double probability) {
		this.foodItems = new ArrayList<FoodItem>();
		this.totalWeight = 0;
		this.probability = probability;
	}
	
	public Meal(Meal other) {
		this.foodItems = other.foodItems;
		this.totalWeight = other.totalWeight;
		this.probability = other.probability;
	}
	
	/**
	 * TODO: Decide if we want to calculate this everytime we need it or just keep a member variable that updates every time we add a food
	 * @return The total weight of the meal
	 * can be changed 
	 */
	public double calculateWeight() {
		//TODO: Actually Populate the method
		return totalWeight;
	}
	
	public String foodItemstoString() {
		return foodItems.toString();
		
	}
	
	/**
	 * TODO: If we decide to update the weight member variable just remember to do that here and add the member variable
	 * @param item The food item to be passed to the list
	 * can be changed 
	 * Should be run everything we increment a food in the simulation setting page
	 */
	public void addFoodItem(FoodItem item) {
		foodItems.add(item);
		totalWeight += item.getWeight();
	}

	public double getProbability() {
		return probability;
	}

	public void setProbability(double probability) {
		this.probability = probability;
	}
}
