/*
 * NAME: SIA WAI SUAN
 * STUDENT ID: S3308555
 * MOBILE APPLICATION DEVELOPMENT
 * ASSIGNMENT 2
 * FILE: TaskExplorerHelper.java
 */
package mad.ass2.meetup.google;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import mad.ass2.meetup.asynctask.DelTaskAsyncTask;
import mad.ass2.meetup.asynctask.InsertTaskAsyncTask;
import mad.ass2.meetup.asynctask.TaskAsyncTask;
import mad.ass2.meetup.asynctask.TaskListAsyncTask;
import mad.ass2.meetup.asynctask.UpdateTaskAsyncTask;
import mad.ass2.meetup.constants.RequestType;
import mad.ass2.meetup.db.TaskDBModel;
import mad.ass2.meetup.model.EventModel;
import mad.ass2.meetup.model.InterfaceEvent;
import mad.ass2.meetup.model.Task;

/*
 * A helper class for coordinating the necessary/specific operations on multiple TASKS and fire off their
 * corresponding AsyncTasks.
 * Each method SHOULD be executed in a particular sequence in order to uphold synchronization regulations.
 */
public class TaskExplorerHelper implements RequestType{

	public ArrayList<Task> queue = new ArrayList<Task>();

	private String taskListID = null;
	private ArrayList<String> tasks = new ArrayList<String>();
	private ArrayList<InterfaceEvent> insertQ = new ArrayList<InterfaceEvent>();
	private ArrayList<InterfaceEvent> updateQ = new ArrayList<InterfaceEvent>();
	private ArrayList<InterfaceEvent> deleteQ = new ArrayList<InterfaceEvent>();
	private ArrayList<String> reqQ = new ArrayList<String>();

	//A singleton class.
	private static TaskExplorerHelper helperInstance = null;

	private String authToken = null;

	private String authStatus;

	private Task task;

	private InsertTaskAsyncTask insertTask = null;
	private UpdateTaskAsyncTask updateTask = null;
	private DelTaskAsyncTask deleteTask = null;

	private static boolean canPutInDB = true;

	public static boolean initialising = false;

	//Constructor -- Retrieve (if any) TASKS that were put on queue but have yet to be executed before the
	//app crashed/device closed itself.
	public TaskExplorerHelper()
	{
		this.queue = TaskDBModel.getDBInstance(null).getTasks();
	}

	//Other class may call the items from this class whilst using this getter method.
	public static TaskExplorerHelper getSingletonInstance()
	{
		if (helperInstance == null)
			helperInstance = new TaskExplorerHelper();

		return helperInstance;
	}

	public String getTaskListID()
	{
		return taskListID;
	}

	public void saveAuthToken(String authToken)
	{
		this.authToken = authToken;
	}

	//Every time an operation is carried out, the AuthManager is called to get a new token.
	public void reAuthenticate()
	{
		AuthenticationManager.getSingletonInstance().authenticate();
	}

	//All TASKS are first placed into the QUEUE (which is then handled by the QueueThread) before being fired off
	//to the Google Tasks API.
	public void putInQueue(Task qTask)
	{
		boolean check = true;
		int hasInsert = 0;
		int hasUpdate = 0;

		synchronized(queue)
		{
			//If the TASK is of DELETE type, check whether there are any UPDATE and/or INSERT tasks within the QUEUE.
			//If there are and their EVENT ID match, remove the UPDATE & INSERT tasks from the queue.
			//If there is only an UPDATE operation in the queue, remove that but KEEP the DELETE operation.
			//If there are both UPDATE and INSERT operations, remove them and the DELETE operation as well.
			if(qTask.getReqType().equals("DEL_TASK"))
			{
				for(int i = queue.size()-1; i >= 0; i--)
				{
					Log.i("queue_LOOP", queue.get(i).getEvent().getEventTitle());

					if(queue.get(i).getEvent().getEventID().equals(qTask.getEvent().getEventID()) &&
							queue.get(i).getReqType().equals("UPDATE_TASK"))
					{
						Log.i("REMOVE_UPDATE", "update");

						popFromDB(queue.get(i));

						queue.remove(i);

						hasUpdate++;
					}
					else if(queue.get(i).getEvent().getEventID().equals(qTask.getEvent().getEventID()) &&
							queue.get(i).getReqType().equals("INSERT_TASK"))
					{
						Log.i("REMOVE_INSERT", "insert");

						popFromDB(queue.get(i));

						queue.remove(i);

						hasInsert++;
					}
				}

				if(hasInsert > 0 && hasUpdate > 0)
				{
					check = false;
				}
				else if(hasInsert > 0)
				{
					check = false;
				}
			}

			if(check == true)
			{
				Log.i("PUT IN QUEUE", qTask.getEvent().getEventTitle());

				queue.add(qTask);

				for(int i = 0; i < queue.size(); i++)
				{
					Log.i("IN QUEUE", queue.get(i).getEvent().getEventTitle());
				}
			}
			else
			{
				Log.i("DONT_PUT_IN_DB", "DELETE");
				canPutInDB = false;
			}
		}
	}

	//As soon as the TASKS are added to the queue, they are also added to the DB for safety purposes.
	public void putInDB(Task task)
	{
		TaskDBModel.getDBInstance(null).insertTask(task);
	}

	//Create the TASK object.
	public void createTask(String reqType, InterfaceEvent event)
	{
		task = new Task(reqType, event);
	}

	//Called after the AuthManager has successfully retrieved the AuthToken.
	//Any pending requests in the (local) request queue are executed here.
	public void start()
	{	
		if(reqQ.size() != 0)
		{
			authStatus = reqQ.remove(0);
			reqQ.trimToSize();

			Log.i("REQ_TYPE", authStatus);

			if(authStatus.equals(GET_TASKLIST))
			{
				continueGetTaskList();
			}
			else if(authStatus.equals(GET_TASKS))
			{
				continueGetTasks();
			}
			else if(authStatus.equals(INSERT_TASK))
			{
				continueInsert();
			}
			else if(authStatus.equals(UPDATE_TASK))
			{
				continueUpdate();
			}
			else if(authStatus.equals(DEL_TASK))
			{
				continueDelete();
			}
		}
	}

	//Called after the taskListID is retrieved.
	//If there are any pending requests in the (local) request queue, execute them.
	public void handleOtherReq()
	{
		if(reqQ.size() != 0)
		{
			start();
		}
	}

	//Initiate get TASK LIST ID.
	public void doGetTaskList()
	{
		initialising = true;

		reqQ.add(GET_TASKLIST);

		reAuthenticate();
	}

	//Get TASK LIST ID by executing the necessary AsyncTask.
	public void continueGetTaskList()
	{
		TaskListAsyncTask getTaskList = new TaskListAsyncTask(authToken);
		getTaskList.execute();

		//DO NOT REMOVE
		//		try {
		//			getTaskList.execute().get();
		//		} catch (InterruptedException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		} catch (ExecutionException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}
	}

	//Initiate get all TASKS.
	public void doGetTasks()
	{
		if(taskListID == null)
		{
			doGetTaskList();
			reqQ.add(GET_TASKS);
		}
		else
		{
			reqQ.add(GET_TASKS);
			reAuthenticate();
		}
	}

	//Get all TASKS by executing the necessary AsyncTask.
	public void continueGetTasks()
	{
		TaskAsyncTask getTasks = new TaskAsyncTask(authToken, taskListID);
		getTasks.execute();
	}

	//Insertion operation is called -- Task is created, put into the queue, and then into the DB.
	public void insertQueue(InterfaceEvent event)
	{
		createTask(INSERT_TASK, event);
		putInQueue(task);
		putInDB(task);
	}

	//Initiate insert TASK operation.
	public void doInsert(InterfaceEvent event)
	{
		insertQ.add(event);
		reqQ.add(INSERT_TASK);

		reAuthenticate();
	}

	//Insert a TASK into the TASKLIST by executing the necessary AsyncTask.
	public void continueInsert()
	{
		//Pop event object from the local insertion queue.
		//The first index should represent the next object to be operated on.
		InterfaceEvent event = insertQ.remove(0);
		insertQ.trimToSize();

		String taskTitle = event.getEventTitle();

		String eventNote = "";
		if(event.getEventNote().equals("") || event.getEventNote() == null)
		{
			eventNote = "No Additional Details";
		}
		else
		{
			eventNote = event.getEventNote().replaceAll("\n", " ");
		}

		String taskNotes = event.getEventDate() + ", " + event.getEventStartTime() + " _ " + event.getEventEndTime()
				+ ", " + event.getEventVenue() + ", " + eventNote;

		if(event.getNumOfAttend() != 0)
		{
			String attendees = ", [" + event.getAttendees().get(0);
			for(int i = 1; i < event.getNumOfAttend(); i++)
			{	
				attendees += "+" + event.getAttendees().get(i);
			}
			attendees += "]";

			taskNotes += attendees;
		}

		//Creation of the REQUEST BODY to be sent over the HttpRequest.
		String body = "{\"title\": \"" + taskTitle + "\", \"notes\": \"" + taskNotes + "\"}";

		insertTask = new InsertTaskAsyncTask(authToken, taskListID, body, event);
		insertTask.execute();
	}

	//Update operation is called -- Task is created, put into the queue, and then into the DB.
	public void updateQueue(InterfaceEvent event)
	{
		createTask(UPDATE_TASK, event);
		putInQueue(task);
		putInDB(task);
	}

	//Initiate update TASK operation.
	public void doUpdate(InterfaceEvent event)
	{
		updateQ.add(event);
		reqQ.add(UPDATE_TASK);

		reAuthenticate();
	}

	//Update a TASK in the TASKLIST by executing the necessary AsyncTask.
	public void continueUpdate()
	{
		//Pop event object from the local update queue.
		//The first index should represent the next object to be operated on.
		InterfaceEvent event = updateQ.remove(0);
		updateQ.trimToSize();

		String eventTaskID = "";
		if(event.getTaskID().equals("empty"))
		{
			eventTaskID = EventModel.getSingletonInstance().getEventforId(event.getEventID()).getTaskID();
		}
		else
		{
			eventTaskID = event.getTaskID();
		}

		String eventNote = "";
		if(event.getEventNote().equals("") || event.getEventNote() == null)
		{
			eventNote = "No Additional Details";
		}
		else
		{
			eventNote = event.getEventNote().replaceAll("\n", " ");
		}

		String taskTitle = event.getEventTitle();
		String taskNotes = event.getEventDate() + ", " + event.getEventStartTime() + " _ " + event.getEventEndTime()
				+ ", " + event.getEventVenue() + ", " + eventNote;

		if(event.getNumOfAttend() != 0)
		{
			String attendees = ", [" + event.getAttendees().get(0);
			for(int i = 1; i < event.getNumOfAttend(); i++)
			{	
				attendees += "+" + event.getAttendees().get(i);
			}
			attendees += "]";

			taskNotes += attendees;
		}

		//Creation of the REQUEST BODY to be sent over the HttpRequest.
		String body = "{\"id\": \"" + eventTaskID + "\", \"title\": \"" + taskTitle + "\", \"notes\": \"" + taskNotes + "\"}";

		updateTask = new UpdateTaskAsyncTask(authToken, taskListID, eventTaskID, body);
		updateTask.execute();
	}

	//Delete operation is called -- Task is created, put into the queue, and then into the DB (if needed).
	public void deleteQueue(InterfaceEvent event)
	{
		createTask(DEL_TASK, event);
		putInQueue(task);

		if(canPutInDB == true)
		{
			putInDB(task);
		}
		else
		{
			canPutInDB = true;
		}
	}

	//Initiate delete TASK operation.
	public void doDelete(InterfaceEvent event)
	{
		deleteQ.add(event);
		reqQ.add(DEL_TASK);

		reAuthenticate();
	}

	//Delete a TASK in the TASKLIST by executing the necessary AsyncTask.
	public void continueDelete()
	{
		//Pop event object from the local deletion queue.
		//The first index should represent the next object to be operated on.
		InterfaceEvent event = deleteQ.remove(0);
		deleteQ.trimToSize();

		String eventTaskID = "";
		if(event.getTaskID().equals("empty"))
		{
			eventTaskID = EventModel.getSingletonInstance().getEventforId(event.getEventID()).getTaskID();
		}
		else
		{
			eventTaskID = event.getTaskID();
		}

		deleteTask = new DelTaskAsyncTask(authToken, taskListID, eventTaskID);
		deleteTask.execute();
	}

	//Called after the TaskListAsyncTask has completed.
	public void saveTaskListID(String taskListID)
	{
		this.taskListID = taskListID;

		Log.i("Save Task List ID", taskListID);

		handleOtherReq();
	}

	//Called after the TaskAsyncTask has completed.
	public void saveTasks(ArrayList<String> tasks)
	{
		this.tasks = tasks;

		EventModel.getSingletonInstance().override(tasks);
	}

	//Called after the InsertTaskAsyncTask has completed.
	//Updates all necessary DATA MODELS with the newly received TASK ID.
	public void setEventTaskID(InterfaceEvent event)
	{
		Log.i("SET_EVENT_TASK_ID", event.getTaskID());

		EventModel.getSingletonInstance().updateDataModels(event);

		synchronized(queue)
		{
			for(int i = 0; i < queue.size(); i++)
			{
				if(queue.get(i).getEvent().getEventID().equals(event.getEventID()))
				{
					queue.get(i).getEvent().setTaskID(event.getTaskID());
					updateToTaskDB(queue.get(i));
				}
			}
		}
	}

	public ArrayList<Task> getTaskQueue()
	{
		return this.queue;
	}

	public void updateToTaskDB(Task task)
	{
		TaskDBModel.getDBInstance(null).updateTask(task);
	}

	public void popFromQueue(int index)
	{
		queue.remove(index);
	}

	public void popFromDB(Task task)
	{
		TaskDBModel.getDBInstance(null).deleteTask(task);
	}

	public void deleteAllFromQ()
	{
		queue.clear();
	}

	public void deleteAllFromQDb()
	{
		TaskDBModel.getDBInstance(null).deleteAllTasks();
	}
}
