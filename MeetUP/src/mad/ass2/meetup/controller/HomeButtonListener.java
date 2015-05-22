/*
 * NAME: SIA WAI SUAN
 * STUDENT ID: S3308555
 * MOBILE APPLICATION DEVELOPMENT
 * ASSIGNMENT 2
 * FILE: HomeButtonListener.java
 */
package mad.ass2.meetup.controller;

/*
 * Listener responsible for activating the MainActivity class.
 */
import mad.ass2.meetup.main.MainActivity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class HomeButtonListener implements View.OnClickListener{

	private Context context;
	
	public HomeButtonListener(Context context)
	{
		this.context = context;
	}

	@Override 
	public void onClick(View v) {
		backToMain(v);
	}

	//A home button is provided to allow the user to navigate back to the main screen.
	public void backToMain(View v)
	{
		Intent returnIntent = new Intent(context, MainActivity.class);
		context.startActivity(returnIntent);
	}
}
