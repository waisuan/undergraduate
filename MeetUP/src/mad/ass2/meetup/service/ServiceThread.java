/*
 * NAME: SIA WAI SUAN
 * STUDENT ID: S3308555
 * MOBILE APPLICATION DEVELOPMENT
 * ASSIGNMENT 2
 * FILE: ServiceThread.java
 */
package mad.ass2.meetup.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import mad.ass1.meetup.main.R;
import mad.ass2.meetup.model.EventModel;
import mad.ass2.meetup.model.InterfaceEvent;
import mad.ass2.meetup.view.SingleEventViewActivity;

/*
 * Background thread extending from the EventNotificationService class with the purpose notifying users
 * 15 MINUTES before the time of EVENT.
 */
public class ServiceThread extends Thread{
	private ArrayList<InterfaceEvent> eventList;
	private static final String INNER_TAG = "MyThread";
	//15 MINUTES
	private long MAX_DURATION = 15; //15*60*1000
	private Context context;

	public ServiceThread(EventNotificationService context)
	{
		this.context = context;
	}
	
	//Create NOTIFICATION at the TOP of the device screen when TIME'S UP.
	public void createNotification(int index, String eventTitle, String eDate, String eStart, String eEnd) {
		NotificationManager notificationManager =
				(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		Intent notificationIntent = new Intent(context, SingleEventViewActivity.class);
		notificationIntent.putExtra(EventNotificationService.serviceClassName, index);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
		Notification notification = new Notification.Builder(context)
		.setContentTitle("[Event] " + eventTitle)
		.setContentText(eDate + " " + eStart + " - " + eEnd).setSmallIcon(R.drawable.mu_launcher)
		.setContentIntent(pendingIntent)
		.build();
		notificationManager.notify(0, notification);
		Log.i("NOTIFICATION", String.valueOf(index));
	}
	
	//Main function of thread.
	public void run() 
	{
		this.setName(INNER_TAG);
		String prevEvent = " ";
		String prevTime = " ";

		Log.i("SERVICE_THREAD", "RUN");

		//Whilst the SERVICE class is not stopped, keep running this thread.
		while(EventNotificationService.serviceStatus == true)
		{
			//Get EVENT LIST.
			eventList = EventModel.getSingletonInstance().getEventList();

			//Get current DATE/TIME.
			DateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
			Date date = new Date();

			for(int i = 0; i < eventList.size(); i++)
			{
				//Calculate the difference of TIME in MINUTES.
				long diffMinutes = ((eventList.get(i).getEventDateFormat().getTime() - date.getTime()) / 
						(60 * 1000) % 60)+1;
				
				//If EVENT's DATE matches the CURRENT DATE.
				if(dateFormat.format(date).equals(eventList.get(i).getEventDate()))
				{
					//If the DIFFERENCE in MINUTES is 15.
					if(diffMinutes == MAX_DURATION)
					{
						if(!(prevEvent.equals(eventList.get(i).getEventTitle())) || 
								!(prevTime.equals(eventList.get(i).getEventStartTime())))
						{
							Log.i("SERVICE_THREAD", eventList.get(i).getEventTitle() + " | " + diffMinutes);
							createNotification(i, eventList.get(i).getEventTitle(), eventList.get(i).getEventDate(),
									eventList.get(i).getEventStartTime(), eventList.get(i).getEventEndTime());
							prevEvent = eventList.get(i).getEventTitle();
							prevTime = eventList.get(i).getEventStartTime();
						}
					}
				}
			}
			
			try {
				//Necessary to prevent concurrency issues on the EVENT LIST data structure.
				this.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
