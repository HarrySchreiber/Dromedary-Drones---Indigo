package application;

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

public class TspGeneticAlgorithm {

     /* GA parameters */
     private static final double mutationRate = 0.015;
     private static final int tournamentSize = 5;
     private static final boolean elitism = true;
 
     // Evolves a population over one generation
     public static TspPopulation evolvePopulation(TspPopulation pop) {
         TspPopulation newPopulation = new TspPopulation(pop.populationSize(), true);
 
         // Keep our best individual if elitism is enabled
         int elitismOffset = 0;
         if (elitism) {
             newPopulation.saveTour(0, pop.getFittest());
             elitismOffset = 1;
         }
 
         // Crossover population
         // Loop over the new population's size and create individuals from
         // Current population
         for (int i = elitismOffset; i < newPopulation.populationSize(); i++) {
             // Select parents
             TspTour parent1 = tournamentSelection(pop);
             TspTour parent2 = tournamentSelection(pop);
             // Crossover parents
             TspTour child = crossover(parent1, parent2);
             // Add child to new population
             newPopulation.saveTour(i, child);
         }
 
         // Mutate the new population a bit to add some new genetic material
         for (int i = elitismOffset; i < newPopulation.populationSize(); i++) {
             mutate(newPopulation.getTour(i));
         }
 
         return newPopulation;
     }
 
     // Applies crossover to a set of parents and creates offspring
     public static TspTour crossover(TspTour parent1, TspTour parent2) {
         // Create new child tour
         TspTour child = new TspTour();
 
         // Get start and end sub tour positions for parent1's tour
         int startPos = (int) (Math.random() * parent1.tourSize());
         int endPos = (int) (Math.random() * parent1.tourSize());
 
         // Loop and add the sub tour from parent1 to our child
         for (int i = 0; i < child.tourSize(); i++) {
             // If our start position is less than the end position
             if (startPos < endPos && i > startPos && i < endPos) {
                 child.setLocation(i, parent1.getLocation(i));
             } // If our start position is larger
             else if (startPos > endPos) {
                 if (!(i < startPos && i > endPos)) {
                     child.setLocation(i, parent1.getLocation(i));
                 }
             }
         }
 
         // Loop through parent2's city tour
         for (int i = 0; i < parent2.tourSize(); i++) {
             // If child doesn't have the city add it
             if (!child.containsCity(parent2.getLocation(i))) {
                 // Loop to find a spare position in the child's tour
                 for (int ii = 0; ii < child.tourSize(); ii++) {
                     // Spare position found, add city
                     if (child.getLocation(ii) == null) {
                         child.setLocation(ii, parent2.getLocation(i));
                         break;
                     }
                 }
             }
         }
         return child;
     }
 
     // Mutate a tour using swap mutation
     private static void mutate(TspTour tour) {
         // Loop through tour cities
         for(int tourPos1=0; tourPos1 < tour.tourSize(); tourPos1++){
             // Apply mutation rate
             if(Math.random() < mutationRate){
                 // Get a second random position in the tour
                 int tourPos2 = (int) (tour.tourSize() * Math.random());
 
                 // Get the cities at target position in tour
                 Location locationOne = tour.getLocation(tourPos1);
                 Location locationTwo = tour.getLocation(tourPos2);
 
                 // Swap them around
                 tour.setLocation(tourPos2, locationOne);
                 tour.setLocation(tourPos1, locationTwo);
             }
         }
     }
 
     // Selects candidate tour for crossover
     private static TspTour tournamentSelection(TspPopulation pop) {
         // Create a tournament population
         TspPopulation tournament = new TspPopulation(tournamentSize, false);
         // For each place in the tournament get a random candidate tour and
         // add it
         for (int i = 0; i < tournamentSize; i++) {
             int randomId = (int) (Math.random() * pop.populationSize());
             tournament.saveTour(i, pop.getTour(randomId));
         }
         // Get the fittest tour
         TspTour fittest = tournament.getFittest();
         return fittest;
     }

}