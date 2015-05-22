/*
 * SOFTWARE ARCHITECTURE DEVELOPMENT & IMPLEMENTATION
 * ASSIGNMENT 2
 * FILE: LOGINFRAME.JAVA
 * DONE BY: MASON CHONG SZE ZHEN (s3310388)
 */
package sadi2013.ass2.loginview;
import java.awt.BorderLayout;

import javax.swing.JFrame;

import sadi2013.ass2.rmi.client.RMIClient;
/*Login frame used to contain the login panel.*/
@SuppressWarnings("serial")
public class LoginFrame extends JFrame
{
	private LoginPanel loginPanel;
	/*Constructor used to initialise the login frame.*/
	public LoginFrame(RMIClient client)
	{
		this.loginPanel = new LoginPanel(this);
		this.loginPanel.getRegister().addActionListener(new RegisterButtonListener(loginPanel, this, client));
		this.loginPanel.getLogin().addActionListener(new LoginButtonListener(loginPanel, this, client));
		this.setTitle("Snake Game Login");
		this.setSize(300,150);
		this.setResizable(false);
		this.setLayout(new BorderLayout());
		this.add(loginPanel, BorderLayout.CENTER);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		
	}
}
