/*
 * NAME: SIA WAI SUAN
 * STUDENT ID: S3308555
 * MOBILE APPLICATION DEVELOPMENT
 * ASSIGNMENT 2
 * FILE: TaskDBModel.java
 */
package mad.ass2.meetup.db;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import mad.ass2.meetup.model.BasicEvent;
import mad.ass2.meetup.model.InterfaceEvent;
import mad.ass2.meetup.model.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/*
 * (TASK) Helper class for handling specific TABLE operations as well as OPENING and CLOSING of the DB.
 */
public class TaskDBModel {

	private CreateDB dbHelper;
	private String[] allTaskColumns = { CreateDB.COLUMN_TABLE_TASK_ID,
			CreateDB.COLUMN_TASK_OBJID,
			CreateDB.COLUMN_TASK_REQTYPE,
			CreateDB.COLUMN_TASK_EVENT_ID, 
			CreateDB.COLUMN_TASK_EVENT_TITLE, 
			CreateDB.COLUMN_TASK_EVENT_VENUE,
			CreateDB.COLUMN_TASK_EVENT_NOTE,
			CreateDB.COLUMN_TASK_EVENT_DATE,
			CreateDB.COLUMN_TASK_EVENT_STARTTIME,
			CreateDB.COLUMN_TASK_EVENT_ENDTIME,
			CreateDB.COLUMN_TASK_EVENT_ATTENDEES,
			CreateDB.COLUMN_TASK_EVENT_DATEFORMAT,
			CreateDB.COLUMN_TASK_TASK_ID};

	//Singleton class.
	private static TaskDBModel taskDataAccess = null;

	//Calls for TABLE(s) to be created IF necessary.
	//CreateDB class then returns an instance of itself for use.
	private TaskDBModel(Context context) {
		Log.i("TASKDB", "CONSTRUCTOR");
		dbHelper = new CreateDB(context);
	}

	//Other class may call the items from this class whilst using this getter method.
	public static TaskDBModel getDBInstance(Context context)
	{
		Log.i("TASKDB", "GET INSTANCE");
		if(taskDataAccess == null)
		{
			taskDataAccess = new TaskDBModel(context);
		}

		return taskDataAccess;
	}

	//Opens the database as well as retrieving a SQLiteDatabase instance.
	//SQLiteDatabase should only be instantiated ONCE throughout the life cycle of the app.
	//(Unless there are multiple DBs).
	public void open() throws SQLException {
		Log.i("OPEN_TASK_DB", "OPEN");

		if(CreateDB.database == null)
		{
			CreateDB.database = dbHelper.getWritableDatabase();
		}
	}

	public void close() {
		Log.i("CLOSE_TASK_DB", "CLOSE");
		dbHelper.close();
	}

	//Insert TASK object into the DB.
	//Attributes of the object are extracted according to the specified COLUMNS in the TABLE.
	public void insertTask(Task task)
	{
		Log.i("INSERT_TASK_DB", "INSERT");
		ContentValues values = new ContentValues();
		values.put(CreateDB.COLUMN_TASK_OBJID, task.getTaskObjID());
		values.put(CreateDB.COLUMN_TASK_REQTYPE, task.getReqType());
		values.put(CreateDB.COLUMN_TASK_EVENT_TITLE, task.getEvent().getEventTitle());
		values.put(CreateDB.COLUMN_TASK_EVENT_VENUE, task.getEvent().getEventVenue());
		values.put(CreateDB.COLUMN_TASK_EVENT_NOTE, task.getEvent().getEventNote());
		values.put(CreateDB.COLUMN_TASK_EVENT_DATE, task.getEvent().getEventDate());
		values.put(CreateDB.COLUMN_TASK_EVENT_DATEFORMAT, task.getEvent().getEventDateFormat().toString());
		values.put(CreateDB.COLUMN_TASK_EVENT_STARTTIME, task.getEvent().getEventStartTime());
		values.put(CreateDB.COLUMN_TASK_EVENT_ENDTIME, task.getEvent().getEventEndTime());
		values.put(CreateDB.COLUMN_TASK_EVENT_ID, task.getEvent().getEventID());
		values.put(CreateDB.COLUMN_TASK_TASK_ID, task.getEvent().getTaskID());

		//Convert ARRAY_LIST into a JSONARRAY and in turn, convert the JSONARRAY into a STRING type.
		//Columns don't accept ARRAYS/ARRAY_LISTS.
		JSONObject json = new JSONObject();
		try {
			json.put("taskEventAttendees", new JSONArray(task.getEvent().getAttendees()));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String attendees = json.toString();

		values.put(CreateDB.COLUMN_TASK_EVENT_ATTENDEES, attendees);

		//Insert into the DB.
		CreateDB.database.insert(CreateDB.TABLE_TASKS, null, values);
	}

	//Updates a particular ROW in the DB TABLE.
	//Attributes of the object are extracted according to the specified COLUMNS in the TABLE.
	public void updateTask(Task task)
	{
		ContentValues values = new ContentValues();
		values.put(CreateDB.COLUMN_TASK_OBJID, task.getTaskObjID());
		values.put(CreateDB.COLUMN_TASK_REQTYPE, task.getReqType());
		values.put(CreateDB.COLUMN_TASK_EVENT_TITLE, task.getEvent().getEventTitle());
		values.put(CreateDB.COLUMN_TASK_EVENT_VENUE, task.getEvent().getEventVenue());
		values.put(CreateDB.COLUMN_TASK_EVENT_NOTE, task.getEvent().getEventNote());
		values.put(CreateDB.COLUMN_TASK_EVENT_DATE, task.getEvent().getEventDate());
		values.put(CreateDB.COLUMN_TASK_EVENT_DATEFORMAT, task.getEvent().getEventDateFormat().toString());
		values.put(CreateDB.COLUMN_TASK_EVENT_STARTTIME, task.getEvent().getEventStartTime());
		values.put(CreateDB.COLUMN_TASK_EVENT_ENDTIME, task.getEvent().getEventEndTime());
		values.put(CreateDB.COLUMN_TASK_EVENT_ID, task.getEvent().getEventID());
		values.put(CreateDB.COLUMN_TASK_TASK_ID, task.getEvent().getTaskID());

		//Convert ARRAY_LIST into a JSONARRAY and in turn, convert the JSONARRAY into a STRING type.
		//Columns don't accept ARRAYS/ARRAY_LISTS.
		JSONObject json = new JSONObject();
		try {
			json.put("taskEventAttendees", new JSONArray(task.getEvent().getAttendees()));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String attendees = json.toString();

		values.put(CreateDB.COLUMN_TASK_EVENT_ATTENDEES, attendees);

		//SQL Query for updating a SPECIFIC ROW in the TABLE.
		String foundQuery = CreateDB.COLUMN_TASK_OBJID + "='" + task.getTaskObjID() + "'"; 
		Cursor cursor = CreateDB.database.query(CreateDB.TABLE_TASKS, allTaskColumns, foundQuery, null, null, null, null, null);

		//Retrieve the FIRST MATCH.
		cursor.moveToFirst();

		//Updates the DB TABLE ROW.
		CreateDB.database.update(CreateDB.TABLE_TASKS, values, CreateDB.COLUMN_TABLE_TASK_ID + "=" + cursor.getString(0), null);
		cursor.close();
	}

	//Retrieves all the ROWS from the DB TABLE.
	public ArrayList<Task> getTasks()
	{
		Log.i("GET_TASK_DB", "GET");
		ArrayList<Task> tasks = new ArrayList<Task>();

		//SQL Query to get all the rows & columns from the specified TABLE.
		Cursor cursor = CreateDB.database.query(CreateDB.TABLE_TASKS,
				allTaskColumns, null, null, null, null, null);

		//Iterate through the TABLE from the FIRST row to the LAST, convert each ROW into a TASK type and then
		//add to an ARRAY_LIST.
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Task task = null;
			try {
				task = cursorToTask(cursor);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			tasks.add(task);
			cursor.moveToNext();
		}

		// Make sure to close the cursor
		cursor.close();

		return tasks;
	}

	//Function to convert the values in each ROW into a TASK object/type. 
	private Task cursorToTask(Cursor cursor) throws JSONException {

		InterfaceEvent event = new BasicEvent(null, null, null, null, null, null, null, null, null);
		Task task = new Task(null, event);

		task.setTaskObjID(cursor.getString(1));
		task.setReqType(cursor.getString(2));

		task.getEvent().setEventID(cursor.getString(3));
		task.getEvent().setEventTitle(cursor.getString(4));
		task.getEvent().setEventVenue(cursor.getString(5));
		task.getEvent().setEventNote(cursor.getString(6));
		task.getEvent().setEventDate(cursor.getString(7));
		task.getEvent().setEventStartTime(cursor.getString(8));
		task.getEvent().setEventEndTime(cursor.getString(9));

		//Read the value from the ROW as a STRING type, converts it into a JSONARRAY and in turn, convert the JSONARRAY
		//into an ARRAY_LIST type (manual).
		JSONObject json = new JSONObject(cursor.getString(10));
		JSONArray taskEventAttendees = json.optJSONArray("taskEventAttendees");
		ArrayList<String> attendeeList = new ArrayList<String>();
		if (taskEventAttendees != null) { 
			int len = taskEventAttendees.length();
			for (int i = 0; i < len; i++){ 
				attendeeList.add(taskEventAttendees.get(i).toString());
			} 
		} 

		task.getEvent().setEventAttendees(attendeeList);

		//Convert the DATE/TIME string into a DATE type/format.
		try {
			Date eDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH).parse(cursor.getString(11));
			task.getEvent().setEventDateFormat(eDateFormat);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		task.getEvent().setTaskID(cursor.getString(12));

		return task;
	}

	//Deletes a specific ROW from the DB TABLE.
	public void deleteTask(Task task)
	{
		Log.i("DEL_TASK_DB", "DELETE");

		CreateDB.database.delete(CreateDB.TABLE_TASKS, CreateDB.COLUMN_TASK_OBJID
				+ "='" + task.getTaskObjID() + "'", null);
	}

	//Deletes all ROWS from the DB TABLE.
	public void deleteAllTasks()
	{
		CreateDB.database.delete(CreateDB.TABLE_TASKS, null, null);
	}
}
