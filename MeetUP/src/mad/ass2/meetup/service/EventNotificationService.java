/*
 * NAME: SIA WAI SUAN
 * STUDENT ID: S3308555
 * MOBILE APPLICATION DEVELOPMENT
 * ASSIGNMENT 2
 * FILE: EventNotificationService.java
 */
package mad.ass2.meetup.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import mad.ass1.meetup.main.R;
import mad.ass2.meetup.db.EventDBModel;
import mad.ass2.meetup.main.MainActivity;
import mad.ass2.meetup.model.EventModel;
import mad.ass2.meetup.model.InterfaceEvent;
import mad.ass2.meetup.view.SingleEventViewActivity;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/*
 * Service class with the purpose of handling the NOTIFICATIONS of EVENTS when it is 15 MINUTES before the time of EVENT.
 */
public class EventNotificationService extends Service{

	public final static String serviceClassName = "mad.ass1.meetup.timer.EventNotificationService";
	public static boolean serviceStatus = false;

	@Override
	public void onCreate() {
		Toast.makeText(this, "NOTIFICATION SERVICE IS CREATED", Toast.LENGTH_LONG).show();
		Log.i("SERVICE", "ONCREATE");
	}

	@Override
	public void onStart(Intent intent, int startId) {
		Toast.makeText(this, "NOTIFICATION SERVICE IS STARTED", Toast.LENGTH_LONG).show();  
		Log.i("SERVICE", "ONSTART");
	}

	@Override
	public void onDestroy() {
		Toast.makeText(this, "NOTIFICATION SERVICE IS STOPPED", Toast.LENGTH_LONG).show();
		serviceStatus = false;
		EventDBModel.getDBInstance(this).close();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		Log.i("SERVICE", "ONSTARTCOMMAND");

		serviceStatus = true;

		//Open the DB consisting of a table holding EVENTS.
		EventDBModel.getDBInstance(this).open();

		//A background (extended) thread iterating through the EVENT LIST and notify user when necessary.
		ServiceThread sThread = new ServiceThread(this);
		sThread.start();

		//Toast.makeText(this, "Congrats! My Service Started", Toast.LENGTH_LONG).show();
		
		// We need to return if we want to handle this service explicitly. 
		return START_STICKY;
	}
}
