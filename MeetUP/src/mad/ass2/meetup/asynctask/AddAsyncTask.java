/*
 * NAME: SIA WAI SUAN
 * STUDENT ID: S3308555
 * MOBILE APPLICATION DEVELOPMENT
 * ASSIGNMENT 2
 * FILE: AddAsyncTask.java
 */
package mad.ass2.meetup.asynctask;

import java.util.ArrayList;

import mad.ass2.meetup.constants.ClassName;
import mad.ass2.meetup.db.EventDBModel;
import mad.ass2.meetup.model.EventModel;
import mad.ass2.meetup.view.NewEventActivity;

import android.content.Context;
import android.os.AsyncTask;

/*
 * AsyncTask is called when a NewEvent is created and needs to be added into the Event DATA MODEL.
 */
public class AddAsyncTask extends AsyncTask<Void, Void, Void>{

	private ArrayList<String> eventDetails;
	private Context context;
	private int whoIsCalling;

	public AddAsyncTask(Context context, ArrayList<String> eventDetails, int whoIsCalling)
	{
		this.context = context;
		this.eventDetails = eventDetails;
		this.whoIsCalling = whoIsCalling;
	}

	@Override
	protected Void doInBackground(Void... unused) 
	{	
		//Create EVENT in MODEL class.
		EventModel.getSingletonInstance().setEvent(eventDetails);

		return null;
	}

	@Override
	protected void onPostExecute(Void unused)
	{
		if(whoIsCalling == ClassName.NEWEVENTVIEW)
		{
			//Go back to caller activity when task is complete.
			((NewEventActivity)context).goBackToList(eventDetails.get(0));
		}
	}
}
