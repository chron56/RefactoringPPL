package data.dataProccessing;

import gr.uoi.cs.daintiness.hecate.sql.Attribute;
import gr.uoi.cs.daintiness.hecate.transitions.Deletion;
import gr.uoi.cs.daintiness.hecate.transitions.Insersion;
import gr.uoi.cs.daintiness.hecate.transitions.Transition;
import gr.uoi.cs.daintiness.hecate.transitions.TransitionList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import data.dataPPL.pplTransition.AtomicChange;

public class AtomicChangeConstruction {
	
	private static ArrayList<AtomicChange> atomicChanges = null;
	private static ArrayList<TransitionList> allTransitions = new ArrayList<TransitionList>();

	public AtomicChangeConstruction(ArrayList<TransitionList> tmpAllTransitions){
		atomicChanges = new ArrayList<AtomicChange>();
		allTransitions=tmpAllTransitions;
		
	}
	
	public void makeAtomicChanges(){
		
		for(int i=0; i<allTransitions.size(); i++){
			
			TransitionList currentTransitionList=allTransitions.get(i);
			
			String oldVersion = currentTransitionList.getOldVersion();
			
			String newVersion = currentTransitionList.getNewVersion();
			
			ArrayList<Transition> currentTransitions = currentTransitionList.getList();
			
			for(int j=0; j<currentTransitions.size(); j++){
				
				
				Collection<Attribute> tmpAffectedAttributes = currentTransitions.get(j).getAffAttributes();
				
				Iterator<Attribute> lala = tmpAffectedAttributes.iterator();

				while(lala.hasNext()){					
					
					Attribute tmpHecAttribute = (Attribute) lala.next();
					String tmpType;
					
					if(currentTransitions.get(j) instanceof Insersion){
						
						if(currentTransitions.get(j).getType().equals("UpdateTable")){
							tmpType="Addition";
						}
						else{
							tmpType="Addition of New Table";
						}
					}
					else if(currentTransitions.get(j) instanceof Deletion){
						

						if(currentTransitions.get(j).getType().equals("UpdateTable")){
							tmpType="Deletion";
						}
						else{
							tmpType="Deletion of whole Table";
						}
						
					}
					else{
						
						if(currentTransitions.get(j).getType().equals("TypeChange")){
							tmpType="Type Change";
						}
						else{
							tmpType="Key Change";
						}
					}
					
					AtomicChange tmpAtomicChange= new AtomicChange(currentTransitions.get(j).getAffTable().getName(),tmpHecAttribute.getName(),tmpType,oldVersion,newVersion,i);
					
					atomicChanges.add(tmpAtomicChange);
				
				}
				
			}
			
			
		}
		
		
	}
	
	public ArrayList<AtomicChange> getAtomicChanges(){
		
		return atomicChanges;
		
	}

}
