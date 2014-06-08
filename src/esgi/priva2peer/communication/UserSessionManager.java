package esgi.priva2peer.communication;

import java.util.HashMap;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import esgi.priva2peer.activity.Home;

public class UserSessionManager
{

	// Shared Preferences reference
	SharedPreferences pref;

	// Editor reference for Shared preferences
	Editor editor;

	// Context
	Context _context;

	// Shared pref mode
	int PRIVATE_MODE = 0;

	// Sharedpref file name
	private static final String PREFER_NAME = "AndroidExamplePref";

	// All Shared Preferences Keys
	private static final String IS_USER_LOGIN = "IsUserLoggedIn";

	public static final String KEY_NAME = "name";

	public static final String KEY_EMAIL = "email";
	public static final String KEY_FirstName = "firstname";
	public static final String KEY_LastName = "lastname";

	// Constructor
	@SuppressLint("CommitPrefEdits")
	public UserSessionManager(Context context)
	{
		this._context = context;
		pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}

	// Create login session
	public void createUserLoginSession(String name, String email, String firstname, String lastname)
	{
		// Storing login value as TRUE
		editor.putBoolean(IS_USER_LOGIN, true);

		// Storing name in pref
		editor.putString(KEY_NAME, name);

		// Storing email in pref
		editor.putString(KEY_EMAIL, email);
		editor.putString(KEY_FirstName, firstname);
		editor.putString(KEY_LastName, lastname);

		// commit changes
		editor.commit();
	}

	/**
	 * Check login method will check user login status If false it will redirect
	 * user to login page Else do anything
	 * */
	public boolean checkLogin()
	{
		// Check login status
		if (!this.isUserLoggedIn())
		{

			// user is not logged in redirect him to Login Activity
			Intent i = new Intent(_context, Home.class);

			// Closing all the Activities from stack
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

			// Add new Flag to start new Activity
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			// Staring Login Activity
			_context.startActivity(i);

			return true;
		}
		return false;
	}

	/**
	 * Get stored session data
	 * */
	public HashMap<String, String> getUserDetails()
	{
		HashMap<String, String> user = new HashMap<String, String>();
		user.put(KEY_NAME, pref.getString(KEY_NAME, null));
		user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
		user.put(KEY_FirstName, pref.getString(KEY_FirstName, null));
		user.put(KEY_LastName, pref.getString(KEY_LastName, null));

		// return user
		return user;
	}

	/**
	 * Clear session details
	 * */
	public void logoutUser()
	{

		// Clearing all user data from Shared Preferences
		editor.clear();
		editor.commit();

		// After logout redirect user to Login Activity
		Intent i = new Intent(_context, Home.class);

		// Closing all the Activities
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		// Add new Flag to start new Activity
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		// Staring Login Activity
		_context.startActivity(i);
	}

	// Check for login
	public boolean isUserLoggedIn()
	{
		return pref.getBoolean(IS_USER_LOGIN, false);
	}
}
