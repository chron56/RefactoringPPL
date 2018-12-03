package test.testEngine;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class testAgent {
	PrintWriter out;
	
	public testAgent() {
		try {
			out = new PrintWriter ("file.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	 public void testLog(String logSentence) { 
		//File file = new File ("C:/Users/Me/Desktop/directory/file.txt");
			out.write(logSentence);
			//out.close();	
	}
	 
	 public void closeLog() { 

			out.close();	
	}
}
