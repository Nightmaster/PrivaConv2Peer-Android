package esgi.priva2peer.activity;

import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import esgi.priva2peer.R;
import esgi.priva2peer.UserSessionManager;
import esgi.priva2peer.data.Constants;

/**
 * @author Bruno Gb
 */
public class ChangeProfile extends Activity
{

	UserSessionManager session;
	final Context context = this;

	EditText editTextUserName, editTextPassword, editTextConfirmPassword, editTextUserMail, editTextFirstName, editTextLastName;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_profile);

		session = new UserSessionManager(getApplicationContext());
		HashMap<String, String> user = session.getUserDetails();

		editTextUserName = (EditText) findViewById(R.id.editTextUserName);
		editTextUserMail = (EditText) findViewById(R.id.editTextUserMail);
		editTextLastName = (EditText) findViewById(R.id.editTextLastName);
		editTextFirstName = (EditText) findViewById(R.id.editTextFirstName);
		editTextPassword = (EditText) findViewById(R.id.editTextPassword);
		editTextConfirmPassword = (EditText) findViewById(R.id.editTextConfirmPassword);

		Button btnchangedProfile;

		btnchangedProfile = (Button) findViewById(R.id.profile_changed);

		btnchangedProfile.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				String userMail = editTextUserMail.getText().toString();
				String userName = editTextUserName.getText().toString();

				String lastname = editTextLastName.getText().toString();
				String firstname = editTextFirstName.getText().toString();

				String password = editTextPassword.getText().toString();
				String confirmPassword = editTextConfirmPassword.getText().toString();

				try
				{
					userMail = URLEncoder.encode(userMail, "UTF-8");
					userName = URLEncoder.encode(userName, "UTF-8");
					lastname = URLEncoder.encode(lastname, "UTF-8");
					firstname = URLEncoder.encode(firstname, "UTF-8");

				}
				catch (Exception e)
				{}

				if (password.equals("") || confirmPassword.equals(""))
				{
					Toast.makeText(getApplicationContext(), "Field Vaccant", Toast.LENGTH_LONG).show();
					return;
				}
				// check if both password matches
				if (!password.equals(confirmPassword))
				{
					Toast.makeText(getApplicationContext(), "Password does not match", Toast.LENGTH_LONG).show();
					return;
				}
				else
				{
					HttpClient Client = new DefaultHttpClient();
					try
					{
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
						String parametre = "";
						if (userName != "")
							parametre = "?username=" + userName;
						if (userMail != "")
							parametre = (parametre == "") ? "?email=" + userMail : "&email=" + userMail;
						if (lastname != "")
							parametre = (parametre == "") ? "?name=" + lastname : "&name=" + lastname;
						if (firstname != "")
							parametre = (parametre == "") ? "?firstname=" + firstname : "&firstname=" + firstname;
						if (hashtext != "" && (password != "" && confirmPassword != ""))
							parametre = (parametre == "") ? "?pw=" + hashtext : "&pw=" + hashtext;

						String URL = Constants.SRV_URL + Constants.SRV_API + "updateInfos" + parametre;
						HttpGet httpget = new HttpGet(URL);
						ResponseHandler<String> responseHandler = new BasicResponseHandler();
						if (PreferenceManager.getDefaultSharedPreferences(context).getString("MYLABEL", "defaultStringIfNothingFound") != "defaultStringIfNothingFound")
						{
							httpget.setHeader("Cookie", PreferenceManager.getDefaultSharedPreferences(context).getString("MYLABEL", "defaultStringIfNothingFound"));
						}

						String SetServerString = Client.execute(httpget, responseHandler);
					}
					catch (Exception ex)
					{
						Log.i("json response", "fail!");
					}
					Toast.makeText(getApplicationContext(), "Account Successfully Created ", Toast.LENGTH_LONG).show();
				}

				Intent Listintent = new Intent(getApplicationContext(), ListFriends.class);
				startActivity(Listintent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.layout.change_profile, menu);
		return true;
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
