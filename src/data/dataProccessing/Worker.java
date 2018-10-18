package data.dataProccessing;


import gr.uoi.cs.daintiness.hecate.sql.Schema;
import gr.uoi.cs.daintiness.hecate.transitions.TransitionList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

import data.dataPPL.pplSQLSchema.PPLSchema;
import data.dataPPL.pplSQLSchema.PPLTable;
import data.dataPPL.pplTransition.AtomicChange;
import data.dataPPL.pplTransition.PPLTransition;
import data.dataPPL.pplTransition.TableChange;

public class Worker {
	
	private String filename=null;
	private String transitionsFile=null;

	private TreeMap<String,PPLSchema> allPPLSchemas = null;
	private TreeMap<String,PPLTable> allTables = null;
	private ArrayList<AtomicChange> atomicChanges = null;
	private TreeMap<String,TableChange> tableChanges = null;
	private TreeMap<Integer,PPLTransition> allPPLTransitions = null;
	
	public Worker(String tmpFilename,String transitionsFile){
		allPPLSchemas = new TreeMap<String,PPLSchema>();
		allTables = new TreeMap<String,PPLTable>();
		atomicChanges = new ArrayList<AtomicChange>();
		tableChanges = new TreeMap<String,TableChange>();
		allPPLTransitions = new TreeMap<Integer,PPLTransition>();
		filename=tmpFilename;
		this.transitionsFile=transitionsFile;
		
	}

	public void work() throws IOException{
		
		ImportSchemas filesToImportData=new ImportSchemas(filename,transitionsFile);
		try {
			filesToImportData.loadDataset();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<Schema> allHecSchemas = new ArrayList<Schema>();
		allHecSchemas=filesToImportData.getAllHecSchemas();
		ArrayList<TransitionList> allTransitions=filesToImportData.getAllTransitions();

		
		PPLSchemasConstruction pplSchemas = new PPLSchemasConstruction(allHecSchemas);
		pplSchemas.makePPLSchemas();
		allPPLSchemas=pplSchemas.getAllPPLSchemas();
		
		PPLTablesConstruction pplTables = new PPLTablesConstruction(allPPLSchemas);
		pplTables.makeAllPPLTables();
		allTables=pplTables.getAllPPLTables();
		
		AtomicChangeConstruction atomicChangesC = new AtomicChangeConstruction(allTransitions);
		atomicChangesC.makeAtomicChanges();
		atomicChanges=atomicChangesC.getAtomicChanges();
		
		TableChangeConstruction tableChangesC = new TableChangeConstruction(atomicChanges, allTables);
		tableChangesC.makeTableChanges();
		tableChanges=tableChangesC.getTableChanges();
		
		PPLTransitionConstruction pplTransitionC = new PPLTransitionConstruction(allPPLSchemas, tableChanges);
		pplTransitionC.makePPLTransitions();
		allPPLTransitions=pplTransitionC.getAllPPLTransitions();

		
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
	
	public TreeMap<Integer,PPLTransition> getAllPPLTransitions(){
		
		return allPPLTransitions;
		
	}
	
	public String getDataFolder(){
		return filename.replaceAll(".txt", "");
	}
	
}
