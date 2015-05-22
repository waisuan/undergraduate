/*
 * SOFTWARE ARCHITECTURE DEVELOPMENT & IMPLEMENTATION
 * ASSIGNMENT 2
 * FILE: SERVERPANEL.JAVA
 * DONE BY: SIA WAI SUAN (s3308555)
 */
package sadi2013.ass2.view;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class ServerPanel extends JPanel{

	private JTextArea jta = new JTextArea();
	
	public ServerPanel()
	{
		this.setLayout(new BorderLayout());
		
		jta.setBorder( new EmptyBorder( 8, 8, 8, 8 ) );
		
		jta.setEditable(false);
		
		jta.setVisible(true);
		
		this.add(jta, BorderLayout.CENTER);
		
		this.setVisible(true);
	}
	
	public void setTextAreaText(String txt)
	{
		jta.append(txt + "\n");
	}
}
