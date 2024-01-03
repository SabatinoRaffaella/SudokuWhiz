package utils;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author raffy
 */
public class SolutionStatistics {
    private int sol_nodes = 0;
    private long explored_nodes = 0; 
    private int generated_nodes = 0;
    public SolutionStatistics(){}
    /**
     * Memorizza il numero di nodi soluzione trovati durante
     * l'esecuzione degli algortimi di risoluzione e il numero totale
     * di nodi esplorati.
     * @param explored_nodes: numero dei nodi esplorati.
     * @param sol_nodes: numero dei nodi soluzione esplorati.
     */
    public void recordSolutionStatistics(long explored_nodes, int sol_nodes, int generated_nodes){
        this.explored_nodes = explored_nodes;
        this.sol_nodes = sol_nodes;
        this.generated_nodes = generated_nodes;
    }
    public int getGeneratedNodes(){
        return this.generated_nodes;
    }
    public int getSolNodes(){
        return this.sol_nodes;
    }
    public long getExploredNodes(){
        return this.explored_nodes;
    }
    /** Prima Euristica usata per l'algoritmo di ricerca A*
     * Restituisce il numero di celle bianche contenute all'interno del parametro grid.
     * @param grid: Matrice di cui si deve calcolare il numero di celle vuote.
     * @return Numero di celle bianche.
     */    
    public int heuristic1(int[][] grid) {
        int count = 0;
        for (int[] row : grid) {
            for (int cell : row) {
                if (cell == 0) {
                    count++;
                }
            }
        }
        return count;
    }

     /** Seconda Euristica usata per l'algoritmo di ricerca A*
     * Restituisce il numero di possibili valori che una cella può assumere all'interno del parametro grid.
     * @param grid: Matrice di cui si deve calcolare il numero di celle vuote.
     * @param row: Riga in cui si trova la cella.
     * @param col: Colonna in cui si trova la cella.
     * @return Numero di possibili valori che la cella può assumere.
     */  
    public int heuristic2(int[][] grid, int row, int col) {
        Set<Integer> options = new HashSet<>();
        for (int i = 1; i <= 9; i++) {
            options.add(i);
        }
        for (int i = 0; i < 9; i++) {
            options.remove(grid[row][i]);
            options.remove(grid[i][col]);
        }
        int startRow = 3 * (row / 3);
        int startCol = 3 * (col / 3);
        for (int i = startRow; i < startRow + 3; i++) {
            for (int j = startCol; j < startCol + 3; j++) {
                options.remove(grid[i][j]);
            }
        }
        return options.size();
    }    
     /*
     * Funzione utilizzata per creare un identificativo per ogni stato visitato
     * durante la ricerca A*
     **/
    public String getGridHash(int[][] grid) {
        StringBuilder hashBuilder = new StringBuilder();
        for (int[] row : grid) {
            for (int cell : row) {
                hashBuilder.append(cell);
            }
        }
        return hashBuilder.toString();
    }
}
