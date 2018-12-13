package gui.treeElements;

import javax.swing.JTree;
import data.dataKeeper.GlobalDataKeeper;


public class TreeConstructionPhases implements TreeConstruction {
	
	private GlobalDataKeeper dataKeeper=null;
	//private testAgent agent = null;

	public TreeConstructionPhases(GlobalDataKeeper dataKeeper) {
		this.dataKeeper=dataKeeper;
		//this.agent = new testAgent();
	}
	
	@Override
	public JTree constructTree() {
		
		JTree treeToConstruct = dataKeeper.getPhasesTree();		
		return treeToConstruct;
	}
	


}
