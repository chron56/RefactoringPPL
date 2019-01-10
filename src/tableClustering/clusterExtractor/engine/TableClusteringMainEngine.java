package tableClustering.clusterExtractor.engine;

import java.util.ArrayList;
import java.util.TreeMap;

import tableClustering.clusterExtractor.analysis.ClusterExtractor;
import tableClustering.clusterExtractor.analysis.ClusterExtractorFactory;
import tableClustering.clusterExtractor.commons.Cluster;
import tableClustering.clusterExtractor.commons.ClusterCollector;
import data.dataPPL.pplSQLSchema.PPLSchema;
import data.dataPPL.pplSQLSchema.PPLTable;

public class TableClusteringMainEngine implements ITableClustering{
	
	
		
	private ArrayList<ClusterCollector> allClusterCollectors;

	@Override
	public void extractClusters(TreeMap<String,PPLTable> tables,TreeMap<String, PPLSchema> pplSchemas, Integer numClusters,Double birthWeight, Double deathWeight,
			Double changeWeight){
		
		ClusterExtractorFactory clusterExtractorFactory = new ClusterExtractorFactory();
		ClusterExtractor clusterExtractor = clusterExtractorFactory.createClusterExtractor("AgglomerativeClusterExtractor");
		
		allClusterCollectors = new ArrayList<ClusterCollector>();
		
		ClusterCollector clusterCollector = new ClusterCollector();
		clusterCollector = clusterExtractor.extractAtMostKClusters(tables, pplSchemas,numClusters, birthWeight, deathWeight, changeWeight);
		clusterCollector.sortClustersByBirthDeath();
		
		
		allClusterCollectors.add(clusterCollector);

	}
	@Override
	public void print(){
		
		String toPrint="";
		
		for(int i=0; i<allClusterCollectors.size(); i++){
			ClusterCollector clusterCollector=allClusterCollectors.get(i);
			toPrint=toPrint+clusterCollector.toString();
			
		}
		
		System.out.println(toPrint);
	}
	@Override
	public ArrayList<ClusterCollector> getClusterCollectors(){
		return allClusterCollectors;
	}
	
	public ArrayList<Cluster> getClusters(){
		return allClusterCollectors.get(0).getClusters();
	}
}
