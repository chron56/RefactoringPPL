package gui.tableElements.commons;


import javax.swing.JTable;
import javax.swing.table.TableModel;

import data.dataController.DataController;

public class ExtendedJvTable extends JTable {

	private static final long serialVersionUID = 1L;

    public ExtendedJvTable(TableModel dataModel)
    {
        super(dataModel);
    }

    public void setZoom(int rowHeight, int columnWidth)
    {
    	
		for(int i=0; i<super.getRowCount(); i++){
			super.setRowHeight(i, rowHeight);
				
		}

		for(int i=0; i<super.getColumnCount(); i++){
			if(i==0){
				super.getColumnModel().getColumn(0).setPreferredWidth(columnWidth);
			
			}
			else{
				super.getColumnModel().getColumn(i).setPreferredWidth(columnWidth);
				
			}
		}
        firePropertyChange("zoom", 1500, 5000);
    }
    


    public void uniformlyDistributed(int columnWidth){
    	for(int i=0; i<super.getColumnCount(); i++){
			if(i==0){
				super.getColumnModel().getColumn(0).setPreferredWidth(86);
				
			}
			else{
				super.getColumnModel().getColumn(i).setPreferredWidth(1);
				
			}
		}
        firePropertyChange("uniformly", 1500, 5000);
    }
   
    
    public void notUniformlyDistributed(DataController dataController){
    	for(int i=0; i<super.getColumnCount(); i++){
    		if(i==0){
    			super.getColumnModel().getColumn(0).setPreferredWidth(60);
    			
    		}
    		else{
    			int tot=750/dataController.getAllPPLTransitions().size();
    			int sizeOfColumn=dataController.getPhaseCollectors().get(0).getPhases().get(i-1).getSize()*tot;
    			super.getColumnModel().getColumn(i).setPreferredWidth(sizeOfColumn);
    			
    		}
		}
        firePropertyChange("uniformly", 1500, 5000);
    }
   
    
}
