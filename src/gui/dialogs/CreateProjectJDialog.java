package gui.dialogs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextField;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CreateProjectJDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField textFieldProjectName;
	private JTextField textFieldDatasetTxt;
	private JTextField textFieldInputCsv;
	private JTextField textFieldAss1;
	private JTextField textFieldAss2;
	private JTextField textFieldTransXml;
	private File fileToCreate=null;
	private boolean confirm=false;


	/**
	 * Launch the application.
	 */
	
	public CreateProjectJDialog(String projectName,String datasetTxt,String inputCsv,String ass1,String ass2,String transXml) {
		
		
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		JLabel lblNewLabel = new JLabel("Project Name:");
		
		JLabel lblNewLabel_1 = new JLabel("Dataset-txt:");
		
		JLabel lblNewLabel_2 = new JLabel("Input-csv:");
		
		JLabel lblNewLabel_3 = new JLabel("Assessement1-output:");
		
		JLabel lblAssessementoutput = new JLabel("Assessement2-output:");
		
		JLabel lblNewLabel_4 = new JLabel("Transition-xml:");
		
		textFieldProjectName = new JTextField();
		textFieldProjectName.setColumns(10);
		
		textFieldDatasetTxt = new JTextField();
		textFieldDatasetTxt.setColumns(10);
		
		textFieldInputCsv = new JTextField();
		textFieldInputCsv.setColumns(10);
		
		textFieldAss1 = new JTextField();
		textFieldAss1.setColumns(10);
		
		textFieldAss2 = new JTextField();
		textFieldAss2.setColumns(10);
		
		textFieldTransXml = new JTextField();
		textFieldTransXml.setColumns(10);
		
		this.textFieldProjectName.setText(projectName);
		this.textFieldDatasetTxt.setText(datasetTxt);
		this.textFieldInputCsv.setText(inputCsv);
		this.textFieldAss1.setText(ass1);
		this.textFieldAss2.setText(ass2);
		this.textFieldTransXml.setText(transXml);
		
		JButton buttonDatasetTxt = new JButton("...");
		buttonDatasetTxt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				JFileChooser fcDatasetTxt = new JFileChooser();
				int returnValDatasetTxt = fcDatasetTxt.showDialog(CreateProjectJDialog.this, "Create");
				
				if (returnValDatasetTxt == JFileChooser.APPROVE_OPTION) {
					
		            File file = fcDatasetTxt.getSelectedFile();
		            System.out.println(file.toString());
		            textFieldDatasetTxt.setText(file.toString());

				}
				else{
					return;
				}
				
			}
		});
		
		JButton buttonInputCsv = new JButton("...");
		buttonInputCsv.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				JFileChooser fcInputCsv = new JFileChooser();
				int returnValInputCsv = fcInputCsv.showDialog(CreateProjectJDialog.this, "Create");
				
				if (returnValInputCsv == JFileChooser.APPROVE_OPTION) {
					
		            File file = fcInputCsv.getSelectedFile();
		            System.out.println(file.toString());
		            textFieldInputCsv.setText(file.toString());

				}
				else{
					return;
				}
				
			}
		});
		
		JButton buttonAss1 = new JButton("...");
		buttonAss1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				JFileChooser fcAss1 = new JFileChooser();
				int returnValAss1 = fcAss1.showDialog(CreateProjectJDialog.this, "Create");
				
				if (returnValAss1 == JFileChooser.APPROVE_OPTION) {
					
		            File file = fcAss1.getSelectedFile();
		            System.out.println(file.toString());
		            textFieldAss1.setText(file.toString());

				}
				else{
					return;
				}
				
			}
		});
		
		JButton buttonAss2 = new JButton("...");
		buttonAss2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				JFileChooser fcAss2 = new JFileChooser();
				int returnValAss2 = fcAss2.showDialog(CreateProjectJDialog.this, "Create");
				
				if (returnValAss2 == JFileChooser.APPROVE_OPTION) {
					
		            File file = fcAss2.getSelectedFile();
		            System.out.println(file.toString());
		            textFieldAss2.setText(file.toString());

		            
				}
				else{
					return;
				}
				
			}
		});
		
		JButton buttonTransXml = new JButton("...");
		buttonTransXml.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				JFileChooser fcTransXml = new JFileChooser();
				int returnValTransXml = fcTransXml.showDialog(CreateProjectJDialog.this, "Create");
				
				if (returnValTransXml == JFileChooser.APPROVE_OPTION) {
					
		            File file = fcTransXml.getSelectedFile();
		            System.out.println(file.toString());
		            textFieldTransXml.setText(file.toString());
		            
				}
				else{
					return;
				}
				
			}
		});
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblNewLabel)
						.addComponent(lblNewLabel_1)
						.addComponent(lblNewLabel_2)
						.addComponent(lblNewLabel_3)
						.addComponent(lblAssessementoutput)
						.addComponent(lblNewLabel_4))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addComponent(textFieldProjectName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(55))
						.addComponent(textFieldDatasetTxt, Alignment.LEADING)
						.addComponent(textFieldInputCsv, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
						.addComponent(textFieldAss1, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
						.addComponent(textFieldAss2, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
						.addComponent(textFieldTransXml, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(buttonTransXml, 0, 0, Short.MAX_VALUE)
						.addComponent(buttonAss2, 0, 0, Short.MAX_VALUE)
						.addComponent(buttonAss1, 0, 0, Short.MAX_VALUE)
						.addComponent(buttonInputCsv, 0, 0, Short.MAX_VALUE)
						.addComponent(buttonDatasetTxt, GroupLayout.PREFERRED_SIZE, 41, Short.MAX_VALUE)))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel)
						.addComponent(textFieldProjectName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING, false)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblNewLabel_1)
								.addComponent(buttonDatasetTxt)))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
							.addComponent(textFieldDatasetTxt, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_2)
						.addComponent(textFieldInputCsv, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(buttonInputCsv))
					.addGap(7)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_3)
						.addComponent(textFieldAss1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(buttonAss1))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(textFieldAss2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblAssessementoutput)
						.addComponent(buttonAss2))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(textFieldTransXml, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel_4)
						.addComponent(buttonTransXml))
					.addGap(18))
		);
		contentPanel.setLayout(gl_contentPanel);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						boolean empty=false;
						
						if(textFieldProjectName.getText().isEmpty()){
							JOptionPane.showMessageDialog(null,"Project Name cannot be empty!");
							empty=true;
						}
						if(textFieldDatasetTxt.getText().isEmpty()){
							JOptionPane.showMessageDialog(null,"Dataset Txt cannot be empty!");
							empty=true;
						}
						if(textFieldInputCsv.getText().isEmpty()){
							JOptionPane.showMessageDialog(null,"Input Csv cannot be empty!");
							empty=true;
						}
						if(textFieldAss1.getText().isEmpty()){
							JOptionPane.showMessageDialog(null,"Assessment 1 Output cannot be empty!");
							empty=true;
						}
						if(textFieldAss2.getText().isEmpty()){
							JOptionPane.showMessageDialog(null,"Assessment 2 Output cannot be empty!");
							empty=true;
						}
						if(textFieldTransXml.getText().isEmpty()){
							JOptionPane.showMessageDialog(null,"Transitions Xml cannot be empty!");
							empty=true;
						}
						
					
						
						if(!empty){
							
							confirm=true;
							setVisible(false);
							
							String toWrite="Project-name:"+textFieldProjectName.getText()+"\n";
							toWrite=toWrite+"Dataset-txt:"+textFieldDatasetTxt.getText()+"\n";
							toWrite=toWrite+"Input-csv:"+textFieldInputCsv.getText()+"\n";
							toWrite=toWrite+"Assessement1-output:"+textFieldAss1.getText()+"\n";
							toWrite=toWrite+"Assessement2-output:"+textFieldAss2.getText()+"\n";
							toWrite=toWrite+"Transition-xml:"+textFieldTransXml.getText();
							
							System.out.println(toWrite);
							
							fileToCreate = new File("filesHandler/inis/"+textFieldProjectName.getText()+".ini");
							
							System.out.print(fileToCreate.getAbsolutePath());
							 
							// if file doesnt exists, then create it
							if (!fileToCreate.exists()) {
								
								try {
									fileToCreate.createNewFile();
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								
							}

							FileWriter fw;
							BufferedWriter bw;
							try {
								fw = new FileWriter(fileToCreate.getAbsoluteFile());
								bw = new BufferedWriter(fw);
								bw.write(toWrite);
								bw.close();


							} catch (IOException e2) {
								// TODO Auto-generated catch block
								e2.printStackTrace();
							}	
							
						}
						else {
							confirm=false;
						}
						
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						confirm=false;
						setVisible(false);

					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	
	public File getFile(){
		return fileToCreate;
	}
	
	public boolean getConfirmation(){
		
		return confirm;
	}
	
}
