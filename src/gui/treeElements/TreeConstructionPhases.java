package gui.treeElements;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.swing.JTree;
import test.testEngine.testAgent;
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
		/*Object temp1;
		try {
			temp1 = agent.convertFromBytes("Datakeeperfilebytes.txt");
			GlobalDataKeeper temp = (GlobalDataKeeper)temp1;
			this.agent.testLog(temp);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		}
		*/
		
		return treeToConstruct;
	}
	


}
