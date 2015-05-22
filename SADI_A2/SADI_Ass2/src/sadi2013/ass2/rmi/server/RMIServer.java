/*
 * SOFTWARE ARCHITECTURE DEVELOPMENT & IMPLEMENTATION
 * ASSIGNMENT 2
 * FILE: RMISERVER.JAVA
 * DONE BY: SIA WAI SUAN (s3308555)
 */
package sadi2013.ass2.rmi.server;

import java.util.ArrayList;

import sadi2013.ass2.rmi.client.Callback;

/*
 * Interface class for the RMISERVERIMP implementation.
 */
public interface RMIServer extends java.rmi.Remote{
	
	/*
	 * Details of the methods here will be explained in RMIServerImp.java
	 */
	public int wantToJoin(Callback c) throws java.rmi.RemoteException;
	
	public void setNumOfP(Callback c, int numOfP) throws java.rmi.RemoteException;
	
	public void setPos(int pos) throws java.rmi.RemoteException;
	
	public void move(int nextMove, int whichClient) throws java.rmi.RemoteException;
	
	public void writeScore(int whichClient) throws java.rmi.RemoteException;
	
	public int lookupDB(String username, String password) throws java.rmi.RemoteException;
	
	public boolean regInDB(String data) throws java.rmi.RemoteException;
	
	public void revSnake(int whichClient) throws java.rmi.RemoteException;
	
	public int reqForTotalScore(int whichClient, int whichSnake) throws java.rmi.RemoteException;
	
	public ArrayList<String> getUserDetails(String username) throws java.rmi.RemoteException;
}

