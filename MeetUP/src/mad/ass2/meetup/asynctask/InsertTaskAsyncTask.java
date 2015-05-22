/*
 * NAME: SIA WAI SUAN
 * STUDENT ID: S3308555
 * MOBILE APPLICATION DEVELOPMENT
 * ASSIGNMENT 2
 * FILE: InsertTaskAsyncTask.java
 */
package mad.ass2.meetup.asynctask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;

import mad.ass2.meetup.constants.UserCredentials;
import mad.ass2.meetup.google.TaskExplorerHelper;
import mad.ass2.meetup.model.InterfaceEvent;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

/*
 * Sends request to INSERT a TASK into the TASKLIST in the USER'S GOOGLE ACCOUNT.
 */
public class InsertTaskAsyncTask extends AsyncTask<Void, Void, Void> implements UserCredentials{

	private HttpPost request;
	private HttpResponse response;
	private HttpClient client;
	private String responseStr;
	
	private String authToken = null;

	private JSONObject json;

	private String taskListID;
	
	private String newtask;
	
	private String taskID = "empty";
	
	InterfaceEvent event;

	//Constructor to hold the required attributes needed by this class.
	public InsertTaskAsyncTask(String authToken, String taskListID, String newtask, InterfaceEvent event)
	{
		this.authToken = authToken;
		this.taskListID = taskListID;
		this.newtask = newtask;
		this.event = event;
	}

	@Override
	protected Void doInBackground(Void... params) {

		response = null;
		client = new DefaultHttpClient();

		request = new HttpPost();

		try
		{
			setURI(request);

			setHeaders(request);
			
			setEntity(request);
			
			response = client.execute(request);
			
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		handleResponse();

		return null;
	}

	//Converts the INPUTSTREAM from the HttpResponse to STRING type.
	public static String convertStreamToString(InputStream inputStream) throws IOException {
		if (inputStream != null) {
			Writer writer = new StringWriter();

			char[] buffer = new char[1024];
			try {
				Reader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"),1024);
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
			} finally {
				inputStream.close();
			}
			return writer.toString();
		} else {
			return "";
		}
	}

	public void handleResponse()
	{
		if(response != null)
		{
			try
			{
				responseStr = convertStreamToString(response.getEntity().getContent());
				Log.i("GOOGLE", responseStr);

				try {
					json = new JSONObject(responseStr);
					
					taskID = json.getString("id");
					
					Log.i("INSERT_TASK", taskID);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			//error message...
		}
	}

	public void setURI(HttpPost request) throws URISyntaxException
	{
		request.setURI(new URI(tasksURI + taskListID + tasksURISuffix + apiKey));
	}

	public void setHeaders(HttpPost request)
	{
		request.addHeader("client_id", clientID);
		request.addHeader("client_secret", clientSecret);
		request.setHeader("Authorization", "Bearer " + authToken);
		request.setHeader("Accept", "application/json");
		request.setHeader("Content-type", "application/json");
	}

	public void setEntity(HttpPost request) throws UnsupportedEncodingException
	{
		StringEntity entity= new StringEntity(newtask);
		request.setEntity(entity);
	}

	@Override
	public void onPostExecute(Void unused)
	{
		Log.i("INSERT_TASK_ASYNC", "FINISH");
		
		event.setTaskID(taskID);

		//Set the newly retrieved TASK_ID in all necessary data models.
		TaskExplorerHelper.getSingletonInstance().setEventTaskID(event);
	}
}
