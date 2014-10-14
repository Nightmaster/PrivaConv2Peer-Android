package esgi.priva2peer.activity;

import java.net.URLEncoder;
import java.util.ArrayList;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import esgi.priva2peer.R;
import esgi.priva2peer.communication.parser.AddFriendJsonParser;
import esgi.priva2peer.communication.parser.JSONParser;
import esgi.priva2peer.communication.parser.SearchJsonParser;
import esgi.priva2peer.communication.parser.subclasses.UserInfos;
import esgi.priva2peer.data.Constants;

/**
 * @author Bruno Gb
 */
public class AddFriend extends Activity
{
	EditText UserName;
	final Context context = this;

	Button SearchMail, Add;
	TextView result;
	EditText editTextUserName;

	ArrayList<String> list = new ArrayList<String>();

	ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_friend);

		editTextUserName = (EditText) findViewById(R.id.editTextUserName);

		SearchMail = (Button) findViewById(R.id.SearchMail);
		result = (TextView) findViewById(R.id.result);

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
				String userName = editTextUserName.getText().toString();

				String parametre = "";

				if (userName != "") // juste login
				{
					parametre = "?username=" + userName;
				}
				HttpClient Client = new DefaultHttpClient();
				String URL = "http://54.194.20.131:8080/webAPI/search" + parametre;
				Log.d("fd", URL);
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

					SearchJsonParser stAlJson = JSONParser.getSearchParser(SetServerString);
					if (stAlJson.isError() == false)
					{

						for (UserInfos friend : stAlJson.getProfiles())
						{
							Log.d("dfefd", friend.getLogin() + "  /  " + friend.getName() + " / " + friend.getFirstname() + " ");
							result.setText("Login : " + friend.getLogin() + " Nom : " + friend.getName() + " Prénom : " + friend.getFirstname());

							AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

							alertDialogBuilder.setTitle("L'inviter?");

							alertDialogBuilder.setMessage("Inviter à sa liste d'amis?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener()
							{
								@Override
								public void onClick(DialogInterface dialog, int id)
								{
									AddFriend();
									AddFriend.this.finish();
								}

								private void AddFriend()
								{
									// AddFriend();

								}
							}).setNegativeButton("No", new DialogInterface.OnClickListener()
							{
								@Override
								public void onClick(DialogInterface dialog, int id)
								{
									dialog.cancel();
								}
							});

							AlertDialog alertDialog = alertDialogBuilder.create();
							alertDialog.show();
						}

					}
					else
					{
						Toast.makeText(getApplicationContext(), "non connu", Toast.LENGTH_SHORT).show();
					}
				}
				catch (Exception ex)
				{}
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

	protected AddFriend()
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
				AddFriendJsonParser stAlJson = JSONParser.getAddFriendParser(SetServerString);
				if (stAlJson.isInvitationSent())
				{
					Toast.makeText(getApplicationContext(), "Invitation Envoyé", Toast.LENGTH_LONG).show();
				}

			}
			catch (Exception ex)
			{}
		}
	}
}
