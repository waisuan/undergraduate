/*
 * NAME: SIA WAI SUAN
 * STUDENT ID: S3308555
 * MOBILE APPLICATION DEVELOPMENT
 * ASSIGNMENT 2
 * FILE: CreateDB.java
 */
package mad.ass2.meetup.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/*
 * Creation of the Database file is done here.
 * Tables are only created if they do not exist already.
 */
public class CreateDB extends SQLiteOpenHelper{

	//Only ONE SQLiteDatabase should be initialized/created throughout the life cycle of the app.
	//(Unless there are multiple DBs).
	public static SQLiteDatabase database = null;
	
	//COLUMNS for the EVENT table.
	public static final String TABLE_EVENTS = "events";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_EVENT_ID = "eventID";
	public static final String COLUMN_EVENT_TITLE = "eventTitle";
	public static final String COLUMN_EVENT_VENUE = "eventVenue";
	public static final String COLUMN_EVENT_NOTE = "eventNote";
	public static final String COLUMN_EVENT_DATE = "eventDate";
	public static final String COLUMN_EVENT_STARTTIME = "eventStartTime";
	public static final String COLUMN_EVENT_ENDTIME = "eventEndTime";
	public static final String COLUMN_EVENT_ATTENDEES = "eventAttendees";
	public static final String COLUMN_EVENT_DATEFORMAT = "eventDateFormat";
	public static final String COLUMN_TASK_ID = "eventTaskID";
	
	//COLUMNS for the TASK table.
	public static final String TABLE_TASKS = "tasks";
	public static final String COLUMN_TABLE_TASK_ID = "_id";
	public static final String COLUMN_TASK_OBJID = "taskObjID";
	public static final String COLUMN_TASK_REQTYPE = "taskReqType";
	public static final String COLUMN_TASK_EVENT_ID = "taskEventID";
	public static final String COLUMN_TASK_EVENT_TITLE = "taskEventTitle";
	public static final String COLUMN_TASK_EVENT_VENUE = "taskEventVenue";
	public static final String COLUMN_TASK_EVENT_NOTE = "taskEventNote";
	public static final String COLUMN_TASK_EVENT_DATE = "taskEventDate";
	public static final String COLUMN_TASK_EVENT_STARTTIME = "taskEventStartTime";
	public static final String COLUMN_TASK_EVENT_ENDTIME = "taskEventEndTime";
	public static final String COLUMN_TASK_EVENT_ATTENDEES = "taskEventAttendees";
	public static final String COLUMN_TASK_EVENT_DATEFORMAT = "taskEventDateFormat";
	public static final String COLUMN_TASK_TASK_ID = "taskEventTaskID";

	private static final String DATABASE_NAME = "events.db";
	private static final int DATABASE_VERSION = 1;

	//Database creation SQL statement.
	//TWO tables are created.
	private static final String DATABASE_CREATE = "create table if not exists "
			+ TABLE_EVENTS + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " 
			+ COLUMN_EVENT_ID + " text not null, " 
			+ COLUMN_EVENT_TITLE + " text not null, "
			+ COLUMN_EVENT_VENUE + " text not null, "
			+ COLUMN_EVENT_NOTE + " text not null, "
			+ COLUMN_EVENT_DATE + " text not null, "
			+ COLUMN_EVENT_STARTTIME + " text not null, "
			+ COLUMN_EVENT_ENDTIME + " text not null, "
			+ COLUMN_EVENT_ATTENDEES + " text not null, "
			+ COLUMN_EVENT_DATEFORMAT + " text not null,"
			+ COLUMN_TASK_ID + " text not null);";
	
	private static final String DATABASE_TASK_CREATE = "create table if not exists "
			+ TABLE_TASKS + "(" + COLUMN_TABLE_TASK_ID
			+ " integer primary key autoincrement, "
			+ COLUMN_TASK_OBJID + " text not null, "
			+ COLUMN_TASK_REQTYPE + " text not null, "
			+ COLUMN_TASK_EVENT_ID + " text not null, " 
			+ COLUMN_TASK_EVENT_TITLE + " text not null, "
			+ COLUMN_TASK_EVENT_VENUE + " text not null, "
			+ COLUMN_TASK_EVENT_NOTE + " text not null, "
			+ COLUMN_TASK_EVENT_DATE + " text not null, "
			+ COLUMN_TASK_EVENT_STARTTIME + " text not null, "
			+ COLUMN_TASK_EVENT_ENDTIME + " text not null, "
			+ COLUMN_TASK_EVENT_ATTENDEES + " text not null, "
			+ COLUMN_TASK_EVENT_DATEFORMAT + " text not null,"
			+ COLUMN_TASK_TASK_ID + " text not null);";

	//Constructor.
	public CreateDB(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	//Called from the CONSTRUCTOR.
	//Tables are created ONLY IF they don't already exist.
	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
		database.execSQL(DATABASE_TASK_CREATE);
		
		Log.i("CREATE_DB", "CREATED");
	}

	//DO NOT REMOVE.
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(CreateDB.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
		onCreate(db);
	}
}
