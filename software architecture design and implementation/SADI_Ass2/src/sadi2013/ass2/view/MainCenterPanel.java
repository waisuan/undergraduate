/*
 * SOFTWARE ARCHITECTURE DEVELOPMENT & IMPLEMENTATION
 * ASSIGNMENT 2
 * FILE: MAINCENTERPANEL.JAVA
 * DONE BY: MASON CHONG SZE ZHEN (s3310388) & MING WEI TEE (s3260935)
 */
package sadi2013.ass2.view;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import javax.swing.JPanel;

import sadi2013.ass2.model.Coordinate;

@SuppressWarnings("serial")
public class MainCenterPanel extends JPanel
{	
	private static final int rectangleWidth = 20;
	private static final int rectangleHeight = 20;
	int startPos = 0;
	int endPos = 200;
	private SnakeBoardFrame snakeBoardFrame;
	private Graphics2D graphic2;

	private ArrayList<ArrayList<Rectangle2D.Float>> rectangleArray = new ArrayList<ArrayList<Rectangle2D.Float>>();
	private ArrayList<RoundRectangle2D.Float> foodArray = new ArrayList<RoundRectangle2D.Float>();
	public static ArrayList<Coordinate> foodCoor = new ArrayList<Coordinate>();
	private ArrayList<Color> snakeColor = new ArrayList<Color>();	

	public MainCenterPanel(SnakeBoardFrame snakeBoardFrame)
	{
		this.snakeColor.add(Color.GREEN);
		this.snakeColor.add(Color.RED);
		this.snakeColor.add(Color.BLUE);
		this.snakeColor.add(Color.YELLOW);

		/*
		 * Create food on the map/board.
		 */
		synchronized(foodCoor)
		{
			if(foodCoor.size() == 0)
			{
				foodCoor.add(new Coordinate(17, 10));
				foodCoor.add(new Coordinate(20, 15));
				foodCoor.add(new Coordinate(22,18));
			}
		}

		this.snakeBoardFrame = snakeBoardFrame;
		this.setBounds(0, 0, 1008, 710);
		this.setBackground(Color.BLACK);
		this.setVisible(true);
		this.setDoubleBuffered(true);		
	}

	public void setStartPos(int startPos) 
	{
		this.startPos = startPos;
	}

	public void setFood(int index, int x, int y, int w, int v)
	{
		foodArray.get(index).setRoundRect(x, y, rectangleWidth, rectangleHeight, w, v);
	}

	public void resetFoodArray()
	{
		foodArray.clear();
	}

	public void setRectangle(int j, int i, int x, int y)
	{
		rectangleArray.get(j).get(i).setFrame(x, y, rectangleWidth, rectangleHeight);
	}

	public void resetRectangleArray()
	{
		rectangleArray.clear();
		rectangleArray.trimToSize();
	}

	public SnakeBoardFrame getSnakeBoardFrame()
	{
		return snakeBoardFrame;
	}

	public JPanel getMainCenterPanel()
	{
		return this;
	}

	/*draw the snake on to the board*/
	public void drawSnake(Graphics2D graphic2, ArrayList<Color> snakeColor)
	{	
		for(int j = 0;j<snakeBoardFrame.getSnakeList().size();j++)
		{	
			graphic2.setColor(snakeColor.get(j));
			rectangleArray.add(new ArrayList<Rectangle2D.Float>());
			for(int i = 0;i < snakeBoardFrame.getSnakeList().get(j).getLength();i++)
			{	
				rectangleArray.get(j).add(new Rectangle2D.Float());
				this.setRectangle(j,i, snakeBoardFrame.getSnakeList().get(j).getBody().get(i).x*rectangleWidth, snakeBoardFrame.getSnakeList().get(j).getBody().get(i).y*rectangleHeight);
				graphic2.fill(rectangleArray.get(j).get(i));		
			}
		}
		Toolkit.getDefaultToolkit().sync();	
	}

	//draw the food on to the board
	@SuppressWarnings("static-access")
	public void drawFood(Graphics2D graphic2, Color color)
	{
		graphic2.setColor(color.GRAY);

		for(int i = 0;i < foodCoor.size();i++)
		{
			foodArray.add(new RoundRectangle2D.Float());
			this.setFood(i, foodCoor.get(i).x*rectangleWidth, foodCoor.get(i).y*rectangleHeight, 20, 20);
			graphic2.fill(foodArray.get(i));
		}
	}

	//draw the board on the panel
	public void drawBoard(Graphics2D graphic2)
	{
		for (int x=0; x<50; x++)
		{
			for (int y=0; y<34; y++)
			{
				graphic2.drawRect(x*20, y*20, 20, 20);
			}
		}
	}

	protected void paintComponent(Graphics graphic)
	{
		super.paintComponent(graphic);

		graphic2 = (Graphics2D) graphic;

		RenderingHints rehnderingHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		rehnderingHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

		graphic2.setRenderingHints(rehnderingHints);
		drawSnake(graphic2, snakeColor);	
		drawFood(graphic2, Color.MAGENTA);
		drawBoard(graphic2);

		snakeBoardFrame.getClient().paintFinish();
	}
}
