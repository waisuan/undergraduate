/*
 * NAME: SIA WAI SUAN
 * STUDENT ID: S3308555
 * MOBILE APPLICATION DEVELOPMENT
 * ASSIGNMENT 2
 * FILE: MainActivity.java
 */
package mad.ass2.meetup.main;

import mad.ass1.meetup.main.R;
import mad.ass2.meetup.controller.CalButtonListener;
import mad.ass2.meetup.controller.EventButtonListener;
import mad.ass2.meetup.controller.NewButtonListener;
import mad.ass2.meetup.controller.OverDBListener;
import mad.ass2.meetup.db.EventDBModel;
import mad.ass2.meetup.google.AuthenticationManager;
import mad.ass2.meetup.google.TaskExplorerHelper;
import mad.ass2.meetup.service.EventNotificationService;
import mad.ass2.meetup.service.QueueHandlerService;
import mad.ass2.meetup.service.QueueThread;
import mad.ass2.meetup.view.EventViewActivity;
import mad.ass2.meetup.view.NewEventActivity;
import mad.ass2.meetup.view.WeekViewActivity;
import android.os.Bundle;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/*
 * Main class/interface of the application. 
 * From here, the user is able to navigate towards the necessary functionalities of the application.
 */
@SuppressLint("SetJavaScriptEnabled")
public class MainActivity extends Activity {
	//Integer identifier of the class.
	public static final int mainCallView = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//If the AuthManager has not been started before, start it up. 
		//This will only be started ONE time throughout the life cycle of the program.
		//Firstly, a pop up option box is shown for the user to select the account to use for the app.
		if(AuthenticationManager.getSingletonInstance().getState() == false)
		{
			Intent accountIntent = android.accounts.AccountManager.newChooseAccountIntent(null, null, new String[] {
			"com.google"}, true, null, null, null, null);
			startActivityForResult(accountIntent, mainCallView);
		}

		//Necessary initializers of the buttons on the interface.
		View schedButton=findViewById(R.id.schedEventButton);
		schedButton.setOnClickListener(new NewButtonListener(this));

		View viewEventButton=findViewById(R.id.viewEventButton);
		viewEventButton.setOnClickListener(new EventButtonListener(this));

		View viewCalButton=findViewById(R.id.viewCalendarButton);
		viewCalButton.setOnClickListener(new CalButtonListener(this));

		View overrideDBButton = findViewById(R.id.overrideDB);
		overrideDBButton.setOnClickListener(new OverDBListener(this));

		//DO NOT REMOVE --  FOR FUTURE TESTING PURPOSES...
		//		View toggleButton = findViewById(R.id.toggler);
		//		toggleButton.setOnClickListener(new View.OnClickListener() {
		//
		//			@Override
		//			public void onClick(View v) { 
		//				if(toggler == false)
		//				{
		//					Toast.makeText(MainActivity.this, "ON", Toast.LENGTH_LONG).show(); 
		//					toggler = true;
		//				}
		//				else
		//				{
		//					Toast.makeText(MainActivity.this, "OFF", Toast.LENGTH_LONG).show(); 
		//					toggler = false;
		//				}
		//			}
		//		});
	}

	//If the user refuses to select an account, re-prompt the pop up box.
	public void reStartActivityResult()
	{
		Intent accountIntent = android.accounts.AccountManager.newChooseAccountIntent(null, null, new String[] {
		"com.google"}, true, null, null, null, null);
		startActivityForResult(accountIntent, mainCallView);
	}

	//Accepts the user account and sets up the AuthManager accordingly.
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

		if(resultCode == RESULT_OK)
		{
			if(intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME) != null)
			{
				Log.i("MAIN", intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME));

				AuthenticationManager.getSingletonInstance().setUp(this, intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME));
			}

			if(QueueThread.netStatus == true)
			{
				AuthenticationManager.getSingletonInstance().authenticate();
			}
		}
		else
		{
			reStartActivityResult();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	//Start the necessary services if they are not already started.
	@Override
	protected void onStart() {

		if(EventNotificationService.serviceStatus == false)
		{
			startService(new Intent(this, EventNotificationService.class));
		}

		if(QueueHandlerService.queueServiceStatus == false)
		{
			startService(new Intent(this, QueueHandlerService.class));
		}

		super.onStart();
	}
}
