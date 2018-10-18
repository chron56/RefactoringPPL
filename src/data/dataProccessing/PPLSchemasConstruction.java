package data.dataProccessing;

import gr.uoi.cs.daintiness.hecate.sql.Attribute;
import gr.uoi.cs.daintiness.hecate.sql.Schema;
import gr.uoi.cs.daintiness.hecate.sql.Table;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import data.dataPPL.pplSQLSchema.PPLAttribute;
import data.dataPPL.pplSQLSchema.PPLSchema;
import data.dataPPL.pplSQLSchema.PPLTable;

public class PPLSchemasConstruction {

	private static ArrayList<Schema> allSchemas = new ArrayList<Schema>();
	private static TreeMap<String,PPLSchema> allPPLSchemas = null;

	public PPLSchemasConstruction(ArrayList<Schema> tmpAllSchemas){
		allPPLSchemas = new TreeMap<String,PPLSchema>();
		allSchemas = new ArrayList<Schema>();
		allSchemas=tmpAllSchemas;

	}
	
	
	public void makePPLSchemas(){
		

		for(int i=0; i<allSchemas.size(); i++){
			
			Schema tmpHecSchema = new Schema();
			
			tmpHecSchema=allSchemas.get(i);
			
			
			PPLSchema tmpPPLSchema = new PPLSchema(tmpHecSchema.getName(),tmpHecSchema);
			
			
			TreeMap<String,Table> tmpHecTables = new TreeMap<String,Table>();
			
			tmpHecTables=tmpHecSchema.getTables();
			
			for (Map.Entry<String, Table> t : tmpHecTables.entrySet()) {
				
				PPLTable tmpPPLTable = new PPLTable(t.getValue().getName(),t.getValue());
				
				
				TreeMap<String,Attribute> tmpHecAttributes = new TreeMap<String,Attribute>();
				
				tmpHecAttributes=t.getValue().getAttrs();
				
				for (Map.Entry<String, Attribute> a : tmpHecAttributes.entrySet()) {

					PPLAttribute tmpPPLAttribute = new PPLAttribute(a.getValue());
					
					tmpPPLTable.addAttribute(tmpPPLAttribute);
					
				}
				
				tmpPPLSchema.addTable(tmpPPLTable);

			}

			allPPLSchemas.put(tmpPPLSchema.getName(),tmpPPLSchema);

			
		}
		

		
	}
	
	public TreeMap<String,PPLSchema> getAllPPLSchemas(){

		return allPPLSchemas;

	}
	
}
