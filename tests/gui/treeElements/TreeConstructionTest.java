package gui.treeElements;

import static org.junit.Assert.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TreeConstructionTest {
	private static final File dir = new File("/");
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		Assert.assertTrue("Unable to create " + dir.getAbsolutePath(), dir.exists());
	}

	@After
	public void tearDown() throws Exception {
	}

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
		fail("Not yet implemented");
	}

}
