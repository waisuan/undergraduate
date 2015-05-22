/*
 * NAME: SIA WAI SUAN
 * STUDENT ID: S3308555
 * MOBILE APPLICATION DEVELOPMENT
 * ASSIGNMENT 2
 * FILE: EditAsyncTask.java
 */
package mad.ass2.meetup.asynctask;

import java.util.ArrayList;

import mad.ass2.meetup.constants.ClassName;
import mad.ass2.meetup.db.EventDBModel;
import mad.ass2.meetup.model.EventModel;
import mad.ass2.meetup.view.EditEventActivity;
import mad.ass2.meetup.view.NewEventActivity;
import android.content.Context;
import android.os.AsyncTask;

/*
 * EditAsyncTask is called when an Event needs to be edited in the DATA MODEL.
 */
public class EditAsyncTask extends AsyncTask<Void, Void, Void>{

	private Context context;
	private ArrayList<String> newEventDetails;
	private int whoIsCalling;
	
	public EditAsyncTask(Context context, ArrayList<String> newEventDetails, int whoIsCalling)
	{
		this.context = context;
		this.newEventDetails = newEventDetails;
		this.whoIsCalling = whoIsCalling;
	}
	
	@Override
	protected Void doInBackground(Void... unused) 
	{
		//Update EVENT object in the MODEL class.
		EventModel.getSingletonInstance().updateEvent(newEventDetails);
		
		return null;
	}
	
	@Override
	protected void onPostExecute(Void unused)
	{
		//Go back to caller activity when task is complete.
		if(whoIsCalling == ClassName.EDITEVENTVIEW)
		{
			((EditEventActivity)context).goBackToList(newEventDetails.get(0));
		}
	}
}
