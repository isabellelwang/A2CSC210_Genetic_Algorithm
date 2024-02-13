import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;
import java.util.Comparator;
import java.lang.Math;

public class GA_Simulation {

  /**
   * * numGeneration- The number of individuals in each generation, ***n*** (100)
   * * numWinners - The number of winners (individuals allowed to reproduce) in
   * each generation, ***k*** (15)
   * * evolutionRounds - The number of rounds of evolution to run, ***r*** (100)
   * * chromosomeSize - The initial chromosome size, ***c_0*** (8)
   * * chromosomeMax- The maximum chromosome size, ***c_max*** (20)
   * * chanceOfMutation - Chance per round of a mutation in each gene, ***m***
   * (0.01)
   * * numStates - Number of states possible per gene, ***g*** (5)
   */
  private int numGeneration;
  private int numWinners;
  private int evolutionRounds;
  private int chromosomeSize;
  private int chromosomeMax;
  private double chanceOfMutation;
  private int numStates;

  /**
   * 
   * @param n     int the number of individuals in each generation,
   * @param k     int the number of winners (individuals allowed to reproduce) in
   *              each generation
   * @param r     int the number of rounds of evolution to run
   * @param c_0   int the initial chromosome size
   * @param c_max int the maximum chromosome size
   * @param m     double chance per round of a mutation in each gene
   * @param g     int number of states possible per gene
   */
  public GA_Simulation(int n, int k, int r, int c_0, int c_max, double m, int g) {
    numGeneration = n;
    numWinners = k;
    evolutionRounds = r;
    chromosomeSize = c_0;
    chromosomeMax = c_max;
    chanceOfMutation = m;
    numStates = g;
  }

  /**
   * adds indidividuals to a population arraylist
   * 
   * @param pop Arraylist of numGeneration of individuals
   */
  public void init(ArrayList<Individual> pop) {

    for (int i = 0; i < numGeneration; i++) {
      // add each individuals to the population
      pop.add(new Individual(chromosomeSize, numStates));
    }
  }

  /**
   * Sorts population by fitness score, best first
   * 
   * @param pop: ArrayList of Individuals in the current generation
   * @return: An ArrayList of Individuals sorted by fitness
   */
  public void rankPopulation(ArrayList<Individual> pop) {
    // sort population by fitness
    Comparator<Individual> ranker = new Comparator<>() {
      // this order will sort higher scores at the front
      public int compare(Individual c1, Individual c2) {
        return (int) Math.signum(c2.getFitness() - c1.getFitness());
      }
    };
    pop.sort(ranker);
  }

  /**
   * ranks the old generation, reduces the size of the population to the kth size.
   * Randomly chooses the two parents, creates an offspring and adds it to a new
   * generation.
   * 
   * @param pop ArrayList of individuals
   * @return Arraylist of new generation of individuals
   */
  public ArrayList<Individual> evolve(ArrayList<Individual> pop) {

    ArrayList<Individual> newGen = new ArrayList<>();

    // loop through number of individuals in each generation and rank it
    for (int i = 0; i < numGeneration; i++) {
      rankPopulation(pop);

      // reduces the size of the population to the number of winners size
      while (pop.size() > numWinners) {
        pop.remove(pop.size() - 1);
      }

      // chooses two random parents among the winners and adds it to the new
      // generation
      int parentIndex1 = (int) (Math.random() * numWinners + 0);
      int parentIndex2 = (int) (Math.random() * numWinners + 0);

      newGen.add(new Individual(pop.get(parentIndex1), pop.get(parentIndex2), chromosomeMax, chanceOfMutation));
    }
    return newGen;
  }

  /**
   * prints out the fitness level of fittest individual, numWinner's fitness
   * level, the chromosome of numWinners, the least fit individual's fitness and
   * chromosome, and the best individual's chromosome.
   * 
   * @param pop population Arraylist of the new generation
   */
  public void describeGeneration(ArrayList<Individual> pop) {

    rankPopulation(pop);

    System.out.println(
        "The fittess level of the fittest individual in the new generation is " + pop.get(0).getFitness() + ". ");
    System.out.println("The " + numWinners + "th winner's fitness level is " + pop.get(numWinners).getFitness() + ".");
    System.out.println("The chromosome of the " + numWinners + "th individual is " + pop.get(numWinners) + ".");
    System.out.println("The least fit inidividual's fitness level is " + pop.get(pop.size() - 1).getFitness() + ".");
    System.out.println("The least fit individual's chromosome is " + pop.get(pop.size() - 1) + ".");
    System.out.println("The chromosome of the best individual is " + pop.get(0) + ".");
  }

  /**
   * runs the simulation game, creates a generaton and evolves it for number of
   * evolution rounds and describes the generation
   * 
   * @param gen Simulation of the game
   */
  public void run(GA_Simulation gen) {

    ArrayList<Individual> originGen = new ArrayList<>();

    // creates population and ranks the origin population
    gen.init(originGen);
    gen.rankPopulation(originGen);

    for (int i = 1; i < evolutionRounds + 1; i++) {
      System.out.println("Evolution round " + i + ": ");
      // creates a new arraylist and sets new evolved generation into the new
      // generation
      ArrayList<Individual> newGen = gen.evolve(originGen);
      // sets the original generation to the new generation
      originGen = newGen;
      // describes the generation
      gen.describeGeneration(newGen);
      System.out.println();
    }
  }

  // running the game
  public static void main(String[] args) {
    GA_Simulation gen = new GA_Simulation(100, 15, 100, 8, 20, 0.01, 5);
    gen.run(gen);
  }
}