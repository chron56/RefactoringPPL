package gui.treeElements;
import javax.swing.JTree;
import test.testEngine.testAgent;
import data.dataKeeper.GlobalDataKeeper;

public class TreeConstructionPhasesWithClusters implements TreeConstruction {
	
	private GlobalDataKeeper dataKeeper=null;
	private testAgent agent = null;

	public TreeConstructionPhasesWithClusters(GlobalDataKeeper dataKeeper) {
		this.dataKeeper=dataKeeper;
		//this.agent = new testAgent();
	}
	
	@Override
	public JTree constructTree() {
		
		JTree treeToConstruct = dataKeeper.getPhasesWithClustersTree();
		//this.agent.testLog(treeToConstruct);
		return treeToConstruct;
	}

}
