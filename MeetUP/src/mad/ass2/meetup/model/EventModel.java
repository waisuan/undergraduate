/*
 * NAME: SIA WAI SUAN
 * STUDENT ID: S3308555
 * MOBILE APPLICATION DEVELOPMENT
 * ASSIGNMENT 2
 * FILE: EventModel.java
 */
package mad.ass2.meetup.model;

/*
 * Model class for holding the necessary data / data structures.
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.UUID;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.json.JSONException;

import mad.ass2.meetup.db.EventDBModel;
import mad.ass2.meetup.google.TaskExplorerHelper;
import mad.ass2.meetup.main.MainActivity;

import android.graphics.Color;
import android.util.Log;
import android.widget.TableRow;
import android.widget.TextView;

public class EventModel {

	private ArrayList<String> eAttendees = new ArrayList<String>();
	private String eDateTime;
	private Date eDateFormat = null;
	private StringTokenizer st;

	//Main data structure for this application.
	//Holds all the events.
	private ArrayList<InterfaceEvent> eventList = new ArrayList<InterfaceEvent>();

	//A singleton class with a private (empty) constructor.
	private static EventModel singletonInstance;

	private EventModel()
	{
	}

	//Processes the new event data and then proceeds to add it to the array list.
	public void setEvent(ArrayList<String> eventDetails)
	{	
		eDateTime = eventDetails.get(3) + " " + eventDetails.get(4);

		try 
		{
			eDateFormat = new SimpleDateFormat("MMMM d, yyyy hh:mm a", Locale.ENGLISH).parse(eDateTime);
		} 
		catch (ParseException e) {
			e.printStackTrace();
		}

		eAttendees = new ArrayList<String>();

		st = new StringTokenizer(eventDetails.get(6), ", ");

		while(st.hasMoreTokens()) { 
			eAttendees.add((String) st.nextToken()); 
		}

		InterfaceEvent newEvent = new BasicEvent(eventDetails.get(0), eventDetails.get(1), eventDetails.get(2), eventDetails.get(3), eDateFormat, eventDetails.get(4), eventDetails.get(5), UUID.randomUUID().toString(), eAttendees);

		eventList.add(newEvent);
		Collections.sort(eventList);

		addEventToDB(newEvent);

		addEventToGoogle(newEvent);
	}

	//Handles edited events.
	public void updateEvent(ArrayList<String> eventDetails)
	{
		for(int i = 0; i < getListSize(); i++)
		{
			if(eventList.get(i).getEventID().equals(eventDetails.get(7)))
			{
				eventList.get(i).setEventTitle(eventDetails.get(0));
				eventList.get(i).setEventVenue(eventDetails.get(1));
				eventList.get(i).setEventNote(eventDetails.get(2));

				eDateTime = eventDetails.get(3) + " " + eventDetails.get(4);

				try 
				{
					eDateFormat = new SimpleDateFormat("MMMM d, yyyy hh:mm a", Locale.ENGLISH).parse(eDateTime);
				} 
				catch (ParseException e) {
					e.printStackTrace();
				}

				eventList.get(i).setEventDate(eventDetails.get(3));
				eventList.get(i).setEventDateFormat(eDateFormat);
				eventList.get(i).setEventStartTime(eventDetails.get(4));
				eventList.get(i).setEventEndTime(eventDetails.get(5));

				eAttendees = new ArrayList<String>();

				StringTokenizer st = new StringTokenizer(eventDetails.get(6), ", ");

				while(st.hasMoreTokens()) { 
					eAttendees.add((String) st.nextToken()); 
				}

				eventList.get(i).setEventAttendees(eAttendees);

				updateEventInDB(eventList.get(i));

				updateEventInGoogle(eventList.get(i));

				break;
			}
		}
		//Sort the events by DATE.
		Collections.sort(eventList);
	}

	//Other class may call the items from this class whilst using this getter method.
	public static EventModel getSingletonInstance()
	{
		if (singletonInstance == null)
			singletonInstance = new EventModel();

		return singletonInstance;
	}

	//Insert TASK.
	public void addEventToGoogle(InterfaceEvent event)
	{	
		Log.i("ADD_EVENT_TO_GOOGLE", event.getTaskID());
		TaskExplorerHelper.getSingletonInstance().insertQueue(event);
	}

	//Insert EVENT into DB.
	public void addEventToDB(InterfaceEvent event)
	{	
		Log.i("ADD_EVENT_TO_DB", event.getEventTitle());
		//Insert into database before continuing.
		EventDBModel.getDBInstance(null).createEvent(event);
	}

	//Update EVENT_LIST and DATABASE with the TASK ID.
	public void updateDataModels(InterfaceEvent event)
	{
		Log.i("UPDATE_DATA_MODELS", event.getTaskID());

		InterfaceEvent eventInList = getEventforId(event.getEventID());
		eventInList.setTaskID(event.getTaskID());

		EventDBModel.getDBInstance(null).updateEvent(eventInList);
	}

	//Update a specific ROW in the DB.
	public void updateEventInDB(InterfaceEvent event)
	{
		Log.i("UPDATE_EVENT_TO_DB", event.getEventTitle());

		EventDBModel.getDBInstance(null).updateEvent(event);
	}

	//Update TASK.
	public void updateEventInGoogle(InterfaceEvent event)
	{
		Log.i("UPDATE_EVENT_TO_GOOGLE", event.getEventTitle());

		TaskExplorerHelper.getSingletonInstance().updateQueue(event);
	}

	//Removes event from the array list by EVENT object.
	public void deleteEvent(InterfaceEvent event)
	{
		Log.i("DEL_EVENT", event.getEventTitle());

		eventList.remove(event);
		deleteEventFromDB(event);
		deleteEventFromGoogle(event);
	}

	//Removes event from array list by EVENT position.
	public String deleteEvent(int position)
	{
		Log.i("DEL_EVENT", "hehe");

		String deletedEvent = getEventList().get(position).getEventTitle();
		deleteEventFromDB(getEventList().get(position));
		deleteEventFromGoogle(getEventList().get(position));

		eventList.remove(position);

		return deletedEvent;
	}

	//Delete a specific EVENT from the DB.
	public void deleteEventFromDB(InterfaceEvent event)
	{
		Log.i("DEL_EVENT_FROM_DB", event.getEventTitle());

		EventDBModel.getDBInstance(null).deleteEvent(event);
	}

	//Delete TASK.
	public void deleteEventFromGoogle(InterfaceEvent event)
	{
		Log.i("DEL_EVENT_FROM_GOOGLE", event.getEventTitle());

		TaskExplorerHelper.getSingletonInstance().deleteQueue(event);
	}

	public void deleteAllFromList()
	{
		getEventList().clear();
	}

	public void deleteAllFromDB()
	{
		EventDBModel.getDBInstance(null).deleteAllEvents();
	}

	//Called when USER selects the OVERRIDE DB button which GRABS all the TASKS from the user's Google account
	//and overrides the local data with it.
	public void initOverride()
	{
		TaskExplorerHelper.getSingletonInstance().doGetTasks();
	}

	//Called when USER selects the OVERRIDE DB button which GRABS all the TASKS from the user's Google account
	//and overrides the local data with it.
	public void override(ArrayList<String> tasks)
	{
		deleteAllFromList();
		deleteAllFromDB();
		TaskExplorerHelper.getSingletonInstance().deleteAllFromQ();
		TaskExplorerHelper.getSingletonInstance().deleteAllFromQDb();
		
		for(int i = 0; i < tasks.size(); i++)
		{
			ArrayList<String> tempList = new ArrayList<String>();
			StringTokenizer st = new StringTokenizer(tasks.get(i), ";");
			while(st.hasMoreTokens())
			{
				tempList.add(st.nextToken());
			}

			String eventTaskID = tempList.get(0);
			String eventTitle = tempList.get(1);

			ArrayList<String> tempNotes = new ArrayList<String>();
			st = new StringTokenizer(tempList.get(2), ",_");
			while(st.hasMoreTokens())
			{
				tempNotes.add(st.nextToken());
			}

			String taskDate = tempNotes.get(0) + "," + tempNotes.get(1);
			String taskStart = tempNotes.get(2).substring(1, tempNotes.get(2).length() - 1);
			String taskEnd = tempNotes.get(3).substring(1, tempNotes.get(3).length());

			eDateTime = taskDate + " " + taskStart;

			try 
			{
				eDateFormat = new SimpleDateFormat("MMMM d, yyyy hh:mm a", Locale.ENGLISH).parse(eDateTime);
			} 
			catch (ParseException e) {
				e.printStackTrace();
			}

			String taskVenue = tempNotes.get(4).substring(1, tempNotes.get(4).length());
			String taskNote = tempNotes.get(5).substring(1, tempNotes.get(5).length());
			if(taskNote.equals("No Additional Details"))
			{
				taskNote = "";
			}

			String attendees = "";
			if(tempNotes.size() == 7)
			{
				attendees = tempNotes.get(6).substring(1, tempNotes.get(6).length());
				attendees = attendees.substring(1, attendees.length() - 1);

				eAttendees = new ArrayList<String>();

				st = new StringTokenizer(attendees, "+");

				while(st.hasMoreTokens()) { 
					eAttendees.add((String) st.nextToken()); 
				}
			}

			InterfaceEvent newEvent = new BasicEvent(eventTitle, taskVenue, taskNote, taskDate, eDateFormat, taskStart, taskEnd, UUID.randomUUID().toString(), eAttendees);

			newEvent.setTaskID(eventTaskID);
			
			eventList.add(newEvent);
			Collections.sort(eventList);

			addEventToDB(newEvent);
		}
	}

	//Retrieve event by ID.
	public InterfaceEvent getEventforId(String eID)
	{
		for(int i = 0; i < getListSize(); i++)
		{
			if(getEventList().get(i).getEventID().equals(eID))
			{
				return getEventList().get(i);
			}
		}

		return null;
	}

	//Retrieve the entire array list.
	public synchronized ArrayList<InterfaceEvent> getEventList()
	{
		if(eventList.size() == 0)
		{
			eventList = EventDBModel.getDBInstance(null).getEvents();
		}

		return eventList;
	}

	//Return array list size.
	public int getListSize()
	{
		return getEventList().size();
	}
}
