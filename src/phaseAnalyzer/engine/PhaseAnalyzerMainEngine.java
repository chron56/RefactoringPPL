package phaseAnalyzer.engine;


import java.util.ArrayList;
import java.util.HashMap;

import phaseAnalyzer.analysis.IPhaseExtractor;
import phaseAnalyzer.analysis.PhaseExtractorFactory;
import phaseAnalyzer.commons.PhaseCollector;
import phaseAnalyzer.commons.TransitionHistory;
import phaseAnalyzer.parser.IParser;
import phaseAnalyzer.parser.ParserFactory;
import data.dataKeeper.GlobalDataKeeper;

public class PhaseAnalyzerMainEngine {
	
	public PhaseAnalyzerMainEngine(String inputCsv,String outputAssessment1,String outputAssessment2,Float tmpTimeWeight, Float tmpChangeWeight,
														Boolean tmpPreProcessingTime,Boolean tmpPreProcessingChange){
		
		timeWeight=tmpTimeWeight;
		changeWeight=tmpChangeWeight;
		preProcessingTime=tmpPreProcessingTime;
		preProcessingChange=tmpPreProcessingChange;
		
		this.inputCsv=inputCsv;
		
		parserFactory = new ParserFactory();
		parser = parserFactory.createParser("SimpleTextParser");

		phaseExtractorFactory = new PhaseExtractorFactory();
		phaseExtractor = phaseExtractorFactory.createPhaseExtractor("BottomUpPhaseExtractor");
		
		transitionHistory = new TransitionHistory();
		
		allPhaseCollectors = new HashMap<String, ArrayList<PhaseCollector>>();
		
	}

	public ArrayList<PhaseCollector> getPhaseCollectors(){
		return phaseCollectors;
	}
	
	public void extractPhases(int numPhases){
		phaseCollectors = new ArrayList<PhaseCollector>();
		
		PhaseCollector phaseCollector = new PhaseCollector();
		phaseCollector = phaseExtractor.extractAtMostKPhases(transitionHistory, numPhases,timeWeight,changeWeight,preProcessingTime,preProcessingChange);
		phaseCollectors.add(phaseCollector);
		
		allPhaseCollectors.put(inputCsv, phaseCollectors);
	}

	public void connectTransitionsWithPhases(GlobalDataKeeper tmpGlobalDataKeeper){
		phaseCollectors.get(0).connectPhasesWithTransitions(tmpGlobalDataKeeper);
	}
	
	public void parseInput(){

		this.transitionHistory = parser.parse(inputCsv, ";"); 
		this.transitionHistory.consoleVerticalReport();
	}
	
	private ParserFactory parserFactory;
	private IParser parser;
	private PhaseExtractorFactory phaseExtractorFactory;
	private IPhaseExtractor phaseExtractor;
	private TransitionHistory transitionHistory;
	
	private ArrayList<PhaseCollector> phaseCollectors;
	private HashMap<String,ArrayList<PhaseCollector>> allPhaseCollectors;
	private String inputCsv;

	private float timeWeight;
	private float changeWeight;
	private boolean preProcessingTime;
	private boolean preProcessingChange;
	
	
}
