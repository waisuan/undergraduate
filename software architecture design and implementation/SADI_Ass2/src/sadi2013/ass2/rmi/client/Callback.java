/*
 * SOFTWARE ARCHITECTURE DEVELOPMENT & IMPLEMENTATION
 * ASSIGNMENT 2
 * FILE: CALLBACK.JAVA
 * DONE BY: SIA WAI SUAN (s3308555)
 */
package sadi2013.ass2.rmi.client;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/*
 * Interface class for the CALLBACK implementation.
 */
public interface Callback extends Remote{
	
	/*
	 * Details of the methods here will be explained in CallbackImp.java
	 */
	public void clientSelectPos(int client, ArrayList<Integer> posToDisable) throws RemoteException;
	
	public void startGame(int numOfP) throws RemoteException;
	
	public void updateSnakeBoard(String boardDetails) throws RemoteException;
	
	public void setClientID(int clientID) throws RemoteException;
	
	public void rejectPlayer() throws RemoteException;
}
