/*
 * NAME: SIA WAI SUAN
 * STUDENT ID: S3308555
 * MOBILE APPLICATION DEVELOPMENT
 * ASSIGNMENT 2
 * FILE: EventViewActivity.java
 */
package mad.ass2.meetup.view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.UUID;

import org.json.JSONException;

import mad.ass1.meetup.main.R;
import mad.ass1.meetup.main.R.layout;
import mad.ass1.meetup.main.R.menu;
//import mad.ass1.meetup.model.Event;
import mad.ass2.meetup.asynctask.AddAsyncTask;
import mad.ass2.meetup.asynctask.DelAsyncTask;
import mad.ass2.meetup.asynctask.RetrieveAsyncTask;
import mad.ass2.meetup.asynctask.ViewAsyncTask;
import mad.ass2.meetup.controller.CalButtonListener;
import mad.ass2.meetup.controller.HomeButtonListener;
import mad.ass2.meetup.controller.ListViewListener;
import mad.ass2.meetup.db.EventDBModel;
import mad.ass2.meetup.main.MainActivity;
import mad.ass2.meetup.model.EventModel;
import mad.ass2.meetup.model.InterfaceEvent;
import mad.ass2.meetup.view.model.EventViewAdapter;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

/*
 * The activity class for viewing a list of created events in their SORTED order.
 * In this activity, the events can be viewed FULLY with a single click as well as
 * edited, deleted & added through a long press action.
 */
public class EventViewActivity extends Activity {
	private final static int eventViewActivity = 100;
	//String identifier of the class.
	public final static String SELECTED_EVENT = "mad.ass1.meetup.view.SELECTED";

	private ListView listView;
	private Button homeButton;
	private Button calButton;
	private TextView emptyText;

	private EventViewAdapter eva;

	private int callingActivity;
	private String callerDel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_view);

		listView = (ListView) findViewById(R.id.eventsView);

		//An appropriate text message is shown when the list view is empty.
		emptyText = (TextView)findViewById(android.R.id.empty);
		listView.setEmptyView(emptyText);

		//Retrieve the caller class identifier in order to carry out caller class specific functions.
		Intent intent = getIntent();
		
		//Execute the necessary AsyncTask.
		ViewAsyncTask viewAsyncTask = new ViewAsyncTask(this, eventViewActivity);
		viewAsyncTask.execute();

		homeButton = (Button) findViewById(R.id.homeButton);
		homeButton.setOnClickListener(new HomeButtonListener(this));

		calButton = (Button) findViewById(R.id.calViewButton);
		calButton.setOnClickListener(new CalButtonListener(this));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_event_view, menu);
		return true;
	}

	//The context menu is inflated with a custom layout as well as its options/items.
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.float_context_menu, menu);
	}

	//An appropriate action is done when the user has selected one of the two options on the menu.
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		final int position = info.position;

		switch (item.getItemId()) {
		case R.id.menu_edit:
			jumpToEditEvent(position);
			return true;
		case R.id.menu_add:
			jumpToAddEvent();
			return true;
		case R.id.menu_delete:

			//A helpful dialog box is shown to confirm the user's choice of deleting an event.
			Builder alertDialog = new AlertDialog.Builder(EventViewActivity.this).setTitle("DELETION")
			.setMessage("Are you sure that you want to delete this event?")
			.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					callDelTask(position);
				}
			})
			.setNegativeButton("No", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) { 
					// do nothing
				}
			});

			alertDialog.show();

			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	//Retrieve the RESULT from AsyncTask.
	public void displayEvents(ArrayList<InterfaceEvent> eventList)
	{
		//An appropriate dialog box is shown when the list view is empty.
		//This prompts the user to add a new event (optional).
		if(eventList.size() == 0)
		{
			Builder alertDialog = new AlertDialog.Builder(EventViewActivity.this).setTitle("TIP")
					.setMessage("There are no ongoing events right now. Do you want to add a new event?")
					.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							jumpToAddEvent();
						}
					})
					.setNegativeButton("No", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) { 
							// do nothing
						}
					});

			alertDialog.show();
		}

		//This activity is accompanied by a custom list view adapter in order to help show
		//the events in a better format within the list view.
		eva = new EventViewAdapter(this, eventList);

		listView.setAdapter(eva);

		//Contents of the list view is click-able for FULL viewing.
		listView.setOnItemClickListener(new ListViewListener(this));
		
		//With a long press action, a context menu will show up with EDIT/DELETE options.
		this.registerForContextMenu(listView);	

		Toast.makeText(getApplicationContext(), "Number of event(s): " + eventList.size(), Toast.LENGTH_LONG).show();
	}

	//Method is called when the user has selected to add a specific event from the context menu.
	public void jumpToAddEvent()
	{
		Intent newIntent = new Intent(this, NewEventActivity.class);
		startActivity(newIntent);
	}

	//Execute the necessary AsyncTask.
	public void callDelTask(int position)
	{
		DelAsyncTask delAsyncTask = new DelAsyncTask(this, (InterfaceEvent) listView.getItemAtPosition(position), eventViewActivity);
		delAsyncTask.execute();
	}

	//Method is called when the user has selected to delete a specific event from the context menu.
	public void refreshCurrActivity(String deletedEvent)
	{
		Toast.makeText(getApplicationContext(), "Deleted event: " + deletedEvent, Toast.LENGTH_LONG).show();

		Intent refreshIntent = new Intent(EventViewActivity.this, EventViewActivity.class);
		//Re-start the current activity with the UPDATED content.
		startActivity(refreshIntent);
	}

	//Method is called when the user has selected to edit a specific event from the context menu.
	public void jumpToEditEvent(int position)
	{
		Intent newIntent = new Intent(this, EditEventActivity.class);

		newIntent.putExtra(SELECTED_EVENT, position);

		startActivity(newIntent);
	}
}
