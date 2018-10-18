package data.dataSorters;

import java.util.Comparator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import data.dataPPL.pplSQLSchema.PPLTable;

public class PPLTableSortingClass {
	
	
	//static <K,V extends Comparable<? super V>>
	public SortedSet<Map.Entry<String,PPLTable>> entriesSortedByBirthDeath(Map<String,PPLTable> map) {
	    SortedSet<Map.Entry<String,PPLTable>> sortedEntries = new TreeSet<Map.Entry<String,PPLTable>>(
	        new Comparator<Map.Entry<String,PPLTable>>() {
	            @Override public int compare(Map.Entry<String,PPLTable> e1, Map.Entry<String,PPLTable> e2) {
	            	
	            	if (e1.getValue().getBirthVersionID()<e2.getValue().getBirthVersionID()) {
						return -1;
					}
		        	else if(e1.getValue().getBirthVersionID()>e2.getValue().getBirthVersionID()){
		        		return 1;
		        	}
		        	else{
		        		if (e1.getValue().getDeathVersionID()<e2.getValue().getDeathVersionID()) {
							return -1;
						}
			        	else{
			        		return 1;
			        	}
			        	
		        	}
		        }
	        }
	    );
	    sortedEntries.addAll(map.entrySet());
	    return sortedEntries;
	}

}
