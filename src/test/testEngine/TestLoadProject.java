package test.testEngine;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

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
	public void updateOption() {
		option++;
	}
	public String getOption() {
		if(option==1) {
			return "CLEProject-";
		}
		else {
			return "ButtonT"+String.valueOf(option-1)+"-" ;
		}
		
	}
	
	public void writeToFile(String aString) {
		writer.println(aString);
		
	}
	
	public void writeObjectToFile(String filename,Object obj) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out;
		try {
			out = new ObjectOutputStream(bos);
			out.writeObject(obj);
			FileOutputStream fos= new FileOutputStream(filename);
			fos.write(bos.toByteArray());
			fos.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	public void closeFile() {
		writer.flush();
		writer.close();
	
	}
	

}
