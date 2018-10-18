package tableClustering.clusterValidator.clusterValidityMetrics.externalEvaluation.externalClusterMetrics;

import java.util.ArrayList;

import tableClustering.clusterExtractor.commons.Cluster;
import tableClustering.clusterValidator.commons.ClassOfObjects;

public class ClusterEntropyMetric implements ExternalClusterMetric {

	private ArrayList<ClassOfObjects> classesOfObjects = new ArrayList<ClassOfObjects>();
	//private Cluster currentCluster = new Cluster();
	private Double clusterEntropy = new Double(0);
	private ArrayList<Cluster> clusters  = new ArrayList<Cluster>();
	private int classIndexOfThisCluster;
	
	public ClusterEntropyMetric(ArrayList<ClassOfObjects> classesOfObjects, 
								ArrayList<Cluster> clusters,int classIndexOfThisCluster) {
		this.classesOfObjects = classesOfObjects;
		//this.currentCluster = currentCluster;
		this.clusters = clusters;
		this.classIndexOfThisCluster = classIndexOfThisCluster;
	}
	
	@Override
	public void compute() {
		/*
		 * for current cluster (j)!!!!
		 * 
		 * pij = mij/mi
		 * 
		 * mi:  the number of objects in cluster i
		 * mij: the number of objects of class j in cluster i
		 * 
		 * the entropy of each cluster i is calculated using 
		 * the standard formula, 
		 * 
		 * ei = -Sum(j=1 ews L)pij*log2(pij)
		 * where L is the number of classes
		 * 
		 */
		Double mi = new Double(0);
		Double mij = new Double(0);
		Double pij = new Double(0);
		Double ei= new Double(0);
		
		for(int i=0 ; i<this.clusters.size(); i++){
			
			mi = (double)this.clusters.get(i).getTables().size();
			mij=0.0;
			pij=0.0;
			
			ArrayList<String> tablesOfCompared = this.clusters.get(i).getNamesOfTables();
			ClassOfObjects classOfThisCluster = classesOfObjects.get(classIndexOfThisCluster);
			ArrayList<String> objects = classOfThisCluster.getObjects();
			
			
			for(int j=0; j<objects.size(); j++){
				if(tablesOfCompared.contains(objects.get(j))){
					mij++;
				}
			}
			
			pij=mij/mi;
			
			if(mi!=0 && mij!=0){
				
				ei = ei + (pij * (Math.log(pij)/Math.log(2.0d)));

			}
			
		}
		
		clusterEntropy = -ei;
	}

	@Override
	public Double getResult() {
		// TODO Auto-generated method stub
		return clusterEntropy;
	}

	
}
