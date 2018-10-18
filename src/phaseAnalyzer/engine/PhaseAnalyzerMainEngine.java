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
	/*
	public void extractReportAssessment1() throws IOException{
		String report="";
		System.out.println("!!:"+phaseCollectors.size());
		report=report+inputCsv+"\n";
		int position=0;
		
		String preProcessingTime="";
		String preProcessingChanges="";
		if(preProcessingChange){
			preProcessingChanges="PreProcessingChanges:ON";
		}
		else{
			preProcessingChanges="PreProcessingChanges:OFF";
		}
		if(this.preProcessingTime){
			preProcessingTime="PreProcessingTime:ON";
		}
		else{
			preProcessingTime="PreProcessingTime:OFF";
		}
		
		report=report+"WC: "+changeWeight+"\t"+"WT: "+timeWeight+"\n";
		//System.out.println(PhaseExtractionParameters.CHANGE_WEIGHT_AR.get(i)+"\t"+PhaseExtractionParameters.TIME_WEIGHT_AR.get(i)+"\n");
		report=report+preProcessingChanges+"\t"+preProcessingTime+"\n";
		//System.out.println(preProcessingChanges+"\t"+preProcessingTime+"\n");
		report=report+"Start"+"\t"+"End"+"\t"+"TotUpd"+"\t"+"mi"+"\t"+"ei"+"\t"+"abs"+"\t"+"pow"+"\t"+"sum"+"\n";
		//System.out.println("Start"+"\t"+"End"+"\t"+"TotUpd"+"\t"+"mi"+"\t"+"ei"+"\t"+"abs"+"\t"+"pow"+"\t"+"sum");
		report=report+phaseCollectors.get(position).toStringShort();
		report=report+"\n";
		//System.out.println(phaseCollector.toStringShort());
		
		position++;
		
		
		
		File file = new File(outputAssessment1);
		 System.out.println(inputCsv);
		// if file doesnt exists, then create it
		if (!file.exists()) {
			
			file.createNewFile();
			
		}

		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(report);
		bw.close();
		
		System.out.println(inputCsv+"Done");


		
	}
	
	public void extractReportAssessment2() throws IOException{
		String report="";
		System.out.println("!!:"+phaseCollectors.size());
		report=report+inputCsv+"\n";
		int position=0;
				
		String preProcessingTime="";
		String preProcessingChanges="";
		if(preProcessingChange){
			preProcessingChanges="PreProcessingChanges:ON";
		}
		else{
			preProcessingChanges="PreProcessingChanges:OFF";
		}
		if(this.preProcessingTime){
			preProcessingTime="PreProcessingTime:ON";
		}
		else{
			preProcessingTime="PreProcessingTime:OFF";
		}
		
		report=report+"WC: "+changeWeight+"\t"+"WT: "+timeWeight+"\n";
		//System.out.println(PhaseExtractionParameters.CHANGE_WEIGHT_AR.get(i)+"\t"+PhaseExtractionParameters.TIME_WEIGHT_AR.get(i)+"\n");
		report=report+preProcessingChanges+"\t"+preProcessingTime+"\n";
		//System.out.println(preProcessingChanges+"\t"+preProcessingTime+"\n");
		report=report+"Phases"+"\t"+"dTime"+"\t"+"dChange"+"\t"+"avgDChange"+"\n";
		//System.out.println("Start"+"\t"+"End"+"\t"+"TotUpd"+"\t"+"mi"+"\t"+"ei"+"\t"+"abs"+"\t"+"pow"+"\t"+"sum");
		//report=report+position+"-"+(position+1)+"\n";
		report=report+phaseCollectors.get(position).toStringShortAss2();

		
		report=report+"\n";
		//System.out.println(phaseCollector.toStringShort());
		
		position++;
		
		File file = new File(outputAssessment2);
		 
		// if file doesnt exists, then create it
		if (!file.exists()) {
			
			file.createNewFile();
			
		}

		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(report);
		bw.close();
		
		System.out.println(inputCsv+"Done");


		
	}
	
	public void extractWinnersReport() throws IOException{
		String folder = new String("output/");

		String reportWinners="\t";
		
				
		String preProcessingTime="";
		String preProcessingChanges="";
		if(preProcessingChange){
			preProcessingChanges="PreProCh:ON";
		}
		else{
			preProcessingChanges="PreProCh:OFF";
		}
		if(this.preProcessingTime){
			preProcessingTime="PreProTi:ON";
		}
		else{
			preProcessingTime="PreProTi:OFF";
		}
		
		reportWinners=reportWinners+"WC:"+changeWeight+" "+"WT:"
				+timeWeight+" "+preProcessingChanges+" "+preProcessingTime;

		reportWinners=reportWinners+"\t";
				
			
		
		for(Map.Entry<String, ArrayList<PhaseCollector>> entry: allPhaseCollectors.entrySet()) {
			reportWinners=reportWinners+"\n";
			reportWinners=reportWinners+entry.getKey()+"\t";
			ArrayList<PhaseCollector> tmpPhaseCollectors=entry.getValue();
			for(PhaseCollector phCl:tmpPhaseCollectors){
				reportWinners=reportWinners+phCl.getTotalSum()+"\t";
			}
		    //System.out.println(entry.getKey() + " : " + entry.getValue());
		}
		
		
		
		
		File file = new File(folder+"reportWinners"+".txt");
		 
		// if file doesnt exists, then create it
		if (!file.exists()) {
			
			file.createNewFile();
			
		}

		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(reportWinners);
		bw.close();
		
		System.out.println("Done");
	
		
	}
	*/
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
	//private String outputAssessment1;
	//private String outputAssessment2;
	private float timeWeight;
	private float changeWeight;
	private boolean preProcessingTime;
	private boolean preProcessingChange;
	
	
}
