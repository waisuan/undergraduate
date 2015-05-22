/*
 * NAME: SIA WAI SUAN
 * STUDENT ID: S3308555
 * MOBILE APPLICATION DEVELOPMENT
 * ASSIGNMENT 2
 * FILE: AuthenticationManager.java
 */
package mad.ass2.meetup.google;

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

import mad.ass2.meetup.db.EventDBModel;
import mad.ass2.meetup.main.MainActivity;
import mad.ass2.meetup.model.EventModel;
import mad.ass2.meetup.service.QueueThread;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

/*
 * Handles all necessary authentication mechanisms to Google Tasks API.
 * Most importantly, generating the AuthToken is done here.
 */
public class AuthenticationManager implements AccountManagerCallback<Bundle>{

	private AccountManager mAccountManager;
	private String authToken = null;
	private Activity activity;
	private Account account;
	private String AUTH_TOKEN_TYPE = "oauth2:https://www.googleapis.com/auth/tasks";
	private String accountType = "com.google";

	//A singleton class with a private (empty) constructor.
	private static AuthenticationManager authManager = null;

	public boolean state = false;

	//Empty constructor
	public AuthenticationManager()
	{	
	}

	//Other class may call the items from this class whilst using this getter method.
	public static AuthenticationManager getSingletonInstance()
	{
		if (authManager == null)
			authManager = new AuthenticationManager();

		return authManager;
	}

	//Initial setup is called from MainActivity of AuthManager is not called before.
	public void setUp(Activity activity, String accName)
	{
		this.activity = activity;

		account = convertAccName(accName);

		this.state = true;

		Log.i("ACCOUNT", account.name);
	}

	//Convert STRING account name to account type.
	public Account convertAccName(String accName)
	{
		mAccountManager = AccountManager.get(activity);
		Account[] accounts = mAccountManager.getAccountsByType(accountType);

		for(int i = 0; i < accounts.length; i++)
		{
			if((accounts[i].name).equals(accName))
			{
				return accounts[i];
			}
		}

		return null;
	}

	//State dictates whether AuthManager has been called before or not.
	public boolean getState()
	{
		return state;
	}

	//Most important function in this class.
	//Basically, retrieval of token is done here.
	public void authenticate()
	{		
		Log.i("AUTHENTICATION_account", account.name);

		//Destroy previous token and get a brand new one. This is for precaution purposes as 
		//authtokens have an expiry date/time.
		mAccountManager.invalidateAuthToken(accountType, authToken);
		mAccountManager.getAuthToken(account, AUTH_TOKEN_TYPE, null, activity, this, null);
	}

	//AuthManager itself is a thread and therefore, the RUN method is called in order to grab the new token.
	@Override
	public void run(AccountManagerFuture<Bundle> future) {
		try {

			Intent launch = (Intent) future.getResult().get(AccountManager.KEY_INTENT);
			if (launch != null) {
				activity.startActivityForResult(launch, 0);
				return;
			}

			//AuthToken is retrieved.
			authToken = future.getResult().getString(AccountManager.KEY_AUTHTOKEN);

			Log.i("AUTHENTICATION_token", authToken);

			//Save AuthToken.
			giveToken();

		} catch (OperationCanceledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AuthenticatorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			authenticate();
			e.printStackTrace();
		}
	}

	//Save/give token to the TaskExplorerHelper class for specific purposes..
	public void giveToken()
	{
		TaskExplorerHelper.getSingletonInstance().saveAuthToken(authToken);
		TaskExplorerHelper.getSingletonInstance().start();
	}
}
