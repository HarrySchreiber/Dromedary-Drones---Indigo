package application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Contains all the logic and information to run a simulation
 * 
 * @author Harry Schreiber, Matthew Lew, Jacob Ramseyer, Nicholas Grube
 *
 */
public class Simulation {
	
	private static final int MINUTES_IN_AN_HOUR = 60;
	private static final int FEET_IN_A_MILE = 5280;
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
	
	public void knapsackSimulation(ArrayList<Order> foodList) {
		ArrayList<Order> foodListDeepCopy = new ArrayList<Order>();
		for(Order ordr : foodList) {
			foodListDeepCopy.add(new Order(ordr));
		}
		
		double time = 0;
		Drone d = new Drone();
		
		//Run simulation until all orders have been delivered
		while(!foodListDeepCopy.isEmpty()) {
			//Pack the Drone
			ArrayList<Order> onDrone = new ArrayList<Order>();
			boolean full = false;
			//If the drone isn't full and there is still food yet to be delivered
			while(!full && !foodListDeepCopy.isEmpty()) {
				//Figure out if anything has been skipped
				ArrayList<Order> skipped = new ArrayList<Order>();
				for(int i = 0; i < foodListDeepCopy.size(); i++) {
					if(foodListDeepCopy.get(i).isSkipped()) {
						skipped.add(foodListDeepCopy.get(i));
					}
				}
				
				//Loop through skipped orders to add to the drone
				for(Order ordr : skipped) {
					if(ordr.getMeal().calculateWeight() + calculateWeightOnDrone(onDrone) <= d.getMaxCargo()) {
						onDrone.add(ordr);
						foodListDeepCopy.remove(ordr);
					}
				}
				
				//Figure out the heaviest thing to put on the drone 
				double max_weight = 0;
				int max_index = -1;	
				for(int i = 0; i < foodListDeepCopy.size(); i++) {
					//Check its weight and if its within the time frame of execution
					if(max_weight < foodListDeepCopy.get(i).getMeal().calculateWeight() && foodListDeepCopy.get(i).getTimeStamp() <= time) {
						max_weight = foodListDeepCopy.get(i).getMeal().calculateWeight();
						max_index = i;
					}
				}
				
				//Make sure we have a valid index, make sure that the food can actually fit on the drone
				if(max_index >= 0 && foodListDeepCopy.get(max_index).getMeal().calculateWeight() + calculateWeightOnDrone(onDrone) <= d.getMaxCargo()) {
					//Add delivery to drone 
					onDrone.add(foodListDeepCopy.remove(max_index));
					//Mark anything as skipped if it was skipped
					for(int i = 0; i < max_index - 1; i++) {
						foodListDeepCopy.get(i).setSkipped(true);
					}
				}else {
					//If anything else happens then the drone is full and we send it
					full = true;
				}
			}
			
			//TODO: Traveling Salesman Problem goes here probably
			
			//Run calculations on the time of the simulation
			Location homeBase = new Location("Sac",0,0);	//Set the drones initial location
			for(int i = 0; i < onDrone.size(); i++) {
				double distance;
				//First destination needs to leave from the initial location
				if(i == 0) {
					distance = (Math.sqrt(Math.pow((onDrone.get(i).getDeliveryPoint().getX()-homeBase.getX()), 2)+Math.pow((onDrone.get(i).getDeliveryPoint().getY()-homeBase.getY()),2))) / FEET_IN_A_MILE;
				//Every other destination is based on the previous destination
				}else {
					distance = (Math.sqrt(Math.pow(onDrone.get(i).getDeliveryPoint().getX()-onDrone.get(i-1).getDeliveryPoint().getX(), 2)+Math.pow(onDrone.get(i).getDeliveryPoint().getY()-onDrone.get(i-1).getDeliveryPoint().getY(), 2))) / FEET_IN_A_MILE;
				}
				//Calculate the time that it took between 2 locations
				double curTime = distance/d.getAvgCruisingSpeed() * MINUTES_IN_AN_HOUR;
				//Add that time and the unload time to the current time
				time += curTime + d.getUnloadTime();
				//Calculate the number of minutes between order and receiving the food
				int minutesTaken = (int) Math.round(time-onDrone.get(i).getTimeStamp());
				
				//Add the data to the HashMap
				if(knapsackData.containsKey(minutesTaken)) {
					knapsackData.put(minutesTaken, knapsackData.get(minutesTaken) + 1);
				}else {
					knapsackData.put(minutesTaken, 1);
				}
			}
			//Finish out the simulation timings for returning to home base
			if(!onDrone.isEmpty()) {
				//Calculate the distance back to home base
				double finalDistance = Math.sqrt(Math.pow((onDrone.get(onDrone.size()-1).getDeliveryPoint().getX()-homeBase.getX()), 2)+Math.pow((onDrone.get(onDrone.size()-1).getDeliveryPoint().getY()-homeBase.getY()),2)) / FEET_IN_A_MILE;
				//Calculate the time its going to take to get back to base
				double finalTime = finalDistance/d.getAvgCruisingSpeed() * MINUTES_IN_AN_HOUR;
				//Add the time it takes to get to base and the turn around time
				time += finalTime + d.getTurnAroundTime();
			}else {
				//Idle time: ie no orders have come in yet and we don't want to increment by 3 TODO: Rewrite this comment or put somewhere else if neccisary
				time++;
			}
		}
	}
	
	public void fifoSimulation(ArrayList<Order> foodList) {
		ArrayList<Order> foodListDeepCopy = new ArrayList<Order>();
		for(Order ordr : foodList) {
			foodListDeepCopy.add(new Order(ordr));
		}
		
		double time = 0;
		Drone d = new Drone();
		while(!foodListDeepCopy.isEmpty()) {
			//Pack the Drone
			ArrayList<Order> onDrone = new ArrayList<Order>();
			boolean full = false;
			//If the drone is not at capacity already, if the food list has items still, and if the item were looking at came in before or at the current time
			while(!full && !foodListDeepCopy.isEmpty() && foodListDeepCopy.get(0).getTimeStamp() <= time) {
				//If the weight of the next item to be added to the drone comes under the drones weight capacity when added to the drone
				if(foodListDeepCopy.get(0).getMeal().calculateWeight() + calculateWeightOnDrone(onDrone) <= d.getMaxCargo()) {
					onDrone.add(foodListDeepCopy.remove(0));
				}else {
					full = true;
				}
			}
			
			//TODO: Traveling Salesman Problem goes here probably
			
			//Run calculations on the time of the simulation
			Location homeBase = new Location("Sac",0,0);	//Set the drones initial location
			for(int i = 0; i < onDrone.size(); i++) {
				double distance;
				//First destination needs to leave from the initial location
				if(i == 0) {
					distance = (Math.sqrt(Math.pow((onDrone.get(i).getDeliveryPoint().getX()-homeBase.getX()), 2)+Math.pow((onDrone.get(i).getDeliveryPoint().getY()-homeBase.getY()),2))) / FEET_IN_A_MILE;
				//Every other destination is based on the previous destination
				}else {
					distance = (Math.sqrt(Math.pow(onDrone.get(i).getDeliveryPoint().getX()-onDrone.get(i-1).getDeliveryPoint().getX(), 2)+Math.pow(onDrone.get(i).getDeliveryPoint().getY()-onDrone.get(i-1).getDeliveryPoint().getY(), 2))) / FEET_IN_A_MILE;
				}
				//Calculate the time that it took between 2 locations
				double curTime = distance/d.getAvgCruisingSpeed() * MINUTES_IN_AN_HOUR;
				//Add that time and the unload time to the current time
				time += curTime + d.getUnloadTime();
				//Calculate the number of minutes between order and receiving the food
				int minutesTaken = (int) Math.round(time-onDrone.get(i).getTimeStamp());
				
				//Add the data to the HashMap
				if(fifoData.containsKey(minutesTaken)) {
					fifoData.put(minutesTaken, fifoData.get(minutesTaken) + 1);
				}else {
					fifoData.put(minutesTaken, 1);
				}
			}
			//Finish out the simulation timings for returning to home base
			if(!onDrone.isEmpty()) {
				//Calculate the distance back to home base
				double finalDistance = Math.sqrt(Math.pow((onDrone.get(onDrone.size()-1).getDeliveryPoint().getX()-homeBase.getX()), 2)+Math.pow((onDrone.get(onDrone.size()-1).getDeliveryPoint().getY()-homeBase.getY()),2)) / FEET_IN_A_MILE;
				//Calculate the time its going to take to get back to base
				double finalTime = finalDistance/d.getAvgCruisingSpeed() * MINUTES_IN_AN_HOUR;
				//Add the time it takes to get to base and the turn around time
				time += finalTime + d.getTurnAroundTime();
			}else {
				//Idle time: ie no orders have come in yet and we don't want to increment by 3 TODO: Rewrite this comment or put somewhere else if neccisary
				time++;
			}
		}
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
	
	
	/**
	 * @param onDrone An ArrayList of Orders that are on the drone
	 * @return The total weight of the Orders that are on the drone currently
	 */
	public double calculateWeightOnDrone(ArrayList<Order> onDrone) {
		double ret = 0;
		for(Order ordr : onDrone) {
			ret += ordr.getMeal().calculateWeight();
		}
		return ret;
	}
	
	public Map<Integer, Integer> getFifoData() {
		return fifoData;
	}
	
	public Map<Integer, Integer> getKnapsackData() {
		return knapsackData;
	}
	
}
