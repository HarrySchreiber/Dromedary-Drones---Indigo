package application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Random;

/**
 * Contains all the logic and information to run a simulation
 * 
 * @author Harry Schreiber, Matthew Lew, Jacob Ramseyer, Nicholas Grube
 *
 */
public class Simulation {
	
	private static final int MINUTES_IN_AN_HOUR = 60;
	private static final int FEET_IN_A_MILE = 5280;
	private ArrayList<Order> fifoData;
	private ArrayList<Order> knapsackData;
	private int maxLocations;


	private String chosenAlgorithm = "Greedy";

	public void setAlgorithm(String algo) {
		chosenAlgorithm = algo;
	}

	/**
	 * Creates a simulation object
	 */
	public Simulation() {
		fifoData = new ArrayList<Order>();
		knapsackData = new ArrayList<Order>();
	}
	
	/**
	 * Contains the logic required to run the full simulation
	 * @throws FileNotFoundException 
	 */
	public void runSimulation(SimulationSettings sim, Drone d) {
		fifoData = new ArrayList<Order>();
		knapsackData = new ArrayList<Order>();
		
		maxLocations = maxNumberOfLocationsToDeliverTo(sim.getCurrentDrone(), sim.getLocations());
		
		Random rnd = new Random();
		for(int simulationNum = 0; simulationNum < 50; simulationNum++) {
			ArrayList<Order> orders = new ArrayList<Order>();
			for(int i = 0; i< sim.getHoursPerShift(); i++) {
				int numberOfOrdersThisHour = rnd.nextInt((sim.getOrderUpper()-sim.getOrderLower())+1)+sim.getOrderLower();
				for(int j = 0; j< numberOfOrdersThisHour; j++) {
					Order o = new Order(simulationNum, mealPicker(sim.getMeals()), rnd.nextInt(60)+1 + (i*60), sim.getLocations().get(rnd.nextInt(sim.getLocations().size())));
					orders.add(o);
				}
			}
			
			
			Collections.sort(orders);
					
			knapsackSimulation(orders, d);
			fifoSimulation(orders, d);
		}
		
	}
	
	/**
	 * Contains logic to run a simulation for the knapsack implementation
	 * @param foodList The list of orders that need to be processed
	 */
	public void knapsackSimulation(ArrayList<Order> foodList, Drone d) {
		ArrayList<Order> foodListDeepCopy = new ArrayList<Order>();
		for(Order ordr : foodList) {
			foodListDeepCopy.add(new Order(ordr));
		}
		
		double time = 0;

		
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
					if(ordr.getMeal().calculateWeight() + calculateWeightOnDrone(onDrone) <= d.getMaxCargo() && checkDroneCargoAgainstMaxDeliveryPoints(onDrone, ordr)) {
						onDrone.add(ordr);
						foodListDeepCopy.remove(ordr);
					}
				}
				
				//Figure out the heaviest thing to put on the drone 
				double max_weight = 0;
				int max_index = -1;	
				for(int i = 0; i < foodListDeepCopy.size(); i++) {
					//Check its weight and if its within the time frame of execution
					if(max_weight < foodListDeepCopy.get(i).getMeal().calculateWeight() && foodListDeepCopy.get(i).getTimeStampOrder() <= time) {
						max_weight = foodListDeepCopy.get(i).getMeal().calculateWeight();
						max_index = i;
					}
				}
				
				//Make sure we have a valid index, make sure that the food can actually fit on the drone
				if(max_index >= 0 && foodListDeepCopy.get(max_index).getMeal().calculateWeight() + calculateWeightOnDrone(onDrone) <= d.getMaxCargo() && checkDroneCargoAgainstMaxDeliveryPoints(onDrone, foodListDeepCopy.get(max_index))) {
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
			
			
		if (onDrone.size() > 1 ) {
			TravelingSalesmanProblem tsp = new TravelingSalesmanProblem(onDrone);
			if (chosenAlgorithm.contains("Genetic")) {
				onDrone = tsp.GeneticAlgorithmTSP();
			}
			else {
				onDrone = tsp.GreedyTSP();
			}		
		}

			

			
			//Run calculations on the time of the simulation
			for(int i = 0; i < onDrone.size(); i++) {
				double distance;
				//First destination needs to leave from the initial location
				if(i == 0) {
					distance = (Math.sqrt(Math.pow((onDrone.get(i).getDeliveryPoint().getX()-0), 2)+Math.pow((onDrone.get(i).getDeliveryPoint().getY()-0),2))) / FEET_IN_A_MILE;
				//Every other destination is based on the previous destination
				}else {
					distance = (Math.sqrt(Math.pow(onDrone.get(i).getDeliveryPoint().getX()-onDrone.get(i-1).getDeliveryPoint().getX(), 2)+Math.pow(onDrone.get(i).getDeliveryPoint().getY()-onDrone.get(i-1).getDeliveryPoint().getY(), 2))) / FEET_IN_A_MILE;
				}
				//Calculate the time that it took between 2 locations
				double curTime = distance/d.getAvgCruisingSpeed() * MINUTES_IN_AN_HOUR;
				//Add that time and the unload time to the current time
				time += curTime + d.getUnloadTime();
				//Add the data to the ArrayList
				onDrone.get(i).setTimeStampDelivered((int) Math.round(time));
				knapsackData.add(onDrone.get(i));
			}
			//Finish out the simulation timings for returning to home base
			if(!onDrone.isEmpty()) {
				//Calculate the distance back to home base
				double finalDistance = Math.sqrt(Math.pow((onDrone.get(onDrone.size()-1).getDeliveryPoint().getX()-0), 2)+Math.pow((onDrone.get(onDrone.size()-1).getDeliveryPoint().getY()-0),2)) / FEET_IN_A_MILE;
				//Calculate the time its going to take to get back to base
				double finalTime = finalDistance/d.getAvgCruisingSpeed() * MINUTES_IN_AN_HOUR;
				//Add the time it takes to get to base and the turn around time
				time += finalTime + d.getTurnAroundTime();
			}else {
				//Idle time: ie no orders have come in yet and we don't want to increment by the turn over time
				time++;
			}
		}
	}
	
	/**
	 * Contains logic to run a simulation for the fifo implementation
	 * @param foodList The list of orders that need to be processed
	 */
	public void fifoSimulation(ArrayList<Order> foodList, Drone d) {
		//Make a deep copy of the ArrayList
		ArrayList<Order> foodListDeepCopy = new ArrayList<Order>();
		for(Order ordr : foodList) {
			foodListDeepCopy.add(new Order(ordr));
		}
		
		double time = 0;
		
		while(!foodListDeepCopy.isEmpty()) {
			//Pack the Drone
			ArrayList<Order> onDrone = new ArrayList<Order>();
			boolean full = false;
			//If the drone is not at capacity already, if the food list has items still, and if the item were looking at came in before or at the current time
			while(!full && !foodListDeepCopy.isEmpty() && foodListDeepCopy.get(0).getTimeStampOrder() <= time) {
				//If the weight of the next item to be added to the drone comes under the drones weight capacity when added to the drone
				if(foodListDeepCopy.get(0).getMeal().calculateWeight() + calculateWeightOnDrone(onDrone) <= d.getMaxCargo()  && checkDroneCargoAgainstMaxDeliveryPoints(onDrone, foodListDeepCopy.get(0))) {
					onDrone.add(foodListDeepCopy.remove(0));
				}else {
					full = true;
				}
			}
			
			if (onDrone.size() > 1 ) {
				TravelingSalesmanProblem tsp = new TravelingSalesmanProblem(onDrone);
				if (chosenAlgorithm.contains("Genetic")) {
					onDrone = tsp.GeneticAlgorithmTSP();
				}
				else {
					onDrone = tsp.GreedyTSP();
				}		
			}
			
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
				//Add the data to the ArrayList
				onDrone.get(i).setTimeStampDelivered((int) Math.round(time));
				fifoData.add(onDrone.get(i));
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
				//Idle time: ie no orders have come in yet and we don't want to increment by turn around time
				time++;
			}
		}
	}
	
	/**
	 * @param map The map that needs to be calculated on
	 * @return Average of all of the times of the deliveries
	 */
	public double findAverage(Map<Integer, Integer> map) {
		double ret = 0;	//The average time taken
		int totalDeliveries = 0;	//The total number of deliveries made
		
		//Loop through all the keys
		for(Integer key : map.keySet()) {
			totalDeliveries += map.get(key);	//Add up all the deliveries
			ret += map.get(key) * key;	//Add up all the deliveries
		}
		
		ret /= totalDeliveries;	//Make the final average calcualtion
		
		return ret;
	}
	
	/**
	 * @param map The map that needs to be calculated on
	 * @return Worst of all of the times of the deliveries
	 */
	public double findWorst(Map<Integer, Integer> map) {
		return Collections.max(map.keySet());
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
	
	/**
	 * @return the data from the fifo implementation
	 */
	public ArrayList<Order> getFifoData() {
		return fifoData;
	}
	
	/**
	 * @return the data from the knapsack implementation
	 */
	public ArrayList<Order> getKnapsackData() {
		return knapsackData;
	}

	/**
	 * Picks a meal based on a random number generated
	 * @param meals A list of meals to be picked from for this simulation
	 * @return A meal that was chosen by the random number
	 */
	public Meal mealPicker(ArrayList<Meal> meals) {
		Random rnd = new Random();
		double rndNum = rnd.nextDouble();	//Chose the random number between 0.0->1.0
		Meal retMeal;
		retMeal = meals.get(0);
		
		//Find which meal falls into that section of the probability
		double curMaxProb = 0;
		for(int i = 0; i < meals.size(); i++) {
			//If the random number is greater than the current max probability and less than the future max probability 
			if(rndNum >= curMaxProb && rndNum <= (meals.get(i).getProbability() + curMaxProb)) {
				retMeal = new Meal(meals.get(i));
			}
			//Add the future max probability
			curMaxProb += meals.get(i).getProbability();
		}
		return retMeal;
	}
	
	/**
	 * Method for finding the max distance between any 2 points on our map
	 * @param locations The array of locations to be assessed
	 * @return The max distance between to points on the map
	 */
	public double getMaxDistanceOnMap(ArrayList<Location> locations) {
		//Standard distance formula mixed with standard max finding, the nested loop run n(n+1)/2 times rather than n*n, thanks Gauss
		double max = 0;
		for(int i = 0; i < locations.size(); i++) {
			for(int j = i; j < locations.size(); j++) {
				if((Math.sqrt(Math.pow((locations.get(i).getX()-locations.get(j).getX()), 2)+Math.pow((locations.get(i).getY()-locations.get(j).getY()), 2))) > max) {
					max = (Math.sqrt(Math.pow((locations.get(i).getX()-locations.get(j).getX()), 2)+Math.pow((locations.get(i).getY()-locations.get(j).getY()), 2)));
				}
			}
		}
		return max;
	}
	
	/**
	 * Will return the max locations a drone can deliver to on a single trip
	 * @param drone The drone in this simulation
	 * @param locations Array of all possible Locations
	 * @return the max locations a drone can deliver to on a single trip
	 */
	public int maxNumberOfLocationsToDeliverTo(Drone drone, ArrayList<Location> locations) {
		int ret = 0;
		double maxDistance = getMaxDistanceOnMap(locations) / FEET_IN_A_MILE;
		double timeToGoMaxDistance = maxDistance/drone.getAvgCruisingSpeed() * MINUTES_IN_AN_HOUR;
		ret = (int) (drone.getMaxFlightTime()/timeToGoMaxDistance);
		return ret;
	}
	
	/**
	 * Checks to see if a drone can make it to all of the locations in the alloted max flight time
	 * @param ordersOnDrone The items currently on the drone
	 * @param prospectiveOrder The item were trying to pack on the drone
	 * @return true if we can fit the order on the drone, false if we cannot fit the item on the drone
	 */
	public boolean checkDroneCargoAgainstMaxDeliveryPoints(ArrayList<Order> ordersOnDrone, Order prospectiveOrder) {
		//Add all of the current items on the drone to a list of locations
		ArrayList<Location> locations = new ArrayList<Location>();
		for(Order order : ordersOnDrone) {
			boolean locationNotPresent = true;
			
			for(Location location : locations) {
				if(location.equals(order.getDeliveryPoint())) {
					locationNotPresent = false;
				}
			}
			
			if(locationNotPresent) {
				locations.add(order.getDeliveryPoint());
			}
		}
		
		//If the prospective order had a location outside of the locations already present then we add that to the locations array
		boolean locationNotPresent = true;
		
		for(Location location : locations) {
			if(location.equals(prospectiveOrder.getDeliveryPoint())) {
				locationNotPresent = false;
			}
		}
		
		if(locationNotPresent) {
			locations.add(prospectiveOrder.getDeliveryPoint());
		}
		
		//If the number of locations exceeds the max number of locations we can have return false
		return locations.size() <= maxLocations;
	}
}
