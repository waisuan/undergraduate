/*
 * NAME: SIA WAI SUAN
 * STUDENT ID: S3308555
 * MOBILE APPLICATION DEVELOPMENT
 * ASSIGNMENT 2
 * FILE: ViewAsyncTask.java
 */
package mad.ass2.meetup.asynctask;

import java.util.ArrayList;
import java.util.Collections;

import mad.ass2.meetup.constants.ClassName;
import mad.ass2.meetup.db.EventDBModel;
import mad.ass2.meetup.model.EventModel;
import mad.ass2.meetup.model.InterfaceEvent;
import mad.ass2.meetup.view.EventViewActivity;
import android.content.Context;
import android.os.AsyncTask;

/*
 * AsyncTask is called when the entire EVENT LIST needs to be retrieved from the DATA MODEL.
 */
public class ViewAsyncTask extends AsyncTask<Void, Void, Void>{

	private Context context;
	private int whoIsCalling;
	private ArrayList<InterfaceEvent> eventList;
	
	public ViewAsyncTask(Context context, int whoIsCalling)
	{
		this.context = context;
		this.whoIsCalling = whoIsCalling;
	}
	
	@Override
	protected Void doInBackground(Void... unused) 
	{
		//Retrieve EVENT LIST from the MODEL class.
		eventList = EventModel.getSingletonInstance().getEventList();
		
		return null;
	}

	@Override
	protected void onPostExecute(Void unused)
	{
		//Go back to caller activity when task is complete.
		if(whoIsCalling == ClassName.EVENTVIEW)
		{
			((EventViewActivity)context).displayEvents(eventList);
		}
	}
}
