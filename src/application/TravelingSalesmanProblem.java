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
        TspTourManager.clearLocations();
        for (int i = 0; i < orders.size(); i++) {
            TspTourManager.addLocation(orders.get(i).getDeliveryPoint());
            locations.add(orders.get(i).getDeliveryPoint());
        }
        locSize = locations.size();
        
        //TspTourManager.addAll(locations);
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
                locations.get(row).distanceTo( locations.get(col));
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

    public ArrayList<Order> GeneticAlgorithmTSP() {
        int factor = TspTourManager.numLocations();
        if (TspTourManager.numLocations() > 0) {
            TspPopulation pop = new TspPopulation(3*factor, true);

            for (int i = 0; i < 5*factor; i++) {
                pop = TspGeneticAlgorithm.evolvePopulation(pop);
            }

            return locationToOrder(pop.getFittest().getAllLocations());
        }
        else {
            return locationToOrder((Location[]) locations.toArray());
        }


    }

}