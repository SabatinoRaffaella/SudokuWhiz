package graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

public class LayoutHelper {
    JFrame frame; 
    JPanel panel;
    JTable table;
    public LayoutHelper(JFrame frame, JPanel panel, JTable table){
        this.frame = frame;
        this.panel = panel;
        this.table = table;
    }
    /**
     * Definisce un gridbagLayout da applicare sul secondo pannello e posiziona in questo ordine: 
     * la matrice del sudoku
     * la targhetta del misuratore di tempo
     * il timer
     * Una volta definite le posizioni modifica i componenti del pannello all'interno del frame.
     */
    public void materializeMatrix(){
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.weightx = 0.1;
        gbc.weighty = 0.1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(1,0,1,1);
        //panel.add(new JScrollPane(table), gbc);
        panel.add((table), gbc);
        JLabel jLabel3= new JLabel("Time Elapsed:");
        JLabel time_elapsed = new JLabel("0");
        gbc.gridx = 0;
        gbc.gridy = 0;      
        gbc.insets = new Insets(0,0,0,0);
        panel.add(jLabel3,gbc);
        gbc.insets = new Insets(0,90,0,0);
        panel.add(time_elapsed,gbc); 
        
        ///Cerco di aggiungere la label per i nodi esplorati+risultato
        /// e numero di nodi soluzione esplorati.
        JLabel test= new JLabel("N° Explored Solution Nodes:");
        JLabel sol_nodes = new JLabel("0");  
        gbc.insets = new Insets(40,0,0,0);
        panel.add(test,gbc);
        gbc.insets = new Insets(40,165,0,0);
        panel.add(sol_nodes,gbc);
        JLabel test2= new JLabel("N° Explored Nodes:");
        JLabel exp_nodes = new JLabel("0");  
        gbc.insets = new Insets(70,0,0,0);
        panel.add(test2,gbc);
        gbc.insets = new Insets(70,165,0,0);
        panel.add(exp_nodes,gbc);
        JLabel test3= new JLabel("N° Generated Nodes:");
        JLabel gen_nodes = new JLabel("0");  
        gbc.insets = new Insets(40,230,0,0);
        panel.add(test3,gbc);
        gbc.insets = new Insets(40,360,0,0);
        panel.add(gen_nodes,gbc);
        
        panel.revalidate();
        panel.repaint();
        frame.add(panel);  
    }
}
