package gui.treeElements;

import javax.swing.JTree;
import data.dataKeeper.GlobalDataKeeper;


public class TreeConstructionPhases implements TreeConstruction {
	
	private GlobalDataKeeper dataKeeper=null;

	public TreeConstructionPhases(GlobalDataKeeper dataKeeper) {
		this.dataKeeper=dataKeeper;
	}
	
	@Override
	public JTree constructTree() {
		
		JTree treeToConstruct = dataKeeper.getPhasesTree();		
		return treeToConstruct;
	}
	


}
