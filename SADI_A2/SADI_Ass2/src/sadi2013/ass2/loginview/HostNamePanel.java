/*
 * SOFTWARE ARCHITECTURE DEVELOPMENT & IMPLEMENTATION
 * ASSIGNMENT 2
 * FILE: HOSTNAMEPANEL.JAVA
 * DONE BY: MASON CHONG SZE ZHEN (s3310388)
 */
package sadi2013.ass2.loginview;

import java.awt.GridLayout;
import java.awt.Panel;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
/*Host name panel for initialise the component.*/
@SuppressWarnings("serial")
public class HostNamePanel extends Panel
{
	private JLabel hostNameLabel = new JLabel("Host Address: ");
	private JTextField hostNameText= new JTextField(10);
	
	private JButton connectButton = new JButton("Connect");
	/*Add in the component to the panel in constructor.*/
	public HostNamePanel(HostNameFrame hostNameFrame)
	{
		this.setLayout(new GridLayout(2,2,10,10));
		this.add(hostNameLabel);
		this.add(hostNameText);
		this.add(new JLabel());
		this.add(connectButton);
	}
	
	public JLabel getHostNameLabel() {
		return hostNameLabel;
	}

	public JTextField getHostNameText() {
		return hostNameText;
	}

	public JButton getConnectButton() {
		return connectButton;
	}

	public void setHostNameLabel(JLabel hostNameLabel) {
		this.hostNameLabel = hostNameLabel;
	}

	public void setHostNameText(JTextField hostNameText) {
		this.hostNameText = hostNameText;
	}

	public void setConnectButton(JButton connectButton) {
		this.connectButton = connectButton;
	}
	
}
