package ga;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import utils.ManageMatrix;

public class Algorithm {
    /* GA parametri */
    private static final int GRID_NO = 9; // numero di griglie nel Sudoku
    private static final double MUTATION_RATE = 0.9;
    private static final double CROSSOVER_RATE = 0.5;
    private static final int POPULATION_SIZE = 200;
    private static final int ELITISM_POOL = POPULATION_SIZE / 2;
    private static final int MATING_POOL = POPULATION_SIZE - ELITISM_POOL;

    // Evoluzione della popolazione

    public static Population evolvePopulation(Population pop, int[][] board, List<Integer> hints) {
        Population newPopulation = new Population(pop.size());
        List<Integer> rhints = hints;

        FitnessCalc fc = new FitnessCalc();
        // Elitismo
        Individual elites[] = fc.getElites(pop);
        pop.removeElites(elites);

        for (int i = 0; i < ELITISM_POOL; i++) {
            newPopulation.saveIndividual(i, elites[i]);
        }

        // Si calcolano le probabilità che hanno gli individui di essere scelti.
        List<Individual> parents = selectParents(pop);
        // Dal mating pool generato si effettua il crossover
        for (int i = 0; i < parents.size() - 1; i++) {
            int fatherIndex = new Random().nextInt(parents.size());
            int motherIndex = new Random().nextInt(parents.size());

            Individual father = parents.get(fatherIndex);
            Individual mother = parents.get(motherIndex);
            Individual[] children = crossover(father, mother);
            newPopulation.saveIndividual(newPopulation, i + ELITISM_POOL, children[0]);
            newPopulation.saveIndividual(newPopulation, i + 1 + ELITISM_POOL, children[1]);
        }
        // Mutazione
        for (int i = ELITISM_POOL; i < newPopulation.size(); i++) {
            if (newPopulation.getIdByPhisicIndex(i) != null) {
                mutate(newPopulation.getIdByPhisicIndex(i), rhints);
            }
        }

        return newPopulation;
    }

    // Metodo che effettua il crossover
    private static Individual[] crossover(Individual indiv1, Individual indiv2) {

        int[][] fatherGrid = indiv1.getSudo_m();
        int[][] motherGrid = indiv2.getSudo_m();

        Individual child1 = new Individual(fatherGrid);
        Individual child2 = new Individual(motherGrid);

        // Si prende un numero casuale di celle dal padre
        Set<Integer> fatherSubmatrices = new HashSet<>();
        for (int i = 0; i < GRID_NO; i++) {
            if (Math.random() <= CROSSOVER_RATE) {
                fatherSubmatrices.add(i);
            }
        }

        // Generazione del primo figlio
        for (int i = 0; i < GRID_NO; i++) {
            int[][] source = fatherSubmatrices.contains(i) ? motherGrid : fatherGrid;
            Individual.copySubgrid(i, source, child1.getSudo_m());
        }

        // Generazione del secondo figlio
        for (int i = 0; i < GRID_NO; i++) {
            int[][] source = fatherSubmatrices.contains(i) ? fatherGrid : motherGrid;
            Individual.copySubgrid(i, source, child2.getSudo_m());
        }

        return new Individual[] { child1, child2 };
    }

    // Metodo che effettua la mutazione di un individuo
    public static void mutate(Individual indiv, List<Integer> hints) {
        Random rand = new Random();
        if (Math.random() <= MUTATION_RATE) { // probabilità che indiv subisca una mutazione
            ManageMatrix m = new ManageMatrix();
            int x = rand.nextInt(9);
            int y = rand.nextInt(9);
            int start[] = m.identify_Subgrid_startRowCol(x, y);
            int start_row = start[0];
            int start_col = start[1];
            int[] temp = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };

            int indiv_sudo_m[][] = indiv.getSudo_m();
            int counter = 0;
            // In un array temporaneo vengono copiati gli elementi della sottogriglia scelta
            for (int i = start_row; i < start_row + 3; i++) {
                for (int j = start_col; j < start_col + 3; j++) {
                    if (hints.contains(i * 9 + j)) {
                        temp[counter] = 0;
                    } else
                        temp[counter] = indiv_sudo_m[i][j];
                    counter++;
                }
            }
            // Si effettua la permutazione delle posizioni delle celle
            int random_swaps = rand.nextInt(9);
            counter = 0;
            int first_cell;
            int second_cell;
            while (random_swaps > 0) {
                for (int i = 0; i < 9; i++) {
                    do {
                        first_cell = rand.nextInt(9);
                        second_cell = rand.nextInt(9);
                    } while (temp[first_cell] == 0 || temp[second_cell] == 0);
                    int temp_cell = temp[first_cell];
                    temp[first_cell] = temp[second_cell];
                    temp[second_cell] = temp_cell;
                    random_swaps--;
                }
            }
            for (int i = start_row; i < start_row + 3; i++) {
                for (int j = start_col; j < start_col + 3; j++) {
                    if (hints.contains(i * 9 + j))
                        ;
                    else {
                        if (temp[counter] != 0) {
                            indiv_sudo_m[i][j] = temp[counter];
                        }
                    }
                    counter++;
                }
            }
            indiv.setSudo_m(indiv_sudo_m);
        }
    }

    // Roulette Wheel selection
    private static Individual rouletteWheelSelection(Population population) {
        FitnessCalc c = new FitnessCalc();
        int totalFitness = c.calculateTotalFitness(population);
        int randomValue = new Random().nextInt(totalFitness);
        int cumulativeFitness = 0;
        for (Individual individual : population.getIndividuals()) {
            cumulativeFitness += individual.getFitness();
            if (cumulativeFitness >= randomValue) {
                return individual;
            }
        }
        return population.getIndividual(population.size() - 1);
    }

    // Creazione del mating pool mediante Roulette Wheel selection
    private static List<Individual> selectParents(Population population) {
        List<Individual> parents = new ArrayList<>();
        for (int i = 0; i < MATING_POOL; i++) {
            Individual selected = rouletteWheelSelection(population);
            parents.add(selected);
        }
        return parents;
    }

}