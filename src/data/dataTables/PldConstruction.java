package data.dataTables;

import java.util.ArrayList;

public abstract class PldConstruction {
	private Integer[][] schemaColumnId=null;
	private int columnsNumber=0;

	public abstract int getColumnSize();
	public abstract Integer[] getSegmentSize();
	public abstract void setSegmentSize();
	public abstract ArrayList<String> setColumnLabel(ArrayList<String> columnsList);
	public abstract void setColumnsNumber(int size);
	public abstract void setColumnId(Integer[][] schemaColumnId);
	public abstract ArrayList<String[]> constructParticularRows(ArrayList<String[]> AllRows);

	
	public String[] constructColumns(){		
		ArrayList<String> columnsList=new ArrayList<String>();		
		schemaColumnId=new Integer[getColumnSize()][2];		
		for(int i=0;i<getColumnSize();i++){
			schemaColumnId[i][0]=i;
			if(i==0){
				schemaColumnId[i][1]=1;
			}
			else{
				schemaColumnId[i][1]=schemaColumnId[i-1][1]+1;
			}
		}		
		columnsList.add("Table name");	
		setColumnLabel(columnsList);		
		columnsNumber=columnsList.size();
		setColumnsNumber(columnsList.size()); 
		setColumnId(schemaColumnId);
		String[] tmpcolumns=new String[columnsList.size()];		
		for(int j=0; j<columnsList.size(); j++ ){			
			tmpcolumns[j]=columnsList.get(j);			
		}		
		return(tmpcolumns);		
	}
	
	public String[][] constructRows(){		
		ArrayList<String[]> allRows=new ArrayList<String[]>();
		allRows = constructParticularRows(allRows);
		String[][] tmpRows=new String[allRows.size()][columnsNumber];		
		for(int z=0; z<allRows.size(); z++){			
			String[] tmpOneRow=allRows.get(z);
			for(int j=0; j<tmpOneRow.length; j++ ){				
				tmpRows[z][j]=tmpOneRow[j];				
			}			
		}		
		setSegmentSize();		
		return tmpRows;
		
	}
	

}
