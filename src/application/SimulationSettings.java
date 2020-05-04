package application;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * The settings for a simulation
 * 
 * @author Harry Schreiber, Matthew Lew, Jacob Ramseyer, Nicholas Grube
 *
 */
public class SimulationSettings {
	private String name;
	private Drone currentDrone;
	private ArrayList<Location> locations;
	private ArrayList<Meal> meals;
	private int hoursPerShift;
	private int orderUpper;
	private int orderLower;
	
	public SimulationSettings() {
		
	}
	/**
	 * Constructor for a simulation setting
	 * @param name The name of the simulation
	 * @param drone The drone of the simulation
	 * @param locations The locations that a simulation can deliver to
	 * @param meals The meals that the simulation can pick from
	 * @param hoursPerShift The hours in a shift
	 * @param orderUpper The upper number of orders in an hour
	 * @param orderLower The lower number of orders in an hour
	 */
	public SimulationSettings(String name, Drone drone, ArrayList<Location> locations, ArrayList<Meal> meals, int hoursPerShift, int orderUpper, int orderLower) {
		this.name = name;
		this.currentDrone = drone;
		this.locations = locations;
		this.meals = meals;	//TODO: Question 1: I feel like we need to pull this in from it being built in the main frontend side Question 2: Do we need a deep copy constructor here?
		this.hoursPerShift = hoursPerShift;
		this.orderUpper = orderUpper;
		this.orderLower = orderLower;
	}
	
	//TODO: Are we going to do this here or in main?
	public boolean checkPercentage() {
		//TODO: Add real logic here
		return true;
	}
	
	
	/**
	 * @return The Locations in a simulation
	 */
	public ArrayList<Location> getLocations() {
		return locations;
	}

	/**
	 * @param locations The locations in a simulation
	 */
	public void setLocations(ArrayList<Location> locations) {
		this.locations = locations;
	}

	/**
	 * @return The Meals in a simulation
	 */
	public ArrayList<Meal> getMeals() {
		return meals;
	}

	/**
	 * @param meals The Meals in a simulation
	 */
	public void setMeals(ArrayList<Meal> meals) {
		this.meals = meals;
	}
	
	
	/**
	 * @return Name of the current simulation
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name Name of the current simulation
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return Drone of the current simulation
	 */
	public Drone getCurrentDrone() {
		return currentDrone;
	}

	/**
	 * @param currentDrone Drone of the current simulation
	 */
	public void setCurrentDrone(Drone currentDrone) {
		this.currentDrone = currentDrone;
	}

	/**
	 * @return Hours Per Shift in the current simulation
	 */
	public int getHoursPerShift() {
		return hoursPerShift;
	}

	/**
	 * @param hoursPerShift Hours Per Shift in the current simulation
	 */
	public void setHoursPerShift(int hoursPerShift) {
		this.hoursPerShift = hoursPerShift;
	}

	/**
	 * @return Upper bound of the orders per hour in the simulation
	 */
	public int getOrderUpper() {
		return orderUpper;
	}

	/**
	 * @param orderUpper Upper bound of the orders per hour in the simulation
	 */
	public void setOrderUpper(int orderUpper) {
		this.orderUpper = orderUpper;
	}

	/**
	 * @return Lower bound of the orders per hour in the simulation
	 */
	public int getOrderLower() {
		return orderLower;
	}

	/**
	 * @param orderLower Lower bound of the orders per hour in the simulation
	 */
	public void setOrderLower(int orderLower) {
		this.orderLower = orderLower;
	}

	//TODO: We probably just need to pitch this jawn
	public ArrayList<Location> populateLocations(String deliveryPoints) throws FileNotFoundException {
		ArrayList<Location> deliveryLocations = new ArrayList<Location>();
		
		try{
			File deliveryMap = new File(deliveryPoints);
			Scanner scan = new Scanner(deliveryMap);
			String line;
			Scanner lineScan;
			Location current;


			while (scan.hasNextLine()) {
				line = scan.nextLine();
				lineScan = new Scanner(line);
				lineScan.useDelimiter(",");
				current = new Location(lineScan.next(), lineScan.nextInt(), lineScan.nextInt());
				deliveryLocations.add(current);

			}

			scan.close();
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return deliveryLocations;
	}
}
