/**
 * 
 */
package phaseAnalyzer.analysis;

import java.util.ArrayList;
import java.util.Iterator;

import phaseAnalyzer.commons.Phase;
import phaseAnalyzer.commons.PhaseCollector;
import phaseAnalyzer.commons.TransitionHistory;
import phaseAnalyzer.commons.TransitionStats;

public class BottomUpPhaseExtractor implements IPhaseExtractor {

	/* (non-Javadoc)
	 * @see analysis.IPhaseExtractor#extractAtMostKPhases(commons.TransitionHistory, int)
	 */
	@Override
	public PhaseCollector extractAtMostKPhases(	TransitionHistory transitionHistory, int numPhases,
											float timeWeight,float changeWeight,boolean preProcessingTime,boolean preProcessingChange) {
		
		PhaseCollector initSolution = new PhaseCollector();
		this.init(transitionHistory, initSolution);
		
		/* 
		 * TIME preprocessing
		 * */
		PhaseCollector preProcessedSolutionTime = new PhaseCollector();
		preProcessedSolutionTime = initSolution;
		
		if (preProcessingTime){
			preProcessedSolutionTime = performTimePreprocessing(transitionHistory, initSolution);
		}
		/* 
		 * CHANGE preprocessing
		 * */
		PhaseCollector preProcessedSolutionChanges = new PhaseCollector();
		preProcessedSolutionChanges = preProcessedSolutionTime; 
		if (preProcessingChange){	
			preProcessedSolutionChanges = performChangePreprocessing(transitionHistory, preProcessedSolutionTime);
		}
		/*
		 * REGULAR processing
		 */
		PhaseCollector currentSolution = new PhaseCollector();
		currentSolution = this.newPhaseCollector(transitionHistory, preProcessedSolutionChanges,timeWeight,changeWeight);

		while (currentSolution.getPhases().size() > numPhases){
			currentSolution = this.newPhaseCollector(transitionHistory, currentSolution,timeWeight,changeWeight);
		}
		return currentSolution;
	}

	/**
	 * @param transitionHistory
	 * @param initSolution
	 * @return
	 */
	private PhaseCollector performTimePreprocessing(
				TransitionHistory transitionHistory, PhaseCollector initSolution) {
		
		PhaseCollector preProcessedSolutionTime = new PhaseCollector();
		preProcessedSolutionTime = this.preProcessOverTime(transitionHistory, initSolution);
		int oldSize=initSolution.getPhases().size();

		while(oldSize!=preProcessedSolutionTime.getPhases().size()){

			oldSize=preProcessedSolutionTime.getPhases().size();
			preProcessedSolutionTime = this.preProcessOverTime(transitionHistory, preProcessedSolutionTime);
			
		}
		return preProcessedSolutionTime;
	}

	/**
	 * @param transitionHistory
	 * @param preProcessedSolutionTime
	 * @return
	 */
	private PhaseCollector performChangePreprocessing(TransitionHistory transitionHistory,
														PhaseCollector preProcessedSolutionTime) {
		
		int oldSize;
		PhaseCollector preProcessedSolutionChanges = new PhaseCollector();
		preProcessedSolutionChanges=this.preProcessOverChanges(transitionHistory, preProcessedSolutionTime);
		oldSize=preProcessedSolutionTime.getPhases().size();

		while(oldSize!=preProcessedSolutionChanges.getPhases().size()){

			oldSize=preProcessedSolutionChanges.getPhases().size();
			preProcessedSolutionChanges = this.preProcessOverChanges(transitionHistory, preProcessedSolutionChanges);
			
		}
		return preProcessedSolutionChanges;
	}

	public PhaseCollector newPhaseCollector(TransitionHistory transitionHistory, PhaseCollector prevCollector,float timeWeight,float changeWeight){
		PhaseCollector newCollector = new PhaseCollector();
		ArrayList<Phase> newPhases = new ArrayList<Phase>();
		ArrayList<Phase> oldPhases = prevCollector.getPhases();
		int oldSize = oldPhases.size();
		if (oldSize == 0){
			
			System.out.println("Sth went terribly worng at method XXX");
			System.exit(-10);
		}

		//compute the distances for all the bloody phases
		//TODO add it at phase collector to move on !$#@$#%$^$%&%&
		double distances[] = new double[oldSize];
		distances[0] = Double.MAX_VALUE;
		int pI = 0;
		
	    Iterator<Phase> phaseIter = oldPhases.iterator();
	    Phase previousPhase = phaseIter.next();
	    while (phaseIter.hasNext()){
	      Phase p = phaseIter.next();
	      pI++;
	      distances[pI] = p.distance(previousPhase,timeWeight,changeWeight);
	      //System.out.println(pI +":\t" + distances[pI]);
	      
	      previousPhase = p;
	    }

		//find the two most similar phases in the old collection
	    int posI=-1; double minDist = Double.MAX_VALUE;
	    for(int i=1; i<oldSize; i++){
	    	if(distances[i]<minDist){
	    		posI = i;
	    		minDist = distances[i];
	    	}
	    }
	    //merge them in a new phase. Merge posI with its PREVIOUS (ATTN!!)
		Phase toMerge = oldPhases.get(posI-1);
		Phase newPhase = toMerge.mergeWithNextPhase(oldPhases.get(posI));
		//System.out.println("\n\nMErging: " + (posI-1) + " , " + posI + " with distance "  + minDist);		
		//assuming we merge i, i+1 add the 0..i-1 to the new phases
		for(int i=0; i < posI-1; i++){
			Phase p = oldPhases.get(i);
			newPhases.add(p);
		}
		//add the new i_new = merge(i,i+1) to the new phases
		newPhases.add(newPhase);
		//add the i+1, .. last to the new phases
		if(posI<oldSize-1){
			for(int i=posI+1; i < oldSize; i++){
				Phase p = oldPhases.get(i);
				newPhases.add(p);
			}		
		}
		newCollector.setPhases(newPhases);

		return newCollector;
	}
	
	private PhaseCollector preProcessOverTime(TransitionHistory transitionHistory, PhaseCollector prevCollector){
		
		PhaseCollector preProcessedCollector = new PhaseCollector();
		ArrayList<Phase> oldPhases = prevCollector.getPhases();
		ArrayList<Phase> newPhases = new ArrayList<Phase>();
		int pos=0;
		int oldSize = oldPhases.size();

		Iterator<Phase> phaseIter = oldPhases.iterator();
	    Phase previousPhase = phaseIter.next();
	    //System.out.println(oldSize);

	   
	    while (phaseIter.hasNext()){
	    	pos++;
	    	Phase p = phaseIter.next();
	    	if(((transitionHistory.getValues().get(p.getStartPos()).getTime()-transitionHistory.getValues().get(previousPhase.getEndPos()).getTime())/84600)<3){
	    		Phase toMerge = previousPhase;
	    		Phase newPhase = toMerge.mergeWithNextPhase(p);
	    		//System.out.println("Merging "+(toMerge.getStartPos())+" with "+(p.getEndPos())+" pos "+pos);
	    		
	    		for(int i=0; i < pos-1; i++){
	    			Phase p1 = oldPhases.get(i);
	    			newPhases.add(p1);
	    		}
	    		//add the new i_new = merge(i,i+1) to the new phases
	    		newPhases.add(newPhase);
	    		//add the i+1, .. last to the new phases
	    		if(pos<oldSize-1){

	    			for(int i=pos+1; i < oldSize; i++){

	    				Phase p1 = oldPhases.get(i);
	    				newPhases.add(p1);
	    			}		
	    		}
	    		
	    		break;
	    	}
	    	previousPhase = p;
	    }
	    
	    if(newPhases.size()!=0){
	    	preProcessedCollector.setPhases(newPhases);
	    }
	    else{
	    	preProcessedCollector.setPhases(oldPhases);
	    }
		return preProcessedCollector;
	}
	
	private PhaseCollector preProcessOverChanges(TransitionHistory transitionHistory, PhaseCollector prevCollector){
		
		PhaseCollector preProcessedCollector = new PhaseCollector();
		ArrayList<Phase> oldPhases = prevCollector.getPhases();
		ArrayList<Phase> newPhases = new ArrayList<Phase>();
		int pos=0;
		int oldSize = oldPhases.size();

		Iterator<Phase> phaseIter = oldPhases.iterator();
	    Phase previousPhase = phaseIter.next();
	    //System.out.println(newPhases.size()+" "+oldSize);

	   
	    while (phaseIter.hasNext()){
	    	pos++;
	    	Phase p = phaseIter.next();
	    	//System.out.println(("****** "+(transitionHistory.getValues().get(p.getStartPos()).getTotalAttrChange()-transitionHistory.getValues().get(previousPhase.getEndPos()).getTotalAttrChange())));
	    	if(((transitionHistory.getValues().get(p.getStartPos()).getTotalAttrChange()-transitionHistory.getValues().get(previousPhase.getEndPos()).getTotalAttrChange()==0))){
	    		Phase toMerge = previousPhase;
	    		Phase newPhase = toMerge.mergeWithNextPhase(p);
	    		//System.out.println("Merging "+(toMerge.getStartPos())+" with "+(p.getEndPos()));
	    		
	    		for(int i=0; i < pos-1; i++){
	    			Phase p1 = oldPhases.get(i);
	    			newPhases.add(p1);
	    		}
	    		//add the new i_new = merge(i,i+1) to the new phases
	    		newPhases.add(newPhase);
	    		//add the i+1, .. last to the new phases
	    		if(pos<oldSize-1){
	    			for(int i=pos+1; i < oldSize; i++){
	    				Phase p1 = oldPhases.get(i);
	    				newPhases.add(p1);
	    			}		
	    		}
	    		
	    		break;
	    	}
	    	previousPhase = p;
	    }
	    
	    if(newPhases.size()!=0){
	    	preProcessedCollector.setPhases(newPhases);
	    }
	    else{
	    	preProcessedCollector.setPhases(oldPhases);
	    }

		return preProcessedCollector;
	}
	
	public PhaseCollector init(TransitionHistory transitionHistory, PhaseCollector phaseCollector){
		for(TransitionStats v: transitionHistory.getValues()){
			Phase p = new Phase(transitionHistory);
			int pos = transitionHistory.getValues().indexOf(v);
			p.setStartPos(pos);
			p.setEndPos(pos);
			p.setTotalUpdates(v.getTotalAttrChange());
			phaseCollector.addPhase(p);
			
		}
		return phaseCollector;
	}
}
