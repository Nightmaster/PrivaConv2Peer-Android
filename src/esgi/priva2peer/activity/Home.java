package esgi.priva2peer.activity;

import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import esgi.priva2peer.R;
import esgi.priva2peer.UserSessionManager;
import esgi.priva2peer.communication.parser.ConnectionJsonParser;
import esgi.priva2peer.communication.parser.JSONParser;
import esgi.priva2peer.communication.parser.PrivateKeyJsonParser;
import esgi.priva2peer.data.Constants;

/**
 * @author Bruno Gb
 */
public class Home extends Activity
{
	protected static final String LOGTAG = null;

	final Context context = this;
	public String cookie_sessId = " ";
	public String friends = " ";

	UserSessionManager session;
	TextView content;

	Button btnSignIn, btnSignUp, btnLogout;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		session = new UserSessionManager(getApplicationContext());
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		btnSignIn = (Button) findViewById(R.id.buttonSignIN);
		btnSignUp = (Button) findViewById(R.id.buttonSignUP);
		btnLogout = (Button) findViewById(R.id.btnLogout);

		btnSignUp.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{

				Intent intentSignUP = new Intent(getApplicationContext(), SignUPActivity.class);
				startActivity(intentSignUP);
			}
		});

		btnLogout.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View arg0)
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
						Log.d("deco ok", "ui");
					}

					String SetServerString = "";
					SetServerString = Client.execute(httpget, responseHandler);
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}

			}
		});

	}

	public void signIn(View V)
	{
		final Dialog dialog = new Dialog(Home.this);
		dialog.setContentView(R.layout.login);
		dialog.setTitle("Login");

		final EditText editTextUserName = (EditText) dialog.findViewById(R.id.editTextUserNameToLogin);
		final EditText editTextPassword = (EditText) dialog.findViewById(R.id.editTextPasswordToLogin);

		Button btnSignIn = (Button) dialog.findViewById(R.id.buttonSignIn);
		btnSignIn.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				String userName = editTextUserName.getText().toString();
				String password = editTextPassword.getText().toString();
				try
				{
					userName = URLEncoder.encode(userName, "UTF-8");

				}
				catch (Exception e)
				{}

				MessageDigest m = null;
				try
				{
					m = MessageDigest.getInstance("MD5");
				}
				catch (NoSuchAlgorithmException e)
				{
					e.printStackTrace();
				}
				m.reset();
				m.update(password.getBytes());
				byte[] digest = m.digest();
				BigInteger bigInt = new BigInteger(1, digest);
				String hashtext = bigInt.toString(16);
				while (hashtext.length() < 32)
				{
					hashtext = "0" + hashtext;
				}

				String caractere = "@";
				String login = "";
				boolean trouve = editTextUserName.getText().toString().contains(caractere);
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

					String URL = Constants.SRV_URL + Constants.SRV_API + "connect?" + login + editTextUserName.getText().toString() + "&pw=" + hashtext;
					HttpClient client = new DefaultHttpClient();
					Log.d("fd", URL);
					ResponseHandler<String> responseHandler = new BasicResponseHandler();
					HttpGet get = new HttpGet(URL);
					get.setHeader("Cookie", "sessId");
					HttpResponse responseGet = client.execute(get);
					String SetServerString = "";
					SetServerString = client.execute(get, responseHandler);

					JSONObject JSONObject = new JSONObject(SetServerString);
					String name = JSONObject.get("user").toString();
					String[] parts = name.split("\"");

					friends = SetServerString;
					session = new UserSessionManager(getApplicationContext());
					session.createUserLoginSession(parts[7], parts[3], parts[11], parts[15]);
					Header[] headers = responseGet.getAllHeaders();

					for (int i = 0; i < headers.length; i++ )
					{
						Header header = headers[i];
						if (header.getValue().contains("sessId") == true)
						{
							String sessId = header.getValue();
							String[] sess = sessId.split(";");
							String content_sesId = sess[0];
							PreferenceManager.getDefaultSharedPreferences(context).edit().putString("MYLABEL", content_sesId).commit();
							cookie_sessId = content_sesId;
							Log.d("sessId", PreferenceManager.getDefaultSharedPreferences(context).getString("MYLABEL", "defaultStringIfNothingFound"));

						}
					}

					ConnectionJsonParser stAlJson = JSONParser.getConnectionParser(SetServerString);
					if (stAlJson.isConnectionValidated())
					{

						Intent list_f_intent = new Intent(getApplicationContext(), MainActivity.class);
						// getPrivateKey(editTextUserName.getText().toString());
						// list_f_intent.putExtra("key_private",
						// getPrivateKey(editTextUserName.getText().toString()).toString());
						list_f_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(list_f_intent);
						dialog.dismiss();
					}
					else
					{
						Toast.makeText(getApplicationContext(), "Login ou mot de passe non valide", Toast.LENGTH_LONG).show();
					}

				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
		dialog.show();
	}

	public String getPrivateKey(String username)
	{
		HttpClient Client = new DefaultHttpClient();
		String URL = Constants.SRV_URL + Constants.SRV_API + "getPrivateKey/" + username;
		String key = null;

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
			PrivateKeyJsonParser stAlJson = JSONParser.getPrivateKeyParser(SetServerString);
			if (!stAlJson.isError())
			{
				String[] parts = SetServerString.split("\"");
				key = parts[5];
				// Log.d("PrivK", parts[5]); // clef public de l'ami
				//
			}
		}
		catch (Exception ex)
		{}
		return key;
	}

	@Override
	protected void onPause()
	{
		super.onPause();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
	}

	@Override
	protected void onStart()
	{
		super.onStart();
	}

	@Override
	protected void onStop()
	{
		super.onStop();
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
