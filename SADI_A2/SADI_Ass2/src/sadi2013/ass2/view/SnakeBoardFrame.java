/*
 * SOFTWARE ARCHITECTURE DEVELOPMENT & IMPLEMENTATION
 * ASSIGNMENT 2
 * FILE: SNAKEBOARDFRAME.JAVA
 * DONE BY: MASON CHONG SZE ZHEN (s3310388) & MING WEI TEE (s3260935)
 */
package sadi2013.ass2.view;
import java.awt.BorderLayout;
import java.io.Serializable;
import java.util.ArrayList;
import javax.swing.JFrame;
import sadi2013.ass2.model.Coordinate;
import sadi2013.ass2.model.Direction;
import sadi2013.ass2.model.Snake;
import sadi2013.ass2.rmi.client.RMIClient;
import sadi2013.ass2.rmi.server.RMIServerImp;
import sadi2013.ass2.utilities.Protocol;
import sadi2013.ass2.utilities.SnakeControlListener;

@SuppressWarnings("serial")
public class SnakeBoardFrame extends JFrame implements Serializable
{	
	private MainCenterPanel mainCenterPanel;
	private SnakeControlListener snakeControlListener = new SnakeControlListener(this);
	private EastPanel eastPanel = new EastPanel(this);
	private ArrayList<Snake> snakeList;
	private RMIClient client;
	private int snakeID;

	/*
	 * For CLIENT THREAD.
	 */
	public SnakeBoardFrame(int clientID)
	{
		snakeID = clientID;

		snakeList = new ArrayList<Snake>();

		mainCenterPanel = new MainCenterPanel(this);
		setLayout(new BorderLayout());		
		this.add(mainCenterPanel,BorderLayout.CENTER);
		this.add(eastPanel, BorderLayout.WEST);

		/*
		 * Create snakes.
		 */
		for(int i = 0; i < RMIServerImp.clients.size(); i++)
		{
			if(RMIServerImp.posToDisable.get(i) == Protocol.TOP_LEFT)
			{
				this.snakeList.add(new Snake(new Coordinate(1, 32), Direction.RIGHT, snakeID, RMIServerImp.usersScore.get(i)));
			}
			else if(RMIServerImp.posToDisable.get(i) == Protocol.TOP_RIGHT)
			{
				this.snakeList.add(new Snake(new Coordinate(48, 32), Direction.LEFT, snakeID, RMIServerImp.usersScore.get(i)));
			}
			else if(RMIServerImp.posToDisable.get(i) == Protocol.BOT_LEFT)
			{
				this.snakeList.add(new Snake(new Coordinate(1, 1), Direction.RIGHT, snakeID, RMIServerImp.usersScore.get(i)));
			}
			else if(RMIServerImp.posToDisable.get(i) == Protocol.BOT_RIGHT)
			{
				this.snakeList.add(new Snake(new Coordinate(48, 1), Direction.LEFT, snakeID, RMIServerImp.usersScore.get(i)));
			}
		}
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);		
		this.setBounds(0, 0, 1208, 710);
		this.setTitle("Snake");
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	/*
	 * For CLIENT.
	 */
	public SnakeBoardFrame(RMIClient client, int numOfP)
	{
		this.client = client;

		snakeList = new ArrayList<Snake>();
		
		/*
		 * Create dummy snakes.
		 */
		for(int i = 0; i < numOfP; i++)
		{
			snakeList.add(new Snake(i+1));
		}
		
		mainCenterPanel = new MainCenterPanel(this);
		setLayout(new BorderLayout());		
		this.add(mainCenterPanel,BorderLayout.CENTER);
		this.add(eastPanel, BorderLayout.WEST);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);		
		this.setBounds(0, 0, 1208, 710);
		this.addKeyListener(snakeControlListener);
		this.setTitle("Snake");
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	public RMIClient getClient()
	{
		return client;
	}

	public int getSnakeID()
	{
		return snakeID;
	}

	public void setMainCenterPanel(MainCenterPanel mcp)
	{
		this.mainCenterPanel = mcp;
		this.add(mainCenterPanel,BorderLayout.CENTER);
		this.add(eastPanel, BorderLayout.WEST);
	}

	public MainCenterPanel getMainCenterPanel()
	{
		return mainCenterPanel;
	}

	public EastPanel getEastPanel() 
	{
		return eastPanel;
	}

	public ArrayList<Snake> getSnakeList() 
	{
		return snakeList;
	}
	
}
