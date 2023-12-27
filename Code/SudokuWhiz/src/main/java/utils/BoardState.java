package utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
public class BoardState {

    private static final int SIZE=9;
    private static final int SUBGRID_SIZE=3;
    private int sudo_m[][];
    private int pathCost = 0;
    private int possibleVals[][];
    private int[] lastFilledCell;

    public BoardState(int sudo_m[][]){
        this.sudo_m = new int[SIZE][SIZE];
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                this.sudo_m[row][col] = sudo_m[row][col];
            }
        }
        this.pathCost = 0;
        this.lastFilledCell = new int[2];
    }

    public int[][] getSudokuBoard(){
        return this.sudo_m;
    }

    public int getPathCost(){
        return this.pathCost;
    }

    public void setPathCost(int cost){
        this.pathCost = cost;
    }

    public int[] getLastFilledCell(){
        if (lastFilledCell != null && lastFilledCell.length > 0) {
            return lastFilledCell;
        } else {
            return null;
        }
    }

    public void setLastFilledCell(int[] lastCell){
        this.lastFilledCell = Arrays.copyOf(lastCell, 2);
    }

    /*Si determina il costo di cammino per raggiungere un nodo n dell'albero di ricerca, a 
     * partire dal nodo radice, ovvero dallo stato iniziale, sapendo che:
     * - inserimento/cancellazione di un numero : costa 2;
     * - lettura del valore di una cella: costa 1;
     * - spostamento da una cella ad una successiva: costa 1.
     * Visto che la generazione di un nuovo stato currentState deriva dall'inserimento/cancellazione di un valore in una cella
     * nello stato previousState, per determinare il costo di cammino si tiene conto della lettura del valore (0 nel caso della cella vuota),
     * dell'inserimento del valore e della distanza tra l'ultima cella riempita in previousState e la cella riempita che ha generato currentState.
     * Per determinare il numero di spostamenti si utilizza la distanza euclidea.
     * Nel caso dello stato iniziale, il quale non ha traccia dell'ultima cella riempita, il numero di spostamenti per riempire una cella vuota
     * verrà calcolato dalla cella (0,0). 
     * 
    */

    public static int computePathCost(BoardState previousState, BoardState currentState){
        // Considera il costo di inserimento e cancellazione nella cella attuale
        int insertionDeletionCost = 2;
        // Considera il costo di lettura della cella attuale
        int readCost = 1;
        //Calcolo lo spostamento dall'ultima cella riempita alla cella attuale mediante la distanza euclidea
        int moveCost;
        if(previousState.getLastFilledCell() == null)
            moveCost = calculateMovementCost(0, 0, currentState.getLastFilledCell()[0], currentState.getLastFilledCell()[1]);
        else
            moveCost = calculateMovementCost(previousState.getLastFilledCell()[0], previousState.getLastFilledCell()[1], currentState.getLastFilledCell()[0], currentState.getLastFilledCell()[1]);
        
        return insertionDeletionCost + readCost + moveCost;
    }

    public static int calculateMovementCost(int row1, int col1, int row2, int col2) {
        // Determina la distanza euclidea tra le due celle, arrotondando al valore intero più vicino
        double euclideanDistance = Math.sqrt(Math.pow(row2 - row1, 2) + Math.pow(col2 - col1, 2));
        return (int) Math.round(euclideanDistance);
    }

    /***
     * Restituisce la lista dei possibili valori esplorando la 
     * matrice e rimuovendo quelli già usati, quindi se una cella è piena
     * l'hashset dei valori conterrà tutti gli n!=valore usato nella cella.
     * @param row: riga in cui si trova il numero di cui recuperare i valori assumibili.
     * @param col: colonna in cui si trova il numero di cui recuperare i valori assumibili.
     * @return la Lista dei valori che la cella sudo[row][col] può assumere, invalidando i numeri già usati.
     */
    public String getPossibleValues(int row, int col) {
        int possibilities = (1 << (SIZE + 1)) - 2; // All bits set to 1

        for (int i = 0; i < SIZE; i++) {
            possibilities &= ~(1 << sudo_m[row][i]); // check row
            possibilities &= ~(1 << sudo_m[i][col]); // check column
        }

        int subgridStartRow = row - row % SUBGRID_SIZE;
        int subgridStartCol = col - col % SUBGRID_SIZE;
        for (int i = 0; i < SUBGRID_SIZE; i++) {
            for (int j = 0; j < SUBGRID_SIZE; j++) {
                possibilities &= ~(1 << sudo_m[subgridStartRow + i][subgridStartCol + j]); // check subgrid
            }
        }
        
        StringBuilder result = new StringBuilder();
        for (int i = 1; i <= SIZE; i++) {
            int bit = (possibilities & (1 << i)) != 0 ? 1 : 0;
            result.insert(0, bit); // Prepend the bit to the result string
        }

        return result.toString();
    }

    
    /**
     * Restituisce lo stato di avvicinamento alla soluzione contando
     * il numero di celle vuote usando la matrice sudo_m di cui bisogna verificare la distanza dallo
     * stato obbiettivo.
     * @return Lo stato di avvicinamento alla soluzione.
     */
    public int getDistance_ToGoal(){
        int numBlankSpaces = 0;
        for(int row=0;row<SIZE;row++){
            for(int col=0;col<SIZE;col++){
                if(sudo_m[row][col]==0) numBlankSpaces++;
            }
        }
        return numBlankSpaces;
    }   
    /***
     * Controllo per vedere se tutte le celle sono state riempite e non ci sono spazi bianchi.
     * @return true se il problema è risolto
     */
    public boolean isSolved() {
        for(int row=0;row<SIZE;row++){
            for(int col=0;col<SIZE;col++){
                if(sudo_m[row][col]==0) return false;
            }
        }
        return true;
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
            }
        }
        return newBoard;
    } 
    /**
     * Scansiona la matrice alla ricerca delle mosse migliori da fare, quindi
     * delle celle con il minimo numero di possibilità e restiuisce la migliore cella trovata.
     * @return L'array contente le migliori celle per numero di possibilità.
     */
    private int[] findBestCell() {
        int[] bestCell = null;
        int minPossibilities = SIZE + 1;
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (sudo_m[row][col] == 0) {
                	int countPoss = 0;
                    String possibilities = getPossibleValues(row, col);
                    for(int i = 0; i < possibilities.length(); i++)
                    	if(possibilities.charAt(i) == '1')
                    		countPoss++;
                    if (countPoss < minPossibilities) {
                        minPossibilities = countPoss;
                        bestCell = new int[]{row, col};
                    }
                }
            }
        }
        return bestCell;
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
    
    /**
     * Si cercano la migliore cella su cui operare con i possibili valori che questa può
     * assumere e la prima cella della griglia vuota e l'insieme dei suoi valori ammissibili.
     * In base alla distanza delle celle bestCell e firstEmptyCell dall'ultima cella riempita,
     * si decide di creare delle copie-figli che utilizzano i diversi possibili
     * valori di una delle due celle per ottenere nuove configurazioni e le aggiunge alla lista dei
     * possibili stati successivi
     * in cui può trovarsi il sudoku, di cui si vuole selezionare quello che più si
     * avvicina allo stato obbiettivo.
     * 
     * @return La lista dei possibili stati successivi in cui si può trovare la
     *         soluzione.
     */
    public List<BoardState> generateNextStates() {
        List<BoardState> nextStates = new ArrayList<>();
        int[] bestCell = findBestCell();
        int[] firstEmptyCell = this.findFirstEmptyCell();
        int rowB = bestCell[0];
        int colB = bestCell[1];
        int rowE = firstEmptyCell[0];
        int colE = firstEmptyCell[1];

        if (calculateMovementCost(this.getLastFilledCell()[0], this.getLastFilledCell()[1], rowB, colB) <
               calculateMovementCost(this.getLastFilledCell()[0], this.getLastFilledCell()[1], rowE, colE)) {
                
            String possibleValues = getPossibleValues(rowB, colB);

            for (int i = 0; i < possibleValues.length(); i++) {
                char bitChar = possibleValues.charAt(i);
                if (bitChar == '1') {
                    int value = 9 - i;
                    int[][] newBoard = copyBoard();
                    newBoard[rowB][colB] = value;
                    BoardState newBoardState = new BoardState(newBoard);
                    int computePath = computePathCost(this, newBoardState);
                    newBoardState.setPathCost(computePath);
                    newBoardState.setLastFilledCell(bestCell);
                    nextStates.add(newBoardState);
                }
            }
        } else {
            String possibleValues = getPossibleValues(rowE, colE);

            for (int i = 0; i < possibleValues.length(); i++) {
                char bitChar = possibleValues.charAt(i);
                if (bitChar == '1') {
                    int value = 9 - i;
                    int[][] newBoard = copyBoard();
                    newBoard[rowE][colE] = value;
                    BoardState newBoardState = new BoardState(newBoard);
                    int computePath = computePathCost(this, newBoardState);
                    newBoardState.setPathCost(computePath);
                    newBoardState.setLastFilledCell(firstEmptyCell);
                    nextStates.add(newBoardState);
                }
            }
        }

        return nextStates;
    }

}