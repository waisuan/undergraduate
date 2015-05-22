/*
 * SOFTWARE ARCHITECTURE DEVELOPMENT & IMPLEMENTATION
 * ASSIGNMENT 2
 * FILE: SNAKEMOVEMENT.JAVA
 * DONE BY: MASON CHONG SZE ZHEN (s3310388) & MING WEI TEE (s3260935)
 */

package sadi2013.ass2.utilities;
import java.util.Timer;
import java.util.TimerTask;

import sadi2013.ass2.view.SnakeBoardFrame;
/*
 *SnakeMovement class used to manage the auto movement of the snake includes handling the speed up and down functionality. 
 * */
public class SnakeMovement
{
	private Timer timer;
	private int msPerUpdate = 180;
	private int speedCounter = 1;
	private int currentSpeed = msPerUpdate;
	private SnakeBoardFrame snakeBoardFrame;
	
	public SnakeMovement(SnakeBoardFrame snakeBoardFrame)
	{
		this.snakeBoardFrame = snakeBoardFrame;
		timer = new Timer();
		timer.scheduleAtFixedRate(new ScheduleTask(), 100, msPerUpdate);	
	}
	/*
	 * Speed down method to decrease the rate of snake movement.
	 * */
	public void speedDown()
	{
		timer.cancel();
		timer = new Timer();
		if(speedCounter > 1)
		{
			currentSpeed = currentSpeed * 2;
			speedCounter--;
			timer.scheduleAtFixedRate(new ScheduleTask(), 0, currentSpeed);	
		}	
		else
		{
			timer.scheduleAtFixedRate(new ScheduleTask(), 0, currentSpeed);	
			speedCounter = 1;
		}
	}
	/*
	 * Speed up method to increase the rate of snake movement.
	 * */
	public void speedUp()
	{
		timer.cancel();
		timer = new Timer();
		speedCounter++;
		if(speedCounter <= 3)
		{	
			currentSpeed = currentSpeed/2;
			timer.scheduleAtFixedRate(new ScheduleTask(), 0, currentSpeed);
		}
		else
		{
			timer.scheduleAtFixedRate(new ScheduleTask(), 0, currentSpeed);
			speedCounter = 3;
		}
	}
	public SnakeMovement getSnakeMovement()
	{
		return this;
	}
	/*
	 * ScheduleTask class used to constantly move the snake according to current speed.
	 * If there is only 1 snake left on the board. This timer will stop.
	 * */
	class ScheduleTask extends TimerTask 
	{
		@SuppressWarnings("static-access")
		@Override
		public void run() 
		{
			int myIndex = snakeBoardFrame.getSnakeID()-1;

			if(snakeBoardFrame.getSnakeList().get(myIndex).getLength() != 0 && snakeBoardFrame.getSnakeList().get(myIndex).move(snakeBoardFrame.getMainCenterPanel().foodCoor, snakeBoardFrame.getSnakeList()) == false)			
			{
				snakeBoardFrame.getSnakeList().get(myIndex).getBody().clear();
				snakeBoardFrame.getSnakeList().get(myIndex).setLength(0);
			}
			int counter = 0;
			for(int i=0;i < snakeBoardFrame.getSnakeList().size();i++)
			{
				if(snakeBoardFrame.getSnakeList().get(i).getLength() != 0)
				{
					counter++;
				}
			}
			if(counter == 1)
			{
				timer.cancel();
				snakeBoardFrame.getMainCenterPanel().repaint();
			}
		}
	}
}
