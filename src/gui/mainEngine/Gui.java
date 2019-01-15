package gui.mainEngine;

//try to extract relationship between gui and pplSchema and pplTransition

import data.dataController.DataController;
import gui.tableElements.commons.ExtendedJvTable;
import gui.tableElements.commons.ExtendedTableModel;
import gui.tableElements.tableRenderers.IDUHeaderTableRenderer;
import gui.tableElements.tableRenderers.IDUTableRenderer;
import outputSerializer.OutputSerializer;

import org.antlr.v4.runtime.RecognitionException;

import gui.listeners.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;


public class Gui extends JFrame{

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
    private ExtendedTableModel detailedModel;
    private ExtendedTableModel generalModel;
    private ExtendedTableModel zoomModel;
    private ExtendedJvTable LifeTimeTable;
    private ExtendedJvTable zoomAreaTable;
    private DataController dataController;
	
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
	private OutputSerializer tlp= new OutputSerializer("wannabeTestingOutput.txt");
	private mainFlowListener flowlistener;
	private showListener showlistener;
	private mouseListener mouselistener;
	private menuButtonListener menulistener;
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

        flowlistener = new mainFlowListener(this);
        showlistener = new showListener(this);
        menulistener = new menuButtonListener(this);
        mouselistener = new mouseListener(this);
		JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmCreateProject = new JMenuItem("Create Project");
		mntmCreateProject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				flowlistener.createProject();
			}
		});
		mnFile.add(mntmCreateProject);
		
		JMenuItem mntmLoadProject = new JMenuItem("Load Project");
		mntmLoadProject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {			
				flowlistener.loadProject();	
			}
		});
		mnFile.add(mntmLoadProject);
		
		JMenuItem mntmEditProject = new JMenuItem("Edit Project");
		mntmEditProject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				flowlistener.editProject();	
			}
		});
		mnFile.add(mntmEditProject);
		
		
		JMenu mnTable = new JMenu("Table");
		menuBar.add(mnTable);
		
		JMenuItem mntmShowLifetimeTable = new JMenuItem("Show Full Detailed LifeTime Table");
		mntmShowLifetimeTable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showlistener.showLifetimeTable(tlp);
			}
		});
		
		JMenuItem mntmShowGeneralLifetimeIDU = new JMenuItem("Show PLD");
		mntmShowGeneralLifetimeIDU.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
                showlistener.showGeneralLifetimeIDU( tlp);
			}
		});
		mnTable.add(mntmShowGeneralLifetimeIDU);
		
		JMenuItem mntmShowGeneralLifetimePhasesPLD = new JMenuItem("Show Phases PLD");
		mntmShowGeneralLifetimePhasesPLD.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				showlistener.showGeneralLifetimePhasesPLD( tlp);	
			}
		});
		mnTable.add(mntmShowGeneralLifetimePhasesPLD);
		
		JMenuItem mntmShowGeneralLifetimePhasesWithClustersPLD = new JMenuItem("Show Phases With Clusters PLD");
		mntmShowGeneralLifetimePhasesWithClustersPLD.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				showlistener.showGeneralLifetimePhasesWithClustersPLD( tlp);
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
				menulistener.getHelpButtonListener();
			}
		});

        this.mnProject = new JMenu("Project");
        menuBar.add(this.mnProject);

        this.mntmInfo = new JMenuItem("Info");
        this.mntmInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menulistener.getInfoButtonListener(Gui.this);
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
                        .addComponent(this.getTabbedPane(), GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 1474, Short.MAX_VALUE)
		);
		gl_contentPane.setVerticalGroup(
                gl_contentPane.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(this.getTabbedPane(), GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 771, Short.MAX_VALUE)
		);


        this.getTabbedPane().addTab("LifeTime Table", null, this.lifeTimePanel, null);

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
        
        
        
        mouselistener.listenZoomInButton(this);
        
        this.zoomOutButton = new JButton("Zoom Out");
        
        mouselistener.listenZoomOutButton(this);
        
        
       


        this.showThisToPopup = new JButton("Enlarge");
        
        mouselistener.listenToEnlarge(this);
        


        this.undoButton = new JButton("Undo");
        mouselistener.listenUndoButton(this);

        this.uniformlyDistributedButton = new JButton("Same Width");
        mouselistener.listenWidthButton(this);

        this.notUniformlyDistributedButton = new JButton("Over Time");
        mouselistener.listenOvertimeButton(this);

        this.lifeTimePanel.add(this.getZoomInButton());
        this.lifeTimePanel.add(this.getUndoButton());
        this.lifeTimePanel.add(this.getZoomOutButton());
        this.lifeTimePanel.add(this.getUniformlyDistributedButton());
        this.lifeTimePanel.add(this.getNotUniformlyDistributedButton());
        this.lifeTimePanel.add(this.getShowThisToPopup());

        this.lifeTimePanel.add(this.zoomAreaLabel);

        this.lifeTimePanel.add(this.generalTableLabel);

        this.contentPane.setLayout(gl_contentPane);

        this.pack();
        this.setBounds(30, 30, 1300, 700);


    }


	public void makeGeneralTableIDU() {

        String[][] sortedRows = this.getDataController().getSortedRows(this.getFinalRowsZoomArea());
        this.setFinalRowsZoomArea(sortedRows);
        this.setShowingPld(true);
        this.getZoomInButton().setVisible(true);
        this.getZoomOutButton().setVisible(true);
        this.getShowThisToPopup().setVisible(true);
        int numberOfColumns = this.getFinalRowsZoomArea()[0].length;
        int numberOfRows = this.getFinalRowsZoomArea().length;

        this.selectedRows = new ArrayList<Integer>();

		String[][] rows=new String[numberOfRows][numberOfColumns];

		for(int i=0; i<numberOfRows; i++){

            rows[i][0] = this.getFinalRowsZoomArea()[i][0];

        }
		
        this.zoomModel = new ExtendedTableModel(this.getFinalColumnsZoomArea(), rows);
        ExtendedJvTable generalTable = new ExtendedJvTable(this.zoomModel);
        
		generalTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        if (this.getRowHeight() < 1) {
            this.setRowHeight(1);
        }
        if (this.getColumnWidth() < 1) {
            this.setColumnWidth(1);
        }

		for(int i=0; i<generalTable.getRowCount(); i++){
            generalTable.setRowHeight(i, this.getRowHeight());

		}


		generalTable.setShowGrid(false);
		generalTable.setIntercellSpacing(new Dimension(0, 0));


        for(int i=0; i<generalTable.getColumnCount(); i++){
			if(i==0){
                generalTable.getColumnModel().getColumn(0).setPreferredWidth(this.getColumnWidth());

			}
			else{
                generalTable.getColumnModel().getColumn(i).setPreferredWidth(this.getColumnWidth());

			}
		}

		int start=-1;
		int end=-1;
		
        if (this.getWholeCol() != -1 && this.getWholeCol() != 0) {
            start = this.getDataController().getPhases().get(this.getWholeCol() - 1).getStartPos();
            end = this.getDataController().getPhases().get(this.getWholeCol() - 1).getEndPos();
		}
        

        if (this.getWholeCol() != -1) {
			for(int i=0; i<generalTable.getColumnCount(); i++){
				if(!(generalTable.getColumnName(i).equals("Table name"))){
					if(Integer.parseInt(generalTable.getColumnName(i))>=start && Integer.parseInt(generalTable.getColumnName(i))<=end){

						generalTable.getColumnModel().getColumn(i).setHeaderRenderer(new IDUHeaderTableRenderer());

					}
				}
			}
		}
       
        final IDUTableRenderer renderer = new IDUTableRenderer(Gui.this, this.getFinalRowsZoomArea(), this.getDataController(), this.getSegmentSize());

       
		generalTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer()
		{

			private static final long serialVersionUID = 1L;

			@Override
		    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		    {
		        final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                String tmpValue = Gui.this.getFinalRowsZoomArea()[row][column];
		        String columnName=table.getColumnName(column);
		        Color fr=new Color(0,0,0);

		        c.setForeground(fr);
                this.setOpaque(true);
                //bottom table column selection
                if (column == Gui.this.getWholeColZoomArea() && Gui.this.getWholeColZoomArea() != 0) {

		        	String description="Transition ID:"+table.getColumnName(column)+"\n";
                    description = description + "Old Version Name:" + Gui.this.getDataController().getAllPPLTransitions().
	        				get(Integer.parseInt(table.getColumnName(column))).getOldVersionName()+"\n";
                    description = description + "New Version Name:" + Gui.this.getDataController().getAllPPLTransitions().
                            get(Integer.parseInt(table.getColumnName(column))).getNewVersionName() + "\n";

                    description = description + "Transition Changes:" + Gui.this.getDataController().getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getNumberOfChangesForOneTr() + "\n";
                    description = description + "Additions:" + Gui.this.getDataController().getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getNumberOfAdditionsForOneTr() + "\n";
                    description = description + "Deletions:" + Gui.this.getDataController().getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getNumberOfDeletionsForOneTr() + "\n";
                    description = description + "Updates:" + Gui.this.getDataController().getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getNumberOfUpdatesForOneTr() + "\n";

                    Gui.this.descriptionText.setText(description);
                    
		        	Color cl = new Color(255,69,0,100);

	        		c.setBackground(cl);
	        		return c;
	        		
		        } else if (Gui.this.getSelectedColumnZoomArea() == 0) { //bottom table row selection

		        	if (isSelected){
		        		Color cl = new Color(255,69,0,100);
		        		c.setBackground(cl);

                        String description = "Table:" + Gui.this.getFinalRowsZoomArea()[row][0] + "\n";
                        description = description + "Birth Version Name:" + Gui.this.getDataController().getAllPPLTables().get(Gui.this.getFinalRowsZoomArea()[row][0]).getBirth() + "\n";
                        description = description + "Birth Version ID:" + Gui.this.getDataController().getAllPPLTables().get(Gui.this.getFinalRowsZoomArea()[row][0]).getBirthVersionID() + "\n";
                        description = description + "Death Version Name:" + Gui.this.getDataController().getAllPPLTables().get(Gui.this.getFinalRowsZoomArea()[row][0]).getDeath() + "\n";
                        description = description + "Death Version ID:" + Gui.this.getDataController().getAllPPLTables().get(Gui.this.getFinalRowsZoomArea()[row][0]).getDeathVersionID() + "\n";
                        description = description + "Total Changes:" + Gui.this.getDataController().getAllPPLTables().get(Gui.this.getFinalRowsZoomArea()[row][0]).getTotalChanges() + "\n";
                        Gui.this.descriptionText.setText(description);
                                    
		        		return c;


                    }
		        }
		        else{


                    if (Gui.this.getSelectedFromTree().contains(Gui.this.getFinalRowsZoomArea()[row][0])) {


		        		Color cl = new Color(255,69,0,100);

		        		c.setBackground(cl);

		        		return c;
		        	}

                    //bottom table click cell  
                    if (isSelected && hasFocus){

		        		String description="";
		        		
		        		if(!table.getColumnName(column).contains("Table name")){
                            description = "Table:" + Gui.this.getFinalRowsZoomArea()[row][0] + "\n";

                            description = description + "Old Version Name:" + Gui.this.getDataController().getAllPPLTransitions().
			        				get(Integer.parseInt(table.getColumnName(column))).getOldVersionName()+"\n";
                            description = description + "New Version Name:" + Gui.this.getDataController().getAllPPLTransitions().
                                    get(Integer.parseInt(table.getColumnName(column))).getNewVersionName() + "\n";
                            
                            
                            if (Gui.this.getDataController().getAllPPLTables().get(Gui.this.getFinalRowsZoomArea()[row][0]).
			        				getTableChanges().getTableAtChForOneTransition(Integer.parseInt(table.getColumnName(column)))!=null){
                                description = description + "Transition Changes:" + Gui.this.getDataController().getAllPPLTables().get(Gui.this.getFinalRowsZoomArea()[row][0]).
			        				getTableChanges().getTableAtChForOneTransition(Integer.parseInt(table.getColumnName(column))).size()+"\n";
                                description = description + "Additions:" + Gui.this.getDataController().getAllPPLTables().get(Gui.this.getFinalRowsZoomArea()[row][0]).
			        					getNumberOfAdditionsForOneTr(Integer.parseInt(table.getColumnName(column)))+"\n";
                                description = description + "Deletions:" + Gui.this.getDataController().getAllPPLTables().get(Gui.this.getFinalRowsZoomArea()[row][0]).
			        					getNumberOfDeletionsForOneTr(Integer.parseInt(table.getColumnName(column)))+"\n";
                                description = description + "Updates:" + Gui.this.getDataController().getAllPPLTables().get(Gui.this.getFinalRowsZoomArea()[row][0]).
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
	        			insersionColor=new Color(154,205,50,200);
	        		} else if (numericValue > 0 && numericValue <= Gui.this.getSegmentSizeZoomArea()[3]) {

	        			insersionColor=new Color(176,226,255);
		        	} else if (numericValue > Gui.this.getSegmentSizeZoomArea()[3] && numericValue <= 2 * Gui.this.getSegmentSizeZoomArea()[3]) {
	        			insersionColor=new Color(92,172,238);
	        		} else if (numericValue > 2 * Gui.this.getSegmentSizeZoomArea()[3] && numericValue <= 3 * Gui.this.getSegmentSizeZoomArea()[3]) {

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



		

		mouselistener.listenToRightClick(this,generalTable,renderer);



        this.tmpScrollPaneZoomArea.setViewportView(this.getZoomAreaTable());
        this.tmpScrollPaneZoomArea.setAlignmentX(0);
        this.tmpScrollPaneZoomArea.setAlignmentY(0);
        this.tmpScrollPaneZoomArea.setBounds(300, 300, 950, 250);
        this.tmpScrollPaneZoomArea.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        this.tmpScrollPaneZoomArea.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        this.lifeTimePanel.setCursor(this.getCursor());
        this.lifeTimePanel.add(this.tmpScrollPaneZoomArea);



	}

public void makeGeneralTablePhases() {
    this.getUniformlyDistributedButton().setVisible(true);

    this.getNotUniformlyDistributedButton().setVisible(true);

    int numberOfColumns = this.getFinalRows()[0].length;
    int numberOfRows = this.getFinalRows().length;

    this.selectedRows = new ArrayList<Integer>();

	String[][] rows=new String[numberOfRows][numberOfColumns];

	for(int i=0; i<numberOfRows; i++){

        rows[i][0] = this.getFinalRows()[i][0];

    }

    this.generalModel = new ExtendedTableModel(this.getFinalColumns(), rows);
   
    tlp.writeTable("Phases Table", this.getFinalColumns(), this.getFinalRows());
    final ExtendedJvTable generalTable = new ExtendedJvTable(this.generalModel);
   
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

            String tmpValue = Gui.this.getFinalRows()[row][column];
	        String columnName=table.getColumnName(column);
	        Color fr=new Color(0,0,0);
	        c.setForeground(fr);

	        //top table column selection
            if (column == Gui.this.getWholeCol() && Gui.this.getWholeCol() != 0) {

	        	String description=table.getColumnName(column)+"\n";
                description = description + "First Transition ID:" + Gui.this.getDataController().getPhases().
        				get(column-1).getStartPos()+"\n";
                description = description + "Last Transition ID:" + Gui.this.getDataController().getPhases().
        				get(column-1).getEndPos()+"\n";
                description = description + "Total Changes For This Phase:" + Gui.this.getDataController().getPhases().
        				get(column-1).getTotalUpdates()+"\n";
                description = description + "Additions For This Phase:" + Gui.this.getDataController().getPhases().
        				get(column-1).getTotalAdditionsOfPhase()+"\n";
                description = description + "Deletions For This Phase:" + Gui.this.getDataController().getPhases().
        				get(column-1).getTotalDeletionsOfPhase()+"\n";
                description = description + "Updates For This Phase:" + Gui.this.getDataController().getPhases().
        				get(column-1).getTotalUpdatesOfPhase()+"\n";

                Gui.this.descriptionText.setText(description);
              
                
	        	Color cl = new Color(255,69,0,100);

        		c.setBackground(cl);
        		return c;
        		//top table row selection
	        } else if (Gui.this.getSelectedColumn() == 0) {
	        	if (isSelected){
	        		//row selection cluster case
                    if (Gui.this.getFinalRows()[row][0].contains("Cluster")) {
                        String description = "Cluster:" + Gui.this.getFinalRows()[row][0] + "\n";
                        description = description + "Birth Version Name:" + Gui.this.getDataController().getClusters().get(row).getBirthSqlFile() + "\n";
                        description = description + "Birth Version ID:" + Gui.this.getDataController().getClusters().get(row).getBirth() + "\n";
                        description = description + "Death Version Name:" + Gui.this.getDataController().getClusters().get(row).getDeathSqlFile() + "\n";
                        description = description + "Death Version ID:" + Gui.this.getDataController().getClusters().get(row).getDeath() + "\n";
                        description = description + "Tables:" + Gui.this.getDataController().getClusters().get(row).getNamesOfTables().size() + "\n";
                        description = description + "Total Changes:" + Gui.this.getDataController().getClusters().get(row).getTotalChanges() + "\n";
                        
                        Gui.this.descriptionText.setText(description);
                       
                      
                       
	        		}
	        		else{// row selection table case
                        String description = "Table:" + Gui.this.getFinalRows()[row][0] + "\n";
                        description = description + "Birth Version Name:" + Gui.this.getDataController().getAllPPLTables().get(Gui.this.getFinalRows()[row][0]).getBirth() + "\n";
                        description = description + "Birth Version ID:" + Gui.this.getDataController().getAllPPLTables().get(Gui.this.getFinalRows()[row][0]).getBirthVersionID() + "\n";
                        description = description + "Death Version Name:" + Gui.this.getDataController().getAllPPLTables().get(Gui.this.getFinalRows()[row][0]).getDeath() + "\n";
                        description = description + "Death Version ID:" + Gui.this.getDataController().getAllPPLTables().get(Gui.this.getFinalRows()[row][0]).getDeathVersionID() + "\n";
                        description = description + "Total Changes:" + Gui.this.getDataController().getAllPPLTables().get(Gui.this.getFinalRows()[row][0]).getTotalChanges() + "\n";
                        Gui.this.descriptionText.setText(description);
                        
                     }


                    Color cl = new Color(255,69,0,100);

	        		c.setBackground(cl);
	        		return c;
	        	}
	        }
	        else{

                if (Gui.this.getSelectedFromTree().contains(Gui.this.getFinalRows()[row][0])) {

	        		Color cl = new Color(255,69,0,100);

	        		c.setBackground(cl);

	        		return c;
	        	}
                //On top table cell selection
	        	if (isSelected && hasFocus){

	        		String description="";
	        		//cell selection cluster case
	        		if(!table.getColumnName(column).contains("Table name")){

                        if (Gui.this.getFinalRows()[row][0].contains("Cluster")) {

                            description = Gui.this.getFinalRows()[row][0] + "\n";
                            description = description + "Tables:" + Gui.this.getDataController().getClusters().get(row).getNamesOfTables().size() + "\n\n";

			        		description=description+table.getColumnName(column)+"\n";
                            description = description + "First Transition ID:" + Gui.this.getDataController().getPhases().
			        				get(column-1).getStartPos()+"\n";
                            description = description + "Last Transition ID:" + Gui.this.getDataController().getPhases().
			        				get(column-1).getEndPos()+"\n\n";
			        		description=description+"totTotal Changes For This Phase:"+tmpValue+"\n";
			        		
			        		
			        		
		        		}
		        		else{//each cell table case
		        			description=table.getColumnName(column)+"\n";
                            description = description + "First Transition ID:" + Gui.this.getDataController().getPhases().
			        				get(column-1).getStartPos()+"\n";
                            description = description + "Last Transition ID:" + Gui.this.getDataController().getPhases().
			        				get(column-1).getEndPos()+"\n\n";
                            description = description + "Table:" + Gui.this.getFinalRows()[row][0] + "\n";
                            description = description + "Birth Version Name:" + Gui.this.getDataController().getAllPPLTables().get(Gui.this.getFinalRows()[row][0]).getBirth() + "\n";
                            description = description + "Birth Version ID:" + Gui.this.getDataController().getAllPPLTables().get(Gui.this.getFinalRows()[row][0]).getBirthVersionID() + "\n";
                            description = description + "Death Version Name:" + Gui.this.getDataController().getAllPPLTables().get(Gui.this.getFinalRows()[row][0]).getDeath() + "\n";
                            description = description + "Death Version ID:" + Gui.this.getDataController().getAllPPLTables().get(Gui.this.getFinalRows()[row][0]).getDeathVersionID() + "\n";
			        		description=description+"Total Changes For This Phase:"+tmpValue+"\n";
			        					        		
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
        		} else if (numericValue > 0 && numericValue <= Gui.this.getSegmentSize()[3]) {

        			insersionColor=new Color(176,226,255);
	        	} else if (numericValue > Gui.this.getSegmentSize()[3] && numericValue <= 2 * Gui.this.getSegmentSize()[3]) {
        			insersionColor=new Color(92,172,238);
        		} else if (numericValue > 2 * Gui.this.getSegmentSize()[3] && numericValue <= 3 * Gui.this.getSegmentSize()[3]) {

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
	
	mouselistener.listenToRightClick(this, generalTable);

    this.tmpScrollPane.setViewportView(this.getLifeTimeTable());
    this.tmpScrollPane.setAlignmentX(0);
    this.tmpScrollPane.setAlignmentY(0);
    this.tmpScrollPane.setBounds(300, 30, 950, 265);
    this.tmpScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    this.tmpScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

    this.lifeTimePanel.setCursor(this.getCursor());
    this.lifeTimePanel.add(this.tmpScrollPane);



}



public void showSelectionToZoomArea(int selectedColumn){
	this.getDataController().constructZoomAreaTable( this.getTablesSelected(), selectedColumn);
	final String[] columns=this.getDataController().getTableColumns("ZoomArea");
	final String[][] rows=this.getDataController().getTableRows("ZoomArea");
    this.setSegmentSizeZoomArea(this.getDataController().getSegmentSize("ZoomArea"));

    System.out.println("Schemas: " + this.getDataController().getAllPPLSchemas().size());
	System.out.println("C: "+columns.length+" R: "+rows.length);

    this.setFinalColumnsZoomArea(columns);
    this.setFinalRowsZoomArea(rows);
    this.getTabbedPane().setSelectedIndex(0);
    this.makeZoomAreaTable();



}

public void showClusterSelectionToZoomArea(int selectedColumn,String selectedCluster){


	ArrayList<String> tablesOfCluster=new ArrayList<String>();
    for (int i = 0; i < this.getTablesSelected().size(); i++) {
        String[] selectedClusterSplit = this.getTablesSelected().get(i).split(" ");
		int cluster=Integer.parseInt(selectedClusterSplit[1]);
        ArrayList<String> namesOfTables = this.getDataController().getClusters().get(cluster).getNamesOfTables();
		for(int j=0; j<namesOfTables.size(); j++){
			tablesOfCluster.add(namesOfTables.get(j));
		}
        System.out.println(this.getTablesSelected().get(i));
    }


	final String[] columns;
	final String[][] rows;
	if (selectedColumn==0) {
		this.getDataController().constructClusterTablesPhasesZoomA(tablesOfCluster);
		columns=this.getDataController().getTableColumns("PhasesZoomA");
		rows=this.getDataController().getTableRows("PhasesZoomA");
		this.setSegmentSizeZoomArea(this.getDataController().getSegmentSize("PhasesZoomA")); 
	}
	else{
		this.getDataController().constructZoomAreaTable( this.getTablesSelected(), selectedColumn);
		columns=this.getDataController().getTableColumns("ZoomArea");
		rows=this.getDataController().getTableRows("ZoomArea");
	    this.setSegmentSizeZoomArea(this.getDataController().getSegmentSize("ZoomArea"));
	}

    System.out.println("Schemas: " + this.getDataController().getAllPPLSchemas().size());
	System.out.println("C: "+columns.length+" R: "+rows.length);

    this.setFinalColumnsZoomArea(columns);
    this.setFinalRowsZoomArea(rows);
    this.getTabbedPane().setSelectedIndex(0);
    this.makeZoomAreaTableForCluster();


}

private void makeZoomAreaTable() {
    this.setShowingPld(false);
    int numberOfColumns = this.getFinalRowsZoomArea()[0].length;
    int numberOfRows = this.getFinalRowsZoomArea().length;


	final String[][] rowsZoom=new String[numberOfRows][numberOfColumns];

	for(int i=0; i<numberOfRows; i++){

        rowsZoom[i][0] = this.getFinalRowsZoomArea()[i][0];

    }

    this.zoomModel = new ExtendedTableModel(this.getFinalColumnsZoomArea(), rowsZoom);

    final ExtendedJvTable zoomTable = new ExtendedJvTable(this.zoomModel);

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


            String tmpValue = Gui.this.getFinalRowsZoomArea()[row][column];
	        String columnName=table.getColumnName(column);
	        Color fr=new Color(0,0,0);
	        c.setForeground(fr);

            if (column == Gui.this.getWholeColZoomArea()) {

	        	String description="Transition ID:"+table.getColumnName(column)+"\n";
                description = description + "Old Version Name:" + Gui.this.getDataController().getAllPPLTransitions().
        				get(Integer.parseInt(table.getColumnName(column))).getOldVersionName()+"\n";
                description = description + "New Version Name:" + Gui.this.getDataController().getAllPPLTransitions().
                        get(Integer.parseInt(table.getColumnName(column))).getNewVersionName() + "\n";

                description = description + "Transition Changes:" + Gui.this.getDataController().getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getNumberOfClusterChangesForOneTr(rowsZoom) + "\n";
                description = description + "Additions:" + Gui.this.getDataController().getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getNumberOfClusterAdditionsForOneTr(rowsZoom) + "\n";
                description = description + "Deletions:" + Gui.this.getDataController().getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getNumberOfClusterDeletionsForOneTr(rowsZoom) + "\n";
                description = description + "Updates:" + Gui.this.getDataController().getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getNumberOfClusterUpdatesForOneTr(rowsZoom) + "\n";


                Gui.this.descriptionText.setText(description);
	        	Color cl = new Color(255,69,0,100);
        		c.setBackground(cl);

        		return c;
	        } else if (Gui.this.getSelectedColumnZoomArea() == 0) {
	        	if (isSelected){
                    String description = "Table:" + Gui.this.getFinalRowsZoomArea()[row][0] + "\n";
                    description = description + "Birth Version Name:" + Gui.this.getDataController().getAllPPLTables().get(Gui.this.getFinalRowsZoomArea()[row][0]).getBirth() + "\n";
                    description = description + "Birth Version ID:" + Gui.this.getDataController().getAllPPLTables().get(Gui.this.getFinalRowsZoomArea()[row][0]).getBirthVersionID() + "\n";
                    description = description + "Death Version Name:" + Gui.this.getDataController().getAllPPLTables().get(Gui.this.getFinalRowsZoomArea()[row][0]).getDeath() + "\n";
                    description = description + "Death Version ID:" + Gui.this.getDataController().getAllPPLTables().get(Gui.this.getFinalRowsZoomArea()[row][0]).getDeathVersionID() + "\n";
                    description = description + "Total Changes:" + Gui.this.getDataController().getAllPPLTables().get(Gui.this.getFinalRowsZoomArea()[row][0]).
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
                        description = "Table:" + Gui.this.getFinalRowsZoomArea()[row][0] + "\n";

                        description = description + "Old Version Name:" + Gui.this.getDataController().getAllPPLTransitions().
		        				get(Integer.parseInt(table.getColumnName(column))).getOldVersionName()+"\n";
                        description = description + "New Version Name:" + Gui.this.getDataController().getAllPPLTransitions().
                                get(Integer.parseInt(table.getColumnName(column))).getNewVersionName() + "\n";
                        if (Gui.this.getDataController().getAllPPLTables().get(Gui.this.getFinalRowsZoomArea()[row][0]).
		        				getTableChanges().getTableAtChForOneTransition(Integer.parseInt(table.getColumnName(column)))!=null){
                            description = description + "Transition Changes:" + Gui.this.getDataController().getAllPPLTables().get(Gui.this.getFinalRowsZoomArea()[row][0]).
		        				getTableChanges().getTableAtChForOneTransition(Integer.parseInt(table.getColumnName(column))).size()+"\n";
                            description = description + "Additions:" + Gui.this.getDataController().getAllPPLTables().get(Gui.this.getFinalRowsZoomArea()[row][0]).
		        					getNumberOfAdditionsForOneTr(Integer.parseInt(table.getColumnName(column)))+"\n";
                            description = description + "Deletions:" + Gui.this.getDataController().getAllPPLTables().get(Gui.this.getFinalRowsZoomArea()[row][0]).
		        					getNumberOfDeletionsForOneTr(Integer.parseInt(table.getColumnName(column)))+"\n";
                            description = description + "Updates:" + Gui.this.getDataController().getAllPPLTables().get(Gui.this.getFinalRowsZoomArea()[row][0]).
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
        		} else if (numericValue > 0 && numericValue <= Gui.this.getSegmentSizeZoomArea()[3]) {

        			insersionColor=new Color(176,226,255);
	        	} else if (numericValue > Gui.this.getSegmentSizeZoomArea()[3] && numericValue <= 2 * Gui.this.getSegmentSizeZoomArea()[3]) {
        			insersionColor=new Color(92,172,238);
        		} else if (numericValue > 2 * Gui.this.getSegmentSizeZoomArea()[3] && numericValue <= 3 * Gui.this.getSegmentSizeZoomArea()[3]) {

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

	mouselistener.listenToRightClick2(zoomTable);

    this.tmpScrollPaneZoomArea.setViewportView(this.getZoomAreaTable());
    this.tmpScrollPaneZoomArea.setAlignmentX(0);
    this.tmpScrollPaneZoomArea.setAlignmentY(0);
    this.tmpScrollPaneZoomArea.setBounds(300, 300, 950, 250);
    this.tmpScrollPaneZoomArea.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    this.tmpScrollPaneZoomArea.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);


    this.lifeTimePanel.setCursor(this.getCursor());
    this.lifeTimePanel.add(this.tmpScrollPaneZoomArea);



}

public void makeZoomAreaTableForCluster() {
    this.setShowingPld(false);
    int numberOfColumns = this.getFinalRowsZoomArea()[0].length;
    int numberOfRows = this.getFinalRowsZoomArea().length;
    this.getUndoButton().setVisible(true);

	final String[][] rowsZoom=new String[numberOfRows][numberOfColumns];

	for(int i=0; i<numberOfRows; i++){

        rowsZoom[i][0] = this.getFinalRowsZoomArea()[i][0];

    }

    this.zoomModel = new ExtendedTableModel(this.getFinalColumnsZoomArea(), rowsZoom);

    ExtendedJvTable zoomTable = new ExtendedJvTable(this.zoomModel);

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


            String tmpValue = Gui.this.getFinalRowsZoomArea()[row][column];
	        String columnName=table.getColumnName(column);
	        Color fr=new Color(0,0,0);
	        c.setForeground(fr);

	        //column tou panw pinaka
            if (column == Gui.this.getWholeColZoomArea() && Gui.this.getWholeColZoomArea() != 0) {

	        	String description=table.getColumnName(column)+"\n";
                description = description + "First Transition ID:" + Gui.this.getDataController().getPhases().
        				get(column-1).getStartPos()+"\n";
                description = description + "Last Transition ID:" + Gui.this.getDataController().getPhases().
        				get(column-1).getEndPos()+"\n";
                description = description + "Total Changes For This Phase:" + Gui.this.getDataController().getPhases().
        				get(column-1).getTotalUpdates()+"\n";
                description = description + "Additions For This Phase:" + Gui.this.getDataController().getPhases().
        				get(column-1).getTotalAdditionsOfPhase()+"\n";
                description = description + "Deletions For This Phase:" + Gui.this.getDataController().getPhases().
        				get(column-1).getTotalDeletionsOfPhase()+"\n";
                description = description + "Updates For This Phase:" + Gui.this.getDataController().getPhases().
        				get(column-1).getTotalUpdatesOfPhase()+"\n";

                Gui.this.descriptionText.setText(description);

	        	Color cl = new Color(255,69,0,100);

        		c.setBackground(cl);
        		return c;
	        } else if (Gui.this.getSelectedColumnZoomArea() == 0) {
	        	if (isSelected){


                    String description = "Table:" + Gui.this.getFinalRowsZoomArea()[row][0] + "\n";
                    description = description + "Birth Version Name:" + Gui.this.getDataController().getAllPPLTables().get(Gui.this.getFinalRowsZoomArea()[row][0]).getBirth() + "\n";
                    description = description + "Birth Version ID:" + Gui.this.getDataController().getAllPPLTables().get(Gui.this.getFinalRowsZoomArea()[row][0]).getBirthVersionID() + "\n";
                    description = description + "Death Version Name:" + Gui.this.getDataController().getAllPPLTables().get(Gui.this.getFinalRowsZoomArea()[row][0]).getDeath() + "\n";
                    description = description + "Death Version ID:" + Gui.this.getDataController().getAllPPLTables().get(Gui.this.getFinalRowsZoomArea()[row][0]).getDeathVersionID() + "\n";
                    description = description + "Total Changes:" + Gui.this.getDataController().getAllPPLTables().get(Gui.this.getFinalRowsZoomArea()[row][0]).getTotalChanges() + "\n";
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

                        description = description + "Old Version:" + Gui.this.getDataController().getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getOldVersionName() + "\n";
                        description = description + "New Version:" + Gui.this.getDataController().getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getNewVersionName() + "\n\n";

	        			//description=description+"First Transition ID:"+DataController.getPhases().
		        				//get(column-1).getStartPos()+"\n";
		        		//description=description+"Last Transition ID:"+DataController.getPhases().
		        			//	get(column-1).getEndPos()+"\n\n";
                        description = description + "Table:" + Gui.this.getFinalRowsZoomArea()[row][0] + "\n";
                        description = description + "Birth Version Name:" + Gui.this.getDataController().getAllPPLTables().get(Gui.this.getFinalRowsZoomArea()[row][0]).getBirth() + "\n";
                        description = description + "Birth Version ID:" + Gui.this.getDataController().getAllPPLTables().get(Gui.this.getFinalRowsZoomArea()[row][0]).getBirthVersionID() + "\n";
                        description = description + "Death Version Name:" + Gui.this.getDataController().getAllPPLTables().get(Gui.this.getFinalRowsZoomArea()[row][0]).getDeath() + "\n";
                        description = description + "Death Version ID:" + Gui.this.getDataController().getAllPPLTables().get(Gui.this.getFinalRowsZoomArea()[row][0]).getDeathVersionID() + "\n";
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
        		} else if (numericValue > 0 && numericValue <= Gui.this.getSegmentSizeZoomArea()[3]) {

        			insersionColor=new Color(176,226,255);
	        	} else if (numericValue > Gui.this.getSegmentSizeZoomArea()[3] && numericValue <= 2 * Gui.this.getSegmentSizeZoomArea()[3]) {
        			insersionColor=new Color(92,172,238);
        		} else if (numericValue > 2 * Gui.this.getSegmentSizeZoomArea()[3] && numericValue <= 3 * Gui.this.getSegmentSizeZoomArea()[3]) {

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

	mouselistener.listenToRightClick3(zoomTable);

    this.tmpScrollPaneZoomArea.setViewportView(this.getZoomAreaTable());
    this.tmpScrollPaneZoomArea.setAlignmentX(0);
    this.tmpScrollPaneZoomArea.setAlignmentY(0);
    this.tmpScrollPaneZoomArea.setBounds(300, 300, 950, 250);
    this.tmpScrollPaneZoomArea.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    this.tmpScrollPaneZoomArea.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);


    this.lifeTimePanel.setCursor(this.getCursor());
    this.lifeTimePanel.add(this.tmpScrollPaneZoomArea);



}

	public void makeDetailedTable(String[] columns , String[][] rows, final boolean levelized){

        this.detailedModel = new ExtendedTableModel(columns, rows);

        ExtendedJvTable tmpLifeTimeTable = new ExtendedJvTable(this.detailedModel);

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


                if (Gui.this.getSelectedColumn() == 0) {
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
		        		} else if (numericValue > 0 && numericValue <= Gui.this.getSegmentSizeDetailedTable()[0]) {

		        			insersionColor=new Color(193,255,193);
			        	} else if (numericValue > Gui.this.getSegmentSizeDetailedTable()[0] && numericValue <= 2 * Gui.this.getSegmentSizeDetailedTable()[0]) {
		        			insersionColor=new Color(84,255,159);
		        		} else if (numericValue > 2 * Gui.this.getSegmentSizeDetailedTable()[0] && numericValue <= 3 * Gui.this.getSegmentSizeDetailedTable()[0]) {

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
		        		} else if (numericValue > 0 && numericValue <= Gui.this.getSegmentSizeDetailedTable()[1]) {

		        			insersionColor=new Color(176,226,255);
			        	} else if (numericValue > Gui.this.getSegmentSizeDetailedTable()[1] && numericValue <= 2 * Gui.this.getSegmentSizeDetailedTable()[1]) {
		        			insersionColor=new Color(92,172,238);
		        		} else if (numericValue > 2 * Gui.this.getSegmentSizeDetailedTable()[1] && numericValue <= 3 * Gui.this.getSegmentSizeDetailedTable()[1]) {

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
		        		} else if (numericValue > 0 && numericValue <= Gui.this.getSegmentSizeDetailedTable()[2]) {

		        			insersionColor=new Color(255,106,106);
			        	} else if (numericValue > Gui.this.getSegmentSizeDetailedTable()[2] && numericValue <= 2 * Gui.this.getSegmentSizeDetailedTable()[2]) {
		        			insersionColor=new Color(255,0,0);
		        		} else if (numericValue > 2 * Gui.this.getSegmentSizeDetailedTable()[2] && numericValue <= 3 * Gui.this.getSegmentSizeDetailedTable()[2]) {

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
	
	public void importData(String fileName) throws IOException, RecognitionException {


        BufferedReader br = new BufferedReader(new FileReader(fileName));

        String line;

        while(true) {
			line = br.readLine();
            if (line == null)
				break;
			if(line.contains("Project-name")){
				String[] projectNameTable=line.split(":");
                this.setProjectName(projectNameTable[1]);
			}
			else if(line.contains("Dataset-txt")){
				String[] datasetTxtTable=line.split(":");
                this.setDatasetTxt(datasetTxtTable[1]);
			}
			else if(line.contains("Input-csv")){
				String[] inputCsvTable=line.split(":");
                this.setInputCsv(inputCsvTable[1]);
			}
			else if(line.contains("Assessement1-output")){
				String[] outputAss1=line.split(":");
                this.setOutputAssessment1(outputAss1[1]);
			}
			else if(line.contains("Assessement2-output")){
				String[] outputAss2=line.split(":");
                this.setOutputAssessment2(outputAss2[1]);
			}
			else if(line.contains("Transition-xml")){
				String[] transitionXmlTable=line.split(":");
                this.setTransitionsFile(transitionXmlTable[1]);
			}


        }

        br.close();


        System.out.println("Project Name:" + this.getProjectName());
        System.out.println("Dataset txt:" + this.getDatasetTxt());
        System.out.println("Input Csv:" + this.getInputCsv());
        System.out.println("Output Assessment1:" + this.getOutputAssessment1());
        System.out.println("Output Assessment2:" + this.getOutputAssessment2());
        System.out.println("Transitions File:" + this.getTransitionsFile());

        this.setDataController(new DataController(this.getDatasetTxt(), this.getTransitionsFile()));
        System.out.println(this.getDataController().getAllPPLTables().size());
		System.out.println(fileName);

        String logSentence="load project test \n ";
        logSentence += "Project Name:" + this.getProjectName() + "\n";
        logSentence += "Dataset txt:" + this.getDatasetTxt() + "\n";
        logSentence += "Input Csv:" + this.getInputCsv() + "\n";
        logSentence += "Output Assessment1:" + this.getOutputAssessment1() + "\n";
        logSentence += "Output Assessment2:" + this.getOutputAssessment2() + "\n";
        logSentence += "Transitions File:" + this.getTransitionsFile() + "\n";
        logSentence += this.getDataController().getAllPPLTables().size() + "\n";
		
		
		tlp.writeString("-----[ Projects Details ]-----");
		tlp.writeString(logSentence);
 

        this.fillTable();
        this.fillTree();

        this.setCurrentProject(fileName);
        
	}

    private class ColumnListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent event) {
            if (event.getValueIsAdjusting()) {
                return;
            }

        }
    }
	
	private void fillTable() {
		this.getDataController().constructTableConstructionIDU();
		final String[] columns=this.getDataController().getTableColumns("IDU");
		final String[][] rows=this.getDataController().getTableRows("IDU");
		this.setSegmentSizeZoomArea(this.getDataController().getSegmentSize("IDU")); 



        this.setFinalColumnsZoomArea(columns);
        System.out.println(Arrays.toString( this.getFinalColumnsZoomArea())); 
        
        this.setFinalRowsZoomArea(rows);
        this.getTabbedPane().setSelectedIndex(0);
        tlp.writeTable("General Table", columns, rows);
        this.makeGeneralTableIDU();

        this.setTimeWeight((float) 0.5);
        this.setChangeWeight((float) 0.5);
        this.setPreProcessingTime(false);
        this.setPreProcessingChange(false);
        if (this.getDataController().getAllPPLTransitions().size() < 56) {
            this.setNumberOfPhases(40);
        }
        else{
            this.setNumberOfPhases(56);
        }
        this.setNumberOfClusters(14);

        System.out.println(this.getTimeWeight() + " " + this.getChangeWeight());


		Double b=new Double(0.3);
		Double d=new Double(0.3);
		Double c=new Double(0.3);

        //mainEngine.parseInput();
		System.out.println("\n\n\n");
        //mainEngine.extractPhases(this.getNumberOfPhases());

       // mainEngine.connectTransitionsWithPhases(this.getDataController());
       // this.getDataController().setPhaseCollectors(mainEngine.getPhaseCollectors());
		this.getDataController().setPhaseCollectors(this.getNumberOfPhases(),this.getInputCsv(), this.getOutputAssessment1(), this.getOutputAssessment2(), this.getTimeWeight(),this.getChangeWeight(), this.getPreProcessingTime(), this.getPreProcessingChange());
        this.getDataController().extractClusters(this.getNumberOfClusters(),b, d, c);
        
        //mainEngine2.print();

        if (this.getDataController().getPhaseCollectors().size() != 0) {
        	this.getDataController().constructTableWithClusters();
            final String[] columnsP=this.getDataController().getTableColumns("Clusters");;
            final String[][] rowsP=this.getDataController().getTableRows("Clusters");
            this.setSegmentSize(this.getDataController().getSegmentSize("Clusters"));
            this.setFinalColumns(columnsP);
            this.setFinalRows(rowsP);      
            this.getTabbedPane().setSelectedIndex(0);
            this.makeGeneralTablePhases();
            this.fillClustersTree();
        }
        System.out.println("Schemas:" + this.getDataController().getAllPPLSchemas().size());
        System.out.println("Transitions:" + this.getDataController().getAllPPLTransitions().size());
        System.out.println("Tables:" + this.getDataController().getAllPPLTables().size());

        String logSentence = "Schemas:" + this.getDataController().getAllPPLSchemas().size() + "\n";//testing Giorgos
        logSentence += "Transitions:" + this.getDataController().getAllPPLTransitions().size() + "\n";//testing Giorgos
        logSentence += "Tables:" + this.getDataController().getAllPPLTables().size() + "\n";//testing Giorgos

        tlp.writeString("-----[ Overview Output ]-----");
        tlp.writeString(logSentence);
	}
	

	

	public void fillTree(){
        this.tablesTree = this.getDataController().constructTree("General");
        tlp.writeTree("Tree with Versions",this.tablesTree);
        
        mouselistener.listenTreeSelection(tablesTree);
        
        

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

        this.tablesTree = this.getDataController().constructTree("Phases");
        tlp.writeTree("Tree with Phases", this.tablesTree);
 
        mouselistener.listenTreeSelection(tablesTree);
        
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

        this.tablesTree =  this.getDataController().constructTree("PhasesWithClusters");   
        tlp.writeTree("Tree with Clusters",this.tablesTree);      
       
        mouselistener.listenTreeSelection(tablesTree);
        
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

    public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getDatasetTxt() {
		return datasetTxt;
	}

	public void setDatasetTxt(String datasetTxt) {
		this.datasetTxt = datasetTxt;
	}

	public String getInputCsv() {
		return inputCsv;
	}

	public void setInputCsv(String inputCsv) {
		this.inputCsv = inputCsv;
	}

	public String getOutputAssessment1() {
		return outputAssessment1;
	}

	public void setOutputAssessment1(String outputAssessment1) {
		this.outputAssessment1 = outputAssessment1;
	}

	public String getOutputAssessment2() {
		return outputAssessment2;
	}

	public void setOutputAssessment2(String outputAssessment2) {
		this.outputAssessment2 = outputAssessment2;
	}

	public String getTransitionsFile() {
		return transitionsFile;
	}

	public void setTransitionsFile(String transitionsFile) {
		this.transitionsFile = transitionsFile;
	}

	public String getCurrentProject() {
		return currentProject;
	}

	public void setCurrentProject(String currentProject) {
		this.currentProject = currentProject;
	}

	public Integer[] getSegmentSizeDetailedTable() {
		return segmentSizeDetailedTable;
	}

	public void setSegmentSizeDetailedTable(Integer[] segmentSizeDetailedTable) {
		this.segmentSizeDetailedTable = segmentSizeDetailedTable;
	}

	public DataController getDataController() {
		return dataController;
	}

	public void setDataController(DataController dataController) {
		this.dataController = dataController;
	}

	public JTabbedPane getTabbedPane() {
		return tabbedPane;
	}

	public JButton getZoomInButton() {
		return zoomInButton;
	}

	public JButton getZoomOutButton() {
		return zoomOutButton;
	}

	public Integer[] getSegmentSizeZoomArea() {
		return segmentSizeZoomArea;
	}

	public void setSegmentSizeZoomArea(Integer[] segmentSizeZoomArea) {
		this.segmentSizeZoomArea = segmentSizeZoomArea;
	}

	public String[] getFinalColumnsZoomArea() {
		return finalColumnsZoomArea;
	}

	public void setFinalColumnsZoomArea(String[] finalColumnsZoomArea) {
		this.finalColumnsZoomArea = finalColumnsZoomArea;
	}

	public String[][] getFinalRowsZoomArea() {
		return finalRowsZoomArea;
	}

	public void setFinalRowsZoomArea(String[][] finalRowsZoomArea) {
		this.finalRowsZoomArea = finalRowsZoomArea;
	}

	public int getWholeCol() {
		return wholeCol;
	}

	public void setWholeCol(int wholeCol) {
		this.wholeCol = wholeCol;
	}

	public Float getTimeWeight() {
		return timeWeight;
	}

	public void setTimeWeight(Float timeWeight) {
		this.timeWeight = timeWeight;
	}

	public Float getChangeWeight() {
		return changeWeight;
	}

	public void setChangeWeight(Float changeWeight) {
		this.changeWeight = changeWeight;
	}

	public Boolean getPreProcessingTime() {
		return preProcessingTime;
	}

	public void setPreProcessingTime(Boolean preProcessingTime) {
		this.preProcessingTime = preProcessingTime;
	}

	public Boolean getPreProcessingChange() {
		return preProcessingChange;
	}

	public void setPreProcessingChange(Boolean preProcessingChange) {
		this.preProcessingChange = preProcessingChange;
	}

	public Integer getNumberOfPhases() {
		return numberOfPhases;
	}

	public void setNumberOfPhases(Integer numberOfPhases) {
		this.numberOfPhases = numberOfPhases;
	}

	public Integer[] getSegmentSize() {
		return segmentSize;
	}

	public void setSegmentSize(Integer[] segmentSize) {
		this.segmentSize = segmentSize;
	}

	public String[] getFinalColumns() {
		return finalColumns;
	}

	public void setFinalColumns(String[] finalColumns) {
		this.finalColumns = finalColumns;
	}

	public String[][] getFinalRows() {
		return finalRows;
	}

	public void setFinalRows(String[][] finalRows) {
		this.finalRows = finalRows;
	}

	public Integer getNumberOfClusters() {
		return numberOfClusters;
	}

	public void setNumberOfClusters(Integer numberOfClusters) {
		this.numberOfClusters = numberOfClusters;
	}

	public Double getBirthWeight() {
		return birthWeight;
	}

	public void setBirthWeight(Double birthWeight) {
		this.birthWeight = birthWeight;
	}

	public Double getDeathWeight() {
		return deathWeight;
	}

	public void setDeathWeight(Double deathWeight) {
		this.deathWeight = deathWeight;
	}

	public Double getChangeWeightCl() {
		return changeWeightCl;
	}

	public void setChangeWeightCl(Double changeWeightCl) {
		this.changeWeightCl = changeWeightCl;
	}

	public ArrayList<String> getTablesSelected() {
		return tablesSelected;
	}

	public void setTablesSelected(ArrayList<String> tablesSelected) {
		this.tablesSelected = tablesSelected;
	}

	public int getRowHeight() {
		return rowHeight;
	}

	public void setRowHeight(int rowHeight) {
		this.rowHeight = rowHeight;
	}

	public int getColumnWidth() {
		return columnWidth;
	}

	public void setColumnWidth(int columnWidth) {
		this.columnWidth = columnWidth;
	}

	public ExtendedJvTable getZoomAreaTable() {
		return zoomAreaTable;
	}

	public void setZoomAreaTable(ExtendedJvTable zoomAreaTable) {
		this.zoomAreaTable = zoomAreaTable;
	}

	public JButton getShowThisToPopup() {
		return showThisToPopup;
	}

	public JButton getUndoButton() {
		return undoButton;
	}

	public String[] getFirstLevelUndoColumnsZoomArea() {
		return firstLevelUndoColumnsZoomArea;
	}

	public void setFirstLevelUndoColumnsZoomArea(String[] firstLevelUndoColumnsZoomArea) {
		this.firstLevelUndoColumnsZoomArea = firstLevelUndoColumnsZoomArea;
	}

	public String[][] getFirstLevelUndoRowsZoomArea() {
		return firstLevelUndoRowsZoomArea;
	}

	public void setFirstLevelUndoRowsZoomArea(String[][] firstLevelUndoRowsZoomArea) {
		this.firstLevelUndoRowsZoomArea = firstLevelUndoRowsZoomArea;
	}

	public ExtendedJvTable getLifeTimeTable() {
		return LifeTimeTable;
	}

	public void setLifeTimeTable(ExtendedJvTable lifeTimeTable) {
		LifeTimeTable = lifeTimeTable;
	}

	public JButton getUniformlyDistributedButton() {
		return uniformlyDistributedButton;
	}

	public JButton getNotUniformlyDistributedButton() {
		return notUniformlyDistributedButton;
	}

	public int[] getSelectedRowsFromMouse() {
		return selectedRowsFromMouse;
	}

	public void setSelectedRowsFromMouse(int[] selectedRowsFromMouse) {
		this.selectedRowsFromMouse = selectedRowsFromMouse;
	}

	public ArrayList<String> getSelectedFromTree() {
		return selectedFromTree;
	}

	public void setSelectedFromTree(ArrayList<String> selectedFromTree) {
		this.selectedFromTree = selectedFromTree;
	}

	public int getWholeColZoomArea() {
		return wholeColZoomArea;
	}

	public void setWholeColZoomArea(int wholeColZoomArea) {
		this.wholeColZoomArea = wholeColZoomArea;
	}

	public int getSelectedColumn() {
		return selectedColumn;
	}

	public void setSelectedColumn(int selectedColumn) {
		this.selectedColumn = selectedColumn;
	}

	public boolean isShowingPld() {
		return showingPld;
	}

	public void setShowingPld(boolean showingPld) {
		this.showingPld = showingPld;
	}

	public int getSelectedColumnZoomArea() {
		return selectedColumnZoomArea;
	}

	public void setSelectedColumnZoomArea(int selectedColumnZoomArea) {
		this.selectedColumnZoomArea = selectedColumnZoomArea;
	}

	private class RowListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent event) {
            if (event.getValueIsAdjusting()) {
                return;
            }

            int selectedRow = Gui.this.getLifeTimeTable().getSelectedRow();

            Gui.this.selectedRows.add(selectedRow);

        }
    }



		
	
}