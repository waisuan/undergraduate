/*
 * NAME: SIA WAI SUAN
 * STUDENT ID: S3308555
 * MOBILE APPLICATION DEVELOPMENT
 * ASSIGNMENT 2
 * FILE: WeekViewActivity.java
 */
package mad.ass2.meetup.view;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.StringTokenizer;

import mad.ass1.meetup.main.R;
import mad.ass1.meetup.main.R.layout;
import mad.ass1.meetup.main.R.menu;
//import mad.ass1.meetup.model.Event;
import mad.ass2.meetup.asynctask.DelAsyncTask;
import mad.ass2.meetup.asynctask.RetrieveAsyncTask;
import mad.ass2.meetup.controller.EventButtonListener;
import mad.ass2.meetup.controller.HomeButtonListener;
import mad.ass2.meetup.db.EventDBModel;
import mad.ass2.meetup.main.MainActivity;
import mad.ass2.meetup.model.EventModel;
import mad.ass2.meetup.model.InterfaceEvent;
import mad.ass2.meetup.view.model.EventViewAdapter;
//import mad.ass1.meetup.view.model.WeekViewAdapter;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

/*
 * The activity class for viewing the CALENDAR form of the events.
 * Calendar is in the form of a WEEKLY VIEW.
 * Note that the current week is DYNAMIC.
 * In this activity, the user is able to edit, delete and/or add event(s).
 */
public class WeekViewActivity extends Activity {
	private final static int weekViewActivity = 500;
	//String identifier(s) of the class.
	public final static String SELECTED_EVENT = "mad.ass1.meetup.view.SELECTED";
	public final static String EMPTY_CELL = "mad.ass1.meetup.view.EMPTY_CELL";

	private ArrayList<InterfaceEvent> eventList;

	private Button tableHomeButton;
	private Button eveViewButton;
	private TableLayout tl2;
	private TableLayout tl1;
	private TableRow tr;
	private TableRow tr3;
	private LayoutInflater inflater;
	private TextView tvTitle;
	private TextView tvValue1;
	private TextView tvValue2;
	private TextView tvValue3;
	private TextView tvValue4;
	private TextView tvValue5;
	private TextView tvValue6;
	private TextView tvValue7;
	private int[] dayTableRow = {R.id.day1, R.id.day2, R.id.day3, R.id.day4, R.id.day5, R.id.day6, R.id.day7};
	private int[] dummyRow2 = {R.id.dummyText10, R.id.dummyText11, R.id.dummyText12, R.id.dummyText13, R.id.dummyText14, R.id.dummyText15, R.id.dummyText16};
	private int[] tableRow = {R.id.tableTextView2, R.id.tableTextView3, R.id.tableTextView4, R.id.tableTextView5, R.id.tableTextView6, R.id.tableTextView7, R.id.tableTextView8};
	private ArrayList<TextView> eventCells;
	private ArrayList<Integer> thisWeekEvents;

	private Calendar cal; 
	private ArrayList<String> dTokens = new ArrayList<String>();
	private ArrayList<Integer> thisWeekDate = new ArrayList<Integer>();
	private ArrayList<String> thisWeekDay = new ArrayList<String>();
	private String thisMonth;
	private int thisYear;
	private String[] clockTime = {"12am", "", "1", "", "2", "", "3", "", "4", "", "5", "", "6", "", "7", "", "8", "", "9", "", "10", "", "11", "", "12pm", "", "1", "", "2", "", "3", "", "4", "", "5", "", "6", "", "7", "", "8", "", "9", "", "10", "", "11", ""};
	private String[] twelveHours = {"12:00", "12:30", "1:00", "1:30", "2:00", "2:30", "3:00", "3:30", "4:00", "4:30", "5:00", "5:30", "6:00", "6:30", "7:00", "7:30", "8:00", "8:30", "9:00", "9:30", "10:00", "10:30", "11:00", "11:30"};
	private HashMap<String, Integer> amTime = new HashMap<String, Integer>();
	private HashMap<String, Integer> pmTime = new HashMap<String, Integer>();
	private int NUM_OF_DAYS = 7;
	private int i, j;
	private int counter = 1;
	private int column;
	private boolean found = false;
	private boolean stillValid = true;
	private int startRow;
	private int endRow;
	private int rowCount = 1;
	private int colCount = 1;
	private int indexOfFullCell;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_week_view);
		
		//Execute the necessary AsyncTask.
		RetrieveAsyncTask retAsyncTask = new RetrieveAsyncTask(this, 0, weekViewActivity);
		retAsyncTask.execute();

		//Initialize two hash maps that store AM and PM times respective to their rows on the table.
		for(i = 0; i < twelveHours.length; i++)
		{
			amTime.put(twelveHours[i], i+1);
			pmTime.put(twelveHours[i], i+25);
		}

		updateHeaderRow();

		updateTableRow();

		//Allow the user to return to the main interface.
		tableHomeButton = (Button) findViewById(R.id.tableHomeButton);
		tableHomeButton.setOnClickListener(new HomeButtonListener(this));

		eveViewButton = (Button) findViewById(R.id.eveViewButton);
		eveViewButton.setOnClickListener(new EventButtonListener(this));
		
	}
	
	//Retrieve the RESULT from AsyncTask.
	public void giveList(ArrayList<InterfaceEvent> eventList)
	{
		this.eventList = eventList;
		
		Toast.makeText(getApplicationContext(), "Number of event(s): " + eventList.size(), Toast.LENGTH_LONG).show();

		processTableEvents();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.week_view, menu);
		return true;
	}

	//Method is called to initialize the table header with the current days of the week.
	public void updateHeaderRow()
	{
		cal = Calendar.getInstance();

		tl1 = (TableLayout) findViewById(R.id.tableLayout1);
		tl2 = (TableLayout) findViewById(R.id.tableLayout2);

		tr = (TableRow) tl1.getChildAt(0);
		tr3 = (TableRow) tl2.getChildAt(0);

		StringTokenizer st = new StringTokenizer(cal.getTime().toString(), " ");

		while(st.hasMoreTokens()) { 
			dTokens.add((String) st.nextToken()); 
		}

		//Print the CURRENT MONTH and YEAR.
		tvValue1 = (TextView) tr.findViewById(R.id.emptyCell);
		//Print the CURRENT DAY.
		tvValue3 = (TextView) tr3.findViewById(R.id.dummyText9);

		tvValue1.setText(dTokens.get(1) + " " + dTokens.get(5));
		tvValue3.setText(dTokens.get(1) + " " + dTokens.get(5));

		thisMonth = dTokens.get(1);
		thisYear = Integer.parseInt(dTokens.get(5));

		//Print the rest of the days of the week.
		for(i = 0; i < NUM_OF_DAYS; i++)
		{
			tvValue1 = (TextView) tr.findViewById(dayTableRow[i]);

			tvValue3 = (TextView) tr3.findViewById(dummyRow2[i]);

			tvValue1.setText(dTokens.get(0) + " " + dTokens.get(2));

			tvValue3.setText(dTokens.get(0) + " " + dTokens.get(2));

			thisWeekDay.add(dTokens.get(0));
			thisWeekDate.add(Integer.parseInt(dTokens.get(2)));

			cal.add(Calendar.DATE, counter);

			dTokens.clear();

			st = new StringTokenizer(cal.getTime().toString(), " ");

			while(st.hasMoreTokens()) { 
				dTokens.add((String) st.nextToken()); 
			}
		}

	}

	//Method is called to set up the rest of the cells within the table.
	public void updateTableRow()
	{
		tl2 = (TableLayout) findViewById(R.id.tableLayout2);

		inflater = getLayoutInflater();

		//Inflate the first column with TIME strings.
		for(i = 0; i < clockTime.length; i++)
		{
			colCount = 1;

			tr = (TableRow)inflater.inflate(R.layout.table_textview, tl2, false);

			tvTitle = (TextView)tr.findViewById(R.id.tableTextView1);
			tvTitle.setText(clockTime[i]);

			//Set each empty cell with the Long Click Listener.
			//Set the tag for each empty cell for future manipulation.
			tvValue1 = (TextView)tr.findViewById(R.id.tableTextView2);
			tvValue1.setText("");
			tvValue1.setTag("empty" + " " + String.valueOf(rowCount) + " " + String.valueOf(colCount));

			colCount++;

			tvValue1.setOnLongClickListener(ocl);

			tvValue2 = (TextView)tr.findViewById(R.id.tableTextView3);
			tvValue2.setText("");
			tvValue2.setTag("empty" + " " + String.valueOf(rowCount) + " " + String.valueOf(colCount));

			colCount++;

			tvValue2.setOnLongClickListener(ocl);

			tvValue3 = (TextView)tr.findViewById(R.id.tableTextView4);
			tvValue3.setText("");
			tvValue3.setTag("empty" + " " + String.valueOf(rowCount) + " " + String.valueOf(colCount));

			colCount++;

			tvValue3.setOnLongClickListener(ocl);

			tvValue4 = (TextView)tr.findViewById(R.id.tableTextView5);
			tvValue4.setText("");
			tvValue4.setTag("empty" + " " + String.valueOf(rowCount) + " " + String.valueOf(colCount));

			colCount++;

			tvValue4.setOnLongClickListener(ocl);

			tvValue5 = (TextView)tr.findViewById(R.id.tableTextView6);
			tvValue5.setText("");
			tvValue5.setTag("empty" + " " + String.valueOf(rowCount) + " " + String.valueOf(colCount));

			colCount++;

			tvValue5.setOnLongClickListener(ocl);

			tvValue6 = (TextView)tr.findViewById(R.id.tableTextView7);
			tvValue6.setText("");
			tvValue6.setTag("empty" + " " + String.valueOf(rowCount) + " " + String.valueOf(colCount));

			colCount++;

			tvValue6.setOnLongClickListener(ocl);

			tvValue7 = (TextView)tr.findViewById(R.id.tableTextView8);
			tvValue7.setText("");
			tvValue7.setTag("empty" + " " + String.valueOf(rowCount) + " " + String.valueOf(colCount));

			colCount++;

			tvValue7.setOnLongClickListener(ocl);

			tl2.addView(tr);

			rowCount++;
		}	
	}

	//Implementation of the Long Click Listener.
	//This should allow the user to add an event on an empty cell.
	OnLongClickListener ocl = new OnLongClickListener(){

		@Override
		public boolean onLongClick(View v) {

			StringTokenizer st = new StringTokenizer((String) v.getTag(), " ");
			String cellState = st.nextToken();

			//Only works if cell is empty.
			//Non-empty cell is handled by the Context Menu.
			if(cellState.equals("empty"))
			{
				int cellRow = Integer.parseInt(st.nextToken());
				int cellCol = Integer.parseInt(st.nextToken());
				String fromTime = null;
				String newDate = null;
				ArrayList<String> newEventDetails = new ArrayList<String>();

				if(amTime.containsValue(cellRow))
				{
					if(cellRow > 24)
					{
						cellRow -= 24;
					}

					fromTime = twelveHours[cellRow-1] + " AM";
					newDate = thisMonth + " " + thisWeekDate.get(cellCol-1) + ", " + String.valueOf(thisYear);
					newEventDetails.add(twelveHours[cellRow-1]);
					newEventDetails.add("AM");
				}
				else if(pmTime.containsValue(cellRow))
				{
					if(cellRow > 24)
					{
						cellRow -= 24;
					}

					fromTime = twelveHours[cellRow-1] + " PM";
					newDate = thisMonth + " " + thisWeekDate.get(cellCol-1) + ", " + String.valueOf(thisYear);
					newEventDetails.add(twelveHours[cellRow-1]);
					newEventDetails.add("PM");
				}

				newEventDetails.add(newDate);
				newEventDetails.add(fromTime);

				//Pass the EMPTY cell details to the Add Event activity for processing.
				Intent newIntent = new Intent(WeekViewActivity.this, NewEventActivity.class);

				newIntent.putStringArrayListExtra(EMPTY_CELL, newEventDetails);

				startActivity(newIntent);

				return true;
			}

			return false;
		}

	};

	//Context Menu is started IF the cell is not empty. i.e. There's an existing event.
	//Context Menu is mainly used for editing and deleting events.
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {

		super.onCreateContextMenu(menu, v, menuInfo);

		StringTokenizer st = new StringTokenizer((String) v.getTag(), " ");
		st.nextToken();

		indexOfFullCell = Integer.parseInt(st.nextToken());

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.table_context_menu, menu);
	}

	//Once the user has selected a particular option on the context menu, do the appropriate action.
	@Override
	public boolean onContextItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.table_menu_edit:
			Intent editIntent = new Intent(WeekViewActivity.this, EditEventActivity.class);
			editIntent.putExtra(SELECTED_EVENT, indexOfFullCell);
			//Go to Edit Event activity.
			startActivity(editIntent);
			return true;
		case R.id.table_menu_delete:
			Builder alertDialog = new AlertDialog.Builder(WeekViewActivity.this).setTitle("DELETION")
			.setMessage("Are you sure that you want to delete this event?")
			.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					callDelTask(indexOfFullCell);
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
	
	public void callDelTask(int position)
	{	
		DelAsyncTask delAsyncTask = new DelAsyncTask(this, position, weekViewActivity);
		delAsyncTask.execute();
	}
	
	public void refreshCurrActivity(String deletedEvent)
	{
		Toast.makeText(getApplicationContext(), "Deleted event: " + deletedEvent, Toast.LENGTH_LONG).show();
		
		Intent refreshIntent = new Intent(WeekViewActivity.this, WeekViewActivity.class);
		//Re-start the current activity with the UPDATED content.
		startActivity(refreshIntent);
	}

	//Once the table is created, the activity will then check to see if any events exist.
	//If an event(s) exists, update the table to relate to the particular event(s) such that
	//the highlighted cells would be in accordance to the event.
	public void processTableEvents()
	{
		if(eventList.size() != 0)
		{
			tl2 = (TableLayout) findViewById(R.id.tableLayout2);

			eventCells = new ArrayList<TextView>();
			thisWeekEvents = new ArrayList<Integer>();

			StringTokenizer st;
			ArrayList<String> eDateTokens = new ArrayList<String>();
			ArrayList<String> eStartTokens = new ArrayList<String>();
			ArrayList<String> eEndTokens = new ArrayList<String>();
			ArrayList<String> sTimeTokens = new ArrayList<String>();
			ArrayList<String> eTimeTokens = new ArrayList<String>();

			//Traverse through the list of events, match with the corresponding date/day of the week
			//and highlight the appropriate cells on the table.
			for(i = 0; i < eventList.size(); i++)
			{
				found = false;
				stillValid = true;

				st = new StringTokenizer(eventList.get(i).getEventDate(), ", ");
				while(st.hasMoreTokens())
				{
					eDateTokens.add((String) st.nextToken());
				}

				//If event year/month matches CURRENT year/month.
				if(thisYear == Integer.parseInt(eDateTokens.get(2)) && thisMonth.equals(eDateTokens.get(0)))
				{
					for(j = 0; j < thisWeekDate.size(); j++)
					{
						//If event day/date matches any day/date of the CURRENT week.
						if(thisWeekDate.get(j) == Integer.parseInt(eDateTokens.get(1)))
						{
							column = j;
							found = true;
							break;
						}
					}

					if(found == true)
					{
						st = new StringTokenizer(eventList.get(i).getEventStartTime(), " ");
						while(st.hasMoreTokens())
						{
							eStartTokens.add((String) st.nextToken());
						}

						//START TIME is handled/processed first.
						if(eStartTokens.get(1).equals("AM"))
						{
							st = new StringTokenizer(eStartTokens.get(0), ":");
							while(st.hasMoreTokens())
							{
								sTimeTokens.add((String) st.nextToken());
							}

							if(amTime.containsKey(eStartTokens.get(0)) != true)
							{
								//Round off the minutes into either 00 or 30.
								if(Integer.parseInt(sTimeTokens.get(1)) > 0 && Integer.parseInt(sTimeTokens.get(1)) > 30)
								{
									sTimeTokens.set(0, String.valueOf(Integer.parseInt(sTimeTokens.get(0))+1));
									sTimeTokens.set(1, "00");

									if(sTimeTokens.get(0).equals("12"))
									{
										stillValid = false;
									}
									else if(Integer.parseInt(sTimeTokens.get(0)) > 12)
									{
										sTimeTokens.set(0, String.valueOf(Integer.parseInt(sTimeTokens.get(0))-12));
									}
								}
								else if(Integer.parseInt(sTimeTokens.get(1)) > 0 && Integer.parseInt(sTimeTokens.get(1)) < 30)
								{
									sTimeTokens.set(1, "30");
								}

								eStartTokens.set(0, sTimeTokens.get(0) + ":" + sTimeTokens.get(1));
							}

							if(stillValid == true)
							{
								startRow = amTime.get(eStartTokens.get(0));
							}
							else
							{
								startRow = pmTime.get(eStartTokens.get(0));
								stillValid = true;
							}

							//Select the appropriate table row/column and highlight it.
							tr = (TableRow) tl2.getChildAt(startRow);

							tvValue1 = (TextView) tr.findViewById(tableRow[column]);

							tvValue1.setBackgroundColor(Color.CYAN);

							tvValue1.setText("S");

							tvValue1.setTag("full " + String.valueOf(i));

							registerForContextMenu(tvValue1);

							eventCells.add(tvValue1);
							thisWeekEvents.add(i);

						}
						else if(eStartTokens.get(1).equals("PM"))
						{
							st = new StringTokenizer(eStartTokens.get(0), ":");
							while(st.hasMoreTokens())
							{
								sTimeTokens.add((String) st.nextToken());
							}

							if(pmTime.containsKey(eStartTokens.get(0)) != true)
							{
								//Round off the minutes into either 00 or 30.
								if(Integer.parseInt(sTimeTokens.get(1)) > 0 && Integer.parseInt(sTimeTokens.get(1)) > 30)
								{
									//Do not round off if hour is 11.
									if(!(sTimeTokens.get(0).equals("11")))
									{
										sTimeTokens.set(0, String.valueOf(Integer.parseInt(sTimeTokens.get(0))+1));
										sTimeTokens.set(1, "00");

										if(Integer.parseInt(sTimeTokens.get(0)) > 12)
										{
											sTimeTokens.set(0, String.valueOf(Integer.parseInt(sTimeTokens.get(0))-12));
										}
									}
									else
									{
										sTimeTokens.set(1, "30");
									}
								}
								else if(Integer.parseInt(sTimeTokens.get(1)) > 0 && Integer.parseInt(sTimeTokens.get(1)) < 30)
								{
									sTimeTokens.set(1, "30");
								}

								eStartTokens.set(0, sTimeTokens.get(0) + ":" + sTimeTokens.get(1));
							}

							startRow = pmTime.get(eStartTokens.get(0));

							//Select the appropriate table row/column and highlight it.
							tr = (TableRow) tl2.getChildAt(startRow);

							tvValue1 = (TextView) tr.findViewById(tableRow[column]);

							tvValue1.setBackgroundColor(Color.CYAN);

							tvValue1.setText("S");

							tvValue1.setTag("full " + String.valueOf(i));

							registerForContextMenu(tvValue1);

							eventCells.add(tvValue1);
							thisWeekEvents.add(i);
						}

						//Handle END TIME.
						st = new StringTokenizer(eventList.get(i).getEventEndTime(), " ");
						while(st.hasMoreTokens())
						{
							eEndTokens.add((String) st.nextToken());
						}

						if(eEndTokens.get(1).equals("AM"))
						{
							if(amTime.containsKey(eEndTokens.get(0)) != true)
							{
								st = new StringTokenizer(eEndTokens.get(0), ":");
								while(st.hasMoreTokens())
								{
									eTimeTokens.add((String) st.nextToken());
								}

								//Round off the minutes into either 00 or 30.
								if(Integer.parseInt(eTimeTokens.get(1)) > 0 && Integer.parseInt(eTimeTokens.get(1)) > 30)
								{
									eTimeTokens.set(0, String.valueOf(Integer.parseInt(eTimeTokens.get(0))+1));
									eTimeTokens.set(1, "00");

									if(eTimeTokens.get(0).equals("12"))
									{
										stillValid = false;
									}
									else if(Integer.parseInt(eTimeTokens.get(0)) > 12)
									{
										eTimeTokens.set(0, String.valueOf(Integer.parseInt(eTimeTokens.get(0))-12));
									}
								}
								else if(Integer.parseInt(eTimeTokens.get(1)) > 0 && Integer.parseInt(eTimeTokens.get(1)) < 30)
								{
									eTimeTokens.set(1, "30");
								}

								eEndTokens.set(0, eTimeTokens.get(0) + ":" + eTimeTokens.get(1));
							}

							if(stillValid == true)
							{
								endRow = amTime.get(eEndTokens.get(0));
							}
							else
							{
								endRow = pmTime.get(eEndTokens.get(0));
								stillValid = true;
							}

							//Select the appropriate table row/column and highlight it.
							tr = (TableRow) tl2.getChildAt(endRow);

							tvValue1 = (TextView) tr.findViewById(tableRow[column]);

							tvValue1.setBackgroundColor(Color.CYAN);

							tvValue1.setText("E");

							tvValue1.setTag("full " + String.valueOf(i));

							registerForContextMenu(tvValue1);

							eventCells.add(tvValue1);
							thisWeekEvents.add(i);
						}
						else if(eEndTokens.get(1).equals("PM"))
						{
							if(pmTime.containsKey(eEndTokens.get(0)) != true)
							{
								st = new StringTokenizer(eEndTokens.get(0), ":");
								while(st.hasMoreTokens())
								{
									eTimeTokens.add((String) st.nextToken());
								}

								//Round off the minutes into either 00 or 30.
								if(Integer.parseInt(eTimeTokens.get(1)) > 0 && Integer.parseInt(eTimeTokens.get(1)) > 30)
								{
									//Do not round off if hour is 11.
									if(!(eTimeTokens.get(0).equals("11")))
									{
										eTimeTokens.set(0, String.valueOf(Integer.parseInt(eTimeTokens.get(0))+1));
										eTimeTokens.set(1, "00");

										if(Integer.parseInt(eTimeTokens.get(0)) > 12)
										{
											eTimeTokens.set(0, String.valueOf(Integer.parseInt(eTimeTokens.get(0))-12));
										}
									}
									else
									{
										eTimeTokens.set(1, "30");
									}
								}
								else if(Integer.parseInt(eTimeTokens.get(1)) > 0 && Integer.parseInt(eTimeTokens.get(1)) < 30)
								{
									eTimeTokens.set(1, "30");
								}

								eEndTokens.set(0, eTimeTokens.get(0) + ":" + eTimeTokens.get(1));
							}

							endRow = pmTime.get(eEndTokens.get(0));

							//Select the appropriate table row/column and highlight it.
							tr = (TableRow) tl2.getChildAt(endRow);

							tvValue1 = (TextView) tr.findViewById(tableRow[column]);

							tvValue1.setBackgroundColor(Color.CYAN);

							//If START TIME ends up to be the same as END TIME, label as follows.
							if(sTimeTokens.get(0).equals("11") && sTimeTokens.get(1).equals("30") && eStartTokens.get(1).equals("PM"))
							{
								tvValue1.setText("S/E");
							}
							else
							{
								tvValue1.setText("E");

								tvValue1.setTag("full " + String.valueOf(i));

								registerForContextMenu(tvValue1);

								eventCells.add(tvValue1);
								thisWeekEvents.add(i);
							}
						}

						//Highlight the cells in between the START TIME and END TIME.
						if((endRow - startRow) > 0)
						{
							for(j = startRow + 1; j < endRow; j++)
							{
								tr = (TableRow) tl2.getChildAt(j);

								tvValue1 = (TextView) tr.findViewById(tableRow[column]);

								tvValue1.setBackgroundColor(Color.CYAN);

								tvValue1.setTag("full " + String.valueOf(i));

								registerForContextMenu(tvValue1);

								eventCells.add(tvValue1);
								thisWeekEvents.add(i);
							}
						}
					}
				}

				eDateTokens.clear();
				eStartTokens.clear();
				eEndTokens.clear();
				sTimeTokens.clear();
				eTimeTokens.clear();
			}

			//For each HIGHLIGHTED cell, set the listener for on click action.
			for(i = 0; i < eventCells.size(); i++)
			{
				eventCells.get(i).setOnClickListener(new View.OnClickListener() {
					public void onClick(View v)
					{
						StringTokenizer st = new StringTokenizer((String) v.getTag(), " ");
						st.nextToken();

						final int k = Integer.parseInt(st.nextToken());

						Builder alertDialog = new AlertDialog.Builder(WeekViewActivity.this).setTitle("Event")
								.setMessage(eventList.get(k).getEventTitle() + "\n" 
										+ eventList.get(k).getEventDate() + " " + eventList.get(k).getEventStartTime() + " - " + eventList.get(k).getEventEndTime() + "\n"
										+ String.valueOf(eventList.get(k).getNumOfAttend()))
										.setPositiveButton("Full View", new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface dialog, int which) {
												goToFullView(k);
											}
										})
										.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface dialog, int which) { 
												// do nothing
											}
										});
						alertDialog.show();
					}
				});
			}
		}
	}

	//If user selects the FULL VIEW option from the dialog box, go to FULL VIEW activity.
	public void goToFullView(int position)
	{
		Intent newIntent = new Intent(this, SingleEventViewActivity.class);
		newIntent.putExtra(SELECTED_EVENT, position);

		startActivity(newIntent);
	}
}
