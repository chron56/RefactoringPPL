package gui.listeners;

import javax.swing.JOptionPane;

import gui.dialogs.ProjectInfoDialog;
import gui.mainEngine.Gui;

public class menuButtonListener{
	Gui gui;
	public menuButtonListener(Gui gui) {
		this.gui=gui;
	}

	public void getHelpButtonListener() {
		String message ="To open a project, you must select a .txt file that contains the names ONLY of " +
				"the SQL files of the dataset that you want to use."+"\n" +
				"The .txt file must have EXACTLY the same name with the folder " +
				"that contains the DDL Scripts of the dataset."+ "\n" +
				"Both .txt file and dataset folder must be in the same folder.";
		JOptionPane.showMessageDialog(null, message);
	}
	
	public void getInfoButtonListener(Gui gui) {
		if (!(gui.getCurrentProject() == null)) {


            System.out.println("Project Name:" + gui.getProjectName());
            System.out.println("Dataset txt:" + gui.getDatasetTxt());
            System.out.println("Input Csv:" + gui.getInputCsv());
            System.out.println("Output Assessment1:" + gui.getOutputAssessment1());
            System.out.println("Output Assessment2:" + gui.getOutputAssessment2());
            System.out.println("Transitions File:" + gui.getTransitionsFile());

            System.out.println("Schemas:" + gui.getDataController().getAllPPLSchemas().size());
            System.out.println("Transitions:" + gui.getDataController().getAllPPLTransitions().size());
            System.out.println("Tables:" + gui.getDataController().getAllPPLTables().size());


            ProjectInfoDialog infoDialog = new ProjectInfoDialog(gui.getProjectName(), gui.getDatasetTxt(), gui.getInputCsv(), gui.getTransitionsFile(), gui.getDataController().getAllPPLSchemas().size(),
                    gui.getDataController().getAllPPLTransitions().size(), gui.getDataController().getAllPPLTables().size());

			infoDialog.setVisible(true);
		}
		else{
			JOptionPane.showMessageDialog(null, "Select a Project first");
			return;
		}

	}
}