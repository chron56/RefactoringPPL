package gui.listeners;

import java.util.ArrayList;
import javax.swing.JOptionPane;
import gui.dialogs.ParametersJDialog;
import gui.mainEngine.Gui;
import gui.tableElements.commons.ExtendedJvTable;
import outputSerializer.OutputSerializer;

public class showListener{
	Gui gui;
	public showListener(Gui gui) {
		this.gui=gui;
	}

	public void showLifetimeTable( OutputSerializer tlp ) {
		if (!(gui.getCurrentProject() == null)) {
			gui.getDataController().constructTableWithAllSquares();
			String[] columns=gui.getDataController().getTableColumns("AllSquares");
			String [][] rows=gui.getDataController().getTableRows("AllSquares");
			gui.setSegmentSizeDetailedTable(gui.getDataController().getSegmentSize("AllSquares"));
            gui.getTabbedPane().setSelectedIndex(0);
            gui.makeDetailedTable(columns, rows, true);
			tlp.writeString("\n-----[ BUTTON T4 ]-----\n");
            tlp.writeTable("Full Lifetime Table", columns, rows);
            tlp.closeFile();                   
        }
		else{
			JOptionPane.showMessageDialog(null, "Select a Project first");
			return;
		}
	}
	
	public void showGeneralLifetimeIDU(OutputSerializer tlp) {
		if (!(gui.getCurrentProject() == null)) {               	
            gui.getZoomInButton().setVisible(true);
            gui.getZoomOutButton().setVisible(true);
    		gui.getDataController().constructTableConstructionIDU();
    		final String[] columns=gui.getDataController().getTableColumns("IDU");
    		final String[][] rows=gui.getDataController().getTableRows("IDU");
    		gui.setSegmentSizeZoomArea(gui.getDataController().getSegmentSize("IDU"));
            System.out.println("Schemas: " + gui.getDataController().getAllPPLSchemas().size());
			System.out.println("C: "+columns.length+" R: "+rows.length);
            gui.setFinalColumnsZoomArea(columns);                
            gui.setFinalRowsZoomArea(rows);               
            gui.getTabbedPane().setSelectedIndex(0);
            tlp.writeString("\n-----[ BUTTON T1 ]-----\n");
            tlp.writeTable("PLD", gui.getFinalColumnsZoomArea(), gui.getFinalRowsZoomArea());
            gui.makeGeneralTableIDU();
            gui.fillTree();
		}
		else{
			JOptionPane.showMessageDialog(null, "Select a Project first");
			return;
		}
	}
	
	public void showGeneralLifetimePhasesPLD( OutputSerializer tlp) {
		if (!(gui.getProject() == null)) {
            gui.setWholeCol(-1);
			ParametersJDialog jD=new ParametersJDialog(false);
			
			jD.setModal(true);
			
			
			jD.setVisible(true);
			
			if(jD.getConfirmation()){
				
                gui.setTimeWeight(jD.getTimeWeight());
                gui.setChangeWeight(jD.getChangeWeight());
                gui.setPreProcessingTime(jD.getPreProcessingTime());
                gui.setPreProcessingChange(jD.getPreProcessingChange());
                gui.setNumberOfPhases(jD.getNumberOfPhases());
                tlp.writeString("\n-----[ BUTTON T2 ]-----\n");
                System.out.println(gui.getTimeWeight() + " " + gui.getChangeWeight());
                gui.getDataController().setPhaseCollectors(gui.getNumberOfPhases(),gui.getInputCsv(), gui.getOutputAssessment1(), gui.getOutputAssessment2(), gui.getTimeWeight(), gui.getChangeWeight(), gui.getPreProcessingTime(), gui.getPreProcessingChange());
                if (gui.getDataController().getPhaseCollectors().size() != 0) {
            		gui.getDataController().constructPhasesTable();
            		final String[] columns=gui.getDataController().getTableColumns("Phases");
            		final String[][] rows=gui.getDataController().getTableRows("Phases");
                    gui.setSegmentSize(gui.getDataController().getSegmentSize("Phases"));
                    System.out.println("Schemas: " + gui.getDataController().getAllPPLSchemas().size());
					System.out.println("C: "+columns.length+" R: "+rows.length);						
                    gui.setFinalColumns(columns);
                    gui.setFinalRows(rows);
                    gui.getTabbedPane().setSelectedIndex(0);                         
                    gui.makeGeneralTablePhases();
                    gui.fillPhasesTree();                            
				}
				else{
					JOptionPane.showMessageDialog(null, "Extract Phases first");
				}
			}
		}
		else{					
			JOptionPane.showMessageDialog(null, "Please select a project first!");				
		}
	
	}
	
	public void showGeneralLifetimePhasesWithClustersPLD( OutputSerializer tlp ) {
		
		 gui.setWholeCol(-1);
         if (!(gui.getProject() == null)) {
				
				ParametersJDialog jD=new ParametersJDialog(true);					
				jD.setModal(true);			
				jD.setVisible(true);
				
				if(jD.getConfirmation()){

                 gui.setTimeWeight(jD.getTimeWeight());
                 gui.setChangeWeight(jD.getChangeWeight());
                 gui.setPreProcessingTime(jD.getPreProcessingTime());
                 gui.setPreProcessingChange(jD.getPreProcessingChange());
                 gui.setNumberOfPhases(jD.getNumberOfPhases());
                 gui.setNumberOfClusters(jD.getNumberOfClusters());
                 gui.setBirthWeight(jD.geBirthWeight());
                 gui.setDeathWeight(jD.getDeathWeight());
                 gui.setChangeWeightCl(jD.getChangeWeightCluster());
                 tlp.writeString("\n-----[ BUTTON T3 ]-----\n");                      
                 System.out.println(gui.getTimeWeight() + " " + gui.getChangeWeight());	
				 System.out.println("\n\n\n");
                 gui.getDataController().setPhaseCollectors(gui.getNumberOfPhases(),gui.getInputCsv(), gui.getOutputAssessment1(), gui.getOutputAssessment2(), gui.getTimeWeight(), gui.getChangeWeight(), gui.getPreProcessingTime(), gui.getPreProcessingChange());
                 gui.getDataController().extractClusters(gui.getNumberOfClusters(),gui.getBirthWeight(), gui.getDeathWeight(), gui.getChangeWeightCl());
                 if (gui.getDataController().getPhaseCollectors().size() != 0) {
                 	gui.getDataController().constructTableWithClusters();
                    final String[] columns=gui.getDataController().getTableColumns("Clusters");
                    final String[][] rows=gui.getDataController().getTableRows("Clusters");
                    gui.setSegmentSize(gui.getDataController().getSegmentSize("Clusters"));                        
                    System.out.println("Schemas: " + gui.getDataController().getAllPPLSchemas().size());
					System.out.println("C: "+columns.length+" R: "+rows.length);							
                    gui.setFinalColumns(columns);
                    gui.setFinalRows(rows);                           
                    gui.getTabbedPane().setSelectedIndex(0);
                    gui.makeGeneralTablePhases();
                    gui.fillClustersTree();
						

					}
					else{
						JOptionPane.showMessageDialog(null, "Extract Phases first");
					}
				}
			}
			else{
				
				JOptionPane.showMessageDialog(null, "Please select a project first!");
				
			}
	}
	
	public void showDetailsForPhase(ExtendedJvTable generalTable ) {
		String sSelectedRow = gui.getFinalRows()[0][0];
		System.out.println("?"+sSelectedRow);
        gui.setTablesSelected(new ArrayList<String>());
        for (int i = 0; i < gui.getFinalRows().length; i++)
            gui.getTablesSelected().add((String) generalTable.getValueAt(i, 0));

    	if(!sSelectedRow.contains("Cluster ")){

            gui.showSelectionToZoomArea(gui.getWholeCol());
    	}
    	else{
            gui.showClusterSelectionToZoomArea(gui.getWholeCol(), "");
    	}
	}
}