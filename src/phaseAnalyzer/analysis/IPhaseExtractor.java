package phaseAnalyzer.analysis;

import phaseAnalyzer.commons.PhaseCollector;
import phaseAnalyzer.commons.TransitionHistory;

public interface IPhaseExtractor {

	public PhaseCollector extractAtMostKPhases(TransitionHistory transitionHistory, int numPhases,
									float timeWeight,float changeWeight,boolean preProcessingTime,boolean preProcessingChange );
}
