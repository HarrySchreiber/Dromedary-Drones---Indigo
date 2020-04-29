package application;

/**
 * A drone object contains data about a specific drone
 * 
 * @author Harry Schreiber, Matthew Lew, Jacob Ramseyer, Nicholas Grube
 *
 */
public class Drone {
	private String droneID;
	private String name;
	private double maxCargo;
	private double avgCruisingSpeed;
	private double maxFlightTime;
	private double turnAroundTime;
	private double unloadTime;
	
	/**
	 * No argument constructor for all default values as specified in the instructions
	 * will be option to select from the drones list as "Default Drone"
	 * So we can call this argument easier when its selected from drone list..
	 */
	public Drone() {
		this.setDroneID("1");
		this.name = "Default Grove City Drone";
		this.maxCargo = 12;
		this.avgCruisingSpeed = 20;
		this.maxFlightTime = 20;
		this.turnAroundTime = 3;
		this.unloadTime = 0.5;
	}
	
	/**
	 * @param name Name of the Drone
	 * @param maxCargo The Max Cargo the drone can lift in pounds
	 * @param avgCruisingSpeed the cruising speed of the drone in miles per hour
	 * @param maxFlightTime the max amount of time the drone can be making deliveries in minutes
	 * @param turnAroundTime the amount of time the drone takes to be packaged in minutes
	 * @param unloadTime the amount of time the drone takes to deliver an order in minutes
	 */
	public Drone(String droneID, String name, double maxCargo, double avgCruisingSpeed, double maxFlightTime, double turnAroundTime, double unloadTime) {
		this.name = name;
		this.maxCargo = maxCargo;
		this.avgCruisingSpeed = avgCruisingSpeed;
		this.maxFlightTime = maxFlightTime;
		this.turnAroundTime = turnAroundTime;
		this.unloadTime = unloadTime;
	}

	/**
	 * @return the name of the drone
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name of the drone
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return The Max Cargo the drone can lift in pounds
	 */
	public double getMaxCargo() {
		return maxCargo;
	}

	/**
	 * @param maxCargo The Max Cargo the drone can lift in pounds
	 */
	public void setMaxCargo(double maxCargo) {
		this.maxCargo = maxCargo;
	}

	/**
	 * @return the cruising speed of the drone in miles per hour
	 */
	public double getAvgCruisingSpeed() {
		return avgCruisingSpeed;
	}
	/**
	 * @param avgCrusingSpeed the cruising speed of the drone in miles per hour
	 */
	public void setAvgCrusingSpeed(double avgCruisingSpeed) {
		this.avgCruisingSpeed = avgCruisingSpeed;
	}

	/**
	 * @return the max amount of time the drone can be making deliveries in minutes
	 */
	public double getMaxFlightTime() {
		return maxFlightTime;
	}

	/**
	 * @param maxFlightTime the max amount of time the drone can be making deliveries in minutes
	 */
	public void setMaxFlightTime(double maxFlightTime) {
		this.maxFlightTime = maxFlightTime;
	}

	/**
	 * @return the amount of time the drone takes to be packaged in minutes
	 */
	public double getTurnAroundTime() {
		return turnAroundTime;
	}

	/**
	 * @param turnAroundTime the amount of time the drone takes to be packaged in minutes
	 */
	public void setTurnAroundTime(double turnAroundTime) {
		this.turnAroundTime = turnAroundTime;
	}

	/**
	 * @return the amount of time the drone takes to deliver an order in minutes
	 */
	public double getUnloadTime() {
		return unloadTime;
	}

	/**
	 * @param unloadTime the amount of time the drone takes to deliver an order in minutes
	 */
	public void setUnloadTime(double unloadTime) {
		this.unloadTime = unloadTime;
	}

	/**
	 * @return The id of the Drone
	 */
	public String getDroneID() {
		return droneID;
	}

	/**
	 * @param droneID The id of the Drone
	 */
	public void setDroneID(String droneID) {
		this.droneID = droneID;
	}
	
	
}
