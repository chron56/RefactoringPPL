package data.dataTrees;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import data.dataPPL.pplSQLSchema.PPLSchema;
import data.dataPPL.pplTransition.PPLTransition;
import data.dataPPL.pplTransition.TableChange;
import phaseAnalyzer.commons.Phase;


public class TreeConstructionPhases implements TreeConstruction {
	
	private ArrayList<Phase> phases ;
	private TreeMap<String,PPLSchema> schemas;
	
	public TreeConstructionPhases(ArrayList<Phase> phases, TreeMap<String, PPLSchema> schemas) {
		this.phases= new ArrayList<Phase>(phases);
		this.schemas= new TreeMap<String, PPLSchema>(schemas);
	}
	
	@Override
	public JTree constructTree() {
		DefaultMutableTreeNode top=new DefaultMutableTreeNode("Phases");
		
		
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

				schemas.put(tree.getValue().getOldVersionName(),schemas.get(tree.getValue().getOldVersionName()));
				schemas.put(tree.getValue().getNewVersionName(),schemas.get(tree.getValue().getNewVersionName()));
			}
			
		}
		
		JTree treeToConstruct = new JTree(top);
	
		return treeToConstruct;
	}
	


}
