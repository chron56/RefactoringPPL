package gui.dialogs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JTextField;

public class ParametersJDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private final ButtonGroup buttonGroup_1 = new ButtonGroup();
	private Float timeWeight=null;
	private Float changeWeight=null;
	private Boolean preprocessingTime=null;
	private Boolean preprocessingChange=null;
	private Integer numberOfPhases=null;
	private Integer numberOfClusters=null;
	private Double birthWeight=null;
	private Double deathWeight=null;
	private Double changeWeightCl=null;

	private boolean confirm=false;
	private JTextField textField;
	private JTextField giveClustersTxtFields;
	private JTextField birthWeightTxtF;
	private JTextField changeWeightTxtF;
	private JTextField deathWeightTxtF;
	


	/**
	 * Create the dialog.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ParametersJDialog(boolean clusters) {
		setBounds(100, 100, 509, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.WEST);
		
		JLabel lblChooseTimeWeight = new JLabel("Choose Time Weight");
		
		final JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"0.0", "0.5", "1.0"}));
		comboBox.setSelectedIndex(1);
		
		JLabel lblChooseChangeWeight = new JLabel("Choose Change Weight");
		
		final JComboBox comboBox_1 = new JComboBox();
		comboBox_1.setModel(new DefaultComboBoxModel(new String[] {"0.0", "0.5", "1.0"}));
		comboBox_1.setSelectedIndex(1);
		
		JLabel lblTimePreprocessing = new JLabel("Time PreProcessing");
		
		final JRadioButton rdbtnOn = new JRadioButton("ON");
		buttonGroup.add(rdbtnOn);
		
		final JRadioButton rdbtnOff = new JRadioButton("OFF");
		rdbtnOff.setSelected(true);
		buttonGroup.add(rdbtnOff);
		
		JLabel lblNewLabel = new JLabel("Change PreProcessing");
		
		final JRadioButton rdbtnOn_1 = new JRadioButton("ON");
		buttonGroup_1.add(rdbtnOn_1);
		
		final JRadioButton rdbtnOff_1 = new JRadioButton("OFF");
		rdbtnOff_1.setSelected(true);
		buttonGroup_1.add(rdbtnOff_1);
		
		JLabel lblGiveNumberOf = new JLabel("Give Number of Phases");
		
		textField = new JTextField();
		textField.setText("56");
		textField.setColumns(10);
		
		giveClustersTxtFields = new JTextField();
		giveClustersTxtFields.setText("14");
		giveClustersTxtFields.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Give Number of Clusters");
		
		
		JLabel lblBirthWeight = new JLabel("Birth Weight");
		
		JLabel lblDeathWeight = new JLabel("Death Weight");
		
		JLabel lblChangeWeight = new JLabel("Change Weight");
		
		birthWeightTxtF = new JTextField();
		birthWeightTxtF.setText("0.333");
		birthWeightTxtF.setColumns(10);
		
		changeWeightTxtF = new JTextField();
		changeWeightTxtF.setText("0.333");
		changeWeightTxtF.setColumns(10);
		
		deathWeightTxtF = new JTextField();
		deathWeightTxtF.setText("0.333");
		deathWeightTxtF.setColumns(10);
		

		if(!clusters){
			giveClustersTxtFields.setVisible(false);
			lblNewLabel_1.setVisible(false);
			lblChangeWeight.setVisible(false);
			lblBirthWeight.setVisible(false);
			lblDeathWeight.setVisible(false);
			changeWeightTxtF.setVisible(false);
			birthWeightTxtF.setVisible(false);
			deathWeightTxtF.setVisible(false);
		}
		else{
			giveClustersTxtFields.setVisible(true);
			lblNewLabel_1.setVisible(true);
			lblChangeWeight.setVisible(true);
			lblBirthWeight.setVisible(true);
			lblDeathWeight.setVisible(true);
			changeWeightTxtF.setVisible(true);
			birthWeightTxtF.setVisible(true);
			deathWeightTxtF.setVisible(true);
		}
		
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING, false)
								.addComponent(lblChooseChangeWeight, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(lblChooseTimeWeight, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(lblTimePreprocessing, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
							.addGap(35))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addComponent(lblNewLabel, GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)))
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGap(0)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING, false)
								.addGroup(gl_contentPanel.createSequentialGroup()
									.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING, false)
										.addComponent(comboBox_1, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(comboBox, 0, 41, Short.MAX_VALUE))
									.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_contentPanel.createSequentialGroup()
											.addGap(90)
											.addComponent(textField, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE))
										.addGroup(gl_contentPanel.createSequentialGroup()
											.addGap(33)
											.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
												.addComponent(lblGiveNumberOf)
												.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING)
													.addGroup(gl_contentPanel.createSequentialGroup()
														.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
															.addComponent(lblChangeWeight)
															.addGroup(gl_contentPanel.createSequentialGroup()
																.addPreferredGap(ComponentPlacement.RELATED)
																.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
																	.addComponent(lblDeathWeight)
																	.addComponent(lblBirthWeight))))
														.addPreferredGap(ComponentPlacement.UNRELATED)
														.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
															.addGroup(gl_contentPanel.createSequentialGroup()
																.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING)
																	.addComponent(birthWeightTxtF, GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE)
																	.addComponent(deathWeightTxtF, GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE))
																.addPreferredGap(ComponentPlacement.RELATED))
															.addComponent(changeWeightTxtF, GroupLayout.DEFAULT_SIZE, 54, Short.MAX_VALUE)))
													.addComponent(lblNewLabel_1, Alignment.LEADING)))))
									.addGap(83))
								.addGroup(gl_contentPanel.createSequentialGroup()
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
										.addComponent(rdbtnOn)
										.addComponent(rdbtnOff)
										.addComponent(rdbtnOn_1))
									.addGap(111)
									.addComponent(giveClustersTxtFields, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE))))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(rdbtnOff_1)))
					.addContainerGap())
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblChooseTimeWeight, GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
						.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblGiveNumberOf, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE))
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGap(18)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblChooseChangeWeight, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
								.addComponent(comboBox_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGap(29)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblTimePreprocessing)
								.addComponent(rdbtnOn))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(rdbtnOff)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPanel.createSequentialGroup()
									.addGap(33)
									.addComponent(lblNewLabel))
								.addGroup(gl_contentPanel.createSequentialGroup()
									.addGap(18)
									.addComponent(rdbtnOn_1)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(rdbtnOff_1))))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGap(7)
							.addComponent(lblNewLabel_1)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(giveClustersTxtFields, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblBirthWeight)
								.addComponent(birthWeightTxtF, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(deathWeightTxtF, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblDeathWeight))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblChangeWeight)
								.addComponent(changeWeightTxtF, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE))))
					.addGap(6))
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
						
						
						
						changeWeight = Float.valueOf((String) comboBox.getSelectedItem());
						timeWeight = Float.valueOf((String)comboBox_1.getSelectedItem());
						if(!textField.getText().isEmpty())
							numberOfPhases=Integer.parseInt(textField.getText());
						if(!giveClustersTxtFields.getText().isEmpty()){
							numberOfClusters=Integer.parseInt(giveClustersTxtFields.getText());
						}
						if(!birthWeightTxtF.getText().isEmpty()){
							birthWeight=Double.parseDouble(birthWeightTxtF.getText());
						}
						if(!deathWeightTxtF.getText().isEmpty()){
							deathWeight=Double.parseDouble(deathWeightTxtF.getText());
						}
						if(!changeWeightTxtF.getText().isEmpty()){
							changeWeightCl=Double.parseDouble(changeWeightTxtF.getText());
						}
						if(rdbtnOn.isSelected()){
							preprocessingTime=true;
						}
						else if(rdbtnOff.isSelected()){
							preprocessingTime=false;
						}
						if(rdbtnOn_1.isSelected()){
							preprocessingChange=true;
						}
						else if(rdbtnOff_1.isSelected()){
							preprocessingChange=false;
						}
						
						if(changeWeight!=null && timeWeight!=null && preprocessingTime!=null && preprocessingChange!=null && numberOfPhases!=null){
							
							confirm=true;

							setVisible(false);
						}
						else{
							
							timeWeight=null;
							changeWeight=null;
							preprocessingTime=null;
							preprocessingChange=null;
							
							JOptionPane.showMessageDialog(null, "You have to fill every field!");
							
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
	
	
	public float getTimeWeight(){
		
		return timeWeight; 
	}
	
	public float getChangeWeight(){
		
		return changeWeight; 
	}

	public Integer getNumberOfPhases() {
		return numberOfPhases;
	}
	
	public Integer getNumberOfClusters() {
		return numberOfClusters;
	}
	
	public Double getChangeWeightCluster(){
		
		return changeWeightCl; 
	}
	
	public Double geBirthWeight(){
		
		return birthWeight; 
	}

	public Double getDeathWeight(){
		
		return deathWeight; 
	}
	
	public boolean getPreProcessingTime(){
		
		return preprocessingTime;
	}
	
	public boolean getPreProcessingChange(){
		
		return preprocessingChange;
	}
	
	public boolean getConfirmation(){
		
		return confirm;
	}
}
