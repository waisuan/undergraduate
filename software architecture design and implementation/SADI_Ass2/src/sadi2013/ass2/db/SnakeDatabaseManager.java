/*
 * SOFTWARE ARCHITECTURE DEVELOPMENT & IMPLEMENTATION
 * ASSIGNMENT 2
 * FILE: SNAKEDATABASEMANAGER.JAVA
 * DONE BY: MASON CHONG SZE ZHEN (s3310388)
 */
package sadi2013.ass2.db;
import java.sql.*;
import java.util.*;

public class SnakeDatabaseManager 
{
	private static final String URL = "jdbc:oracle:thin:@emu.cs.rmit.edu.au:1521:GENERAL";
	private static final String USERNAME = "s3310388";
	private static final String PASSWORD = "Danny12345";
	/*Update score in the database according to the username and sum up with his current score.*/
	public static void updateScore(Statement statement, String username, int score) 
	{
		String query = "Select username, score from player";
		ResultSet rs;
		try 
		{
			rs = statement.executeQuery(query);
			while(rs.next())
			{
				String usernameindatabase = rs.getString(1).replaceAll(" ", "");
				int scoreindatabase = rs.getInt(2);

				if(usernameindatabase.compareTo(username) == 0)
				{
					int updateScore = scoreindatabase + score;
					statement.executeUpdate("update player set score="+ updateScore+" where username ='"+ username +"'");
					break;
				}

			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}
	/*Get the user's current score in the database.*/
	public static int getScore(Statement statement, String username)
	{
		String query = "Select username, score from player";
		ResultSet rs;
		try 
		{
			rs = statement.executeQuery(query);
			while(rs.next())
			{
				String usernameindatabase = rs.getString(1).replaceAll(" ", "");
				int scoreindatabase = rs.getInt(2);

				if(usernameindatabase.compareTo(username) == 0)
				{
					return scoreindatabase;
				}

			}
		} 
		catch (SQLException e) 
		{
			return -1;
		}
		return -1;
	}
	/*Get user details from the database*/
	public static ArrayList<String> getUserDetails(Statement statement, String username)
	{
		String query = "Select username, first_name, sur_name, address, phonenumber, score from player";
		ResultSet rs;
		ArrayList<String> tempList = new ArrayList<String>();
		try 
		{
			rs = statement.executeQuery(query);

			while(rs.next())
			{
				String usernameindatabase = rs.getString(1).replaceAll(" ", "");

				if(usernameindatabase.compareTo(username) == 0)
				{
					tempList.add(rs.getString(2));
					tempList.add(rs.getString(3));
					tempList.add(rs.getString(4));
					tempList.add(String.valueOf(rs.getInt(5)));
					tempList.add(String.valueOf(rs.getInt(6)));
				}

			}
		} 
		catch (SQLException e) 
		{
			return null;
		}

		return tempList;
	}
	/*Insert a new user to the database, return false if duplicate username detected*/
	public static boolean insertPlayer(Statement statement, String data)
	{
		String query = "insert into player values("+ data + ")";
		try 
		{
			statement.executeUpdate(query);
		} 
		catch (SQLException e) 
		{
			return false;
		}
		return true;
	}
	/*Create a new table in the database.*/
	public static void createTable(Statement statement, String table_name)
	{
		String query = "create table "+ table_name +"(username char(10) primary key, " + "password char(10), " + 
		"first_name char(10), " + "sur_name char(10), " + "address char(100), " + "phonenumber number, " + "score number)";
		try
		{
			statement.executeUpdate(query);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}
	/*Drop the table according to the table name passed in in the database.*/
	public static void dropTable(Statement statement, String table_name)
	{
		String query = "drop table " + table_name;
		try 
		{
			statement.executeUpdate(query);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}
	/*Log the user into the game by checking the username and password in the database, returns false if login failed.*/
	public static boolean login(Statement statement, String username, String password)
	{
		String query = "SELECT username, password from player";
		boolean login = false;
		ResultSet rs;
		try 
		{
			rs = statement.executeQuery(query);

			while (rs.next())
			{
				String usernameindatabase = rs.getString(1).replaceAll(" ", "");
				String passwordindatabase = rs.getString(2).replaceAll(" ", "");
				if(usernameindatabase.compareTo(username) == 0 && passwordindatabase.compareTo(password)==0)
				{
					login = true;
					break;
				}
			}

		}
		catch (SQLException e) 
		{

			e.printStackTrace();
		}
		if(login == false)
		{
			return false;
		}
		else
		{
			return true;
		}

	}

	public static Statement initialiseDatabase(Connection connection)
	{
		Statement statement = null;
		try 
		{
			statement = connection.createStatement();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return statement;
	}

	public static Connection initialiseConnection()
	{
		Connection connection = null;
		try 
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
		} 
		catch (SQLException e) 
		{

			e.printStackTrace();
		} 
		catch (ClassNotFoundException e) 
		{

			e.printStackTrace();
		}
		return connection;
	}

	public static void closeConnection(Connection connection)
	{
		try 
		{
			connection.close();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}
}