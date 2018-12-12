package gui.treeElements;

import javax.swing.JTree;
import data.dataKeeper.GlobalDataKeeper;
import test.testEngine.testAgent;

public class TreeConstructionGeneral {

	private GlobalDataKeeper dataKeeper=null;
	private testAgent agent = null;
	public TreeConstructionGeneral(GlobalDataKeeper dataKeeper){
		this.dataKeeper=dataKeeper;
		//this.agent = new testAgent();
	}
	
	public JTree constructTree(){
		
		
		JTree treeToConstruct = dataKeeper.getGeneralTree();
		//this.agent.testLog(treeToConstruct);
		return treeToConstruct;
		
	}
	
}
