package utils;

import java.io.File;
import java.io.FileNotFoundException;
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
}
