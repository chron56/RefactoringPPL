package data.dataTables;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import data.dataPPL.pplSQLSchema.PPLSchema;
import data.dataPPL.pplSQLSchema.PPLTable;
import data.dataPPL.pplTransition.AtomicChange;
import data.dataPPL.pplTransition.PPLTransition;
import data.dataPPL.pplTransition.TableChange;

public class TableConstructionZoomArea extends PldConstruction {

	private static TreeMap<String,PPLSchema> allPPLSchemas=new TreeMap<String,PPLSchema>();
	private static TreeMap<String,PPLSchema> selectedPPLSchemas=new TreeMap<String,PPLSchema>();
	private ArrayList<PPLTable>	tables=new ArrayList<PPLTable>();
	private TreeMap<String,PPLTable> selectedTables = new TreeMap<String,PPLTable>();
	private ArrayList<String> sSelectedTables = new ArrayList<String>();
	private TreeMap<Integer,PPLTransition> pplTransitions = new TreeMap<Integer,PPLTransition>();
	private int selectedColumn;

	private int columnsNumber=0;
	private Integer[][] schemaColumnId=null;
	private int maxDeletions=1;
	private int maxInsersions=1;
	private int maxUpdates=1;
	private int maxTotalChangesForOneTr=1;

	private Integer segmentSize[]=new Integer[4];
	
	public TableConstructionZoomArea(ArrayList<String> sSelectedTables,int selectedColumn){
		this.sSelectedTables=sSelectedTables;
		this.selectedColumn=selectedColumn;
	}
	
	
	
	public void fillSelectedPPLTransitions(TreeMap<Integer, PPLTransition> pplTransitions,TreeMap<Integer, PPLTransition> phasePplTransitions ) {
		
		if(selectedColumn==0){
			this.pplTransitions=pplTransitions;
		}
		else{
			this.pplTransitions=phasePplTransitions;

		}
		
	}
	
	public void fillSelectedPPLSchemas(TreeMap<String, PPLSchema> pplSchemas){
		allPPLSchemas=pplSchemas;
		for (Map.Entry<Integer,PPLTransition> pplTr : pplTransitions.entrySet()) {
			
			selectedPPLSchemas.put(pplTr.getValue().getOldVersionName(), pplSchemas.get(pplTr.getValue().getOldVersionName()));
			
		}
		
	}
	
	public void fillSelectedTables(TreeMap<String, PPLTable> AllPPLTables){
		
		for(int i=0; i<sSelectedTables.size(); i++){
			selectedTables.put(sSelectedTables.get(i),AllPPLTables.get(sSelectedTables.get(i)) );
		}
		
	}
	

	
	public ArrayList<String[]> constructParticularRows(ArrayList<String[]> allRows ){
	    ArrayList<String>	allTables=new ArrayList<String>();
		int found=0;	
			for(Map.Entry<String, PPLTable> oneTable:selectedTables.entrySet()){
				
					String tmpTableName=oneTable.getKey();
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
						tables.add(oneTable.getValue());
						String[] tmpOneRow=constructOneRow(oneTable.getValue());
						allRows.add(tmpOneRow);
						tmpOneRow=new String[columnsNumber];
					}
					else{
						found=0;
					}
				
				
			}
		
		
		
		return allRows;
		
	}
	
	private String[] constructOneRow(PPLTable oneTable){
		
		String[] oneRow=new String[columnsNumber];
		int deletedAllTable=0;
		int pointerCell=0;
		int updn=0;
		int deln=0;
		int insn=0;
		int totalChangesForOneTransition=-1;
		boolean reborn = true;
		oneRow[pointerCell]=oneTable.getName();
		boolean exists=false;
		for (Map.Entry<Integer,PPLTransition> pplTr : pplTransitions.entrySet()) {
						
			pointerCell++;
			
			PPLSchema oldS = allPPLSchemas.get(pplTr.getValue().getOldVersionName());
			PPLSchema newS = allPPLSchemas.get(pplTr.getValue().getNewVersionName());

			if(oldS.getTables().containsKey(oneTable.getName())|| newS.getTables().containsKey(oneTable.getName())){
				exists=true;
				break;
			}
			
		}		
		
		
		int initialization=pointerCell-1;
		
		
		Integer[] mapKeys = new Integer[pplTransitions.size()];
		int pos2 = 0;
		for (Integer key : pplTransitions.keySet()) {
		    mapKeys[pos2++] = key;
		}
		
		Integer pos3=null;
		if(exists){
			for(int i=initialization; i<pplTransitions.size(); i++){
				
				pos3=mapKeys[i];
				
				PPLTransition  tmpTL=pplTransitions.get(pos3);
				
				String sc=tmpTL.getNewVersionName();
				
				ArrayList<TableChange> tmpTR=tmpTL.getTableChanges();
				
				updn=0;
				deln=0;
				insn=0;
				
				if(tmpTR!=null){
					totalChangesForOneTransition=-1;
					
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
									 
									 int num=getNumOfAttributesOfNextSchema(sc, oneTable.getName());
									 
									 if(num==0){
										 
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
				if(pointerCell>=columnsNumber){
					
					break;
				}
				totalChangesForOneTransition=insn+updn+deln;

				if(totalChangesForOneTransition>=0 && reborn){

					oneRow[pointerCell]=Integer.toString(totalChangesForOneTransition);
					
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
				
				if (totalChangesForOneTransition>maxTotalChangesForOneTr) {
					maxTotalChangesForOneTr=totalChangesForOneTransition;
				}
				
				insn=0;
				updn=0;
				deln=0;
				
			}
		}
		
		for(int i=0; i<oneRow.length; i++){
			if(oneRow[i]==null){
				oneRow[i]="";
			}
		}
		
	
		return oneRow;
		
	}
	
	private int getNumOfAttributesOfNextSchema(String schema,String table){
		int num = 0;
		PPLSchema sc=allPPLSchemas.get(schema);
		
		for(int i=0;i<sc.getTables().size();i++){
			if(sc.getTableAt(i).getName().equals(table)){
				num=sc.getTableAt(i).getAttrs().size();
				return num;
			}
		}
		return num;
	}

	public int getColumnSize(){
		return pplTransitions.size();
	}

	public ArrayList<String> setColumnLabel(ArrayList<String> columnsList) {
		for (Map.Entry<Integer,PPLTransition> pplTr : pplTransitions.entrySet()) {
			
			String label=Integer.toString(pplTr.getKey());
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
