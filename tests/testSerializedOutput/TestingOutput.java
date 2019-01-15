package testSerializedOutput;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;

public class TestingOutput {

	public static void assertReaders(BufferedReader expected,
	          BufferedReader actual) throws IOException {
	    String line;
	    while ((line = expected.readLine()) != null) {
	        assertEquals(line, actual.readLine());
	    }

	}
	@Test
	public void test() {
		try {
			BufferedReader expectedOutput= new BufferedReader(new FileReader("serializedOutputsGroundTruth/wannabeTestingOutput-Atlas.txt"));
			BufferedReader actualOutput = new BufferedReader(new FileReader("wannabeTestingOutput.txt"));
			assertReaders(expectedOutput,actualOutput);
			System.out.println("The two files are identical");
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}

}
