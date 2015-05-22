/*
 * NAME: SIA WAI SUAN
 * STUDENT ID: S3308555
 * MOBILE APPLICATION DEVELOPMENT
 * ASSIGNMENT 2
 * FILE: TaskListAsyncTask.java
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

import mad.ass2.meetup.constants.UserCredentials;
import mad.ass2.meetup.google.TaskExplorerHelper;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

/*
 * Sends request to GET the TASKLIST_ID from the TASKLIST in the USER'S GOOGLE ACCOUNT.
 */
public class TaskListAsyncTask extends AsyncTask<Void, Void, String> implements UserCredentials{
	
	private HttpGet request;
	private HttpResponse response;
	private HttpClient client;
	private String responseStr;

	private String authToken = null;
	
	private JSONObject json;
	
	private String taskListID;
	
	//Constructor to hold the required attributes needed by this class.
	public TaskListAsyncTask(String authToken)
	{
		this.authToken = authToken;
	}

	@Override
	protected String doInBackground(Void... unused)
	{
		response = null;
		client = new DefaultHttpClient();
		
		request = new HttpGet();

		try {
			setURI(request);

			setHeaders(request);
			
			response = client.execute(request);

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		handleResponse();
		
		return taskListID;
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
			try {
				responseStr = convertStreamToString(response.getEntity().getContent());
				Log.i("GOOGLE", responseStr);

				try {
					json = new JSONObject(responseStr);

					//Retrieve the first element from the JSONArray labeled with 'ITEMS' and get the task list ID from there.
					taskListID = json.getJSONArray("items").getJSONObject(0).getString("id");

					Log.i("JSON", taskListID);

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
		request.setURI(new URI(listURI + apiKey));
	}
	
	public void setHeaders(HttpGet request)
	{
		request.addHeader("client_id", clientID);
		request.addHeader("client_secret", clientSecret);
		request.setHeader("Authorization", "Bearer " + authToken);
	}

	@Override
	public void onPostExecute(String taskListID)
	{
		Log.i("TASK_LIST_ASYNC", "FINISH");

		//Save the task list ID in the TaskExplorerHelper class for future use.
		TaskExplorerHelper.getSingletonInstance().saveTaskListID(taskListID);
	}
}
