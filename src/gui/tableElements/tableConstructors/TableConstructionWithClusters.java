package gui.tableElements.tableConstructors;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import phaseAnalyzer.commons.Phase;
import tableClustering.clusterExtractor.commons.Cluster;
import data.dataKeeper.GlobalDataKeeper;
import data.dataPPL.pplTransition.AtomicChange;
import data.dataPPL.pplTransition.PPLTransition;
import data.dataPPL.pplTransition.TableChange;

public class TableConstructionWithClusters implements PldConstruction{
	
	private ArrayList<Phase> phases = new ArrayList<Phase>();
	private ArrayList<Cluster> clusters = new ArrayList<Cluster>();


	private int columnsNumber=0;
	private Integer[][] schemaColumnId=null;
	private int maxDeletions=1;
	private int maxInsersions=1;
	private int maxUpdates=1;
	private int maxTotalChangesForOneTr=1;
	private Integer[] segmentSize=new Integer[4];
	
	public TableConstructionWithClusters(GlobalDataKeeper globalDataKeeper){
		
		globalDataKeeper.getAllPPLSchemas();
		phases=globalDataKeeper.getPhaseCollectors().get(0).getPhases();
		clusters=globalDataKeeper.getClusterCollectors().get(0).getClusters();
		
		
	}
	
	public String[] constructColumns(){
		
		ArrayList<String> columnsList=new ArrayList<String>();
		
		schemaColumnId=new Integer[phases.size()][2];
		
		for(int i=0;i<phases.size();i++){
			schemaColumnId[i][0]=i;
			if(i==0){
				schemaColumnId[i][1]=1;
			}
			else{
				schemaColumnId[i][1]=schemaColumnId[i-1][1]+1;
			}
		}
		
		columnsList.add("Table name");
		
		for(int i=0;i<phases.size(); i++){
			String label="Phase "+i;
			columnsList.add(label);
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
			
		for(int j=0; j<clusters.size(); j++){
			
				
			String[] tmpOneRow=constructOneRow(clusters.get(j),j);
			allRows.add(tmpOneRow);
			tmpOneRow=new String[columnsNumber];
				
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
		
		float maxT=(float) maxTotalChangesForOneTr/4;
		segmentSize[3]=(int) Math.rint(maxT);
		
		return tmpRows;
		
	}
	
	private String[] constructOneRow(Cluster cl,int clusteNum){
		
		String[] oneRow=new String[columnsNumber];
		int deletedAllTable=0;
		int pointerCell=0;
		int updn=0;
		int deln=0;
		int insn=0;
		int totalChangesForOnePhase=0;
		oneRow[pointerCell]="Cluster "+clusteNum;
		int deadCell=0;
		
		for(int p=0; p<phases.size(); p++){
			if(phases.get(p).getPhasePPLTransitions().containsKey(cl.getBirth())){
				pointerCell=p+1;
				break;
			}
		}

		for(int p=0; p<phases.size(); p++){
			if(phases.get(p).getPhasePPLTransitions().containsKey(cl.getDeath()-1)){
				deadCell=p+1;
				break;
			}
		}
		
		int initialization=0;
		if(pointerCell>0){
			initialization=pointerCell-1;
		}
		
		for(int p=initialization; p<phases.size(); p++){
			if(p<deadCell){
				TreeMap<Integer,PPLTransition> phasePPLTransitions=phases.get(p).getPhasePPLTransitions();
	
				if (totalChangesForOnePhase>maxTotalChangesForOneTr) {
					maxTotalChangesForOneTr=totalChangesForOnePhase;
				}
				totalChangesForOnePhase=0;
				
				
				for(Map.Entry<Integer, PPLTransition> tmpTL:phasePPLTransitions.entrySet()){
					
					ArrayList<TableChange> tmpTR=tmpTL.getValue().getTableChanges();
					
					if(tmpTR!=null){
						
						for(int j=0; j<tmpTR.size(); j++){
							
							TableChange tableChange=tmpTR.get(j);
							
							if(cl.getTables().containsKey(tableChange.getAffectedTableName())){
								
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
					
					if(deletedAllTable==1){
						break;
					}
					
				}
				
				if(pointerCell>=columnsNumber){
	
					break;
				}
				
				totalChangesForOnePhase=insn+updn+deln;
	
				oneRow[pointerCell]=Integer.toString(totalChangesForOnePhase);
				
				pointerCell++;
				
				if(deletedAllTable==1){
					break;
				}
				
				insn=0;
				updn=0;
				deln=0;
	
			}
			else{
				break;
			}
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
	
	public Integer[] getSegmentSize(){
		return segmentSize;
	}
	
}
