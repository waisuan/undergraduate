/*
 * NAME: SIA WAI SUAN
 * STUDENT ID: S3308555
 * MOBILE APPLICATION DEVELOPMENT
 * ASSIGNMENT 2
 * FILE: EditEventActivity.java
 */
package mad.ass2.meetup.view;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.StringTokenizer;

import mad.ass1.meetup.main.R;
import mad.ass1.meetup.main.R.layout;
import mad.ass1.meetup.main.R.menu;
import mad.ass2.meetup.asynctask.DelAsyncTask;
import mad.ass2.meetup.asynctask.EditAsyncTask;
import mad.ass2.meetup.asynctask.RetrieveAsyncTask;
import mad.ass2.meetup.controller.ContactsListener;
import mad.ass2.meetup.controller.MultiLineKeyListener;
import mad.ass2.meetup.db.EventDBModel;
import mad.ass2.meetup.model.EventModel;
import mad.ass2.meetup.model.InterfaceEvent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

/*
 * The activity class for editing already created events.
 * In this activity, the values from the already created event is inserted into their
 * respective text fields for editing purposes.
 */
public class EditEventActivity extends Activity {
	private final static int editEventActivity = 300;
	//Integer identifier for the CONTACT PICKER function.
	private static final int CONTACT_PICKER_RESULT = 1001;
	//String identifier of the class.
	public final static String NEW_EVENT_DETAILS = "mad.ass1.meetup.view.NEW_DETAILS";
	//Integer identifier of the class.
	//public static final int editCallView = 2;

	private EditText editNoteText;
	private EditText editVenueText;
	private EditText editTitleText;
	private Button editButton;
	private Button cancelButton;
	private Button selectContacts;
	private TextView eventDate;
	private TextView eventStartTime;
	private TextView eventEndTime;
	private EditText contactsText;
	private Button selectDate;
	private Button selectStartTime;
	private Button selectEndTime;
	private Calendar calendar = Calendar.getInstance();

	private ArrayList<String> singleEventDetails = new ArrayList<String>();
	private int listPosition;
	private InterfaceEvent event;
	private String startAM_PM;
	private String endAM_PM;
	private String startTimeToken;
	private String endTimeToken;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_event);

		//Retrieve the caller class identifier in order to carry out caller class specific functions.
		Intent intent = getIntent();

		listPosition = intent.getIntExtra(SingleEventViewActivity.SINGLE_EVENT_DETAILS, 0);

		if(listPosition == 0)
		{
			listPosition = intent.getIntExtra(EventViewActivity.SELECTED_EVENT, 0);

			if(listPosition == 0)
			{
				listPosition = intent.getIntExtra(WeekViewActivity.SELECTED_EVENT, 0);
			}
		}

		//Execute the necessary AsyncTask.
		RetrieveAsyncTask retAsyncTask = new RetrieveAsyncTask(this, listPosition, editEventActivity);
		retAsyncTask.execute();

		//Places the values of the event into their respective text fields/positions.
		editTitleText = (EditText) findViewById(R.id.editEventTitleText);

		editVenueText = (EditText) findViewById(R.id.editEventAddrText);

		editNoteText = (EditText) findViewById(R.id.editNoteText);

		eventDate = (TextView) findViewById(R.id.editDateText);

		eventStartTime = (TextView) findViewById(R.id.editStartTimeText);

		eventEndTime = (TextView) findViewById(R.id.editEndTimeText);

		contactsText = (EditText) findViewById(R.id.editContactsText);
		
		//Disallow the ENTER key to be pressed in the multiple-line edit text box.
		contactsText.setOnKeyListener(new MultiLineKeyListener());

		//Initialize button functionalities.
		selectContacts = (Button) findViewById(R.id.editContactsButton);
		selectContacts.setOnClickListener(new ContactsListener(this, editEventActivity));

		selectDate = (Button) findViewById(R.id.editDateButton);
		selectDate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				new DatePickerDialog(EditEventActivity.this, datePicker, calendar
						.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
						calendar.get(Calendar.DAY_OF_MONTH)).show();
			}
		});

		selectStartTime = (Button) findViewById(R.id.editStartTimeButton);
		selectStartTime.setOnClickListener(new View.OnClickListener() {
			public  void onClick(View v) {
				new TimePickerDialog(EditEventActivity.this, startTimePicker, calendar
						.get(Calendar.HOUR_OF_DAY), calendar
						.get(Calendar.MINUTE), true).show();
			}
		});

		selectEndTime = (Button) findViewById(R.id.editEndTimeButton);
		selectEndTime.setOnClickListener(new View.OnClickListener() {
			public  void onClick(View v) {
				new TimePickerDialog(EditEventActivity.this, endTimePicker, calendar
						.get(Calendar.HOUR_OF_DAY), calendar
						.get(Calendar.MINUTE), true).show();
			}
		});

		editButton = (Button) findViewById(R.id.doneEditButton);
		editButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v)
			{
				editCurrEvent(v);
			}
		});

		cancelButton = (Button) findViewById(R.id.cancelEditButton);
		cancelButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v)
			{
				cancelEvent(v);
			}
		});
	}
	
	//Retrieve the RESULT from AsyncTask.
	public void giveList(InterfaceEvent event)
	{
		this.event = event;
		editTitleText.setText(event.getEventTitle());
		editVenueText.setText(event.getEventVenue());
		editNoteText.setText(event.getEventNote());
		eventDate.setText(event.getEventDate());
		eventStartTime.setText(event.getEventStartTime());
		eventEndTime.setText(event.getEventEndTime());
		
		setTimeTokens(event.getEventStartTime(), "startAM_PM", "startTimeToken");
		setTimeTokens(event.getEventEndTime(), "endAM_PM", "endTimeToken");
		
		if(event.getNumOfAttend() != 0)
		{
			contactsText.setText(event.getAttendees().get(0));
			for(int i = 1; i < event.getNumOfAttend(); i++)
			{
				contactsText.append(", " + event.getAttendees().get(i));
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_event, menu);
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

	//Method is used to determine if the event start/end time is in AM or PM.
	//Also, it is used to separate the HOUR and MINUTE of the start/end time for future use.
	public void setTimeTokens(String eTime, String whichAmPm, String whichToken)
	{
		ArrayList<String> timeTokens = new ArrayList<String>();

		StringTokenizer st = new StringTokenizer(eTime, ": "); 
		while(st.hasMoreTokens()) { 
			timeTokens.add((String) st.nextToken()); 
		}

		if(whichAmPm.equals("startAM_PM"))
		{
			startAM_PM = timeTokens.get(2);
		}
		else
		{
			endAM_PM = timeTokens.get(2);
		}

		if(whichToken.equals("startTimeToken"))
		{
			//Convert time into 24-hour format for validation purposes.
			if(startAM_PM.equals("PM") && !timeTokens.get(0).equals("12"))
			{
				timeTokens.set(0, String.valueOf(Integer.parseInt(timeTokens.get(0)) + 12));
			}
			else if(startAM_PM.equals("AM") && timeTokens.get(0).equals("12"))
			{
				timeTokens.set(0, "00");
			}

			startTimeToken = timeTokens.get(0) + timeTokens.get(1);
		}
		else
		{
			//Convert time into 24-hour format for validation purposes.
			if(endAM_PM.equals("PM") && !timeTokens.get(0).equals("12"))
			{
				timeTokens.set(0, String.valueOf(Integer.parseInt(timeTokens.get(0)) + 12));
			}
			else if(endAM_PM.equals("AM") && timeTokens.get(0).equals("12"))
			{
				timeTokens.set(0, "00");
			}

			endTimeToken = timeTokens.get(0) + timeTokens.get(1);
		}
	}

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

		eventStartTime.setText(dateTimeTokens.get(3) + ":" + dateTimeTokens.get(4) + " " + dateTimeTokens.get(6));

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

		eventEndTime.setText(dateTimeTokens.get(3) + ":" + dateTimeTokens.get(4) + " " + dateTimeTokens.get(6));

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

	//Method is called when user has finished editing the event and is ready to update it.
	public void editCurrEvent(View view)
	{	
		String eID = event.getEventID();

		String eventTitle = ((EditText) findViewById(R.id.editEventTitleText)).getText().toString();

		String eventVenue = ((EditText) findViewById(R.id.editEventAddrText)).getText().toString();

		String eventNote = ((EditText) findViewById(R.id.editNoteText)).getText().toString();

		String eventDate = ((TextView) findViewById(R.id.editDateText)).getText().toString();
		String eventStartTime = ((TextView) findViewById(R.id.editStartTimeText)).getText().toString();
		String eventEndTime = ((TextView) findViewById(R.id.editEndTimeText)).getText().toString();

		String eventAttend = ((EditText) findViewById(R.id.editContactsText)).getText().toString();
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
		else if(eventStartTime.equals(eventEndTime))
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
			//Place the necessary event values into a list and pass it to the Event Model class for updating.
			//Also, this activity returns to the Event View activity for convenient purposes.
			singleEventDetails.add(eventTitle);
			singleEventDetails.add(eventVenue);
			singleEventDetails.add(eventNote);
			singleEventDetails.add(eventDate);
			singleEventDetails.add(eventStartTime);
			singleEventDetails.add(eventEndTime);
			singleEventDetails.add(eventAttend);
			singleEventDetails.add(eID);

			//Execute the necessary AsyncTask.
			EditAsyncTask editAsyncTask = new EditAsyncTask(this, singleEventDetails, editEventActivity);
			editAsyncTask.execute();

		}
	}
	
	//Retrieve RESULT from AsyncTask.
	public void goBackToList(String eventTitle)
	{
		Intent returnIntent = new Intent(this, EventViewActivity.class);

		Toast.makeText(getApplicationContext(), "Edited event: " + eventTitle, Toast.LENGTH_LONG).show();

		startActivity(returnIntent);
	}
}
