package esgi.priva2peer.activity;

import java.net.URLEncoder;
import java.util.ArrayList;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import esgi.priva2peer.R;
import esgi.priva2peer.communication.parser.JSONParser;
import esgi.priva2peer.communication.parser.StayAliveJsonParser;
import esgi.priva2peer.communication.parser.subclasses.Friend;
import esgi.priva2peer.data.Constants;

/**
 * @author Bruno Gb
 */
public class AddFriend extends Activity
{
	EditText UserName;
	final Context context = this;

	Button SearchMail;

	ArrayList<String> list = new ArrayList<String>();

	ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_friend);

		UserName = (EditText) findViewById(R.id.userName);
		SearchMail = (Button) findViewById(R.id.SearchMail);

		// new Thread(new Server()).start();

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
			Log.d("Json", "yes = " + SetServerString);

			StayAliveJsonParser stAlJson = JSONParser.getStayAliveParser(SetServerString);
			stAlJson.getFriendList();
			for (Friend friend : stAlJson.getFriendList())
				Log.d("dfefd", friend.getUsername() + "  espace  " + friend.isConnected());
		}
		catch (Exception ex)
		{
			Log.d("liste", "Fail! 22222");
		}

		SearchMail.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				String userName = UserName.getText().toString();

				if (userName.equals(""))
				{
					Toast.makeText(getApplicationContext(), "Field Vaccant", Toast.LENGTH_LONG).show();
					return;
				}
				else
				{
					String caractere = "@";
					String login = "";
					boolean trouve = userName.contains(caractere);
					if (trouve == true)
					{
						login += "email=";
					}
					else
					{
						login += "username=";
					}
					try
					{
						userName = URLEncoder.encode(userName, "UTF-8");

					}
					catch (Exception e)
					{}
					HttpClient Client = new DefaultHttpClient();
					try
					{
						String URL = Constants.SRV_URL + Constants.SRV_API + "addFriend?" + login + userName;

						HttpGet httpget = new HttpGet(URL);
						ResponseHandler<String> responseHandler = new BasicResponseHandler();
						if (PreferenceManager.getDefaultSharedPreferences(context).getString("MYLABEL", "defaultStringIfNothingFound") != "defaultStringIfNothingFound")
						{
							httpget.setHeader("Cookie", PreferenceManager.getDefaultSharedPreferences(context).getString("MYLABEL", "defaultStringIfNothingFound"));
						}

						String SetServerString = "";
						SetServerString = Client.execute(httpget, responseHandler);
					}
					catch (Exception ex)
					{}

				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.layout.add_friend, menu);
		return true;
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}

}
