package tableClustering.clusterValidator.commons;

import java.util.ArrayList;

public class ClassOfObjects {
	
	private ArrayList<String> objectsOfClass = new ArrayList<String>();
	private String name;
	
	public ClassOfObjects(String name){
		this.name = name;
	}
	
	public void setObjects(ArrayList<String> objectsOfClass){
		this.objectsOfClass = objectsOfClass;
	}

	public String getName(){
		return name;
	}
	
	public ArrayList<String> getObjects(){
		return objectsOfClass;
	}
	
	public String toString(){
		String toReturn=this.name+"\n";
		
		for(int i=0; i<objectsOfClass.size(); i++){
			toReturn = toReturn + objectsOfClass.get(i)+"\n";
		}
		
		return toReturn;
	}
	
}
