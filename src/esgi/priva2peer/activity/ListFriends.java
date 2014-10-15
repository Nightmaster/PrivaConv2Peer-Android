package esgi.priva2peer.activity;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;
import android.annotation.SuppressLint;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import esgi.priva2peer.R;
import esgi.priva2peer.UserSessionManager;
import esgi.priva2peer.communication.parser.JSONParser;
import esgi.priva2peer.communication.parser.StayAliveJsonParser;
import esgi.priva2peer.communication.parser.subclasses.Friend;
import esgi.priva2peer.data.Constants;

/**
 * @author Bruno Gb
 * @param <CustomList>
 */
@SuppressLint("ViewHolder")
public class ListFriends extends Activity
{
	// User Session Manager Class
	UserSessionManager session;
	final Context context = this;
	public String friend_selected;

	public static DefaultHttpClient getThreadSafeClient()
	{
		DefaultHttpClient client = new DefaultHttpClient();
		ClientConnectionManager mgr = client.getConnectionManager();
		HttpParams params = client.getParams();

		client = new DefaultHttpClient(new ThreadSafeClientConnManager(params, mgr.getSchemeRegistry()), params);

		return client;
	}

	@SuppressWarnings("unused")
	@SuppressLint("CutPasteId")
	@Override
	public void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.friendlist);
		Button btnAddFriends, btnchangeProfile;
		ListView listfriends;

		Home ui = new Home();
		session = new UserSessionManager(getApplicationContext());
		btnAddFriends = (Button) findViewById(R.id.buttonAddFriends);
		btnchangeProfile = (Button) findViewById(R.id.changeProfile);
		String d_ami1 = null;
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
			Log.d("sdfv", SetServerString);
			StayAliveJsonParser stAlJson = JSONParser.getStayAliveParser(SetServerString);
			Friend[] fl = stAlJson.getFriendList();
			Log.d("fds", "longueur" + fl.length);
			final String[] str = new String[fl.length];
			final Boolean[] state = new Boolean[fl.length];
			Integer[] imageId = new Integer[fl.length];
			for (int i = 0; i < state.length; i++ )
			{
				Log.d("for", fl[i].getUsername() + fl[i].isConnected());
				str[i] = fl[i].getUsername();
				state[i] = fl[i].isConnected();
				imageId[i] = state[i] ? R.drawable.greenstar : R.drawable.redstar;
			}

			CustomList adapter = new CustomList(ListFriends.this, str, imageId);
			ListView friends_row = (ListView) findViewById(R.id.friends_row);
			friends_row.setAdapter(adapter);
			friends_row.setOnItemClickListener(new AdapterView.OnItemClickListener()
			{
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id)
				{
					if (state[position] == true)
					{
						Intent select_f_intent = new Intent(view.getContext(), ChatActivity.class);
						select_f_intent.putExtra("selected_item", str[position].toString());
						startActivity(select_f_intent);
					}
					else
					{
						Toast.makeText(getApplicationContext(), str[position] + " non connecté", Toast.LENGTH_SHORT).show();
					}
				}
			});

			JSONObject friend_Object = new JSONObject(SetServerString);
			String friends = friend_Object.get("friends").toString();
			String[] parts = friends.split("\"");
			String askfriend = friend_Object.get("askFriend").toString();
			String[] a_friend = askfriend.split("\"");
			d_ami1 = a_friend[1];
			if (d_ami1 != "")
			{
				final Dialog dialog = new Dialog(context);
				dialog.setContentView(R.layout.popup_invite_friends);
				dialog.setTitle("New friends?");
				final String user = d_ami1;
				TextView text = (TextView) dialog.findViewById(R.id.text);
				text.setText(user + " te demande en ami.");

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
		{}

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

	}

	public static String getIpAddress()
	{
		String ip = null;
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
						ip = inetAddress.getHostAddress().toString();
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		}
		catch (SocketException e)
		{}
		return ip;
	}

	@Override
	protected void onDestroy()
	{
		session.logoutUser();
		HttpClient Client = new DefaultHttpClient();
		String URL = Constants.SRV_URL + Constants.SRV_API + "disconnect";
		try
		{
			HttpGet httpget = new HttpGet(URL);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			if (PreferenceManager.getDefaultSharedPreferences(context).getString("MYLABEL", "defaultStringIfNothingFound") != "defaultStringIfNothingFound")
			{
				httpget.setHeader("Cookie", PreferenceManager.getDefaultSharedPreferences(context).getString("MYLABEL", "defaultStringIfNothingFound"));
			}

			String SetServerString = "";
			SetServerString = Client.execute(httpget, responseHandler);
			android.os.Process.killProcess(android.os.Process.myPid());
			finish();
			System.exit(0);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		super.onDestroy();
	}
}