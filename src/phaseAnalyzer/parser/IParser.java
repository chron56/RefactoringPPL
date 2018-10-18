package phaseAnalyzer.parser;

import phaseAnalyzer.commons.TransitionHistory;

public interface IParser {
	public abstract TransitionHistory parse(String fileName, String delimeter);
}
