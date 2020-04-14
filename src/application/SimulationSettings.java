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
	private int percentageUsed;
	private int percentageLeft;
	private ArrayList<Meal> meals;
	private int hoursPerShift;
	private int orderUpper;
	private int orderLower;

	public SimulationSettings() {
		
	}
	
	//TODO: IMPORTANT Lets discuss this class again to figure out what we have to actually have here and how we need to populate this stuff
	//Like what are we going to pass it? An XML of all of the settings already filled out? Or are we giving it all of the variables?
	public SimulationSettings(String name, String locationFileName, ArrayList<Meal> meals, int hoursPerShift, int orderUpper, int orderLower) throws FileNotFoundException {
		this.name = name;
		this.currentDrone = new Drone();	//TODO: This will have to be updated with whatever drone the user wants
		this.locations = populateLocations(locationFileName);
		this.percentageUsed = 0;	//TODO: If were going to handle this in main then we may not even need this
		this.percentageLeft = 100;	//TODO: If were going to handle this in main then we may not even need this
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
