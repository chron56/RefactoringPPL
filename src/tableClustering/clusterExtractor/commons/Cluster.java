package tableClustering.clusterExtractor.commons;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import data.dataPPL.pplSQLSchema.PPLTable;

public class Cluster {

	private int birth;
	private String birthVersion;
	private int death;
	private String deathVersion;
	private int totalChanges=0;
	private TreeMap<String,PPLTable> tables=null;
	
	public Cluster(){
		
		tables=new TreeMap<String, PPLTable>();
	}
	
	public Cluster(int birth,String birthVersion, int death,String deathVersion, int totalChanges){
		
		this.birth=birth;
		this.birthVersion=birthVersion;
		this.death=death;
		this.deathVersion=deathVersion;
		this.totalChanges=totalChanges;
		tables=new TreeMap<String, PPLTable>();

		
	}
	
	public TreeMap<String,PPLTable> getTables(){
		return tables;
	}
	
	public ArrayList<String> getNamesOfTables(){
		ArrayList<String> tablesNames = new ArrayList<String>();
		for(Map.Entry<String, PPLTable> pplTb:tables.entrySet()){
			tablesNames.add(pplTb.getKey());
		}
		return tablesNames;
	}
	
	public void addTable(PPLTable table){
		this.tables.put(table.getName(), table);
	}
	
	public int getBirth(){
		return this.birth;
	}
	
	public int getDeath(){
		return this.death;
	}
	
	public String getBirthSqlFile(){
		return this.birthVersion;
	}
	
	public String getDeathSqlFile(){
		return this.deathVersion;
	}
	
	public int getTotalChanges(){
		return totalChanges;
	}
	
	public double distance(Cluster anotherCluster,Double birthWeight, Double deathWeight ,Double changeWeight,int dbDuration){
		
		//double changeDistance = Math.abs(this.totalChanges - anotherCluster.totalChanges);
		double normalizedChangeDistance= Math.abs((this.totalChanges - anotherCluster.totalChanges)/((double)(this.totalChanges + anotherCluster.totalChanges)));
		//System.out.println("C:"+changeDistance+"-"+normalizedChangeDistance);
		
		//double birthDistance = Math.abs(this.birth-anotherCluster.birth);
		double normalizedBirthDistance = Math.abs((this.birth-anotherCluster.birth)/(double)dbDuration);
		//System.out.println("B:"+birthDistance+"-"+normalizedBirthDistance);

		//double deathDistance = Math.abs(this.death-anotherCluster.death);
		double normalizedDeathDistance = Math.abs((this.death-anotherCluster.death)/(double)dbDuration);
		//System.out.println("D:"+deathDistance+"-"+normalizedDeathDistance);

		//double totalDistance = changeWeight * changeDistance + birthWeight * birthDistance + deathWeight * deathDistance;
		double normalizedTotalDistance = changeWeight * normalizedChangeDistance + birthWeight * normalizedBirthDistance + deathWeight * normalizedDeathDistance;
		//System.out.println("TD:"+totalDistance+"-"+normalizedTotalDistance);
		
		return normalizedTotalDistance;
		
	}
	
	public Cluster mergeWithNextCluster(Cluster nextCluster){
		
		Cluster newCluster = new Cluster();
	
		int minBirth;
		String minBirthVersion="";
		if(this.birth<=nextCluster.birth){
			minBirth=this.birth;
			minBirthVersion=this.birthVersion;
		}
		else {
			minBirth=nextCluster.birth;
			minBirthVersion=nextCluster.birthVersion;
		}
		
		int maxDeath;
		String maxDeathVersion="";
		if(this.death>=nextCluster.death){
			maxDeath=this.death;
			maxDeathVersion=this.deathVersion;
		}
		else {
			maxDeath=nextCluster.death;
			maxDeathVersion=nextCluster.deathVersion;

		}
		
		newCluster.birth = minBirth;
		newCluster.birthVersion = minBirthVersion;
		newCluster.death =maxDeath;
		newCluster.deathVersion = maxDeathVersion;
		
		newCluster.totalChanges = this.totalChanges + nextCluster.totalChanges;
		
		for (Map.Entry<String,PPLTable> tab : tables.entrySet()) {
			
			newCluster.addTable(tab.getValue());
			
		}
		
		for (Map.Entry<String,PPLTable> tabNext : nextCluster.getTables().entrySet()) {
			
			newCluster.addTable(tabNext.getValue());
			
		}
		
		
		return newCluster;
	}
	
	public String toString(){
		

		String toReturn="Cluster";
		
		
		toReturn=toReturn+"\t"+this.birth+"\t"+this.death+"\t"+this.totalChanges+"\n";
		
		
		for(Map.Entry<String, PPLTable> t: this.tables.entrySet()){
			toReturn=toReturn+t.getKey()+"\t"+t.getValue().getBirthVersionID()+"\t"+t.getValue().getDeathVersionID()+"\t"+t.getValue().getTotalChanges()+"\n";
		}
		
		
		return toReturn;
		
	}
	
	
}
