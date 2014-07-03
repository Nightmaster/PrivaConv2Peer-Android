package esgi.priva2peer.activity;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;
import esgi.priva2peer.R;
import esgi.priva2peer.UserSessionManager;
import esgi.priva2peer.communication.server.Connexion;
import esgi.priva2peer.data.Constants;

/**
 * @author Bruno Gb
 */
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
		Connexion con = new Connexion();
		String num = con.getLocalIpAddress();
		Toast.makeText(getApplicationContext(), num, Toast.LENGTH_LONG).show();
		btnAddFriends = (Button) findViewById(R.id.buttonAddFriends);
		btnchangeProfile = (Button) findViewById(R.id.changeProfile);
		listfriends = (ListView) findViewById(R.id.friends_row);

		HttpClient Client = new DefaultHttpClient();
		String URL = "http://54.194.20.131:8080/webAPI/stayAlive";
		try
		{
			HttpGet httpget = new HttpGet(URL);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			if (PreferenceManager.getDefaultSharedPreferences(context).getString("MYLABEL", "defaultStringIfNothingFound") != "IfNothingFound")
			{
				httpget.setHeader("Cookie", PreferenceManager.getDefaultSharedPreferences(context).getString("MYLABEL", ""));
			}
			String SetServerString = "";
			SetServerString = Client.execute(httpget, responseHandler);

			JSONObject friend_Object = new JSONObject(SetServerString);
			String friends = friend_Object.get("friends").toString();
			String[] parts = friends.split("\"");
			Log.d("liste damis", friends);

			PreferenceManager.getDefaultSharedPreferences(context).edit().putString("friend_1", parts[3]).commit();
			PreferenceManager.getDefaultSharedPreferences(context).edit().putString("friend_2", parts[9]).commit();
			PreferenceManager.getDefaultSharedPreferences(context).edit().putString("friend_3", parts[15]).commit();

			JSONObject askfriend_Object = new JSONObject(SetServerString);
			String askfriend = askfriend_Object.get("askFriend").toString();
			String[] a_friend = askfriend.split("\"");
			Log.i("MyApp", a_friend[1]);
			PreferenceManager.getDefaultSharedPreferences(context).edit().putString("ami1", a_friend[1]).commit();

			if (a_friend[1] != "")
			{
				final Dialog dialog = new Dialog(context);
				dialog.setContentView(R.layout.popup_invite_friends);
				dialog.setTitle("New friends?");

				TextView text = (TextView) dialog.findViewById(R.id.text);
				text.setText(a_friend[1] + " te demande en ami.");
				Button ConfirmButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
				Button RefusedButton = (Button) dialog.findViewById(R.id.dialogButtonNOK);
				ConfirmButton.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						try
						{
							HttpClient client = new DefaultHttpClient();
							String user = PreferenceManager.getDefaultSharedPreferences(context).getString("ami1", "defaultStringIfNothingFound");

							String URL = Constants.SRV_URL + Constants.SRV_API + "answerRequest?username=" + user + "&validate=true";

							ResponseHandler<String> responseHandler = new BasicResponseHandler();
							HttpGet get = new HttpGet(URL);
							if (PreferenceManager.getDefaultSharedPreferences(context).getString("MYLABEL", "defaultStringIfNothingFound") != "IfNothingFound")
							{
								get.setHeader("Cookie", PreferenceManager.getDefaultSharedPreferences(context).getString("MYLABEL", ""));
							}
							String SetServerString = "";
							SetServerString = client.execute(get, responseHandler);

							JSONObject JSONObject = new JSONObject(SetServerString);
							String name = JSONObject.get("user").toString();
							String[] parts = name.split("\"");
							Log.d("MyApp", parts[7]);

						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
						dialog.dismiss();
					}
				});
				RefusedButton.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{

						try
						{
							HttpClient client = new DefaultHttpClient();
							String user = PreferenceManager.getDefaultSharedPreferences(context).getString("ami1", "defaultStringIfNothingFound");
							String URL = Constants.SRV_URL + Constants.SRV_API + "answerRequest?username=" + user + "&validate=false";

							ResponseHandler<String> responseHandler = new BasicResponseHandler();
							HttpGet get = new HttpGet(URL);
							if (PreferenceManager.getDefaultSharedPreferences(context).getString("MYLABEL", "defaultStringIfNothingFound") != "IfNothingFound")
							{
								get.setHeader("Cookie", PreferenceManager.getDefaultSharedPreferences(context).getString("MYLABEL", ""));
							}
							String SetServerString = "";
							SetServerString = client.execute(get, responseHandler);

							JSONObject JSONObject = new JSONObject(SetServerString);
							String name = JSONObject.get("user").toString();
							String[] parts = name.split("\"");

						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
						dialog.dismiss();
					}
				});
				dialog.show();
			}

			if (a_friend[3] != "")
			{
				final Dialog dialog = new Dialog(context);
				dialog.setContentView(R.layout.popup_invite_friends);
				dialog.setTitle("New friends?");

				TextView text = (TextView) dialog.findViewById(R.id.text);
				text.setText(a_friend[1] + " te demande en ami.");
				Button ConfirmButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
				Button RefusedButton = (Button) dialog.findViewById(R.id.dialogButtonNOK);
				ConfirmButton.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						try
						{
							HttpClient client = new DefaultHttpClient();
							String user = PreferenceManager.getDefaultSharedPreferences(context).getString("ami1", "defaultStringIfNothingFound");

							String URL = Constants.SRV_URL + Constants.SRV_API + "answerRequest?username=" + user + "&validate=true";

							ResponseHandler<String> responseHandler = new BasicResponseHandler();
							HttpGet get = new HttpGet(URL);
							if (PreferenceManager.getDefaultSharedPreferences(context).getString("MYLABEL", "defaultStringIfNothingFound") != "IfNothingFound")
							{
								get.setHeader("Cookie", PreferenceManager.getDefaultSharedPreferences(context).getString("MYLABEL", ""));
							}
							String SetServerString = "";
							SetServerString = client.execute(get, responseHandler);

							JSONObject JSONObject = new JSONObject(SetServerString);
							String name = JSONObject.get("user").toString();
							String[] parts = name.split("\"");
							Log.d("MyApp", parts[7]);

						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
						dialog.dismiss();
					}
				});
				RefusedButton.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{

						try
						{
							HttpClient client = new DefaultHttpClient();
							String user = PreferenceManager.getDefaultSharedPreferences(context).getString("ami1", "defaultStringIfNothingFound");
							String URL = Constants.SRV_URL + Constants.SRV_API + "answerRequest?username=" + user + "&validate=false";

							ResponseHandler<String> responseHandler = new BasicResponseHandler();
							HttpGet get = new HttpGet(URL);
							if (PreferenceManager.getDefaultSharedPreferences(context).getString("MYLABEL", "defaultStringIfNothingFound") != "IfNothingFound")
							{
								get.setHeader("Cookie", PreferenceManager.getDefaultSharedPreferences(context).getString("MYLABEL", ""));
							}
							String SetServerString = "";
							SetServerString = client.execute(get, responseHandler);

							JSONObject JSONObject = new JSONObject(SetServerString);
							String name = JSONObject.get("user").toString();
							String[] parts = name.split("\"");

						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
						dialog.dismiss();
					}
				});
				dialog.show();
			}

			if (a_friend[5] != "")
			{
				final Dialog dialog = new Dialog(context);
				dialog.setContentView(R.layout.popup_invite_friends);
				dialog.setTitle("New friends?");

				TextView text = (TextView) dialog.findViewById(R.id.text);
				text.setText(a_friend[1] + " te demande en ami.");
				Button ConfirmButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
				Button RefusedButton = (Button) dialog.findViewById(R.id.dialogButtonNOK);
				ConfirmButton.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						try
						{
							HttpClient client = new DefaultHttpClient();
							String user = PreferenceManager.getDefaultSharedPreferences(context).getString("ami1", "defaultStringIfNothingFound");

							String URL = Constants.SRV_URL + Constants.SRV_API + "answerRequest?username=" + user + "&validate=true";

							ResponseHandler<String> responseHandler = new BasicResponseHandler();
							HttpGet get = new HttpGet(URL);
							if (PreferenceManager.getDefaultSharedPreferences(context).getString("MYLABEL", "defaultStringIfNothingFound") != "IfNothingFound")
							{
								get.setHeader("Cookie", PreferenceManager.getDefaultSharedPreferences(context).getString("MYLABEL", ""));
							}
							String SetServerString = "";
							SetServerString = client.execute(get, responseHandler);

							JSONObject JSONObject = new JSONObject(SetServerString);
							String name = JSONObject.get("user").toString();
							String[] parts = name.split("\"");
							Log.d("MyApp", parts[7]);

						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
						dialog.dismiss();
					}
				});
				RefusedButton.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{

						try
						{
							HttpClient client = new DefaultHttpClient();
							String user = PreferenceManager.getDefaultSharedPreferences(context).getString("ami1", "defaultStringIfNothingFound");
							String URL = Constants.SRV_URL + Constants.SRV_API + "answerRequest?username=" + user + "&validate=false";

							ResponseHandler<String> responseHandler = new BasicResponseHandler();
							HttpGet get = new HttpGet(URL);
							if (PreferenceManager.getDefaultSharedPreferences(context).getString("MYLABEL", "defaultStringIfNothingFound") != "IfNothingFound")
							{
								get.setHeader("Cookie", PreferenceManager.getDefaultSharedPreferences(context).getString("MYLABEL", ""));
							}
							String SetServerString = "";
							SetServerString = client.execute(get, responseHandler);

							JSONObject JSONObject = new JSONObject(SetServerString);
							String name = JSONObject.get("user").toString();
							String[] parts = name.split("\"");

						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
						dialog.dismiss();
					}
				});
				dialog.show();
			}
		}
		catch (Exception ex)
		{
			Log.d("liste", "Fail!");
		}

		String[] listeStrings = {PreferenceManager.getDefaultSharedPreferences(context).getString("friend_1", ""), PreferenceManager.getDefaultSharedPreferences(context).getString("friend_2", "")};
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
				Intent add_f_intent = new Intent(view.getContext(), ChatActivity.class);
				startActivity(add_f_intent);
			}
		});

	}

	public static String getIpAddress()
	{
		try
		{
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();)
			{
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();)
				{
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress())
					{
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		}
		catch (SocketException e)
		{
			// Log.e(Constants.LOG_TAG, e.getMessage(), e);
		}
		return null;
	}
}