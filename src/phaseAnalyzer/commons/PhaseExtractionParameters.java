package phaseAnalyzer.commons;

import java.util.Arrays;
import java.util.List;

public class PhaseExtractionParameters {
	//All the variables of the project collected here
	//public static final [type] [NAME_IN_ALL_CAPS] = [value];
	//In the code to be used as 
	//PhaseExtractionParameters.[NAME_IN_ALL_CAPS]
	//public static final String DATASET = "typo3"; //String dataSetNames[] ={"atlas", "biosql", "coppermine", "ensembl", "mwiki", "opencart", "phpBB", "typo3"};
	public static final List<String> DATASET_AR= Arrays.asList("atlas", "biosql", "coppermine", "ensembl", "mwiki", "opencart", "phpBB", "typo3");
	public static final int DEFAULT_NUM_PHASES = 10;	//10
	public static final double CHANGE_WEIGHT = 0.5;		//0.5
	public static final double TIME_WEIGHT = 0.5;		//0.5
	public static final boolean PREPROCESSING_TIME_ON = true;		//false
	public static final boolean PREPROCESSING_CHANGE_ON = true;		//false
	public static final List<Double> CHANGE_WEIGHT_AR = Arrays.asList(0.0,0.5,1.0);
	public static final List<Double> TIME_WEIGHT_AR= Arrays.asList(1.0,0.5,0.0);
	public static final List<Boolean> PREPROCESSING_TIME_ON_AR= Arrays.asList(false,false,true,true);
	public static final List<Boolean> PREPROCESSING_CHANGE_ON_AR= Arrays.asList(false,true,false,true);

}
