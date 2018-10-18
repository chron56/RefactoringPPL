/**
 * This class represents the sequence of transitions that we receive as input.
 */
package phaseAnalyzer.commons;

import java.util.ArrayList;

public class TransitionHistory {

	public TransitionHistory() {
		this.values = new ArrayList<TransitionStats>();
	}
	public TransitionHistory(ArrayList<TransitionStats> values) {
		this.values = values;
	}

	
	public int getTotalUpdates(){
		return this.totalUpdates;
	}

	public void addValue(TransitionStats v){
		values.add(v);
	}
	
	public void consoleVerticalReport(){
		for (TransitionStats v: values){
			System.out.println(v.toStringShort());
		}
		System.out.println();
	}

	public ArrayList<TransitionStats> getValues() {
		return values;
	}
		
	public void setTotalUpdates(int totalUpdates){
		this.totalUpdates=totalUpdates;
	}

	public void setTotalTime(){
		this.totalTime=(this.values.get(this.values.size()-1).getTime()-this.values.get(0).getTime())/86400;
		

	}
	
	public double getTotalTime(){
		return this.totalTime;
	}
	
	private ArrayList<TransitionStats> values;
	private int totalUpdates;
	private double totalTime;
	
	
}
