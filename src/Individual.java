import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;
import java.lang.Math;

public class Individual {

  /**
   * Chromosome stores the individual's genetic data as an ArrayList of characters
   * Each character represents a gene
   */
  ArrayList<Character> chromosome;

  /**
   * Chooses a letter at random, in the range from A to the number of states
   * indicated
   * 
   * @param num_letters The number of letters available to choose from (number of
   *                    possible states)
   * @return The letter as a Character
   */
  private Character randomLetter(int num_letters) {
    return Character.valueOf((char) (65 + ThreadLocalRandom.current().nextInt(num_letters)));
  }

  /**
   * Method to determine whether a given gene will mutate based on the parameter
   * ***m***
   * 
   * @param m the mutation rate
   * @return true if a number randomly chosen between 0 and 1 is less than
   *         ***m***, else false
   */
  private Boolean doesMutate(float m) {
    float randomNum = ThreadLocalRandom.current().nextInt(0, 1);
    return randomNum < m;
  }

  /**
   * Expresses the individual's chromosome as a String, for display purposes
   * 
   * @return The chromosome as a String
   */
  public String toString() {
    StringBuilder builder = new StringBuilder(chromosome.size());
    for (Character ch : chromosome) {
      builder.append(ch);
    }
    return builder.toString();
  }

  /**
   * Inital constructor to generate initial population members
   * 
   * @param c_0         The initial chromosome size
   * @param num_letters The number of letters available to choose from
   */
  public Individual(int c_0, int num_letters) {
    this.chromosome = new ArrayList<>(c_0);

    for (int i = 0; i < c_0; i++) {
      this.chromosome.add(randomLetter(num_letters));
    }
  }

  /**
   * Second constructor to create parents and offspring chromosomes
   * 
   * @param parent1 The first parent chromosome
   * @param parent2 The second parent chromosome
   * @param c_max   The maximum chromosome size
   * @param m       The chances per round of mutation in each gene
   */
  public Individual(Individual parent1, Individual parent2, int c_max, double m) {
    // fill in
    int randomParent1 = 0; 
    int randomParent2 = 0;  

    //random generates number of characters included in offspring
    while(randomParent1 == 0 || randomParent2 == 0) {
      randomParent1 = ThreadLocalRandom.current().nextInt(1, parent1.chromosome.size() + 1);
      randomParent2 = ThreadLocalRandom.current().nextInt(1, parent2.chromosome.size() + 1);
    }

    chromosome = new ArrayList<>();
    
    //iterates through the randoParent and adds to chromosomes
    for (int i = 0; i < randomParent1; i++) {

      chromosome.add(parent1.chromosome.get(i));

      //checks if mutated and changes chromosome if true
      if (doesMutate((float) m)) {
        this.chromosome.set(i, randomLetter(5));
      }
    }

    //iterates through parent 2 and adds to chromosome 
    for (int i = 0; i < randomParent2; i++) {

      this.chromosome.add((parent2.chromosome.get(i)));

      //checks if mutated and changes chromosome if true
      if (doesMutate((float) m)) {
        this.chromosome.set(i, randomLetter(5));
      }
    }

    //removes last chromosome letters if length of chromosome exceeds the max
    while(chromosome.size() >= c_max) {
      chromosome.remove(chromosome.size() - 1);
    }
  }

  /**
   * Calculates the fitness score of each chromosome
   * 
   * @return The fitness score as an int
   */
  public int getFitness() {
    int fitnessCount = 0;

    //iterates through chromosome.size/2 (plus one if size is odd) and increments/decrements the fitness count
    for (int i = 0; i < Math.round(chromosome.size() / 2.0); i++) {
      if ((chromosome.get(i)).equals(chromosome.get(chromosome.size() - 1 - i))) {
        fitnessCount++;
      } else {
        fitnessCount--;
      }
    }

    //decrease fitness if adjacent chromosomes are the same
    for (int i = 0; i < chromosome.size() - 1; i++) {
      if (chromosome.get(i).equals(chromosome.get(i + 1))) {
        fitnessCount--;
      }

    }
    return fitnessCount;
  }
  
  //testing the methods
  public static void main(String[] args) {
    Individual parent1 = new Individual(8, 5);
    Individual parent2 = new Individual(8, 5);

    System.out.println(parent1);
    System.out.println(parent2);

    Individual child = new Individual(parent1, parent2, 8, 4);
    System.out.println(child);

    System.out.println(child.getFitness());

  }

}
