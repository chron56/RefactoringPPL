
package phaseAnalyzer.commons;

import java.util.ArrayList;

import data.dataKeeper.GlobalDataKeeper;

public class PhaseCollector {
	public PhaseCollector(){
		phases = new ArrayList<Phase>();
	}

	public PhaseCollector(ArrayList<Phase> phases) {
		this.phases = phases;
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
	
	
	public String toStringShort() {
		String s=new String("");

		for(Phase p: phases){
			s = s + p.toStringShort()+ "\n";
			totalSum=totalSum+p.getSum();
		}
		s= s + "\t"+ "\t"+ "\t"+totalSum;
		s = s + "\n";
		return s;
	}

	public String toStringShortAss2() {
		String s=new String("");
		
		for(int i=0; i<phases.size()-1; i++){
			
			s=s+i+"@"+(i+1)+"\t";
			float timeD = Math.abs((float)(phases.get(i).getTransitionHistory().getValues().get(phases.get(i).getEndPos()).getTime()-
					phases.get(i+1).getTransitionHistory().getValues().get(phases.get(i+1).getStartPos()).getTime())/84600);
			s=s+timeD+"\t";
			float changeD = Math.abs((float)(phases.get(i).getTransitionHistory().getValues().get(phases.get(i).getEndPos()).getTotalUpdInTr()-
					phases.get(i+1).getTransitionHistory().getValues().get(phases.get(i+1).getStartPos()).getTotalUpdInTr()));
			float avgD = (Math.abs((float)((phases.get(i).getTotalUpdates()/(phases.get(i).getEndPos()-phases.get(i).getStartPos()+1))-
					(phases.get(i+1).getTotalUpdates()/(phases.get(i+1).getEndPos()-phases.get(i+1).getStartPos()+1)))))/phases.get(i).getTransitionHistory().getTotalUpdates();
			s=s+changeD+"\t"+avgD+"\n";
			
		}
		s = s + "\n";
		return s;
	}

	public void connectPhasesWithTransitions(GlobalDataKeeper tmpGlobalDataKeeper){
		for(Phase p: phases){
		
			p.connectWithPPLTransitions(tmpGlobalDataKeeper);
			
		}
		
	}
	
	private double totalSum=0;
	private ArrayList<Phase> phases;

}
