/*
 * NAME: SIA WAI SUAN
 * STUDENT ID: S3308555
 * MOBILE APPLICATION DEVELOPMENT
 * ASSIGNMENT 2
 * FILE: NewEventActivity.java
 */
package mad.ass2.meetup.view;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.StringTokenizer;

import mad.ass1.meetup.main.R;
import mad.ass1.meetup.main.R.id;
import mad.ass1.meetup.main.R.layout;
import mad.ass2.meetup.asynctask.AddAsyncTask;
import mad.ass2.meetup.controller.ContactsListener;
import mad.ass2.meetup.controller.MultiLineKeyListener;
import mad.ass2.meetup.model.EventModel;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.Contacts;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

/*
 * The activity class for adding a new event to the list view.
 * In this activity, the user has to key in the necessary event details before adding the event.
 * Validation is implemented here to prevent any unnecessary situations.
 */
public class NewEventActivity extends Activity {
	private final static int newEventActivity = 200;
	//String identifier of the class.
	public final static String EVENT_DETAILS = "mad.ass1.meetup.view.DETAILS";
	//Integer identifier for the CONTACT PICKER function.
	private static final int CONTACT_PICKER_RESULT = 1000;

	private Button addButton;
	private Button cancelButton;
	private Button selectContacts;
	private TextView eventDate;
	private TextView startTime;
	private TextView endTime;
	private EditText contactsText;
	private Button selectDate;
	private Button selectStart;
	private Button selectEnd;
	private Calendar calendar = Calendar.getInstance();

	private ArrayList<String> eventDetails = new ArrayList<String>();
	private String startAM_PM;
	private String endAM_PM;
	private String startTimeToken;
	private String endTimeToken;
	private ArrayList<String> weekViewCell = new ArrayList<String>();

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_event);

		//Retrieve the caller class identifier in order to carry out caller class specific functions.
		Intent intent = getIntent();

		weekViewCell = intent.getStringArrayListExtra(WeekViewActivity.EMPTY_CELL);

		//Disallow the ENTER key to be pressed in the multiple-line edit text box.
		contactsText = (EditText) findViewById(R.id.contactsText);
		contactsText.setOnKeyListener(new MultiLineKeyListener());
		
		//Necessary initializers of the layout items on the interface.
		selectContacts = (Button) findViewById(R.id.selContactsButton);
		selectContacts.setOnClickListener(new ContactsListener(this, newEventActivity));

		eventDate = (TextView) findViewById(R.id.dateText);
		startTime = (TextView) findViewById(R.id.startTimeText);
		endTime = (TextView) findViewById(R.id.endTimeText);

		//If the caller class is the Week View activity, initialize the DATE, START TIME & END TIME
		//with their necessary values.
		if(weekViewCell != null)
		{
			StringTokenizer st = new StringTokenizer(weekViewCell.get(0), ":");

			String sHour = st.nextToken();
			String sMin = st.nextToken();

			startAM_PM = weekViewCell.get(1);

			//Convert time into 24-hour format for validation purposes.
			if(startAM_PM.equals("PM") && !sHour.equals("12"))
			{
				sHour = String.valueOf(Integer.parseInt(sHour) + 12);
			}
			else if(startAM_PM.equals("AM") && sHour.equals("12"))
			{
				sHour = "00";
			}

			//Separate the HOUR and MINUTE of the start/end time for future use.
			startTimeToken = sHour + sMin;

			eventDate.setText(weekViewCell.get(2));
			startTime.setText(weekViewCell.get(3));
		}

		selectDate = (Button) findViewById(R.id.selDateButton);
		selectDate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				new DatePickerDialog(NewEventActivity.this, datePicker, calendar
						.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
						calendar.get(Calendar.DAY_OF_MONTH)).show();
			}
		});

		selectStart = (Button) findViewById(R.id.startTimeButton);
		selectStart.setOnClickListener(new View.OnClickListener() {
			public  void onClick(View v) {
				new TimePickerDialog(NewEventActivity.this, startTimePicker, calendar
						.get(Calendar.HOUR_OF_DAY), calendar
						.get(Calendar.MINUTE), true).show();
			}
		});

		selectEnd = (Button) findViewById(R.id.endTimeButton);
		selectEnd.setOnClickListener(new View.OnClickListener() {
			public  void onClick(View v) {
				new TimePickerDialog(NewEventActivity.this, endTimePicker, calendar
						.get(Calendar.HOUR_OF_DAY), calendar
						.get(Calendar.MINUTE), true).show();
			}
		});

		addButton = (Button) findViewById(R.id.addEventButton);
		addButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v)
			{
				addNewEvent(v);
			}
		});

		cancelButton = (Button) findViewById(R.id.cancelEventButton);
		cancelButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v)
			{
				cancelEvent(v);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_new_event, menu);
		return true;
	}

	//Dialog pickers for DATE and TIME and their respective methods.
	DatePickerDialog.OnDateSetListener datePicker = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			calendar.set(Calendar.YEAR, year);
			calendar.set(Calendar.MONTH, monthOfYear);
			calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			updateDateLabel();
		}
	};

	TimePickerDialog.OnTimeSetListener startTimePicker = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
			calendar.set(Calendar.MINUTE, minute);
			updateStartTimeLabel();
		}
	};

	TimePickerDialog.OnTimeSetListener endTimePicker = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
			calendar.set(Calendar.MINUTE, minute);
			updateEndTimeLabel();
		}
	};

	//Method is called when user has selected a DATE from the DatePickerDialog.
	public void updateDateLabel() {
		DateFormat dateFormat = DateFormat.getDateTimeInstance();

		String dateStamp = dateFormat.format(calendar.getTime());

		ArrayList<String> dateTimeTokens = new ArrayList<String>();

		StringTokenizer st = new StringTokenizer(dateStamp, " "); 
		while(st.hasMoreTokens()) { 
			dateTimeTokens.add((String) st.nextToken()); 
		}

		eventDate.setText(dateTimeTokens.get(0) + " " + dateTimeTokens.get(1) + " " + dateTimeTokens.get(2));
	}

	//Method is called when user has selected a START TIME from the TimePickerDialog.
	public void updateStartTimeLabel(){
		DateFormat dateFormat = DateFormat.getDateTimeInstance();

		String dateStamp = dateFormat.format(calendar.getTime());

		ArrayList<String> dateTimeTokens = new ArrayList<String>();

		StringTokenizer st = new StringTokenizer(dateStamp, ": "); 
		while(st.hasMoreTokens()) { 
			dateTimeTokens.add((String) st.nextToken()); 
		}

		startTime.setText(dateTimeTokens.get(3) + ":" + dateTimeTokens.get(4) + " " + dateTimeTokens.get(6));

		startAM_PM = dateTimeTokens.get(6);

		//Convert time into 24-hour format for validation purposes.
		if(startAM_PM.equals("PM") && !dateTimeTokens.get(3).equals("12"))
		{
			dateTimeTokens.set(3, String.valueOf(Integer.parseInt(dateTimeTokens.get(3)) + 12));
		}
		else if(startAM_PM.equals("AM") && dateTimeTokens.get(3).equals("12"))
		{
			dateTimeTokens.set(3, "00");
		}

		startTimeToken = dateTimeTokens.get(3) + dateTimeTokens.get(4);
	}

	//Method is called when user has selected a END TIME from the TimePickerDialog.
	public void updateEndTimeLabel(){
		DateFormat dateFormat = DateFormat.getDateTimeInstance();

		String dateStamp = dateFormat.format(calendar.getTime());

		ArrayList<String> dateTimeTokens = new ArrayList<String>();

		StringTokenizer st = new StringTokenizer(dateStamp, ": "); 
		while(st.hasMoreTokens()) { 
			dateTimeTokens.add((String) st.nextToken()); 
		}

		endTime.setText(dateTimeTokens.get(3) + ":" + dateTimeTokens.get(4) + " " + dateTimeTokens.get(6));

		endAM_PM = dateTimeTokens.get(6);

		//Convert time into 24-hour format for validation purposes.
		if(endAM_PM.equals("PM") && !dateTimeTokens.get(3).equals("12"))
		{
			dateTimeTokens.set(3, String.valueOf(Integer.parseInt(dateTimeTokens.get(3)) + 12));
		}
		else if(endAM_PM.equals("AM") && dateTimeTokens.get(3).equals("12"))
		{
			dateTimeTokens.set(3, "00");
		}

		endTimeToken = dateTimeTokens.get(3) + dateTimeTokens.get(4);
	}

	//Method is called when user has selected a contact and is ready for processing.
	@Override  
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
		if (resultCode == RESULT_OK) {  
			switch (requestCode) {  
			case CONTACT_PICKER_RESULT:  
				Cursor cursor = null;  
				String email = "";  
				try {  
					Uri result = data.getData();
					Log.v("CONTACT_PICKER", "Got a contact result: " + result.toString());  

					String id = result.getLastPathSegment();  

					cursor = getContentResolver().query(Email.CONTENT_URI,  
							null, Email.CONTACT_ID + "=?", new String[] { id },  
							null);  
					int emailIdx = cursor.getColumnIndex(Email.DATA);  

					if (cursor.moveToFirst()) {  
						email = cursor.getString(emailIdx);  
						Log.v("CONTACT_PICKER", "Got email: " + email); 
					} else {  
						Log.w("CONTACT_PICKER", "No results");  
					}  
				} catch (Exception e) {  
					Log.e("CONTACT_PICKER", "Failed to get email data", e);  
				} finally {  
					if (cursor != null) {  
						cursor.close();  
					}

					contactsText = (EditText) findViewById(R.id.contactsText);
					
					String content = contactsText.getText().toString();
					content = content.replaceAll(" ", "");
					
					if (content.matches("")) {
						contactsText.setText(email);
					}
					else
					{
						contactsText.append(", " + email);
					}
					if (email.length() == 0) {  
						Toast.makeText(this, "No email found for contact.", Toast.LENGTH_LONG).show();  
					}  
				}  
				break;  
			}  
		} 
		else 
		{  
			Log.w("CONTACT_PICKER", "Warning: activity result not ok");  
		}  
	}

	//Discard current activity and return to previous activity.
	public void cancelEvent(View view)
	{
		finish();
	}

	//Method is called when user is ready to create the new event.
	public void addNewEvent(View view)
	{
		String eventTitle = ((EditText) findViewById(R.id.eventTitleText)).getText().toString();

		String eventVenue = ((EditText) findViewById(R.id.eventAddrText)).getText().toString();

		String eventNote = ((EditText) findViewById(R.id.noteText)).getText().toString();

		String eventDate = ((TextView) findViewById(R.id.dateText)).getText().toString();
		String startTime = ((TextView) findViewById(R.id.startTimeText)).getText().toString();
		String endTime = ((TextView) findViewById(R.id.endTimeText)).getText().toString();

		String eventAttend = ((EditText) findViewById(R.id.contactsText)).getText().toString();
		eventAttend = eventAttend.replaceAll(" ", "");
		
		//Validation is done for EVENT TITLE, EVENT VENUE, DATE, START TIME and END TIME.
		if(eventTitle.length() == 0)
		{
			new AlertDialog.Builder(this)
			.setTitle("ERROR!")
			.setMessage("Event Title cannot be empty!")
			.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) { 
				}
			})
			.show();
		}
		else if(eventVenue.length() == 0)
		{
			new AlertDialog.Builder(this)
			.setTitle("ERROR!")
			.setMessage("Event Venue cannot be empty!")
			.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) { 
				}
			})
			.show();
		}
		else if(eventDate.equals("Date?") || startTime.equals("Start?") || endTime.equals("End?"))
		{
			new AlertDialog.Builder(this)
			.setTitle("ERROR!")
			.setMessage("Please select a DATE & TIME of event!")
			.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) { 
				}
			})
			.show();
		}
		else if(startTime.equals(endTime))
		{
			new AlertDialog.Builder(this)
			.setTitle("ERROR!")
			.setMessage("START time and END time cannot be equal!")
			.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) { 
				}
			})
			.show();
		}
		else if(startAM_PM.equals("PM") && endAM_PM.equals("AM"))
		{
			new AlertDialog.Builder(this)
			.setTitle("ERROR!")
			.setMessage("Sorry, but this APP does not support events that span over a day!")
			.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) { 
				}
			})
			.show();
		}
		else if(Integer.parseInt(startTimeToken) > Integer.parseInt(endTimeToken))
		{
			new AlertDialog.Builder(this)
			.setTitle("ERROR!")
			.setMessage("Sorry, but this APP does not support events that span over a day!")
			.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) { 
				}
			})
			.show();
		}
		else
		{	
			//Place the necessary event values into a list and pass it to the Event Model class for creation.
			//Also, this activity returns to the Event View activity for convenient purposes.
			eventDetails.add(eventTitle);
			eventDetails.add(eventVenue);
			eventDetails.add(eventNote);
			eventDetails.add(eventDate);
			eventDetails.add(startTime);
			eventDetails.add(endTime);
			eventDetails.add(eventAttend);

			//Execute the necessary AsyncTask.
			AddAsyncTask addAsyncTask = new AddAsyncTask(this, eventDetails, newEventActivity);
			addAsyncTask.execute();
		}
		
	}
	
	//Retrieve the RESULT from AsyncTask.
	public void goBackToList(String eventTitle)
	{
		Intent intent = new Intent(this, EventViewActivity.class);
		
		Toast.makeText(getApplicationContext(), "Added event: " + eventTitle, Toast.LENGTH_LONG).show();
		
		startActivity(intent);
	}
}
