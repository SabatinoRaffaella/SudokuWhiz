
package oop21.sudokuwhiz;

import utils.ManageMatrix;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;


///////TO DO : PROVA  A CAMBIARE IL SISTEMA DI RECUPERO DELLA MATRICE 
////// PROVA A FARE RIFERIMENTO AL FILE, PRIMA DI CHIAMARE IL RISOULTORE 
////// LA RECUPERO DAL FILE DESCRIPTOR.
public class RunButtonPressed implements ActionListener{
    private static final int DIVIDE_TO_SECOND = 1000000000; 
    JPanel panel;
    int sudo_m[][];
    ButtonGroup choiceAlg;
    
    public void getInfo (JPanel panel, int sudo_m[][], ButtonGroup choiceAlg){
        this.panel = panel;
        this.sudo_m = sudo_m;
        this.choiceAlg = choiceAlg;
        System.out.println(choiceAlg.getSelection().getActionCommand());       
    } 
    @Override
    public void actionPerformed(ActionEvent ae) {
        ManageMatrix m = new ManageMatrix();
        m.printMatrix(this.sudo_m);
        SudokuSolver s = new SudokuSolver();
        long startTime = 0;
        long endTime = 0;
        String action = choiceAlg.getSelection().getActionCommand();
        switch(action){
            case "BackTracking":                
                startTime = System.nanoTime();
                s.solve_sudoku_backtrack(this.sudo_m);       
                endTime = System.nanoTime();
            break;
            case "A_Star":
                startTime = System.nanoTime();
                sudo_m = s.solveSudoku_AsteriskA(sudo_m);
                endTime = System.nanoTime();
            break;    
        }
        JLabel time = (JLabel)panel.getComponent(2);
        //startTime = System.nanoTime();
        //s.solve_sudoku_backtrack(sudo_m,objectfied_m,table);        
        //s.solve_sudoku_backtrack(sudo_m);       
        //endTime = System.nanoTime();
        long duration = (endTime - startTime);
        double elapsedTimeInSecond = (double) duration / DIVIDE_TO_SECOND;
        time.setText(Double.toString(elapsedTimeInSecond)); 
        JTable table = (JTable) panel.getComponent(0);
        MatrxiModel mm = (MatrxiModel)table.getModel();
        mm.replaceTable(sudo_m);     
        panel.revalidate();
        panel.repaint();
        
    }
}
