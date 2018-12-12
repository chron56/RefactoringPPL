package gui.mainEngine;

//try to extract relationship between gui and pplSchema and pplTransition

import data.dataKeeper.GlobalDataKeeper;
import data.dataSorters.PldRowSorter;
import gui.dialogs.CreateProjectJDialog;
import gui.dialogs.EnlargeTable;
import gui.dialogs.ParametersJDialog;
import gui.dialogs.ProjectInfoDialog;
import gui.tableElements.commons.JvTable;
import gui.tableElements.commons.MyTableModel;
import gui.tableElements.tableConstructors.*;
import gui.tableElements.tableRenderers.IDUHeaderTableRenderer;
import gui.tableElements.tableRenderers.IDUTableRenderer;
import gui.treeElements.TreeConstructionGeneral;
import gui.treeElements.TreeConstructionPhases;
import gui.treeElements.TreeConstructionPhasesWithClusters;
import org.antlr.v4.runtime.RecognitionException;
import phaseAnalyzer.engine.PhaseAnalyzerMainEngine;
import tableClustering.clusterExtractor.engine.TableClusteringMainEngine;
import tableClustering.clusterValidator.engine.ClusterValidatorMainEngine;
import test.testEngine.TestLoadProject;
import test.testEngine.testAgent;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Gui extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    private final JPanel contentPane;
    private final JPanel lifeTimePanel = new JPanel();
    private final JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.TOP);
    private final JScrollPane tmpScrollPane = new JScrollPane();
    private final JScrollPane treeScrollPane = new JScrollPane();
    private final JScrollPane tmpScrollPaneZoomArea = new JScrollPane();
    private final JPanel sideMenu = new JPanel();
    private final JPanel tablesTreePanel = new JPanel();
    private final JPanel descriptionPanel = new JPanel();
    private final JLabel treeLabel;
    private final JLabel generalTableLabel;
	
	
	
	private ArrayList<Integer> selectedRows=new ArrayList<Integer>();
    private final JLabel zoomAreaLabel;
    private final JLabel descriptionLabel;
    private final JTextArea descriptionText;
    private final JButton zoomInButton;
    private final JButton zoomOutButton;
    private final JButton uniformlyDistributedButton;
    private final JButton notUniformlyDistributedButton;
    private final JButton showThisToPopup;
    private final JButton undoButton;
	
	
	
	private Integer[] segmentSize=new Integer[4];
	private Integer[] segmentSizeZoomArea=new Integer[4];
	private Integer[] segmentSizeDetailedTable=new Integer[3];
    private final JMenu mnProject;
    private final JMenuItem mntmInfo;
    private MyTableModel detailedModel;
    private MyTableModel generalModel;
    private MyTableModel zoomModel;
    private JvTable LifeTimeTable;
    private JvTable zoomAreaTable;
    private GlobalDataKeeper globalDataKeeper;
	
	private String projectName="";
	private String datasetTxt="";
	private String inputCsv="";
	private String outputAssessment1="";
	private String outputAssessment2="";
	private String transitionsFile="";
	private ArrayList<String> selectedFromTree=new ArrayList<String>();
	
	private JTree tablesTree=new JTree();
    private String[] finalColumns;
    private String[][] finalRows;
    private String[] finalColumnsZoomArea;
    private String[][] finalRowsZoomArea;
    private String[] firstLevelUndoColumnsZoomArea;
    private String[][] firstLevelUndoRowsZoomArea;
    private String currentProject;
    private String project;
    private Float timeWeight;
    private Float changeWeight;
    private Double birthWeight;
    private Double deathWeight;
    private Double changeWeightCl;


	private int[] selectedRowsFromMouse;
	private int selectedColumn=-1;
	private int selectedColumnZoomArea=-1;
	private int wholeCol=-1;
	private int wholeColZoomArea=-1;
	
	private int rowHeight=1;
	private int columnWidth=1;

	private ArrayList<String> tablesSelected = new ArrayList<String>();
    private Integer numberOfPhases;
    private Integer numberOfClusters;
    private Boolean preProcessingTime;
    private Boolean preProcessingChange;
    private boolean showingPld;
	private TestLoadProject tlp= new TestLoadProject("FILE.txt");
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Gui frame = new Gui();
					frame.setVisible(true);
					
				} catch (Exception e) {
					//return;
					e.printStackTrace();
				}
				
			}
			
		}); 
	}

	/**
	 * Create the frame.
	 */
	public Gui() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setResizable(false);

        
		JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmCreateProject = new JMenuItem("Create Project");
		mntmCreateProject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				CreateProjectJDialog createProjectDialog=new CreateProjectJDialog("","","","","","");

				createProjectDialog.setModal(true);
				
				
				createProjectDialog.setVisible(true);
				
				if(createProjectDialog.getConfirmation()){
					
					createProjectDialog.setVisible(false);
					
					File file = createProjectDialog.getFile();
                    System.out.println(file);
                    Gui.this.project = file.getName();
		            String fileName=file.toString();
                    System.out.println("!!" + Gui.this.project);
		          
					try {
                        Gui.this.importData(fileName);
					} catch (IOException e) {
						JOptionPane.showMessageDialog(null, "Something seems wrong with this file");
						return;
					} catch (RecognitionException e) {
						
						JOptionPane.showMessageDialog(null, "Something seems wrong with this file");
						return;
					}
					
					
				}
				
		            
				
			}
		});
		mnFile.add(mntmCreateProject);
		
		JMenuItem mntmLoadProject = new JMenuItem("Load Project");
		mntmLoadProject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				String fileName=null;
				File dir=new File("filesHandler/inis");
				JFileChooser fcOpen1 = new JFileChooser();
				fcOpen1.setCurrentDirectory(dir);
				int returnVal = fcOpen1.showDialog(Gui.this, "Open");
				
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					
		            File file = fcOpen1.getSelectedFile();
                    System.out.println(file);
                    Gui.this.project = file.getName();
		            fileName=file.toString();
                    System.out.println("!!" + Gui.this.project);
		          

				}
				else{
					return;
				}
				try {
                    Gui.this.importData(fileName);
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, "Something seems wrong with this file");
					return;
				} catch (RecognitionException e) {
					
					JOptionPane.showMessageDialog(null, "Something seems wrong with this file");
					return;
				}
				
			}
		});
		mnFile.add(mntmLoadProject);
		
		JMenuItem mntmEditProject = new JMenuItem("Edit Project");
		mntmEditProject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				String fileName=null;
				File dir=new File("filesHandler/inis");
				JFileChooser fcOpen1 = new JFileChooser();
				fcOpen1.setCurrentDirectory(dir);
				int returnVal = fcOpen1.showDialog(Gui.this, "Open");
				
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					
		            File file = fcOpen1.getSelectedFile();
                    System.out.println(file);
                    Gui.this.project = file.getName();
		            fileName=file.toString();
                    System.out.println("!!" + Gui.this.project);
		          
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
                                Gui.this.projectName = projectNameTable[1];
							}
							else if(line.contains("Dataset-txt")){
								String[] datasetTxtTable=line.split(":");
                                Gui.this.datasetTxt = datasetTxtTable[1];
							}
							else if(line.contains("Input-csv")){
								String[] inputCsvTable=line.split(":");
                                Gui.this.inputCsv = inputCsvTable[1];
							}
							else if(line.contains("Assessement1-output")){
								String[] outputAss1=line.split(":");
                                Gui.this.outputAssessment1 = outputAss1[1];
							}
							else if(line.contains("Assessement2-output")){
								String[] outputAss2=line.split(":");
                                Gui.this.outputAssessment2 = outputAss2[1];
							}
							else if(line.contains("Transition-xml")){
								String[] transitionXmlTable=line.split(":");
                                Gui.this.transitionsFile = transitionXmlTable[1];
							}


                        }

                        br.close();
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					
					}

                    System.out.println(Gui.this.projectName);

                    CreateProjectJDialog createProjectDialog = new CreateProjectJDialog(Gui.this.projectName, Gui.this.datasetTxt, Gui.this.inputCsv, Gui.this.outputAssessment1, Gui.this.outputAssessment2, Gui.this.transitionsFile);
				
					createProjectDialog.setModal(true);
					
					createProjectDialog.setVisible(true);
					
					if(createProjectDialog.getConfirmation()){
						
						createProjectDialog.setVisible(false);
						
						file = createProjectDialog.getFile();
                        System.out.println(file);
                        Gui.this.project = file.getName();
			            fileName=file.toString();
                        System.out.println("!!" + Gui.this.project);
					
						try {
                            Gui.this.importData(fileName);
						} catch (IOException e) {
							JOptionPane.showMessageDialog(null, "Something seems wrong with this file");
							return;
						} catch (RecognitionException e) {
							
							JOptionPane.showMessageDialog(null, "Something seems wrong with this file");
							return;
						}
						
					}
					
				}
				else{
					return;
				}
				
			}
		});
		mnFile.add(mntmEditProject);
		
		
		JMenu mnTable = new JMenu("Table");
		menuBar.add(mnTable);
		
		JMenuItem mntmShowLifetimeTable = new JMenuItem("Show Full Detailed LifeTime Table");
		mntmShowLifetimeTable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                if (!(Gui.this.currentProject == null)) {
                    TableConstructionAllSquaresIncluded table = new TableConstructionAllSquaresIncluded(Gui.this.globalDataKeeper);
					final String[] columns=table.constructColumns();
					final String[][] rows=table.constructRows();
                    Gui.this.segmentSizeDetailedTable = table.getSegmentSize();
                    Gui.this.tabbedPane.setSelectedIndex(0);
                    Gui.this.makeDetailedTable(columns, rows, true);
                    
                    tlp.writeToFile("[BT4 1] Show Full Detailed LifeTime Table button listener Columns => "+Arrays.toString(columns));
                    tlp.writeToFile("[BT4 2] Show Full Detailed LifeTime Table button listener Rows => "+Arrays.deepToString(rows));
                    tlp.closeFile();
                }
				else{
					JOptionPane.showMessageDialog(null, "Select a Project first");
					return;
				}
			}
		});
		
		JMenuItem mntmShowGeneralLifetimeIDU = new JMenuItem("Show PLD");
		mntmShowGeneralLifetimeIDU.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
                if (!(Gui.this.currentProject == null)) {
                	tlp.updateOption();
                	tlp.writeToFile("### "+tlp.getOption()+" ###\n");
                    Gui.this.zoomInButton.setVisible(true);
                    Gui.this.zoomOutButton.setVisible(true);
                    TableConstructionIDU table = new TableConstructionIDU(Gui.this.globalDataKeeper);
					
                    final String[] columns=table.constructColumns();
					final String[][] rows=table.constructRows();
                    Gui.this.segmentSizeZoomArea = table.getSegmentSize();
                    System.out.println("Schemas: " + Gui.this.globalDataKeeper.getAllPPLSchemas().size());
					System.out.println("C: "+columns.length+" R: "+rows.length);
					tlp.writeToFile("[BT1 1] Show PLD button listener segmentSizeZoomArea => "+Arrays.toString(Gui.this.segmentSizeZoomArea));
                    Gui.this.finalColumnsZoomArea = columns;
                    tlp.writeToFile("[BT1 2] Show PLD button listener finalColumnsZoomArea => "+Arrays.toString(Gui.this.finalColumnsZoomArea));
                    Gui.this.finalRowsZoomArea = rows;
                    tlp.writeToFile("[BT1 3] Show PLD button listener finalRowsZoomArea => "+Arrays.deepToString(Gui.this.finalRowsZoomArea));
                    Gui.this.tabbedPane.setSelectedIndex(0);
                    Gui.this.makeGeneralTableIDU();
                    Gui.this.fillTree();
				}
				else{
					JOptionPane.showMessageDialog(null, "Select a Project first");
					return;
				}
			}
		});
		mnTable.add(mntmShowGeneralLifetimeIDU);
		
		JMenuItem mntmShowGeneralLifetimePhasesPLD = new JMenuItem("Show Phases PLD");
		mntmShowGeneralLifetimePhasesPLD.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

                if (!(Gui.this.project == null)) {
                    Gui.this.wholeCol = -1;
					ParametersJDialog jD=new ParametersJDialog(false);
					
					jD.setModal(true);
					
					
					jD.setVisible(true);
					
					if(jD.getConfirmation()){
						
                        Gui.this.timeWeight = jD.getTimeWeight();
                        Gui.this.changeWeight = jD.getChangeWeight();
                        Gui.this.preProcessingTime = jD.getPreProcessingTime();
                        Gui.this.preProcessingChange = jD.getPreProcessingChange();
                        Gui.this.numberOfPhases = jD.getNumberOfPhases();
                        tlp.updateOption();
                        tlp.writeToFile("### "+tlp.getOption()+" ###\n");
                        System.out.println(Gui.this.timeWeight + " " + Gui.this.changeWeight);
                        tlp.writeToFile("[BT2 1] Show Phases PLD button listener timeWeight => "+Gui.this.timeWeight);
                        tlp.writeToFile("[BT2 2] Show Phases PLD button listener changeWeight => "+Gui.this.changeWeight);
                        tlp.writeToFile("[BT2 3] Show Phases PLD button listener numberOfPhases => "+Gui.this.numberOfPhases);
                        PhaseAnalyzerMainEngine mainEngine = new PhaseAnalyzerMainEngine(Gui.this.inputCsv, Gui.this.outputAssessment1, Gui.this.outputAssessment2, Gui.this.timeWeight, Gui.this.changeWeight, Gui.this.preProcessingTime, Gui.this.preProcessingChange);
	
						mainEngine.parseInput();		
						System.out.println("\n\n\n");
                        mainEngine.extractPhases(Gui.this.numberOfPhases);

                        mainEngine.connectTransitionsWithPhases(Gui.this.globalDataKeeper);
                        Gui.this.globalDataKeeper.setPhaseCollectors(mainEngine.getPhaseCollectors());


                        if (Gui.this.globalDataKeeper.getPhaseCollectors().size() != 0) {
                            TableConstructionPhases table = new TableConstructionPhases(Gui.this.globalDataKeeper);
							final String[] columns=table.constructColumns();
							final String[][] rows=table.constructRows();
                            Gui.this.segmentSize = table.getSegmentSize();
                            tlp.writeToFile("[BT2 4] Show Phases PLD button listener segmentSize => "+Gui.this.segmentSize);
                            System.out.println("Schemas: " + Gui.this.globalDataKeeper.getAllPPLSchemas().size());
							System.out.println("C: "+columns.length+" R: "+rows.length);
							tlp.writeToFile("[BT2 5] Show Phases PLD button listener => "+"Schemas: " + Gui.this.globalDataKeeper.getAllPPLSchemas().size()+" "+"C: "+columns.length+" R: "+rows.length);
                            Gui.this.finalColumns = columns;
                            Gui.this.finalRows = rows;
                            tlp.writeToFile("[BT2 6] Show Phases PLD button listener finalColumns => "+Arrays.toString(Gui.this.finalColumns));
                            tlp.writeToFile("[BT2 7] Show Phases PLD button listener finalRows => "+Arrays.deepToString(Gui.this.finalRows));
                            Gui.this.tabbedPane.setSelectedIndex(0);
                            Gui.this.makeGeneralTablePhases();
                            Gui.this.fillPhasesTree();
                            
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
		});
		mnTable.add(mntmShowGeneralLifetimePhasesPLD);
		
		JMenuItem mntmShowGeneralLifetimePhasesWithClustersPLD = new JMenuItem("Show Phases With Clusters PLD");
		mntmShowGeneralLifetimePhasesWithClustersPLD.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
                Gui.this.wholeCol = -1;
                if (!(Gui.this.project == null)) {
					
					ParametersJDialog jD=new ParametersJDialog(true);
					
					jD.setModal(true);
					
					jD.setVisible(true);
					tlp.updateOption();
					tlp.writeToFile("### "+tlp.getOption()+" ###\n");
					if(jD.getConfirmation()){

                        Gui.this.timeWeight = jD.getTimeWeight();
                        Gui.this.changeWeight = jD.getChangeWeight();
                        Gui.this.preProcessingTime = jD.getPreProcessingTime();
                        Gui.this.preProcessingChange = jD.getPreProcessingChange();
                        Gui.this.numberOfPhases = jD.getNumberOfPhases();
                        Gui.this.numberOfClusters = jD.getNumberOfClusters();
                        Gui.this.birthWeight = jD.geBirthWeight();
                        Gui.this.deathWeight = jD.getDeathWeight();
                        Gui.this.changeWeightCl = jD.getChangeWeightCluster();
                        tlp.writeToFile("[BT3 1] Show Phases with Clusters PLD button listener timeWeight => "+Gui.this.timeWeight);
                        tlp.writeToFile("[BT3 2] Show Phases with Clusters PLD button listener changeWeight => "+Gui.this.changeWeight);
                        tlp.writeToFile("[BT3 3] Show Phases with Clusters PLD button listener numberOfPhases => "+Gui.this.numberOfPhases);
                        tlp.writeToFile("[BT3 4] Show Phases with Clusters PLD button listener numberOfClusters => "+Gui.this.numberOfClusters);
                        tlp.writeToFile("[BT3 5] Show Phases with Clusters PLD button listener birthWeight => "+Gui.this.birthWeight);
                        tlp.writeToFile("[BT3 6] Show Phases with Clusters PLD button listener deathWeight => "+Gui.this.deathWeight);
                        tlp.writeToFile("[BT3 7] Show Phases with Clusters PLD button listener changeWeightCl => "+Gui.this.changeWeightCl);
                        System.out.println(Gui.this.timeWeight + " " + Gui.this.changeWeight);

                        PhaseAnalyzerMainEngine mainEngine = new PhaseAnalyzerMainEngine(Gui.this.inputCsv, Gui.this.outputAssessment1, Gui.this.outputAssessment2, Gui.this.timeWeight, Gui.this.changeWeight, Gui.this.preProcessingTime, Gui.this.preProcessingChange);
						
						mainEngine.parseInput();		
						System.out.println("\n\n\n");
                        mainEngine.extractPhases(Gui.this.numberOfPhases);


                        mainEngine.connectTransitionsWithPhases(Gui.this.globalDataKeeper);
                        Gui.this.globalDataKeeper.setPhaseCollectors(mainEngine.getPhaseCollectors());
                        TableClusteringMainEngine mainEngine2 = new TableClusteringMainEngine(Gui.this.globalDataKeeper, Gui.this.birthWeight, Gui.this.deathWeight, Gui.this.changeWeightCl);
                        mainEngine2.extractClusters(Gui.this.numberOfClusters);
                        Gui.this.globalDataKeeper.setClusterCollectors(mainEngine2.getClusterCollectors());
						mainEngine2.print();

                        if (Gui.this.globalDataKeeper.getPhaseCollectors().size() != 0) {
                            TableConstructionWithClusters table = new TableConstructionWithClusters(Gui.this.globalDataKeeper);
							final String[] columns=table.constructColumns();
							final String[][] rows=table.constructRows();
                            Gui.this.segmentSize = table.getSegmentSize();
                            tlp.writeToFile("[BT3 8] Show Phases with Clusters PLD button listener segmentSize => "+Gui.this.segmentSize);
                            System.out.println("Schemas: " + Gui.this.globalDataKeeper.getAllPPLSchemas().size());
							System.out.println("C: "+columns.length+" R: "+rows.length);
							tlp.writeToFile("[BT3 9] Show Phases with Clusters PLD button listener => "+"Schemas: " + Gui.this.globalDataKeeper.getAllPPLSchemas().size()+"C: "+columns.length+" R: "+rows.length);
                            Gui.this.finalColumns = columns;
                            Gui.this.finalRows = rows;
                            tlp.writeToFile("[BT3 10] Show Phases with Clusters PLD button listener finalColumns => "+Arrays.toString(Gui.this.finalColumns));
                            tlp.writeToFile("[BT3 11] Show Phases with Clusters PLD button listener finalRows => "+Arrays.deepToString(Gui.this.finalRows));
                            Gui.this.tabbedPane.setSelectedIndex(0);
                            Gui.this.makeGeneralTablePhases();
                            Gui.this.fillClustersTree();
							

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
		});
		mnTable.add(mntmShowGeneralLifetimePhasesWithClustersPLD);
		
		
		mnTable.add(mntmShowLifetimeTable);


        this.sideMenu.setBounds(0, 0, 280, 600);
        this.sideMenu.setBackground(Color.DARK_GRAY);


        GroupLayout gl_sideMenu = new GroupLayout(this.sideMenu);
		gl_sideMenu.setHorizontalGroup(
                gl_sideMenu.createParallelGroup(GroupLayout.Alignment.LEADING)
		);
		gl_sideMenu.setVerticalGroup(
                gl_sideMenu.createParallelGroup(GroupLayout.Alignment.LEADING)
		);

        this.sideMenu.setLayout(gl_sideMenu);

        this.tablesTreePanel.setBounds(10, 400, 260, 180);
        this.tablesTreePanel.setBackground(Color.LIGHT_GRAY);

        GroupLayout gl_tablesTreePanel = new GroupLayout(this.tablesTreePanel);
		gl_tablesTreePanel.setHorizontalGroup(
                gl_tablesTreePanel.createParallelGroup(GroupLayout.Alignment.LEADING)
		);
		gl_tablesTreePanel.setVerticalGroup(
                gl_tablesTreePanel.createParallelGroup(GroupLayout.Alignment.LEADING)
		);

        this.tablesTreePanel.setLayout(gl_tablesTreePanel);

        this.treeLabel = new JLabel();
        this.treeLabel.setBounds(10, 370, 260, 40);
        this.treeLabel.setForeground(Color.WHITE);
        this.treeLabel.setText("Tree");

        this.descriptionPanel.setBounds(10, 190, 260, 180);
        this.descriptionPanel.setBackground(Color.LIGHT_GRAY);

        GroupLayout gl_descriptionPanel = new GroupLayout(this.descriptionPanel);
		gl_descriptionPanel.setHorizontalGroup(
                gl_descriptionPanel.createParallelGroup(GroupLayout.Alignment.LEADING)
		);
		gl_descriptionPanel.setVerticalGroup(
                gl_descriptionPanel.createParallelGroup(GroupLayout.Alignment.LEADING)
		);

        this.descriptionPanel.setLayout(gl_descriptionPanel);

        this.descriptionText = new JTextArea();
        this.descriptionText.setBounds(5, 5, 250, 170);
        this.descriptionText.setForeground(Color.BLACK);
        this.descriptionText.setText("");
        this.descriptionText.setBackground(Color.LIGHT_GRAY);

        this.descriptionPanel.add(this.descriptionText);


        this.descriptionLabel = new JLabel();
        this.descriptionLabel.setBounds(10, 160, 260, 40);
        this.descriptionLabel.setForeground(Color.WHITE);
        this.descriptionLabel.setText("Description");

        this.sideMenu.add(this.treeLabel);
        this.sideMenu.add(this.tablesTreePanel);

        this.sideMenu.add(this.descriptionLabel);
        this.sideMenu.add(this.descriptionPanel);

        this.lifeTimePanel.add(this.sideMenu);

		JButton buttonHelp=new JButton("Help");
		buttonHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String message ="To open a project, you must select a .txt file that contains the names ONLY of " +
									"the SQL files of the dataset that you want to use."+"\n" +
									"The .txt file must have EXACTLY the same name with the folder " +
									"that contains the DDL Scripts of the dataset."+ "\n" +
									"Both .txt file and dataset folder must be in the same folder.";
                JOptionPane.showMessageDialog(null, message);
			}
		});

        this.mnProject = new JMenu("Project");
        menuBar.add(this.mnProject);

        this.mntmInfo = new JMenuItem("Info");
        this.mntmInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {


                if (!(Gui.this.currentProject == null)) {


                    System.out.println("Project Name:" + Gui.this.projectName);
                    System.out.println("Dataset txt:" + Gui.this.datasetTxt);
                    System.out.println("Input Csv:" + Gui.this.inputCsv);
                    System.out.println("Output Assessment1:" + Gui.this.outputAssessment1);
                    System.out.println("Output Assessment2:" + Gui.this.outputAssessment2);
                    System.out.println("Transitions File:" + Gui.this.transitionsFile);

                    System.out.println("Schemas:" + Gui.this.globalDataKeeper.getAllPPLSchemas().size());
                    System.out.println("Transitions:" + Gui.this.globalDataKeeper.getAllPPLTransitions().size());
                    System.out.println("Tables:" + Gui.this.globalDataKeeper.getAllPPLTables().size());


                    ProjectInfoDialog infoDialog = new ProjectInfoDialog(Gui.this.projectName, Gui.this.datasetTxt, Gui.this.inputCsv, Gui.this.transitionsFile, Gui.this.globalDataKeeper.getAllPPLSchemas().size(),
                            Gui.this.globalDataKeeper.getAllPPLTransitions().size(), Gui.this.globalDataKeeper.getAllPPLTables().size());

					infoDialog.setVisible(true);
				}
				else{
					JOptionPane.showMessageDialog(null, "Select a Project first");
					return;
				}


            }
		});
        this.mnProject.add(this.mntmInfo);
		buttonHelp.setBounds(900,900 , 80, 40);
		menuBar.add(buttonHelp);


        this.contentPane = new JPanel();

        this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.setContentPane(this.contentPane);


        GroupLayout gl_contentPane = new GroupLayout(this.contentPane);
		gl_contentPane.setHorizontalGroup(
                gl_contentPane.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(this.tabbedPane, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 1474, Short.MAX_VALUE)
		);
		gl_contentPane.setVerticalGroup(
                gl_contentPane.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(this.tabbedPane, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 771, Short.MAX_VALUE)
		);


        this.tabbedPane.addTab("LifeTime Table", null, this.lifeTimePanel, null);

        GroupLayout gl_lifeTimePanel = new GroupLayout(this.lifeTimePanel);
		gl_lifeTimePanel.setHorizontalGroup(
                gl_lifeTimePanel.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGap(0, 1469, Short.MAX_VALUE)
		);
		gl_lifeTimePanel.setVerticalGroup(
                gl_lifeTimePanel.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGap(0, 743, Short.MAX_VALUE)
		);
        this.lifeTimePanel.setLayout(gl_lifeTimePanel);


        this.generalTableLabel = new JLabel("Parallel Lives Diagram");
        this.generalTableLabel.setBounds(300, 0, 150, 30);
        this.generalTableLabel.setForeground(Color.BLACK);

        this.zoomAreaLabel = new JLabel();
        this.zoomAreaLabel.setText("<HTML>Z<br>o<br>o<br>m<br><br>A<br>r<br>e<br>a</HTML>");
        this.zoomAreaLabel.setBounds(1255, 325, 15, 300);
        this.zoomAreaLabel.setForeground(Color.BLACK);

        this.zoomInButton = new JButton("Zoom In");
        this.zoomInButton.setBounds(1000, 560, 100, 30);


        this.zoomInButton.addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseClicked(MouseEvent e) {
                Gui.this.rowHeight = Gui.this.rowHeight + 2;
                Gui.this.columnWidth = Gui.this.columnWidth + 1;
                Gui.this.zoomAreaTable.setZoom(Gui.this.rowHeight, Gui.this.columnWidth);

			}
		});

        this.zoomOutButton = new JButton("Zoom Out");
        this.zoomOutButton.setBounds(1110, 560, 100, 30);

        this.zoomOutButton.addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseClicked(MouseEvent e) {
                Gui.this.rowHeight = Gui.this.rowHeight - 2;
                Gui.this.columnWidth = Gui.this.columnWidth - 1;
                if (Gui.this.rowHeight < 1) {
                    Gui.this.rowHeight = 1;
				}
                if (Gui.this.columnWidth < 1) {
                    Gui.this.columnWidth = 1;
				}
                Gui.this.zoomAreaTable.setZoom(Gui.this.rowHeight, Gui.this.columnWidth);

			}
		});

        this.zoomInButton.setVisible(false);
        this.zoomOutButton.setVisible(false);


        this.showThisToPopup = new JButton("Enlarge");
        this.showThisToPopup.setBounds(800, 560, 100, 30);

        this.showThisToPopup.addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseClicked(MouseEvent e) {

                EnlargeTable showEnlargmentPopup = new EnlargeTable(Gui.this.finalRowsZoomArea, Gui.this.finalColumnsZoomArea, Gui.this.segmentSizeZoomArea);
				showEnlargmentPopup.setBounds(100, 100, 1300, 700);

				showEnlargmentPopup.setVisible(true);


            }
		});

        this.showThisToPopup.setVisible(false);


        this.undoButton = new JButton("Undo");
        this.undoButton.setBounds(680, 560, 100, 30);

        this.undoButton.addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseClicked(MouseEvent e) {
                if (Gui.this.firstLevelUndoColumnsZoomArea != null) {
                    Gui.this.finalColumnsZoomArea = Gui.this.firstLevelUndoColumnsZoomArea;
                    Gui.this.finalRowsZoomArea = Gui.this.firstLevelUndoRowsZoomArea;
                    Gui.this.makeZoomAreaTableForCluster();
				}

			}
		});

        this.undoButton.setVisible(false);


        this.uniformlyDistributedButton = new JButton("Same Width");
        this.uniformlyDistributedButton.setBounds(980, 0, 120, 30);

        this.uniformlyDistributedButton.addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseClicked(MouseEvent e) {
                Gui.this.LifeTimeTable.uniformlyDistributed(1);

            }
		});

        this.uniformlyDistributedButton.setVisible(false);

        this.notUniformlyDistributedButton = new JButton("Over Time");
        this.notUniformlyDistributedButton.setBounds(1100, 0, 120, 30);

        this.notUniformlyDistributedButton.addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseClicked(MouseEvent e) {
                Gui.this.LifeTimeTable.notUniformlyDistributed(Gui.this.globalDataKeeper);

            }
		});

        this.notUniformlyDistributedButton.setVisible(false);

        this.lifeTimePanel.add(this.zoomInButton);
        this.lifeTimePanel.add(this.undoButton);
        this.lifeTimePanel.add(this.zoomOutButton);
        this.lifeTimePanel.add(this.uniformlyDistributedButton);
        this.lifeTimePanel.add(this.notUniformlyDistributedButton);
        this.lifeTimePanel.add(this.showThisToPopup);

        this.lifeTimePanel.add(this.zoomAreaLabel);

        this.lifeTimePanel.add(this.generalTableLabel);

        this.contentPane.setLayout(gl_contentPane);

        this.pack();
        this.setBounds(30, 30, 1300, 700);


    }


	private void makeGeneralTableIDU() {

        PldRowSorter sorter = new PldRowSorter(this.finalRowsZoomArea, this.globalDataKeeper);

        this.finalRowsZoomArea = sorter.sortRows();
        tlp.writeToFile("[P 5] makeGeneralTableIDU() finalRowsZoomArea => "+Arrays.deepToString( this.finalRowsZoomArea));
        this.showingPld = true;
        this.zoomInButton.setVisible(true);
        this.zoomOutButton.setVisible(true);

        this.showThisToPopup.setVisible(true);

        int numberOfColumns = this.finalRowsZoomArea[0].length;
        int numberOfRows = this.finalRowsZoomArea.length;

        this.selectedRows = new ArrayList<Integer>();

		String[][] rows=new String[numberOfRows][numberOfColumns];

		for(int i=0; i<numberOfRows; i++){

            rows[i][0] = this.finalRowsZoomArea[i][0];

        }
		tlp.writeToFile("[P 6] makeGeneralTableIDU() finalColumnsZoomArea  to zoomModel => "+ Arrays.toString( this.finalColumnsZoomArea));
		tlp.writeToFile("[P 7] makeGeneralTableIDU() rows to zoomModel => "+ Arrays.deepToString( rows));
        this.zoomModel = new MyTableModel(this.finalColumnsZoomArea, rows);

        final JvTable generalTable = new JvTable(this.zoomModel);
       
        tlp.writeObjectToFile(tlp.getOption()+"generalTable-makeGeneralTableIDU.txt", generalTable);
		generalTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        if (this.rowHeight < 1) {
            this.rowHeight = 1;
        }
        if (this.columnWidth < 1) {
            this.columnWidth = 1;
        }

		for(int i=0; i<generalTable.getRowCount(); i++){
            generalTable.setRowHeight(i, this.rowHeight);

		}


		generalTable.setShowGrid(false);
		generalTable.setIntercellSpacing(new Dimension(0, 0));


        for(int i=0; i<generalTable.getColumnCount(); i++){
			if(i==0){
                generalTable.getColumnModel().getColumn(0).setPreferredWidth(this.columnWidth);

			}
			else{
                generalTable.getColumnModel().getColumn(i).setPreferredWidth(this.columnWidth);

			}
		}

		int start=-1;
		int end=-1;
        if (this.globalDataKeeper.getPhaseCollectors() != null && this.wholeCol != -1 && this.wholeCol != 0) {
            start = this.globalDataKeeper.getPhaseCollectors().get(0).getPhases().get(this.wholeCol - 1).getStartPos();
            end = this.globalDataKeeper.getPhaseCollectors().get(0).getPhases().get(this.wholeCol - 1).getEndPos();
		}


        if (this.wholeCol != -1) {
			for(int i=0; i<generalTable.getColumnCount(); i++){
				if(!(generalTable.getColumnName(i).equals("Table name"))){
					if(Integer.parseInt(generalTable.getColumnName(i))>=start && Integer.parseInt(generalTable.getColumnName(i))<=end){

						generalTable.getColumnModel().getColumn(i).setHeaderRenderer(new IDUHeaderTableRenderer());

					}
				}
			}
		}
       
        final IDUTableRenderer renderer = new IDUTableRenderer(Gui.this, this.finalRowsZoomArea, this.globalDataKeeper, this.segmentSize);

       
		generalTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer()
		{

			private static final long serialVersionUID = 1L;

			@Override
		    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		    {
		        final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                String tmpValue = Gui.this.finalRowsZoomArea[row][column];
		        String columnName=table.getColumnName(column);
		        Color fr=new Color(0,0,0);

		        c.setForeground(fr);
                this.setOpaque(true);
                //bottom table column selection
                if (column == Gui.this.wholeColZoomArea && Gui.this.wholeColZoomArea != 0) {

		        	String description="Transition ID:"+table.getColumnName(column)+"\n";
                    description = description + "Old Version Name:" + Gui.this.globalDataKeeper.getAllPPLTransitions().
	        				get(Integer.parseInt(table.getColumnName(column))).getOldVersionName()+"\n";
                    description = description + "New Version Name:" + Gui.this.globalDataKeeper.getAllPPLTransitions().
                            get(Integer.parseInt(table.getColumnName(column))).getNewVersionName() + "\n";

                    description = description + "Transition Changes:" + Gui.this.globalDataKeeper.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getNumberOfChangesForOneTr() + "\n";
                    description = description + "Additions:" + Gui.this.globalDataKeeper.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getNumberOfAdditionsForOneTr() + "\n";
                    description = description + "Deletions:" + Gui.this.globalDataKeeper.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getNumberOfDeletionsForOneTr() + "\n";
                    description = description + "Updates:" + Gui.this.globalDataKeeper.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getNumberOfUpdatesForOneTr() + "\n";

                    Gui.this.descriptionText.setText(description);
                    
                    String descriptionStatic="Transition ID:"+table.getColumnName(5)+"\n";
                    descriptionStatic = descriptionStatic + "Old Version Name:" + Gui.this.globalDataKeeper.getAllPPLTransitions().
	        				get(Integer.parseInt(table.getColumnName(5))).getOldVersionName()+"\n";
                    descriptionStatic = descriptionStatic + "New Version Name:" + Gui.this.globalDataKeeper.getAllPPLTransitions().
                            get(Integer.parseInt(table.getColumnName(5))).getNewVersionName() + "\n";

                    descriptionStatic = descriptionStatic + "Transition Changes:" + Gui.this.globalDataKeeper.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(5))).getNumberOfChangesForOneTr() + "\n";
                    descriptionStatic = descriptionStatic + "Additions:" + Gui.this.globalDataKeeper.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(5))).getNumberOfAdditionsForOneTr() + "\n";
                    descriptionStatic = descriptionStatic + "Deletions:" + Gui.this.globalDataKeeper.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(5))).getNumberOfDeletionsForOneTr() + "\n";
                    descriptionStatic = descriptionStatic + "Updates:" + Gui.this.globalDataKeeper.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(5))).getNumberOfUpdatesForOneTr() + "\n";
                    
                    tlp.writeToFile("On Bottom Table Column (5) description makeGeneralTableIDU  => "+ descriptionStatic);


		        	Color cl = new Color(255,69,0,100);

	        		c.setBackground(cl);
	        		return c;
	        		
		        } else if (Gui.this.selectedColumnZoomArea == 0) { //bottom table row selection

		        	if (isSelected){
		        		Color cl = new Color(255,69,0,100);
		        		c.setBackground(cl);

                        String description = "Table:" + Gui.this.finalRowsZoomArea[row][0] + "\n";
                        description = description + "Birth Version Name:" + Gui.this.globalDataKeeper.getAllPPLTables().get(Gui.this.finalRowsZoomArea[row][0]).getBirth() + "\n";
                        description = description + "Birth Version ID:" + Gui.this.globalDataKeeper.getAllPPLTables().get(Gui.this.finalRowsZoomArea[row][0]).getBirthVersionID() + "\n";
                        description = description + "Death Version Name:" + Gui.this.globalDataKeeper.getAllPPLTables().get(Gui.this.finalRowsZoomArea[row][0]).getDeath() + "\n";
                        description = description + "Death Version ID:" + Gui.this.globalDataKeeper.getAllPPLTables().get(Gui.this.finalRowsZoomArea[row][0]).getDeathVersionID() + "\n";
                        description = description + "Total Changes:" + Gui.this.globalDataKeeper.getAllPPLTables().get(Gui.this.finalRowsZoomArea[row][0]).getTotalChanges() + "\n";
                        Gui.this.descriptionText.setText(description);
                        
                        String descriptionStatic = "Table:" + Gui.this.finalRowsZoomArea[5][0] + "\n";
                        descriptionStatic = descriptionStatic + "Birth Version Name:" + Gui.this.globalDataKeeper.getAllPPLTables().get(Gui.this.finalRowsZoomArea[5][0]).getBirth() + "\n";
                        descriptionStatic = descriptionStatic + "Birth Version ID:" + Gui.this.globalDataKeeper.getAllPPLTables().get(Gui.this.finalRowsZoomArea[5][0]).getBirthVersionID() + "\n";
                        descriptionStatic = descriptionStatic + "Death Version Name:" + Gui.this.globalDataKeeper.getAllPPLTables().get(Gui.this.finalRowsZoomArea[5][0]).getDeath() + "\n";
                        descriptionStatic = descriptionStatic + "Death Version ID:" + Gui.this.globalDataKeeper.getAllPPLTables().get(Gui.this.finalRowsZoomArea[5][0]).getDeathVersionID() + "\n";
                        descriptionStatic = descriptionStatic + "Total Changes:" + Gui.this.globalDataKeeper.getAllPPLTables().get(Gui.this.finalRowsZoomArea[5][0]).getTotalChanges() + "\n";

                        tlp.writeToFile("On Bottom Table row (5) description makeGeneralTableIDU  => "+ descriptionStatic);
		        		return c;


                    }
		        }
		        else{


                    if (Gui.this.selectedFromTree.contains(Gui.this.finalRowsZoomArea[row][0])) {


		        		Color cl = new Color(255,69,0,100);

		        		c.setBackground(cl);

		        		return c;
		        	}

                    //bottom table click cell  
                    if (isSelected && hasFocus){

		        		String description="";
		        		String descriptionStatic="";
		        		if(!table.getColumnName(column).contains("Table name")){
                            description = "Table:" + Gui.this.finalRowsZoomArea[row][0] + "\n";

                            description = description + "Old Version Name:" + Gui.this.globalDataKeeper.getAllPPLTransitions().
			        				get(Integer.parseInt(table.getColumnName(column))).getOldVersionName()+"\n";
                            description = description + "New Version Name:" + Gui.this.globalDataKeeper.getAllPPLTransitions().
                                    get(Integer.parseInt(table.getColumnName(column))).getNewVersionName() + "\n";
                            
                            descriptionStatic = "Table:" + Gui.this.finalRowsZoomArea[5][0] + "\n";

                            descriptionStatic = descriptionStatic + "Old Version Name:" + Gui.this.globalDataKeeper.getAllPPLTransitions().
			        				get(Integer.parseInt(table.getColumnName(5))).getOldVersionName()+"\n";
                            descriptionStatic = descriptionStatic + "New Version Name:" + Gui.this.globalDataKeeper.getAllPPLTransitions().
                                    get(Integer.parseInt(table.getColumnName(5))).getNewVersionName() + "\n";
                            
                            if (Gui.this.globalDataKeeper.getAllPPLTables().get(Gui.this.finalRowsZoomArea[row][0]).
			        				getTableChanges().getTableAtChForOneTransition(Integer.parseInt(table.getColumnName(column)))!=null){
                                description = description + "Transition Changes:" + Gui.this.globalDataKeeper.getAllPPLTables().get(Gui.this.finalRowsZoomArea[row][0]).
			        				getTableChanges().getTableAtChForOneTransition(Integer.parseInt(table.getColumnName(column))).size()+"\n";
                                description = description + "Additions:" + Gui.this.globalDataKeeper.getAllPPLTables().get(Gui.this.finalRowsZoomArea[row][0]).
			        					getNumberOfAdditionsForOneTr(Integer.parseInt(table.getColumnName(column)))+"\n";
                                description = description + "Deletions:" + Gui.this.globalDataKeeper.getAllPPLTables().get(Gui.this.finalRowsZoomArea[row][0]).
			        					getNumberOfDeletionsForOneTr(Integer.parseInt(table.getColumnName(column)))+"\n";
                                description = description + "Updates:" + Gui.this.globalDataKeeper.getAllPPLTables().get(Gui.this.finalRowsZoomArea[row][0]).
			        					getNumberOfUpdatesForOneTr(Integer.parseInt(table.getColumnName(column)))+"\n";

                                descriptionStatic = descriptionStatic + "Transition Changes:" + Gui.this.globalDataKeeper.getAllPPLTables().get(Gui.this.finalRowsZoomArea[5][0]).
    			        				getTableChanges().getTableAtChForOneTransition(Integer.parseInt(table.getColumnName(5))).size()+"\n";
                                    descriptionStatic = descriptionStatic + "Additions:" + Gui.this.globalDataKeeper.getAllPPLTables().get(Gui.this.finalRowsZoomArea[5][0]).
    			        					getNumberOfAdditionsForOneTr(Integer.parseInt(table.getColumnName(5)))+"\n";
                                    descriptionStatic = descriptionStatic + "Deletions:" + Gui.this.globalDataKeeper.getAllPPLTables().get(Gui.this.finalRowsZoomArea[5][0]).
    			        					getNumberOfDeletionsForOneTr(Integer.parseInt(table.getColumnName(5)))+"\n";
                                    descriptionStatic = descriptionStatic + "Updates:" + Gui.this.globalDataKeeper.getAllPPLTables().get(Gui.this.finalRowsZoomArea[5][0]).
    			        					getNumberOfUpdatesForOneTr(Integer.parseInt(table.getColumnName(5)))+"\n";
			        		}
			        		else{
			        			description=description+"Transition Changes:0"+"\n";
			        			description=description+"Additions:0"+"\n";
			        			description=description+"Deletions:0"+"\n";
			        			description=description+"Updates:0"+"\n";

			        		}

                            Gui.this.descriptionText.setText(description);
                            tlp.writeToFile("On Bottom Table cell (5,5) description makeGeneralTableIDU  => "+ descriptionStatic);
		        		}
		        		Color cl = new Color(255,69,0,100);

		        		c.setBackground(cl);

		        		return c;
			        }


                }

		        try{
		        	int numericValue=Integer.parseInt(tmpValue);
		        	Color insersionColor=null;
                    this.setToolTipText(Integer.toString(numericValue));


	        		if(numericValue==0){
	        			insersionColor=new Color(154,205,50,200);
	        		} else if (numericValue > 0 && numericValue <= Gui.this.segmentSizeZoomArea[3]) {

	        			insersionColor=new Color(176,226,255);
		        	} else if (numericValue > Gui.this.segmentSizeZoomArea[3] && numericValue <= 2 * Gui.this.segmentSizeZoomArea[3]) {
	        			insersionColor=new Color(92,172,238);
	        		} else if (numericValue > 2 * Gui.this.segmentSizeZoomArea[3] && numericValue <= 3 * Gui.this.segmentSizeZoomArea[3]) {

	        			insersionColor=new Color(28,134,238);
	        		}
	        		else{
	        			insersionColor=new Color(16,78,139);
	        		}
	        		c.setBackground(insersionColor);

		        	return c;
		        }
		        catch(Exception e){


	        		if(tmpValue.equals("")){
	        			c.setBackground(Color.GRAY);
                        return c;
	        		}
	        		else{
	        			if(columnName.contains("v")){
	        				c.setBackground(Color.lightGray);
                            this.setToolTipText(columnName);
	        			}
	        			else{
	        				Color tableNameColor=new Color(205,175,149);
	        				c.setBackground(tableNameColor);
	        			}
                        return c;
	        		}


                }
		    }
		});

		generalTable.addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseClicked(MouseEvent e) {

				if (e.getClickCount() == 1) {
					JTable target = (JTable)e.getSource();

                    Gui.this.selectedRowsFromMouse = target.getSelectedRows();
                    Gui.this.selectedColumnZoomArea = target.getSelectedColumn();
                    renderer.setSelCol(Gui.this.selectedColumnZoomArea);
			         target.getSelectedColumns();

                    Gui.this.zoomAreaTable.repaint();
				}

			  }
		});

		generalTable.addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseReleased(MouseEvent e) {

					if(SwingUtilities.isRightMouseButton(e)){
						System.out.println("Right Click");

						JTable target1 = (JTable)e.getSource();
						target1.getSelectedColumns();
                        Gui.this.selectedRowsFromMouse = target1.getSelectedRows();
						System.out.println(target1.getSelectedColumns().length);
						System.out.println(target1.getSelectedRow());
                        for (int rowsSelected = 0; rowsSelected < Gui.this.selectedRowsFromMouse.length; rowsSelected++) {
                            System.out.println(generalTable.getValueAt(Gui.this.selectedRowsFromMouse[rowsSelected], 0));
						}
						final JPopupMenu popupMenu = new JPopupMenu();
				        JMenuItem showDetailsItem = new JMenuItem("Clear Selection");
				        showDetailsItem.addActionListener(new ActionListener() {

				            @Override
				            public void actionPerformed(ActionEvent e) {
                                Gui.this.selectedFromTree = new ArrayList<String>();
                                Gui.this.zoomAreaTable.repaint();
				            }
				        });
				        popupMenu.add(showDetailsItem);
				        popupMenu.show(generalTable, e.getX(),e.getY());


                    }

			   }
		});


        generalTable.getTableHeader().addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
                Gui.this.wholeColZoomArea = generalTable.columnAtPoint(e.getPoint());
		        renderer.setWholeCol(generalTable.columnAtPoint(e.getPoint()));
		        generalTable.repaint();
		    }
		});

		generalTable.getTableHeader().addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseReleased(MouseEvent e) {
		    	if(SwingUtilities.isRightMouseButton(e)){
					System.out.println("Right Click");

							final JPopupMenu popupMenu = new JPopupMenu();
					        JMenuItem showDetailsItem = new JMenuItem("Clear Column Selection");
					        showDetailsItem.addActionListener(new ActionListener() {

					            @Override
					            public void actionPerformed(ActionEvent e) {
                                    Gui.this.wholeColZoomArea = -1;
                                    renderer.setWholeCol(Gui.this.wholeColZoomArea);

					            	generalTable.repaint();
					            }
					        });
					        popupMenu.add(showDetailsItem);
					        popupMenu.show(generalTable, e.getX(),e.getY());

				}

		   }

		});

        this.zoomAreaTable = generalTable;
        this.tmpScrollPaneZoomArea.setViewportView(this.zoomAreaTable);
        this.tmpScrollPaneZoomArea.setAlignmentX(0);
        this.tmpScrollPaneZoomArea.setAlignmentY(0);
        this.tmpScrollPaneZoomArea.setBounds(300, 300, 950, 250);
        this.tmpScrollPaneZoomArea.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        this.tmpScrollPaneZoomArea.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        this.lifeTimePanel.setCursor(this.getCursor());
        this.lifeTimePanel.add(this.tmpScrollPaneZoomArea);



	}

private void makeGeneralTablePhases() {
    this.uniformlyDistributedButton.setVisible(true);

    this.notUniformlyDistributedButton.setVisible(true);

    int numberOfColumns = this.finalRows[0].length;
    int numberOfRows = this.finalRows.length;

    this.selectedRows = new ArrayList<Integer>();

	String[][] rows=new String[numberOfRows][numberOfColumns];

	for(int i=0; i<numberOfRows; i++){

        rows[i][0] = this.finalRows[i][0];

    }

    this.generalModel = new MyTableModel(this.finalColumns, rows);

    final JvTable generalTable = new JvTable(this.generalModel);
   
	generalTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

	generalTable.setShowGrid(false);
	generalTable.setIntercellSpacing(new Dimension(0, 0));
    


    for(int i=0; i<generalTable.getColumnCount(); i++){
		if(i==0){
			generalTable.getColumnModel().getColumn(0).setPreferredWidth(86);

		}
		else{

			generalTable.getColumnModel().getColumn(i).setPreferredWidth(1);

		}
	}
   
	generalTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer()
	{

		private static final long serialVersionUID = 1L;

		@Override
	    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	    {
	        final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            String tmpValue = Gui.this.finalRows[row][column];
	        String columnName=table.getColumnName(column);
	        Color fr=new Color(0,0,0);
	        c.setForeground(fr);

	        //top table column selection
            if (column == Gui.this.wholeCol && Gui.this.wholeCol != 0) {

	        	String description=table.getColumnName(column)+"\n";
                description = description + "First Transition ID:" + Gui.this.globalDataKeeper.getPhaseCollectors().get(0).getPhases().
        				get(column-1).getStartPos()+"\n";
                description = description + "Last Transition ID:" + Gui.this.globalDataKeeper.getPhaseCollectors().get(0).getPhases().
        				get(column-1).getEndPos()+"\n";
                description = description + "Total Changes For This Phase:" + Gui.this.globalDataKeeper.getPhaseCollectors().get(0).getPhases().
        				get(column-1).getTotalUpdates()+"\n";
                description = description + "Additions For This Phase:" + Gui.this.globalDataKeeper.getPhaseCollectors().get(0).getPhases().
        				get(column-1).getTotalAdditionsOfPhase()+"\n";
                description = description + "Deletions For This Phase:" + Gui.this.globalDataKeeper.getPhaseCollectors().get(0).getPhases().
        				get(column-1).getTotalDeletionsOfPhase()+"\n";
                description = description + "Updates For This Phase:" + Gui.this.globalDataKeeper.getPhaseCollectors().get(0).getPhases().
        				get(column-1).getTotalUpdatesOfPhase()+"\n";

                Gui.this.descriptionText.setText(description);
                
                String descriptionStatic=table.getColumnName(5)+"\n";
                descriptionStatic = descriptionStatic + "First Transition ID:" + Gui.this.globalDataKeeper.getPhaseCollectors().get(0).getPhases().
        				get(5-1).getStartPos()+"\n";
                descriptionStatic = descriptionStatic + "Last Transition ID:" + Gui.this.globalDataKeeper.getPhaseCollectors().get(0).getPhases().
        				get(5-1).getEndPos()+"\n";
                descriptionStatic = descriptionStatic + "Total Changes For This Phase:" + Gui.this.globalDataKeeper.getPhaseCollectors().get(0).getPhases().
        				get(5-1).getTotalUpdates()+"\n";
                descriptionStatic = descriptionStatic + "Additions For This Phase:" + Gui.this.globalDataKeeper.getPhaseCollectors().get(0).getPhases().
        				get(5-1).getTotalAdditionsOfPhase()+"\n";
                descriptionStatic = descriptionStatic + "Deletions For This Phase:" + Gui.this.globalDataKeeper.getPhaseCollectors().get(0).getPhases().
        				get(5-1).getTotalDeletionsOfPhase()+"\n";
                descriptionStatic = descriptionStatic + "Updates For This Phase:" + Gui.this.globalDataKeeper.getPhaseCollectors().get(0).getPhases().
        				get(5-1).getTotalUpdatesOfPhase()+"\n";
                
                tlp.writeToFile("On Top Table Column (5) description makeGeneralTablePhases  => "+ descriptionStatic);
	        	Color cl = new Color(255,69,0,100);

        		c.setBackground(cl);
        		return c;
        		//top table row selection
	        } else if (Gui.this.selectedColumn == 0) {
	        	if (isSelected){
	        		//row selection cluster case
                    if (Gui.this.finalRows[row][0].contains("Cluster")) {
                        String description = "Cluster:" + Gui.this.finalRows[row][0] + "\n";
                        description = description + "Birth Version Name:" + Gui.this.globalDataKeeper.getClusterCollectors().get(0).getClusters().get(row).getBirthSqlFile() + "\n";
                        description = description + "Birth Version ID:" + Gui.this.globalDataKeeper.getClusterCollectors().get(0).getClusters().get(row).getBirth() + "\n";
                        description = description + "Death Version Name:" + Gui.this.globalDataKeeper.getClusterCollectors().get(0).getClusters().get(row).getDeathSqlFile() + "\n";
                        description = description + "Death Version ID:" + Gui.this.globalDataKeeper.getClusterCollectors().get(0).getClusters().get(row).getDeath() + "\n";
                        description = description + "Tables:" + Gui.this.globalDataKeeper.getClusterCollectors().get(0).getClusters().get(row).getNamesOfTables().size() + "\n";
                        description = description + "Total Changes:" + Gui.this.globalDataKeeper.getClusterCollectors().get(0).getClusters().get(row).getTotalChanges() + "\n";
                        
                        Gui.this.descriptionText.setText(description);
                        
                        String descriptionStatic = "Cluster:" + Gui.this.finalRows[5][0] + "\n";
                        descriptionStatic = descriptionStatic + "Birth Version Name:" + Gui.this.globalDataKeeper.getClusterCollectors().get(0).getClusters().get(5).getBirthSqlFile() + "\n";
                        descriptionStatic = descriptionStatic + "Birth Version ID:" + Gui.this.globalDataKeeper.getClusterCollectors().get(0).getClusters().get(5).getBirth() + "\n";
                        descriptionStatic = descriptionStatic + "Death Version Name:" + Gui.this.globalDataKeeper.getClusterCollectors().get(0).getClusters().get(5).getDeathSqlFile() + "\n";
                        descriptionStatic = descriptionStatic + "Death Version ID:" + Gui.this.globalDataKeeper.getClusterCollectors().get(0).getClusters().get(5).getDeath() + "\n";
                        descriptionStatic = descriptionStatic + "Tables:" + Gui.this.globalDataKeeper.getClusterCollectors().get(0).getClusters().get(5).getNamesOfTables().size() + "\n";
                        descriptionStatic = descriptionStatic + "Total Changes:" + Gui.this.globalDataKeeper.getClusterCollectors().get(0).getClusters().get(5).getTotalChanges() + "\n";
                        tlp.writeToFile("On Top Table row (5) [cluster] description makeGeneralTablePhases  => "+ descriptionStatic);
                       
	        		}
	        		else{// row selection table case
                        String description = "Table:" + Gui.this.finalRows[row][0] + "\n";
                        description = description + "Birth Version Name:" + Gui.this.globalDataKeeper.getAllPPLTables().get(Gui.this.finalRows[row][0]).getBirth() + "\n";
                        description = description + "Birth Version ID:" + Gui.this.globalDataKeeper.getAllPPLTables().get(Gui.this.finalRows[row][0]).getBirthVersionID() + "\n";
                        description = description + "Death Version Name:" + Gui.this.globalDataKeeper.getAllPPLTables().get(Gui.this.finalRows[row][0]).getDeath() + "\n";
                        description = description + "Death Version ID:" + Gui.this.globalDataKeeper.getAllPPLTables().get(Gui.this.finalRows[row][0]).getDeathVersionID() + "\n";
                        description = description + "Total Changes:" + Gui.this.globalDataKeeper.getAllPPLTables().get(Gui.this.finalRows[row][0]).getTotalChanges() + "\n";
                        Gui.this.descriptionText.setText(description);
                        
                        String descriptionStatic = "Table:" + Gui.this.finalRows[row][0] + "\n";
                        descriptionStatic = descriptionStatic + "Birth Version Name:" + Gui.this.globalDataKeeper.getAllPPLTables().get(Gui.this.finalRows[row][0]).getBirth() + "\n";
                        descriptionStatic = descriptionStatic + "Birth Version ID:" + Gui.this.globalDataKeeper.getAllPPLTables().get(Gui.this.finalRows[row][0]).getBirthVersionID() + "\n";
                        descriptionStatic = descriptionStatic + "Death Version Name:" + Gui.this.globalDataKeeper.getAllPPLTables().get(Gui.this.finalRows[row][0]).getDeath() + "\n";
                        descriptionStatic = descriptionStatic + "Death Version ID:" + Gui.this.globalDataKeeper.getAllPPLTables().get(Gui.this.finalRows[row][0]).getDeathVersionID() + "\n";
                        descriptionStatic = descriptionStatic + "Total Changes:" + Gui.this.globalDataKeeper.getAllPPLTables().get(Gui.this.finalRows[row][0]).getTotalChanges() + "\n";
                        tlp.writeToFile("On Top Table row (5) [table] description makeGeneralTablePhases  => "+ descriptionStatic);
                       

	        		}


                    Color cl = new Color(255,69,0,100);

	        		c.setBackground(cl);
	        		return c;
	        	}
	        }
	        else{

                if (Gui.this.selectedFromTree.contains(Gui.this.finalRows[row][0])) {

	        		Color cl = new Color(255,69,0,100);

	        		c.setBackground(cl);

	        		return c;
	        	}
                //On top table cell selection
	        	if (isSelected && hasFocus){

	        		String description="";
	        		//cell selection cluster case
	        		if(!table.getColumnName(column).contains("Table name")){

                        if (Gui.this.finalRows[row][0].contains("Cluster")) {

                            description = Gui.this.finalRows[row][0] + "\n";
                            description = description + "Tables:" + Gui.this.globalDataKeeper.getClusterCollectors().get(0).getClusters().get(row).getNamesOfTables().size() + "\n\n";

			        		description=description+table.getColumnName(column)+"\n";
                            description = description + "First Transition ID:" + Gui.this.globalDataKeeper.getPhaseCollectors().get(0).getPhases().
			        				get(column-1).getStartPos()+"\n";
                            description = description + "Last Transition ID:" + Gui.this.globalDataKeeper.getPhaseCollectors().get(0).getPhases().
			        				get(column-1).getEndPos()+"\n\n";
			        		description=description+"totTotal Changes For This Phase:"+tmpValue+"\n";
			        		
			        		String descriptionStatic="";
			        		descriptionStatic = Gui.this.finalRows[5][0] + "\n";
			        		descriptionStatic = descriptionStatic + "Tables:" + Gui.this.globalDataKeeper.getClusterCollectors().get(0).getClusters().get(5).getNamesOfTables().size() + "\n\n";

			        		descriptionStatic=descriptionStatic+table.getColumnName(5)+"\n";
			        		descriptionStatic=descriptionStatic + "First Transition ID:" + Gui.this.globalDataKeeper.getPhaseCollectors().get(0).getPhases().
			        				get(5-1).getStartPos()+"\n";
			        		descriptionStatic=descriptionStatic + "Last Transition ID:" + Gui.this.globalDataKeeper.getPhaseCollectors().get(0).getPhases().
			        				get(5-1).getEndPos()+"\n\n";
			        		descriptionStatic=descriptionStatic+"Total Changes For This Phase:"+tmpValue+"\n";
			        		tlp.writeToFile("On Top Table cell (5,5) [cluster] description makeGeneralTablePhases  => "+ descriptionStatic);
		        		}
		        		else{//each cell table case
		        			description=table.getColumnName(column)+"\n";
                            description = description + "First Transition ID:" + Gui.this.globalDataKeeper.getPhaseCollectors().get(0).getPhases().
			        				get(column-1).getStartPos()+"\n";
                            description = description + "Last Transition ID:" + Gui.this.globalDataKeeper.getPhaseCollectors().get(0).getPhases().
			        				get(column-1).getEndPos()+"\n\n";
                            description = description + "Table:" + Gui.this.finalRows[row][0] + "\n";
                            description = description + "Birth Version Name:" + Gui.this.globalDataKeeper.getAllPPLTables().get(Gui.this.finalRows[row][0]).getBirth() + "\n";
                            description = description + "Birth Version ID:" + Gui.this.globalDataKeeper.getAllPPLTables().get(Gui.this.finalRows[row][0]).getBirthVersionID() + "\n";
                            description = description + "Death Version Name:" + Gui.this.globalDataKeeper.getAllPPLTables().get(Gui.this.finalRows[row][0]).getDeath() + "\n";
                            description = description + "Death Version ID:" + Gui.this.globalDataKeeper.getAllPPLTables().get(Gui.this.finalRows[row][0]).getDeathVersionID() + "\n";
			        		description=description+"Total Changes For This Phase:"+tmpValue+"\n";
			        		
			        		String descriptionStatic="";
			        		descriptionStatic=table.getColumnName(5)+"\n";
			        		descriptionStatic = descriptionStatic + "First Transition ID:" + Gui.this.globalDataKeeper.getPhaseCollectors().get(0).getPhases().
			        				get(5-1).getStartPos()+"\n";
			        		descriptionStatic = descriptionStatic + "Last Transition ID:" + Gui.this.globalDataKeeper.getPhaseCollectors().get(0).getPhases().
			        				get(5-1).getEndPos()+"\n\n";
			        		descriptionStatic = descriptionStatic + "Table:" + Gui.this.finalRows[5][0] + "\n";
			        		descriptionStatic = descriptionStatic + "Birth Version Name:" + Gui.this.globalDataKeeper.getAllPPLTables().get(Gui.this.finalRows[5][0]).getBirth() + "\n";
			        		descriptionStatic = descriptionStatic + "Birth Version ID:" + Gui.this.globalDataKeeper.getAllPPLTables().get(Gui.this.finalRows[5][0]).getBirthVersionID() + "\n";
			        		descriptionStatic = descriptionStatic + "Death Version Name:" + Gui.this.globalDataKeeper.getAllPPLTables().get(Gui.this.finalRows[5][0]).getDeath() + "\n";
			        		descriptionStatic = descriptionStatic + "Death Version ID:" + Gui.this.globalDataKeeper.getAllPPLTables().get(Gui.this.finalRows[5][0]).getDeathVersionID() + "\n";
			        		
			        		tlp.writeToFile("On Top Table cell (5,5) [table] description makeGeneralTablePhases  => "+ descriptionStatic);
		        		}

                        Gui.this.descriptionText.setText(description);

	        		}
	        		
	        		Color cl = new Color(255,69,0,100);

	        		c.setBackground(cl);
	        		return c;
		        }

	        }


	        try{
	        	int numericValue=Integer.parseInt(tmpValue);
	        	Color insersionColor=null;
                this.setToolTipText(Integer.toString(numericValue));


        		if(numericValue==0){
        			insersionColor=new Color(154,205,50,200);
        		} else if (numericValue > 0 && numericValue <= Gui.this.segmentSize[3]) {

        			insersionColor=new Color(176,226,255);
	        	} else if (numericValue > Gui.this.segmentSize[3] && numericValue <= 2 * Gui.this.segmentSize[3]) {
        			insersionColor=new Color(92,172,238);
        		} else if (numericValue > 2 * Gui.this.segmentSize[3] && numericValue <= 3 * Gui.this.segmentSize[3]) {

        			insersionColor=new Color(28,134,238);
        		}
        		else{
        			insersionColor=new Color(16,78,139);
        		}
        		c.setBackground(insersionColor);

	        	return c;
	        }
	        catch(Exception e){


                if(tmpValue.equals("")){
        			c.setBackground(Color.gray);
                    return c;
        		}
        		else{
        			if(columnName.contains("v")){
        				c.setBackground(Color.lightGray);
                        this.setToolTipText(columnName);
        			}
        			else{
        				Color tableNameColor=new Color(205,175,149);
        				c.setBackground(tableNameColor);
        			}
                    return c;
        		}


            }
	    }
	});
	
	generalTable.addMouseListener(new MouseAdapter() {
		@Override
		   public void mouseClicked(MouseEvent e) {

			if (e.getClickCount() == 1) {
				JTable target = (JTable)e.getSource();
                Gui.this.selectedRowsFromMouse = target.getSelectedRows();
                Gui.this.selectedColumn = target.getSelectedColumn();
                Gui.this.LifeTimeTable.repaint();
			}

		   }
	});

	generalTable.addMouseListener(new MouseAdapter() {
		@Override
		   public void mouseReleased(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON3){
					System.out.println("Right Click");

					JTable target1 = (JTable)e.getSource();
                    Gui.this.selectedColumn = target1.getSelectedColumn();
                    Gui.this.selectedRowsFromMouse = new int[target1.getSelectedRows().length];
                    Gui.this.selectedRowsFromMouse = target1.getSelectedRows();

					final String sSelectedRow = (String) generalTable.getValueAt(target1.getSelectedRow(),0);
                    Gui.this.tablesSelected = new ArrayList<String>();

                    for (int rowsSelected = 0; rowsSelected < Gui.this.selectedRowsFromMouse.length; rowsSelected++) {
                        Gui.this.tablesSelected.add((String) generalTable.getValueAt(Gui.this.selectedRowsFromMouse[rowsSelected], 0));
					}

					JPopupMenu popupMenu = new JPopupMenu();
			        JMenuItem showDetailsItem = new JMenuItem("Show Details for the selection");
			        showDetailsItem.addActionListener(new ActionListener() {

			            @Override
			            public void actionPerformed(ActionEvent le) {
			            	if(sSelectedRow.contains("Cluster ")){
                                Gui.this.showClusterSelectionToZoomArea(Gui.this.selectedColumn, sSelectedRow);

			            	}
			            	else{
                                Gui.this.showSelectionToZoomArea(Gui.this.selectedColumn);
			            	}
			            }
			        });
			        popupMenu.add(showDetailsItem);
			        JMenuItem clearSelectionItem = new JMenuItem("Clear Selection");
			        clearSelectionItem.addActionListener(new ActionListener() {

			            @Override
			            public void actionPerformed(ActionEvent le) {

                            Gui.this.selectedFromTree = new ArrayList<String>();
                            Gui.this.LifeTimeTable.repaint();
			            }
			        });
			        popupMenu.add(clearSelectionItem);
			        popupMenu.show(generalTable, e.getX(),e.getY());

				}

		   }
	});

	generalTable.getTableHeader().addMouseListener(new MouseAdapter() {
	    @Override
	    public void mouseClicked(MouseEvent e) {
            Gui.this.wholeCol = generalTable.columnAtPoint(e.getPoint());
            String name = generalTable.getColumnName(Gui.this.wholeCol);
            System.out.println("Column index selected " + Gui.this.wholeCol + " " + name);
	        generalTable.repaint();
            if (Gui.this.showingPld) {
                Gui.this.makeGeneralTableIDU();
			}
	    }
	});

	generalTable.getTableHeader().addMouseListener(new MouseAdapter() {
	    @Override
	    public void mouseReleased(MouseEvent e) {
	    	if(SwingUtilities.isRightMouseButton(e)){
				System.out.println("Right Click");

						final JPopupMenu popupMenu = new JPopupMenu();
				        JMenuItem clearColumnSelectionItem = new JMenuItem("Clear Column Selection");
				        clearColumnSelectionItem.addActionListener(new ActionListener() {

				            @Override
				            public void actionPerformed(ActionEvent e) {
                                Gui.this.wholeCol = -1;
				            	generalTable.repaint();
                                if (Gui.this.showingPld) {
                                    Gui.this.makeGeneralTableIDU();
				            	}
				            }
				        });
				        popupMenu.add(clearColumnSelectionItem);
				        JMenuItem showDetailsItem = new JMenuItem("Show Details for this Phase");
				        showDetailsItem.addActionListener(new ActionListener() {

				            @Override
				            public void actionPerformed(ActionEvent e) {
                                String sSelectedRow = Gui.this.finalRows[0][0];
								System.out.println("?"+sSelectedRow);
                                Gui.this.tablesSelected = new ArrayList<String>();
                                for (int i = 0; i < Gui.this.finalRows.length; i++)
                                    Gui.this.tablesSelected.add((String) generalTable.getValueAt(i, 0));

				            	if(!sSelectedRow.contains("Cluster ")){

                                    Gui.this.showSelectionToZoomArea(Gui.this.wholeCol);
				            	}
				            	else{
                                    Gui.this.showClusterSelectionToZoomArea(Gui.this.wholeCol, "");
				            	}

				            }
				        });
				        popupMenu.add(showDetailsItem);
				        popupMenu.show(generalTable, e.getX(),e.getY());

			}

	   }

	});

	tlp.writeObjectToFile(tlp.getOption()+"generalTable-makeGeneralTablePhases.txt", generalTable);
    this.LifeTimeTable = generalTable;

    this.tmpScrollPane.setViewportView(this.LifeTimeTable);
    this.tmpScrollPane.setAlignmentX(0);
    this.tmpScrollPane.setAlignmentY(0);
    this.tmpScrollPane.setBounds(300, 30, 950, 265);
    this.tmpScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    this.tmpScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

    this.lifeTimePanel.setCursor(this.getCursor());
    this.lifeTimePanel.add(this.tmpScrollPane);



}



private void showSelectionToZoomArea(int selectedColumn){

    TableConstructionZoomArea table = new TableConstructionZoomArea(this.globalDataKeeper, this.tablesSelected, selectedColumn);
	final String[] columns=table.constructColumns();
	final String[][] rows=table.constructRows();
    this.segmentSizeZoomArea = table.getSegmentSize();

    System.out.println("Schemas: " + this.globalDataKeeper.getAllPPLSchemas().size());
	System.out.println("C: "+columns.length+" R: "+rows.length);

    this.finalColumnsZoomArea = columns;
    this.finalRowsZoomArea = rows;
    this.tabbedPane.setSelectedIndex(0);
    this.makeZoomAreaTable();



}

private void showClusterSelectionToZoomArea(int selectedColumn,String selectedCluster){


	ArrayList<String> tablesOfCluster=new ArrayList<String>();
    for (int i = 0; i < this.tablesSelected.size(); i++) {
        String[] selectedClusterSplit = this.tablesSelected.get(i).split(" ");
		int cluster=Integer.parseInt(selectedClusterSplit[1]);
        ArrayList<String> namesOfTables = this.globalDataKeeper.getClusterCollectors().get(0).getClusters().get(cluster).getNamesOfTables();
		for(int j=0; j<namesOfTables.size(); j++){
			tablesOfCluster.add(namesOfTables.get(j));
		}
        System.out.println(this.tablesSelected.get(i));
    }

	PldConstruction table;
	if (selectedColumn==0) {
        table = new TableConstructionClusterTablesPhasesZoomA(this.globalDataKeeper, tablesOfCluster);
	}
	else{
        table = new TableConstructionZoomArea(this.globalDataKeeper, tablesOfCluster, selectedColumn);
	}
	final String[] columns=table.constructColumns();
	final String[][] rows=table.constructRows();
    this.segmentSizeZoomArea = table.getSegmentSize();
    System.out.println("Schemas: " + this.globalDataKeeper.getAllPPLSchemas().size());
	System.out.println("C: "+columns.length+" R: "+rows.length);

    this.finalColumnsZoomArea = columns;
    this.finalRowsZoomArea = rows;
    this.tabbedPane.setSelectedIndex(0);
    this.makeZoomAreaTableForCluster();


}

private void makeZoomAreaTable() {
    this.showingPld = false;
    int numberOfColumns = this.finalRowsZoomArea[0].length;
    int numberOfRows = this.finalRowsZoomArea.length;


	final String[][] rowsZoom=new String[numberOfRows][numberOfColumns];

	for(int i=0; i<numberOfRows; i++){

        rowsZoom[i][0] = this.finalRowsZoomArea[i][0];

    }

    this.zoomModel = new MyTableModel(this.finalColumnsZoomArea, rowsZoom);

    final JvTable zoomTable = new JvTable(this.zoomModel);

	zoomTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);


	zoomTable.setShowGrid(false);
	zoomTable.setIntercellSpacing(new Dimension(0, 0));


    for(int i=0; i<zoomTable.getColumnCount(); i++){
		if(i==0){
			zoomTable.getColumnModel().getColumn(0).setPreferredWidth(150);

		}
		else{


            zoomTable.getColumnModel().getColumn(i).setPreferredWidth(20);
			zoomTable.getColumnModel().getColumn(i).setMaxWidth(20);
			zoomTable.getColumnModel().getColumn(i).setMinWidth(20);
		}
	}

	zoomTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer()
	{

		private static final long serialVersionUID = 1L;

		@Override
	    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	    {
	        final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);


            String tmpValue = Gui.this.finalRowsZoomArea[row][column];
	        String columnName=table.getColumnName(column);
	        Color fr=new Color(0,0,0);
	        c.setForeground(fr);

            if (column == Gui.this.wholeColZoomArea) {

	        	String description="Transition ID:"+table.getColumnName(column)+"\n";
                description = description + "Old Version Name:" + Gui.this.globalDataKeeper.getAllPPLTransitions().
        				get(Integer.parseInt(table.getColumnName(column))).getOldVersionName()+"\n";
                description = description + "New Version Name:" + Gui.this.globalDataKeeper.getAllPPLTransitions().
                        get(Integer.parseInt(table.getColumnName(column))).getNewVersionName() + "\n";

                description = description + "Transition Changes:" + Gui.this.globalDataKeeper.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getNumberOfClusterChangesForOneTr(rowsZoom) + "\n";
                description = description + "Additions:" + Gui.this.globalDataKeeper.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getNumberOfClusterAdditionsForOneTr(rowsZoom) + "\n";
                description = description + "Deletions:" + Gui.this.globalDataKeeper.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getNumberOfClusterDeletionsForOneTr(rowsZoom) + "\n";
                description = description + "Updates:" + Gui.this.globalDataKeeper.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getNumberOfClusterUpdatesForOneTr(rowsZoom) + "\n";


                Gui.this.descriptionText.setText(description);
	        	Color cl = new Color(255,69,0,100);
        		c.setBackground(cl);

        		return c;
	        } else if (Gui.this.selectedColumnZoomArea == 0) {
	        	if (isSelected){
                    String description = "Table:" + Gui.this.finalRowsZoomArea[row][0] + "\n";
                    description = description + "Birth Version Name:" + Gui.this.globalDataKeeper.getAllPPLTables().get(Gui.this.finalRowsZoomArea[row][0]).getBirth() + "\n";
                    description = description + "Birth Version ID:" + Gui.this.globalDataKeeper.getAllPPLTables().get(Gui.this.finalRowsZoomArea[row][0]).getBirthVersionID() + "\n";
                    description = description + "Death Version Name:" + Gui.this.globalDataKeeper.getAllPPLTables().get(Gui.this.finalRowsZoomArea[row][0]).getDeath() + "\n";
                    description = description + "Death Version ID:" + Gui.this.globalDataKeeper.getAllPPLTables().get(Gui.this.finalRowsZoomArea[row][0]).getDeathVersionID() + "\n";
                    description = description + "Total Changes:" + Gui.this.globalDataKeeper.getAllPPLTables().get(Gui.this.finalRowsZoomArea[row][0]).
	        				getTotalChangesForOnePhase(Integer.parseInt(table.getColumnName(1)), Integer.parseInt(table.getColumnName(table.getColumnCount()-1)))+"\n";
                    Gui.this.descriptionText.setText(description);

	        		Color cl = new Color(255,69,0,100);

	        		c.setBackground(cl);
	        		return c;
	        	}
	        }
	        else{
	        	if (isSelected && hasFocus){

	        		String description="";
	        		if(!table.getColumnName(column).contains("Table name")){
                        description = "Table:" + Gui.this.finalRowsZoomArea[row][0] + "\n";

                        description = description + "Old Version Name:" + Gui.this.globalDataKeeper.getAllPPLTransitions().
		        				get(Integer.parseInt(table.getColumnName(column))).getOldVersionName()+"\n";
                        description = description + "New Version Name:" + Gui.this.globalDataKeeper.getAllPPLTransitions().
                                get(Integer.parseInt(table.getColumnName(column))).getNewVersionName() + "\n";
                        if (Gui.this.globalDataKeeper.getAllPPLTables().get(Gui.this.finalRowsZoomArea[row][0]).
		        				getTableChanges().getTableAtChForOneTransition(Integer.parseInt(table.getColumnName(column)))!=null){
                            description = description + "Transition Changes:" + Gui.this.globalDataKeeper.getAllPPLTables().get(Gui.this.finalRowsZoomArea[row][0]).
		        				getTableChanges().getTableAtChForOneTransition(Integer.parseInt(table.getColumnName(column))).size()+"\n";
                            description = description + "Additions:" + Gui.this.globalDataKeeper.getAllPPLTables().get(Gui.this.finalRowsZoomArea[row][0]).
		        					getNumberOfAdditionsForOneTr(Integer.parseInt(table.getColumnName(column)))+"\n";
                            description = description + "Deletions:" + Gui.this.globalDataKeeper.getAllPPLTables().get(Gui.this.finalRowsZoomArea[row][0]).
		        					getNumberOfDeletionsForOneTr(Integer.parseInt(table.getColumnName(column)))+"\n";
                            description = description + "Updates:" + Gui.this.globalDataKeeper.getAllPPLTables().get(Gui.this.finalRowsZoomArea[row][0]).
		        					getNumberOfUpdatesForOneTr(Integer.parseInt(table.getColumnName(column)))+"\n";

		        		}
		        		else{
		        			description=description+"Transition Changes:0"+"\n";
		        			description=description+"Additions:0"+"\n";
		        			description=description+"Deletions:0"+"\n";
		        			description=description+"Updates:0"+"\n";

		        		}

                        Gui.this.descriptionText.setText(description);
	        		}

	        		Color cl = new Color(255,69,0,100);

	        		c.setBackground(cl);
	        		return c;
		        }

	        }


	        try{
	        	int numericValue=Integer.parseInt(tmpValue);
	        	Color insersionColor=null;
                this.setToolTipText(Integer.toString(numericValue));


        		if(numericValue==0){
        			insersionColor=new Color(0,100,0);
        		} else if (numericValue > 0 && numericValue <= Gui.this.segmentSizeZoomArea[3]) {

        			insersionColor=new Color(176,226,255);
	        	} else if (numericValue > Gui.this.segmentSizeZoomArea[3] && numericValue <= 2 * Gui.this.segmentSizeZoomArea[3]) {
        			insersionColor=new Color(92,172,238);
        		} else if (numericValue > 2 * Gui.this.segmentSizeZoomArea[3] && numericValue <= 3 * Gui.this.segmentSizeZoomArea[3]) {

        			insersionColor=new Color(28,134,238);
        		}
        		else{
        			insersionColor=new Color(16,78,139);
        		}
        		c.setBackground(insersionColor);

	        	return c;
	        }
	        catch(Exception e){


                if(tmpValue.equals("")){
        			c.setBackground(Color.DARK_GRAY);
                    return c;
        		}
        		else{
        			if(columnName.contains("v")){
        				c.setBackground(Color.lightGray);
                        this.setToolTipText(columnName);
        			}
        			else{
        				Color tableNameColor=new Color(205,175,149);
        				c.setBackground(tableNameColor);
        			}
                    return c;
        		}


            }
	    }
	});

	zoomTable.addMouseListener(new MouseAdapter() {
		@Override
		   public void mouseClicked(MouseEvent e) {

			if (e.getClickCount() == 1) {
				JTable target = (JTable)e.getSource();

                Gui.this.selectedRowsFromMouse = target.getSelectedRows();
                Gui.this.selectedColumnZoomArea = target.getSelectedColumn();
                Gui.this.zoomAreaTable.repaint();
			}

		   }
	});

	zoomTable.addMouseListener(new MouseAdapter() {
		@Override
		   public void mouseReleased(MouseEvent e) {

				if(SwingUtilities.isRightMouseButton(e)){
					System.out.println("Right Click");

					JTable target1 = (JTable)e.getSource();
                    Gui.this.selectedColumnZoomArea = target1.getSelectedColumn();
                    Gui.this.selectedRowsFromMouse = target1.getSelectedRows();
					System.out.println(target1.getSelectedColumn());
					System.out.println(target1.getSelectedRow());
					final ArrayList<String> tablesSelected = new ArrayList<String>();
                    for (int rowsSelected = 0; rowsSelected < Gui.this.selectedRowsFromMouse.length; rowsSelected++) {
                        tablesSelected.add((String) zoomTable.getValueAt(Gui.this.selectedRowsFromMouse[rowsSelected], 0));
						System.out.println(tablesSelected.get(rowsSelected));
					}


                }

		   }
	});

	// listener
	zoomTable.getTableHeader().addMouseListener(new MouseAdapter() {
	    @Override
	    public void mouseClicked(MouseEvent e) {
            Gui.this.wholeColZoomArea = zoomTable.columnAtPoint(e.getPoint());
            String name = zoomTable.getColumnName(Gui.this.wholeColZoomArea);
            System.out.println("Column index selected " + Gui.this.wholeCol + " " + name);
	        zoomTable.repaint();
	    }
	});

	zoomTable.getTableHeader().addMouseListener(new MouseAdapter() {
	    @Override
	    public void mouseReleased(MouseEvent e) {
	    	if(SwingUtilities.isRightMouseButton(e)){
				System.out.println("Right Click");

						final JPopupMenu popupMenu = new JPopupMenu();
				        JMenuItem showDetailsItem = new JMenuItem("Clear Column Selection");
				        showDetailsItem.addActionListener(new ActionListener() {

				            @Override
				            public void actionPerformed(ActionEvent e) {
                                Gui.this.wholeColZoomArea = -1;
				            	zoomTable.repaint();
				            }
				        });
				        popupMenu.add(showDetailsItem);
				        popupMenu.show(zoomTable, e.getX(),e.getY());

			}

	   }

	});


    this.zoomAreaTable = zoomTable;

    this.tmpScrollPaneZoomArea.setViewportView(this.zoomAreaTable);
    this.tmpScrollPaneZoomArea.setAlignmentX(0);
    this.tmpScrollPaneZoomArea.setAlignmentY(0);
    this.tmpScrollPaneZoomArea.setBounds(300, 300, 950, 250);
    this.tmpScrollPaneZoomArea.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    this.tmpScrollPaneZoomArea.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);


    this.lifeTimePanel.setCursor(this.getCursor());
    this.lifeTimePanel.add(this.tmpScrollPaneZoomArea);



}

private void makeZoomAreaTableForCluster() {
    this.showingPld = false;
    int numberOfColumns = this.finalRowsZoomArea[0].length;
    int numberOfRows = this.finalRowsZoomArea.length;
    this.undoButton.setVisible(true);

	final String[][] rowsZoom=new String[numberOfRows][numberOfColumns];

	for(int i=0; i<numberOfRows; i++){

        rowsZoom[i][0] = this.finalRowsZoomArea[i][0];

    }

    this.zoomModel = new MyTableModel(this.finalColumnsZoomArea, rowsZoom);

    final JvTable zoomTable = new JvTable(this.zoomModel);

	zoomTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);


    zoomTable.setShowGrid(false);
	zoomTable.setIntercellSpacing(new Dimension(0, 0));


    for(int i=0; i<zoomTable.getColumnCount(); i++){
		if(i==0){
			zoomTable.getColumnModel().getColumn(0).setPreferredWidth(150);

		}
		else{

			zoomTable.getColumnModel().getColumn(i).setPreferredWidth(10);
			zoomTable.getColumnModel().getColumn(i).setMaxWidth(10);
			zoomTable.getColumnModel().getColumn(i).setMinWidth(70);
		}
	}

	zoomTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer()
	{

		private static final long serialVersionUID = 1L;

		@Override
	    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	    {
	        final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);


            String tmpValue = Gui.this.finalRowsZoomArea[row][column];
	        String columnName=table.getColumnName(column);
	        Color fr=new Color(0,0,0);
	        c.setForeground(fr);

	        //column tou panw pinaka
            if (column == Gui.this.wholeColZoomArea && Gui.this.wholeColZoomArea != 0) {

	        	String description=table.getColumnName(column)+"\n";
                description = description + "First Transition ID:" + Gui.this.globalDataKeeper.getPhaseCollectors().get(0).getPhases().
        				get(column-1).getStartPos()+"\n";
                description = description + "Last Transition ID:" + Gui.this.globalDataKeeper.getPhaseCollectors().get(0).getPhases().
        				get(column-1).getEndPos()+"\n";
                description = description + "Total Changes For This Phase:" + Gui.this.globalDataKeeper.getPhaseCollectors().get(0).getPhases().
        				get(column-1).getTotalUpdates()+"\n";
                description = description + "Additions For This Phase:" + Gui.this.globalDataKeeper.getPhaseCollectors().get(0).getPhases().
        				get(column-1).getTotalAdditionsOfPhase()+"\n";
                description = description + "Deletions For This Phase:" + Gui.this.globalDataKeeper.getPhaseCollectors().get(0).getPhases().
        				get(column-1).getTotalDeletionsOfPhase()+"\n";
                description = description + "Updates For This Phase:" + Gui.this.globalDataKeeper.getPhaseCollectors().get(0).getPhases().
        				get(column-1).getTotalUpdatesOfPhase()+"\n";

                Gui.this.descriptionText.setText(description);

	        	Color cl = new Color(255,69,0,100);

        		c.setBackground(cl);
        		return c;
	        } else if (Gui.this.selectedColumnZoomArea == 0) {
	        	if (isSelected){


                    String description = "Table:" + Gui.this.finalRowsZoomArea[row][0] + "\n";
                    description = description + "Birth Version Name:" + Gui.this.globalDataKeeper.getAllPPLTables().get(Gui.this.finalRowsZoomArea[row][0]).getBirth() + "\n";
                    description = description + "Birth Version ID:" + Gui.this.globalDataKeeper.getAllPPLTables().get(Gui.this.finalRowsZoomArea[row][0]).getBirthVersionID() + "\n";
                    description = description + "Death Version Name:" + Gui.this.globalDataKeeper.getAllPPLTables().get(Gui.this.finalRowsZoomArea[row][0]).getDeath() + "\n";
                    description = description + "Death Version ID:" + Gui.this.globalDataKeeper.getAllPPLTables().get(Gui.this.finalRowsZoomArea[row][0]).getDeathVersionID() + "\n";
                    description = description + "Total Changes:" + Gui.this.globalDataKeeper.getAllPPLTables().get(Gui.this.finalRowsZoomArea[row][0]).getTotalChanges() + "\n";
                    Gui.this.descriptionText.setText(description);



	        		Color cl = new Color(255,69,0,100);

	        		c.setBackground(cl);
	        		return c;
	        	}
	        }
	        else{


                if (isSelected && hasFocus){

	        		String description="";
	        		if(!table.getColumnName(column).contains("Table name")){


                        description="Transition "+table.getColumnName(column)+"\n";

                        description = description + "Old Version:" + Gui.this.globalDataKeeper.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getOldVersionName() + "\n";
                        description = description + "New Version:" + Gui.this.globalDataKeeper.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getNewVersionName() + "\n\n";

	        			//description=description+"First Transition ID:"+globalDataKeeper.getPhaseCollectors().get(0).getPhases().
		        				//get(column-1).getStartPos()+"\n";
		        		//description=description+"Last Transition ID:"+globalDataKeeper.getPhaseCollectors().get(0).getPhases().
		        			//	get(column-1).getEndPos()+"\n\n";
                        description = description + "Table:" + Gui.this.finalRowsZoomArea[row][0] + "\n";
                        description = description + "Birth Version Name:" + Gui.this.globalDataKeeper.getAllPPLTables().get(Gui.this.finalRowsZoomArea[row][0]).getBirth() + "\n";
                        description = description + "Birth Version ID:" + Gui.this.globalDataKeeper.getAllPPLTables().get(Gui.this.finalRowsZoomArea[row][0]).getBirthVersionID() + "\n";
                        description = description + "Death Version Name:" + Gui.this.globalDataKeeper.getAllPPLTables().get(Gui.this.finalRowsZoomArea[row][0]).getDeath() + "\n";
                        description = description + "Death Version ID:" + Gui.this.globalDataKeeper.getAllPPLTables().get(Gui.this.finalRowsZoomArea[row][0]).getDeathVersionID() + "\n";
		        		description=description+"Total Changes For This Phase:"+tmpValue+"\n";

                        Gui.this.descriptionText.setText(description);

	        		}

                    Color cl = new Color(255,69,0,100);

                    c.setBackground(cl);
	        		return c;
		        }

            }


	        try{
	        	int numericValue=Integer.parseInt(tmpValue);
	        	Color insersionColor=null;
                this.setToolTipText(Integer.toString(numericValue));


        		if(numericValue==0){
        			insersionColor=new Color(0,100,0);
        		} else if (numericValue > 0 && numericValue <= Gui.this.segmentSizeZoomArea[3]) {

        			insersionColor=new Color(176,226,255);
	        	} else if (numericValue > Gui.this.segmentSizeZoomArea[3] && numericValue <= 2 * Gui.this.segmentSizeZoomArea[3]) {
        			insersionColor=new Color(92,172,238);
        		} else if (numericValue > 2 * Gui.this.segmentSizeZoomArea[3] && numericValue <= 3 * Gui.this.segmentSizeZoomArea[3]) {

        			insersionColor=new Color(28,134,238);
        		}
        		else{
        			insersionColor=new Color(16,78,139);
        		}
        		c.setBackground(insersionColor);

                return c;
	        }
	        catch(Exception e){


                if(tmpValue.equals("")){
        			c.setBackground(Color.DARK_GRAY);
                    return c;
        		}
        		else{
        			if(columnName.contains("v")){
        				c.setBackground(Color.lightGray);
                        this.setToolTipText(columnName);
        			}
        			else{
        				Color tableNameColor=new Color(205,175,149);
        				c.setBackground(tableNameColor);
        			}
                    return c;
        		}


            }
	    }
	});

    zoomTable.addMouseListener(new MouseAdapter() {
		@Override
		   public void mouseClicked(MouseEvent e) {

            if (e.getClickCount() == 1) {
				JTable target = (JTable)e.getSource();

                Gui.this.selectedRowsFromMouse = target.getSelectedRows();
                Gui.this.selectedColumnZoomArea = target.getSelectedColumn();
                Gui.this.zoomAreaTable.repaint();
			}

        }
	});

    zoomTable.addMouseListener(new MouseAdapter() {
		@Override
		   public void mouseReleased(MouseEvent e) {

            if(SwingUtilities.isRightMouseButton(e)){
					System.out.println("Right Click");

						JTable target1 = (JTable)e.getSource();
                Gui.this.selectedColumnZoomArea = target1.getSelectedColumn();
                Gui.this.selectedRowsFromMouse = target1.getSelectedRows();
						System.out.println(target1.getSelectedColumn());
						System.out.println(target1.getSelectedRow());

                Gui.this.tablesSelected = new ArrayList<String>();

                for (int rowsSelected = 0; rowsSelected < Gui.this.selectedRowsFromMouse.length; rowsSelected++) {
                    Gui.this.tablesSelected.add((String) zoomTable.getValueAt(Gui.this.selectedRowsFromMouse[rowsSelected], 0));
                    System.out.println(Gui.this.tablesSelected.get(rowsSelected));
						}
                if (zoomTable.getColumnName(Gui.this.selectedColumnZoomArea).contains("Phase")) {

							final JPopupMenu popupMenu = new JPopupMenu();
					        JMenuItem showDetailsItem = new JMenuItem("Show Details");
					        showDetailsItem.addActionListener(new ActionListener() {

					            @Override
					            public void actionPerformed(ActionEvent e) {
                                    Gui.this.firstLevelUndoColumnsZoomArea = Gui.this.finalColumnsZoomArea;
                                    Gui.this.firstLevelUndoRowsZoomArea = Gui.this.finalRowsZoomArea;
                                    Gui.this.showSelectionToZoomArea(Gui.this.selectedColumnZoomArea);


					            }
					        });
					        popupMenu.add(showDetailsItem);
					        popupMenu.show(zoomTable, e.getX(),e.getY());
		            	}


            }

        }
	});

    // listener
	zoomTable.getTableHeader().addMouseListener(new MouseAdapter() {
	    @Override
	    public void mouseClicked(MouseEvent e) {
            Gui.this.wholeColZoomArea = zoomTable.columnAtPoint(e.getPoint());
            String name = zoomTable.getColumnName(Gui.this.wholeColZoomArea);
            System.out.println("Column index selected " + Gui.this.wholeCol + " " + name);
	        zoomTable.repaint();
	    }
	});

    zoomTable.getTableHeader().addMouseListener(new MouseAdapter() {
	    @Override
	    public void mouseReleased(MouseEvent e) {
	    	if(SwingUtilities.isRightMouseButton(e)){
				System.out.println("Right Click");

                final JPopupMenu popupMenu = new JPopupMenu();
				        JMenuItem showDetailsItem = new JMenuItem("Clear Column Selection");
				        showDetailsItem.addActionListener(new ActionListener() {

				            @Override
				            public void actionPerformed(ActionEvent e) {
                                Gui.this.wholeColZoomArea = -1;
				            	zoomTable.repaint();
				            }
				        });
				        popupMenu.add(showDetailsItem);
				        popupMenu.show(zoomTable, e.getX(),e.getY());

            }

        }

    });


    this.zoomAreaTable = zoomTable;

    this.tmpScrollPaneZoomArea.setViewportView(this.zoomAreaTable);
    this.tmpScrollPaneZoomArea.setAlignmentX(0);
    this.tmpScrollPaneZoomArea.setAlignmentY(0);
    this.tmpScrollPaneZoomArea.setBounds(300, 300, 950, 250);
    this.tmpScrollPaneZoomArea.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    this.tmpScrollPaneZoomArea.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);


    this.lifeTimePanel.setCursor(this.getCursor());
    this.lifeTimePanel.add(this.tmpScrollPaneZoomArea);



}

	private void makeDetailedTable(String[] columns , String[][] rows, final boolean levelized){

        this.detailedModel = new MyTableModel(columns, rows);

        final JvTable tmpLifeTimeTable = new JvTable(this.detailedModel);

		tmpLifeTimeTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        if(levelized==true){
			for(int i=0; i<tmpLifeTimeTable.getColumnCount(); i++){
				if(i==0){
					tmpLifeTimeTable.getColumnModel().getColumn(0).setPreferredWidth(150);
					tmpLifeTimeTable.getColumnModel().getColumn(0).setMaxWidth(150);
					tmpLifeTimeTable.getColumnModel().getColumn(0).setMinWidth(150);
				}
				else{
					if(tmpLifeTimeTable.getColumnName(i).contains("v")){
						tmpLifeTimeTable.getColumnModel().getColumn(i).setPreferredWidth(100);
						tmpLifeTimeTable.getColumnModel().getColumn(i).setMaxWidth(100);
						tmpLifeTimeTable.getColumnModel().getColumn(i).setMinWidth(100);
					}
					else{
						tmpLifeTimeTable.getColumnModel().getColumn(i).setPreferredWidth(25);
						tmpLifeTimeTable.getColumnModel().getColumn(i).setMaxWidth(25);
						tmpLifeTimeTable.getColumnModel().getColumn(i).setMinWidth(25);
					}
				}
			}
		}
		else{
			for(int i=0; i<tmpLifeTimeTable.getColumnCount(); i++){
				if(i==0){
					tmpLifeTimeTable.getColumnModel().getColumn(0).setPreferredWidth(150);
					tmpLifeTimeTable.getColumnModel().getColumn(0).setMaxWidth(150);
					tmpLifeTimeTable.getColumnModel().getColumn(0).setMinWidth(150);
				}
				else{

                    tmpLifeTimeTable.getColumnModel().getColumn(i).setPreferredWidth(20);
					tmpLifeTimeTable.getColumnModel().getColumn(i).setMaxWidth(20);
					tmpLifeTimeTable.getColumnModel().getColumn(i).setMinWidth(20);

                }
			}
		}

        tmpLifeTimeTable.setName("LifeTimeTable");


        tmpLifeTimeTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer()
		{

            private static final long serialVersionUID = 1L;

			@Override
		    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		    {
		        final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                String tmpValue=(String) table.getValueAt(row, column);
		        String columnName=table.getColumnName(column);
		        Color fr=new Color(0,0,0);
		        c.setForeground(fr);


                if (Gui.this.selectedColumn == 0) {
		        	if (isSelected){
		        		Color cl = new Color(255,69,0, 100);

		        		c.setBackground(cl);

                        return c;
		        	}
		        }
		        else{
		        	if (isSelected && hasFocus){

                        c.setBackground(Color.YELLOW);
		        		return c;
			        }

                }

                try{
		        	int numericValue=Integer.parseInt(tmpValue);
		        	Color insersionColor=null;

                    if(columnName.equals("I")){
		        		if(numericValue==0){
		        			insersionColor=new Color(255,231,186);
		        		} else if (numericValue > 0 && numericValue <= Gui.this.segmentSizeDetailedTable[0]) {

		        			insersionColor=new Color(193,255,193);
			        	} else if (numericValue > Gui.this.segmentSizeDetailedTable[0] && numericValue <= 2 * Gui.this.segmentSizeDetailedTable[0]) {
		        			insersionColor=new Color(84,255,159);
		        		} else if (numericValue > 2 * Gui.this.segmentSizeDetailedTable[0] && numericValue <= 3 * Gui.this.segmentSizeDetailedTable[0]) {

		        			insersionColor=new Color(0,201,87);
		        		}
		        		else{
		        			insersionColor=new Color(0,100,0);
		        		}
		        		c.setBackground(insersionColor);
		        	}

                    if(columnName.equals("U")){
		        		if(numericValue==0){
		        			insersionColor=new Color(255,231,186);
		        		} else if (numericValue > 0 && numericValue <= Gui.this.segmentSizeDetailedTable[1]) {

		        			insersionColor=new Color(176,226,255);
			        	} else if (numericValue > Gui.this.segmentSizeDetailedTable[1] && numericValue <= 2 * Gui.this.segmentSizeDetailedTable[1]) {
		        			insersionColor=new Color(92,172,238);
		        		} else if (numericValue > 2 * Gui.this.segmentSizeDetailedTable[1] && numericValue <= 3 * Gui.this.segmentSizeDetailedTable[1]) {

		        			insersionColor=new Color(28,134,238);
		        		}
		        		else{
		        			insersionColor=new Color(16,78,139);
		        		}
		        		c.setBackground(insersionColor);
		        	}

                    if(columnName.equals("D")){
		        		if(numericValue==0){
		        			insersionColor=new Color(255,231,186);
		        		} else if (numericValue > 0 && numericValue <= Gui.this.segmentSizeDetailedTable[2]) {

		        			insersionColor=new Color(255,106,106);
			        	} else if (numericValue > Gui.this.segmentSizeDetailedTable[2] && numericValue <= 2 * Gui.this.segmentSizeDetailedTable[2]) {
		        			insersionColor=new Color(255,0,0);
		        		} else if (numericValue > 2 * Gui.this.segmentSizeDetailedTable[2] && numericValue <= 3 * Gui.this.segmentSizeDetailedTable[2]) {

		        			insersionColor=new Color(205,0,0);
		        		}
		        		else{
		        			insersionColor=new Color(139,0,0);
		        		}
		        		c.setBackground(insersionColor);
		        	}

                    return c;
		        }
		        catch(Exception e){

                    if(tmpValue.equals("")){
		        			c.setBackground(Color.black);
                        return c;
		        		}
		        		else{
		        			if(columnName.contains("v")){
		        				c.setBackground(Color.lightGray);
		        				if(levelized==false){
                                    this.setToolTipText(columnName);
		        				}
		        			}
		        			else{
		        				Color tableNameColor=new Color(205,175,149);
		        				c.setBackground(tableNameColor);
		        			}
                        return c;
		        		}


                }
		    }
		});

        tmpLifeTimeTable.setOpaque(true);

        tmpLifeTimeTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	    tmpLifeTimeTable.getSelectionModel().addListSelectionListener(new RowListener());
	    tmpLifeTimeTable.getColumnModel().getSelectionModel().addListSelectionListener(new ColumnListener());


        JScrollPane detailedScrollPane=new JScrollPane();
	    detailedScrollPane.setViewportView(tmpLifeTimeTable);
	    detailedScrollPane.setAlignmentX(0);
	    detailedScrollPane.setAlignmentY(0);
	    detailedScrollPane.setBounds(0,0,1280,650);
        detailedScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        detailedScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        detailedScrollPane.setCursor(this.getCursor());

	    JDialog detailedDialog=new JDialog();
	    detailedDialog.setBounds(100, 100, 1300, 700);

        JPanel panelToAdd=new JPanel();

        GroupLayout gl_panel = new GroupLayout(panelToAdd);
	    gl_panel.setHorizontalGroup(
                gl_panel.createParallelGroup(GroupLayout.Alignment.LEADING)
		);
	    gl_panel.setVerticalGroup(
                gl_panel.createParallelGroup(GroupLayout.Alignment.LEADING)
		);
		panelToAdd.setLayout(gl_panel);
	    
	    panelToAdd.add(detailedScrollPane);
	    detailedDialog.getContentPane().add(panelToAdd);
	    detailedDialog.setVisible(true);
		
		
	}
	
	private void importData(String fileName) throws IOException, RecognitionException {


        BufferedReader br = new BufferedReader(new FileReader(fileName));

        String line;

        while(true) {
			line = br.readLine();
            if (line == null)
				break;
			if(line.contains("Project-name")){
				String[] projectNameTable=line.split(":");
                this.projectName = projectNameTable[1];
			}
			else if(line.contains("Dataset-txt")){
				String[] datasetTxtTable=line.split(":");
                this.datasetTxt = datasetTxtTable[1];
			}
			else if(line.contains("Input-csv")){
				String[] inputCsvTable=line.split(":");
                this.inputCsv = inputCsvTable[1];
			}
			else if(line.contains("Assessement1-output")){
				String[] outputAss1=line.split(":");
                this.outputAssessment1 = outputAss1[1];
			}
			else if(line.contains("Assessement2-output")){
				String[] outputAss2=line.split(":");
                this.outputAssessment2 = outputAss2[1];
			}
			else if(line.contains("Transition-xml")){
				String[] transitionXmlTable=line.split(":");
                this.transitionsFile = transitionXmlTable[1];
			}


        }

        br.close();


        System.out.println("Project Name:" + this.projectName);
        System.out.println("Dataset txt:" + this.datasetTxt);
        System.out.println("Input Csv:" + this.inputCsv);
        System.out.println("Output Assessment1:" + this.outputAssessment1);
        System.out.println("Output Assessment2:" + this.outputAssessment2);
        System.out.println("Transitions File:" + this.transitionsFile);

        this.globalDataKeeper = new GlobalDataKeeper(this.datasetTxt, this.transitionsFile);
        this.globalDataKeeper.setData();
        System.out.println(this.globalDataKeeper.getAllPPLTables().size());
		System.out.println(fileName);

        String logSentence="load project test \n ";//testing Giorgos
        logSentence += "Project Name:" + this.projectName + "\n";//testing Giorgos
        logSentence += "Dataset txt:" + this.datasetTxt + "\n";//testing Giorgos
        logSentence += "Input Csv:" + this.inputCsv + "\n";//testing Giorgos
        logSentence += "Output Assessment1:" + this.outputAssessment1 + "\n";//testing Giorgos
        logSentence += "Output Assessment2:" + this.outputAssessment2 + "\n";//testing Giorgos
        logSentence += "Transitions File:" + this.transitionsFile + "\n";//testing Giorgos
        logSentence += this.globalDataKeeper.getAllPPLTables().size() + "\n";//testing Giorgos
		logSentence += fileName+ "\n";//testing Giorgos
		tlp.writeToFile(tlp.getOption());
		tlp.writeToFile("[P 1] Projects Details => "+logSentence);
		
 

        this.fillTable();
        this.fillTree();

        this.currentProject = fileName;
        
	}

    private class ColumnListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent event) {
            if (event.getValueIsAdjusting()) {
                return;
            }

        }
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        // TODO Auto-generated method stub

    }
	
	public void fillTable() {
        TableConstructionIDU table = new TableConstructionIDU(this.globalDataKeeper);
		final String[] columns=table.constructColumns();
		final String[][] rows=table.constructRows();
        this.segmentSizeZoomArea = table.getSegmentSize();
        tlp.writeToFile("[P 2] fillTable() segmentSizeZoomArea => "+Arrays.toString(this.segmentSizeZoomArea));
        
        this.finalColumnsZoomArea = columns;
        System.out.println(Arrays.toString( this.finalColumnsZoomArea));
        tlp.writeToFile("[P 3] fillTable() finalColumnsZoomArea => "+Arrays.toString(this.finalColumnsZoomArea));
        
        this.finalRowsZoomArea = rows;
        tlp.writeToFile("[P 4] fillTable() finalRowsZoomArea => "+Arrays.deepToString(this.finalRowsZoomArea));
        this.tabbedPane.setSelectedIndex(0);
        this.makeGeneralTableIDU();

        this.timeWeight = (float) 0.5;
        this.changeWeight = (float) 0.5;
        this.preProcessingTime = false;
        this.preProcessingChange = false;
        if (this.globalDataKeeper.getAllPPLTransitions().size() < 56) {
            this.numberOfPhases = 40;
        }
        else{
            this.numberOfPhases = 56;
        }
        this.numberOfClusters = 14;

        System.out.println(this.timeWeight + " " + this.changeWeight);

        PhaseAnalyzerMainEngine mainEngine = new PhaseAnalyzerMainEngine(this.inputCsv, this.outputAssessment1, this.outputAssessment2, this.timeWeight, this.changeWeight, this.preProcessingTime, this.preProcessingChange);

		Double b=new Double(0.3);
		Double d=new Double(0.3);
		Double c=new Double(0.3);

        mainEngine.parseInput();
		System.out.println("\n\n\n");
        mainEngine.extractPhases(this.numberOfPhases);

        mainEngine.connectTransitionsWithPhases(this.globalDataKeeper);
        this.globalDataKeeper.setPhaseCollectors(mainEngine.getPhaseCollectors());
        TableClusteringMainEngine mainEngine2 = new TableClusteringMainEngine(this.globalDataKeeper, b, d, c);
        mainEngine2.extractClusters(this.numberOfClusters);
        this.globalDataKeeper.setClusterCollectors(mainEngine2.getClusterCollectors());
        
        mainEngine2.print();

        if (this.globalDataKeeper.getPhaseCollectors().size() != 0) {
            TableConstructionWithClusters tableP = new TableConstructionWithClusters(this.globalDataKeeper);
			final String[] columnsP=tableP.constructColumns();
			final String[][] rowsP=tableP.constructRows();
            this.segmentSize = tableP.getSegmentSize();
            
            tlp.writeToFile("[P 8] fillTable() segmentsize => "+ Arrays.toString(this.segmentSize));
            this.finalColumns = columnsP;
            tlp.writeToFile("[P 9] fillTable() finalColumns => "+ Arrays.toString(this.finalColumns));
            this.finalRows = rowsP;
            tlp.writeToFile("[P 10] fillTable() finalRows => "+ Arrays.deepToString(this.finalRows));
            this.tabbedPane.setSelectedIndex(0);
            this.makeGeneralTablePhases();
            this.fillClustersTree();
        }
        System.out.println("Schemas:" + this.globalDataKeeper.getAllPPLSchemas().size());
        System.out.println("Transitions:" + this.globalDataKeeper.getAllPPLTransitions().size());
        System.out.println("Tables:" + this.globalDataKeeper.getAllPPLTables().size());

        String logSentence = "Schemas:" + this.globalDataKeeper.getAllPPLSchemas().size() + "\n";//testing Giorgos
        logSentence += "Transitions:" + this.globalDataKeeper.getAllPPLTransitions().size() + "\n";//testing Giorgos
        logSentence += "Tables:" + this.globalDataKeeper.getAllPPLTables().size() + "\n";//testing Giorgos

        tlp.writeToFile("[P 11] Overview Output => "+ logSentence);
	}
	
	public void optimize() throws IOException{

        String lalaString="Birth Weight:"+"\tDeath Weight:"+"\tChange Weight:"+"\tTotal Cohesion:"+"\tTotal Separation:"+"\n";
		int counter=0;
		for(double wb=0.0; wb<=1.0; wb=wb+0.01){

            for(double wd=(1.0-wb); wd>=0.0; wd=wd-0.01){

                double wc=1.0-(wb+wd);
                TableClusteringMainEngine mainEngine2 = new TableClusteringMainEngine(this.globalDataKeeper, wb, wd, wc);
                mainEngine2.extractClusters(this.numberOfClusters);
                this.globalDataKeeper.setClusterCollectors(mainEngine2.getClusterCollectors());

                ClusterValidatorMainEngine lala = new ClusterValidatorMainEngine(this.globalDataKeeper);
					lala.run();

                lalaString=lalaString+wb+"\t"+wd+"\t"+wc
							+"\t"+lala.getTotalCohesion()+"\t"+lala.getTotalSeparation()+"\t"+(wb+wd+wc)+"\n";

                counter++;
					System.err.println(counter);


            }


        }

		FileWriter fw;
		try {
			fw = new FileWriter("lala.csv");

            BufferedWriter bw = new BufferedWriter(fw);
			bw.write(lalaString);
			bw.close();

        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


        System.out.println(lalaString);


    }
	
	public void getExternalValidityReport() throws IOException{

        String lalaString="Birth Weight:"+"\tDeath Weight:"+"\tChange Weight:"+"\n";
		int counter=0;

        TableClusteringMainEngine mainEngine2 = new TableClusteringMainEngine(this.globalDataKeeper, 0.333, 0.333, 0.333);
		mainEngine2.extractClusters(4);
        this.globalDataKeeper.setClusterCollectors(mainEngine2.getClusterCollectors());

        ClusterValidatorMainEngine lala = new ClusterValidatorMainEngine(this.globalDataKeeper);
		lala.run();

        lalaString=lalaString+"\n"+"0.333"+"\t"+"0.333"+"\t"+"0.333"
				+"\n"+lala.getExternalEvaluationReport();

        for(double wb=0.0; wb<=1.0; wb=wb+0.5){

            for(double wd=(1.0-wb); wd>=0.0; wd=wd-0.5){

                double wc=1.0-(wb+wd);
                mainEngine2 = new TableClusteringMainEngine(this.globalDataKeeper, wb, wd, wc);
					mainEngine2.extractClusters(4);
                this.globalDataKeeper.setClusterCollectors(mainEngine2.getClusterCollectors());

                lala = new ClusterValidatorMainEngine(this.globalDataKeeper);
					lala.run();

                lalaString=lalaString+"\n"+wb+"\t"+wd+"\t"+wc
							+"\n"+lala.getExternalEvaluationReport();

                counter++;
					System.err.println(counter);


            }


        }

		FileWriter fw;
		try {
			fw = new FileWriter("lala.csv");

            BufferedWriter bw = new BufferedWriter(fw);
			bw.write(lalaString);
			bw.close();

        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


        System.out.println(lalaString);


    }
	
	public void fillTree(){
        TreeConstructionGeneral tc = new TreeConstructionGeneral(this.globalDataKeeper);
        this.tablesTree = new JTree();
        this.tablesTree = tc.constructTree();
        tlp.writeObjectToFile(tlp.getOption()+"tablesTree-fillTree.txt", this.tablesTree);
        
        this.tablesTree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent ae) {
			    	TreePath selection = ae.getPath();
                Gui.this.selectedFromTree.add(selection.getLastPathComponent().toString());
                System.out.println(selection.getLastPathComponent() + " is selected");
                tlp.writeToFile("On Trees table [general] selection is"+selection.getLastPathComponent());
			    }
		 });

        this.tablesTree.addMouseListener(new MouseAdapter() {
				@Override
				   public void mouseReleased(MouseEvent e) {

                    if(SwingUtilities.isRightMouseButton(e)){
							System.out.println("Right Click Tree");

                        final JPopupMenu popupMenu = new JPopupMenu();
							        JMenuItem showDetailsItem = new JMenuItem("Show This into the Table");
							        showDetailsItem.addActionListener(new ActionListener() {

                                        @Override
							            public void actionPerformed(ActionEvent e) {

                                            Gui.this.LifeTimeTable.repaint();

							            }
							        });
							        popupMenu.add(showDetailsItem);
                        popupMenu.show(Gui.this.tablesTree, e.getX(), e.getY());

						}

                }
			});

        this.treeScrollPane.setViewportView(this.tablesTree);

        this.treeScrollPane.setBounds(5, 5, 250, 170);
        this.treeScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        this.treeScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        this.tablesTreePanel.add(this.treeScrollPane);

        this.treeLabel.setText("General Tree");

        this.sideMenu.revalidate();
        this.sideMenu.repaint();

	}
	
	public void fillPhasesTree(){

        TreeConstructionPhases tc = new TreeConstructionPhases(this.globalDataKeeper);
        this.tablesTree = tc.constructTree();
        tlp.writeObjectToFile(tlp.getOption()+"tablesTree-fillPhasesTree.txt",this.tablesTree);
        this.tablesTree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent ae) {
			    	TreePath selection = ae.getPath();
                Gui.this.selectedFromTree.add(selection.getLastPathComponent().toString());
                System.out.println(selection.getLastPathComponent() + " is selected");
                tlp.writeToFile("On Trees table [phases] selection is"+selection.getLastPathComponent());
			    }
		 });

        this.tablesTree.addMouseListener(new MouseAdapter() {
				@Override
				   public void mouseReleased(MouseEvent e) {

                    if(SwingUtilities.isRightMouseButton(e)){
							System.out.println("Right Click Tree");

                        final JPopupMenu popupMenu = new JPopupMenu();
					        JMenuItem showDetailsItem = new JMenuItem("Show This into the Table");
					        showDetailsItem.addActionListener(new ActionListener() {

					            @Override
					            public void actionPerformed(ActionEvent e) {

                                    Gui.this.LifeTimeTable.repaint();

					            }
					        });
					        popupMenu.add(showDetailsItem);
                        popupMenu.show(Gui.this.tablesTree, e.getX(), e.getY());


						}

                }
			});

        this.treeScrollPane.setViewportView(this.tablesTree);
        this.treeScrollPane.setBounds(5, 5, 250, 170);
        this.treeScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        this.treeScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        this.tablesTreePanel.add(this.treeScrollPane);

        this.treeLabel.setText("Phases Tree");

        this.sideMenu.revalidate();
        this.sideMenu.repaint();

	}
	
	public void fillClustersTree(){

        TreeConstructionPhasesWithClusters tc = new TreeConstructionPhasesWithClusters(this.globalDataKeeper);
        this.tablesTree = tc.constructTree();
        tlp.writeObjectToFile(tlp.getOption()+"tablesTree-fillClustersTree.txt", this.tablesTree);
        
        this.tablesTree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent ae) {
			    	TreePath selection = ae.getPath();
                Gui.this.selectedFromTree.add(selection.getLastPathComponent().toString());
                System.out.println(selection.getLastPathComponent() + " is selected");
                tlp.writeToFile("On Trees table [cluster] selectio is"+selection.getLastPathComponent());
			    }
		 });

        this.tablesTree.addMouseListener(new MouseAdapter() {
				@Override
				   public void mouseReleased(MouseEvent e) {

                    if(SwingUtilities.isRightMouseButton(e)){
							System.out.println("Right Click Tree");

                        final JPopupMenu popupMenu = new JPopupMenu();
					        JMenuItem showDetailsItem = new JMenuItem("Show This into the Table");
					        showDetailsItem.addActionListener(new ActionListener() {

					            @Override
					            public void actionPerformed(ActionEvent e) {

                                    Gui.this.LifeTimeTable.repaint();

					            }
					        });
					        popupMenu.add(showDetailsItem);
                        popupMenu.show(Gui.this.tablesTree, e.getX(), e.getY());

						}

                }
			});

        this.treeScrollPane.setViewportView(this.tablesTree);


        this.treeScrollPane.setBounds(5, 5, 250, 170);
        this.treeScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        this.treeScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        this.tablesTreePanel.add(this.treeScrollPane);

        this.treeLabel.setText("Clusters Tree");

        this.sideMenu.revalidate();
        this.sideMenu.repaint();


	}
	
	public void setDescription(String descr){
        this.descriptionText.setText(descr);
    }

    private class RowListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent event) {
            if (event.getValueIsAdjusting()) {
                return;
            }

            int selectedRow = Gui.this.LifeTimeTable.getSelectedRow();

            Gui.this.selectedRows.add(selectedRow);

        }
    }

		
	
}