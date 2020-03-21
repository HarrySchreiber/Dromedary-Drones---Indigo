package application;

import java.util.HashMap;
import java.util.Map;

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
	
	
	
	
}
