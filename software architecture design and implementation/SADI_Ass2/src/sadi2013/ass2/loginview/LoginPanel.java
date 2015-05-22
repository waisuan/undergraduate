/*
 * SOFTWARE ARCHITECTURE DEVELOPMENT & IMPLEMENTATION
 * ASSIGNMENT 2
 * FILE: LOGINPANEL.JAVA
 * DONE BY: MASON CHONG SZE ZHEN (s3310388)
 */
package sadi2013.ass2.loginview;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/*LoginPanel used to initialise the component for login frame.*/
@SuppressWarnings("serial")
public class LoginPanel extends JPanel
{
	private JLabel usernameLabel = new JLabel("Username:");
	private JLabel passwordLabel = new JLabel("Password:");

	private JTextField usernameText = new JTextField(10);
	private JPasswordField passwordText = new JPasswordField(10);
	
	private JButton login = new JButton("Login");
	private JButton register = new JButton("Register");
	/*Adds the component to the panel and set the layout.*/
	public LoginPanel(LoginFrame loginFrame)
	{
		this.setLayout(new GridLayout(3,2,10,10));
		this.add(usernameLabel);
		this.add(usernameText);
		this.add(passwordLabel);
		this.add(passwordText);
		this.add(login);
		this.add(register);
	}

	public JLabel getUsernameLabel() {
		return usernameLabel;
	}

	public JLabel getPasswordLabel() {
		return passwordLabel;
	}

	public JTextField getUsernameText() {
		return usernameText;
	}

	public JPasswordField getPasswordText() {
		return passwordText;
	}

	public JButton getLogin() {
		return login;
	}

	public JButton getRegister() {
		return register;
	}

}
