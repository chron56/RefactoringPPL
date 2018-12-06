package test.testEngine;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

public class testAgent {
	PrintWriter out2;
	
	public testAgent() {
		try {
			out2 = new PrintWriter ("file.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	 public void testLog(String logSentence) { 	
			out2.write(logSentence);	
	}
	 
	public void testLog(Object e) { 

		try {
			byte array[] = convertToBytes(e);
			
			FileOutputStream fos= new FileOutputStream("filebytes.txt");
			fos.write(array);
			fos.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
	}
	
	private byte[] convertToBytes(Object object) throws IOException{
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
				ObjectOutput out = new ObjectOutputStream(bos)){
				out.writeObject(object);
				return bos.toByteArray();
		}
	}
	 
	
	public  Object convertFromBytes(String file) throws IOException, ClassNotFoundException {
		FileInputStream fileInputStream = new FileInputStream(file);
		BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
		ObjectInputStream objectInputStream = new ObjectInputStream(bufferedInputStream);
		Object object = objectInputStream.readObject();
		objectInputStream.close();
		return object;
	}
	
	
	 public void closeLog() { 

			out2.close();	
	}
}
