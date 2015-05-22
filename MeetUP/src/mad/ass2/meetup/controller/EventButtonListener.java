/*
 * NAME: SIA WAI SUAN
 * STUDENT ID: S3308555
 * MOBILE APPLICATION DEVELOPMENT
 * ASSIGNMENT 2
 * FILE: EventButtonListener.java
 */
package mad.ass2.meetup.controller;

import mad.ass2.meetup.view.EventViewActivity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

/*
 * Listener responsible for activating the EventViewActivity class.
 */
public class EventButtonListener implements OnClickListener{

	public int mainCallView = 1;
	private Context context;
	
	public EventButtonListener(Context context)
	{
		this.context = context;
	}
	
	@Override
	public void onClick(View v) {
		viewEvent(v);
	}

	//Navigate to the EVENT VIEW activity.
	public void viewEvent(View view)
	{
		Intent intent = new Intent(context, EventViewActivity.class);
		intent.putExtra("CALLER_MAIN", mainCallView);
		context.startActivity(intent);
	}
}
