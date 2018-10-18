package data.dataPPL.pplSQLSchema;

import gr.uoi.cs.daintiness.hecate.sql.Attribute;

//import hecate.sqlSchema.Attribute;

public class PPLAttribute {

	private int totalAttributeChanges;
	private Attribute hecAttribute;
	
	
	public PPLAttribute(Attribute tmpHecAttribute){
		
		hecAttribute=tmpHecAttribute;
		
	}
	
	public PPLAttribute(){
		
	}
	
	public String getName(){
		
		return hecAttribute.getName();
	}

	public int getTotalAttributeChanges(){
		return totalAttributeChanges;
	}
	
	
	public void setTotalAttributeChanges(int tmpTotalAttributeChanges){
		totalAttributeChanges=tmpTotalAttributeChanges;
	}
	
	public Attribute getHecAttribute(){
		
		return hecAttribute;
		
	}
	
	
}
