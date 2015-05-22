/*
 * SOFTWARE ARCHITECTURE DEVELOPMENT & IMPLEMENTATION
 * ASSIGNMENT 2
 * FILE: SNAKE.JAVA
 * DONE BY: MASON CHONG SZE ZHEN (s3310388)
 */
package sadi2013.ass2.model;

import java.util.*;
import java.io.*;
/*Snake class used to hold the snake body, basic getter and setter methods, move and method for extending body.*/
@SuppressWarnings("serial")
public class Snake implements Serializable{

	private final int MAP_WIDTH = 50;
	private final int MAP_HEIGHT = 34;
	private final int INITIAL_LENGTH  = 3; /*Initial length of the snake.*/

	private ArrayList<Coordinate> body; /*ArrayList of coordinate of the snake.*/
	private int length;
	private int direction;/*Current direciton of the snake.*/
	private int snakeID;
	private int snakeScore = 0;
	private int totalScore = 0;

	private boolean reversing = false; 

	/*Server side snake object constuctor.*/
	public Snake(Coordinate startingCoor, int direction, int snakeID, int snakeScore)
	{
		this.snakeID = snakeID;
		this.body = new ArrayList<Coordinate>();
		this.length = INITIAL_LENGTH;
		this.direction = direction;
		startingCoor.y = (MAP_HEIGHT-1) - startingCoor.y;
		this.body.add(startingCoor);
		for(int i = 0; i < INITIAL_LENGTH-1 ;i++)
		{
			this.body.add(nextCoor(this.body.get(this.body.size()-1), this.direction));
		}

		this.totalScore = snakeScore;
	}
	/*Client side snake object constuctor.*/
	public Snake(int snakeID)
	{
		this.snakeID = snakeID;
		this.body = new ArrayList<Coordinate>();
		this.length = 0;
	}

	//Snake moving a step forward.
	public boolean move(ArrayList<Coordinate> foodCoor, ArrayList<Snake> snakeList)
	{	/*Check the reversing boolean and call swapTailToHead method to reverse the body list.*/
		if(reversing == true)
		{
			synchronized(this.body)
			{
				swapTailToHead();
			}
			reversing = false;
		}
		/*Checking for any other snake's body is blocking the way, current snake will lose if any snake body is blocking ahead.*/
		for(int j = 0;j<snakeList.size();j++)
		{
			for(int i =0;i<snakeList.get(j).getLength();i++)
			{
				if(nextCoor(body.get(this.getLength()-1), this.direction).equals(snakeList.get(j).getBody().get(i)) == true)
				{
					if(snakeList.get(j).getBody().get(i).equals(snakeList.get(j).getBody().get(snakeList.get(j).getLength()-1)) 
							&& this.getLength() >= snakeList.get(j).getLength()
							&& (this.getDirection()/snakeList.get(j).getDirection() == 2 || 
									snakeList.get(j).getDirection()/this.getDirection() == 2))
					{
						if(snakeList.get(j).getLength() == this.getLength())
						{
							snakeList.get(j).setLength(0);
							return false;
						}
						else
							return true;
					}
					else
					{
						return false;
					}
				}
			}
		}
		/*Checking for any food in front, consume it if there is any.*/
		for(int i = 0;i<foodCoor.size();i++)
		{
			if(nextCoor(body.get(length-1), this.direction).equals(foodCoor.get(i)) == true)
			{
				consumeFood(foodCoor, i);
				extendLength(foodCoor, snakeList);
				//Adds a point every time the snake eats a food. 
				this.snakeScore++;
				return true;
			}
		}		
		/*While the body length is no 0, the snake move a step forward according to it's direction.*/
		if(length !=0)
		{
			this.body.add(nextCoor(body.get(length-1),this.direction));
			this.body.remove(0);
			this.body.trimToSize();
		}
		//}
		return true;
	}
	/*Spawn another food after consuming one.*/
	public void spawnFood(ArrayList<Coordinate> foodCoor)
	{
		int widthMax = 49;
		int widthMin = 0;

		int heightMax = 33;
		int heightMin = 0;

		int currFoodListSize = 3;

		boolean foodCrash = false;
		Coordinate c;
		/*Synchronize the food coordinate so that it can add in a new food coordinate.*/
		synchronized(foodCoor)
		{
			do
			{
				foodCrash = false;
				Random r = new Random();
				int x = (r.nextInt(widthMax - widthMin) + widthMin);		
				int y = (r.nextInt(heightMax - heightMin) + heightMin);
				c = new Coordinate(x,y);
				for(int i = 0; i < this.body.size();i++)
				{
					if(this.body.get(i).equals(c) == true)
						foodCrash = true;
				}
			}
			while(foodCrash == true);

			if(foodCoor.size() == currFoodListSize)
			{
				foodCoor.add(c);
			}
		}
	}
	/*Consume a food in the food coordinates.*/
	public void consumeFood(ArrayList<Coordinate> foodCoor, int indexOfFood)
	{
		int maxFoodListSize = 4;

		spawnFood(foodCoor);

		synchronized(foodCoor)
		{
			if(foodCoor.size() == maxFoodListSize)
			{
				foodCoor.remove(indexOfFood);
				foodCoor.trimToSize();
			}
		}
	}

	//Snake extends it's length by 1 cell and move 1 step forward.
	public void extendLength(ArrayList<Coordinate> foodCoor, ArrayList<Snake> snakeList)
	{
		Coordinate extension = new Coordinate(this.body.get(0));

		this.move(foodCoor, snakeList);

		this.body.add(new Coordinate());

		for(int i = (this.length-1); i>=0 ;i--)
		{
			this.body.get(i+1).equalTo(this.body.get(i));
		}

		this.body.get(0).equalTo(extension);

		this.length++;
	}

	//Get the next coordinate according it's direction and head's coordinate.
	public Coordinate nextCoor(Coordinate c, int d)
	{
		Coordinate next = new Coordinate();

		if(d == Direction.LEFT)
		{
			next.y = c.y;
			if(c.x-1 < 0)
				next.x = MAP_WIDTH-1;
			else
				next.x = c.x-1;
		}

		else if(d == Direction.RIGHT)
		{
			next.y = c.y;
			if(c.x+1 > MAP_WIDTH-1)
				next.x = 0;
			else
				next.x = c.x+1;
		}

		else if(d == Direction.UP)
		{
			next.x = c.x;
			if(c.y-1 < 0)
				next.y = MAP_HEIGHT-1;
			else
				next.y = c.y-1;
		}

		else if(d == Direction.DOWN)
		{
			next.x = c.x;
			if(c.y+1 > MAP_HEIGHT-1)
				next.y = 0;
			else
				next.y = c.y+1;
		}

		return next;
	}

	public ArrayList<Coordinate> getBody()
	{
		return body;
	}

	public int getLength()
	{
		return length;
	}

	public int getDirection()
	{
		return direction;
	}

	public boolean setDirection(int direction)
	{
		if(nextCoor(this.body.get(this.body.size()-1), direction).equals(this.body.get(this.body.size()-2)))
		{
			return false;
		}
		else
		{
			this.direction = direction;
			return true;
		}
	}

	public void setLength(int i)
	{
		length = i;
	}

	public int getCurrSnakeID()
	{
		return snakeID;
	}

	public void setSnakeScore(int snakeScore)
	{
		this.snakeScore = snakeScore;
	}

	public int getSnakeScore()
	{
		return this.snakeScore;
	}

	public void setTotalScore()
	{
		this.totalScore += this.snakeScore;
	}

	public void setTotalScore(int tScore)
	{
		this.totalScore = tScore;
	}

	public int getTotalScore()
	{
		return this.totalScore;
	}

	public void reverse()
	{
		this.reversing = true;
	}
	//Swaps the tail to head and check the correct direction.
	//For reversing.
	public void swapTailToHead()
	{
		Coordinate lastCoor = this.body.get(0);
		Coordinate lastSecondCoor = this.body.get(1);

		if(nextCoor(lastCoor, Direction.UP).equals(lastSecondCoor))
		{
			direction = Direction.DOWN;
		}
		else if(nextCoor(lastCoor, Direction.RIGHT).equals(lastSecondCoor))
		{
			direction = Direction.LEFT;
		}
		else if(nextCoor(lastCoor, Direction.LEFT).equals(lastSecondCoor))
		{
			direction = Direction.RIGHT;
		}
		else if(nextCoor(lastCoor, Direction.DOWN).equals(lastSecondCoor))
		{
			direction = Direction.UP;
		}				

		Collections.reverse(this.body);
	}
}
