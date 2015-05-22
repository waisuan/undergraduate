/*
 * NAME: SIA WAI SUAN
 * STUDENT ID: S3308555
 * MOBILE APPLICATION DEVELOPMENT
 * ASSIGNMENT 2
 * FILE: EventDBModel.java
 */
package mad.ass2.meetup.db;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import mad.ass2.meetup.model.BasicEvent;
import mad.ass2.meetup.model.InterfaceEvent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/*
 * (EVENT) Helper class for handling specific TABLE operations as well as OPENING and CLOSING of the DB.
 */
public class EventDBModel{

	private CreateDB dbHelper;
	private String[] allColumns = { CreateDB.COLUMN_ID,
			CreateDB.COLUMN_EVENT_ID, 
			CreateDB.COLUMN_EVENT_TITLE, 
			CreateDB.COLUMN_EVENT_VENUE,
			CreateDB.COLUMN_EVENT_NOTE,
			CreateDB.COLUMN_EVENT_DATE,
			CreateDB.COLUMN_EVENT_STARTTIME,
			CreateDB.COLUMN_EVENT_ENDTIME,
			CreateDB.COLUMN_EVENT_ATTENDEES,
			CreateDB.COLUMN_EVENT_DATEFORMAT,
			CreateDB.COLUMN_TASK_ID};
	
	//Singleton class.
	private static EventDBModel dataAccess = null;

	//Calls for TABLE(s) to be created IF necessary.
	//CreateDB class then returns an instance of itself for use.
	private EventDBModel(Context context) {
		dbHelper = new CreateDB(context);
	}
	
	//Other class may call the items from this class whilst using this getter method.
	public static EventDBModel getDBInstance(Context context)
	{
		if(dataAccess == null)
		{
			dataAccess = new EventDBModel(context);
		}
		
		return dataAccess;
	}

	//Opens the database as well as retrieving a SQLiteDatabase instance.
	//SQLiteDatabase should only be instantiated ONCE throughout the life cycle of the app.
	//(Unless there are multiple DBs).
	public void open() throws SQLException {
		Log.i("OPEN_EVENT_DB", "OPEN");
		
		if(CreateDB.database == null)
		{
			CreateDB.database = dbHelper.getWritableDatabase();
		}
	}

	public void close() {
		Log.i("CLOSE_EVENT_DB", "CLOSE");
		dbHelper.close();
	}

	//Insert EVENT object into the DB.
	//Attributes of the object are extracted according to the specified COLUMNS in the TABLE.
	public void createEvent(InterfaceEvent event)
	{
		ContentValues values = new ContentValues();
		values.put(CreateDB.COLUMN_EVENT_TITLE, event.getEventTitle());
		values.put(CreateDB.COLUMN_EVENT_VENUE, event.getEventVenue());
		values.put(CreateDB.COLUMN_EVENT_NOTE, event.getEventNote());
		values.put(CreateDB.COLUMN_EVENT_DATE, event.getEventDate());
		values.put(CreateDB.COLUMN_EVENT_DATEFORMAT, event.getEventDateFormat().toString());
		values.put(CreateDB.COLUMN_EVENT_STARTTIME, event.getEventStartTime());
		values.put(CreateDB.COLUMN_EVENT_ENDTIME, event.getEventEndTime());
		values.put(CreateDB.COLUMN_EVENT_ID, event.getEventID());
		values.put(CreateDB.COLUMN_TASK_ID, event.getTaskID());

		//Convert ARRAY_LIST into a JSONARRAY and in turn, convert the JSONARRAY into a STRING type.
		//Columns don't accept ARRAYS/ARRAY_LISTS.
		JSONObject json = new JSONObject();
		try {
			json.put("eventAttendees", new JSONArray(event.getAttendees()));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String attendees = json.toString();

		values.put(CreateDB.COLUMN_EVENT_ATTENDEES, attendees);

		//Insert into the DB.
		long insertID = CreateDB.database.insert(CreateDB.TABLE_EVENTS, null, values);
	}

	//Updates a particular ROW in the DB TABLE.
	//Attributes of the object are extracted according to the specified COLUMNS in the TABLE.
	public void updateEvent(InterfaceEvent event)
	{
		ContentValues values = new ContentValues();
		values.put(CreateDB.COLUMN_EVENT_TITLE, event.getEventTitle());
		values.put(CreateDB.COLUMN_EVENT_VENUE, event.getEventVenue());
		values.put(CreateDB.COLUMN_EVENT_NOTE, event.getEventNote());
		values.put(CreateDB.COLUMN_EVENT_DATE, event.getEventDate());
		values.put(CreateDB.COLUMN_EVENT_DATEFORMAT, event.getEventDateFormat().toString());
		values.put(CreateDB.COLUMN_EVENT_STARTTIME, event.getEventStartTime());
		values.put(CreateDB.COLUMN_EVENT_ENDTIME, event.getEventEndTime());
		values.put(CreateDB.COLUMN_EVENT_ID, event.getEventID());
		values.put(CreateDB.COLUMN_TASK_ID, event.getTaskID());

		//Convert ARRAY_LIST into a JSONARRAY and in turn, convert the JSONARRAY into a STRING type.
		//Columns don't accept ARRAYS/ARRAY_LISTS.
		JSONObject json = new JSONObject();
		try {
			json.put("eventAttendees", new JSONArray(event.getAttendees()));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String attendees = json.toString();

		values.put(CreateDB.COLUMN_EVENT_ATTENDEES, attendees);

		//SQL Query for updating a SPECIFIC ROW in the TABLE.
		String foundQuery = CreateDB.COLUMN_EVENT_ID + "='" + event.getEventID() + "'"; 
		Cursor cursor = CreateDB.database.query(CreateDB.TABLE_EVENTS, allColumns, foundQuery, null, null, null, null, null);

		//Retrieve the FIRST MATCH.
		cursor.moveToFirst();

		//Updates the DB TABLE ROW.
		CreateDB.database.update(CreateDB.TABLE_EVENTS, values, CreateDB.COLUMN_ID + "=" + cursor.getString(0), null);
		cursor.close();
	}

	//Retrieves all the ROWS from the DB TABLE.
	public ArrayList<InterfaceEvent> getEvents()
	{
		ArrayList<InterfaceEvent> events = new ArrayList<InterfaceEvent>();

		//SQL Query to get all the rows & columns from the specified TABLE.
		Cursor cursor = CreateDB.database.query(CreateDB.TABLE_EVENTS,
				allColumns, null, null, null, null, null);

		//Iterate through the TABLE from the FIRST row to the LAST, convert each ROW into an EVENT type and then
		//add to an ARRAY_LIST.
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			InterfaceEvent event = null;
			try {
				event = cursorToEvent(cursor);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			events.add(event);
			cursor.moveToNext();
		}
		
		// Make sure to close the cursor
		cursor.close();

		//Sort the newly created ARRAY_LIST.
		Collections.sort(events);
		
		return events;
	}

	//Function to convert the values in each ROW into an EVENT object/type. 
	private InterfaceEvent cursorToEvent(Cursor cursor) throws JSONException {
		InterfaceEvent event = new BasicEvent(null, null, null, null, null, null, null, null, null);
		event.setEventID(cursor.getString(1));
		event.setEventTitle(cursor.getString(2));
		event.setEventVenue(cursor.getString(3));
		event.setEventNote(cursor.getString(4));
		event.setEventDate(cursor.getString(5));
		event.setEventStartTime(cursor.getString(6));
		event.setEventEndTime(cursor.getString(7));

		//Read the value from the ROW as a STRING type, converts it into a JSONARRAY and in turn, convert the JSONARRAY
		//into an ARRAY_LIST type (manual).
		JSONObject json = new JSONObject(cursor.getString(8));
		JSONArray eventAttendees = json.optJSONArray("eventAttendees");
		ArrayList<String> attendeeList = new ArrayList<String>();
		if (eventAttendees != null) { 
			int len = eventAttendees.length();
			for (int i = 0; i < len; i++){ 
				attendeeList.add(eventAttendees.get(i).toString());
			} 
		} 

		event.setEventAttendees(attendeeList);

		//Convert the DATE/TIME string into a DATE type/format.
		try {
			Date eDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH).parse(cursor.getString(9));
			event.setEventDateFormat(eDateFormat);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		event.setTaskID(cursor.getString(10));
		
		return event;
	}

	//Deletes a specific ROW from the DB TABLE.
	public void deleteEvent(InterfaceEvent event)
	{
		CreateDB.database.delete(CreateDB.TABLE_EVENTS, CreateDB.COLUMN_EVENT_ID
				+ "='" + event.getEventID() + "'", null);
	}
	
	//Deletes all ROWS from the DB TABLE.
	public void deleteAllEvents()
	{
		CreateDB.database.delete(CreateDB.TABLE_EVENTS, null, null);
	}
}
