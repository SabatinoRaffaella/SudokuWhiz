/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ga;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import utils.BoardRandomizer;
import utils.ManageMatrix;

//è la griglia Sudoku

public class Individual implements Comparable<Individual>{
	
	private static final int SIZE = 9;
	private static final int SUBGRID_SIZE = 3;
	private int[][] sudo_m;
	private int[][] hints;
        private int pos_inside_population;
        private int fitness;
	
	public Individual(int[][] sudo_m, int[][] hints) {
            this.sudo_m = Arrays.copyOf(sudo_m, sudo_m.length);
            this.hints = Arrays.copyOf(hints, hints.length);
	}
	
	public Individual(int[][] sudo_m) {
            this.hints = Arrays.copyOf(sudo_m, sudo_m.length);
            BoardRandomizer br = new BoardRandomizer();
            this.sudo_m = br.generateRandomSudokuGA(sudo_m);         
	}    
        @Override
	public int compareTo(Individual other) {
	    // Confronta gli individui in base al loro fitness
	    return Integer.compare(this.getFitness(), other.getFitness());
	}
        /******METODI USATI IN CROSSOVER********/
	// Metodo per copiare il contenuto della sottogriglia n da M in child
    public static void copySubgrid(int n, int[][] M, int[][] child) {
        // Calcolare le coordinate di inizio della sottogriglia
        int startRow = (n / (SIZE / SUBGRID_SIZE)) * SUBGRID_SIZE;
        int startCol = (n % (SIZE / SUBGRID_SIZE)) * SUBGRID_SIZE;       
        // Copiare la sottogriglia da M a child
        for (int i = startRow; i < SUBGRID_SIZE + startCol; i++) {
            for (int j = startCol; j < SUBGRID_SIZE + startCol; j++) {
                child[i][j] = M[i][j];
            }
        }
    }
    public int[][] getSudo_m() {
	return sudo_m;
    }
    public void setPosInPopulation(int pos_inside_population){
        this.pos_inside_population = pos_inside_population;
    }
    public int getPosInPopulation(){
        return pos_inside_population;
    }
    public void setSudo_m(int[][] sudo_m) {
        this.sudo_m = sudo_m;
    }
    public int[][] getHints() {
        return hints;
    }
    
    public int getFitness() {
        if (fitness == 0) {
            fitness = FitnessCalc.computeFitness(this.sudo_m);
        }
        return fitness;
    }
	public void printSudoku() {
        for (int i = 0; i < 9; i++) {
            if (i > 0 && i % 3 == 0) {
                // Stampa una linea orizzontale ogni 3 righe
                System.out.println("------+-------+------");
            }
            for (int j = 0; j < 9; j++) {
                if (j > 0 && j % 3 == 0) {
                    // Stampa una barra verticale ogni 3 colonne
                    System.out.print("| ");
                }
                System.out.print(sudo_m[i][j] + " ");
            }
            System.out.println();
        }
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
	
	//Verifica se esiste un numero nella sottogriglia
	private boolean isSafeSubgrid(int[][] sudoku, int row, int col, int number) {
    	// Verifica che il numero non sia presente nella sottogriglia 3x3
    	
        int startRow = row - row % SUBGRID_SIZE;
        int startCol = col - col % SUBGRID_SIZE;
        for (int i = 0; i < SUBGRID_SIZE; i++) {
            for (int j = 0; j < SUBGRID_SIZE; j++) {
                if ((sudoku[startRow + i][startCol + j] == number) || (sudoku[startRow + i][startCol + j] == number)) {
                    return false;
                }
            }
        }

        return true;
        }
        
        public void setRow(int i, int childRow[]){
            for(int col=0; col<SIZE; col++){
                sudo_m[i][col] = childRow[col];
            }
        }
     // Metodo per permutare casualmente i numeri delle celle non indizi in una sottogriglia
    public void shuffleSubgrid(int[][] board, int subgridNumber, int m, List<Integer> hints) {
        Random random = new Random();       
        int startRow = (subgridNumber / (SIZE / SUBGRID_SIZE)) * SUBGRID_SIZE;
        int startCol = (subgridNumber % (SIZE / SUBGRID_SIZE)) * SUBGRID_SIZE;       
        
        // Trova le celle non indizi nella sottogriglia
        List<Integer> nonClueCells = new ArrayList<>();
        for (int i = startRow; i < startRow + SUBGRID_SIZE; i++) {
            for (int j = startCol; j < startCol + SUBGRID_SIZE; j++) {
            	//System.out.println("\n\nCELLA : " + i + ", "+ j);               
                if (hints.contains(i*9+j));
                else{
                 //   System.out.println("\n\nCELLA NON IN HINTS: " + i + ", "+ j);
                    nonClueCells.add(i * SIZE + j);
                }
            }
        }
        // Verifica che ci siano abbastanza celle non indizi da permutare
        //System.out.println("\n\nVALORE m inizio: " + m);
        m = Math.min(m, nonClueCells.size());
        //System.out.println("\n\nVALORE m scelto: " + m);
        // Permuta casualmente m celle non indizi nella sottogriglia
        Collections.shuffle(nonClueCells);
        for (int i = 0; i < m; i++) {
            int provaindex = 53;
            int index = nonClueCells.get(i);
            int row = provaindex / SIZE;
            int col = provaindex % SIZE;            
            int temp = board[row][col];
            board[row][col] = random.nextInt(9) + 1;  // Cambia il valore in modo casuale
           // System.out.println("Scambio: " + temp + " con " + board[row][col]);
        }
        /*
        // Stampa la griglia risultante
        System.out.println("NUOVO INDIVIDUO GRIGLIA \n\n");
        for (int i = 0; i < 9; i++) {
            if (i > 0 && i % 3 == 0) {
                // Stampa una linea orizzontale ogni 3 righe
                System.out.println("------+-------+------");
            }
            for (int j = 0; j < 9; j++) {
                if (j > 0 && j % 3 == 0) {
                    // Stampa una barra verticale ogni 3 colonne
                    System.out.print("| ");
                }
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }*/
    }
}
