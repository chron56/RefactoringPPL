package gui.listeners;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.antlr.v4.runtime.RecognitionException;

import gui.dialogs.CreateProjectJDialog;
import gui.mainEngine.Gui;

public class mainFlowListener{
	
	public void createProject(Gui gui ) {
				CreateProjectJDialog createProjectDialog=new CreateProjectJDialog("","","","","","");
	            setProjectDialog(createProjectDialog, gui);
	}
	
	public void loadProject(Gui gui ) {
		String fileName=null;
		File dir=new File("filesHandler/inis");
		JFileChooser fcOpen1 = new JFileChooser();
		fcOpen1.setCurrentDirectory(dir);
		int returnVal = fcOpen1.showDialog(gui, "Open");
		
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			
            File file = fcOpen1.getSelectedFile();
            System.out.println(file);
            gui.setProject(file.getName());
            fileName=file.toString();
            System.out.println("!!" + gui.getProject());
          

		}
		else{
			return;
		}
		importData(gui,fileName);
	}
	
	public void editProject(Gui gui ) {
		String fileName=null;
		File dir=new File("filesHandler/inis");
		JFileChooser fcOpen1 = new JFileChooser();
		fcOpen1.setCurrentDirectory(dir);
		int returnVal = fcOpen1.showDialog(gui, "Open");
		
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			
            File file = fcOpen1.getSelectedFile();
            System.out.println(file);
            gui.setProject(file.getName());
            fileName=file.toString();
            System.out.println("!!" + gui.getProject());
          
            BufferedReader br;
			try {
				br = new BufferedReader(new FileReader(fileName));
				String line;
				
				while(true) {
					line = br.readLine();
					if (line == null) 
						break;
					if(line.contains("Project-name")){
						String[] projectNameTable=line.split(":");
                        gui.setProjectName(projectNameTable[1]);
					}
					else if(line.contains("Dataset-txt")){
						String[] datasetTxtTable=line.split(":");
                        gui.setDatasetTxt(datasetTxtTable[1]);
					}
					else if(line.contains("Input-csv")){
						String[] inputCsvTable=line.split(":");
                        gui.setInputCsv(inputCsvTable[1]);
					}
					else if(line.contains("Assessement1-output")){
						String[] outputAss1=line.split(":");
                        gui.setOutputAssessment1(outputAss1[1]);
					}
					else if(line.contains("Assessement2-output")){
						String[] outputAss2=line.split(":");
                        gui.setOutputAssessment2(outputAss2[1]);
					}
					else if(line.contains("Transition-xml")){
						String[] transitionXmlTable=line.split(":");
                        gui.setTransitionsFile(transitionXmlTable[1]);
					}


                }

                br.close();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			
			}

            System.out.println(gui.getProjectName());

            CreateProjectJDialog createProjectDialog = new CreateProjectJDialog(gui.getProjectName(), gui.getDatasetTxt(), gui.getInputCsv(), gui.getOutputAssessment1(), gui.getOutputAssessment2(), gui.getTransitionsFile());
            setProjectDialog(createProjectDialog, gui);

			
		}
		else{
			return;
		}
	}
	
	
	private void importData(Gui gui, String fileName) {
		try {
            gui.importData(fileName);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Something seems wrong with this file");
			return;
		} catch (RecognitionException e) {
			
			JOptionPane.showMessageDialog(null, "Something seems wrong with this file");
			return;
		}
	}
	
	private void setProjectDialog(CreateProjectJDialog createProjectDialog, Gui gui) {
		createProjectDialog.setModal(true);
		
		createProjectDialog.setVisible(true);
		
		if(createProjectDialog.getConfirmation()){
			
			createProjectDialog.setVisible(false);
			
			File file = createProjectDialog.getFile();
            System.out.println(file);
            gui.setProject(file.getName());
            String fileName = file.toString();
            System.out.println("!!" + gui.getProject());
            importData(gui, fileName);
			
			
		}
	}
}