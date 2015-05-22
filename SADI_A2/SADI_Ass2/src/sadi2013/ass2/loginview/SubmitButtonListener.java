/*
 * SOFTWARE ARCHITECTURE DEVELOPMENT & IMPLEMENTATION
 * ASSIGNMENT 2
 * FILE: SUBMITBUTTONLISTENER.JAVA
 * DONE BY: MASON CHONG SZE ZHEN (s3310388)
 */
package sadi2013.ass2.loginview;
import javax.swing.*;
import sadi2013.ass2.rmi.client.RMIClient;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/*Submit the information user type in and register the user to the host.*/
public class SubmitButtonListener implements ActionListener
{
	private RegisterPanel loginPanel;
	private RegisterFrame registerFrame;
	private RMIClient client;
	
	public SubmitButtonListener(RegisterPanel loginPanel, RegisterFrame registerFrame, RMIClient client)
	{
		this.loginPanel = loginPanel;
		this.registerFrame = registerFrame;
		this.client = client;
	}
	/*Submit the information to the server, validate the fields before submit it to the host.*/
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		String username = loginPanel.getUsernameText().getText();
		@SuppressWarnings("deprecation")
		String password = loginPanel.getPasswordText().getText();
		String firstname = loginPanel.getFirstnameText().getText();
		String surname = loginPanel.getSurnameText().getText();
		String address = loginPanel.getAddressText().getText();
		String phonenumber = loginPanel.getPhonenumberText().getText();
		/*Username and Password must be at least 6 characters, other fields must not be empty, phone number must be numbers.*/
		if(username.length() < 6 )
		{
			JOptionPane.showMessageDialog(this.registerFrame, "Username length must be 6 characters or longer!", "Register Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if(password.length() < 6 )
		{
			JOptionPane.showMessageDialog(this.registerFrame, "Password length must be 6 characters or longer!", "Register Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if(firstname.length() == 0 )
		{
			JOptionPane.showMessageDialog(this.registerFrame, "First name cannot be empty!", "Register Error", JOptionPane.ERROR_MESSAGE);
			return;
		}		
		if(surname.length() == 0 )
		{
			JOptionPane.showMessageDialog(this.registerFrame, "Sur name cannot be empty!", "Register Error", JOptionPane.ERROR_MESSAGE);
			return;
		}	
		if(address.length() == 0 )
		{
			JOptionPane.showMessageDialog(this.registerFrame, "Address cannot be empty!", "Register Error", JOptionPane.ERROR_MESSAGE);
			return;
		}	
		if(phonenumber.length() == 0 )
		{
			JOptionPane.showMessageDialog(this.registerFrame, "Phone number cannot be empty!", "Register Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		else
		{
			try { 
		        Integer.parseInt(phonenumber); 
		    } catch(NumberFormatException nfe) { 
		    	JOptionPane.showMessageDialog(this.registerFrame, "Phone number should be in a NUMBER format!", "Register Error", JOptionPane.ERROR_MESSAGE);
				return;
		    }
		}
		
		int score = 0;
		/*Constructer the data string and register the user to the host.*/
		String data = "'" + username + "', " + "'" + password + "', " +  "'" + firstname + "', " + "'" + surname + "', " + "'" + address + "', " + "'" + phonenumber + "', " + score ;
		
		client.newClient(data, username, password);
	}

}
