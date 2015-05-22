/*
 * NAME: SIA WAI SUAN
 * STUDENT ID: S3308555
 * MOBILE APPLICATION DEVELOPMENT
 * ASSIGNMENT 2
 * FILE: ListViewListener.java
 */
package mad.ass2.meetup.controller;

import mad.ass2.meetup.view.SingleEventViewActivity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

/*
 * Listener responsible for activating the SingleEventViewActivity class.
 */
public class ListViewListener implements OnItemClickListener{

	private Context context;
	private String SELECTED_EVENT = "mad.ass1.meetup.view.SELECTED";
	
	public ListViewListener(Context context)
	{
		this.context = context;
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		selectedEventView(view, position);
	}

	//Method is called when the user has clicked on a specific event on the list view.
	public void selectedEventView(View v, int position)
	{
		Intent newIntent = new Intent(context, SingleEventViewActivity.class);

		newIntent.putExtra(SELECTED_EVENT, position);

		context.startActivity(newIntent);
	}
}
