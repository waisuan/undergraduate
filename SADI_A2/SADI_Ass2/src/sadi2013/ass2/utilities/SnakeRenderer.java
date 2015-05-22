/*
 * SOFTWARE ARCHITECTURE DEVELOPMENT & IMPLEMENTATION
 * ASSIGNMENT 2
 * FILE: SNAKERENDERER.JAVA
 * DONE BY: MASON CHONG SZE ZHEN (s3310388) & MING WEI TEE (s3260935)
 */
package sadi2013.ass2.utilities;

import java.rmi.RemoteException;
import java.util.Timer;
import java.util.TimerTask;
import sadi2013.ass2.model.Snake;
import sadi2013.ass2.rmi.server.RMIServerImp;
import sadi2013.ass2.view.SnakeBoardFrame;

/*
 * SnakeRenderer class performs rendering for the board view to refresh the snake movement.
 * */
public class SnakeRenderer
{
	private static final int RENDER_SPEED = 50;
	private Timer timerRender;
	private SnakeBoardFrame snakeBoardFrame;

	public SnakeRenderer(SnakeBoardFrame snakeBoardFrame)
	{
		this.snakeBoardFrame = snakeBoardFrame;
		timerRender = new Timer();
		timerRender.scheduleAtFixedRate(new ScheduleTaskRender(), 100, RENDER_SPEED);	
	}
	/*Rendering schedule task CONSTANTLY sends the new board state to the client for repainting.*/
	class ScheduleTaskRender extends TimerTask
	{
		@SuppressWarnings("static-access")
		@Override
		public void run() 
		{
			snakeBoardFrame.getMainCenterPanel().resetRectangleArray();
			snakeBoardFrame.getMainCenterPanel().repaint();
			/*A fixed format of string being constructed here and send to the client side.*/
			int myIndex = snakeBoardFrame.getSnakeID()-1;

			for(int i = 0; i < RMIServerImp.srList.size(); i++)
				//			for(int i = 0; i < snakeBoardFrame.getSnakeList().size(); i++)
			{
				if((snakeBoardFrame.getSnakeID()-1) != i)
				{
					/*
					 * Don't send snake body until the other clients are ready.
					 */
					while(RMIServerImp.srList.get(i) == null)
					{
					}

					RMIServerImp.srList.get(i).sendMyBody(snakeBoardFrame.getSnakeList().get(myIndex));
				}
			}

			/*
			 * Construction of message to be sent to the client.
			 */
			String boardDetails = snakeBoardFrame.getSnakeList().size() + ";";

			for(int i = 0; i < snakeBoardFrame.getSnakeList().size(); i++)
			{
				boardDetails += i + ",";

				boardDetails += snakeBoardFrame.getSnakeList().get(i).getSnakeScore() + ",";

				for(int j = 0; j < snakeBoardFrame.getSnakeList().get(i).getLength(); j++)
				{
					boardDetails += snakeBoardFrame.getSnakeList().get(i).getBody().get(j).x + ",";
					boardDetails += snakeBoardFrame.getSnakeList().get(i).getBody().get(j).y + ",";
				}

				boardDetails += ";";
			}

			boardDetails += "/";

			for(int i = 0; i < snakeBoardFrame.getMainCenterPanel().foodCoor.size(); i++)
			{
				boardDetails += snakeBoardFrame.getMainCenterPanel().foodCoor.get(i).x + "," + snakeBoardFrame.getMainCenterPanel().foodCoor.get(i).y + ",";
			}

			try {
				RMIServerImp.clients.get(snakeBoardFrame.getSnakeID()-1).updateSnakeBoard(boardDetails);
			} catch (RemoteException e) {
				timerRender.cancel();
			}
		}		
	}
	/*Send own body to other client threads so that they can update their snake list.*/
	public void sendMyBody(Snake opponentBody)
	{
		snakeBoardFrame.getSnakeList().set(opponentBody.getCurrSnakeID()-1, opponentBody);
	}
}

