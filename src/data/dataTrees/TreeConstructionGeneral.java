package data.dataTrees;


import java.util.Map;
import java.util.TreeMap;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import data.dataPPL.pplSQLSchema.PPLSchema;
import data.dataPPL.pplSQLSchema.PPLTable;

public class TreeConstructionGeneral implements TreeConstruction {

	private  TreeMap<String,PPLSchema> schemas=new TreeMap<String,PPLSchema>();

	public TreeConstructionGeneral(TreeMap<String, PPLSchema> pplSchemas){
		this.schemas= new TreeMap<String, PPLSchema>(pplSchemas);
	}
	
	public JTree constructTree(){
		
		DefaultMutableTreeNode top=new DefaultMutableTreeNode("Versions");
		
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
	
}
