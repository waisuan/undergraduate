/*
 * NAME: SIA WAI SUAN
 * STUDENT ID: S3308555
 * MOBILE APPLICATION DEVELOPMENT
 * ASSIGNMENT 2
 * FILE: SingleEventViewActivity.java
 */
package mad.ass2.meetup.view;

import java.util.ArrayList;
import java.util.StringTokenizer;

import mad.ass1.meetup.main.R;
import mad.ass1.meetup.main.R.layout;
import mad.ass1.meetup.main.R.menu;
import mad.ass2.meetup.asynctask.DelAsyncTask;
import mad.ass2.meetup.asynctask.RetrieveAsyncTask;
import mad.ass2.meetup.controller.EditButtonListener;
import mad.ass2.meetup.db.EventDBModel;
import mad.ass2.meetup.model.EventModel;
import mad.ass2.meetup.model.InterfaceEvent;
import mad.ass2.meetup.service.EventNotificationService;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/*
 * The activity class for viewing FULL details of the selected event.
 * In this activity, the user is able to edit and/or delete the event.
 */
public class SingleEventViewActivity extends Activity {
	private final static int singleEventView = 400;
	//String identifier of the class.
	public final static String SINGLE_EVENT_DETAILS = "mad.ass1.meetup.view.DETAILS";

	private TextView eventTitleText;
	private TextView eventVenueText;
	private TextView eventNoteText;
	private TextView eventDateTimeText;
	private TextView eventAttendeeText;
	private Button editEventButt;
	private Button deleteEventButt;

	private int listPosition;
	private InterfaceEvent event;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_single_event_view);

		//Retrieve the caller class identifier in order to carry out caller class specific functions.
		Intent intent = getIntent();

		listPosition = intent.getIntExtra(EventViewActivity.SELECTED_EVENT, 0);

		if(listPosition == 0)
		{
			listPosition = intent.getIntExtra(WeekViewActivity.SELECTED_EVENT, 0);
			
			if(listPosition == 0)
			{
				listPosition = intent.getIntExtra(EventNotificationService.serviceClassName, 0);
			}
		}

		//Execute the necessary AsyncTask.
		RetrieveAsyncTask retAsyncTask = new RetrieveAsyncTask(this, listPosition, singleEventView);
		retAsyncTask.execute();

		//Places the values of the event into their respective text labels/positions.
		eventTitleText = ((TextView) findViewById(R.id.eventTitleView));

		eventVenueText = ((TextView) findViewById(R.id.eventVenueView));

		eventNoteText = ((TextView) findViewById(R.id.eventNoteView));

		eventDateTimeText = ((TextView) findViewById(R.id.eventDateTimeView));

		eventAttendeeText = ((TextView) findViewById(R.id.eventAttendView));

		//Allow the text label to be scroll-able.
		eventAttendeeText.setMovementMethod(new ScrollingMovementMethod());


		//Initialize button functionalities.
		editEventButt = (Button) findViewById(R.id.editEventButton);
		editEventButt.setOnClickListener(new EditButtonListener(this, listPosition));

		deleteEventButt = (Button) findViewById(R.id.delEventButton);
		deleteEventButt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Builder alertDialog = new AlertDialog.Builder(SingleEventViewActivity.this).setTitle("DELETION")
						.setMessage("Are you sure that you want to delete this event?")
						.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								delEvent();
							}
						})
						.setNegativeButton("No", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) { 
								// do nothing
							}
						});

				alertDialog.show();
			}
		});
	}
	
	//Retrieve the RESULT from AsyncTask.
	public void giveList(InterfaceEvent event)
	{
		this.event = event;
		eventTitleText.setText(event.getEventTitle());
		eventVenueText.setText(event.getEventVenue());
		eventNoteText.setText(event.getEventNote());
		eventDateTimeText.setText(event.getEventDate() + " " + event.getEventStartTime() + " - " + event.getEventEndTime());
		if(event.getNumOfAttend() != 0)
		{
			eventAttendeeText.setText(event.getAttendees().get(0));
			for(int i = 1; i < event.getNumOfAttend(); i++)
			{
				eventAttendeeText.append("\n" + event.getAttendees().get(i));
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.single_event_view, menu);
		return true;
	}

	//Method is called when the user has selected to delete the current event.
	public void delEvent()
	{	
		DelAsyncTask delAsyncTask = new DelAsyncTask(this, event, singleEventView);
		delAsyncTask.execute();
	}

	//Retrieve the RESULT from AsyncTask.
	public void goBack()
	{
		Intent returnIntent = new Intent(this, EventViewActivity.class);

		startActivity(returnIntent);
	}
}
