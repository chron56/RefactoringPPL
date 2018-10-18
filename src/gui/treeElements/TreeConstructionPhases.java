package gui.treeElements;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import phaseAnalyzer.commons.Phase;
import data.dataKeeper.GlobalDataKeeper;
import data.dataPPL.pplSQLSchema.PPLSchema;
import data.dataPPL.pplTransition.PPLTransition;
import data.dataPPL.pplTransition.TableChange;

public class TreeConstructionPhases implements TreeConstruction {
	
	private GlobalDataKeeper dataKeeper=null;

	public TreeConstructionPhases(GlobalDataKeeper dataKeeper) {
		this.dataKeeper=dataKeeper;
	}
	
	@Override
	public JTree constructTree() {
		
		DefaultMutableTreeNode top=new DefaultMutableTreeNode("Phases");
		TreeMap<String, PPLSchema> schemas=new TreeMap<String, PPLSchema>();
		
				
		ArrayList<Phase> phases=dataKeeper.getPhaseCollectors().get(0).getPhases();
		
		for(int i=0; i<phases.size(); i++){
			
			DefaultMutableTreeNode a=new DefaultMutableTreeNode("Phase "+i);
			top.add(a);
			TreeMap<Integer,PPLTransition> transitions=phases.get(i).getPhasePPLTransitions();
			
			for(Map.Entry<Integer, PPLTransition> tr:transitions.entrySet()){
				DefaultMutableTreeNode a1=new DefaultMutableTreeNode(tr.getKey());
				ArrayList<TableChange> tableChanges=tr.getValue().getTableChanges();
				for(int j=0; j<tableChanges.size(); j++){
					DefaultMutableTreeNode a2=new DefaultMutableTreeNode(tableChanges.get(j).getAffectedTableName());
					a1.add(a2);
				}
				a.add(a1);

				schemas.put(tr.getValue().getOldVersionName(),dataKeeper.getAllPPLSchemas().get(tr.getValue().getOldVersionName()));
				schemas.put(tr.getValue().getNewVersionName(),dataKeeper.getAllPPLSchemas().get(tr.getValue().getNewVersionName()));
			}
			
		}
		
		JTree treeToConstruct = new JTree(top);
		
		return treeToConstruct;
	}

}
