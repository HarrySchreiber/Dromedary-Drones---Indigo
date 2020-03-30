package application;

import java.util.ArrayList;

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
		Drone standardDrone = new Drone();
	}
	
	public boolean checkPercentage() {
		//TODO: Add real logic here
		return true;
	}
}
