package utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

public class BoardState {
	
    private static final int INSERT_DELETE_COST = 2;    
    private static final int GRID_SIZE = 9;
    private int[][] grid;
    private double cost;
    private int heuristicValue;
    private int[] lastFilledCell;
    
    public BoardState(int[][] grid, double cost, int heuristicValue) {
        this.grid = new int[GRID_SIZE][GRID_SIZE];
            copyValues(grid, this.grid);
            this.cost = cost;
            this.heuristicValue = heuristicValue;
            this.lastFilledCell = findLastFilledCell(grid);
    }

    public BoardState(int[][] grid, double cost, int heuristicValue, int[] filledCell) {
            this.grid = new int[GRID_SIZE][GRID_SIZE];
            copyValues(grid, this.grid);
            this.cost = cost;
            this.heuristicValue = heuristicValue;
            this.lastFilledCell = Arrays.copyOf(filledCell, filledCell.length);
    }

    public int[][] getGrid() {
        return grid;
    }

    public void setPathCost(int cost){
        this.pathCost = cost;

    public double getTotalCost() {
        return cost + heuristicValue;
    }

    public boolean isGoal() {
        for (int[] row : grid) {
            for (int cell : row) {
                if (cell == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    
    /** Prima Euristica usata per l'algoritmo di ricerca A*
     * Restituisce il numero di celle bianche contenute all'interno del parametro grid.
     * @param grid: Matrice di cui si deve calcolare il numero di celle vuote.
     * @return Numero di celle bianche.
     */    
    public static int heuristic1(int[][] grid) {
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
    public static int heuristic2(int[][] grid, int row, int col) {
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
    

    private int[] findLastFilledCell(int[][] grid) { // viene utilizzata solo per assegnare allo stato iniziale
                                                         // l'ultima cella riempita
        for (int row = 8; row >= 0; row--) {
            for (int col = 8; col >= 0; col--) {
                /*
                * Si controlla se una data cella non è bianca e se in quella riga ed in quella
                * colonna c'è almeno una
                * cella bianca.
                * Tali controlli sono necessari per evitare un blocco nel caloclo della
                * bestCell.
                */
                if (grid[row][col] != 0 && (hasEmptyCellsInRow(grid, row) || hasEmptyCellsInColumn(grid, col))) {
                    return new int[] { row, col };
                }
            }
        }
        return null;
    }

    public boolean hasEmptyCellsInRow(int[][] grid, int row) {
        for (int col = 0; col < GRID_SIZE; col++) {
            if (grid[row][col] == 0) {
                return true; // Trovata una cella bianca
            }
        }
        return false; // Nessuna cella bianca trovata
    }


    public boolean hasEmptyCellsInColumn(int[][] grid, int col) {
        for (int row = 0; row < GRID_SIZE; row++) {
            if (grid[row][col] == 0) {
                return true; // Trovata una cella bianca
            }
        }
        return false; // Nessuna cella bianca trovata
    }

    /**
     * Duplica la tabella di partenza per valutare le diverse alternative proposte dalle
     * diramazioni per raggiungere lo stato obbiettivo.
     * @return Un duplicato della tabella del sudoku da usare come nuova diramazione.
     * 
     */

    private int[][] copyBoard() {
        int[][] newBoard = new int[SIZE][SIZE];
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
               newBoard[row][col] = sudo_m[row][col];


           
    /**
     * Genera delle soluzioni candidate considerando le celle vuote nelle righe e nelle colonne,
     * cercando le celle vuote e utilizzando la seconda euristica definita per il problema
     * cerca la cella con il minimo numero di valori assumibili e la seleziona come possibile cella candidata, 
     * una volta selezionata controlla quali valori non violano il vincolo 
     * isSafe(il valore della cella non deve essere presente nella riga-colonna-sottogriglia) e crea un nuovo
     * individuo calcolandone il costo e aggiungendolo alla lista delle soluzioni candidate. 
     * @return Restituisce la lista di soluzioni candidate.
     */

    public List<BoardState> generateSuccessors() {
        
        List<BoardState> successors = new ArrayList<>();
        int minOptions = Integer.MAX_VALUE;
        int[] bestCell = null;


        if (lastFilledCell != null) {
            int row = lastFilledCell[0];
            int col = lastFilledCell[1];
            // Verifica se la riga corrente ha ancora celle vuote
            if (hasEmptyCellsInRow(grid, row)) {
                for (int i = 0; i < GRID_SIZE; i++) {
                    if (grid[row][i] == 0) {

                        int options = heuristic2(grid, row, i);

                        if (options < minOptions) {
                            minOptions = options;
                            bestCell = new int[] { row, i };
                        }
                    }
                }
            } else {
                // Cerca una nuova riga con celle vuote
                for (int i = 0; i < GRID_SIZE; i++) {
                    if (hasEmptyCellsInRow(grid, i)) {
                        for (int j = 0; j < GRID_SIZE; j++) {
                            if (grid[i][j] == 0) {

                                int options = heuristic2(grid, i, j);

                                if (options < minOptions) {
                                    minOptions = options;
                                    bestCell = new int[] { i, j };
                                }
                            }
                        }
                        break; // Esci dal ciclo esterno
                    }
                }
            }
                // Verifica se la colonna corrente ha ancora celle vuote
            if (hasEmptyCellsInColumn(grid, col)) {
                for (int i = 0; i < GRID_SIZE; i++) {
                    if (grid[i][col] == 0) {

                        int options = heuristic2(grid, i, col);

                        if (options < minOptions) {
                            minOptions = options;
                            bestCell = new int[] { i, col };
                        }
                    }
                }
            } else {
                // Cerca una nuova colonna con celle vuote
                for (int j = 0; j < GRID_SIZE; j++) {
                    if (hasEmptyCellsInColumn(grid, j)) {
                        for (int i = 0; i < GRID_SIZE; i++) {
                            if (grid[i][j] == 0) {

                                int options = heuristic2(grid, i, j);

                                if (options < minOptions) {
                                    minOptions = options;
                                    bestCell = new int[] { i, j };
                                }
                            }
                        }
                        break; // Esci dal ciclo esterno
                    }
                }
            }
        }

        if (bestCell != null) {  
            int row = bestCell[0];
            int col = bestCell[1];

            for (int value = 1; value <= GRID_SIZE; value++) {
                if (isSafe(row, col, value)) {
                    int[][] successorGrid = new int[GRID_SIZE][GRID_SIZE];
                    copyValues(grid, successorGrid);
                    successorGrid[row][col] = value;

                    
                    //int moveCost = (grid[row][col] == 0) ? MOVE_COST : INSERT_DELETE_COST;
                    double moveCost = INSERT_DELETE_COST + euclideanDistance(bestCell, lastFilledCell);
                    successors.add(new BoardState(successorGrid, cost + moveCost, heuristic1(successorGrid),
                               new int[] { row, col }));
                }
            }
        }

        return successors;
    }

  /* Determina la prima cella della griglia vuota. 
     * @return firstCell: la posizione (riga, colonna) della prima
     * cella vuota.
    */
    public int[] findFirstEmptyCell() {
        int[] firstCell = new int[2];

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (this.getSudokuBoard()[i][j] == 0) {
                    firstCell[0] = i;
                    firstCell[1] = j;
                    break;
                }
            }
        }
        return firstCell;
    }


        /* Verifico se vengono rispettati i vincoli nella griglia */
    private boolean isSafe(int row, int col, int value) {
       for (int i = 0; i < GRID_SIZE; i++) {
            if (grid[row][i] == value || grid[i][col] == value) {
                return false;
            }
        }

        int startRow = 3 * (row / 3);
        int startCol = 3 * (col / 3);
        for (int i = startRow; i < startRow + 3; i++) {
            for (int j = startCol; j < startCol + 3; j++) {
                if (grid[i][j] == value) {
                   return false;
                }
            }

    public static void copyValues(int[][] source, int[][] destination) {
        for (int i = 0; i < GRID_SIZE; i++) {
            System.arraycopy(source[i], 0, destination[i], 0, 9);

        }
    }

    private double euclideanDistance(int[] bestCell, int[] lastFilledCell) {
		int x1 = bestCell[0];
		int y1 = bestCell[1];
		
		int x2 = lastFilledCell[0];
		int y2 = lastFilledCell[1];
		
		return Math.sqrt(Math.pow((double) x2-x1, 2) + Math.pow((double)y2-y1, 2));
	}
}