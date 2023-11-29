package oop21.sudokuwhiz;
public class SudokuSolver {
    ///BACKTRACKING DI BASE
    /**
     * Risolve il sudoku passato come parametro utilizzando il Backtracking. 
     * @param sudo_m: matrice del sudoku da risolvere.
     * @return sudo_m (soluzione del sudoku) o null nel caso in cui il sudoku non sia risolvibile.
     *
     */
    public int [][] solve_sudoku_backtrack(int sudo_m[][]){
        if(solveSudoku_basic(sudo_m)==true) return sudo_m;
        else return null;
    
    }
    private boolean solveSudoku_basic(int sudo_m[][]){
        int row = 0;
        int col = 0;
        boolean checkBlankSpaces = false;
        /* controllo se il sudoku è risolto e in caso negativo,
        prendo la posizione della prossima cella "vuota"*/ 
        for (row = 0; row < sudo_m.length; row++) {
            for (col = 0; col < sudo_m[row].length; col++) {
                if (sudo_m[row][col] == 0) {
                    checkBlankSpaces = true;
                    break;
                }
            }
            if (checkBlankSpaces == true) {
            break;
            }
        }
      // se non ci sono più celle "vuote" significa che il sudoku è stato risolto.
        if (checkBlankSpaces == false) {
            return true;
        }
        // cerco di riempire le celle "vuote" con il numero corretto
        for (int num = 1; num <= 9; num++) {
            /* isSafe controlla che num non sia già presente 
            nella riga, colonna, o area-regione 3x3 
            (sotto le funzioni che si occupano di fare questi controlli) */ 
            if (isSafe(sudo_m, row, col, num)) {
                sudo_m[row][col] = num;
            if (solveSudoku_basic(sudo_m)) {
                return true;
            }
            /* se num è stato piazzato in una posizione scorretta, 
            marco nuovamente la cella come "vuota", poi faccio il backtrack su 
            un num differente su cui provare gli altri numeri che non sono stati provati*/ 
            sudo_m[row][col] = 0;
            }
        }
    return false;
    }
    
    private boolean isSafe(int sudo_m[][], int row, int col, int num) {
    return (
        !usedInRow(sudo_m, row, num) && 
        !usedInCol(sudo_m, col, num) && 
        !usedInBox(sudo_m, row - (row % 3), col - (col % 3), num)
    );
    }
    
    /**
     * 
     * @param sudo_m: matrice in cui verificare la presenza del numero inserito
     * @param row: riga in cui controllare
     * @param num: numero di cui verificare la presenza
     * @return true se il numero è presente, false altrimenti
     */
    private boolean usedInRow(int sudo_m[][], int row, int num) {
        for (int col = 0; col < sudo_m.length; col++) {
            if (sudo_m[row][col] == num) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Verifica la presenza del parametro num nella colonna col
     * @param sudo_m: matrice in cui verificare la presenza del numero inserito
     * @param col: colonna in cui controllare
     * @param num: numero di cui verificare la presenza
     * @return true se il numero è presente, false altrimenti
     */
    private boolean usedInCol(int sudo_m[][], int col, int num) {
        for (int row = 0; row < sudo_m.length; row++) {
            if (sudo_m[row][col] == num) {
                return true;
            }
        }
        return false;
    }
    /**
     * Verifica la presenza del parametro num nella regione delimitata da boxStartRow
     * e boxStartCol se lo trova restituisce true, false altrimenti
     * @param sudo_m: matrice in cui verificare la presenza del numero inserito
     * @param boxStartRow: riga in cui inizia la e-nesima regione in cui controllare
     * @param boxStartCol: colonna in cui inizia la e-nesima regione in cui controllare
     * @param num: numero di cui verificare la presenza
     * @return true se il numero è presente, false altrimenti
     */
    private boolean usedInBox(int sudo_m[][], int boxStartRow, int boxStartCol, int num) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (sudo_m[row + boxStartRow][col + boxStartCol] == num) {
                    return true;
                }
            }
        }
        return false;
    }
}

