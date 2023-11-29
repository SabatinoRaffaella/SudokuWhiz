/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package oop21.sudokuwhiz;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
public class SudokuWhiz {
    private static final int DIVIDE_TO_SECOND = 1000000000; 
    public static void main(String[] args) throws FileNotFoundException {  
        ///Indicare il percorso del file da aprire 
       // File fd= new File("sudoku_master1.txt");
        try{
            ///Leggo il percorso del file da cui prendere il sudoku da tastiera
            InputStreamReader in = new InputStreamReader(System.in);
            BufferedReader br = new BufferedReader(in);
            System.out.println("Inserire il percorso del file da aprire");
            File fd = new File(br.readLine());
            int sudo_m [][];
            ManageMatrix m = new ManageMatrix();
            sudo_m= m.recoverMatrix(fd);
            m.printMatrix(sudo_m);
            SudokuSolver s = new SudokuSolver();
            long startTime = System.nanoTime();
            int solved_sudo_m[][] = s.solve_sudoku_backtrack(sudo_m);
            long endTime = System.nanoTime();
            long duration = (endTime - startTime);
            double elapsedTimeInSecond = (double) duration / DIVIDE_TO_SECOND;
            m.printMatrix(solved_sudo_m);
            System.out.println("Tempo in secondi impiegato dall'algoritmo di backtracking:"+"\n"+elapsedTimeInSecond);
        }
        catch(IOException i){
            System.out.println("Impossibile trovare il file selezionato");
        }
    } 
}