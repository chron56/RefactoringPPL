package phaseAnalyzer.commons;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import data.dataKeeper.GlobalDataKeeper;
import data.dataPPL.pplTransition.PPLTransition;

public class Phase {
	
	public Phase(TransitionHistory transitionHistory) {
		this.transitionHistory = transitionHistory;
		subPhases=new ArrayList<Phase>();
		
	}
	
	
	public int getStartPos() {
		return startPos;
	}
	public int getEndPos() {
		return endPos;
	}
	public TransitionHistory getTransitionHistory() {
		return transitionHistory;
	}
	public int getTotalUpdates() {
		return totalUpdates;
	}

	public void setStartPos(int startPos) {
		this.startPos = startPos;
		startSQLFile = this.transitionHistory.getValues().get(startPos).getOldVersionFile();
	}
	
	
	public void setEndPos(int endPos) {
		this.endPos = endPos;
		endSQLFile = this.transitionHistory.getValues().get(endPos).getNewVersionFile();
	}
	public void setTransitionHistory(TransitionHistory transitionHistory) {
		this.transitionHistory = transitionHistory;
	}

	public void setTotalUpdates(int totalUpdates) {
		this.totalUpdates = totalUpdates;
	}
	
	public int getTotalAdditionsOfPhase(){
		int additions=0;
		for(Map.Entry<Integer, PPLTransition> pplTr:phasePPLTransitions.entrySet()){
			additions=additions+pplTr.getValue().getNumberOfAdditionsForOneTr();
		}
		return additions;
	}
	
	public int getTotalDeletionsOfPhase(){
		
		int deletions=0;
		
		for(Map.Entry<Integer, PPLTransition> pplTr:phasePPLTransitions.entrySet()){
			deletions=deletions+pplTr.getValue().getNumberOfDeletionsForOneTr();
		}
		
		return deletions;
	}
	
	public int getTotalUpdatesOfPhase(){
		
		int updates=0;
		
		for(Map.Entry<Integer, PPLTransition> pplTr:phasePPLTransitions.entrySet()){
			updates=updates+pplTr.getValue().getNumberOfUpdatesForOneTr();
		}
		
		return updates;
	}
	
	
	public double getSum(){
		return sum;
	}
	
	public TreeMap<Integer,PPLTransition> getPhasePPLTransitions(){
		return phasePPLTransitions;
	}
	
	public String toStringShort(){
		String s = new String();
		DecimalFormat df = new DecimalFormat("0.00");
		float avg=(float)totalUpdates/(endPos-startPos+1);
		s = startPos + "\t" + endPos + "\t" + totalUpdates+ "\t" + df.format(avg) + "\t" ;
		ArrayList<TransitionStats> trSt = transitionHistory.getValues();
		for(int i=startPos; i<=endPos; i++){
			
			TransitionStats currTransition=trSt.get(i);
			int currUpdates=currTransition.getNumAttrDel() + currTransition.getNumAttrDelWithDelTables()+ currTransition.getNumAttrInKeyAlt() +
					currTransition.getNumAttrIns() + currTransition.getNumAttrInsInNewTables() + currTransition.getNumAttrWithTypeAlt();
			float subtraction=Math.abs(currUpdates-avg);
			double power=Math.pow(subtraction, 1);
			sum=sum+power;
			s=s+currUpdates+ "\t" + df.format(subtraction)+ "\t" + df.format(power);
			if(i==endPos){
				s=s+"\t"+df.format(sum);
			}
			s=s+"\n"+"\t"+"\t"+"\t"+"\t";
		}

		return s;
	}

	public double distance(Phase anotherPhase,float timeWeight, float changeWeight){
	
		int transitionHistoryTotalUpdates = transitionHistory.getTotalUpdates();
		double changeDistance = Math.abs(this.totalUpdates - anotherPhase.totalUpdates)/((double)transitionHistoryTotalUpdates);
	
		double timeDistance = 0;
		Phase subsequent, preceding; 
		if(this.startPos > anotherPhase.endPos){
			subsequent = this;
			preceding = anotherPhase;
		}
		else{
			preceding = this;
			subsequent = anotherPhase;
		}

		timeDistance = (((transitionHistory.getValues().get(subsequent.startPos).getTime() - transitionHistory.getValues().get(preceding.endPos).getTime())/86400))/(transitionHistory.getTotalTime());
		double totalDistance = changeWeight * changeDistance + timeWeight * timeDistance;
		return totalDistance;
	}

	/**
	 * Merges a phase with its next and returns a new object
	 * @param nextPhase has to be the next phase that is to be merged with the current one
	 * @return
	 */
	public Phase mergeWithNextPhase(Phase nextPhase){
		Phase newPhase = new Phase(transitionHistory);
			
		newPhase.startPos = this.startPos;
		newPhase.endPos = nextPhase.endPos;
		newPhase.startSQLFile = this.startSQLFile;
		newPhase.endSQLFile = nextPhase.endSQLFile;

		newPhase.totalUpdates = this.totalUpdates + nextPhase.totalUpdates;
		newPhase.subPhases.add(this);
		newPhase.subPhases.add(nextPhase);
		for(int i=0; i<this.subPhases.size(); i++){	
			
			newPhase.subPhases.add(this.subPhases.get(i));
		}
		for(int i=0; i<nextPhase.subPhases.size(); i++){	
			newPhase.subPhases.add(nextPhase.subPhases.get(i));
		}
		
		return newPhase;
	}
	
	public void connectWithPPLTransitions(GlobalDataKeeper tmpGlobalDataKeeper){
		
		TreeMap<Integer,PPLTransition> allPPLTransitions=tmpGlobalDataKeeper.getAllPPLTransitions();
		
		boolean found = false;

		for (Map.Entry<Integer,PPLTransition> tr : allPPLTransitions.entrySet()) {
			
			if(tr.getValue().getOldVersionName().equals(startSQLFile)){
				
				found=true;
				
			}
			if(found) {
				this.phasePPLTransitions.put(tr.getKey(), tr.getValue());

			}
			if(tr.getValue().getNewVersionName().equals(endSQLFile)){
				
				break;				
			}
		}
		System.out.println(startPos+" "+startSQLFile+" "+endPos+" "+endSQLFile);
		
		
	}
	
	public int getSize(){
		return phasePPLTransitions.size();
	}
	
	
	
	private int startPos;
	private int endPos;
	private String startSQLFile;
	private String endSQLFile;
	private ArrayList<Phase> subPhases = new ArrayList<Phase>();
	private int totalUpdates;
	private TransitionHistory transitionHistory;
	private double sum=0;
	private TreeMap<Integer,PPLTransition> phasePPLTransitions = new TreeMap<Integer,PPLTransition>();


}
