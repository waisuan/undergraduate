/*
 * SOFTWARE ARCHITECTURE DEVELOPMENT & IMPLEMENTATION
 * ASSIGNMENT 2
 * FILE: RESETBUTTONLISTENER.JAVA
 * DONE BY: MASON CHONG SZE ZHEN (s3310388)
 */
package sadi2013.ass2.loginview;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/*ResetButtonListener used to empty all the text field in the register frame.*/
public class ResetButtonListener implements ActionListener
{
	private static final String EMPTY_STRING = "";
	
	private RegisterPanel registerPanel;
	
	public ResetButtonListener(RegisterPanel registerPanel, RegisterFrame registerFrame)
	{
		this.registerPanel = registerPanel;
	}
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		this.registerPanel.getUsernameText().setText(EMPTY_STRING);
		this.registerPanel.getPasswordText().setText(EMPTY_STRING);
		this.registerPanel.getFirstnameText().setText(EMPTY_STRING);
		this.registerPanel.getSurnameText().setText(EMPTY_STRING);
		this.registerPanel.getAddressText().setText(EMPTY_STRING);
		this.registerPanel.getPhonenumberText().setText(EMPTY_STRING);
	}

}
