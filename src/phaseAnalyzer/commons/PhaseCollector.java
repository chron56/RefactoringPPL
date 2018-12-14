
package phaseAnalyzer.commons;

import java.util.ArrayList;

import data.dataKeeper.GlobalDataKeeper;

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
	


	public void connectPhasesWithTransitions(GlobalDataKeeper tmpGlobalDataKeeper){
		for(Phase p: phases){
		
			p.connectWithPPLTransitions(tmpGlobalDataKeeper);
			
		}
		
	}
	
	private double totalSum=0;
	private ArrayList<Phase> phases;

}
