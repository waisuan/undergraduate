/*
 * SOFTWARE ARCHITECTURE DEVELOPMENT & IMPLEMENTATION
 * ASSIGNMENT 2
 * FILE: HOSTNAMEFRAME.JAVA
 * DONE BY: MASON CHONG SZE ZHEN (s3310388)
 */
package sadi2013.ass2.loginview;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import sadi2013.ass2.rmi.client.RMIClient;
/*Host name frame for user to type in the host address.*/
@SuppressWarnings("serial")
public class HostNameFrame extends JFrame
{
	private HostNamePanel hostNamePanel;
	/*Constructor of the host name frame.*/
	public HostNameFrame(RMIClient client)
	{
		this.hostNamePanel = new HostNamePanel(this);
		this.hostNamePanel.getConnectButton().addActionListener(new ConnectButtonListener(this, hostNamePanel, client));
		this.setTitle("Connect to Host");
		this.setSize(300,150);
		this.setResizable(false);
		this.setLayout(new BorderLayout());
		this.add(hostNamePanel, BorderLayout.CENTER);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
}
