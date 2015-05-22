/*
 * NAME: SIA WAI SUAN
 * STUDENT ID: S3308555
 * MOBILE APPLICATION DEVELOPMENT
 * ASSIGNMENT 2
 * FILE: EventServiceReceiver.java
 */
package mad.ass2.meetup.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.util.Log;

/*
 * A BROADCAST_RECEIVER in charge of receiving the BOOT_UP_COMPLETE call from the MANIFEST file in order to
 * start up the service when the DEVICE is BOOTED UP.
 */
public class EventServiceReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {

		Intent service = new Intent(context, EventNotificationService.class);

		context.startService(service);
		Log.i("BROADCAST_RECEIVER", "RECEIVED");
	}

}
