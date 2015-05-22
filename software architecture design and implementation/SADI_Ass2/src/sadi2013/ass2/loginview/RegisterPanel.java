/*
 * SOFTWARE ARCHITECTURE DEVELOPMENT & IMPLEMENTATION
 * ASSIGNMENT 2
 * FILE: REGISTERPANEL.JAVA
 * DONE BY: MASON CHONG SZE ZHEN (s3310388)
 */
package sadi2013.ass2.loginview;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
/*RegisterPanel used to initialise the components for RegisterFrame.*/
@SuppressWarnings("serial")
public class RegisterPanel extends JPanel
{
	private JLabel usernameLabel = new JLabel("Username:");
	private JLabel passwordLabel = new JLabel("Password:");
	private JLabel firstnameLabel = new JLabel("First Name:");
	private JLabel surnameLabel = new JLabel("Sur Name:");
	private JLabel addressLabel = new JLabel("Address:");
	private JLabel phonenumberLabel = new JLabel("Phone No:");
	
	private JTextField usernameText = new JTextField(10);
	private JPasswordField passwordText = new JPasswordField(10);
	private JTextField firstnameText = new JTextField(10);
	private JTextField surnameText = new JTextField(10);
	private JTextField addressText = new JTextField(10);
	private JTextField phonenumberText = new JTextField(10);
	
	private JButton resetButton = new JButton("Reset");
	private JButton submitButton = new JButton("Submit");
	/*Adding in the component to the RegisterPanel and set the layout.*/
	public RegisterPanel(RegisterFrame registerFrame)
	{
		this.setLayout(new GridLayout(7,2,10,10));
		this.add(usernameLabel);
		this.add(usernameText);
		this.add(passwordLabel);
		this.add(passwordText);
		this.add(firstnameLabel);
		this.add(firstnameText);
		this.add(surnameLabel);
		this.add(surnameText);
		this.add(addressLabel);
		this.add(addressText);
		this.add(phonenumberLabel);
		this.add(phonenumberText);
		this.add(resetButton);
		this.add(submitButton);
	}

	public JButton getResetButton() {
		return resetButton;
	}

	public JLabel getUsernameLabel() {
		return usernameLabel;
	}

	public JLabel getPasswordLabel() {
		return passwordLabel;
	}

	public JLabel getFirstnameLabel() {
		return firstnameLabel;
	}

	public JLabel getSurnameLabel() {
		return surnameLabel;
	}

	public JLabel getAddressLabel() {
		return addressLabel;
	}

	public JLabel getPhonenumberLabel() {
		return phonenumberLabel;
	}

	public JTextField getUsernameText() {
		return usernameText;
	}

	public JPasswordField getPasswordText() {
		return passwordText;
	}

	public JTextField getFirstnameText() {
		return firstnameText;
	}

	public JTextField getSurnameText() {
		return surnameText;
	}

	public JTextField getAddressText() {
		return addressText;
	}

	public JTextField getPhonenumberText() {
		return phonenumberText;
	}

	public JButton getSubmitButton() {
		return submitButton;
	}
}
