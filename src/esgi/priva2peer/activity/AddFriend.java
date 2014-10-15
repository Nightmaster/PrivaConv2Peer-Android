package esgi.priva2peer.activity;

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
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
	EditText editTextUserName;
	ListView searchList;
	TextView info;

	ArrayList<String> list = new ArrayList<String>();

	ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_friend);

		editTextUserName = (EditText) findViewById(R.id.editTextUserName);
		searchList = (ListView) findViewById(R.id.resultL);

		SearchMail = (Button) findViewById(R.id.SearchMail);
		info = (TextView) findViewById(R.id.info);

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

		}
		catch (Exception ex)
		{}

		SearchMail.setOnClickListener(new View.OnClickListener() // Recherche
																	// D'amis
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

							SearchJsonParser stAlJson = JSONParser.getSearchParser(SetServerString);
							if (!stAlJson.isError())
							{
								final UserInfos[] us = stAlJson.getProfiles();
								String[] list = new String[us.length];
								for (int i = 0; i < us.length; i++ )
								{
									list[i] = "Login : " + us[i].getLogin() + " \nNom : " + us[i].getName() + " \nPrénom : " + us[i].getFirstname();

								}

								ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(AddFriend.this, android.R.layout.simple_list_item_1, list);

								searchList.setAdapter(arrayAdapter);
								searchList.setOnItemClickListener(new OnItemClickListener()
								{
									@Override
									public void onItemClick(AdapterView<?> parent, final View view, int position, long id)
									{
										AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
										alertDialogBuilder.setTitle("Demande d'invitation");
										final String userSelected = us[position].getLogin();

										alertDialogBuilder.setMessage("Ajouter " + userSelected + " à sa liste d'amis?").setCancelable(false).setPositiveButton("Oui", new DialogInterface.OnClickListener()
										{
											@Override
											public void onClick(DialogInterface dialog, int id)
											{

												HttpClient Client = new DefaultHttpClient();
												try
												{
													String field = "username=";
													String URL = Constants.SRV_URL + Constants.SRV_API + "addFriend?" + field + userSelected;
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
												Intent select_f_intent = new Intent(view.getContext(), ListFriends.class);
												startActivity(select_f_intent);
												AddFriend.this.finish();
											}

										}).setNegativeButton("Non", new DialogInterface.OnClickListener()
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
								});
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
		HttpClient Client = new DefaultHttpClient();
		String URL = Constants.SRV_URL + Constants.SRV_API + "disconnect";
		try
		{
			HttpGet httpget = new HttpGet(URL);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			if (PreferenceManager.getDefaultSharedPreferences(context).getString("MYLABEL", "defaultStringIfNothingFound") != "defaultStringIfNothingFound")
			{
				httpget.setHeader("Cookie", PreferenceManager.getDefaultSharedPreferences(context).getString("MYLABEL", "defaultStringIfNothingFound"));
				Log.d("deco ok", "ui");
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
