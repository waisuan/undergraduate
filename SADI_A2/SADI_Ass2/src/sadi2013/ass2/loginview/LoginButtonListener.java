/*
 * SOFTWARE ARCHITECTURE DEVELOPMENT & IMPLEMENTATION
 * ASSIGNMENT 2
 * FILE: LOGINBUTTONLISTENER.JAVA
 * DONE BY: MASON CHONG SZE ZHEN (s3310388)
 */
package sadi2013.ass2.loginview;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import sadi2013.ass2.rmi.client.RMIClient;
/*Login button listener used to verify the user to the host for authentication.*/
public class LoginButtonListener implements ActionListener
{
	private LoginPanel loginPanel;
	private LoginFrame loginFrame;
	private RMIClient client;
	
	public LoginButtonListener(LoginPanel loginPanel, LoginFrame loginFrame, RMIClient client)
	{
		this.loginFrame = loginFrame;
		this.loginPanel = loginPanel;
		this.client = client;
	}
	/*Verify the user to the host.*/
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		String username = this.loginPanel.getUsernameText().getText();
		@SuppressWarnings("deprecation")
		String password = this.loginPanel.getPasswordText().getText();
		/*Username and password cannot be less than 6 characters.*/
		if(username.length() < 6 )
		{
			JOptionPane.showMessageDialog(this.loginFrame, "Username length must be 6 characters or longer!", "Register Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if(password.length() < 6 )
		{
			JOptionPane.showMessageDialog(this.loginFrame, "Password length must be 6 characters or longer!", "Register Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		//Validate the user to the host.
		client.clientCredentials(username, password);
	}

}
