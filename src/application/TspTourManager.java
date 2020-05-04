package application;

import java.util.ArrayList;


/**The code was adapted from the following url:
 * http://www.theprojectspot.com/tutorial-post/applying-a-genetic-algorithm-to-the-travelling-salesman-problem/5
 * 
 * The original author is Lee Jacobson
 * 
 * The code makes use of genetic algorithms to generate a more optimal 
 * tsp solution.
 * 
 * Much of the code has mainly cosmetic changes with 
 * some slight logic change.
 */
public class TspTourManager {

    // Holds our locations
    private static ArrayList<Location> locations = new ArrayList<Location>();

    public static void addAll(ArrayList<Location> locs) {
        for (int i = 0; i < locs.size(); i++) {
            locations.add(locs.get(i));
        }
    }
    // Adds a destination city
    public static void addLocation(Location location) {
        locations.add(location);
        locations = removeDuplicates(locations); 
    }
    
    // Get a city
    public static Location getLocation(int index){
        return (Location)locations.get(index);
    }
    
    // Get the number of destination cities
    public static int numLocations(){
        return locations.size();
    }

    public static void clearLocations() {
        locations = new ArrayList<Location>();
    }

    public static ArrayList<Location> removeDuplicates(ArrayList<Location> locs) { 
  
        ArrayList<Location> newList = new ArrayList<Location>(); 

        for (Location loc : locs) { 
            if (!newList.contains(loc)) { 
                newList.add(loc); 
            } 
        } 
  
        // return the new list 
        return newList; 
    } 

}