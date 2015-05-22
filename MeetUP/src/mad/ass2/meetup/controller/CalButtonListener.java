/*
 * NAME: SIA WAI SUAN
 * STUDENT ID: S3308555
 * MOBILE APPLICATION DEVELOPMENT
 * ASSIGNMENT 2
 * FILE: CalButtonListener.java
 */
package mad.ass2.meetup.controller;

import mad.ass2.meetup.view.WeekViewActivity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

/*
 * Listener responsible for activating the WeekViewActivity class.
 */
public class CalButtonListener implements View.OnClickListener{

	private Context context;
	
	public CalButtonListener(Context context)
	{
		this.context = context;
	}
	
	@Override
	public void onClick(View v) {
		goToCal(v);
	}

	//Allows the user to view calendar.
	public void goToCal(View v)
	{
		Intent returnIntent = new Intent(context, WeekViewActivity.class);
		context.startActivity(returnIntent);
	}
}
