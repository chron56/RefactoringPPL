/**
 * This class represents the stats of a single transition of the input.
 */
package phaseAnalyzer.commons;

public class TransitionStats {
	public TransitionStats(int transitionId, int time, String oldVersionFile,
			String newVersionFile, int numOldTables, int numNewTables,
			int numOldAtributes, int numNewAttributes, int numTablesIns,
			int numTablesDel, int numAttrIns, int numAttrDel,
			int numAttrWithTypeAlt, int numAttrInKeyAlt,
			int numAttrInsInNewTables, int numAttrDelWithDelTables,int totalUpdatesInTr) {
		
		this.transitionId = transitionId;
		this.time = time;
		this.oldVersionFile = oldVersionFile;
		this.newVersionFile = newVersionFile;
		this.numOldTables = numOldTables;
		this.numNewTables = numNewTables;
		this.numOldAtributes = numOldAtributes;
		this.numNewAttributes = numNewAttributes;
		this.numTablesIns = numTablesIns;
		this.numTablesDel = numTablesDel;
		this.numAttrIns = numAttrIns;
		this.numAttrDel = numAttrDel;
		this.numAttrWithTypeAlt = numAttrWithTypeAlt;
		this.numAttrInKeyAlt = numAttrInKeyAlt;
		this.numAttrInsInNewTables = numAttrInsInNewTables;
		this.numAttrDelWithDelTables = numAttrDelWithDelTables;
		this.totalUpdatesInTr = totalUpdatesInTr;
		
		this.totalTableInsDel = this.numTablesIns+ this.numTablesDel;
		this.totalAttrInsDel = this.numAttrIns + this.numAttrDel;
		this.totalAttrUpd = this.numAttrWithTypeAlt + this.numAttrInKeyAlt;
		this.totalAttrChange = this.totalAttrInsDel + this.totalAttrUpd +   this.numAttrInsInNewTables + this.numAttrDelWithDelTables;
	}
	

	public int getTransitionId() {
		return transitionId;
	}
	public int getTime() {
		return time;
	}
	public String getOldVersionFile() {
		return oldVersionFile;
	}
	public String getNewVersionFile() {
		return newVersionFile;
	}
	public int getNumOldTables() {
		return numOldTables;
	}
	public int getNumNewTables() {
		return numNewTables;
	}
	public int getNumOldAtributes() {
		return numOldAtributes;
	}
	public int getNumNewAttributes() {
		return numNewAttributes;
	}
	public int getNumTablesIns() {
		return numTablesIns;
	}
	public int getNumTablesDel() {
		return numTablesDel;
	}
	public int getNumAttrIns() {
		return numAttrIns;
	}
	public int getNumAttrDel() {
		return numAttrDel;
	}
	public int getNumAttrWithTypeAlt() {
		return numAttrWithTypeAlt;
	}
	public int getNumAttrInKeyAlt() {
		return numAttrInKeyAlt;
	}
	public int getNumAttrInsInNewTables() {
		return numAttrInsInNewTables;
	}
	public int getNumAttrDelWithDelTables() {
		return numAttrDelWithDelTables;
	}
	public int getTotalTableInsDel() {
		return totalTableInsDel;
	}
	public int getTotalAttrInsDel() {
		return totalAttrInsDel;
	}
	public int getTotalAttrUpd() {
		return totalAttrUpd;
	}
	public int getTimeDistFromPrevious() {
		return timeDistFromPrevious;
	}
	public int getTotalAttrChange() {
		return totalAttrChange;
	}
	public int getTotalUpdInTr(){
		return totalUpdatesInTr;
	}
	public void setTimeDistFromPrevious(int timeDistFromPrevious) {
		this.timeDistFromPrevious = timeDistFromPrevious;
	}
	
	public String toStringShort(){
		String shortDescr = new String();
		
		shortDescr =  transitionId + "\t" + time + "\t" + timeDistFromPrevious + "\t" + numTablesIns + "\t" + numTablesDel + "\t" + numAttrIns + "\t" + numAttrDel + "\t" + numAttrWithTypeAlt + "\t" + numAttrInKeyAlt + "\t" + numAttrInsInNewTables + "\t" + numAttrDelWithDelTables; 
		return shortDescr;
	}
	
	private int transitionId;
	private int time;
	private String oldVersionFile;
	private String newVersionFile;
	private int numOldTables;
	private int numNewTables;
	private int numOldAtributes;
	private int numNewAttributes;
	private int numTablesIns;
	private int numTablesDel;
	private int numAttrIns;
	private int numAttrDel;
	private int numAttrWithTypeAlt;
	private int numAttrInKeyAlt;
	private int numAttrInsInNewTables;
	private int numAttrDelWithDelTables; 
	
	private int totalTableInsDel;
	private int totalAttrInsDel;
	private int totalAttrUpd;
	private int totalAttrChange;
	private int timeDistFromPrevious;
	private int totalUpdatesInTr;

	

}

