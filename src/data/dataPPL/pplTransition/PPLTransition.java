package data.dataPPL.pplTransition;

import java.util.ArrayList;

public class PPLTransition {
	
	
	private String oldSchema;
	private String newSchema;
	private int pplTransitionID;
	private ArrayList<TableChange> tableChanges = new ArrayList<TableChange>();
	
	public PPLTransition(String tmpOldSchema, String tmpNewSchema,int pplTransitionID){
		
		oldSchema = tmpOldSchema;
		newSchema = tmpNewSchema;
		this.pplTransitionID=pplTransitionID;
		
	}
	
	public void setTableChanges(ArrayList<TableChange> tmpTableChanges){
		
		tableChanges = tmpTableChanges;
		
	}
	
	public ArrayList<TableChange> getTableChanges(){
		
		return tableChanges;
		
	}
	
	public int getPPLTransitionID() {
		return pplTransitionID;
	}
	
	public String getNewVersionName(){
		
		return newSchema;
		
	}
	
	public String getOldVersionName(){
		
		return oldSchema;
		
	}
	

	public int getNumberOfAdditionsForOneTr(){
		
		int additions=0;
		
		for(int i=0; i<tableChanges.size(); i++){
			additions=additions+tableChanges.get(i).getNumberOfAdditionsForOneTr();
		}
		return additions;
	}
	
	public int getNumberOfDeletionsForOneTr(){
		
		int deletions=0;
		
		for(int i=0; i<tableChanges.size(); i++){
			deletions=deletions+tableChanges.get(i).getNumberOfDeletionsForOneTr();
		}
		
		return deletions;
	}
	
	public int getNumberOfUpdatesForOneTr(){
		int updates=0;
		
		for(int i=0; i<tableChanges.size(); i++){
			updates=updates+tableChanges.get(i).getNumberOfUpdatesForOneTr();
		}
		
		return updates;
	}
	
	public int getNumberOfChangesForOneTr(){
		int totalChanges=0;
		
		for(int i=0; i<tableChanges.size(); i++){
			totalChanges=totalChanges+tableChanges.get(i).getTableAtChForOneTransition().size();
		}
		
		return totalChanges;
	}
	
public int getNumberOfClusterAdditionsForOneTr(String[][] rowsZoom){
		
		int additions=0;
		
		for(int j=0; j<rowsZoom.length; j++){
			for(int i=0; i<tableChanges.size(); i++){
				if (tableChanges.get(i).getAffectedTableName().equals(rowsZoom[j][0])) {
					additions=additions+tableChanges.get(i).getNumberOfAdditionsForOneTr();
				}
			}
		}
		
		return additions;
	}
	
	public int getNumberOfClusterDeletionsForOneTr(String[][] rowsZoom){
		
		int deletions=0;
		
		for(int j=0; j<rowsZoom.length; j++){
			for(int i=0; i<tableChanges.size(); i++){
				if (tableChanges.get(i).getAffectedTableName().equals(rowsZoom[j][0])) {
					deletions=deletions+tableChanges.get(i).getNumberOfDeletionsForOneTr();
				}
			}
		}
		
		return deletions;
	}
	
	public int getNumberOfClusterUpdatesForOneTr(String[][] rowsZoom){
		int updates=0;
		
		
		for(int j=0; j<rowsZoom.length; j++){
			for(int i=0; i<tableChanges.size(); i++){
				if (tableChanges.get(i).getAffectedTableName().equals(rowsZoom[j][0])) {
					updates=updates+tableChanges.get(i).getNumberOfUpdatesForOneTr();
				}
			}
		}
		
		
		return updates;
	}
	
	public int getNumberOfClusterChangesForOneTr(String[][] rowsZoom){
		int totalChanges=0;
		for(int j=0; j<rowsZoom.length; j++){
			for(int i=0; i<tableChanges.size(); i++){
				if (tableChanges.get(i).getAffectedTableName().equals(rowsZoom[j][0])) {
					totalChanges=totalChanges+tableChanges.get(i).getTableAtChForOneTransition().size();
				}
			}
		}
		
		return totalChanges;
	}
	
	
	
	
}
