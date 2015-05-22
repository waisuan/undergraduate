/*
 * SOFTWARE ARCHITECTURE DEVELOPMENT & IMPLEMENTATION
 * ASSIGNMENT 2
 * FILE: RMISERVERIMP.JAVA
 * DONE BY: SIA WAI SUAN (s3308555)
 */
package sadi2013.ass2.rmi.server;

import java.sql.Statement;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;

import sadi2013.ass2.db.SnakeDatabaseManager;
import sadi2013.ass2.model.Direction;
import sadi2013.ass2.rmi.client.Callback;
import sadi2013.ass2.utilities.Protocol;
import sadi2013.ass2.utilities.SnakeMovement;
import sadi2013.ass2.utilities.SnakeRenderer;
import sadi2013.ass2.view.ServerFrameView;
import sadi2013.ass2.view.SnakeBoardFrame;

/*
 * Server class.
 * Client class is able to directly call methods that are on here.
 */
public class RMIServerImp extends UnicastRemoteObject implements RMIServer{

	private static final long serialVersionUID = -8885106673072255879L;

	public static ArrayList<Callback> clients = new ArrayList<Callback>();
	
	private ArrayList<Callback> waitingClients = new ArrayList<Callback>();

	private static int clientID = 0;

	private int numOfP = 0;

	private int capacity = 0;

	public static ArrayList<Integer> posToDisable = new ArrayList<Integer>();

	private int hasSelPos = 0;

	private int currClient = 0;

	private SnakeBoardFrame sbf;
	
	private SnakeRenderer snakeRenderer = null;
	
	private SnakeMovement snakeMovement = null;

	public static ArrayList<SnakeBoardFrame> sbfList = new ArrayList<SnakeBoardFrame>();
	
	public static ArrayList<SnakeRenderer> srList = new ArrayList<SnakeRenderer>();
	
	public static ArrayList<SnakeMovement> smList = new ArrayList<SnakeMovement>();

	private Connection connection;
	
	private Statement statement;

	private ArrayList<String> usersInGame = new ArrayList<String>();
	
	public static ArrayList<Integer> usersScore = new ArrayList<Integer>();

	private ServerFrameView sfv;

	public static void main(String argv[]) {
		try {
			//Binds the server to the rmiregistry.
			RMIServerImp serverImp = new RMIServerImp();
			Naming.rebind("//131.170.206.41/RMIServer", serverImp);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	//Compares the given user details with the database and return a true/false integer value.
	public int lookupDB(String username, String password)
	{
		if(SnakeDatabaseManager.login(statement, username, password))
		{
			for(int i = 0; i < usersInGame.size(); i++)
			{
				if(username.equals(usersInGame.get(i)))
				{
					return Protocol.ALREADY_LOGIN;
				}
			}

			usersInGame.add(username);

			sfv.callPanelTextArea("[" + new Date() + "] " + username + " has logged in...");

			usersScore.add(SnakeDatabaseManager.getScore(statement, username));
			return Protocol.LOGIN_SUCCESS;
		}
		else
		{
			return Protocol.LOGIN_FAIL;
		}
	}

	//Retrieves the specified user details from the database and returns to the caller.
	public ArrayList<String> getUserDetails(String username)
	{
		return SnakeDatabaseManager.getUserDetails(statement, username);
	}

	//Registers new client in the DB.
	public boolean regInDB(String data)
	{
		return SnakeDatabaseManager.insertPlayer(statement, data);
	}

	//Constructor.
	public RMIServerImp() throws RemoteException {
		//Setup connection with the database.
		connection = SnakeDatabaseManager.initialiseConnection();
		statement = SnakeDatabaseManager.initialiseDatabase(connection);

		sfv = new ServerFrameView();
		sfv.callPanelTextArea("[" + new Date() + "] Server is started...");
	}

	//A client has chosen to connect to the server.
	//If capacity is full, reject player.
	@Override
	public int wantToJoin(Callback c) throws RemoteException {

		//If the first client has not selected a game capacity.
		if(numOfP != 0 && capacity == 0)
		{	
			waitingClients.add(c);

			return Protocol.WAIT;
		}

		//If capacity quota has yet to be matched.
		if(numOfP <= capacity)
		{
			clients.add(c);

			this.numOfP++;

			//If quota is met and enough players have joined the game.
			if(numOfP == capacity)
			{
				initGameSettings(currClient);
				currClient++;
			}

			sfv.callPanelTextArea("[" + new Date() + "] Player " + (clientID + 1) + " has CONNECTED to the game...");
			sfv.callPanelTextArea("[" + new Date() + "] There are now " + numOfP + " player(s) in the game...");

			return clientID++;
		}

		return Protocol.REJECT;
	}

	//Initialize game capacity.
	public void setNumOfP(Callback c, int numOfP) throws RemoteException {
		this.capacity = numOfP;

		sfv.callPanelTextArea("[" + new Date() + "] Capacity of game has been set to " + capacity + "...");

		checkForWaitingClients();
	}

	//If there are any waiting clients, either accept/reject them.
	public void checkForWaitingClients() throws RemoteException
	{
		if(waitingClients.size() != 0)
		{
			for(int i = 0; i < waitingClients.size(); i++)
			{
				if(this.numOfP != capacity)
				{
					clients.add(waitingClients.get(i));

					this.numOfP++;

					clients.get(clientID).setClientID(clientID);

					sfv.callPanelTextArea("[" + new Date() + "] Player " + (clientID + 1) + " has CONNECTED to the game...");
					sfv.callPanelTextArea("[" + new Date() + "] There are now " + numOfP + " player(s) in the game...");

					clientID++;
				}
				else
				{
					for(int j = i; j < waitingClients.size(); j++)
					{
						waitingClients.get(i).rejectPlayer();

						sfv.callPanelTextArea("[" + new Date() + "] Rejecting LEFTOVER players from the game...");
					}

					waitingClients.clear();

					break;
				}
			}

			if(numOfP == capacity)
			{
				initGameSettings(currClient);
				currClient++;
			}
		}
	}

	//Disable cell positions that have already been selected.
	public void initGameSettings(int whichClient)
	{
		try {
			sfv.callPanelTextArea("[" + new Date() + "] Requesting Player " + (whichClient + 1) + " to select CELL POSITION...");

			clients.get(whichClient).clientSelectPos(whichClient, posToDisable);
		} catch (RemoteException e) {

			e.printStackTrace();
		}
	}

	//Acknowledge that the client has selected a cell position.
	public void setPos(int pos)
	{
		posToDisable.add(pos);
		hasSelPos++;

		if(hasSelPos != capacity)
		{
			initGameSettings(currClient);
			currClient++;
		}
		else
		{
			initStartGame();
		}
	}

	//Game is ready to begin.
	public void initStartGame()
	{		
		sfv.callPanelTextArea("[" + new Date() + "] Starting game...");
		for(int i = 0; i < clients.size(); i++)
		{
			try {
				clients.get(i).startGame(clients.size());

				sbf = new SnakeBoardFrame(i+1);
				sbf.getMainCenterPanel().setVisible(false);
				sbf.setVisible(false);

				sbfList.add(sbf);

				snakeMovement = new SnakeMovement(sbf);

				smList.add(snakeMovement);

				snakeRenderer = new SnakeRenderer(sbf);

				srList.add(snakeRenderer);

			} catch (RemoteException e) {

				e.printStackTrace();
			}
		}
		sfv.callPanelTextArea("[" + new Date() + "] Game started...");
	}

	//Accept changes in the snake movement and update the game board.
	public void move(int nextMove, int whichClient)
	{
		if(nextMove == Protocol.UP)
		{
			if(sbfList.get(whichClient).getSnakeList().get(sbfList.get(whichClient).getSnakeID()-1).getDirection() == Direction.LEFT || sbfList.get(whichClient).getSnakeList().get(sbfList.get(whichClient).getSnakeID()-1).getDirection() == Direction.RIGHT)
			{
				sbfList.get(whichClient).getSnakeList().get(sbfList.get(whichClient).getSnakeID()-1).setDirection(Direction.UP);
			}
		}
		else if(nextMove == Protocol.DOWN)
		{
			if(sbfList.get(whichClient).getSnakeList().get(sbfList.get(whichClient).getSnakeID()-1).getDirection() == Direction.LEFT || sbfList.get(whichClient).getSnakeList().get(sbfList.get(whichClient).getSnakeID()-1).getDirection() == Direction.RIGHT)
			{
				sbfList.get(whichClient).getSnakeList().get(sbfList.get(whichClient).getSnakeID()-1).setDirection(Direction.DOWN);
			}
		}
		else if(nextMove == Protocol.LEFT)
		{
			if(sbfList.get(whichClient).getSnakeList().get(sbfList.get(whichClient).getSnakeID()-1).getDirection() == Direction.UP || sbfList.get(whichClient).getSnakeList().get(sbfList.get(whichClient).getSnakeID()-1).getDirection() == Direction.DOWN)
			{
				sbfList.get(whichClient).getSnakeList().get(sbfList.get(whichClient).getSnakeID()-1).setDirection(Direction.LEFT);
			}
		}
		else if(nextMove == Protocol.RIGHT)
		{
			if(sbfList.get(whichClient).getSnakeList().get(sbfList.get(whichClient).getSnakeID()-1).getDirection() == Direction.UP || sbfList.get(whichClient).getSnakeList().get(sbfList.get(whichClient).getSnakeID()-1).getDirection() == Direction.DOWN)
			{
				sbfList.get(whichClient).getSnakeList().get(sbfList.get(whichClient).getSnakeID()-1).setDirection(Direction.RIGHT);
			}
		}
		else if(nextMove == Protocol.SPEED_UP)
		{
			getSnakeMovement(whichClient).speedUp();
		}
		else if(nextMove == Protocol.SPEED_DOWN)
		{
			getSnakeMovement(whichClient).speedDown();
		}
	}

	//If snake is reversing its direction.
	public void revSnake(int whichClient)
	{
		sbfList.get(whichClient).getSnakeList().get(sbfList.get(whichClient).getSnakeID()-1).reverse();
	}

	public SnakeMovement getSnakeMovement(int index)
	{
		return smList.get(index);
	}

	public SnakeRenderer getSnakeRender(int index)
	{
		return srList.get(index);
	}

	public SnakeBoardFrame getSBF(int index)
	{
		return sbfList.get(index);
	}

	//Updates the player's score in the database.
	public void writeScore(int whichClient)
	{
		int score = sbfList.get(whichClient).getSnakeList().get(sbfList.get(whichClient).getSnakeID()-1).getSnakeScore();
		String user = usersInGame.get(whichClient);

		SnakeDatabaseManager.updateScore(statement, user, score);
	}

	//Returns the total score (current game score + past game scores) to the caller.
	public int reqForTotalScore(int whichClient, int whichSnake)
	{
		return (usersScore.get(whichSnake) + sbfList.get(whichClient).getSnakeList().get(whichSnake).getSnakeScore());
	}
}

