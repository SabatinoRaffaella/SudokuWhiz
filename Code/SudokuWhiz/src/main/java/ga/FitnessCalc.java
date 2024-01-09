/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ga;

import java.util.Arrays;
import java.util.Comparator;

//da vedere utilità modulo
public class FitnessCalc {
    private static final int ELITISM_COUNT = 100;	
    private static final int SIZE = 9;
	
    // Determino la percentuale del numero di duplicati nelle righe e nelle colonne
 	public int computeFitnessPercerntual(int[][] sudo_m) {
 		int maxFitness = 162; //il numero totale di duplicati in una griglia Sudoku [(9 x 9righe) + (9x9colonne)]
 		int duplicatiRighe = contaDuplicatiNelleRighe(sudo_m);
 		int duplicatiColonne = contaDuplicatiNelleColonne(sudo_m);
 		
 		return ((duplicatiRighe + duplicatiColonne)/maxFitness)*100;
 	}
 	
 	public static int computeFitness(int[][] sudo_m) {
 		
 		int duplicatiRighe = contaDuplicatiNelleRighe(sudo_m);
 		int duplicatiColonne = contaDuplicatiNelleColonne(sudo_m);
 		
 		return duplicatiRighe + duplicatiColonne;
 	}
 	
 // Funzione di supporto per contare i duplicati nelle righe
 	private static int contaDuplicatiNelleRighe(int[][] sudokuGrid) {
 		int duplicati = 0;

 		for (int i = 0; i < SIZE; i++) {
 			duplicati += contaDuplicatiInArray(sudokuGrid[i]);
 		}

 		return duplicati;
 	}

 	// Funzione di supporto per contare i duplicati nelle colonne
 	private static int contaDuplicatiNelleColonne(int[][] sudokuGrid) {
 		int duplicati = 0;

 		for (int j = 0; j < 9; j++) {
 			int[] colonna = new int[9];
 			for (int i = 0; i < 9; i++) {
 				colonna[i] = sudokuGrid[i][j];
 			}
 			duplicati += contaDuplicatiInArray(colonna);
 		}

 		return duplicati;
 	}

 	// Funzione di supporto per contare i duplicati in un array
 	private static int contaDuplicatiInArray(int[] array) {
 		int duplicati = 0;
 		boolean[] presente = new boolean[10]; // Array per tenere traccia della presenza dei numeri da 1 a 9

 		for (int numero : array) {
 			if (numero != 0) {
 				if (presente[numero]) {
 					duplicati++; // Duplicato trovato
 				} else {
 					presente[numero] = true;
 				}
 			}
 		}

 		return duplicati;
 	}
    
    /**
     * 
     * @param pop: Popolazione di cui calcolare il valore di fitness totale
     * @return Restituisce il fitness totale della popolazione
     */
    public int calculateTotalFitness (Population pop){
        int total_fitness = 0;
        for(Individual i : pop.getIndividuals()){
            total_fitness = total_fitness + i.getFitness();            
        }
        return total_fitness;
    }
    
    /**
     * Calcola e restituisce le probabilità di ogni individuo della popolazione
     * per concorrere al roulette wheel selection, in base al proprio valore di fitness.
     * 
     * @param population: Popolazione di cui calcolare le singole probabilità.
     * @return Le probabilità che ha ogni individuo di essere scelto.
     */
    
    //a che serve???
    public double[] getProbabilities(Population population){
        int k = 0;
        int total_fitness = calculateTotalFitness(population);
        Individual[] individuals = population.getIndividuals();
        double probabilities[] = new double[individuals.length];
        
        for(Individual i: individuals){
            probabilities[k] = (double) i.getFitness() / total_fitness;
            k++; 
        }
        
        double sum = Arrays.stream(probabilities).sum();
        for (int i = 0; i < individuals.length; i++) {
            probabilities[i] /= sum;	//relative fitness per ogni individuo
        }
        return probabilities;
    }
    
    /**
     * Restituisce la lista ordinata degli individui basandosi su una copia degli individui
     * data la popolazione per fitness, e ne restituisce soltanto il tot necessario per l'elitismo.
     * @param pop: Popolazione di appoggio di cui riordinare gli individui.
     * @return Lista riordinata degli individui.
     */
    public Individual[] getElites(Population pop){
        Individual[] individuals_copy = Arrays.copyOf(pop.getIndividuals(),pop.getIndividuals().length);
        Arrays.sort(individuals_copy, Comparator.comparingInt(individual -> individual.getFitness()));
        Individual [] to_return = Arrays.copyOf(individuals_copy, ELITISM_COUNT);
        return to_return;
    }
}
