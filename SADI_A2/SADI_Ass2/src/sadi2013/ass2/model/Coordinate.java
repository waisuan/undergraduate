/*
 * SOFTWARE ARCHITECTURE DEVELOPMENT & IMPLEMENTATION
 * ASSIGNMENT 2
 * FILE: COORDINATE.JAVA
 * DONE BY: MASON CHONG SZE ZHEN (s3310388)
 */
package sadi2013.ass2.model;

import java.io.Serializable;
/*
 * Coordinate class used to stored the x and y axis.
 * */
@SuppressWarnings("serial")
public class Coordinate implements Serializable
{
	public int x;
	public int y;

	public Coordinate(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	public Coordinate(Coordinate c)
	{
		this.x = c.x;
		this.y = c.y;
	}
	
	public Coordinate()
	{
	}
	
	public void equalTo(Coordinate c)
	{
		this.x = c.x;
		this.y = c.y;		
	}
	
	public boolean equals(Coordinate c)
	{
		if((this.x == c.x) && (this.y == c.y))
			return true;
		else
			return false;
	}
}
