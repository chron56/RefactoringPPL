package tableClustering.clusterValidator.clusterValidityMetrics.externalEvaluation.externalClusterMetrics;

public interface ExternalClusterMetric {

	public void compute();
	public Double getResult();
	
}
