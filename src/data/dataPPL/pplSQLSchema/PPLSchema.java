package data.dataPPL.pplSQLSchema;

import gr.uoi.cs.daintiness.hecate.sql.Schema;

import java.util.Map;
import java.util.TreeMap;

public class PPLSchema {

	private String name;
	private TreeMap<String, PPLTable> tables;

	public PPLSchema(TreeMap<String, PPLTable> t) {
		this.tables = t;
	}	
	
	public PPLSchema() {
		this.tables = new TreeMap<String, PPLTable>();
	}
	
	public PPLSchema(String name) {
		this.tables = new TreeMap<String, PPLTable>();
		this.name = name;
	}

	public PPLSchema(String tmpName,Schema tmpHecSchema){
		
		name=tmpName;
		this.tables = new TreeMap<String, PPLTable>();

	}
	
	
	public String getName() {
		
		return name;
	}
	
	public TreeMap<String, PPLTable> getTables() {
		return this.tables;
	}
	
	public void addTable(PPLTable table) {
		this.tables.put(table.getName(), table);
	}
	
	public String toString() {
		return name;
	}

	public int[] getSize() {
		int attr = 0;
		for (PPLTable t : this.tables.values()) {
			attr += t.getSize();
		}
		int[] res = {this.tables.size(), attr};
		return res;
	}

	public void setTitle(String title) {
		this.name = title;
	}

	public PPLTable getTableAt(int i) {
		int c = 0;
		if (i >= 0 && i < tables.size()){
			for (Map.Entry<String, PPLTable> t : tables.entrySet()) {
				if (c == i) {
					return t.getValue();
				}
				c++;
			}
		}
		return null;
	}
	
}
