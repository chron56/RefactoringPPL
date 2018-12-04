package test.testEngine;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class testAgent {
	private File file;
	private FileWriter fileWriter;

	public testAgent(String fileName) {
		try {
			this.file = new File(fileName);
			this.fileWriter = new FileWriter(this.file, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeLog(String logSentence) {
		try {
			this.fileWriter.write(logSentence);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void closeLog() {
		try {
			this.fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
