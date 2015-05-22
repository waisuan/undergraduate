/*
 * SOFTWARE ARCHITECTURE DEVELOPMENT & IMPLEMENTATION
 * ASSIGNMENT 2
 * FILE: CLIENTFRAMEVIEW.JAVA
 * DONE BY: MASON CHONG SZE ZHEN (s3310388) & MING WEI TEE (s3260935)
 */
package sadi2013.ass2.view;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class ClientFrameView extends JFrame
{
	public ClientPanel clientPanel;
	
	public ClientFrameView()
	{
		clientPanel = new ClientPanel();
		this.add(clientPanel);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(600, 738);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setVisible(true);
	}
	
	public String popUpInputBox()
	{
		return JOptionPane.showInputDialog(this, "Please enter your name:");
	}
	
	public void callPanelTextArea(String txt)
	{
		clientPanel.setTextAreaText(txt);
	}
	
	public JButton getButton2p()
	{
		return clientPanel.getButton2p();
	}
	
	public JButton getButton3p()
	{
		return clientPanel.getButton3p();
	}
	
	public JButton getButton4p()
	{
		return clientPanel.getButton4p();
	}
	
	public JButton getTopLeftButton()
	{
		return clientPanel.getTopLeft();
	}
	
	public JButton getTopRightButton()
	{
		return clientPanel.getTopRight();
	}
	
	public JButton getBotLeftButton()
	{
		return clientPanel.getBotLeft();
	}
	
	public JButton getBotRightButton()
	{
		return clientPanel.getBotRight();
	}
	
	public JButton getConnectButton()
	{
		return clientPanel.getConnect();
	}
}

