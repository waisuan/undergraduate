/*
 * SOFTWARE ARCHITECTURE DEVELOPMENT & IMPLEMENTATION
 * ASSIGNMENT 2
 * FILE: CONNECTBUTTONLISTENER.JAVA
 * DONE BY: MASON CHONG SZE ZHEN (s3310388)
 */
package sadi2013.ass2.loginview;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import sadi2013.ass2.rmi.client.RMIClient;
/*Connect button listener for connect the user to the server.*/
public class ConnectButtonListener implements ActionListener
{
	private HostNameFrame hostNameFrame;
	private HostNamePanel hostNamePanel;
	private RMIClient client;
	
	public ConnectButtonListener(HostNameFrame hostNameFrame, HostNamePanel hostNamePanel, RMIClient client)
	{
		this.hostNameFrame = hostNameFrame;
		this.hostNamePanel = hostNamePanel;
		this.client = client;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		//Connect to the host
		this.hostNameFrame.setVisible(false);
		/*If the host name is empty, host name is invalid.*/
		if(hostNamePanel.getHostNameText().getText().length()==0)
		{
			JOptionPane.showMessageDialog(this.hostNameFrame, "Host Address is invalid!", "Register Error", JOptionPane.ERROR_MESSAGE);
			this.hostNameFrame.setVisible(true);
			return;
		}
		/*If connect to the server successfully, call login frame.*/
		if(client.setSvrHost(hostNamePanel.getHostNameText().getText()))
		{
			client.startLoginFrame();
		}
		else
		{
			client.startHostFrame();
		}
	}

}
