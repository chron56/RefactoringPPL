package phaseAnalyzer.engine;

import java.util.ArrayList;
import java.util.TreeMap;
import data.dataPPL.pplTransition.PPLTransition;
import phaseAnalyzer.commons.Phase;
import phaseAnalyzer.commons.PhaseCollector;

public interface IPhase {
	public void setPhaseCollectors(TreeMap<Integer, PPLTransition> transitions,int numPhases,String inputCsv,String outputAssessment1,String outputAssessment2,Float tmpTimeWeight, Float tmpChangeWeight,
			Boolean tmpPreProcessingTime,Boolean tmpPreProcessingChange);
	public ArrayList<PhaseCollector> getPhaseCollectors();
	public ArrayList<Phase> getPhases();
}
