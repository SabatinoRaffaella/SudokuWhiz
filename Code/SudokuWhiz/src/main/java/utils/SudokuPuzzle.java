package utils;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 *
 * @author raffy
 */
public class SudokuPuzzle {
    private static final int SIZE = 9;
    private static final int SUBGRID_SIZE = 3;
    private int sudo_m[][];
    private int cost;
    private List<Integer> originalEntries;

    public SudokuPuzzle(int sudo_m[][], List<Integer> originalEntries) {
        this.sudo_m = sudo_m;
        this.originalEntries = originalEntries;
        this.cost = scoreBoard();
    }

    public SudokuPuzzle(int sudo_m[][]) {
        this.sudo_m = sudo_m;
    }

    public int[][] getData() {
        return sudo_m;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    /**
     * Restituisce la lista dei possibili valori esplorando la sottomatrice
     * e rimuovendo quelli già usati, quindi se una cella è piena
     * l'hashset dei valori conterrà tutti gli n!=valore usato nella cella.
     * 
     * @param row: riga in cui si trova il numero di cui recuperare i valori
     *             assumibili.
     * @param col: colonna in cui si trova il numero di cui recuperare i valori
     *             assumibili.
     * @return la Lista dei valori che la cella sudo[row][col] può assumere,
     *         invalidando i numeri già usati.
     */
    public String getPossibleValuesSubgrid(int row, int col) {
        int possibilities = (1 << (SIZE + 1)) - 2; // All bits set to 1
        for (int i = row; i < SUBGRID_SIZE + row; i++) {
            for (int j = col; j < SUBGRID_SIZE + col; j++) {
                possibilities &= ~(1 << sudo_m[i][j]); // check subgrid
            }
        }
        StringBuilder result = new StringBuilder();
        for (int i = 1; i <= SIZE; i++) {
            int bit = (possibilities & (1 << i)) != 0 ? 1 : 0;
            result.append(bit); // Append the bit to the result string
        }
        return result.toString();
    }

    /**
     * Restituisce il numero totale di duplicati ottenuti sommando il numero
     * di duplicati presenti nelle righe e nelle colonne.
     * 
     * @return Il numero di duplicati presenti nella matrice.
     */
    public int scoreBoard() {
        int score = countDuplicatesInRows(sudo_m) + countDuplicatesInColumns(sudo_m);
        return score;
    }

    /**
     * Restituisce il numero totale di duplicati presenti nelle righe.
     * 
     * @param grid: Matrice di cui controllare il numero di duplicati.
     * @return Il numero di duplicati presenti nelle righe.
     */
    public static int countDuplicatesInRows(int[][] grid) {
        int duplicates = 0;

        for (int row = 0; row < SIZE; row++) {
            duplicates += countDuplicatesInArray(grid[row]);
        }

        return duplicates;
    }

    /**
     * Restituisce il numero totale di duplicati presenti nelle colonne.
     * 
     * @param grid: Matrice di cui controllare il numero di duplicati.
     * @return Il numero di duplicati presenti nelle colonne.
     */
    public static int countDuplicatesInColumns(int[][] grid) {
        int duplicates = 0;

        for (int col = 0; col < SIZE; col++) {
            int[] columnArray = new int[SIZE];

            for (int row = 0; row < SIZE; row++) {
                columnArray[row] = grid[row][col];
            }

            duplicates += countDuplicatesInArray(columnArray);
        }

        return duplicates;
    }

    /**
     * Restituisce il numero totale di duplicati contati per ogni valore da 1 a 9
     * all'interno dell'array
     * passato come input.
     * 
     * @param array: Array in cui controllare il numero di duplicati.
     * @return Il numero di duplicati presenti.
     */
    private static int countDuplicatesInArray(int[] array) {
        int duplicates = 0;
        int[] counts = new int[SIZE];

        for (int value : array) {
            counts[value - 1]++;
        }

        for (int count : counts) {
            if (count > 1) {
                duplicates += count - 1;
            }
        }

        return duplicates;
    }

    /**
     * 
     * Questo meccanismo di perturbazione consente di generare casualmente
     * uno stato vicino allo stato corrente, scegliendo casualmente due celle
     * non indizi, appartenenti alla stessa sottogriglia.
     *
     * @param originalEntries: La lista degli indizi (delle celle iniziali che
     *                         forniscono
     *                         indizi per risolvere il sudoku e che NON devono
     *                         essere modificate)
     * @return Una soluzione candidata.
     */
    public int[][] makeCandidateData(List<Integer> originalEntries) {

        int[][] new_data = Arrays.copyOf(sudo_m, SIZE);
        Random random = new Random();
        int subgridIndex = random.nextInt(SUBGRID_SIZE) + 1;

        int rowIndex1, colIndex1, rowIndex2, colIndex2;

        do {
            rowIndex1 = (subgridIndex - 1) * SUBGRID_SIZE + random.nextInt(SUBGRID_SIZE);
            colIndex1 = (subgridIndex - 1) * SUBGRID_SIZE + random.nextInt(SUBGRID_SIZE);

        } while (originalEntries.contains(rowIndex1 * 9 + colIndex1));

        do {
            rowIndex2 = (subgridIndex - 1) * SUBGRID_SIZE + random.nextInt(SUBGRID_SIZE);
            colIndex2 = (subgridIndex - 1) * SUBGRID_SIZE + random.nextInt(SUBGRID_SIZE);

        } while (originalEntries.contains(rowIndex2 * 9 + colIndex2)
                && (rowIndex2 == rowIndex1 && colIndex2 == colIndex1));

        int temp = new_data[rowIndex1][colIndex1];
        new_data[rowIndex1][colIndex1] = new_data[rowIndex2][colIndex2];
        new_data[rowIndex2][colIndex2] = temp;

        return new_data;
    }

    /**
     * Restituisce una soluzione candidata, scambiando due celle ottenute dal metodo
     * generateSquaresFirstRandomSecondDupe(originalEntries), a cui passo le celle
     * originali
     * come parametro, per assicurarmi che le due celle scelte non siano indizi.
     * 
     * @param originalEntries: La lista degli indizi (delle celle iniziali che
     *                         forniscono
     *                         indizi per risolvere il sudoku e che NON devono
     *                         essere modificate)
     * @return Una soluzione candidata.
     */
    public int[][] makeCandidateDataDup(List<Integer> originalEntries) {
        int square2 = 0;
        try {
            int[][] new_data = Arrays.copyOf(sudo_m, SIZE);
            // Selezione prima cella
            int[] squares;
            int square1;
            squares = generateSquaresFirstRandomSecondDupe(originalEntries);
            square1 = squares[0];
            // Selezione seconda celle
            square2 = squares[1];
            // Scambio tra le due celle:
            int temp = new_data[square1 / SIZE][square1 % SIZE];
            new_data[square1 / SIZE][square1 % SIZE] = new_data[square2 / SIZE][square2 % SIZE];
            new_data[square2 / SIZE][square2 % SIZE] = temp;
            return new_data;

        } catch (Exception e) {
            System.out.println(square2);
        }
        return null;
    }

    /**
     * Seleziona due celle: di cui la prima è casuale evitando di essere un indizio
     * ed è un ,
     * duplicato scelto casualmente mentre la seconda dipende dalla possibilità che
     * una delle celle
     * nella stessa sottomatrice possà essere un duplioato.
     * 
     * @return Restituisce le due celle generate dalle computazioni.
     * 
     */
    private int[] generateSquaresFirstRandomSecondDupe(List<Integer> originalEntries) {
        int first_square[];
        int square1 = 0;
        int square2 = 0;
        int coordx, coordy;
        boolean notOriginalEntry;
        try {
            do {
                first_square = getRandomeSquareXY();
                square1 = first_square[0] * 9 + first_square[1];
                coordx = first_square[0];
                coordy = first_square[1];
                notOriginalEntry = !(originalEntries != null && originalEntries.contains(square1));
            } while (notOriginalEntry != true || !isDuplicateInRow(sudo_m, first_square[0], sudo_m[coordx][coordy])
                    || !isDuplicateInColumn(sudo_m, first_square[1], sudo_m[coordx][coordy]));

            square2 = generateSecondSquare(originalEntries, first_square[0], first_square[1]);

        } catch (Exception e) {
            System.out.println("errore" + square1 + " coord square2:" + square2);
        }
        // int square2= generateSecondSquare(first_square[0], first_square[1]);
        return new int[] { square1, square2 };
    }

    /**
     * Controlla se il valore inserito come parametro value è contenuto all'interno
     * della lista
     * contenente gli indizi.
     * 
     * @param value: Valore da controllare
     * @return true se il valore e contenuto all'interno della lista- false
     *         altrimenti
     */
    private boolean checkOriginalEntries(List<Integer> originalEntries, int value) {
        try {
            for (int index : originalEntries) {
                if (index == value)
                    return true;
            }
        } catch (Exception e) {
            System.out.println("Cosa devo farekfsdds");
            System.out.println(e.getMessage());

        }
        return false;
    }

    /**
     * 
     * @param board: Matrice in cui controllare se value è un duplicato.
     * @param row:   Riga in cui si trova il possibile valore duplicato.
     * @param value: Numero di cui verificare la presenza nella riga.
     * @return true se value è un duplicato nella riga, false altrimenti.
     */
    private boolean isDuplicateInRow(int[][] board, int row, int value) {
        for (int col = 0; col < SIZE; col++) {
            if (board[row][col] == value) {
                return true;
            }
        }
        return false;
    }

    /**
     * 
     * @param board: Matrice in cui controllare se value è un duplicato.
     * @param col:   Colonna in cui si trova il possibile valore duplicato.
     * @param value: Numero di cui verificare la presenza nella colonna.
     * @return true se value è un duplicato nella colonna, false altrimenti.
     */
    private boolean isDuplicateInColumn(int[][] board, int col, int value) {
        for (int row = 0; row < SIZE; row++) {
            if (board[row][col] == value) {
                return true;
            }
        }
        return false;
    }

    /**
     * 
     * @param row:   Riga in cui contare il numero di duplicati.
     * @param value: Numero di cui verificare la presenza di duplicati.
     * @return Il numero di duplicati nella riga del parametro value.
     */
    private int countDuplicateInRow(int row, int value) {
        int count = 0;
        for (int col = 0; col < SIZE; col++) {
            if (sudo_m[row][col] == value) {
                count++;
            }
        }
        return count;
    }

    /**
     * 
     * @param col:   Colonna in cui contare il numero di duplicati.
     * @param value: Numero di cui verificare la presenza di duplicati.
     * @return Il numero di duplicati nella colonna del parametro value.
     */
    private int countDuplicateInColumn(int col, int value) {
        int count = 0;
        for (int row = 0; row < SIZE; row++) {
            if (sudo_m[row][col] == value) {
                count++;
            }
        }
        return count;
    }

    /**
     * Partendo dalla prima cella generata recupero le celle adiacenti nello spazio
     * 3X3 a quella considerata
     * e ne valuto il punteggio sui duplicati escludendo hint, restituendo le
     * coordinate della cella che ha
     * più duplicati nella riga e nella colonna.
     * 
     * @param square1: Cella della matrice.
     * @return Le coordinate della cella .
     */
    private int[] getSubgridScoreValueAndPickBest(List<Integer> originalEntries, int square1x, int square1y) {
        int max_duplicates = -1;
        int coord_best[] = new int[2];
        int start_row = square1x;
        if (square1x % 3 == 1) {
            start_row = square1x - 1;
        } else if (square1x % 3 == 2) {
            start_row = square1x - 2;
        }
        int start_col = square1y;
        if (square1y % 3 == 1) {
            start_col = square1y - 1;
        } else if (square1y % 3 == 2) {
            start_col = square1y - 2;
        }
        try {
            for (int i = start_row; i < SUBGRID_SIZE + start_row; i++) {
                for (int j = start_col; j < SUBGRID_SIZE + start_col; j++) {
                    // Devo ignorare le entry originali-indizi e la primo cella data in input
                    // e inutile scambiare la primo cella con se stessa.
                    if (checkOriginalEntries(originalEntries, i * 9 + j) || (i == square1x && j == square1y)) {
                    } else {
                        int duplicates = countDuplicateInRow(i, sudo_m[i][j]) + countDuplicateInColumn(j, sudo_m[i][j]);
                        if (max_duplicates < duplicates) {
                            max_duplicates = duplicates;
                            coord_best[0] = i;
                            coord_best[1] = j;
                        }
                    }
                }
            }
        } catch (NullPointerException e) {
            System.out.println(e.getCause());
            System.out.println("prova123");
        }
        return coord_best;
    }

    /**
     * Restituisce il risultato della computazione
     * getSubgridScoreValueAndPickBest(originalEntries,square1x, square1y)
     * che prende come parametri gli stessi parametri passati come input, per
     * scegliere la seconda cella da
     * scambiare ma converte il risultato ottenuto(prima di restituirlo) con la
     * seguente formula (i*9+j) o riga*9+colonna.
     * che rappresenta la posizione nella matrice appiatita della seconda cella e
     * che viene usata per scambiare le due celle.
     * 
     * @param originalEntries: lista degli indizi.
     * @param square1x:        coordinata x della prima cella.
     * @param square1y:        coordinata y della prima cella.
     * @return La posizione nella matrice "appiatita" della seconda cella.
     */
    private int generateSecondSquare(List<Integer> originalEntries, int square1x, int square1y) {
        int second_square_coord[] = getSubgridScoreValueAndPickBest(originalEntries, square1x, square1y);
        return second_square_coord[0] * 9 + second_square_coord[1];
    }

    /**
     * Partendo dalla matrice di partenza scelgo uns cella casuale e
     * ne restiuisco la riga e la colonna come un array contenente i due valori.
     * 
     * @param new_data: Il nuovo individuo in cui vogliamo fare lo swap di due
     *                  celle.
     * @return Una cella casuale come x,y conservati all'interno di un array.
     * 
     */
    private int[] getRandomeSquareXY() {
        Random rand = new Random();
        int x = rand.nextInt(SIZE);
        int y = rand.nextInt(SIZE);
        return new int[] { x, y };
    }

    public double acceptanceProbability(SudokuPuzzle sudoPuzzle2, double temp) {
        if (sudoPuzzle2.getCost() < this.cost) {
            return 1.0;
        } else {
            return Math.exp((this.cost - sudoPuzzle2.getCost()) / temp);
        }
    }

}
