import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;

public class CLEProjectTest {

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

			BufferedReader expectedmakeGeneralTableIDU= new BufferedReader(new FileReader("#CLEProject-generalTable-makeGeneralTableIDU.txt"));
			BufferedReader actualmakeGeneralTableIDU = new BufferedReader(new FileReader("CLEProject-generalTable-makeGeneralTableIDU.txt"));
			assertReaders(expectedmakeGeneralTableIDU,actualmakeGeneralTableIDU);
			BufferedReader expectedmakeGeneralTablePhases= new BufferedReader(new FileReader("#CLEProject-generalTable-makeGeneralTablePhases.txt"));
			BufferedReader actualmakeGeneralTablePhases = new BufferedReader(new FileReader("CLEProject-generalTable-makeGeneralTablePhases.txt"));
			assertReaders(expectedmakeGeneralTablePhases,actualmakeGeneralTablePhases);
			BufferedReader expectedfillClustersTree= new BufferedReader(new FileReader("#CLEProject-tablesTree-fillClustersTree.txt"));
			BufferedReader actualfillClustersTree = new BufferedReader(new FileReader("CLEProject-tablesTree-fillClustersTree.txt"));
			assertReaders(expectedfillClustersTree,actualfillClustersTree);
			BufferedReader expectedfillTree= new BufferedReader(new FileReader("#CLEProject-tablesTree-fillTree.txt"));
			BufferedReader actualfillTree = new BufferedReader(new FileReader("CLEProject-tablesTree-fillTree.txt"));
			assertReaders(expectedfillTree,actualfillTree);
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}

}
