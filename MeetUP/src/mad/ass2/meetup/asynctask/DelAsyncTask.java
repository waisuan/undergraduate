/*
 * NAME: SIA WAI SUAN
 * STUDENT ID: S3308555
 * MOBILE APPLICATION DEVELOPMENT
 * ASSIGNMENT 2
 * FILE: DelAsyncTask.java
 */
package mad.ass2.meetup.asynctask;

import mad.ass2.meetup.constants.ClassName;
import mad.ass2.meetup.db.EventDBModel;
import mad.ass2.meetup.model.EventModel;
import mad.ass2.meetup.model.InterfaceEvent;
import mad.ass2.meetup.view.EventViewActivity;
import mad.ass2.meetup.view.SingleEventViewActivity;
import mad.ass2.meetup.view.WeekViewActivity;
import android.content.Context;
import android.os.AsyncTask;

/*
 * AsyncTask is called when an Event needs to be deleted from the DATA MODEL.
 */
public class DelAsyncTask extends AsyncTask<Void, Void, Void>{

	private InterfaceEvent event;
	private int eventIndex;
	private Context context;
	private int whoIsCalling;
	private String deletedEvent;

	public DelAsyncTask(Context context, InterfaceEvent event, int whoIsCalling)
	{
		this.context = context;
		this.event = event;
		this.whoIsCalling = whoIsCalling;
	}

	public DelAsyncTask(Context context, int eventIndex, int whoIsCalling)
	{
		this.context = context;
		this.eventIndex = eventIndex;
		this.whoIsCalling = whoIsCalling;
	}

	@Override
	protected Void doInBackground(Void... unused) 
	{
		if(whoIsCalling != ClassName.WEEKVIEW)
		{
			//Delete event in the MODEL class (by event).
			EventModel.getSingletonInstance().deleteEvent(event);
		}
		else
		{
			//Delete event in the MODEL class (by event index).
			deletedEvent = EventModel.getSingletonInstance().deleteEvent(eventIndex);
		}

		return null;
	}

	@Override
	protected void onPostExecute(Void unused)
	{
		//Go back to caller activity when task is complete.
		if(whoIsCalling == ClassName.SINGLEVIEW)
		{
			((SingleEventViewActivity)context).goBack();
		}
		else if(whoIsCalling == ClassName.EVENTVIEW)
		{
			((EventViewActivity)context).refreshCurrActivity(event.getEventTitle());
		}
		else if(whoIsCalling == ClassName.WEEKVIEW)
		{
			((WeekViewActivity)context).refreshCurrActivity(deletedEvent);
		}
	}
}
