package test.testEngine;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Enumeration;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

public class TestLoadProject {
	
	PrintWriter writer;
	int option=1;
	public TestLoadProject(String filename) {
		
		File file = new File(filename);
        FileWriter fileWriter;
        
		try {
			fileWriter = new FileWriter(file, true);
	        writer = new PrintWriter(fileWriter);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void writeString(String aString) {
		writer.println(aString);
		
	}
	
	public void writeTable(String name, String[] columns,String[][] rows) {
		writer.println("\n-----[ START "+name+" ]-----");
		writer.println("Columns : " +Arrays.toString(columns));
		writer.println("Rows : " +Arrays.deepToString(rows));
		writer.println("\n-----[ END ]-----\n");
	}
	
	public void writeTree(String name,JTree tree) {
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel().getRoot();
        Enumeration e = root.preorderEnumeration();
        writer.println("\n-----[ START "+name+" ]-----");
        while(e.hasMoreElements()){
           writer.print(e.nextElement());
           writer.print(" ");
        }
        writer.println();
        writer.println("\n-----[ END ]-----\n");
	}
	
	public void closeFile() {
		writer.flush();
		writer.close();	
	}
	

}