package application;

import java.util.ArrayList;

public class TravelingSalesmanProblem {

    private ArrayList<Location> locations = new ArrayList<Location>();
    private ArrayList<Order> orderList = new ArrayList<Order>();
    private int locSize;

    /**
     * Constructor
     */
    public TravelingSalesmanProblem(ArrayList<Order> orders) {
        orderList = orders;
        locations.add(new Location("Home",0,0));

        for (int i = 0; i < orders.size(); i++) {
            locations.add(orders.get(i).getDeliveryPoint());
        }
        locSize = locations.size();
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
    public ArrayList<Order> GreedyTSP() {

        Location[] orderedLocations = new Location[locSize];
        double[][] locationGraph = generateLocationGraph();
        ArrayList<Integer> visitedLocations = new ArrayList<Integer>();
        int col, index, currentLocation, nextLocation;
        double currentMin = Double.MAX_VALUE;

        visitedLocations.add(0);
        currentLocation = 0;
        nextLocation = 0;
        
        while (visitedLocations.size() != locSize) {
            for (col = 0; col < locSize; col++) {
                if (!visitedLocations.contains(col)) {
                    if (locationGraph[currentLocation][col] < currentMin) {
                        currentMin = locationGraph[currentLocation][col];
                        nextLocation = col;
                    }
                }
            }
            currentLocation = nextLocation;
            visitedLocations.add(currentLocation);
            currentMin = Double.MAX_VALUE;
        }

        for (index = 0; index < locSize; index++) {
            orderedLocations[index] = locations.get(visitedLocations.get(index));
        }


        return locationToOrder(orderedLocations);
    }

    private ArrayList<Order> locationToOrder(Location[] locs) {
        ArrayList<Order> orderedListOfOrders = new ArrayList<Order>();
        for (int index = 0; index < locs.length; index++) {
            for (int indexTwo = 0; indexTwo < orderList.size(); indexTwo++) {
                if (orderList.get(indexTwo).getDeliveryPoint().equals(locs[index])) {
                    orderedListOfOrders.add(orderList.get(indexTwo));
                    orderList.remove(indexTwo);
                }
            }
        }
        return orderedListOfOrders;

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