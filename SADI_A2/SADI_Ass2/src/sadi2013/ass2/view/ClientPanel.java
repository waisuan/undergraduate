/*
 * SOFTWARE ARCHITECTURE DEVELOPMENT & IMPLEMENTATION
 * ASSIGNMENT 2
 * FILE: CLIENTPANEL.JAVA
 * DONE BY: MASON CHONG SZE ZHEN (s3310388) & MING WEI TEE (s3260935)
 */
package sadi2013.ass2.view;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.Border;

@SuppressWarnings("serial")
public class ClientPanel extends JPanel
{
	private final int BUTTON_WIDTH = 70;
	private final int BUTTON_HEIGHT = 30;

	private JPanel centerPanel;
	
	public int getBUTTON_WIDTH() {
		return BUTTON_WIDTH;
	}

	public int getBUTTON_HEIGHT() {
		return BUTTON_HEIGHT;
	}

	public JPanel getCenterPanel() {
		return centerPanel;
	}

	public JPanel getFirstLinePanel() {
		return firstLinePanel;
	}

	public JPanel getSecondLinePanel() {
		return secondLinePanel;
	}

	public JPanel getThirdLinePanel() {
		return thirdLinePanel;
	}

	public JPanel getConnectPanel() {
		return connectPanel;
	}

	public JButton getButton2p() {
		return button2p;
	}

	public JButton getButton3p() {
		return button3p;
	}

	public JButton getButton4p() {
		return button4p;
	}

	public JButton getTopLeft() {
		return topLeft;
	}

	public JButton getTopRight() {
		return topRight;
	}

	public JButton getBotLeft() {
		return botLeft;
	}

	public JButton getBotRight() {
		return botRight;
	}

	public JButton getConnect() {
		return connect;
	}

	public TextArea getResponseBox() {
		return responseBox;
	}

	private JPanel firstLinePanel;
	private JPanel secondLinePanel;
	private JPanel thirdLinePanel;
	private JPanel connectPanel;
	
	private JButton button2p;
	private JButton button3p;
	private JButton button4p;
	private JButton topLeft;
	private JButton topRight;
	private JButton botLeft;
	private JButton botRight;
	private JButton connect;
	private TextArea responseBox;
	
	public ClientPanel()
	{
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		
		centerPanel = new JPanel();
		centerPanel.setLayout(new GridBagLayout());
		
		firstLinePanel = new JPanel();
		firstLinePanel.setLayout(new FlowLayout());
		secondLinePanel = new JPanel();
		secondLinePanel.setLayout(new GridBagLayout());		
		
		thirdLinePanel = new JPanel();
		thirdLinePanel.setLayout(new BorderLayout());		
		
		
		//Create the two borders
		Border borderLine = BorderFactory.createLineBorder(Color.BLACK, 1);
		Border borderTitle = BorderFactory.createTitledBorder(borderLine,"Number Of Players");
		Border bevelBorder = BorderFactory.createRaisedBevelBorder();
		
		//Combining the borders into a compound border
		Border compoundBorder = BorderFactory.createCompoundBorder(
				bevelBorder, borderTitle);
		firstLinePanel.setBorder(compoundBorder);
		
		
		//Create the two borders
		borderLine = BorderFactory.createLineBorder(Color.BLACK, 1);
		borderTitle = BorderFactory.createTitledBorder(borderLine,"Starting Position");
		bevelBorder = BorderFactory.createRaisedBevelBorder();
		
		//Combining the borders into a compound border
		compoundBorder = BorderFactory.createCompoundBorder(
				bevelBorder, borderTitle);
		secondLinePanel.setBorder(compoundBorder);
		
		//set bevel for thridLinePanel
		thirdLinePanel.setBorder(bevelBorder);
		
		
		//Create a border
		borderLine = BorderFactory.createLineBorder(Color.LIGHT_GRAY, 3);
		centerPanel.setBorder(borderLine);	
		centerPanel.setBackground(Color.LIGHT_GRAY);		
		
		
		button2p = new JButton("2 Player");
		button3p = new JButton("3 Player");
		button4p = new JButton("4 Player");
		topLeft = new JButton("Top Left");
		topRight = new JButton("Top Right");
		botLeft = new JButton("Bottom Left");
		botRight = new JButton("Bottom Right");
		connect = new JButton("Connect to server");
		
		
		button2p.setEnabled(false);
		button3p.setEnabled(false);
		button4p.setEnabled(false);
		topLeft.setEnabled(false);
		topRight.setEnabled(false);
		botLeft.setEnabled(false);
		botRight.setEnabled(false);
		connect.setEnabled(false);
		
		
		button2p.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		button3p.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		button4p.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		topLeft.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		topRight.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		botLeft.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		botRight.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		connect.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		
		
		responseBox = new TextArea();
		responseBox.setSize(300, 200);
		responseBox.setEditable(false);
		
		this.setLayout(new BorderLayout());

		this.add(centerPanel, BorderLayout.CENTER);
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.ipady = 40;   
		gridBagConstraints.weightx = 1;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		centerPanel.add(firstLinePanel, gridBagConstraints);
		
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.ipady = 80;     
		gridBagConstraints.weightx = 1;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;		
		centerPanel.add(secondLinePanel, gridBagConstraints);
		
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		centerPanel.add(thirdLinePanel, gridBagConstraints);	
		
		firstLinePanel.add(button2p);
		firstLinePanel.add(button3p);
		firstLinePanel.add(button4p);
		
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 0.5;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		secondLinePanel.add(topLeft, gridBagConstraints);
		
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 0.5;
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		secondLinePanel.add(topRight, gridBagConstraints);
		
		
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 0.5;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		secondLinePanel.add(botLeft, gridBagConstraints);
		
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 0.5;
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		secondLinePanel.add(botRight, gridBagConstraints);
		
		thirdLinePanel.add(connect, BorderLayout.CENTER);
		this.add(responseBox, BorderLayout.SOUTH);
		
		this.setVisible(true);
	}
	
	public void setTextAreaText(String txt)
	{
		responseBox.append(txt + "\n");
	}
}

