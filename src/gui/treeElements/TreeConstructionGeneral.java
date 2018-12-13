package gui.treeElements;

import javax.swing.JTree;
import data.dataKeeper.GlobalDataKeeper;

public class TreeConstructionGeneral {

	private GlobalDataKeeper dataKeeper=null;

	public TreeConstructionGeneral(GlobalDataKeeper dataKeeper){
		this.dataKeeper=dataKeeper;
	}
	
	public JTree constructTree(){
		
		
		JTree treeToConstruct = dataKeeper.getGeneralTree();
		return treeToConstruct;
		
	}
	
}
