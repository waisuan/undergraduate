/*
 * NAME: SIA WAI SUAN
 * STUDENT ID: S3308555
 * MOBILE APPLICATION DEVELOPMENT
 * ASSIGNMENT 2
 * FILE: InterfaceEvent.java
 */
package mad.ass2.meetup.model;

import java.util.ArrayList;
import java.util.Date;

public interface InterfaceEvent extends Comparable<InterfaceEvent>
{
	public abstract String getEventTitle();

	public abstract String getEventVenue();

	public abstract String getEventNote();

	public abstract String getEventDate();

	public abstract Date getEventDateFormat();

	public abstract String getEventStartTime();

	public abstract String getEventEndTime();

	public abstract String getEventID();

	public abstract int getNumOfAttend();
	
	public abstract String getTaskID();

	public abstract ArrayList<String> getAttendees();

	public abstract void setEventTitle(String eTitle);

	public abstract void setEventVenue(String eVenue);

	public abstract void setEventNote(String eNote);

	public abstract void setEventDate(String eDate);

	public abstract void setEventDateFormat(Date eDateFormat);

	public abstract void setEventStartTime(String eStartTime);

	public abstract void setEventEndTime(String eEndTime);

	public abstract void setEventAttendees(ArrayList<String> attendees);

	public abstract void setEventID(String newID);
	
	public abstract void setTaskID(String taskID);
}