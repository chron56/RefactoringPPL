import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ButtonT1Test.class, ButtonT2Test.class, ButtonT3Test.class, CLEProjectTest.class })
public class AllTests {
	public static void main(String[] args) {									
		Result result1 = JUnitCore.runClasses(ButtonT1Test.class);					
		for (Failure failure : result1.getFailures()) {							
			System.out.println(failure.toString());					
		}		
		System.out.println("Result=="+result1.wasSuccessful());
		
		Result result2 = JUnitCore.runClasses(ButtonT2Test.class);					
		for (Failure failure : result2.getFailures()) {							
			System.out.println(failure.toString());					
		}		
		System.out.println("Result=="+result2.wasSuccessful());
		
		Result result3 = JUnitCore.runClasses(ButtonT3Test.class);					
		for (Failure failure : result3.getFailures()) {							
			System.out.println(failure.toString());					
		}		
		System.out.println("Result=="+result3.wasSuccessful());
		
		Result result4 = JUnitCore.runClasses(CLEProjectTest.class);					
		for (Failure failure : result4.getFailures()) {							
			System.out.println(failure.toString());					
		}		
		System.out.println("Result=="+result4.wasSuccessful());
   }		
}
