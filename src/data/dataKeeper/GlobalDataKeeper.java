package data.dataKeeper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import phaseAnalyzer.commons.Phase;
import phaseAnalyzer.commons.PhaseCollector;
import tableClustering.clusterExtractor.commons.Cluster;
import tableClustering.clusterExtractor.commons.ClusterCollector;
import data.dataPPL.pplSQLSchema.PPLSchema;
import data.dataPPL.pplSQLSchema.PPLTable;
import data.dataPPL.pplTransition.AtomicChange;
import data.dataPPL.pplTransition.PPLTransition;
import data.dataPPL.pplTransition.TableChange;
import data.dataProccessing.Worker;

public class GlobalDataKeeper {

	
	//test
	private TreeMap<String,PPLSchema> allPPLSchemas = null;
	private TreeMap<String,PPLTable> allTables = null;
	private ArrayList<AtomicChange> atomicChanges = null;
	private TreeMap<String,TableChange> tableChanges = null;
	private TreeMap<String,TableChange> tableChangesForTwo = null;
	private TreeMap<Integer,PPLTransition> allPPLTransitions = null;
	private ArrayList<PhaseCollector> phaseCollectors = null;
	private ArrayList<ClusterCollector> clusterCollectors = null;

	private String 	projectDataFolder=null;
	private String filename=null;
	private String transitionsFile="";
	

	public GlobalDataKeeper(String fl,String transitionsFile){
		allPPLSchemas = new TreeMap<String,PPLSchema>();
		allTables = new  TreeMap<String,PPLTable>();
		atomicChanges = new ArrayList<AtomicChange>();
		tableChanges = new TreeMap<String,TableChange>();
		tableChangesForTwo = new TreeMap<String,TableChange>();
		allPPLTransitions = new TreeMap<Integer,PPLTransition>();
		phaseCollectors = new ArrayList<PhaseCollector>();
		clusterCollectors = new ArrayList<ClusterCollector>();
		filename=fl;
		this.transitionsFile=transitionsFile;
	}
	
	public GlobalDataKeeper(){
		
		
	}
	
	public void setData(){
		
		Worker w = new Worker(filename,transitionsFile);
		try {
			w.work();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		setAllPPLSchemas(w.getAllPPLSchemas());
		setAllPPLTables(w.getAllPPLTables());
		setAllPPLTransitions(w.getAllPPLTransitions());
		setAllTableChanges(w.getAllTableChanges());
		setAtomicChanges(w.getAtomicChanges());
		setDataFolder(w.getDataFolder());
		

		
	}
	
	public void setPhaseCollectors(ArrayList<PhaseCollector> phaseCollectors){
		this.phaseCollectors=phaseCollectors;
	}
	
	public void setClusterCollectors(ArrayList<ClusterCollector> clusterCollectors){
		this.clusterCollectors=clusterCollectors;
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
		return this.phaseCollectors;
	}
	
	public ArrayList<ClusterCollector> getClusterCollectors(){
		return this.clusterCollectors;
	}
	
	//CHANGES
	public JTree getPhasesWithClustersTree() {
		DefaultMutableTreeNode top=new DefaultMutableTreeNode("Clusters");		
		
		ArrayList<Cluster> clusters=getClusterCollectors().get(0).getClusters();
		
		for(int i=0; i<clusters.size(); i++){
			
			DefaultMutableTreeNode a=new DefaultMutableTreeNode("Cluster "+i);
			top.add(a);
			ArrayList<String> tables=clusters.get(i).getNamesOfTables();
			
			for(String tableName:tables){
				DefaultMutableTreeNode a1=new DefaultMutableTreeNode(tableName);
				a.add(a1);
				
			}
			
		}
		JTree treeToConstruct = new JTree(top);
		
		return treeToConstruct;
	}
	
	public JTree getPhasesTree() {
		DefaultMutableTreeNode top=new DefaultMutableTreeNode("Phases");
		TreeMap<String, PPLSchema> schemas=new TreeMap<String, PPLSchema>();
		
				
		ArrayList<Phase> phases=getPhaseCollectors().get(0).getPhases();
		
		for(int i=0; i<phases.size(); i++){
			
			DefaultMutableTreeNode a=new DefaultMutableTreeNode("Phase "+i);
			top.add(a);
			TreeMap<Integer,PPLTransition> transitions=phases.get(i).getPhasePPLTransitions();
			
			for(Map.Entry<Integer, PPLTransition> tree:transitions.entrySet()){
				DefaultMutableTreeNode a1=new DefaultMutableTreeNode(tree.getKey());
				ArrayList<TableChange> tableChanges=tree.getValue().getTableChanges();
				for(int j=0; j<tableChanges.size(); j++){
					DefaultMutableTreeNode a2=new DefaultMutableTreeNode(tableChanges.get(j).getAffectedTableName());
					a1.add(a2);
				}
				a.add(a1);

				schemas.put(tree.getValue().getOldVersionName(),getAllPPLSchemas().get(tree.getValue().getOldVersionName()));
				schemas.put(tree.getValue().getNewVersionName(),getAllPPLSchemas().get(tree.getValue().getNewVersionName()));
			}
			
		}
		
		JTree treeToConstruct = new JTree(top);
		
		return treeToConstruct;
	}
	
	public JTree getGeneralTree() {
		DefaultMutableTreeNode top=new DefaultMutableTreeNode("Versions");
		TreeMap<String, PPLSchema> schemas=getAllPPLSchemas();
		
		for (Map.Entry<String,PPLSchema> pplSchemaIterator : schemas.entrySet()) {
			
			DefaultMutableTreeNode a=new DefaultMutableTreeNode(pplSchemaIterator.getKey());
		    top.add(a);
		    TreeMap<String, PPLTable> tables=pplSchemaIterator.getValue().getTables();
		    
			for (Map.Entry<String,PPLTable> pplT : tables.entrySet()) {
				DefaultMutableTreeNode a1=new DefaultMutableTreeNode(pplT.getKey());
				a.add(a1);
			}
		    
		}
		
		JTree treeToConstruct = new JTree(top);
		
		return treeToConstruct;
	}
	//CHANGES
	
}
