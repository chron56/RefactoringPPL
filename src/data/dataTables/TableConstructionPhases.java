package data.dataTables;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import phaseAnalyzer.commons.Phase;
import data.dataPPL.pplSQLSchema.PPLSchema;
import data.dataPPL.pplSQLSchema.PPLTable;
import data.dataPPL.pplTransition.AtomicChange;
import data.dataPPL.pplTransition.PPLTransition;
import data.dataPPL.pplTransition.TableChange;

public class TableConstructionPhases extends PldConstruction {

	
	private static TreeMap<String,PPLSchema> allPPLSchemas=new TreeMap<String,PPLSchema>();
	private ArrayList<PPLTable>	tables=new ArrayList<PPLTable>();
	private ArrayList<Phase> phases = new ArrayList<Phase>();


	private int columnsNumber=0;
	private Integer[][] schemaColumnId=null;
	private int maxDeletions=1;
	private int maxInsersions=1;
	private int maxUpdates=1;
	private int maxTotalChangesForOneTr=1;
	private Integer[] segmentSize=new Integer[4];
	
	public TableConstructionPhases(TreeMap<String, PPLSchema> pplSchemas,ArrayList<Phase> phases){
		
		allPPLSchemas=pplSchemas;
		this.phases=phases;		
		
	}
	

	
	public ArrayList<String[]> constructParticularRows(ArrayList<String[]> allRows ){
	    ArrayList<String>	allTables=new ArrayList<String>();

		
		int found=0;
		int i=0;
		
		for (Map.Entry<String,PPLSchema> pplSc : allPPLSchemas.entrySet()) {
			
			PPLSchema oneSchema=pplSc.getValue();
			
			
			for(int j=0; j<oneSchema.getTables().size(); j++){
				
				PPLTable oneTable=oneSchema.getTableAt(j);
				
				String tmpTableName=oneTable.getName();
				for(int k=0; k<allTables.size(); k++){
					
					
					if(!tmpTableName.equals(allTables.get(k))){
						found=0;
						
					}
					else{
						found=1;
						break;
						
					}
					
				}
				
				if(found==0){
					
					allTables.add(tmpTableName);
					tables.add(oneTable);
					String[] tmpOneRow=constructOneRow(oneTable,i,oneSchema.getName());
					allRows.add(tmpOneRow);
					oneTable=new PPLTable();
					tmpOneRow=new String[columnsNumber];
				}
				else{
					found=0;
				}
				
				
			}
			
			i++;
		}
		

		
		return allRows;
		
	}
	
	private String[] constructOneRow(PPLTable oneTable,int schemaVersion,String schemaName){
		
		String[] oneRow=new String[columnsNumber];
		int deletedAllTable=0;
		int pointerCell=0;
		int updn=0;
		int deln=0;
		int insn=0;
		int totalChangesForOnePhase=0;
		boolean reborn = true;

		oneRow[pointerCell]=oneTable.getName();
		if(schemaVersion==0){
			pointerCell++;
			
			
		}
		else{
			for(int p=0; p<phases.size(); p++){
				
				TreeMap<Integer,PPLTransition> phasePPLTransitions=phases.get(p).getPhasePPLTransitions();
				for(Map.Entry<Integer, PPLTransition> tr:phasePPLTransitions.entrySet()){
					
					if(tr.getValue().getNewVersionName().equals(schemaName)){	
						pointerCell=p+1;
						break;
					}
				}
				
			}
			
		}
		
		int initialization=0;
		if(pointerCell>0){
			initialization=pointerCell-1;
		}
		
		for(int p=initialization; p<phases.size(); p++){
			TreeMap<Integer,PPLTransition> phasePPLTransitions=phases.get(p).getPhasePPLTransitions();

			if (totalChangesForOnePhase>maxTotalChangesForOneTr) {
				maxTotalChangesForOneTr=totalChangesForOnePhase;
			}
			totalChangesForOnePhase=0;
			
			for(Map.Entry<Integer, PPLTransition> tmpTL:phasePPLTransitions.entrySet()){

				String sc=tmpTL.getValue().getNewVersionName();
				
				ArrayList<TableChange> tmpTR=tmpTL.getValue().getTableChanges();
				
				
				
				if(tmpTR!=null){
					
					for(int j=0; j<tmpTR.size(); j++){
						
						TableChange tableChange=tmpTR.get(j);
						
						if(tableChange.getAffectedTableName().equals(oneTable.getName())){
							if(deletedAllTable==1){
								reborn=true;
							}
							deletedAllTable=0;
							
							ArrayList<AtomicChange> atChs = tableChange.getTableAtChForOneTransition();
							
							for(int k=0; k<atChs.size(); k++){
								
								
								if (atChs.get(k).getType().contains("Addition")){
									deletedAllTable=0;

									insn++;
									
									if(insn>maxInsersions){
										maxInsersions=insn;
									}
									
								}
								else if(atChs.get(k).getType().contains("Deletion")){
									
									deln++;
									
									 if(deln>maxDeletions){
											maxDeletions=deln;
											
									 }
									 
									 boolean existsLater=getNumOfAttributesOfNextSchema(sc, oneTable.getName());
									 
									 if(!existsLater){
										 
										 deletedAllTable=1;
									 }
								}
								else{
									
									updn++;
									
									if(updn>maxUpdates){
										maxUpdates=updn;
									}
									
								}
								
							}
						}
						 
						 
					}
					
					
				}
				
				
			}
			
			if(pointerCell>=columnsNumber){
				
				break;
			}
			totalChangesForOnePhase=insn+updn+deln;
			if(totalChangesForOnePhase>=0 && reborn){

				oneRow[pointerCell]=Integer.toString(totalChangesForOnePhase);
				
			}
			
			pointerCell++;
			if(deletedAllTable==1){
				if(pointerCell>=columnsNumber){
					break;
				}
				if(!reborn){
					oneRow[pointerCell]="";
					pointerCell++;
				}
				reborn=false;
				
				
			}
			
			insn=0;
			updn=0;
			deln=0;
			
		}
		
		for(int i=0; i<oneRow.length; i++){
			if(oneRow[i]==null){
				oneRow[i]="";
			}
		}

		String lala="";
		for (int i = 0; i < oneRow.length; i++) {
			lala=lala+oneRow[i]+",";
		}
		return oneRow;

	}
	
	
	private boolean getNumOfAttributesOfNextSchema(String schema,String table){
		PPLSchema sc=allPPLSchemas.get(schema);
		
		if(sc.getTables().containsKey(table)){
			return true;
		}
		else {
			return false;
		}
		
	}
	
	public int getColumnSize(){

		return phases.size();
	}
	
	public ArrayList<String> setColumnLabel(ArrayList<String> columnsList) {
		for(int i=0;i<phases.size(); i++){
			String label="Phase "+i;
			columnsList.add(label);
		}
		
		return columnsList;
	}

	public void setColumnsNumber(int size){
		columnsNumber = size;
	}
	
	public void setColumnId(Integer[][] schemaColumnId) {
		this.schemaColumnId=schemaColumnId.clone();
	}
	
	public void setmaxInsersions(int insersions) {
		maxInsersions=insersions;
	}
	
	public void setmaxDeletions(int deletions) {
		maxDeletions=deletions;
	}
	
	public void setmaxUpdates(int updates) {
		maxUpdates=updates;
	}
	
	public void setmaxTotalChangesForOneTr(int totalChangesForOneTransition) {
		maxTotalChangesForOneTr=totalChangesForOneTransition;
	}
	
	public Integer[] getSegmentSize() {
		return segmentSize;
	}
	
	public void setSegmentSize() {
		float maxI=(float) maxInsersions/4;
		segmentSize[0]=(int) Math.rint(maxI);
		
		float maxU=(float) maxUpdates/4;
		segmentSize[1]=(int) Math.rint(maxU);
		
		float maxD=(float) maxDeletions/4;
		segmentSize[2]=(int) Math.rint(maxD);
		
		float maxT=(float) maxTotalChangesForOneTr/4;
		segmentSize[3]=(int) Math.rint(maxT);
	}

}
