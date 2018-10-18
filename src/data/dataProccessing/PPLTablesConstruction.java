package data.dataProccessing;

import java.util.Map;
import java.util.TreeMap;

import data.dataPPL.pplSQLSchema.PPLSchema;
import data.dataPPL.pplSQLSchema.PPLTable;
import data.dataPPL.pplTransition.TableChange;

public class PPLTablesConstruction {
	
	private static TreeMap<String,PPLSchema> allPPLSchemas = new TreeMap<String,PPLSchema>();
	private TreeMap<String,PPLTable> allPPLTables = null;

	public PPLTablesConstruction(TreeMap<String,PPLSchema> tmpAllPPLSchemas){
		allPPLTables=new TreeMap<String,PPLTable>();
		allPPLSchemas=tmpAllPPLSchemas;

	}
	
	public void makeAllPPLTables(){
		
		int versionID=0;
		
		for (Map.Entry<String,PPLSchema> pplSch : allPPLSchemas.entrySet()) {
			
			PPLSchema oneSchema = pplSch.getValue();			
			
			for(int j=0; j<oneSchema.getTables().size(); j++){
				
				PPLTable oneTable=oneSchema.getTableAt(j);
				
				if(!allPPLTables.containsKey(oneTable.getName())){
					oneTable.setBirth(oneSchema.getName());
					oneTable.setBirthVersionID(versionID);
					oneTable.setDeath(allPPLSchemas.get(allPPLSchemas.lastKey()).getName());
					oneTable.setDeathVersionID(allPPLSchemas.size()-1);
					oneTable.setActive();
					allPPLTables.put(oneTable.getName(),oneTable);
					oneTable=new PPLTable();
					
				}
			}
			
			boolean found=false;
			
			for (Map.Entry<String,PPLTable> pplTbl : allPPLTables.entrySet()) {
				
				found=false;
				
				for(int z=0; z<oneSchema.getTables().size(); z++){
					
					PPLTable oneTable=oneSchema.getTableAt(z);
					if(pplTbl.getKey().equals(oneTable.getName())){
						found=true;
						break;
					}
				}
				
				if(!found && pplTbl.getValue().getActive()){
					pplTbl.getValue().setDeath(oneSchema.getName());
					pplTbl.getValue().setDeathVersionID(versionID);
					pplTbl.getValue().setActive();

				}
				
			}
			
			versionID++;
			
		}
		
	}
	
	public void matchTableChanges(TreeMap<String,TableChange> allTableChanges){
		
		for (Map.Entry<String, TableChange> t : allTableChanges.entrySet()) {
			
			TableChange tmpTableChange = t.getValue();
						
			allPPLTables.get(t.getKey()).setTableChanges(tmpTableChange);
			

				
		}
		
	}
	
	public TreeMap<String,PPLTable> getAllPPLTables(){
		
		return allPPLTables;
		
	}
	
	

}
