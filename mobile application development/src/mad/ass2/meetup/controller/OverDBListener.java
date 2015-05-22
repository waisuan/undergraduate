/*
 * NAME: SIA WAI SUAN
 * STUDENT ID: S3308555
 * MOBILE APPLICATION DEVELOPMENT
 * ASSIGNMENT 2
 * FILE: OverDBListener.java
 */
package mad.ass2.meetup.controller;

import mad.ass2.meetup.google.TaskExplorerHelper;
import mad.ass2.meetup.model.EventModel;
import mad.ass2.meetup.service.QueueThread;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

/*
 * Listener responsible for initiating the OVERRIDE of local data (IF THERE IS NETWORK CONNECTION).
 */
public class OverDBListener implements View.OnClickListener{

	private Context context;
	
	public OverDBListener(Context context)
	{
		this.context = context;
	}

	@Override
	public void onClick(View v) {
		if(QueueThread.netStatus == false)
		{
			Toast.makeText(context, "NETWORK CONNECTION IS DOWN. OVERRIDE FAILED.", Toast.LENGTH_LONG).show();
		}
		else
		{
			EventModel.getSingletonInstance().initOverride();
			Toast.makeText(context, "NETWORK CONNECTION IS UP. OVERRIDE SUCCESS.", Toast.LENGTH_LONG).show();
		}
	}
}
