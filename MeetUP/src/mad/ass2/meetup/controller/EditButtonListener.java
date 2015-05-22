/*
 * NAME: SIA WAI SUAN
 * STUDENT ID: S3308555
 * MOBILE APPLICATION DEVELOPMENT
 * ASSIGNMENT 2
 * FILE: EditButtonListener.java
 */
package mad.ass2.meetup.controller;

import mad.ass2.meetup.view.EditEventActivity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

/*
 * Listener responsible for activating the EditEventActivity class.
 */
public class EditButtonListener implements View.OnClickListener{

	private Context context;
	private int listPosition;
	private String SINGLE_EVENT_DETAILS = "mad.ass1.meetup.view.DETAILS";

	public EditButtonListener(Context context, int listPosition)
	{
		this.context = context;
		this.listPosition = listPosition;
	}

	@Override
	public void onClick(View v) {
		editEvent(v);
	}

	//Method is called when the user has selected to edit the current event.
	public void editEvent(View v)
	{
		Intent newIntent = new Intent(context, EditEventActivity.class);

		newIntent.putExtra(SINGLE_EVENT_DETAILS, listPosition);

		context.startActivity(newIntent);
	}
}
