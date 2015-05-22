/*
 * NAME: SIA WAI SUAN
 * STUDENT ID: S3308555
 * MOBILE APPLICATION DEVELOPMENT
 * ASSIGNMENT 2
 * FILE: QueueHandlerService.java
 */
package mad.ass2.meetup.service;

import mad.ass2.meetup.db.EventDBModel;
import mad.ass2.meetup.db.TaskDBModel;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/*
 * Service class with the purpose of handling the TASKS that are in QUEUE when NETWORK CONNECTIVITY is DOWN.
 */
public class QueueHandlerService extends Service{

	public final static String qServiceClassName = "mad.ass2.meetup.google.QueueHandlerService";
	public static boolean queueServiceStatus = false;
	
	@Override
	public void onCreate() {
		Toast.makeText(this, "QUEUE SERVICE IS CREATED", Toast.LENGTH_LONG).show();
		Log.i("SERVICE", "ONCREATE");
	}

	@Override
	public void onStart(Intent intent, int startId) {
		Toast.makeText(this, "QUEUE SERVICE IS STARTED", Toast.LENGTH_LONG).show();  
		Log.i("SERVICE", "ONSTART");
	}

	@Override
	public void onDestroy() {
		Toast.makeText(this, "QUEUE SERVICE IS STOPPED", Toast.LENGTH_LONG).show();
		TaskDBModel.getDBInstance(this).close();
		queueServiceStatus = false;
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		Log.i("QUEUE_SERVICE", "ONSTARTCOMMAND");

		queueServiceStatus = true;
		
		//Open the DB consisting of a table holding TASKS that were not handled yet.
		//Retrieve TASKS from the DB (if there are any).
		TaskDBModel.getDBInstance(this).open();
		
		//A background (extended) thread iterating through the queue and carry out necessary operations.
		QueueThread qThread = new QueueThread(this);
		qThread.start();

		//Toast.makeText(this, "QUEUE SERVICE START COMMAND", Toast.LENGTH_LONG).show();
		
		// We need to return if we want to handle this service explicitly. 
		return START_STICKY;
	}
}
