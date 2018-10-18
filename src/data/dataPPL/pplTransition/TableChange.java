package data.dataPPL.pplTransition;

import java.util.ArrayList;
import java.util.TreeMap;

public class TableChange {
	
	
	private String affectedTable;
	private TreeMap<Integer,ArrayList<AtomicChange>> atomicChanges = new TreeMap<Integer,ArrayList<AtomicChange>>();
	private ArrayList<AtomicChange> atomicChangesForOneTransition = new ArrayList<AtomicChange>();
	
	public TableChange(String tmpAffectedTable, TreeMap<Integer,ArrayList<AtomicChange>> tmpAtomicChanges){
		
		affectedTable = tmpAffectedTable;
		atomicChanges = tmpAtomicChanges;
		
	}
	
	public TableChange(){
		
	}
	
	public TableChange(String tmpAffectedTable,ArrayList<AtomicChange> tmpAtomicChanges){
		
		affectedTable = tmpAffectedTable;
		atomicChangesForOneTransition = tmpAtomicChanges;
		
	}
	
	public TreeMap<Integer,ArrayList<AtomicChange>> getTableAtomicChanges(){
		return atomicChanges;
	}
	
	public ArrayList<AtomicChange> getTableAtChForOneTransition(Integer transition){
		
		return atomicChanges.get(transition);
		
	}
	
	public ArrayList<AtomicChange> getTableAtChForOneTransition(){
		
		return atomicChangesForOneTransition;
		
	}
	
	
	public int getNumberOfAdditionsForOneTr(Integer transition){
		int additions=0;
		ArrayList<AtomicChange> tmpAtChs=atomicChanges.get(transition);
		for(int i=0;i<tmpAtChs.size();i++){
			AtomicChange atCh=tmpAtChs.get(i);
			if(atCh.getType().contains("Addition")) {
				additions++;
			}
		}
		return additions;
	}
	
	public int getNumberOfDeletionsForOneTr(Integer transition){
		int deletions=0;
		ArrayList<AtomicChange> tmpAtChs=atomicChanges.get(transition);
		for(int i=0;i<tmpAtChs.size();i++){
			AtomicChange atCh=tmpAtChs.get(i);
			if(atCh.getType().contains("Deletion")) {
				deletions++;
			}
		}
		return deletions;
	}
	
	public int getNumberOfUpdatesForOneTr(Integer transition){
		int updates=0;
		ArrayList<AtomicChange> tmpAtChs=atomicChanges.get(transition);
		for(int i=0;i<tmpAtChs.size();i++){
			AtomicChange atCh=tmpAtChs.get(i);
			if(atCh.getType().contains("Change")) {
				updates++;
			}
		}
		return updates;
	}
	
	public int getNumberOfAdditionsForOneTr(){
		int additions=0;
		for(int i=0;i<atomicChangesForOneTransition.size();i++){
			AtomicChange atCh=atomicChangesForOneTransition.get(i);
			if(atCh.getType().contains("Addition")) {
				additions++;
			}
		}
		return additions;
	}
	
	public int getNumberOfDeletionsForOneTr(){
		int deletions=0;
		for(int i=0;i<atomicChangesForOneTransition.size();i++){
			AtomicChange atCh=atomicChangesForOneTransition.get(i);
			if(atCh.getType().contains("Deletion")) {
				deletions++;
			}
		}
		return deletions;
	}
	
	public int getNumberOfUpdatesForOneTr(){
		int updates=0;
		for(int i=0;i<atomicChangesForOneTransition.size();i++){
			AtomicChange atCh=atomicChangesForOneTransition.get(i);
			if(atCh.getType().contains("Change")) {
				updates++;
			}
		}
		return updates;
	}
	
	
	public String toString(){
		
		String message = "Table Change \n";
		
		for(int i=0; i<atomicChanges.size(); i++){
			
			message=message+atomicChanges.get(i).toString();
			
			message=message+"\n";
			
		}
		
		return message;
		
		
	}
	
	public String getAffectedTableName(){
		
		return affectedTable;
		
	}
	
	

}
