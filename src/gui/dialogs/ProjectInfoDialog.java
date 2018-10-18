package gui.dialogs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ProjectInfoDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();

	/**
	 * Launch the application.
	 */
	

	/**
	 * Create the dialog.
	 */
	public ProjectInfoDialog(String project, String dataset, String inputCsv, String transitionsXml, 
					int numberOfSchemas, int numberOfTransitions, int numberOfTables) {
		setBounds(100, 100, 479, 276);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		JLabel lblSchemas = new JLabel("Schemas:");
		lblSchemas.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		JLabel labelNumberOfSch = new JLabel("-");
		labelNumberOfSch.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		labelNumberOfSch.setText(Integer.toString(numberOfSchemas));
		
		JLabel lblTransitions = new JLabel("Transitions:");
		lblTransitions.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		JLabel labelNumberOfTr = new JLabel("-");
		labelNumberOfTr.setFont(new Font("Tahoma", Font.PLAIN, 12));
		labelNumberOfTr.setText(Integer.toString(numberOfTransitions));

		
		JLabel lblTables = new JLabel("Tables:");
		lblTables.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		JLabel labelNumberOfTables = new JLabel("-");
		labelNumberOfTables.setFont(new Font("Tahoma", Font.PLAIN, 12));
		labelNumberOfTables.setText(Integer.toString(numberOfTables));
		
		JLabel lblProjectName = new JLabel("Project Name");
		lblProjectName.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		JLabel labelPrName = new JLabel("-");
		labelPrName.setFont(new Font("Tahoma", Font.PLAIN, 12));
		labelPrName.setText(project);

		
		JLabel lblDatasetTxt = new JLabel("Dataset Txt");
		lblDatasetTxt.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		JLabel lblDataTxt = new JLabel("-");
		lblDataTxt.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblDataTxt.setText(dataset);

		
		JLabel lblInput = new JLabel("Input  Csv");
		lblInput.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		JLabel labelInCsv = new JLabel("-");
		labelInCsv.setFont(new Font("Tahoma", Font.PLAIN, 12));
		labelInCsv.setText(inputCsv);

		
		JLabel lblTransitionsFile = new JLabel("Transitions File");
		lblTransitionsFile.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		JLabel labelTrFile = new JLabel("-");
		labelTrFile.setFont(new Font("Tahoma", Font.PLAIN, 12));
		labelTrFile.setText(transitionsXml);


		
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING)
						.addComponent(lblSchemas, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 64, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPanel.createSequentialGroup()
									.addComponent(lblTables, GroupLayout.PREFERRED_SIZE, 59, GroupLayout.PREFERRED_SIZE)
									.addGap(196))
								.addGroup(gl_contentPanel.createSequentialGroup()
									.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING, false)
										.addComponent(lblTransitions, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(lblProjectName, Alignment.LEADING)
										.addComponent(lblDatasetTxt, Alignment.LEADING)
										.addComponent(lblInput, Alignment.LEADING)
										.addComponent(lblTransitionsFile, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
									.addGap(13)
									.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
										.addComponent(labelPrName, GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE)
										.addComponent(lblDataTxt, GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE)
										.addGroup(gl_contentPanel.createSequentialGroup()
											.addPreferredGap(ComponentPlacement.UNRELATED)
											.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
												.addComponent(labelTrFile, GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE)
												.addComponent(labelInCsv, GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE)
												.addComponent(labelNumberOfSch, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
												.addComponent(labelNumberOfTr, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
												.addComponent(labelNumberOfTables, GroupLayout.PREFERRED_SIZE, 62, GroupLayout.PREFERRED_SIZE))))))
							.addPreferredGap(ComponentPlacement.RELATED)))
					.addGap(0))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblProjectName)
						.addComponent(labelPrName))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblDatasetTxt)
						.addComponent(lblDataTxt))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(labelInCsv)
						.addComponent(lblInput))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(labelTrFile)
						.addComponent(lblTransitionsFile))
					.addPreferredGap(ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblSchemas)
						.addComponent(labelNumberOfSch))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblTransitions)
						.addComponent(labelNumberOfTr))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblTables)
						.addComponent(labelNumberOfTables))
					.addGap(27))
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
						
						setVisible(false);
						
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
	}
}
