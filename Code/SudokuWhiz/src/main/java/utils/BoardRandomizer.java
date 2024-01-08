package utils;
import java.util.Random;

public class BoardRandomizer {
    private static final int SIZE = 9;
    private static final int SUBGRID_SIZE = 3;  
    /**
     * Mantiene traccia degli indizi come valori originali
     * all'inerno di una stringa nel formato : indizio=0, non indizio=1
     * che viene valutata per ogni sottogriglia e su cui si controllano iterativamente gli indizi
     * che in parallelo vengono rimossi dall'interno di
     * values = [i for i in range(1,10) if i not in block] //contiene i numeri da 1
     * a 9 e 
     * che poi verrà mischiato con shuffle.
     * @param sudo_m: Matrice del sudoku le cui celle vuote devono essere randomizzate.
     * @return la matrioe randomizzata ad eccezione degli indizi.
     */
    public int[][] generateRandomSudoku(int sudo_m[][]) {
        // Uso l'insieme dei possibili valori assumibili dalle celle
        // per randomizzare le griglie del sudoku.
        SudokuPuzzle s = new SudokuPuzzle(sudo_m);
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (i % SUBGRID_SIZE == 0 && j % SUBGRID_SIZE == 0) {
                    int[] values = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
                    int values_lenght = SIZE;
                    shuffleArray(values, values_lenght);
                    String available_values = s.getPossibleValuesSubgrid(i, j);
                    // System.out.println(available_values);

                    for (int k = 0; k < available_values.length(); k++) {
                        if (available_values.charAt(k) == '0') {
                            values_lenght = removeUsedValuesFromPool(k + 1, values, values_lenght);
                        }
                    }
                    shuffleArray(values, values_lenght);
                    randomize_subGrid(sudo_m, i, j, values);
                }
            }

        }
        return sudo_m;
    }
    
    private static boolean isValid(int[][] sudo_m, int row, int col, int num) {
        // Check if 'num' is not present in the current row and column
        for (int x = 0; x < SIZE; x++) {
            if (sudo_m[row][x] == num || sudo_m[x][col] == num) {
                return false;
            }
        }
        return true;
    }
    public int [][] generateRandomGrid(int sudo_m[][]){
        for(int i=0; i<SIZE; i++){
            for(int j=0; j<SIZE; j++){ 
                int[] values = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
                int values_lenght = SIZE;           
                //values_lenght = removeHints(i,j,sudo_m, values, values_lenght);
                shuffleArray(values, values_lenght);
                for (int num : values) {
                    if (sudo_m[i][j]==0 && isValid(sudo_m, i, j, num)) {
                            sudo_m[i][j] = num;
                    }
                }      
            }
        }
        return sudo_m;
    }
    public int removeHints(int x,int y, int sudo_m[][], int [] values, int values_lenght){
        for(int i=0; i<9; i++){
            if(sudo_m[i][y] !=0){ 
                System.out.println("Ignorato indizio nella pos:"+"i: "+i+"y: "+y+" valore: "+sudo_m[i][y]);
                removeUsedValuesFromPool(sudo_m[i][y], values, values_lenght);
            }
        }
        for(int j=0; j<9; j++){
            if(sudo_m[x][j] !=0){ 
                System.out.println("Ignorato indizio nella pos:"+"x: "+x+"j: "+j+"valore: "+sudo_m[x][j]);
                removeUsedValuesFromPool(sudo_m[x][j], values, values_lenght);
            }
        }
        
        return values_lenght;
    }
    private int removeUsedValuesFromPool(int hint, int values[], int values_lenght) {
        int temp;
        for (int h = 0; h < values_lenght - 1; h++) {
            if (hint == values[h]) {
                // System.out.println("valore di h="+values[h]);
                temp = values[h];
                values[h] = values[h + 1];
                values[h + 1] = temp;
            }
        }
        values_lenght--;
        return values_lenght;
    }

    private void shuffleArray(int[] array, int values_lenght) {
        Random random = new Random();
        for (int i = values_lenght - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            int temp = array[i];
            array[i] = array[index];
            array[index] = temp;
        }
    }


    public void randomize_subGrid(int sudo_m[][], int ix, int iy, int values[]) {
        // Sto iterando sulla sottogriglia che si estende da [ix][iy] e finisce quando
        // incontro
        // due posizioni che restituiscono resto 2.
        int oounter = 0;
        for (int i = ix; i < SUBGRID_SIZE + ix; i++) {
            for (int j = iy; j < SUBGRID_SIZE + iy; j++) {
                if (sudo_m[i][j] == 0) {
                    sudo_m[i][j] = values[oounter];
                    oounter++;
                }
            }
        }

    }

}
