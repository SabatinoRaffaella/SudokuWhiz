package oop21.sudokuwhiz;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

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
           Logger.getLogger(SudokuWhiz.class.getName()).log(Level.SEVERE, null, e);
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
    
}
