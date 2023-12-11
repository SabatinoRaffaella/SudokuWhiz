package oop21.sudokuwhiz;

import javax.swing.table.AbstractTableModel;
 public class MatrxiModel extends AbstractTableModel {
        private int[][] data;
        
        public void replaceTable(int[][]newData){
            data = newData;
        }
        public int[][] getTable(){
            return this.data;
        }    
        public MatrxiModel(int[][] data) {
            this.data = data;
        }
        @Override
        public int getRowCount() {
            return data.length;
        }

        @Override
        public int getColumnCount() {
            return data[0].length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return data[rowIndex][columnIndex];
        }

        @Override
        public void setValueAt(Object value, int rowIndex, int columnIndex) {
            // Assuming the matrix is read-only in this example
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            // Set to true if you want cells to be editable
            return false;
        }
}