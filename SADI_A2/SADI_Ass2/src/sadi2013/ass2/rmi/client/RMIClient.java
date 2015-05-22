/*
 * SOFTWARE ARCHITECTURE DEVELOPMENT & IMPLEMENTATION
 * ASSIGNMENT 2
 * FILE: RMICLIENT.JAVA
 * DONE BY: SIA WAI SUAN (s3308555)
 */
package sadi2013.ass2.rmi.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.UnknownHostException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.StringTokenizer;
import javax.swing.JOptionPane;
import sadi2013.ass2.loginview.HostNameFrame;
import sadi2013.ass2.loginview.LoginFrame;
import sadi2013.ass2.loginview.RegisterFrame;
import sadi2013.ass2.model.Coordinate;
import sadi2013.ass2.model.Snake;
import sadi2013.ass2.rmi.server.RMIServer;
import sadi2013.ass2.utilities.Protocol;
import sadi2013.ass2.view.ClientFrameView;
import sadi2013.ass2.view.SnakeBoardFrame;

/*
 * Client class.
 */
public class RMIClient extends UnicastRemoteObject{

	private static final long serialVersionUID = -6050289951517544719L;

	private RMIServer rmiServer;

	private static ClientFrameView cfv;

	private int reply = 0;

	private CallbackImp CallBackControl;

	private int twoP = 2;
	private int threeP = 3;
	private int fourP = 4;

	private int clientID;

	private SnakeBoardFrame sbf;

	private boolean paintFin = true;

	private boolean snakeDead = false;
	private boolean gameOver = false;
	public static boolean endOfGame = false;

	private LoginFrame loginFrame;
	private RegisterFrame regFrame;
	private HostNameFrame hostFrame;

	private String username;

	public static void main(String argv[]) {
		try {
			@SuppressWarnings("unused")
			RMIClient rmiClient = new RMIClient();
		} catch (Exception e ) {
			e.printStackTrace();
		}
	}

	//Sets the server host name as entered by the client.
	//Will validate the host name beforehand.
	public boolean setSvrHost(String hostName)
	{
		try {
			rmiServer = (RMIServer)Naming.lookup("//" + hostName + "/RMIServer");
		} catch (UnknownHostException uhe) {
			JOptionPane.showMessageDialog(this.hostFrame, "Invalid Host Name!", "HostName Error", JOptionPane.ERROR_MESSAGE);	
			return false;
		} catch (MalformedURLException e) {
			JOptionPane.showMessageDialog(this.hostFrame, "Invalid Host Name!", "HostName Error", JOptionPane.ERROR_MESSAGE);
			return false;
		} catch (RemoteException e) {
			JOptionPane.showMessageDialog(this.hostFrame, "Invalid Host Name!", "HostName Error", JOptionPane.ERROR_MESSAGE);
			return false;
		} catch (NotBoundException e) {
			JOptionPane.showMessageDialog(this.hostFrame, "Invalid Host Name!", "HostName Error", JOptionPane.ERROR_MESSAGE);
			return false;
		} 

		return true;
	}

	//Prompts registration form.
	public void callRegFrame()
	{
		regFrame = new RegisterFrame(this);
	}

	//Sends user details to the server for login purposes.
	//If valid, log the user into the game.
	public void clientCredentials(String username, String password)
	{
		loginFrame.setVisible(false);

		try {
			if(rmiServer.lookupDB(username, password) == Protocol.LOGIN_SUCCESS)
			{
				JOptionPane.showMessageDialog(this.loginFrame, "You have logged in successfully!", "Login Information", JOptionPane.INFORMATION_MESSAGE);
				this.username = username;
				initClient();
			}
			else if(rmiServer.lookupDB(username, password) == Protocol.ALREADY_LOGIN)
			{
				JOptionPane.showMessageDialog(this.loginFrame, "User is already LOGGED IN!", "Login Error", JOptionPane.ERROR_MESSAGE);
				loginFrame.setVisible(true);
			}
			else
			{
				JOptionPane.showMessageDialog(this.loginFrame, "Username and/or password input is incorrect!", "Login Error", JOptionPane.ERROR_MESSAGE);
				loginFrame.setVisible(true);
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	//Register user in the database (if valid) and automatically login to the game.
	public void newClient(String data, String username, String password)
	{
		try {
			if(rmiServer.regInDB(data))
			{
				JOptionPane.showMessageDialog(regFrame, "You have registered successfully!", "Registration Information", JOptionPane.INFORMATION_MESSAGE);
				loginFrame.setVisible(false);
				regFrame.setVisible(false);
				clientCredentials(username, password);
			}
			else
			{
				JOptionPane.showMessageDialog(regFrame, "Registration FAILED. Please try again!", "Registration Error", JOptionPane.ERROR_MESSAGE);
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	//Client-side GUI.
	public void initClient() 
	{
		cfv = new ClientFrameView();

		/*
		 * Players are able to CONNECT to the server by clicking on the provided GUI button.
		 */
		cfv.getConnectButton().setEnabled(true);
		cfv.getConnectButton().addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e)
			{
				try {

					clientClickConnect();

				} catch (RemoteException e1) {
					e1.printStackTrace();
				}

				cfv.getConnectButton().setEnabled(false);
			}
		});

		cfv.callPanelTextArea("Client is started...");

		showUserDetails();
	}

	//Shows user details in the client-side GUI frame.
	public void showUserDetails()
	{
		ArrayList<String> userDetails = new ArrayList<String>();

		try {
			userDetails = rmiServer.getUserDetails(username);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		cfv.callPanelTextArea("FIRST NAME: " + userDetails.get(0));
		cfv.callPanelTextArea("SUR NAME: " + userDetails.get(1));
		cfv.callPanelTextArea("ADDRESS: " + userDetails.get(2));
		cfv.callPanelTextArea("PHONE NUMBER: " + userDetails.get(3));
		cfv.callPanelTextArea("SCORE: " + userDetails.get(4));
	}

	//Prompt the user to enter host name address.
	public void startHostFrame()
	{
		hostFrame = new HostNameFrame(this);
	}

	//Prompt the user to enter login details.
	public void startLoginFrame()
	{
		loginFrame = new LoginFrame(this);
	}

	//Constructor.
	public RMIClient() throws RemoteException {
		try {
			//Creates a CallBack to allow user to control the client.
			CallBackControl = new CallbackImp(this);

			startHostFrame();
		} 
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	//Client chooses to connect to the game.
	//If successful, wait for other players.
	//Else, rejected from the game.
	public void clientClickConnect() throws RemoteException
	{
		reply = rmiServer.wantToJoin(CallBackControl);

		if(reply != Protocol.REJECT)
		{
			if(reply == Protocol.WAIT)
			{
				cfv.callPanelTextArea("Waiting for PLAYER 1 to choose game capacity...");
			}
			else
			{
				clientID = reply;

				cfv.callPanelTextArea("Player " + (clientID + 1) + " has connected to the game...");

				if(clientID == 0)
				{
					chooseNumOfP();
				}
				else
				{
					waitForOthers();
				}
			}
		}
		else
		{
			cfv.callPanelTextArea("Sorry, the game is full. Please try again later...");
		}
	}

	//Sets an ID for the current ID.
	public void setID(int clientID) throws RemoteException
	{
		this.clientID = clientID;
	}

	//Rejects player.
	public void reject() throws RemoteException
	{
		cfv.callPanelTextArea("Sorry, the game is full. Please try again later...");
	}

	//First player chooses the number of players allowed in the game.
	public void chooseNumOfP()
	{
		enablePlayerButtons();

		cfv.getButton2p().addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e)
			{
				try {
					rmiServer.setNumOfP(CallBackControl, twoP);
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}

				disablePlayerButtons();
			}
		});

		cfv.getButton3p().addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e)
			{
				try {
					rmiServer.setNumOfP(CallBackControl, threeP);
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}

				disablePlayerButtons();
			}
		}); 

		cfv.getButton4p().addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e)
			{
				try {
					rmiServer.setNumOfP(CallBackControl, fourP);
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}

				disablePlayerButtons();
			}
		});
	}

	//Client is able to select a starting cell position.
	public void selPos(int client, ArrayList<Integer> posToDisable)
	{
		enableSelectCellButtons();

		if(client != 0)
		{
			for(int i = 0; i < posToDisable.size(); i++)
			{
				disableSelectCellButton(posToDisable.get(i));
			}
		}

		cfv.getTopLeftButton().addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e)
			{
				try {
					rmiServer.setPos(Protocol.TOP_LEFT);
				} catch (RemoteException e1) {

					e1.printStackTrace();
				}

				disableSelectCellButtons();
			}
		}); 

		cfv.getTopRightButton().addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e)
			{
				try {
					rmiServer.setPos(Protocol.TOP_RIGHT);
				} catch (RemoteException e1) {

					e1.printStackTrace();
				}

				disableSelectCellButtons();
			}
		}); 

		cfv.getBotLeftButton().addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e)
			{
				try {
					rmiServer.setPos(Protocol.BOT_LEFT);
				} catch (RemoteException e1) {

					e1.printStackTrace();
				}

				disableSelectCellButtons();
			}
		}); 

		cfv.getBotRightButton().addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e)
			{
				try {
					rmiServer.setPos(Protocol.BOT_RIGHT);
				} catch (RemoteException e1) {

					e1.printStackTrace();
				}

				disableSelectCellButtons();
			}
		}); 
	}

	//Wait for other players (Until capacity is filled).
	public void waitForOthers()
	{
		cfv.callPanelTextArea("Waiting for other players...");
	}

	//Start game and brings up the GUI frame.
	public void beginGame(int numOfP)
	{
		cfv.callPanelTextArea("Game is started...");

		/*
		 * Keep reading messages from server and update game board state accordingly.
		 */
		sbf = new SnakeBoardFrame(this, numOfP);
	}

	//Server is able to constantly send game play details to the client through this method.
	@SuppressWarnings("static-access")
	public void updateBoard(String svrBoardDetails) throws RemoteException
	{
		if(endOfGame == false)
		{
			String boardDetails = svrBoardDetails;
			int snakeDeadCount = 0;
			snakeDeadCount = 0;

			StringTokenizer snakeFoodToken = new StringTokenizer(boardDetails, "/");
			String snakeDetails = snakeFoodToken.nextToken();
			String foodDetails = snakeFoodToken.nextToken();

			StringTokenizer snakeToken = new StringTokenizer(snakeDetails, ";");
			int snakeListSize = Integer.parseInt(snakeToken.nextToken());

			//Recreate the snake(s) on the board with updated game details.
			for(int i = 0; i < snakeListSize; i++)
			{
				StringTokenizer snakes = new StringTokenizer(snakeToken.nextToken(), ",");
				int snakeID = Integer.parseInt(snakes.nextToken());

				//Construction of snake object.
				Snake tempSnake = new Snake(snakeID);
				tempSnake.setLength(0);

				tempSnake.setSnakeScore(Integer.parseInt(snakes.nextToken()));

				while(snakes.hasMoreTokens())
				{
					int x = Integer.parseInt(snakes.nextToken());
					int y = Integer.parseInt(snakes.nextToken());
					Coordinate c  = new Coordinate(x,y);
					tempSnake.getBody().add(c);
					tempSnake.setLength(tempSnake.getLength()+1);
				}

				sbf.getSnakeList().set(i, tempSnake);

				if(clientID == snakeID && snakeDead == false)
				{
					//If player loses the game.
					if(tempSnake.getLength() == 0)
					{
						snakeDead = true;
						snakeDeadCount++;
						JOptionPane.showMessageDialog(sbf.getMainCenterPanel(), "Player " + (clientID+1) +": Game Over !!");
						cfv.callPanelTextArea("Player " + (clientID+1) +" has LOST...");
						informServer();
						break;
					}
				}
				else
				{
					//Another player has lost the game.
					if(tempSnake.getLength() == 0)
					{
						snakeDeadCount++;
					}
				}
			}

			//If player is the winner.
			if(snakeDeadCount == snakeListSize-1 && sbf.getSnakeList().get(clientID).getLength() != 0 && gameOver == false)
			{
				gameOver = true;
				JOptionPane.showMessageDialog(sbf.getMainCenterPanel(), "Player " + (clientID+1) +" has WON!");
				cfv.callPanelTextArea("Player " + (clientID+1) + " has WON...");
				informServer();
			}

			//If the entire game is over and a victor is decided.
			//A pop-up would appear at the end of the game to show the current game ranking of the involved players.
			//The ranking is calculated based on the total sum of the current game score and the past game scores of the player(s).
			//E.g. Even if a player wins THIS game, his/her ranking might not be at number 1.
			//Score is calculated based on how many FOOD the snake eats in the game.
			if((snakeDeadCount == snakeListSize - 1 || snakeDeadCount == snakeListSize) && (snakeDead == true || gameOver == true) && endOfGame == false)
			{
				endOfGame = true;

				for(int i = 0; i < snakeListSize; i++)
				{
					int tScore = rmiServer.reqForTotalScore(clientID, i);
					sbf.getSnakeList().get(i).setTotalScore(tScore);
				}

				String rank = "";

				Collections.sort(sbf.getSnakeList(), new Comparator<Snake>() {

					public int compare(Snake s1, Snake s2) {

						return s1.getTotalScore() - s2.getTotalScore();
					}
				});

				int terminator = sbf.getSnakeList().size() - snakeListSize;
				for(int i = (sbf.getSnakeList().size() - 1); i >= terminator ; i--)
				{
					rank += "Player " + (sbf.getSnakeList().get(i).getCurrSnakeID() + 1) + ": " + String.valueOf(sbf.getSnakeList().get(i).getTotalScore());
					rank += "\n";
				}

				JOptionPane.showMessageDialog(sbf.getMainCenterPanel(), "TOTAL GAME RANKING:\n" + rank);
				sbf.getMainCenterPanel().setVisible(false);
				sbf.setVisible(false);
			}
			else
			{
				//Food coordinates.
				StringTokenizer foodToken = new StringTokenizer(foodDetails, ",");
				for(int i=0;foodToken.hasMoreTokens();i++)
				{
					int x = Integer.parseInt(foodToken.nextToken());
					int y = Integer.parseInt(foodToken.nextToken());
					Coordinate c  = new Coordinate(x,y);
					sbf.getMainCenterPanel().foodCoor.set(i, c);
				}

				//Don't repaint until the previous paint job is finish.
				if(paintFin == true)
				{
					sbf.getMainCenterPanel().resetRectangleArray();
					sbf.getMainCenterPanel().repaint();
					sbf.getEastPanel().resetNumOfPlayers(snakeListSize);

					paintFin = false;
				}
			}
		}
	}

	//Player has lost/won -- Inform the server to write the respective score to the database.
	public void informServer()
	{
		try {
			rmiServer.writeScore(clientID);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	//When the player increases/decreases its snake speed.
	public void getSnakeSpeed(int speed)
	{
		try {
			rmiServer.move(speed, clientID);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	//When the player changes its snake movement.
	public void getSnakeMove(int movement)
	{
		try {
			rmiServer.move(movement, clientID);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	//When the player reverses its snake's direction.
	public void getSnakeRev()
	{
		try {
			rmiServer.revSnake(clientID);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	//Current paint job is done.
	public void paintFinish()
	{
		paintFin = true;
	}

	public void enablePlayerButtons()
	{
		cfv.getButton2p().setEnabled(true);
		cfv.getButton3p().setEnabled(true);
		cfv.getButton4p().setEnabled(true);
	}

	public void disablePlayerButtons()
	{
		cfv.getButton2p().setEnabled(false);
		cfv.getButton3p().setEnabled(false);
		cfv.getButton4p().setEnabled(false);
	}

	public void enableSelectCellButtons()
	{
		cfv.getTopLeftButton().setEnabled(true);
		cfv.getTopRightButton().setEnabled(true);
		cfv.getBotLeftButton().setEnabled(true);
		cfv.getBotRightButton().setEnabled(true);
	}

	public void disableSelectCellButtons()
	{
		cfv.getTopLeftButton().setEnabled(false);
		cfv.getTopRightButton().setEnabled(false);
		cfv.getBotLeftButton().setEnabled(false);
		cfv.getBotRightButton().setEnabled(false);
	}

	public void disableSelectCellButton(int posToDisable)
	{
		if(posToDisable == Protocol.TOP_LEFT)
		{
			cfv.getTopLeftButton().setEnabled(false);
		}
		else if(posToDisable == Protocol.TOP_RIGHT)
		{
			cfv.getTopRightButton().setEnabled(false);
		}
		else if(posToDisable == Protocol.BOT_LEFT)
		{
			cfv.getBotLeftButton().setEnabled(false);
		}
		else if(posToDisable == Protocol.BOT_RIGHT)
		{
			cfv.getBotRightButton().setEnabled(false);
		}
	}

}
