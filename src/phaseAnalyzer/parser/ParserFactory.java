/**
 * 
 */
package phaseAnalyzer.parser;

public class ParserFactory {

	public IParser createParser(String concreteClassName){
		if (concreteClassName.equals("SimpleTextParser")){
			return new SimpleTextParser();
		}

		return null;
	}
	
}
