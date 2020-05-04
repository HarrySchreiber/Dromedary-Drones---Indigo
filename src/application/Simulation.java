package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

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
	 * @throws FileNotFoundException 
	 */
	public void runSimulation(ArrayList<Location> locations) throws FileNotFoundException {
		
		SimulationSettings simSettings = new SimulationSettings();

		if (locations.isEmpty()) {
			locations = simSettings.populateLocations("src/application/gccLocationPoints.csv");
		}
		Scanner sc = new Scanner(new File("NewSimData.txt")); 
		StringBuffer buffer = new StringBuffer();
		
		//reads all the lines of the file to buffer
		while (sc.hasNextLine()) {
	         buffer.append(sc.nextLine()+System.lineSeparator());
	    }
		
		String fileContents = buffer.toString();
		String[] fileLines = fileContents.split(System.getProperty("line.separator"));
		int perCountsB [] = new int [50];
	    int burgerCountsB [] = new int [50];
	    int fryCountsB [] = new int [50];
	    int cokeCountsB [] = new int [50];
	    int hoursPerShiftB= 0, lowerBoundB = 0, upperBoundB = 0;
		
		 for(int i = 0; i < fileLines.length; i++) {
		    	String curLine = fileLines[i];
		    	String [] splitVal = curLine.split(": ");
		    	for(int j = 1; j <= 4; j++) {
		    		if(curLine.contains("Order " + String.valueOf(j) + " Percentage:" )){
		    			perCountsB[j-1] = Integer.valueOf(splitVal[1]);
		    			//System.out.println(perCountsB[j-1]);
		    		}
		    		else if (curLine.contains("Order " + String.valueOf(j) + " Burgers:" )){
		    			burgerCountsB[j-1] = Integer.valueOf(splitVal[1]);
		    			//System.out.println(burgerCountsB[j-1]);
		    			
		    		}
		    		else if (curLine.contains("Order " + String.valueOf(j) + " Fries:") ){
		    			fryCountsB[j-1] = Integer.valueOf(splitVal[1]);
		    			//System.out.println(fryCountsB[j-1]);
		    		}
		    		else if (curLine.contains("Order " + String.valueOf(j) + " Cokes:")){
		    			cokeCountsB[j-1] = Integer.valueOf(splitVal[1]);
		    			//System.out.println(cokeCountsB[j-1]);
		    		}
		    		
		    	}
		    	
		    	if (curLine.contains("Hours Per Shift:")) 
		    		hoursPerShiftB = Integer.valueOf(splitVal[1]);
		    	if(curLine.contains("Upper Bound of Orders per Hour:"))
		    		upperBoundB = Integer.valueOf(splitVal[1]);
		    	if(curLine.contains("Lower Bound of Orders per Hour:"))	
		    		lowerBoundB = Integer.valueOf(splitVal[1]);
		 }
		
		 System.out.println(hoursPerShiftB);
		 
		 Meal m1 = new Meal((double) perCountsB[0] / 100);
		 Meal m2 = new Meal((double) perCountsB[1] / 100);
		 Meal m3 = new Meal((double) perCountsB[2] / 100);
		 Meal m4 = new Meal((double) perCountsB[3] / 100);
		
		
		//old 
		/*Meal m1 = new Meal(0.55);
		Meal m2 = new Meal(0.10);
		Meal m3 = new Meal(0.20);
		Meal m4 = new Meal(0.15);*/
		 
		for(int i = 0; i < burgerCountsB[0]; i++) 
			m1.addFoodItem(m1.burgers);
		for(int i = 0; i < fryCountsB[0]; i++) 
			m1.addFoodItem(m1.fries);
		for(int i = 0; i < cokeCountsB[0]; i++) 
			m1.addFoodItem(m1.coke);
		

		
		for(int i = 0; i < burgerCountsB[1]; i++) 
			m2.addFoodItem(m2.burgers);
		for(int i = 0; i < fryCountsB[1]; i++) 
			m2.addFoodItem(m2.fries);
		for(int i = 0; i < cokeCountsB[1]; i++)
		m2.addFoodItem(m2.coke);
		
		for(int i = 0; i < burgerCountsB[2]; i++) 
			m3.addFoodItem(m3.burgers);
		for(int i = 0; i < fryCountsB[2]; i++) 
			m3.addFoodItem(m3.fries);
		for(int i = 0; i < cokeCountsB[0]; i++)
			m3.addFoodItem(m3.coke);
		
		for(int i = 0; i < burgerCountsB[3]; i++) 
			m4.addFoodItem(m4.burgers);
		for(int i = 0; i < fryCountsB[3]; i++) 
			m4.addFoodItem(m4.fries);
		for(int i = 0; i < cokeCountsB[3]; i++) 
			m4.addFoodItem(m4.coke);
		
		ArrayList<Meal> meals = new ArrayList<Meal>();
		meals.add(m1);
		meals.add(m2);
		meals.add(m3);
		meals.add(m4);
		
		Random rnd = new Random();
		for(int simulationNum = 0; simulationNum < 50; simulationNum++) {
			ArrayList<Order> orders = new ArrayList<Order>();
			//TODO: Set these up to dynamically populate: ie take out 4 and 15 and populate from settings
			for(int i = 0; i<hoursPerShiftB; i++) {
				int randomNumberofOrders = rnd.nextInt(((upperBoundB - lowerBoundB) +1) + lowerBoundB);
				for(int j = 0; j<randomNumberofOrders; j++) {
					Order o = new Order(mealPicker(meals), rnd.nextInt(60)+1 + (i*60), locations.get(rnd.nextInt(locations.size())));
					orders.add(o);
				}
			}
			
			
			Collections.sort(orders);
					
			knapsackSimulation(orders);
			fifoSimulation(orders);
		}
		
	}
	
	/**
	 * Contains logic to run a simulation for the knapsack implementation
	 * @param foodList The list of orders that need to be processed
	 */
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
			
			if (onDrone.size() > 1) {
							//TODO: Traveling Salesman Problem goes here probably
				TravelingSalesmanProblem tsp = new TravelingSalesmanProblem(onDrone);
				onDrone = tsp.GeneticAlgorithmTSP();
			}


			

			
			//Run calculations on the time of the simulation
			Location homeBase = new Location("Sac",0,0);	//TODO: Refactor to work with true home base of file Set the drones initial location
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
	
	/**
	 * Contains logic to run a simulation for the fifo implementation
	 * @param foodList The list of orders that need to be processed
	 */
	public void fifoSimulation(ArrayList<Order> foodList) {
		//Make a deep copy of the ArrayList
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
			
			if (onDrone.size() > 1) {
				//TODO: Traveling Salesman Problem goes here probably
				TravelingSalesmanProblem tsp = new TravelingSalesmanProblem(onDrone);
				onDrone = tsp.GeneticAlgorithmTSP();
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
	public Map<Integer, Integer> getFifoData() {
		return fifoData;
	}
	
	/**
	 * @return the data from the knapsack implementation
	 */
	public Map<Integer, Integer> getKnapsackData() {
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
	
}
