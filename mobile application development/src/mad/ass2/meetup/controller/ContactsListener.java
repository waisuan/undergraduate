/*
 * NAME: SIA WAI SUAN
 * STUDENT ID: S3308555
 * MOBILE APPLICATION DEVELOPMENT
 * ASSIGNMENT 2
 * FILE: ContactsListener.java
 */
package mad.ass2.meetup.controller;

import mad.ass1.meetup.main.R;
import mad.ass2.meetup.constants.ClassName;
import mad.ass2.meetup.view.EditEventActivity;
import mad.ass2.meetup.view.NewEventActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/*
 * Listener responsible for bringing up the CONTACT PICKER.
 */
public class ContactsListener implements View.OnClickListener{

	private Context context;
	private int activityID;
	private int NEW_CONTACT_PICKER_RESULT = 1000;
	private int EDIT_CONTACT_PICKER_RESULT = 1001;

	public ContactsListener(Context context, int activityID)
	{
		this.context = context;
		this.activityID = activityID;
	}

	@Override
	public void onClick(View v) {
		doLaunchContactPicker(v);
	}

	//Method is called to activate the Contact Menu for user to choose a contact from.
	public void doLaunchContactPicker(View view) {  
		Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, Contacts.CONTENT_URI);
		if(activityID == ClassName.NEWEVENTVIEW)
		{
			((NewEventActivity)context).startActivityForResult(contactPickerIntent, NEW_CONTACT_PICKER_RESULT); 
		}
		else if(activityID == ClassName.EDITEVENTVIEW)
		{
			((EditEventActivity)context).startActivityForResult(contactPickerIntent, EDIT_CONTACT_PICKER_RESULT); 
		}
	}
}
