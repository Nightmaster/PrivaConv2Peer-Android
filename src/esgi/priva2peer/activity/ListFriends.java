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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import esgi.priva2peer.R;
import esgi.priva2peer.UserSessionManager;
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
		final ListView listfriends;

		Log.d("ddsqd", getIpAddress()); // adresse local
		Home ui = new Home();

		session = new UserSessionManager(getApplicationContext());

		btnAddFriends = (Button) findViewById(R.id.buttonAddFriends);
		btnchangeProfile = (Button) findViewById(R.id.changeProfile);

		listfriends = (ListView) findViewById(R.id.friends_row);
		String d_ami1, friend_1, friend_2, friend_3, friend_4, friend_5, friend_6 = null;
		String state_1, state_2, state_3, state_4, state_5;
		HttpClient Client = new DefaultHttpClient();
		Log.d("json", ui.cookie_sessId);
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
			Log.d("jsonlist", SetServerString);
			Log.d("preferences", PreferenceManager.getDefaultSharedPreferences(context).getString("MYLABEL", ""));

			// Log.d("Json", "yes = " + SetServerString);

			JSONObject friend_Object = new JSONObject(SetServerString);
			String friends = friend_Object.get("friends").toString();
			String[] parts = friends.split("\"");

			Log.d("fdgdsgf", parts[3]);

			friend_1 = parts[3];
			friend_2 = parts[9];
			friend_3 = parts[15];
			String un, deux, trois = "";
			Log.d("chelou", "2");
			if (parts[6].contains("true"))
			{
				un = "en ligne";
			}
			else
			{
				un = "deco";
			}
			if (parts[12].contains("true"))
			{
				deux = "en ligne";

			}
			else
			{
				deux = "deco";
			}
			if (parts[18].contains("true"))
			{
				trois = "en ligne";

			}
			else
			{
				trois = "deco";
			}
			Log.d("fdf", friend_1);

			String[] listeStrings = {friend_1 + " " + un, friend_2 + " " + deux, friend_3 + " " + trois};
			listfriends.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listeStrings));

			String askfriend = friend_Object.get("askFriend").toString();
			String[] a_friend = askfriend.split("\"");
			d_ami1 = a_friend[1];

			Log.d("vfdjiksf", a_friend[1]);
			if (d_ami1 != "")
			{
				final Dialog dialog = new Dialog(context);
				dialog.setContentView(R.layout.popup_invite_friends);
				dialog.setTitle("New friends?");
				final String user = d_ami1;
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
							Log.d("refuse", "Fail!");
							e.printStackTrace();
						}
						dialog.dismiss();
					}
				});
				dialog.show();
			}

			state_1 = parts[6];
			state_2 = parts[12];
			state_3 = parts[18];
			state_4 = parts[24];

		}
		catch (Exception ex)
		{
			Log.d("first try", "Fail!");
		}

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

				friend_selected = (String) ((TextView) view).getText();
				String[] friend = friend_selected.split(" ");
				PreferenceManager.getDefaultSharedPreferences(context).edit().putString("friend_selected", friend[0]).commit();

				if (friend[1].contains("deco"))
				{
					Toast.makeText(getApplicationContext(), "Contact non connecté", Toast.LENGTH_SHORT).show();
				}
				else
				{
					Intent add_f_intent = new Intent(view.getContext(), ChatActivity.class);
					startActivity(add_f_intent);
				}

			}
		});

	}

	public static String rechercheMotCle(String texte, String keyword)
	{
		String resultat = "";
		int count = 0;
		int index = texte.indexOf(keyword);
		// incrémenter le compteur à chaque fois qu'une occurence est trouvée
		while (index != -1)
		{
			++count;
			resultat = resultat + index + " ";
			index = texte.indexOf(keyword, index + 1);
		}
		// Formatage de résultat
		resultat = "" + count;
		return resultat;
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