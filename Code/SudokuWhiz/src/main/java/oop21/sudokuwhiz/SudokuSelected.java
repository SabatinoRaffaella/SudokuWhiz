package oop21.sudokuwhiz;
import utils.ManageMatrix;
import graphics.SudokuRenderer;
import graphics.LayoutHelper;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;

public class SudokuSelected implements ActionListener{   
    JFrame frame; 
    JPanel panel;
    File fd;
    JTable table;
    int sudo_m [][];
    MatrxiModel model;
    public void getInfo (JFrame frame, JPanel panel, File fd){
        this.frame = frame;
        this.fd = fd;
        this.panel = panel;
    }
    /**
     * Cambia il modello della tabella in base a quello
     * selezionato dall'utente quando seleziona un file per la prima volta
     * impostando il layout.
     */
    public void setFirstLayout(){      
       table = new JTable(model);
       LayoutHelper helpy = new LayoutHelper(frame,panel,table);
       table.setDefaultRenderer(Object.class, new SudokuRenderer(sudo_m));
       //table.setDefaultRenderer(Object.class, helpy);
       helpy.materializeMatrix();
    }
    public int[][] recoverFileData(){
        ManageMatrix m = new ManageMatrix();
        sudo_m= m.recoverMatrix(fd);
        model = new MatrxiModel(sudo_m);
        return sudo_m;
    }
    @Override
    public void actionPerformed(ActionEvent ae) {
        try{
            table.setModel(model);
            System.out.println("Sono nella selezione del file!!!!");           
        //table.setModel(model);  
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            ManageMatrix m= new ManageMatrix();
            m.printMatrix(sudo_m);
        }
    }
    
}
