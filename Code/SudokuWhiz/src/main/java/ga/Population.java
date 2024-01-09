/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ga;

import java.util.Arrays;

public class Population {
    private Individual[] individuals;	
    
    public Population(int populationSize, int[][] board) {
	//in caso di inizializzazione della popolazione
	individuals = new Individual[populationSize];
        for(int i = 0; i < populationSize; i++) {
            Individual newIndividual = new Individual(board);
            saveIndividual(i, newIndividual);
            
        }
		
    }	
    
    //in caso di creazione della popolazione
    //uso questo costruttore
    public Population(int populationSize) {
        individuals = new Individual[populationSize];
    }
    
    public void removeElites(Individual[] elites) {
	Arrays.sort(this.individuals);
	Individual[] copy = Arrays.copyOfRange(this.individuals, elites.length, this.individuals.length);
	this.individuals = Arrays.copyOf(copy, copy.length);
    }
    
    public Individual getIndividual(int index){
        for(Individual i: individuals){
            if(i.getPosInPopulation()==index) return i;
        }
        return null;
    }
    
    public Individual getIdByPhisicIndex(int index) {
	return individuals[index];
    }	
    
    public Individual getFittest() {
        Individual fittest = individuals[0];
        // itera sulla popolazione per determinare il miglior individuo
        for (int i = 1; i < size(); i++) {
            if (fittest.getFitness() <= getIndividual(i).getFitness()) {
                fittest = getIdByPhisicIndex(i);
            }
        }
        return fittest;
    }  
    public int size() {
        return individuals.length;
    }
    public void saveIndividual(int i, Individual newIndividual) {
        individuals[i] = newIndividual;
        newIndividual.setPosInPopulation(i);
        
    }
    public void saveIndividual(Population p,int i, Individual newIndividual) {
        p.individuals[i] = newIndividual;
        newIndividual.setPosInPopulation(i);
    }
    public Individual [] getIndividuals(){
        return this.individuals;
    }
}