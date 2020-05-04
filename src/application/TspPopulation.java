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

public class TspPopulation {

    TspTour[] tours;

    // Construct a population
    public TspPopulation(int populationSize, boolean initialize) {
        tours = new TspTour[populationSize];
        // If we need to initialise a population of tours do so
        if (initialize) {
            // Loop and create individuals
            for (int i = 0; i < populationSize(); i++) {
                TspTour newTour = new TspTour();
                newTour.generateIndividual();
                tours[i] = newTour;
            }
        }
    }
    
    // Saves a tour
    public void saveTour(int index, TspTour tour) {
        tours[index] = tour;
    }
    
    // Gets a tour from population
    public TspTour getTour(int index) {
        return tours[index];
    }

    // Gets the best tour in the population
    public TspTour getFittest() {
        TspTour fittest = getTour(0);
        // Loop through individuals to find fittest
        for (int i = 1; i < populationSize(); i++) {
            if (getTour(i).getFitness() <= fittest.getFitness()) {
                fittest = this.getTour(i);
            }
        }
        return fittest;
    }

    // Gets population size
    public int populationSize() {
        return tours.length;
    }

}