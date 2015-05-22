/*
 * NAME: SIA WAI SUAN
 * STUDENT ID: S3308555
 * MOBILE APPLICATION DEVELOPMENT
 * ASSIGNMENT 2
 * FILE: BasicEvent.java
 */
package mad.ass2.meetup.model;

import java.util.ArrayList;
import java.util.Date;

//Comparable interface is implemented in order to allow the DATES of the event to be sorted.
public class BasicEvent extends AbstractEvent implements Comparable<InterfaceEvent>{

	public BasicEvent(String eTitle, String eVenue, String eNote, String eDate,
			Date eDateFormat, String eStartTime, String eEndTime, String eID,
			ArrayList<String> attendees) {
		super(eTitle, eVenue, eNote, eDate, eDateFormat, eStartTime, eEndTime, eID,
				attendees);
	}

	@Override
	public int compareTo(InterfaceEvent e) 
	{
		return getEventDateFormat().compareTo(e.getEventDateFormat());
	}
}
