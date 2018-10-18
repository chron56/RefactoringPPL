package gui.tableElements.tableConstructors;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import data.dataKeeper.GlobalDataKeeper;
import data.dataPPL.pplSQLSchema.PPLSchema;
import data.dataPPL.pplSQLSchema.PPLTable;
import data.dataPPL.pplTransition.AtomicChange;
import data.dataPPL.pplTransition.PPLTransition;
import data.dataPPL.pplTransition.TableChange;

public class TableConstructionAllSquaresIncluded implements PldConstruction {
	
	
	private static TreeMap<String,PPLSchema> allPPLSchemas=new TreeMap<String,PPLSchema>();
	private ArrayList<PPLTable>	tables=new ArrayList<PPLTable>();
	private TreeMap<Integer,PPLTransition> allPPLTransitions = new TreeMap<Integer,PPLTransition>();


	private int columnsNumber=0;
	private Integer[][] schemaColumnId=null;
	private int maxDeletions=1;
	private int maxInsersions=1;
	private int maxUpdates=1;
	private Integer segmentSize[]=new Integer[3];
	
	public TableConstructionAllSquaresIncluded(GlobalDataKeeper globalDataKeeper){
		
		allPPLSchemas=globalDataKeeper.getAllPPLSchemas();
		allPPLTransitions=globalDataKeeper.getAllPPLTransitions();
		
		
	}
	
	public String[] constructColumns(){
		
		ArrayList<String> columnsList=new ArrayList<String>();
		
		schemaColumnId=new Integer[allPPLSchemas.size()][2];
		
		for(int i=0;i<allPPLSchemas.size();i++){
			schemaColumnId[i][0]=i;
			if(i==0){
				schemaColumnId[i][1]=1;
			}
			else{
				schemaColumnId[i][1]=schemaColumnId[i-1][1]+4;
			}
		}
		
		columnsList.add("Table name");
		
		int i=0;
		
		for (Map.Entry<String,PPLSchema> pplSc : allPPLSchemas.entrySet()) {
			
			if(i<allPPLSchemas.size()-1){
				String label="v"+pplSc.getValue().getName().replaceAll(".sql", "");
				columnsList.add(label);
			
				for(int j=0; j<3; j++){
					
					switch(j){
						
						case 0: columnsList.add("I");
								break;
							
						case 1: columnsList.add("U");
								break;
								
						case 2: columnsList.add("D");
								break;
							
						default:break;
					}
					
					
				}
			
			}
			else{
				String label="v"+pplSc.getValue().getName().replaceAll(".sql", "");
				columnsList.add(label);
			}
			
			i++;
				
		}
		
		columnsNumber=columnsList.size();
		String[] tmpcolumns=new String[columnsList.size()];
		
		for(int j=0; j<columnsList.size(); j++ ){
			
			tmpcolumns[j]=columnsList.get(j);
			
		}
		
		return(tmpcolumns);
		
		
	}
	
	public String[][] constructRows(){
		
		ArrayList<String[]> allRows=new ArrayList<String[]>();
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
					String[] tmpOneRow=constructOneRow(oneTable,i);
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
		
		String[][] tmpRows=new String[allRows.size()][columnsNumber];
		
		for(int z=0; z<allRows.size(); z++){
			
			String[] tmpOneRow=allRows.get(z);
			for(int j=0; j<tmpOneRow.length; j++ ){
				
				tmpRows[z][j]=tmpOneRow[j];
				
				
			}
			
		}
		
		float maxI=(float) maxInsersions/4;
		segmentSize[0]=(int) Math.rint(maxI);
		
		float maxU=(float) maxUpdates/4;
		segmentSize[1]=(int) Math.rint(maxU);
		
		float maxD=(float) maxDeletions/4;
		segmentSize[2]=(int) Math.rint(maxD);
		
		return tmpRows;
		
	}
	
	private String[] constructOneRow(PPLTable oneTable,int schemaVersion){
		
		String[] oneRow=new String[columnsNumber];
		int deletedAllTable=0;
		int pointerCell=0;
		int updn=0;
		int deln=0;
		int insn=0;
		oneRow[pointerCell]=oneTable.getName();
		
		if(schemaVersion==0){
			pointerCell++;
			oneRow[pointerCell]="---------";
			pointerCell++;
			
		}
		else{
			
			for(int i=0; i<schemaColumnId.length; i++){
				
				if(schemaVersion==schemaColumnId[i][0]){
					pointerCell=schemaColumnId[i][1]-3;
					break;
				}
				
			}
			
		}
		
		
		int initialization=0;
		if(schemaVersion>0){
			initialization=schemaVersion-1;
		}
		
		Integer[] mapKeys = new Integer[allPPLTransitions.size()];
		int pos2 = 0;
		for (Integer key : allPPLTransitions.keySet()) {
		    mapKeys[pos2++] = key;
		}
		
		Integer pos3=null;

		for(int i=initialization; i<allPPLTransitions.size(); i++){
			
			pos3=mapKeys[i];
			
			PPLTransition  tmpTL=allPPLTransitions.get(pos3);
			
			String sc=tmpTL.getNewVersionName();
			
			ArrayList<TableChange> tmpTR=tmpTL.getTableChanges();
			
			updn=0;
			deln=0;
			insn=0;
			
			if(tmpTR!=null){
				for(int j=0; j<tmpTR.size(); j++){
					
					TableChange tableChange=tmpTR.get(j);
					
					if(tableChange.getAffectedTableName().equals(oneTable.getName())){
						
						
						
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
			
			
			oneRow[pointerCell]=Integer.toString(insn);
			pointerCell++;
			oneRow[pointerCell]=Integer.toString(updn);
			pointerCell++;
			oneRow[pointerCell]=Integer.toString(deln);
			pointerCell++;
			if(deletedAllTable==1){
				break;
			}
			oneRow[pointerCell]="------";
			pointerCell++;
			
			insn=0;
			updn=0;
			deln=0;
			
			
		}
		
		for(int i=0; i<oneRow.length; i++){
			if(oneRow[i]==null){
				oneRow[i]="";
			}
		}
	
		return oneRow;
		
	}
	
	public Integer[] getSegmentSize(){
		return segmentSize;
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
	
	

}
