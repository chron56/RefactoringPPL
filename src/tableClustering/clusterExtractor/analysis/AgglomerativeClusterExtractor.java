package tableClustering.clusterExtractor.analysis;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import tableClustering.clusterExtractor.commons.Cluster;
import tableClustering.clusterExtractor.commons.ClusterCollector;
import data.dataKeeper.GlobalDataKeeper;
import data.dataPPL.pplSQLSchema.PPLTable;


public class AgglomerativeClusterExtractor implements ClusterExtractor{

	

	@Override
	public ClusterCollector extractAtMostKClusters(GlobalDataKeeper dataKeeper,
			int numClusters, Double birthWeight, Double deathWeight, Double changeWeight) {
		
		ClusterCollector initSolution = new ClusterCollector();
		this.init(dataKeeper, initSolution);
		
		ClusterCollector currentSolution = new ClusterCollector();
		currentSolution = this.newClusterCollector(initSolution, birthWeight, deathWeight, changeWeight,dataKeeper.getAllPPLSchemas().size()-1);
		while (currentSolution.getClusters().size() > numClusters){
			currentSolution = this.newClusterCollector(currentSolution, birthWeight, deathWeight, changeWeight,dataKeeper.getAllPPLSchemas().size()-1);
		}
		return currentSolution;
		
	}
	
	public ClusterCollector newClusterCollector(ClusterCollector prevCollector,Double birthWeight, Double deathWeight ,Double changeWeight,int dbDuration){
		
		ClusterCollector newCollector = new ClusterCollector();
		ArrayList<Cluster> newClusters = new ArrayList<Cluster>();
		ArrayList<Cluster> oldClusters = prevCollector.getClusters();

		int oldSize = oldClusters.size();
		if (oldSize == 0){
			
			System.exit(-10);
		}

		//compute the distances for all the bloody clusters
		//TODO add it at cluster collector to move on !$#@$#%$^$%&%&
		double distances[][] = new double[oldSize][oldSize];
		
	    for(int oldI=0; oldI<oldClusters.size(); oldI++){
	    	Cluster currentCluster=oldClusters.get(oldI);
		    for(int oldI1=0; oldI1<oldClusters.size(); oldI1++){
	    		Cluster clusterToCompare=oldClusters.get(oldI1);
	    		distances[oldI][oldI1] = currentCluster.distance(clusterToCompare,birthWeight,deathWeight,changeWeight,dbDuration);
	    	}
	    }
	    
	    for(int i=0; i<distances.length; i++){
	    	for(int j=0; j<distances[0].length; j++){
		    }
	    }
	    
		//find the two most similar clusters in the old collection
	    
	    int posI=-1; 
	    double minDist = Double.MAX_VALUE;
	    int posJ=-1;
	    for(int i=0; i<oldSize; i++){
	    	 for(int j=0; j<oldSize; j++){
	    		if(i!=j){
			    	if(distances[i][j]<minDist){
			    		posI = i;
			    		posJ = j;
			    		minDist = distances[i][j];
	
			    	}
	    		}
	    	 }
	    }
	    

	    //merge them in a new cluster.
		Cluster toMerge = oldClusters.get(posI);
		Cluster newCluster = toMerge.mergeWithNextCluster(oldClusters.get(posJ));
		for(int i=0; i < oldSize; i++){
			if(i!=posI && i!=posJ){
				Cluster c = oldClusters.get(i);
				newClusters.add(c);
			}
		}
		newClusters.add(newCluster);
		
		newCollector.setClusters(newClusters);

		return newCollector;
	}
	
	
	public ClusterCollector init(GlobalDataKeeper dataKeeper, ClusterCollector clusterCollector){
		
		TreeMap<String, PPLTable> tables=dataKeeper.getAllPPLTables();

		
		for (Map.Entry<String,PPLTable> pplTable : tables.entrySet()) {
			Cluster c = new Cluster(pplTable.getValue().getBirthVersionID(),pplTable.getValue().getDeath(),pplTable.getValue().getDeathVersionID(),pplTable.getValue().getDeath(),pplTable.getValue().getTotalChanges());
			c.addTable(pplTable.getValue());
			clusterCollector.addCluster(c);
			
		}
		return clusterCollector;
	}

}
