package application;

import java.util.ArrayList;

public class TravelingSalesmanProblem {

    private ArrayList<Location> locations;
    private int locSize = locations.size();

    /**
     * Constructor
     */
    public TravelingSalesmanProblem(ArrayList<Location> inputLocations) {
        locations = inputLocations;
    }

    /**
     * 
     * @param start
     * @param end
     * @return distance between two locations
     */
    private double distanceBetween(Location start, Location end) {
        //Uses good ole distance formula... (Thanks Pythagoras)
        return (Math.sqrt(Math.pow(start.getX() - end.getX(), 2) 
            + Math.pow(start.getY() - end.getY(), 2)));
    }

    /**
     * 
     * @return a graph-like structure that holds the weight/distance
     *          between two locations
     */
    private double[][] generateLocationGraph() {
    
        double[][] locationGraph = new double[locSize][locSize];
        int row, col;
        
        //Generate graph
        for (row = 0; row < locSize; row++) {
            for (col = 0; col < locSize; col++) {
                locationGraph[row][col] = 
                    distanceBetween(locations.get(row), locations.get(col));
            }
        }
        
        return locationGraph;
    }

    /**
     * This is a fairly simple implementation of the TSP.
     * It utulizes the greedy method of grabbing the next shortest
     * step. Under most circumstances this would not be a
     * preferable solution to TSP, but since we will most likely be
     * visiting less than 5 locations, it should work all right.
     * 
     * @return
     */
    public Location[] GreedyTSP() {

        Location[] orderedLocations = new Location[locSize];
        double[][] locationGraph = generateLocationGraph();
        ArrayList<Integer> visitedLocations = new ArrayList<Integer>();
        int row, col, index;
        double currentMin;

        visitedLocations.add(0);
        for (col = 0; col < locSize - 1 && !visitedLocations.contains(col); col++) {
            currentMin = locationGraph[0][col];
            for (row = 0; row < locSize && !visitedLocations.contains(row); row++) {
                if (locationGraph[row][col] < currentMin) {
                    currentMin = locationGraph[row][col];
                }
            }
            visitedLocations.add(row);
        }

        for (index = 0; index < locSize; index++) {
            orderedLocations[index] = locations.get(visitedLocations.get(index));
        }


        return orderedLocations;
    }




/**
    public Location[] BacktrackingTSP() {
        return BacktrackingTSPRecursiveCall(generateLocationGraph(), 
            new boolean[locSize], 0, 1, 0, Integer.MAX_VALUE);
        
    }

    private Location[] BacktrackingTSPRecursiveCall(double[][] graph, 
        boolean[] visited, int currPos, int count, int cost, int ans) {

            if (count == locSize && graph[currPos][0] > 0) {
                return Math.min(ans, cost + graph[currPos][0]);
            }

            for (int index = 0; index < locSize; index++) {
                if (visited[index] == false && graph[currPos][index] > 0) {

                }
            }
    }
*/

}