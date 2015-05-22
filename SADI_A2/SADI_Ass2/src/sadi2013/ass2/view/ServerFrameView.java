/*
 * SOFTWARE ARCHITECTURE DEVELOPMENT & IMPLEMENTATION
 * ASSIGNMENT 2
 * FILE: SERVERFRAMEVIEW.JAVA
 * DONE BY: SIA WAI SUAN (s3308555)
 */
package sadi2013.ass2.view;

import java.awt.BorderLayout;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class ServerFrameView extends JFrame{
	
	private ServerPanel serverPanel;
	private JFrame jf;
	
	public ServerFrameView()
	{
		jf = new JFrame();
		
		jf.setLayout(new BorderLayout());
		
		serverPanel = new ServerPanel();
		
		jf.setTitle("Server");
		
		jf.setSize(500,400); // default size is 0,0
		
		jf.setLocationRelativeTo(null);
		
		jf.add(serverPanel, BorderLayout.CENTER);

		jf.setResizable(false);
		
		jf.setVisible(true);
	}
	
	public void callPanelTextArea(String txt)
	{
		serverPanel.setTextAreaText(txt);
	}
}
