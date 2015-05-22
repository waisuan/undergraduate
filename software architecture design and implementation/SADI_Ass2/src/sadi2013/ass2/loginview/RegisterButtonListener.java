/*
 * SOFTWARE ARCHITECTURE DEVELOPMENT & IMPLEMENTATION
 * ASSIGNMENT 2
 * FILE: REGISTERBUTTONLISTENER.JAVA
 * DONE BY: MASON CHONG SZE ZHEN (s3310388)
 */
package sadi2013.ass2.loginview;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import sadi2013.ass2.rmi.client.RMIClient;
/*Register the user to the host.*/
public class RegisterButtonListener implements ActionListener
{
	private RMIClient client;
	
	public RegisterButtonListener(LoginPanel loginPanel, LoginFrame loginFrame, RMIClient client)
	{
		this.client = client;
	}
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		client.callRegFrame();
	}

}
