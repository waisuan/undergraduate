/*
 * NAME: SIA WAI SUAN
 * STUDENT ID: S3308555
 * MOBILE APPLICATION DEVELOPMENT
 * ASSIGNMENT 2
 * FILE: EventViewAdapter.java
 */
package mad.ass2.meetup.view.model;

import java.util.ArrayList;
import mad.ass1.meetup.main.R;
import mad.ass1.meetup.main.R.layout;
import mad.ass1.meetup.main.R.menu;
//import mad.ass1.meetup.model.Event;
import mad.ass2.meetup.model.InterfaceEvent;
import android.content.Context;
//import android.provider.ContactsContract.CommonDataKinds.Event;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/*
 * The custom adapter class for assisting the LIST VIEW.
 * Basically, the adapter inflates the rows in the LIST VIEW with custom layout.
 */
public class EventViewAdapter extends BaseAdapter{
	
	private static ArrayList<InterfaceEvent> eventList;
	 
	 private LayoutInflater mInflater;

	 //Constructor
	 public EventViewAdapter(Context context, ArrayList<InterfaceEvent> events) {
		 eventList = events;
		 mInflater = LayoutInflater.from(context);
	 }

	 public int getCount() {
		 return eventList.size();
	 }

	 public Object getItem(int position) {
		 return eventList.get(position);
	 }

	 public long getItemId(int position) {
		 return position;
	 }

	 //Inflating occurs here.
	 public View getView(int position, View convertView, ViewGroup parent) {
		 
		 ViewHolder holder;
		 
		 if (convertView == null) {
			 convertView = mInflater.inflate(R.layout.custom_row, null);
			 
			 holder = new ViewHolder();
			 holder.eventTitle = (TextView) convertView.findViewById(R.id.eventTitleItem);
			 holder.eventDate = (TextView) convertView.findViewById(R.id.eventDTItem);
			 holder.eventAttendance = (TextView) convertView.findViewById(R.id.eventAttendItem);

			 convertView.setTag(holder);
		 } 
		 else {
			 holder = (ViewHolder) convertView.getTag();
		 }
		 
		 //Event text/summary is set.
		 holder.eventTitle.setText(eventList.get(position).getEventTitle());
		 holder.eventDate.setText(eventList.get(position).getEventDate() + " " + eventList.get(position).getEventStartTime() + " - " + eventList.get(position).getEventEndTime());
		 holder.eventAttendance.setText(String.valueOf(eventList.get(position).getNumOfAttend()));

		 return convertView;
	 }

	 //A holder pattern that allows the manipulation of data within the list view row to be easier.
	 static class ViewHolder {
		 TextView eventTitle;
		 TextView eventDate;
		 TextView eventAttendance;
	 }
}
