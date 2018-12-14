package gui.tableElements.commons;

import javax.swing.table.AbstractTableModel;

public class MyTableModel extends AbstractTableModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private String[] columns=null;
	
	private String[][] rows=null;

	
	public MyTableModel(String[] tmpColumns, String [][] tmpRows){
		
		columns=tmpColumns;
		rows=tmpRows;
		
	}
	

	
	public int getColumnCount() {
        return columns.length;
    }

    public int getRowCount() {
        return rows.length;
    }

    public String getColumnName(int col) {
        return columns[col];
    }


    
    public Object getValueAt(int row, int col) {
        return rows[row][col];
    }
    
    
    

    /*
     * JTable uses this method to determine the default renderer/
     * editor for each cell.  If we didn't implement this method,
     * then the last column would contain text ("true"/"false"),
     * rather than a check box.
     */
    public Class<? extends Object> getColumnClass(int c) {
    	
    	Object object=getValueAt(0, c);
    	
    	if(object==null){
    		return Object.class;
    	}
        return getValueAt(0, c).getClass();
    }

    public boolean isCellEditable(int row, int col) {
        //Note that the data/cell address is constant,
        //no matter where the cell appears onscreen.
        return false;
    }

}
