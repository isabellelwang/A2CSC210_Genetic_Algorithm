import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;
import java.util.Comparator;

public class GA_Simulation {

  // * The number of individuals in each generation, ***n*** (100)
  // * The number of winners (individuals allowed to reproduce) in each
  // generation, ***k*** (15)
  // * The number of rounds of evolution to run, ***r*** (100)
  // * The initial chromosome size, ***c_0*** (8)
  // * The maximum chromosome size, ***c_max*** (20)
  // * Chance per round of a mutation in each gene, ***m*** (0.01)
  // * Number of states possible per gene, ***g*** (5)
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
   * 
   * @param pop
   */
  public void init(ArrayList<Individual> pop) {
    for (int i = 0; i < numGeneration; i++) {
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

  public ArrayList<Individual> evolve(ArrayList<Individual> pop) {
    ArrayList<Individual> newGen = new ArrayList<>();
    for (int i = 0; i < numGeneration; i++) {
      rankPopulation(pop);
      while (pop.size() > numWinners) {
        pop.remove(pop.size() - 1);
      }
      int parentIndex1 = ThreadLocalRandom.current().nextInt(0, numWinners);
      int parentIndex2 = ThreadLocalRandom.current().nextInt(0, numWinners);
      newGen.add(new Individual(pop.get(parentIndex1), pop.get(parentIndex2), chromosomeMax, chanceOfMutation));
    }
    return newGen;
  }

  // Specifically, show the fitness of the fittest individual in the generation,
  // the ***k***th individual, and the least fit (last ranking) individual,
  // along with the actual chromosome of the best individual.
  public void describeGeneration(ArrayList<Individual> pop) {
    rankPopulation(pop);
    System.out.println("The fittess level of the fittest individual in this generation is " + pop.get(0).getFitness());
    System.out.println("The " + numWinners + "th winner's fitness level is " + pop.get(numWinners).getFitness());
    System.out.println("The least fit inidividual's fitness level is " + pop.get(pop.size() - 1).getFitness());
    System.out.println("The chromosome of the best individual is " + pop.get(0));

  }

  // first it will initialize the population, rank it, and
  // describe it. Then, for each round, it will evolve the population,
  // rank it, and describe it.
  public void run(GA_Simulation gen) {
    ArrayList<Individual> originGen = new ArrayList<>();
    gen.init(originGen);
    for (int i = 1; i < evolutionRounds + 1; i++) {
      System.out.println("Evolution round " + i + ": ");
      gen.rankPopulation(originGen);
      gen.describeGeneration(gen.evolve(originGen));

      System.out.println();
    }
  }

  public static void main(String[] args) {
    GA_Simulation gen = new GA_Simulation(100, 15, 100, 8, 20, 0.01, 5);
    gen.run(gen);
  }
}