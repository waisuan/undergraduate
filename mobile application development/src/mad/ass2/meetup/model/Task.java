/*
 * NAME: SIA WAI SUAN
 * STUDENT ID: S3308555
 * MOBILE APPLICATION DEVELOPMENT
 * ASSIGNMENT 2
 * FILE: Task.java
 */
package mad.ass2.meetup.model;

import java.util.UUID;


/*
 * A typical MODEL class holding the TASK object attributes that are to be added to the QUEUE and sent to the Google Tasks
 * API.
 */
public class Task {
	
	private String reqType;
	private InterfaceEvent taskEvent;
	private String taskObjID;
	
	public Task(String reqType, InterfaceEvent event)
	{
		this.reqType = reqType;

		//Constructing a new (duplicate) event in the Task class prevents it from holding a COPY of the event.
		taskEvent = new BasicEvent(event.getEventTitle(), event.getEventVenue(), event.getEventNote(), 
				event.getEventDate(), event.getEventDateFormat(), event.getEventStartTime(), event.getEventEndTime(), 
				event.getEventID(), event.getAttendees());
		taskEvent.setTaskID(event.getTaskID());
		
		//Note that this ID is different from the taskID gotten from the INSERTION HttpRequest.
		//This ID is meant to differentiate TASK OBJECTS constructed within this app.
		this.taskObjID = UUID.randomUUID().toString();
	}
	
	public String getReqType()
	{
		return reqType;
	}
	
	public InterfaceEvent getEvent()
	{
		return taskEvent;
	}
	
	public String getTaskObjID()
	{
		return taskObjID;
	}
	
	public void setReqType(String reqType)
	{
		this.reqType = reqType;
	}
	
	public void setTaskObjID(String taskObjID)
	{
		this.taskObjID = taskObjID;
	}
}
