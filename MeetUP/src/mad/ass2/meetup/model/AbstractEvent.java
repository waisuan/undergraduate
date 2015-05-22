/*
 * NAME: SIA WAI SUAN
 * STUDENT ID: S3308555
 * MOBILE APPLICATION DEVELOPMENT
 * ASSIGNMENT 2
 * FILE: AbstractEvent.java
 */
package mad.ass2.meetup.model;

import java.util.ArrayList;
import java.util.Date;

public abstract class AbstractEvent implements InterfaceEvent{

	private Date eDateFormat;
	private String eTitle;
	private String eVenue;
	private String eNote;
	private String eDate;
	private String eStartTime;
	private String eEndTime;
	private String eID;
	private ArrayList<String> attendees = new ArrayList<String>();
	private String taskID;
	
	public AbstractEvent(String eTitle, String eVenue, String eNote, String eDate, Date eDateFormat, String eStartTime, String eEndTime, String eID, ArrayList<String> attendees)
	{
		this.eTitle = eTitle;
		this.eVenue = eVenue;
		this.eNote = eNote;
		this.eDate = eDate;
		this.eDateFormat = eDateFormat;
		this.eStartTime = eStartTime;
		this.eEndTime = eEndTime;
		this.eID = eID;
		this.attendees = attendees;
		this.taskID = "empty";
	}
	
	public String getEventTitle()
	{
		return eTitle;
	}
	
	public String getEventVenue()
	{
		return eVenue;
	}
	
	public String getEventNote()
	{
		return eNote;
	}
	
	public String getEventDate()
	{
		return eDate;
	}
	
	public Date getEventDateFormat()
	{
		return eDateFormat;
	}
	
	public String getEventStartTime()
	{
		return eStartTime;
	}
	
	public String getEventEndTime()
	{
		return eEndTime;
	}
	
	public String getEventID()
	{
		return eID;
	}
	
	public int getNumOfAttend()
	{
		return attendees.size();
	}
	
	public ArrayList<String> getAttendees()
	{
		return attendees;
	}
	
	public String getTaskID()
	{
		return taskID;
	}
	
	public void setEventTitle(String eTitle)
	{
		this.eTitle = eTitle;
	}
	
	public void setEventVenue(String eVenue)
	{
		this.eVenue = eVenue;
	}
	
	public void setEventNote(String eNote)
	{
		this.eNote = eNote;
	}
	
	public void setEventDate(String eDate)
	{
		this.eDate = eDate;
	}
	
	public void setEventDateFormat(Date eDateFormat)
	{
		this.eDateFormat = eDateFormat;
	}
	
	public void setEventStartTime(String eStartTime)
	{
		this.eStartTime = eStartTime;
	}
	
	public void setEventEndTime(String eEndTime)
	{
		this.eEndTime = eEndTime;
	}
	
	public void setEventAttendees(ArrayList<String> attendees)
	{
		this.attendees = attendees;
	}
	
	public void setEventID(String newID)
	{
		this.eID = newID;
	}
	
	public void setTaskID(String taskID)
	{
		this.taskID = taskID;
	}
}
