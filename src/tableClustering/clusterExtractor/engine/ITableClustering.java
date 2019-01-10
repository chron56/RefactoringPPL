package tableClustering.clusterExtractor.engine;

import java.util.ArrayList;
import java.util.TreeMap;
import data.dataPPL.pplSQLSchema.PPLSchema;
import data.dataPPL.pplSQLSchema.PPLTable;
import tableClustering.clusterExtractor.commons.Cluster;
import tableClustering.clusterExtractor.commons.ClusterCollector;

public interface ITableClustering {
	void extractClusters(TreeMap<String,PPLTable> tables, TreeMap<String, PPLSchema> pplSchemas, Integer numClusters,Double birthWeight, Double deathWeight,
			Double changeWeight);
		ArrayList<ClusterCollector> getClusterCollectors();
		ArrayList<Cluster> getClusters();
		void print();
}
