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
	private ArrayList<Drone> drones;
	private Drone currentDrone;
	private ArrayList<Location> locations;
	private int percentageUsed;
	private int percentageLeft;
	private ArrayList<Meal> meals;
	private int hoursPerShift;
	private int orderUpper;
	private int orderLower;
	
	//TODO: IMPORTANT Lets discus this class again to figure out what we have to actually have here and how we need to populate this stuff
	//Like what are we going to pass it? An XML of all of the settings already filled out? Or are we giving it all of the variables?
	public SimulationSettings() {
		
	}
	
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
