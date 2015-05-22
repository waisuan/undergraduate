/*
 * SOFTWARE ARCHITECTURE DEVELOPMENT & IMPLEMENTATION
 * ASSIGNMENT 2
 * FILE: SNAKECONTROLLISTENER.JAVA
 * DONE BY: MING WEI TEE (s3260935)
 */
package sadi2013.ass2.utilities;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.Serializable;

import sadi2013.ass2.view.SnakeBoardFrame;

@SuppressWarnings("serial")
public class SnakeControlListener implements KeyListener, Serializable
{
	SnakeBoardFrame snakeBoardFrame;
	public SnakeControlListener(SnakeBoardFrame snakeBoardFrame) 
	{
		this.snakeBoardFrame = snakeBoardFrame;
	}
	@Override
	public void keyPressed(KeyEvent e)
	{
		//movement key listener
		switch(e.getKeyCode()) {
		case KeyEvent.VK_UP:		
			snakeBoardFrame.getClient().getSnakeSpeed(Protocol.SPEED_UP);			
			break;
		case KeyEvent.VK_DOWN:		
			snakeBoardFrame.getClient().getSnakeSpeed(Protocol.SPEED_DOWN);			
			break;
		case KeyEvent.VK_W:			
			snakeBoardFrame.getClient().getSnakeMove(Protocol.UP);			
			break;
		case KeyEvent.VK_S:			
			snakeBoardFrame.getClient().getSnakeMove(Protocol.DOWN);			
			break;
		case KeyEvent.VK_A:			
			snakeBoardFrame.getClient().getSnakeMove(Protocol.LEFT);			
			break;
		case KeyEvent.VK_D:
			snakeBoardFrame.getClient().getSnakeMove(Protocol.RIGHT);			
			break;	
		case KeyEvent.VK_R:
			snakeBoardFrame.getClient().getSnakeRev();
			break;
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}
