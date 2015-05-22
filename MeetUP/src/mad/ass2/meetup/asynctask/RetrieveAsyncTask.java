/*
 * NAME: SIA WAI SUAN
 * STUDENT ID: S3308555
 * MOBILE APPLICATION DEVELOPMENT
 * ASSIGNMENT 2
 * FILE: RetrieveAsyncTask.java
 */
package mad.ass2.meetup.asynctask;

import java.util.ArrayList;

import mad.ass2.meetup.constants.ClassName;
import mad.ass2.meetup.db.EventDBModel;
import mad.ass2.meetup.model.EventModel;
import mad.ass2.meetup.model.InterfaceEvent;
import mad.ass2.meetup.view.EditEventActivity;
import mad.ass2.meetup.view.NewEventActivity;
import mad.ass2.meetup.view.SingleEventViewActivity;
import mad.ass2.meetup.view.WeekViewActivity;
import android.content.Context;
import android.os.AsyncTask;

/*
 * AsyncTask is called when a specific EVENT(LIST) needs to be retrieved from the DATA MODEL.
 */
public class RetrieveAsyncTask extends AsyncTask<Void, Void, Void>{

	private ArrayList<InterfaceEvent> eventList;
	private InterfaceEvent event;
	private Context context;
	private int whoIsCalling;
	private int listPosition;

	public RetrieveAsyncTask(Context context, int listPosition, int whoIsCalling)
	{
		this.context = context;
		this.listPosition = listPosition;
		this.whoIsCalling = whoIsCalling;
	}

	@Override
	protected Void doInBackground(Void... unused) 
	{	
		//Either retrieve a specific EVENT or the entire LIST according to who is calling.
		if(whoIsCalling == ClassName.EDITEVENTVIEW || whoIsCalling == ClassName.SINGLEVIEW)
		{
			event = EventModel.getSingletonInstance().getEventList().get(listPosition);
		}
		else
		{
			eventList = EventModel.getSingletonInstance().getEventList();
		}

		return null;
	}

	@Override
	protected void onPostExecute(Void unused)
	{
		//Go back to caller activity when task is complete.
		if(whoIsCalling == ClassName.EDITEVENTVIEW)
		{
			((EditEventActivity)context).giveList(event);
		}
		else if(whoIsCalling == ClassName.SINGLEVIEW)
		{
			((SingleEventViewActivity)context).giveList(event);
		}
		else if(whoIsCalling == ClassName.WEEKVIEW)
		{
			((WeekViewActivity)context).giveList(eventList);
		}
	}

}
