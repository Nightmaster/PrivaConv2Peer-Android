//
//

package esgi.priva2peer;

import java.util.HashMap;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import esgi.priva2peer.activity.Home;

public class UserSessionManager
{

	SharedPreferences pref;
	Editor editor;
	Context _context;
	int PRIVATE_MODE = 0;

	private static final String PREFER_NAME = "AndroidExamplePref";

	private static final String IS_USER_LOGIN = "IsUserLoggedIn";
	public static final String KEY_NAME = "name";
	public static final String KEY_EMAIL = "email";
	public static final String KEY_FirstName = "firstname";
	public static final String KEY_LastName = "lastname";

	@SuppressLint("CommitPrefEdits")
	public UserSessionManager(Context context)
	{
		this._context = context;
		pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}

	public void createUserLoginSession(String name, String email, String firstname, String lastname)
	{
		editor.putBoolean(IS_USER_LOGIN, true);
		editor.putString(KEY_NAME, name);
		editor.putString(KEY_EMAIL, email);
		editor.putString(KEY_FirstName, firstname);
		editor.putString(KEY_LastName, lastname);
		editor.commit();
	}

	public boolean checkLogin()
	{
		if (!this.isUserLoggedIn())
		{

			Intent i = new Intent(_context, Home.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			_context.startActivity(i);
			return true;
		}
		return false;
	}

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

		editor.clear();
		editor.commit();
		Intent i = new Intent(_context, Home.class);

		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		_context.startActivity(i);
	}

	// Check for login
	public boolean isUserLoggedIn()
	{
		return pref.getBoolean(IS_USER_LOGIN, false);
	}
}
