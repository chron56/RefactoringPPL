package tableClustering.clusterValidator.clusterValidityMetrics.externalEvaluation.externalTotalMetrics;

import java.util.ArrayList;

import tableClustering.clusterValidator.commons.ClusterInfoKeeper;

public class TotalEntropyMetric implements ExternalTotalMetrics {

	private ArrayList<ClusterInfoKeeper> clusterInfoKeepers = new ArrayList<ClusterInfoKeeper>();
	private Double totalEntropy=new Double(0);
	private int wholeDatasetSize;
	
	public TotalEntropyMetric(ArrayList<ClusterInfoKeeper> clusterInfoKeepers,int wholeDatasetSize) {
	
		this.clusterInfoKeepers = clusterInfoKeepers;
		this.wholeDatasetSize = wholeDatasetSize;
	}
	
	@Override
	public void compute() {
		
		for(int i=0; i<clusterInfoKeepers.size(); i++){
			int currClusterSize = clusterInfoKeepers.get(i).getCluster().getNamesOfTables().size();
			Double currClusterWeight = (double)currClusterSize/wholeDatasetSize;
			Double valueToAdd = currClusterWeight*clusterInfoKeepers.get(i).getClusterEntropy();
			totalEntropy = totalEntropy+ valueToAdd;
		}
		
		
	}

	@Override
	public Double getResult() {
 		return totalEntropy;
	}

}
