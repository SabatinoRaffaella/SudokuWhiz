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
        gbc.insets = new Insets(0,0,10,5);
        panel.add(jLabel3,gbc);
        gbc.insets = new Insets(12,90,20,5);
        panel.add(time_elapsed,gbc); 
        panel.revalidate();
        panel.repaint();
        frame.add(panel);  
    }
}
