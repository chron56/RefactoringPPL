package data.dataProccessing;

import gr.uoi.cs.daintiness.hecate.diff.Delta;
import gr.uoi.cs.daintiness.hecate.diff.DiffResult;
import gr.uoi.cs.daintiness.hecate.parser.HecateParser;
import gr.uoi.cs.daintiness.hecate.sql.Schema;
import gr.uoi.cs.daintiness.hecate.transitions.Deletion;
import gr.uoi.cs.daintiness.hecate.transitions.Insersion;
import gr.uoi.cs.daintiness.hecate.transitions.TransitionList;
import gr.uoi.cs.daintiness.hecate.transitions.Transitions;
import gr.uoi.cs.daintiness.hecate.transitions.Update;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class ImportSchemas {
	
	private static ArrayList<Schema> allSchemas = null;

	private String filepath=null;
	private String transitionsFile=null;
	private static ArrayList<TransitionList> allTransitions = null;
	
	public ImportSchemas(String tmpFilepath,String transitionsFile) {
		allTransitions = new ArrayList<TransitionList>();
		allSchemas = new ArrayList<Schema>();
		filepath=tmpFilepath;
		this.transitionsFile=transitionsFile;
	}

	@SuppressWarnings("static-access")
	public void loadDataset() throws IOException{

		BufferedReader br = new BufferedReader(new FileReader(filepath));
		File f = new File(filepath);
		String dataset = (f.getName().split("\\."))[0];
		String d = f.getParent();
		f = new File(d);
		String path = f.getAbsolutePath() + File.separator + dataset;

		ArrayList<String> sAllSchemas = new ArrayList<String>();
		String line;
		
		while(true) {
			line = br.readLine();
			if (line == null) 
				break;
			sAllSchemas.add(line);
		};

		String sStandardString = path + File.separator;

		Transitions trs = new Transitions();

		
		
		for(int i=0; i<sAllSchemas.size(); i++) {
			if(i==sAllSchemas.size()-1) {
				String sFinalString2=sStandardString+sAllSchemas.get(i);
				Schema schema=HecateParser.parse(sFinalString2);
				allSchemas.add(schema);
				break;
			}
			String sFinalString=sStandardString+sAllSchemas.get(i).trim();

			allSchemas.add(HecateParser.parse(sFinalString));
			
			String sFinalString2=sStandardString+sAllSchemas.get(i+1).trim();

			Schema oldSchema = HecateParser.parse(sFinalString);
			
			
			Schema newSchema = HecateParser.parse(sFinalString2);

			
			Delta delta = new Delta();

			TransitionList tmpTransitionList =new TransitionList();
			DiffResult tmpDiffResult=new DiffResult();
			tmpDiffResult=delta.minus(oldSchema, newSchema); 

			tmpTransitionList=tmpDiffResult.tl;
			trs.add(tmpTransitionList);

		}
		br.close();
		makeTransitions(trs);
		
	}


	public  void makeTransitions(Transitions tl) throws IOException {
		try {



			JAXBContext jaxbContext = JAXBContext.newInstance(Update.class,Deletion.class, Insersion.class, TransitionList.class, Transitions.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(tl, new FileOutputStream(transitionsFile));


			//***********************************************

			JAXBContext jaxbContext1 = JAXBContext.newInstance(Update.class,Deletion.class, Insersion.class, TransitionList.class, Transitions.class);
			Unmarshaller u=jaxbContext1.createUnmarshaller();
			File inputFile=new File(transitionsFile);
			Transitions root = (Transitions) u.unmarshal(inputFile);


			allTransitions=(ArrayList<TransitionList>) root.getList();

			

		} catch (JAXBException e) {
			JOptionPane.showMessageDialog(null, "Something seems wrong with this file");
			return;
		}
	}
	
	public ArrayList<Schema> getAllHecSchemas(){

		return allSchemas;

	}

	public ArrayList<TransitionList> getAllTransitions(){

		return allTransitions;

	}

}
