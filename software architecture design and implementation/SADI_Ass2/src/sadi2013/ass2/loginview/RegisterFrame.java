/*
 * SOFTWARE ARCHITECTURE DEVELOPMENT & IMPLEMENTATION
 * ASSIGNMENT 2
 * FILE: REGISTERFRAME.JAVA
 * DONE BY: MASON CHONG SZE ZHEN (s3310388)
 */
package sadi2013.ass2.loginview;
import java.awt.*;

import javax.swing.*;

import sadi2013.ass2.rmi.client.RMIClient;
/*RegisterFrame used to contain RegisterPanel*/
@SuppressWarnings("serial")
public class RegisterFrame extends JFrame
{
	private RegisterPanel registerPanel;
	/*Constructor of RegisterFrame initialise the frame and add listener to the buttons.*/
	public RegisterFrame(RMIClient client)
	{
		registerPanel = new RegisterPanel(this);
		registerPanel.getSubmitButton().addActionListener(new SubmitButtonListener(registerPanel, this, client));
		registerPanel.getResetButton().addActionListener(new ResetButtonListener(registerPanel, this));
		this.setTitle("Snake Game Registration");
		this.setSize(300,300);
		this.setResizable(false);
		this.setLayout(new BorderLayout());
		this.add(registerPanel, BorderLayout.CENTER);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
}
