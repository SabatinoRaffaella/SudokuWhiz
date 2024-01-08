
package oop21.sudokuwhiz;

import utils.ManageMatrix;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import utils.SolutionStatistics;

public class RunButtonPressed implements ActionListener {
    private static final int DIVIDE_TO_SECOND = 1000000000;
    JPanel panel;
    int sudo_m[][];
    ButtonGroup choiceAlg;
    JButton runButton;

    public void getInfo(JPanel panel, int sudo_m[][], ButtonGroup choiceAlg, JButton runButton) {
        this.panel = panel;
        this.sudo_m = sudo_m;
        this.choiceAlg = choiceAlg;
        this.runButton = runButton;
        // System.out.println(choiceAlg.getSelection().getActionCommand());
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        runButton.setEnabled(false);
        ManageMatrix m = new ManageMatrix();
        SolutionStatistics stat = new SolutionStatistics();
        m.printMatrix(this.sudo_m);
        SudokuSolver s = new SudokuSolver();
        long startTime = 0;
        long endTime = 0;
        String action = choiceAlg.getSelection().getActionCommand();
        switch (action) {
            case "BackTracking":
                startTime = System.nanoTime();
                s.solve_sudoku_backtrack(this.sudo_m, stat);
                endTime = System.nanoTime();
                break;
            case "A_Star":
                startTime = System.nanoTime();
                sudo_m = s.solveSudoku_AsteriskA(sudo_m, stat);
                endTime = System.nanoTime();
                break;

            case "SimulatedAnnealing":
                startTime = System.nanoTime();
                System.out.println("Da aggiungere");
                sudo_m = s.solveSudoku_SimulatedAnnealing(sudo_m, stat);
                endTime = System.nanoTime();
                break;
            default:
                startTime = 0;
                endTime = 0;
                break;
        }
        JLabel time = (JLabel) panel.getComponent(2);

        long duration = (endTime - startTime);
        double elapsedTimeInSecond = (double) duration / DIVIDE_TO_SECOND;
        time.setText(Double.toString(elapsedTimeInSecond));
        /// Aggiorno le label con le statistiche delle computazioni.
        JLabel sol_nodes = (JLabel) panel.getComponent(4);
        sol_nodes.setText(String.valueOf(stat.getSolNodes()));
        JLabel exp_nodes = (JLabel) panel.getComponent(6);
        exp_nodes.setText(String.valueOf(stat.getExploredNodes()));
        JLabel gen_nodes = (JLabel) panel.getComponent(8);
        gen_nodes.setText(String.valueOf(stat.getGeneratedNodes()));

        JTable table = (JTable) panel.getComponent(0);
        MatrxiModel mm = (MatrxiModel) table.getModel();
        mm.replaceTable(sudo_m);
        panel.revalidate();
        panel.repaint();

    }
}
