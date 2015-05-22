/*
 * NAME: SIA WAI SUAN
 * STUDENT ID: S3308555
 * MOBILE APPLICATION DEVELOPMENT
 * ASSIGNMENT 2
 * FILE: TaskAsyncTask.java
 */
package mad.ass2.meetup.asynctask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import mad.ass2.meetup.constants.UserCredentials;
import mad.ass2.meetup.google.TaskExplorerHelper;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

/*
 * Sends request to GET ALL TASKS from the TASKLIST in the USER'S GOOGLE ACCOUNT.
 */
public class TaskAsyncTask extends AsyncTask<Void, Void, Void> implements UserCredentials{

	private HttpGet request;
	private HttpResponse response;
	private HttpClient client;
	private String responseStr;
	
	private String authToken = null;
	
	private JSONObject json;
	
	private String taskListID;
	
	private ArrayList<String> tasks = new ArrayList<String>();
	
	//Constructor to hold the required attributes needed by this class.
	public TaskAsyncTask(String authToken, String taskListID)
	{
		this.authToken = authToken;
		this.taskListID = taskListID;
	}
	
	@Override
	protected Void doInBackground(Void... params) 
	{	
		response = null;
		client = new DefaultHttpClient();
		
		request = new HttpGet();

		try {
			setURI(request);

			setHeaders(request);

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

					//Grab all the elements within the 'ITEMS' label/tuple.
					JSONArray jsonArray = json.getJSONArray("items");
					for (int i = 0; i < jsonArray.length(); i++)
					{
						//Elements are now stored in an array and should be retrieved as so.
						JSONObject tempObj = jsonArray.getJSONObject(i);
						
						if(tempObj.getString("title").equals(""))
						{
							break;
						}
						
						String taskDetails = tempObj.getString("id") + ";" + tempObj.getString("title") + ";"
								+ tempObj.getString("notes");
						
						Log.i("TASK_DETAILS", taskDetails);
						
						tasks.add(taskDetails);
					}
					
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
	
	public void setURI(HttpGet request) throws URISyntaxException
	{
		request.setURI(new URI(tasksURI + taskListID + tasksURISuffix + apiKey));
	}
	
	public void setHeaders(HttpGet request)
	{
		request.addHeader("client_id", clientID);
		request.addHeader("client_secret", clientSecret);
		request.setHeader("Authorization", "Bearer " + authToken);
	}

	@Override
	public void onPostExecute(Void unused)
	{
		Log.i("TASK_ASYNC", "FINISH");

		//Save all the retrieved TASKS in the TaskExplorerHelper class for future use.
		TaskExplorerHelper.getSingletonInstance().saveTasks(tasks);
	}
}
