package esgi.priva2peer.activity;

import java.util.HashMap;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import esgi.priva2peer.R;
import esgi.priva2peer.communication.UserSessionManager;

public class ListFriends extends Activity
{
	// User Session Manager Class
	UserSessionManager session;
	final Context context = this;

	public static DefaultHttpClient getThreadSafeClient()
	{
		DefaultHttpClient client = new DefaultHttpClient();
		ClientConnectionManager mgr = client.getConnectionManager();
		HttpParams params = client.getParams();

		client = new DefaultHttpClient(new ThreadSafeClientConnManager(params, mgr.getSchemeRegistry()), params);

		return client;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friendlist);
		Button btnAddFriends, btnchangeProfile;
		final ListView listfriends;

		// Session value

		session = new UserSessionManager(getApplicationContext());

		HashMap<String, String> user = session.getUserDetails();
		String name = user.get(UserSessionManager.KEY_NAME);
		String email = user.get(UserSessionManager.KEY_EMAIL);
		String last_n = user.get(UserSessionManager.KEY_FirstName);
		String first_n = user.get(UserSessionManager.KEY_LastName);

		btnAddFriends = (Button) findViewById(R.id.buttonAddFriends);
		btnchangeProfile = (Button) findViewById(R.id.changeProfile);
		listfriends = (ListView) findViewById(R.id.friends_row);

		HttpClient Client = new DefaultHttpClient();
		String URL = "http://54.194.20.131:8080/webAPI/stayAlive";
		try
		{
			HttpGet httpget = new HttpGet(URL);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			Log.d("sessId", PreferenceManager.getDefaultSharedPreferences(context).getString("MYLABEL", "IfNothingFound"));

			if (PreferenceManager.getDefaultSharedPreferences(context).getString("MYLABEL", "defaultStringIfNothingFound") != "IfNothingFound")
			{
				httpget.setHeader("Cookie", PreferenceManager.getDefaultSharedPreferences(context).getString("MYLABEL", "IfNothingFound"));
			}

			String SetServerString = "";
			SetServerString = Client.execute(httpget, responseHandler);
			JSONObject JSONObject = new JSONObject(SetServerString);
			String friends = JSONObject.get("friends").toString();
			String[] parts = friends.split("\"");

			PreferenceManager.getDefaultSharedPreferences(context).edit().putString("friend_1", parts[3]).commit();
			PreferenceManager.getDefaultSharedPreferences(context).edit().putString("friend_2", parts[10]).commit();

			String askfriend = JSONObject.get("askfriend").toString();
			String[] invite_friend = askfriend.split("\"");
			Log.i("MyApp", invite_friend[1]);
			Log.i("MyApp", invite_friend[3]);
			Log.i("MyApp", SetServerString);
		}
		catch (Exception ex)
		{
			Log.d("liste", "Fail!");
		}

		String[] listeStrings = {PreferenceManager.getDefaultSharedPreferences(context).getString("friend_1", "IfNothingFound")};

		listfriends.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listeStrings));

		btnAddFriends.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{

				Intent add_f_intent = new Intent(getApplicationContext(), AddFriend.class);
				startActivity(add_f_intent);
			}
		});
		btnchangeProfile.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent add_f_intent = new Intent(getApplicationContext(), ChangeProfile.class);
				startActivity(add_f_intent);
			}
		});
		listfriends.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				String selectedFromList = (listfriends.getItemAtPosition(position).toString());

				Intent add_f_intent = new Intent(view.getContext(), ChatActivity.class);
				add_f_intent.putExtra("mytext", selectedFromList);
				startActivity(add_f_intent);
			}
		});
	}
}