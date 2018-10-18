package tableClustering.clusterValidator.clusterValidityMetrics.externalEvaluation.externalClusterMetrics;

import java.util.ArrayList;

import tableClustering.clusterExtractor.commons.Cluster;
import tableClustering.clusterValidator.commons.ClassOfObjects;

public class ClusterPrecisionMetric implements ExternalClusterMetric {
	
	private Cluster currentCluster = new Cluster();
	private ClassOfObjects classOfObjects=null;
	private Double precision = new Double(0);

	
	public ClusterPrecisionMetric(Cluster currentCluster,ClassOfObjects classOfObjects) {
		this.currentCluster = currentCluster;
		this.classOfObjects = classOfObjects;
		
	}
	
	@Override
	public void compute() {

		//precision(i,j) = pij = mij/mi
		
		Double mi = new Double(0);
		Double mij = new Double(0);
		
		mi = (double)currentCluster.getTables().size();
		mij=0.0;
		
		ArrayList<String> tablesToCompare = currentCluster.getNamesOfTables();
		ArrayList<String> objects = classOfObjects.getObjects();
		
		
		for(int j=0; j<objects.size(); j++){
			if(tablesToCompare.contains(objects.get(j))){
				mij++;
			}
		}
		
		
		precision=mij/mi;
		
		
	}

	@Override
	public Double getResult() {
		// TODO Auto-generated method stub
		return precision;
	}

}
