import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;

public class ButtonT1Test {

	public static void assertReaders(BufferedReader expected,
	          BufferedReader actual) throws IOException {
	    String line;
	    while ((line = expected.readLine()) != null) {
	        assertEquals(line, actual.readLine());
	    }

	    assertNull("Actual had more lines then the expected.", actual.readLine());
	    assertNull("Expected had more lines then the actual.", expected.readLine());
	}
	@Test
	public void test() {
		try {

			BufferedReader expectedgeneralTable= new BufferedReader(new FileReader("#ButtonT1-generalTable-makeGeneralTableIDU.txt"));
			BufferedReader actualgeneralTable = new BufferedReader(new FileReader("ButtonT1-generalTable-makeGeneralTableIDU.txt"));
			assertReaders(expectedgeneralTable,actualgeneralTable);
			BufferedReader expectedtablesTree= new BufferedReader(new FileReader("#ButtonT1-tablesTree-fillTree.txt"));
			BufferedReader actualtablesTree = new BufferedReader(new FileReader("ButtonT1-tablesTree-fillTree.txt"));
			assertReaders(expectedtablesTree,actualtablesTree);
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
}
