package tableClustering.clusterValidator.clusterValidityMetrics.externalEvaluation.externalClusterMetrics;

public class ClusterFMeasureMetric implements ExternalClusterMetric {

	private Double precision = new Double(0);
	private Double recall = new Double(0);
	private Double fMeasure = new Double(0);

	public ClusterFMeasureMetric(Double precision, Double recall) {
		this.precision = precision;
		this.recall = recall;
	}
	
	@Override
	public void compute() {
		// F(i,j) = (2 x precision(i,j) x recall(i,j))/( precision(i,j) + recall(i,j))
		
		Double numerator = new Double(2*precision*recall);
		Double denominator = new Double(precision+recall);
		
		if(numerator!=0 && denominator!=0)
			fMeasure = numerator/denominator;

	}

	@Override
	public Double getResult() {
		return fMeasure;
	}

}
