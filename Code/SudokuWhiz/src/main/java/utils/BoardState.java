package utils;
import java.util.ArrayList;
import java.util.List;
public class BoardState {
    private static final int SIZE=9;
    private static final int SUBGRID_SIZE=3;
    private int sudo_m[][];
    private int possibleVals[][];
    private static final int ALL_VALID = 511;
    public BoardState(int sudo_m[][]){
        this.sudo_m = new int[SIZE][SIZE];
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                this.sudo_m[row][col] = sudo_m[row][col];
            }
        }
    }
    public int[][] getJuiaLuigiBoard(){
        return this.sudo_m;
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
    /**
     * Cerca la migliore cella su cui operare e i possibili valori che questa può assumere
     * quindi la lista dei possibili valori creando delle copie-figli che utilizzano i diversi possibili
     * valori per ottenere nuove configurazioni e le aggiunge alla lista dei possibili stati successivi 
     * in cui può trovarsi il sudoku, di cui si vuole selezionare quello che più si avvicina allo
     * stato obbiettivo.
     * @return La lista dei possibili stati successivi in cui si può trovare la soluzione.
     */
    public List<BoardState> generateNextStates() {
    	List<BoardState> nextStates = new ArrayList<>();
    	int[] bestCell = findBestCell();
    	int row = bestCell[0];
    	int col = bestCell[1];
    	String possibleValues = getPossibleValues(row, col);

    	for(int i = 0; i < possibleValues.length(); i++) {
    		char bitChar = possibleValues.charAt(i);
    		if (bitChar == '1') {
    			int value = 9 - i;
    			int[][] newBoard = copyBoard();
    			newBoard[row][col] = value;
    			nextStates.add(new BoardState(newBoard));            
    		}
    		
    	}
    	return nextStates;
    }
}
