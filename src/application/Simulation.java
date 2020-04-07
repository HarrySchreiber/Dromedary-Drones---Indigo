package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Contains all the logic and information to run a simulation
 * 
 * @author Harry Schreiber, Matthew Lew, Jacob Ramseyer, Nicholas Grube
 *
 */
public class Simulation {
	
	private Map<Integer, Integer> fifoData;
	private Map<Integer, Integer> knapsackData;

	/**
	 * Creates a simulation object
	 * TODO: Possibly send in a SimulationSettings Object to be used in the simulation
	 */
	public Simulation() {
		fifoData = new HashMap<Integer,Integer>();
		knapsackData = new HashMap<Integer,Integer>();
	}
	
	/**
	 * Contains the logic required to run the full simulation
	 */
	public void runSimulation() {
		
		Location l1 = new Location("SAC",0,0);
		Location l2 = new Location("HAL",46,-121);
		Location l3 = new Location("PLC",-106,115);
		Location l4 = new Location("TLC",-419,-487);
		Location l5 = new Location("PFAC",27,-784);
		Location l6 = new Location("Hoyt",-318,-98);
		Location l7 = new Location("STEM",-303,78);
		Location l8 = new Location("Hicks",-272,-542);
		Location l9 = new Location("Zerbe",-964,-239);
		Location l10 = new Location("Ketler",-781,164);
		
		ArrayList<Location> locations = new ArrayList<Location>();
		locations.add(l1);
		locations.add(l2);
		locations.add(l3);
		locations.add(l4);
		locations.add(l5);
		locations.add(l6);
		locations.add(l7);
		locations.add(l8);
		locations.add(l9);
		locations.add(l10);
		
		
		Meal m1 = new Meal(0.55);
		Meal m2 = new Meal(0.10);
		Meal m3 = new Meal(0.20);
		Meal m4 = new Meal(0.15);
		
		m1.addFoodItem(m1.burgers);
		m1.addFoodItem(m1.fries);
		m1.addFoodItem(m1.coke);
		
		m2.addFoodItem(m2.burgers);
		m2.addFoodItem(m2.burgers);
		m2.addFoodItem(m2.coke);
		m2.addFoodItem(m2.fries);
		
		m3.addFoodItem(m3.burgers);
		m3.addFoodItem(m3.fries);
		
		m4.addFoodItem(m4.burgers);
		m4.addFoodItem(m4.burgers);
		m4.addFoodItem(m4.fries);
		
		ArrayList<Meal> meals = new ArrayList<Meal>();
		meals.add(m1);
		meals.add(m2);
		meals.add(m3);
		meals.add(m4);
		
		Random rnd = new Random();
		
		ArrayList<Order> orders = new ArrayList<Order>();
		//TODO: Set these up to dynamically populate: ie take out 4 and 15 and populate from settings
		for(int i = 0; i<4; i++) {
			for(int j = 0; j<15; j++) {
				Order o = new Order(mealPicker(meals), rnd.nextInt(60)+1 + (i*60), locations.get(rnd.nextInt(locations.size())));
				orders.add(o);
			}
		}
		
		System.out.println(orders);
	}
	
	/**
	 * @return Average of all of the times of the deliveries
	 */
	public double findAverage() {
		//TODO: Populate the method
		return 0;
	}
	
	/**
	 * 
	 * @return The worst of all of the times of the deliveries
	 * TODO: Do we need to return a double here or can we just return an integer?
	 */
	public double findWorst() {
		//TODO: Populate the method
		return 0;
	}
	
	public Meal mealPicker(ArrayList<Meal> meals) {
		Random rnd = new Random();
		double rndNum = rnd.nextDouble();
		Meal retMeal;
		retMeal = meals.get(0);
		
		double curMaxProb = 0;
		for(int i = 0; i < meals.size(); i++) {
			if(rndNum >= curMaxProb && rndNum <= (meals.get(i).getProbability() + curMaxProb)) {
				retMeal = meals.get(i);
			}
			curMaxProb += meals.get(i).getProbability();
		}
		return retMeal;
	}
	
}
