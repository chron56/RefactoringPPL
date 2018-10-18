package tableClustering.clusterValidator.clusterValidityMetrics.externalEvaluation.externalClusterMetrics;

import java.util.ArrayList;

import tableClustering.clusterExtractor.commons.Cluster;
import tableClustering.clusterValidator.commons.ClassOfObjects;

public class ClusterRecallMetric implements ExternalClusterMetric {

	private Cluster currentCluster = new Cluster();
	private ClassOfObjects classOfObjects=null;
	private Double recall = new Double(0);

	
	public ClusterRecallMetric(Cluster currentCluster,ClassOfObjects classOfObjects) {
		this.currentCluster = currentCluster;
		this.classOfObjects = classOfObjects;
		
	}
	
	@Override
	public void compute() {
		
		//recall(i,j) = pij = mij/mj
		
		Double mj = new Double(0);
		Double mij = new Double(0);
		
		mj = (double)classOfObjects.getObjects().size();
		mij=0.0;
		
		ArrayList<String> tablesToCompare = currentCluster.getNamesOfTables();
		ArrayList<String> objects = classOfObjects.getObjects();
		
		
		for(int j=0; j<objects.size(); j++){
			if(tablesToCompare.contains(objects.get(j))){
				mij++;
			}
		}
		
		
		recall=mij/mj;
		
	}

	@Override
	public Double getResult() {
		// TODO Auto-generated method stub
		return recall;
	}

}
