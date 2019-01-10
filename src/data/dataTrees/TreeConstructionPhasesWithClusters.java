package data.dataTrees;
import java.util.ArrayList;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import tableClustering.clusterExtractor.commons.Cluster;

public class TreeConstructionPhasesWithClusters implements TreeConstruction {
	
	ArrayList<Cluster> clusters;
	
	public TreeConstructionPhasesWithClusters(ArrayList<Cluster> clusters) {
		this. clusters= new ArrayList<Cluster>(clusters);
	}
	
	@Override
	public JTree constructTree() {
		DefaultMutableTreeNode top=new DefaultMutableTreeNode("Clusters");		
		
		
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

}
