package application;

/**
 * A Location is a structure that contains the name of a place and its coordinates
 * 
 * @author Harry Schreiber, Matthew Lew, Jacob Ramseyer, Nicholas Grube
 *
 */
public class Location {
	private String name;
	private int x;
	private int y;
	
	/**
	 * Location Constructor
	 * 
	 * @param name Name of the Location
	 * @param x	X coordinate of the Location
	 * @param y Y coordinate of the Location
	 */
	public Location(String name, int x, int y) {
		this.name = name;
		this.x = x;
		this.y = y;
	}

	/**
	 * @return Name of the Location
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name Name of the Location
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return X coordinate of the Location
	 */
	public int getX() {
		return x;
	}

	/**
	 * @param x X coordinate of the Location
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * @return Y coordinate of the Location
	 */
	public int getY() {
		return y;
	}

	/**
	 * @param y Y coordinate of the Location
	 */
	public void setY(int y) {
		this.y = y;
	}
	
	
	
}
