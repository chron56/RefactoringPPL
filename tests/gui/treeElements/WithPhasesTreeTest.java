package gui.treeElements;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.swing.JTree;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import test.testEngine.testAgent;

public class WithPhasesTreeTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		testAgent agent = new testAgent();
		Object temp1,temp2;
		try {
			temp1 = agent.convertFromBytes("filebytes.ser");
			temp2 = agent.convertFromBytes("WithPhasesTreefilebytes.ser");
			JTree expected = (JTree)temp1;
			JTree actual = (JTree)temp2;
			assertEquals( expected, actual);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		}

	}

}
