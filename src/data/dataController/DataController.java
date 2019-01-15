package data.dataController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;
import javax.swing.JTree;
import phaseAnalyzer.commons.Phase;
import phaseAnalyzer.commons.PhaseCollector;
import phaseAnalyzer.engine.IPhase;
import phaseAnalyzer.engine.PhaseAnalyzerMainEngine;
import tableClustering.clusterExtractor.commons.Cluster;
import tableClustering.clusterExtractor.engine.ITableClustering;
import tableClustering.clusterExtractor.engine.TableClusteringMainEngine;
import data.dataPPL.pplSQLSchema.PPLSchema;
import data.dataPPL.pplSQLSchema.PPLTable;
import data.dataPPL.pplTransition.AtomicChange;
import data.dataPPL.pplTransition.PPLTransition;
import data.dataPPL.pplTransition.TableChange;
import data.dataProccessing.DataImporter;
import data.dataSorters.PldRowSorter;
import data.dataTables.TableConstructionAllSquaresIncluded;
import data.dataTables.TableConstructionClusterTablesPhasesZoomA;
import data.dataTables.TableConstructionIDU;
import data.dataTables.TableConstructionPhases;
import data.dataTables.TableConstructionWithClusters;
import data.dataTables.TableConstructionZoomArea;
import data.dataTrees.TreeConstruction;
import data.dataTrees.TreeConstructionGeneral;
import data.dataTrees.TreeConstructionPhases;
import data.dataTrees.TreeConstructionPhasesWithClusters;

public class DataController {

	private TreeMap<String,PPLSchema> allPPLSchemas = null;
	private TreeMap<String,PPLTable> allTables = null;
	private ArrayList<AtomicChange> atomicChanges = null;
	private TreeMap<String,TableChange> tableChanges = null;
	private TreeMap<String,TableChange> tableChangesForTwo = null;
	private TreeMap<Integer,PPLTransition> allPPLTransitions = null;
	private String 	projectDataFolder=null;
	private String filename=null;
	private String transitionsFile="";
	private TableConstructionWithClusters clustersTable;
	private TableConstructionZoomArea zoomAreaTable;
	private TableConstructionAllSquaresIncluded allSquaresTable;
	private TableConstructionClusterTablesPhasesZoomA ClusterTablesPhasesZoomAtable;
	private TableConstructionPhases phasesTable;
	private TableConstructionIDU IDUTable;
	ITableClustering mainEngine;
	IPhase mainEnginePhase;
	public DataController() {
		
	}
	
	public DataController(String fl,String transitionsFile){
		allPPLSchemas = new TreeMap<String,PPLSchema>();
		allTables = new  TreeMap<String,PPLTable>();
		atomicChanges = new ArrayList<AtomicChange>();
		tableChanges = new TreeMap<String,TableChange>();
		tableChangesForTwo = new TreeMap<String,TableChange>();
		allPPLTransitions = new TreeMap<Integer,PPLTransition>();
		filename=fl;
		this.transitionsFile=transitionsFile;
		setData();
	}
	
	
	public void setData(){
		
		DataImporter importer = new DataImporter(filename,transitionsFile);
		try {
			importer.loadData();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		setAllPPLSchemas(importer.getAllPPLSchemas());
		setAllPPLTables(importer.getAllPPLTables());
		setAllPPLTransitions(importer.getAllPPLTransitions());
		setAllTableChanges(importer.getAllTableChanges());
		setAtomicChanges(importer.getAtomicChanges());
		setDataFolder(importer.getDataFolder());
		

		
	}
	
	
	public void  setPhaseCollectors(int numPhases,String inputCsv,String outputAssessment1,String outputAssessment2,Float tmpTimeWeight, Float tmpChangeWeight,
			Boolean tmpPreProcessingTime,Boolean tmpPreProcessingChange) {
		mainEnginePhase = new PhaseAnalyzerMainEngine();
		mainEnginePhase.setPhaseCollectors(getAllPPLTransitions(),numPhases,inputCsv,outputAssessment1,outputAssessment2,tmpTimeWeight,tmpChangeWeight,
	tmpPreProcessingTime,tmpPreProcessingChange);
	}
	private void setAllPPLSchemas(TreeMap<String,PPLSchema> tmpAllPPLSchemas){
		
		allPPLSchemas=tmpAllPPLSchemas;
		
	}
	
	private void setAllPPLTables(TreeMap<String,PPLTable> tmpAllTables){
		 allTables=tmpAllTables;

		
	}
	
	private void setAtomicChanges(ArrayList<AtomicChange> tmpAtomicChanges){
		
		 atomicChanges=tmpAtomicChanges;
		
	}
	
	private void setAllTableChanges(TreeMap<String,TableChange> tmpTableChanges){
		
		 tableChanges=tmpTableChanges;
		
	}
	
	
	private void setAllPPLTransitions(TreeMap<Integer,PPLTransition> tmpAllPPLTransitions){
		
		 allPPLTransitions=tmpAllPPLTransitions;
		
	}
	
	private void setDataFolder(String tmpProjectDataFolder){
		 projectDataFolder=tmpProjectDataFolder;
	}
	
	public TreeMap<String,PPLSchema> getAllPPLSchemas(){
		
		return allPPLSchemas;
		
	}
	
	public TreeMap<String,PPLTable> getAllPPLTables(){
		
		return allTables;
		
	}
	
	public ArrayList<AtomicChange> getAtomicChanges(){
		
		return atomicChanges;
		
	}
	
	public TreeMap<String,TableChange> getAllTableChanges(){
		
		return tableChanges;
		
	}
	
	public TreeMap<String,TableChange> getTmpTableChanges(){
		
		return tableChangesForTwo;
		
	}
	
	public TreeMap<Integer,PPLTransition> getAllPPLTransitions(){
		
		return allPPLTransitions;
		
	}
	
	public String getDataFolder(){
		return projectDataFolder;
	}
	

	public ArrayList<PhaseCollector> getPhaseCollectors(){
		return mainEnginePhase.getPhaseCollectors();
	}
	
	//CHANGES
	public JTree constructTree(String type) {
		TreeConstruction tree = null;
		if(type.equals("PhasesWithClusters")) {
			tree = new TreeConstructionPhasesWithClusters(getClusters());
		}else if(type.equals("Phases")) {
			tree = new TreeConstructionPhases(getPhases(), getAllPPLSchemas());
		}else if(type.equals("General")) {
			tree = new TreeConstructionGeneral(getAllPPLSchemas());
		}
		JTree treeToConstruct = tree.constructTree();
		return treeToConstruct;
	}

	public void constructTableWithClusters() {
		clustersTable = new TableConstructionWithClusters(getPhases(),getClusters());
	}

	public String[][] getTableRows(String type) {
		
		String[][] rowsP = null;
		if(type.equals("Clusters")) {
			rowsP=clustersTable.constructRows();
		}else if(type.equals("ZoomArea")) {
			rowsP=zoomAreaTable.constructRows();
		}else if(type.equals("AllSquares")) {
			rowsP=allSquaresTable.constructRows();
		}else if(type.equals("PhasesZoomA")) {
			rowsP=ClusterTablesPhasesZoomAtable.constructRows();
		}else if(type.equals("Phases")) {
			rowsP=phasesTable.constructRows();
		}else if(type.equals("IDU")) {
			rowsP=IDUTable.constructRows();
		}
		return rowsP;
	}
	
	
	public String[] getTableColumns(String type) {
		
		String[] columnsP = null;
		if(type.equals("Clusters")) {
			columnsP=clustersTable.constructColumns();
		}else if(type.equals("ZoomArea")) {
			columnsP=zoomAreaTable.constructColumns();
		}else if(type.equals("AllSquares")) {
			columnsP=allSquaresTable.constructColumns();
		}else if(type.equals("PhasesZoomA")) {
			columnsP=ClusterTablesPhasesZoomAtable.constructColumns();
		}else if(type.equals("Phases")) {
			columnsP=phasesTable.constructColumns();
		}else if(type.equals("IDU")) {
			columnsP=IDUTable.constructColumns();
		}
		return columnsP;
	}
	
	public Integer[] getSegmentSize(String type) {
		
		Integer[] segmentSize = null;
		if(type.equals("Clusters")) {
			segmentSize=clustersTable.getSegmentSize();
		}else if(type.equals("ZoomArea")) {
			segmentSize=zoomAreaTable.getSegmentSize();
		}else if(type.equals("AllSquares")) {
			segmentSize=allSquaresTable.getSegmentSize();
		}else if(type.equals("PhasesZoomA")) {
			segmentSize=ClusterTablesPhasesZoomAtable.getSegmentSize();
		}else if(type.equals("Phases")) {
			segmentSize=phasesTable.getSegmentSize();
		}else if(type.equals("IDU")) {
			segmentSize=IDUTable.getSegmentSize();
		}
		return segmentSize;
	}
	
	public void constructZoomAreaTable( ArrayList<String> selectedTables , int selectedColumn) {
		zoomAreaTable = new TableConstructionZoomArea(selectedTables,selectedColumn);
		zoomAreaTable.fillSelectedPPLTransitions(getAllPPLTransitions(),getPhases().get(selectedColumn-1).getPhasePPLTransitions());
		zoomAreaTable.fillSelectedPPLSchemas(getAllPPLSchemas());
		zoomAreaTable.fillSelectedTables(getAllPPLTables());
	}
	
	public void constructTableWithAllSquares() {
		allSquaresTable = new TableConstructionAllSquaresIncluded(getAllPPLSchemas(),getAllPPLTransitions());
	}

	
	public void constructClusterTablesPhasesZoomA(ArrayList<String> tablesOfCluster ) {
		ClusterTablesPhasesZoomAtable = new TableConstructionClusterTablesPhasesZoomA(getAllPPLSchemas(),getPhases(),tablesOfCluster);
	}

	
	public void constructPhasesTable() {
		phasesTable = new TableConstructionPhases(getAllPPLSchemas(),getPhases());
	}

	
	public void constructTableConstructionIDU() {
		IDUTable = new TableConstructionIDU(getAllPPLSchemas(),getAllPPLTransitions());
	}
	
	public void extractClusters(int numberOfClusters, double birthWeight, double deathWeight, double changeWeightCl) {
		mainEngine = new TableClusteringMainEngine();
		mainEngine.extractClusters(getAllPPLTables(),getAllPPLSchemas(),numberOfClusters,birthWeight, deathWeight, changeWeightCl);
	}
	
	public ArrayList<Cluster> getClusters(){
		return mainEngine.getClusters();
	}
	
	public ArrayList<Phase> getPhases(){
		return mainEnginePhase.getPhases();
	}
	
	public String[][] getSortedRows(String[][] finalRowsZoomArea) {
		PldRowSorter sorter = new PldRowSorter(finalRowsZoomArea, getAllPPLTables());
		String[][] sortedRows = sorter.sortRows();
		return sortedRows;
	}
	//CHANGES
	
}
