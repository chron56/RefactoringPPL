package data.dataSorters;

import java.util.Map;
import java.util.TreeMap;
import data.dataPPL.pplSQLSchema.PPLTable;

public class PldRowSorter {
	
	private String[][] finalRows;
	TreeMap<String,PPLTable> allPPLTables;

	public PldRowSorter(String[][] finalRows, TreeMap<String,PPLTable> allPPLTables){
		this.finalRows=finalRows;
		this.allPPLTables=allPPLTables;
	}
	
	public String[][] sortRows(){
		
		String[][] sortedRows=new String[finalRows.length][finalRows[0].length];
		
		PPLTableSortingClass tablesSorter=new PPLTableSortingClass();
	    int counter=0;
	    for(Map.Entry<String, PPLTable> ppl:tablesSorter.entriesSortedByBirthDeath(allPPLTables)){
			for(int i=0; i<finalRows.length; i++ ){
				if (finalRows[i][0].equals(ppl.getKey())) {
					for(int j=0;j<finalRows[0].length; j++){
						sortedRows[counter][j]=finalRows[i][j];
					}                               
				}
			}
			
			counter++;
		}
		
		return sortedRows;
		
		
	}
	
}
