package application;

import java.util.ArrayList;
import java.util.Collections;

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

public class TspTour {
    private ArrayList<Location> tour = new ArrayList<Location>();
    private double fitness = 0.0;
    private double distance = 0.0;

    public TspTour() {
        for (int index = 0; index < TspTourManager.numLocations(); index++) {
            tour.add(null);
        }
    }

    public TspTour(ArrayList<Location> tour) {
        this.tour = tour;
    }

    public Location[] getAllLocations() {
        Location[] locs = new Location[tourSize()];
        for (int index = 0; index < tourSize(); index++) {
            locs[index] = getLocation(index);
        }
        return locs;
    }

    // Creates a random individual
    public void generateIndividual() {
        // Loop through all our destination cities and add them to our tour
        for (int locIndex = 0; locIndex < TspTourManager.numLocations(); locIndex++) {
            setLocation(locIndex, TspTourManager.getLocation(locIndex));
        }
        // Randomly reorder the tour
        Collections.shuffle(tour);
    }

    // Gets a city from the tour
    public Location getLocation(int tourPosition) {
        return tour.get(tourPosition);
    }

    // Sets a city in a certain position within a tour
    public void setLocation(int tourPosition, Location loc) {
        tour.set(tourPosition, loc);
        // If the tours been altered we need to reset the fitness and distance
        fitness = 0;
        distance = 0;
    }

    // Gets the tours fitness
    public double getFitness() {
        if (fitness == 0.0) {
            fitness = 1/(double)this.getDistance();
        }
        return fitness;
    }
    
    // Gets the total distance of the tour
    public double getDistance(){
        if (distance == 0) {
            int tourDistance = 0;
            // Loop through our tour's cities
            for (int locIndex = 0; locIndex < tourSize(); locIndex++) {

                Location current = getLocation(locIndex);
                Location destination;

                // Check we're not on our tour's last city, if we are set our
                // tour's final destination city to our starting city
                if(locIndex < tourSize() - 1){
                    destination = getLocation(locIndex+1);
                }
                else{
                    destination = getLocation(0);
                }

                // Get the distance between the two cities
                tourDistance += current.distanceTo(destination);
            }
            distance = tourDistance;
        }
        return distance;
    }

    // Get number of cities on our tour
    public int tourSize() {
        return tour.size();
    }
    
    // Check if the tour contains a location
    public boolean containsCity(Location loc){
        return tour.contains(loc);
    }


}