package data.dataPPL.pplSQLSchema;


import gr.uoi.cs.daintiness.hecate.sql.Table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import data.dataPPL.pplTransition.AtomicChange;
import data.dataPPL.pplTransition.TableChange;

public class PPLTable {

	private int age;
	private int totalChanges;
	private int currentChanges;
	private HashMap<String,Integer> coChanges;
	private HashMap<String,Integer> sequenceCoChanges;
	private HashMap<String,Integer> windowCoChanges;
	private ArrayList<Integer> changesForChart = new ArrayList<Integer>();
	private TableChange tableChanges;
	private Table hecTable;
	private TreeMap<String, PPLAttribute> attrs;
	private String name="";
	private String birth=null;
	private int birthVersionID;
	private String death=null;
	private int deathVersionID;
	private boolean active=false;

	
	public PPLTable(String tmpName,Table tmpHecTable){
		
		hecTable=tmpHecTable;
		name=tmpName;
		this.attrs = new TreeMap<String, PPLAttribute>();

	}
	
	public PPLTable(){
			
	}
	
	public void setBirth(String birth){
		this.birth=birth;
	}
	
	public void setBirthVersionID(int birthID){
		birthVersionID=birthID;
	}
	
	public void setDeath(String death){
		this.death=death;
	}
	
	public void setDeathVersionID(int deathID){
		deathVersionID=deathID;
	}
	
	public void setActive(){
		this.active=!this.active;
	}
	
	public boolean getActive(){
		return this.active;
	}
	
	public String getBirth(){
		return this.birth;
	}
	
	public int getBirthVersionID(){
		return birthVersionID;
	}
	
	public int getDeathVersionID(){
		return deathVersionID;
	}
	
	public String getDeath(){
		return this.death;
	}
	
	public void addAttribute(PPLAttribute attr) {
		
		this.attrs.put(attr.getName(), attr);
		
	}
	
	public TreeMap<String, PPLAttribute> getAttrs() {
		return this.attrs;
	}
	
	public PPLAttribute getAttrAt(int i) {
		int c = 0;
		if (i >= 0 && i < attrs.size()){
			for (Map.Entry<String, PPLAttribute> t : attrs.entrySet()) {
				if (c == i) {
					return t.getValue();
				}
				c++;
			}
		}
		return null;
	}
	
	public int getAge(){
		
		return age;
		
	}
	
	public String getName(){
		
		return name;
	}
	
	public int getTotalChanges(){
		return totalChanges;
	}
	
	public int getCurrentChanges(){
		return currentChanges;
	}
	
	public HashMap<String,Integer> getCoChanges(){
		return coChanges;
	}
	
	public HashMap<String,Integer> getSequenceCoChanges(){
		return sequenceCoChanges;
	}
	
	public HashMap<String,Integer> getWindowCoChanges(){
		return windowCoChanges;
	}
	
	public ArrayList<Integer> getChangesForChart(){
		return changesForChart;
	}


	public TableChange getTableChanges(){
		return tableChanges;
	}

	public void setTableChanges(TableChange tmpTableChanges){
		tableChanges=tmpTableChanges;
	}
	
	public void setAge(int tmpAge){
		age=tmpAge;
	}
	
	
	public void setTotalChanges(){
		TreeMap<Integer,ArrayList<AtomicChange>> tc=tableChanges.getTableAtomicChanges();
		for(Map.Entry<Integer, ArrayList<AtomicChange>> tcr:tc.entrySet()){
			totalChanges=totalChanges+tcr.getValue().size();
		}
	}
	
	public void setChangesForChart(ArrayList<Integer> tmpChangesForChart){
		changesForChart=tmpChangesForChart;
	}
	
	public void setCurrentChanges(int tmpCurrentChanges){
		currentChanges=tmpCurrentChanges;
	}
	
	public void setCoChanges(HashMap<String,Integer> tmpCoChanges){
		coChanges=tmpCoChanges;
	}
	
	public void setSequenceCoChanges(HashMap<String,Integer> tmpSequenceCoChanges){
		sequenceCoChanges=tmpSequenceCoChanges;
	}
	
	public void setWindowCoChanges(HashMap<String,Integer> tmpWindowCoChanges){
		windowCoChanges=tmpWindowCoChanges;
	}
	
	
	public Table getHecTable(){
		
		return hecTable;
		
	}
	
	public int getSize() {
		return attrs.size();
	}
	
	public int getTotalChangesForOnePhase(int startPos,int LastPos){
		
		int counter=0;
		for(int i=startPos;i<=LastPos;i++){
			if(tableChanges.getTableAtChForOneTransition(i)!=null){
				counter=counter+tableChanges.getTableAtChForOneTransition(i).size();
			}
		}
		return(counter);
	}
	
	public int getNumberOfAdditionsForOneTr(Integer transition){
		int additions=tableChanges.getNumberOfAdditionsForOneTr(transition);
		return additions;
	}
	
	public int getNumberOfDeletionsForOneTr(Integer transition){
		int deletions=tableChanges.getNumberOfDeletionsForOneTr(transition);
		return deletions;
	}
	
	public int getNumberOfUpdatesForOneTr(Integer transition){
		int updates=tableChanges.getNumberOfUpdatesForOneTr(transition);
		return updates;
	}
	
	
}
