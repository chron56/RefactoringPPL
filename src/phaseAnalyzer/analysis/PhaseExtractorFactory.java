
package phaseAnalyzer.analysis;


public class PhaseExtractorFactory {

	public IPhaseExtractor createPhaseExtractor(String concreteClassName){
		if (concreteClassName.equals("BottomUpPhaseExtractor")){
			return new BottomUpPhaseExtractor();
		}
		
		return null;
	}
	
	
}
