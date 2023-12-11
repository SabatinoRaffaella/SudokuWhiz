package graphics;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
public class SudokuRenderer extends DefaultTableCellRenderer {
    ////Dovrebbe creare qualcosa di simile al Sudoku non ho idea
    /// di come lo faccia, so solo che crea dei bordi compositi
    /// partendo da b0 e b1 e alterna la loro composizione.
  /// Crea dei bordi di diverso spessore e li combina insieme
    // come la girglia del sudoku.
  public static final int BW1 = 1;
  public static final int BW2 = 2;
  private final Border b0 = BorderFactory.createMatteBorder(0, 0, BW1, BW2, Color.white);
  private final Border b1 = BorderFactory.createMatteBorder(0, 0, BW1, BW2, Color.BLACK);
  private final Border b2 = BorderFactory.createCompoundBorder(
      BorderFactory.createMatteBorder(0, 0, BW2, 0, Color.BLACK),
      BorderFactory.createMatteBorder(0, 0, 0, BW1, Color.white));
  private final Border b3 = BorderFactory.createCompoundBorder(
      BorderFactory.createMatteBorder(0, 0, 0, BW2, Color.BLACK),
      BorderFactory.createMatteBorder(0, 0, BW1, 0, Color.white));
  private final int[][] mask;
  /**
   * La copia viene fatta per non perdere i numeri e per nascondere gli 0.
   * @param src Matrice del sudoku da copiare temporaneamente.
   */
    public SudokuRenderer(int[][] src) {
      super();
      int[][] dest = new int[src.length][src[0].length];
      for (int i = 0; i < src.length; i++) {
        System.arraycopy(src[i], 0, dest[i], 0, src[0].length);
      }
      this.mask = dest;
    }  
  @Override public Component getTableCellRendererComponent(
    JTable table, Object value, boolean isSelected, boolean hasFocus,
    int row, int column) {
      // Qui verifica se ci sono 0 da nascondere.
    boolean isEditable = mask[row][column] == 0;
    Component c = super.getTableCellRendererComponent(
    table, value, isEditable && isSelected, hasFocus, row, column);
    //// Se è editabile/cella vuota penso che la rende invisibile, mentre setta il font 
    /// dei caratteri visibili a (Bold recuperandolo stesso dai caratteri visibili).
    /// che in questo modo rimangono inalterati e vengono "renidizzati" normalmente. 
    c.setFont(isEditable ? c.getFont() : c.getFont().deriveFont(Font.BOLD));
    /// Qui aggiunge delle label che vengono allineate orizzontalmente per
    /// "nascondere" le celle vuote
    if (c instanceof JLabel) {
        JLabel l = (JLabel) c;
        l.setHorizontalAlignment(CENTER);
        if (isEditable && Objects.equals(value, 0)) {
          l.setText(" ");
        }
        /// Queste booleane sono utilizzate per capire 
        /// se e come li deve settare i bordi o meno in base a dove si trova
        /// bordi da settare per ogni multiplo di 3.
        boolean rf = (row + 1) % 3 == 0;
        boolean cf = (column + 1) % 3 == 0;
        if (rf && cf) {
          l.setBorder(b1);
        } else if (rf) {
          l.setBorder(b2);
        } else if (cf) {
          l.setBorder(b3);
        } else {
          l.setBorder(b0);
        }
      }
    return c;
    }
}