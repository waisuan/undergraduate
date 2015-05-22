/*
 * SOFTWARE ARCHITECTURE DEVELOPMENT & IMPLEMENTATION
 * ASSIGNMENT 2
 * FILE: EASTPANEL.JAVA
 * DONE BY: MING WEI TEE (s3260935)
 */
package sadi2013.ass2.view;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.util.Collections;
import java.util.Comparator;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JPanel;
import sadi2013.ass2.model.Snake;
import sadi2013.ass2.view.SnakeBoardFrame;


@SuppressWarnings("serial")
public class EastPanel extends JPanel
{
	
	//Set the font for the painting
	private static final Font LARGE_FONT = new Font("Comic Sans MS", Font.BOLD, 32);	

	private static final Font MEDIUM_FONT = new Font("Tahoma", Font.BOLD, 16);

	private static final Font SMALL_FONT = new Font("Tahoma", Font.BOLD, 12);	

	private static final Font PLAYER_FONT = new Font("Tahoma", Font.BOLD, 22);

	private Graphics2D graphic2;
	private Timer timerRender;
	private SnakeBoardFrame snakeBoardFrame;
	private int rankDepth = 15;
	private int noOfPlayers;
	private int playerOneScore = 0;
	private int playerTwoScore = 0;
	private int playerThreeScore = 0;
	private int playerFourScore = 0;
	private int playerOnePos = 360;
	private int playerTwoPos = playerOnePos + 60;
	private int playerThreePos = playerTwoPos + 60;
	private int playerFourPos = playerThreePos + 60;
	private int currentOnePos = playerOnePos;
	private int currentTwoPos = playerTwoPos;
	private int currentThreePos = playerThreePos;
	private int currentFourPos = playerFourPos;
	private int playerOneRank = 1;
	private int playerTwoRank = 2;
	private int playerThreeRank = 3;
	private int playerFourRank = 4;

	private int renderSpeed = 130;

	public EastPanel(SnakeBoardFrame snakeBoardFrame) 
	{
		setPreferredSize(new Dimension(200, 710));
		this.snakeBoardFrame = snakeBoardFrame;
		this.timerRender = new Timer();
		this.timerRender.scheduleAtFixedRate(new ScheduleTaskRender(this), 100, renderSpeed);	
		this.setLayout(new GridLayout(10,0));
		this.setBackground(Color.BLACK);
	}	

	public void resetNumOfPlayers(int numOfP)
	{
		this.noOfPlayers = numOfP;
	}

	@SuppressWarnings("static-access")
	public void drawInfo(Graphics2D graphic2)
	{
		//draw the title of the snake game
		graphic2.setColor(Color.WHITE);		
		graphic2.setFont(LARGE_FONT);
		graphic2.drawString("Snake Game", 5, 35);

		//draw the movement control strings
		graphic2.setFont(MEDIUM_FONT);
		graphic2.drawString("Movement Controls", 9, 80);
		graphic2.setFont(SMALL_FONT);
		graphic2.drawString("Move Up: W", 30, 100);
		graphic2.drawString("Move Down: S", 30, 120);
		graphic2.drawString("Move Left: A", 30, 140);
		graphic2.drawString("Move Right: D", 30, 160);
		graphic2.drawString("Reverse Snake: R", 30, 180);

		//draw the speed control strings
		graphic2.setFont(MEDIUM_FONT);
		graphic2.drawString("Speed Controls", 9, 220);
		graphic2.setFont(SMALL_FONT);
		graphic2.drawString("Speed Up: Up Arrow", 30, 240);
		graphic2.drawString("Speed Down: Down Arrow", 30, 260);	
		graphic2.drawString("_______________________", 8, 280);

		//draw the ranking title
		graphic2.setFont(LARGE_FONT);
		graphic2.drawString("Rankings", 35, 320);

		//draw the player strings	
		graphic2.setFont(PLAYER_FONT);		
		if(noOfPlayers == 2)
		{
			//do not update if the game ends
			if(snakeBoardFrame.getClient().endOfGame == false)
			{
				//getting player scores
				playerOneScore = snakeBoardFrame.getSnakeList().get(0).getSnakeScore();
				playerTwoScore = snakeBoardFrame.getSnakeList().get(1).getSnakeScore();
				if(playerOneScore > playerTwoScore)
				{
					currentOnePos = playerOnePos;
					playerOneRank = 1;

					currentTwoPos = playerTwoPos;
					playerTwoRank = 2;
				}
				else
				{
					currentTwoPos = playerOnePos;
					playerTwoRank = 1;

					currentOnePos = playerTwoPos;
					playerOneRank = 2;
				}
			}

			graphic2.setColor(Color.GREEN);
			graphic2.drawString("Rank " + playerOneRank + " : " + playerOneScore, rankDepth, currentOnePos); 

			graphic2.setColor(Color.RED);
			graphic2.drawString("Rank " + playerTwoRank + " : " + playerTwoScore, rankDepth, currentTwoPos);
		}
		else if(noOfPlayers == 3)
		{		
			if(snakeBoardFrame.getClient().endOfGame == false)
			{
				playerOneScore = snakeBoardFrame.getSnakeList().get(0).getSnakeScore();
				playerTwoScore = snakeBoardFrame.getSnakeList().get(1).getSnakeScore();
				playerThreeScore = snakeBoardFrame.getSnakeList().get(2).getSnakeScore();

				//if player one is ranked first
				if(playerOneScore >= playerTwoScore && playerOneScore >= playerThreeScore)
				{
					currentOnePos = playerOnePos;
					playerOneRank = 1;

					if(playerTwoScore >= playerThreeScore)
					{
						currentTwoPos = playerTwoPos;
						playerTwoRank = 2;

						currentThreePos = playerThreePos;
						playerThreeRank = 3;
					}
					else
					{
						currentThreePos = playerTwoPos;
						playerThreeRank = 2;

						currentTwoPos = playerThreePos;
						playerTwoRank = 3;
					}
				}

				//if player two is ranked first
				if(playerTwoScore >= playerOneScore && playerTwoScore >= playerThreeScore)
				{
					currentTwoPos = playerOnePos;
					playerTwoRank = 1;

					if(playerOneScore >= playerThreeScore)
					{
						currentOnePos = playerTwoPos;
						playerOneRank = 2;

						currentThreePos = playerThreePos;
						playerThreeRank = 3;
					}
					else
					{
						currentThreePos = playerTwoPos;
						playerThreeRank = 2;

						currentOnePos = playerThreePos;
						playerOneRank = 3;
					}
				}

				//if player three is ranked first
				if(playerThreeScore >= playerOneScore && playerThreeScore >= playerTwoScore)
				{
					currentThreePos = playerOnePos;
					playerThreeRank = 1;

					if(playerOneScore >= playerTwoScore)
					{
						currentOnePos = playerTwoPos;
						playerOneRank = 2;

						currentTwoPos = playerThreePos;
						playerTwoRank = 3;
					}
					else
					{
						currentTwoPos = playerTwoPos;
						playerTwoRank = 2;

						currentOnePos = playerThreePos;
						playerOneRank = 3;
					}
				}	
			}

			//Update the new score for the players
			graphic2.setColor(Color.GREEN);
			graphic2.drawString("Rank " + playerOneRank + " : " + playerOneScore, rankDepth, currentOnePos);

			graphic2.setColor(Color.RED);
			graphic2.drawString("Rank " + playerTwoRank + " : " + playerTwoScore, rankDepth, currentTwoPos);

			graphic2.setColor(Color.BLUE);
			graphic2.drawString("Rank " + playerThreeRank + " : " + playerThreeScore, rankDepth, currentThreePos);
		}
		else if(noOfPlayers == 4)
		{
			//do not update if the game has ended
			if(snakeBoardFrame.getClient().endOfGame == false)
			{
				Collections.sort(snakeBoardFrame.getSnakeList(), new Comparator<Snake>() {

					public int compare(Snake s1, Snake s2) {

						return s1.getSnakeScore() - s2.getSnakeScore();
					}
				});

				//find out who is the first rank player
				if(snakeBoardFrame.getSnakeList().get(3).getCurrSnakeID() == 0)
				{
					playerOneScore = snakeBoardFrame.getSnakeList().get(3).getSnakeScore();
					currentOnePos = playerOnePos;
					playerOneRank = 1;
				}
				else if(snakeBoardFrame.getSnakeList().get(3).getCurrSnakeID() == 1)
				{
					playerTwoScore = snakeBoardFrame.getSnakeList().get(3).getSnakeScore();
					currentTwoPos = playerOnePos;
					playerTwoRank = 1;
				}
				else if(snakeBoardFrame.getSnakeList().get(3).getCurrSnakeID() == 2)
				{
					playerThreeScore = snakeBoardFrame.getSnakeList().get(3).getSnakeScore();
					currentThreePos = playerOnePos;
					playerThreeRank = 1;
				}
				else if(snakeBoardFrame.getSnakeList().get(3).getCurrSnakeID() == 3)
				{
					playerFourScore = snakeBoardFrame.getSnakeList().get(3).getSnakeScore();
					currentFourPos = playerOnePos;
					playerFourRank = 1;
				}

				//find out who is the second rank player
				if(snakeBoardFrame.getSnakeList().get(2).getCurrSnakeID() == 0)
				{
					playerOneScore = snakeBoardFrame.getSnakeList().get(2).getSnakeScore();
					currentOnePos = playerTwoPos;
					playerOneRank = 2;
				}
				else if(snakeBoardFrame.getSnakeList().get(2).getCurrSnakeID() == 1)
				{
					playerTwoScore = snakeBoardFrame.getSnakeList().get(2).getSnakeScore();
					currentTwoPos = playerTwoPos;
					playerTwoRank = 2;
				}
				else if(snakeBoardFrame.getSnakeList().get(2).getCurrSnakeID() == 2)
				{
					playerThreeScore = snakeBoardFrame.getSnakeList().get(2).getSnakeScore();
					currentThreePos = playerTwoPos;
					playerThreeRank = 2;
				}
				else if(snakeBoardFrame.getSnakeList().get(2).getCurrSnakeID() == 3)
				{
					playerFourScore = snakeBoardFrame.getSnakeList().get(2).getSnakeScore();
					currentFourPos = playerTwoPos;
					playerFourRank = 2;
				}		

				//find out who is the third rank
				if(snakeBoardFrame.getSnakeList().get(1).getCurrSnakeID() == 0)
				{
					playerOneScore = snakeBoardFrame.getSnakeList().get(1).getSnakeScore();
					currentOnePos = playerThreePos;
					playerOneRank = 3;				
				}
				else if(snakeBoardFrame.getSnakeList().get(1).getCurrSnakeID() == 1)
				{
					playerTwoScore = snakeBoardFrame.getSnakeList().get(1).getSnakeScore();
					currentTwoPos = playerThreePos;
					playerTwoRank = 3;		
				}
				else if(snakeBoardFrame.getSnakeList().get(1).getCurrSnakeID() == 2)
				{
					playerThreeScore = snakeBoardFrame.getSnakeList().get(1).getSnakeScore();
					currentThreePos = playerThreePos;
					playerThreeRank = 3;		
				}
				else if(snakeBoardFrame.getSnakeList().get(1).getCurrSnakeID() == 3)
				{
					playerFourScore = snakeBoardFrame.getSnakeList().get(1).getSnakeScore();
					currentFourPos = playerThreePos;
					playerFourRank = 3;		
				}

				//find out who is the fourth rank
				if(snakeBoardFrame.getSnakeList().get(0).getCurrSnakeID() == 0)
				{
					playerOneScore = snakeBoardFrame.getSnakeList().get(0).getSnakeScore();
					currentOnePos = playerFourPos;
					playerOneRank = 4;		
				}
				else if(snakeBoardFrame.getSnakeList().get(0).getCurrSnakeID() == 1)
				{
					playerTwoScore = snakeBoardFrame.getSnakeList().get(0).getSnakeScore();
					currentTwoPos = playerFourPos;
					playerTwoRank = 4;		
				}
				else if(snakeBoardFrame.getSnakeList().get(0).getCurrSnakeID() == 2)
				{
					playerThreeScore = snakeBoardFrame.getSnakeList().get(0).getSnakeScore();
					currentThreePos = playerFourPos;
					playerThreeRank = 4;		
				}
				else if(snakeBoardFrame.getSnakeList().get(0).getCurrSnakeID() == 3)
				{
					playerFourScore = snakeBoardFrame.getSnakeList().get(0).getSnakeScore();
					currentFourPos = playerFourPos;
					playerFourRank = 4;		
				}
			}
			
			//Update the score board
			graphic2.setColor(Color.GREEN);
			graphic2.drawString("Rank " + playerOneRank + " : " + playerOneScore, rankDepth, currentOnePos);


			graphic2.setColor(Color.RED);
			graphic2.drawString("Rank " + playerTwoRank + " : " + playerTwoScore, rankDepth, currentTwoPos);


			graphic2.setColor(Color.BLUE);
			graphic2.drawString("Rank " + playerThreeRank + " : " + playerThreeScore, rankDepth, currentThreePos);


			graphic2.setColor(Color.YELLOW);
			graphic2.drawString("Rank " + playerFourRank + " : " + playerFourScore, rankDepth, currentFourPos);
		}
	}

	// the painComponent for repainting
	protected void paintComponent(Graphics graphic)
	{
		super.paintComponent(graphic);

		graphic2 = (Graphics2D) graphic;

		RenderingHints rehnderingHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		rehnderingHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

		graphic2.setRenderingHints(rehnderingHints);

		drawInfo(graphic2);

	}

	//this is the timer to refresh the score board
	public class ScheduleTaskRender extends TimerTask
	{
		private EastPanel eastPanel;
		public ScheduleTaskRender(EastPanel eastPanel)
		{
			this.eastPanel = eastPanel;
		}
		@Override
		public void run() 
		{
			eastPanel.repaint();
		}		
	}	
}
