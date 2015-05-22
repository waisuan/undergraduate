/*
 * SOFTWARE ARCHITECTURE DEVELOPMENT & IMPLEMENTATION
 * ASSIGNMENT 2
 * FILE: CALLBACKIMP.JAVA
 * DONE BY: SIA WAI SUAN (s3308555)
 */
package sadi2013.ass2.rmi.client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/*
 * This class acts as an interface between the server and client.
 * Server class is able to remotely call the client's methods through this class.
 */
public class CallbackImp extends UnicastRemoteObject implements Callback{

	private static final long serialVersionUID = 3547433642608155945L;
	
	private RMIClient rmiClient;

	public CallbackImp(Object client) throws RemoteException {
		this.rmiClient = (RMIClient)client;
	}

	//Requests client to select a cell position.
	public void clientSelectPos(int client, ArrayList<Integer> posToDisable) throws RemoteException
	{
		this.rmiClient.selPos(client, posToDisable);
	}
	
	//Requests client to start game.
	public void startGame(int numOfP) throws RemoteException
	{
		this.rmiClient.beginGame(numOfP);
	}
	
	//Requests client to update the snake board.
	public void updateSnakeBoard(String boardDetails) throws RemoteException
	{
		this.rmiClient.updateBoard(boardDetails);
	}
	
	//Requests client to set a client ID for itself.
	public void setClientID(int clientID) throws RemoteException
	{
		this.rmiClient.setID(clientID);
	}
	
	//Reject player from joining the game.
	public void rejectPlayer() throws RemoteException
	{
		this.rmiClient.reject();
	}
}     
