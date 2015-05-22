/*
 * NAME: SIA WAI SUAN
 * STUDENT ID: S3308555
 * MOBILE APPLICATION DEVELOPMENT
 * ASSIGNMENT 2
 * FILE: NewButtonListener.java
 */
package mad.ass2.meetup.controller;

import mad.ass2.meetup.view.NewEventActivity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

/*
 * Listener responsible for activating the NewEventActivity class.
 */
public class NewButtonListener implements OnClickListener{

	private Context context;

	public NewButtonListener(Context context)
	{
		this.context = context;
	}

	@Override
	public void onClick(View v) {
		schedNewEvent(v);
	}

	//Navigate to the NEW EVENT activity.
	public void schedNewEvent(View view)
	{
		Intent intent = new Intent(context, NewEventActivity.class);
		context.startActivity(intent);
	}
}
