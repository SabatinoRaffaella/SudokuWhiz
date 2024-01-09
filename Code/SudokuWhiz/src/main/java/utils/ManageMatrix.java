package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import oop21.sudokuwhiz.SudokuWhizWhiz;

public class ManageMatrix { 
    private static final int MATRIX_DIM = 9;    
    /**
     * Recupera la matrice 9x9 contenuta all'interno del file passato come parametro.
     *  @param fd: file da cui recuperare/leggere la matrice.
     *  @return sudo_m: matrice recuperata dal file di input.
    */
    public int[][] recoverMatrix(File fd){
        int sudo_m [][] = new int [MATRIX_DIM][MATRIX_DIM];
        try{
              Scanner sc = new Scanner(fd);
                while((sc.hasNextInt())){
                    for (int i=0;i<MATRIX_DIM;i++){
                        for(int j=0;j<MATRIX_DIM;j++){
                            sudo_m[i][j] = sc.nextInt();
                        }
                    }
                }            
        }
        catch(FileNotFoundException e){
           Logger.getLogger(SudokuWhizWhiz.class.getName()).log(Level.SEVERE, null, e);
        }
        return sudo_m;    
    }
    /**
     * Stampa la matrice passata come parametro.
     *  @param sudo_m: matrice di cui fare la stampa a video.
    */
    public void printMatrix(int sudo_m[][]){
        System.out.println("Verifico il trasferimento alla matrice degli input dei dati");
        for(int i=0; i<MATRIX_DIM; i++){
            for(int j=0; j<MATRIX_DIM; j++){
                System.out.print(sudo_m[i][j]+" ");
            }
            System.out.println();
       }
    }
    /**
     * Converto l'array di interi in array di oggetti per visualizzarlo
     * nella tabella jtable
     * @param intArray: Array di interi da convertire
     * @return Array convertito in array di oggetti
     */
    public Object[][] convertIntArrayToObjectArray(int[][] intArray) {
    int rows = intArray.length;
    int cols = intArray[0].length;
    
    Object[][] objectArray = new Object[rows][cols];    
    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
            objectArray[i][j] = intArray[i][j];
        }
    }   
    return objectArray;
    }
    /**
     * Copia i valori della matrice source passata come parametro in dest.
     * @param source: Matrice da copiare.
     * @param dest: Matrice di destinazione per la copia.
     */
    public void copy_Matrix(int source[][], int dest[][]){
        for(int row=0; row<MATRIX_DIM; row++){
            for(int col=0; col<MATRIX_DIM; col++){
                dest[row][col] = source[row][col];    
            }
        }
    }
    /**
     * Resetta la matrice passata come parametro. 
     * @param sudo_m: Matrice da resettare.
     */
    public void resetMatrix(int sudo_m[][]){
        for(int row=0; row<MATRIX_DIM; row++){
            for(int col=0; col<MATRIX_DIM; col++){
                sudo_m [row][col] = 0;
            }
        }
    }
    /**
     * Restituisce la lista degli indizi presenti nella matrice del sudoku.
     * @param sudo_m: Matrice di cui recuperare gli indizi.
     * @return Lista degli indizi.
     */
    public List<Integer> getOriginalEntriesList(int sudo_m[][]) {
        List<Integer> originalEntries = new ArrayList<>();
        for (int i = 0; i < sudo_m.length; i++) {
            for (int j = 0; j < sudo_m[i].length; j++) {
                if (sudo_m[i][j] > 0) {
                    originalEntries.add(i * MATRIX_DIM + j);
                }
            }
        }
        return originalEntries;
    }
    
    /**
     * Restituisce ls riga e la colonna di partenza della sottomatrice a cui appartiene
     * la cella che si trova alle coordinate passate come parametri.
     * @param x: riga in cui si trova la sottomatrice da trovare.
     * @param y: colonna in cui si trova la sottomatrice da trovare.
     * @return La riga e la colonna in cui inizia la sottomatrice da le coordinate di una cella.
     */
    public int [] identify_Subgrid_startRowCol(int x, int y){
         int start_row = x;
        if(x%3==1){
            start_row = x-1;
        }
        else if(x%3==2){
            start_row = x-2;
        }
        int start_col = y;
        if(y%3==1){
            start_col = y-1;
        }
        else if(y%3==2){
            start_col = y-2;
        }
        return new int[]{start_row, start_col};
    }
}
