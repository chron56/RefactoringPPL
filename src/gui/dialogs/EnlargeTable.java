package gui.dialogs;

import gui.tableElements.commons.JvTable;
import gui.tableElements.commons.MyTableModel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;

public class EnlargeTable extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private String[][] finalRowsZoomArea;
	private String[] finalColumnsZoomArea;
	private JvTable table;
	private int rowHeight=10;
	private int columnWidth=1;
	private Integer[] segmentSize=new Integer[4];
	private JScrollPane tmpScrollPane;

	/**
	 * Create the dialog.
	 */
	public EnlargeTable(String[][] frows,String[] columns,Integer[] seg) {
		finalRowsZoomArea=frows;
		finalColumnsZoomArea=columns;
		segmentSize=seg;
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setLayout(new BorderLayout());
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		int numberOfColumns=finalRowsZoomArea[0].length;
		int numberOfRows=finalRowsZoomArea.length;
		
		
		String[][] rows=new String[numberOfRows][numberOfColumns];
		
		for(int i=0; i<numberOfRows; i++){
			
			rows[i][0]=finalRowsZoomArea[i][0];
			
		}
		
		MyTableModel zoomModel=new MyTableModel(finalColumnsZoomArea, rows);
		
		final JvTable generalTable=new JvTable(zoomModel);
		
		generalTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		for(int i=0; i<generalTable.getRowCount(); i++){
				generalTable.setRowHeight(i, rowHeight);
				
		}

		generalTable.setShowGrid(false);
		generalTable.setIntercellSpacing(new Dimension(0, 0));
		
		for(int i=0; i<generalTable.getColumnCount(); i++){
			if(i==0){
				generalTable.getColumnModel().getColumn(0).setPreferredWidth(60);
				
			}
			else{
				generalTable.getColumnModel().getColumn(i).setPreferredWidth(columnWidth);
			
			}
		}
		
		generalTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer()
		{
		    
			private static final long serialVersionUID = 1L;

			@Override
		    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		    {
		        final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		        
		        String tmpValue=finalRowsZoomArea[row][column];
		        String columnName=table.getColumnName(column);
		        Color fr=new Color(0,0,0);
		        
		        c.setForeground(fr);
		        setOpaque(true);
		      
		        

		        try{
		        	int numericValue=Integer.parseInt(tmpValue);
		        	Color insersionColor=null;
					setToolTipText(Integer.toString(numericValue));

		        	
	        		if(numericValue==0){
	        			insersionColor=new Color(154,205,50,200);
	        		}
	        		else if(numericValue> 0&& numericValue<=segmentSize[3]){
	        			
	        			insersionColor=new Color(176,226,255);
		        	}
	        		else if(numericValue>segmentSize[3] && numericValue<=2*segmentSize[3]){
	        			insersionColor=new Color(92,172,238);
	        		}
	        		else if(numericValue>2*segmentSize[3] && numericValue<=3*segmentSize[3]){
	        			
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
	        				setToolTipText(columnName);
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
		
		
		
		
		table = generalTable;
		table.setBounds(0,0,1100,580);
		tmpScrollPane=new JScrollPane();
		tmpScrollPane.setViewportView(table);
		tmpScrollPane.setAlignmentX(0);
		tmpScrollPane.setAlignmentY(0);
	    tmpScrollPane.setBounds(0,30,1270,620);
	    tmpScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	    tmpScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	    
	    
	    contentPanel.add(tmpScrollPane, BorderLayout.CENTER);

		
		JButton zoomInButton = new JButton("Zoom In");
		zoomInButton.setBounds(1000, 0, 100, 25);
		
		zoomInButton.addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseClicked(MouseEvent e) {
				rowHeight=rowHeight+1;
				columnWidth=columnWidth+1;
				table.setZoom(rowHeight,columnWidth);
				
			}
		});
		
		JButton zoomOutButton = new JButton("Zoom Out");
		zoomOutButton.setBounds(1110, 0, 100, 25);
		
		zoomOutButton.addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseClicked(MouseEvent e) {
				rowHeight=rowHeight-1;
				columnWidth=columnWidth-1;
				if(rowHeight<1){
					rowHeight=1;
				}
				if (columnWidth<1) {
					columnWidth=1;
				}
				table.setZoom(rowHeight,columnWidth);
				
			}
		});
		
		JPanel subPanel = new JPanel();

		subPanel.add( zoomInButton);
		subPanel.add( zoomOutButton);
		contentPanel.add(subPanel,BorderLayout.NORTH);
		
	}
}
