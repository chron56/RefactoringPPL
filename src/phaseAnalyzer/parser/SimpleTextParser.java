/**
 * 
 */
package phaseAnalyzer.parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Scanner;
import java.util.StringTokenizer;

import phaseAnalyzer.commons.TransitionHistory;
import phaseAnalyzer.commons.TransitionStats;

public class SimpleTextParser implements IParser {

	/* (non-Javadoc)
	 * @see parser.IParser#parse(java.lang.String, java.lang.String)
	 */
	@Override
	public TransitionHistory parse(String fileName, String delimeter) {
		int transitionId; int time; 
		String oldVersionFile; String newVersionFile; 
		int numOldTables; int numNewTables;
		int numOldAtributes; int numNewAttributes; 
		int numTablesIns; int numTablesDel; 
		int numAttrIns; int numAttrDel;
		int numAttrWithTypeAlt; int numAttrInKeyAlt;
		int numAttrInsInNewTables; int numAttrDelWithDelTables;int totalUpdatesInOneTr;
		int totalUpdates=0;
		
		final int _NumFields = 16;
		
		Scanner inputStream = null;
		TransitionHistory transitionHistory =  new TransitionHistory();
		
		//Opening files for read and write, checking exception
		try {
			System.out.println("csv:"+fileName);
			inputStream = new Scanner(new FileInputStream(fileName));
			
		} catch (FileNotFoundException e) {
			System.out.println("Problem opening files.");
			System.exit(0);
		}
		
		int count = 0;
		//process the title of the csv
		String line = inputStream.nextLine();
		count++;

		//process the actual rows one by one
		//TODO add a method parseRows (including header row) and a method getNextRow
		while (inputStream.hasNextLine()) {
			line = inputStream.nextLine();
			count++;
			
			StringTokenizer tokenizer = new StringTokenizer(line, delimeter);
			if(tokenizer.countTokens() != _NumFields){
				System.out.println("Wrong Input format. I found " + tokenizer.countTokens() + " values, but I expect " + _NumFields + " values per row!");
				System.exit(0);				
			}
			
			
			String s = tokenizer.nextToken();
			transitionId = Integer.parseInt(s);
			s = tokenizer.nextToken();
			time = Integer.parseInt(s);		
			oldVersionFile = tokenizer.nextToken();
			newVersionFile = tokenizer.nextToken();
			s = tokenizer.nextToken();
			numOldTables = Integer.parseInt(s);		
			s = tokenizer.nextToken();
			numNewTables= Integer.parseInt(s);  
			s = tokenizer.nextToken();
			numOldAtributes= Integer.parseInt(s);  
			s = tokenizer.nextToken();
			numNewAttributes= Integer.parseInt(s);  
			s = tokenizer.nextToken();
			numTablesIns= Integer.parseInt(s);  
			s = tokenizer.nextToken();
			numTablesDel= Integer.parseInt(s);  
			s = tokenizer.nextToken();
			numAttrIns= Integer.parseInt(s); 
			s = tokenizer.nextToken();
			numAttrDel= Integer.parseInt(s); 
			s = tokenizer.nextToken();
			numAttrWithTypeAlt= Integer.parseInt(s);  
			s = tokenizer.nextToken();
			numAttrInKeyAlt= Integer.parseInt(s);  
			s = tokenizer.nextToken();
			numAttrInsInNewTables= Integer.parseInt(s);  
			s = tokenizer.nextToken();
			numAttrDelWithDelTables= Integer.parseInt(s); 
			
			totalUpdatesInOneTr=numAttrIns+numAttrDel+numAttrWithTypeAlt+numAttrInKeyAlt+numAttrInsInNewTables+numAttrDelWithDelTables;
			
			TransitionStats v = new TransitionStats(transitionId, time, oldVersionFile, newVersionFile, numOldTables, numNewTables, numOldAtributes, numNewAttributes, numTablesIns, numTablesDel, numAttrIns,numAttrDel,numAttrWithTypeAlt, numAttrInKeyAlt, numAttrInsInNewTables, numAttrDelWithDelTables,totalUpdatesInOneTr);
			transitionHistory.addValue(v);
			totalUpdates= totalUpdates+numAttrIns+numAttrDel+numAttrWithTypeAlt+numAttrInKeyAlt+numAttrInsInNewTables+numAttrDelWithDelTables;
		}
		
		transitionHistory.setTotalUpdates(totalUpdates);
		transitionHistory.setTotalTime();
		System.out.println(count + " lines parsed");
		inputStream.close();

		/*
		 * Now, we complete the distance from previous, for all the transitions
		 */
		//TODO add a dedicated method for the task
	    Iterator<TransitionStats> transitionsIter = transitionHistory.getValues().iterator();
	    if (transitionHistory.getValues().size() == 0)
	    	return transitionHistory;
	    
	    TransitionStats previousTransition, currentTransition;
	    //Do the first
	    currentTransition = transitionsIter.next();
	    currentTransition.setTimeDistFromPrevious(0);
	    previousTransition = currentTransition;
	    
	    while (transitionsIter.hasNext()){
		    currentTransition = transitionsIter.next();
		    currentTransition.setTimeDistFromPrevious(currentTransition.getTime() - previousTransition.getTime());
		    previousTransition = currentTransition;
	    }
	  
		
		return transitionHistory;
	}

}
