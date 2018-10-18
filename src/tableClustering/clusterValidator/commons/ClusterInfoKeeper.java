package tableClustering.clusterValidator.commons;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import data.dataPPL.pplSQLSchema.PPLTable;
import tableClustering.clusterExtractor.commons.Cluster;
import tableClustering.clusterValidator.clusterValidityMetrics.externalEvaluation.externalClusterMetrics.ClusterEntropyMetric;
import tableClustering.clusterValidator.clusterValidityMetrics.externalEvaluation.externalClusterMetrics.ClusterFMeasureMetric;
import tableClustering.clusterValidator.clusterValidityMetrics.externalEvaluation.externalClusterMetrics.ClusterPrecisionMetric;
import tableClustering.clusterValidator.clusterValidityMetrics.externalEvaluation.externalClusterMetrics.ClusterRecallMetric;
import tableClustering.clusterValidator.clusterValidityMetrics.externalEvaluation.externalClusterMetrics.ExternalClusterMetric;
import tableClustering.clusterValidator.clusterValidityMetrics.internalEvaluation.internalClusterMetrics.ClusterCohesionMetric;
import tableClustering.clusterValidator.clusterValidityMetrics.internalEvaluation.internalClusterMetrics.ClusterSeparationMetric;
import tableClustering.clusterValidator.clusterValidityMetrics.internalEvaluation.internalClusterMetrics.InternalClusterMetrics;


public class ClusterInfoKeeper {
	
	private Cluster cluster = new Cluster();
	private Centroid clusterCentroid=null;
	private Centroid overallCentroid=null;
	private Double clusterCohesion = null;
	private Double clusterSeparation = null;
	private Double clusterEntropy = null;
	private ArrayList<Double> precisions = new ArrayList<Double>();
	private ArrayList<Double> recalls = new ArrayList<Double>();
	private ArrayList<Double> fMeasures = new ArrayList<Double>();


	
	public ClusterInfoKeeper(Cluster cluster,Centroid overallCentroid){
		this.cluster=cluster;
		this.overallCentroid=overallCentroid;
		initialize();
	}
	
	
	private void initialize(){
		
		initializeCentroid();
		computeClusterCohesion();
		computeClusterSeparation();		
		
	}
	
	private void initializeCentroid(){
		
		TreeMap<String, PPLTable> tables=this.cluster.getTables();
		double x=0;
		double y=0;
		double z=0;
		for(Map.Entry<String,PPLTable> pplTab:tables.entrySet()){
			x = x +pplTab.getValue().getBirthVersionID();
			y = y+pplTab.getValue().getDeathVersionID();
			z= z+pplTab.getValue().getTotalChanges();
		}
		
		x= x/tables.size();
		y= y/tables.size();
		z= z/tables.size();
		
		this.clusterCentroid=new Centroid(x, y, z);
				
	}
	
	private void computeClusterCohesion(){
		
		InternalClusterMetrics cohesionMetricCalculator = new ClusterCohesionMetric(this);
		cohesionMetricCalculator.computeMetric();
		clusterCohesion=cohesionMetricCalculator.getResult();
		//System.out.println(clusterCohesion);
		
	}
	
	private void computeClusterSeparation(){
		
		InternalClusterMetrics separationMetricCalculator = new ClusterSeparationMetric(clusterCentroid,overallCentroid);
		separationMetricCalculator.computeMetric();
		clusterSeparation=(double)this.cluster.getTables().size()*separationMetricCalculator.getResult();
		//System.out.println(clusterSeparation+"\n");
		
	}
	
	public void computeClusterEntropy(ArrayList<ClassOfObjects> classesOfObjects,ArrayList<Cluster> clusters,int classIndex){
		
		ExternalClusterMetric entropyMetricCalculator = new ClusterEntropyMetric(classesOfObjects,clusters,classIndex);
		entropyMetricCalculator.compute();
		clusterEntropy = entropyMetricCalculator.getResult();
		//System.err.println("-------------->"+clusterEntropy);
		
	}
	
	public void computeClusterPrecision(ArrayList<ClassOfObjects> classesOfObjects){
		
		ExternalClusterMetric precisionMetricCalculator;
		for(int i=0; i<classesOfObjects.size(); i++){
			precisionMetricCalculator = new ClusterPrecisionMetric(this.cluster,classesOfObjects.get(i));
			precisionMetricCalculator.compute();
			precisions.add(precisionMetricCalculator.getResult());
		}
		
	}
	
	public void computeClusterRecall(ArrayList<ClassOfObjects> classesOfObjects){
		
		ExternalClusterMetric recallMetricCalculator;
		for(int i=0; i<classesOfObjects.size(); i++){
			recallMetricCalculator = new ClusterRecallMetric(this.cluster,classesOfObjects.get(i));
			recallMetricCalculator.compute();
			recalls.add(recallMetricCalculator.getResult());
		}
				
	}
	
	public void computeClusterFMeasure(){
		
		ExternalClusterMetric fMeasureMetricCalculator;
		for(int i=0; i<precisions.size(); i++){
			fMeasureMetricCalculator = new ClusterFMeasureMetric(precisions.get(i),recalls.get(i));
			fMeasureMetricCalculator.compute();
			fMeasures.add(fMeasureMetricCalculator.getResult());
		}
				
	}
	
	public Cluster getCluster(){
		return this.cluster;
	}
	
	public Centroid getCentroid(){
		return this.clusterCentroid;
	}
	
	public Double getClusterCohesion() {
		return clusterCohesion;
	}
	
	public Double getClusterSeparation() {
		return clusterSeparation;
	}
	
	public Double getClusterEntropy() {
		return clusterEntropy;
	}

	public ArrayList<Double> getPrecisions(){
		return precisions;
	}
	
	public ArrayList<Double> getRecalls(){
		return recalls;
	}
	
	public ArrayList<Double> getFmeasures(){
		return fMeasures;
	}
	
}
