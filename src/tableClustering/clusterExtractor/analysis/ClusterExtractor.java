package tableClustering.clusterExtractor.analysis;

import tableClustering.clusterExtractor.commons.ClusterCollector;
import data.dataKeeper.GlobalDataKeeper;

public interface ClusterExtractor {

	public ClusterCollector extractAtMostKClusters(GlobalDataKeeper dataKeeper,
			int numClusters,Double birthWeight,Double deathWeight, Double changeWeight );
	
}
