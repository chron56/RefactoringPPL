
package phaseAnalyzer.commons;

import java.util.ArrayList;
import java.util.TreeMap;
import data.dataPPL.pplTransition.PPLTransition;

public class PhaseCollector {
	public PhaseCollector(){
		phases = new ArrayList<Phase>();
	}

	
	public ArrayList<Phase> getPhases() {
		return phases;
	}

	public void setPhases(ArrayList<Phase> phases) {
		this.phases = phases;
	}

	public void addPhase(Phase p){
		this.phases.add(p);
	}
	
	public double getTotalSum(){
		
		return totalSum;
		
	}
	
	public int getSize(){
		
		return this.phases.size();
		
	}
	


	public void connectPhasesWithTransitions(TreeMap<Integer, PPLTransition> allPPLTransitions){
		for(Phase p: phases){
		
			p.connectWithPPLTransitions(allPPLTransitions);
			
		}
		
	}
	
	private double totalSum=0;
	private ArrayList<Phase> phases;
	

}
