/*
 * NAME: SIA WAI SUAN
 * STUDENT ID: S3308555
 * MOBILE APPLICATION DEVELOPMENT
 * ASSIGNMENT 2
 * FILE: QueueThread.java
 */
package mad.ass2.meetup.service;

import java.util.ArrayList;

import mad.ass2.meetup.constants.RequestType;
import mad.ass2.meetup.google.AuthenticationManager;
import mad.ass2.meetup.google.TaskExplorerHelper;
import mad.ass2.meetup.main.MainActivity;
import mad.ass2.meetup.model.Task;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/*
 * Background thread extending from the QueueHandlerService class with the purpose of handling QUEUE operations.
 */
public class QueueThread extends Thread implements RequestType{

	private Context context;
	public static boolean netStatus = false;

	public QueueThread(QueueHandlerService context)
	{
		this.context = context;
	}

	//Check network connectivity.
	//If network connectivity is down, do not do anything with the QUEUE.
	public void checkNetworkConn()
	{
		ConnectivityManager cm = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni == null) {
			netStatus = false;
		} 
		else
		{
			netStatus = true;
		}
	}

	//Main function of the thread.
	@SuppressWarnings("static-access")
	public void run()
	{
		ArrayList<Task> tasksInQueue = new ArrayList<Task>();
		Task tempTask = null;

		//Whilst the SERVICE class is not stopped, keep running this thread.
		while(QueueHandlerService.queueServiceStatus == true)
		{
			tasksInQueue = TaskExplorerHelper.getSingletonInstance().getTaskQueue();

			checkNetworkConn();

			//If network connectivity is down, do not do anything with the QUEUE.
			if(netStatus == false)
			{
				continue;
			}

			//Iterate through the queue and do necessary & specific operations on it.
			for(int i = 0; i < tasksInQueue.size(); i++)
			{
				tempTask = tasksInQueue.get(i);

				if(tempTask.getReqType().equals(INSERT_TASK))
				{
					Log.i("QUEUE_THREAD", "INSERT");

					//If taskListID has not been initialized, do so.
					if(TaskExplorerHelper.getSingletonInstance().getTaskListID() == null)
					{
						//If AuthManager has yet to be initialized, break from current loop.
						if(AuthenticationManager.getSingletonInstance().getState() == false)
						{
							break;
						}

						if(TaskExplorerHelper.getSingletonInstance().initialising == false)
						{
							TaskExplorerHelper.getSingletonInstance().doGetTaskList();
						}

						break;
					}

					TaskExplorerHelper.getSingletonInstance().doInsert(tempTask.getEvent());
					TaskExplorerHelper.getSingletonInstance().popFromQueue(i);
					TaskExplorerHelper.getSingletonInstance().popFromDB(tempTask);
				}
				else if(tempTask.getReqType().equals(UPDATE_TASK))
				{
					Log.i("QUEUE_THREAD", "UPDATE");
					if(tempTask.getEvent().getTaskID().equals("empty"))
					{
						continue;
					}
					else if(TaskExplorerHelper.getSingletonInstance().getTaskListID() == null)
					{
						if(AuthenticationManager.getSingletonInstance().getState() == false)
						{
							break;
						}

						if(TaskExplorerHelper.getSingletonInstance().initialising == false)
						{
							TaskExplorerHelper.getSingletonInstance().doGetTaskList();
						}

						break;
					}

					TaskExplorerHelper.getSingletonInstance().doUpdate(tempTask.getEvent());
					TaskExplorerHelper.getSingletonInstance().popFromQueue(i);
					TaskExplorerHelper.getSingletonInstance().popFromDB(tempTask);
				}
				else if(tempTask.getReqType().equals(DEL_TASK))
				{
					Log.i("QUEUE_THREAD", "DELETE");

					if(tempTask.getEvent().getTaskID().equals("empty"))
					{
						continue;
					}
					else if(TaskExplorerHelper.getSingletonInstance().getTaskListID() == null)
					{
						if(AuthenticationManager.getSingletonInstance().getState() == false)
						{
							break;
						}

						if(TaskExplorerHelper.getSingletonInstance().initialising == false)
						{
							TaskExplorerHelper.getSingletonInstance().doGetTaskList();
						}

						break;
					}

					TaskExplorerHelper.getSingletonInstance().doDelete(tempTask.getEvent());
					TaskExplorerHelper.getSingletonInstance().popFromQueue(i);
					TaskExplorerHelper.getSingletonInstance().popFromDB(tempTask);
				}
			}

			try {
				//Necessary to prevent concurrency issues on the QUEUE data structure.
				this.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
