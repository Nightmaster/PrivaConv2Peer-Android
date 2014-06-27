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
import org.json.JSONObject;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import esgi.priva2peer.R;
import esgi.priva2peer.communication.UserSessionManager;
import esgi.priva2peer.data.LoginDataBaseAdapter;

public class Home extends Activity
{
	protected static final String LOGTAG = null;

	final Context context = this;

	private DefaultHttpClient httpClient;
	private String someVariable;
	public static org.apache.http.cookie.Cookie cookie = null;

	UserSessionManager session;
	TextView content;

	Button btnSignIn, btnSignUp, btnLogout;
	LoginDataBaseAdapter loginDataBaseAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		session = new UserSessionManager(getApplicationContext());
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		// create a instance of SQLite Database
		loginDataBaseAdapter = new LoginDataBaseAdapter(this);
		loginDataBaseAdapter = loginDataBaseAdapter.open();

		btnSignIn = (Button) findViewById(R.id.buttonSignIN);
		btnSignUp = (Button) findViewById(R.id.buttonSignUP);
		btnLogout = (Button) findViewById(R.id.btnLogout);
		content = (TextView) findViewById(R.id.content);

		session = new UserSessionManager(getApplicationContext());

		HashMap<String, String> user = session.getUserDetails();
		String name = user.get(UserSessionManager.KEY_NAME);
		String email = user.get(UserSessionManager.KEY_EMAIL);
		String last_n = user.get(UserSessionManager.KEY_FirstName);
		String first_n = user.get(UserSessionManager.KEY_LastName);

		Toast.makeText(getApplicationContext(), "Pseudo : " + name + "\nMail : " + email + "\nprenom : " + first_n + "\nnom : " + last_n, Toast.LENGTH_LONG).show();

		// final Dialog dialog = new Dialog(context);
		// dialog.setContentView(R.layout.popup_invite_friends);
		// dialog.setTitle("New friends?");
		//
		// // set the custom dialog components - text, image and button
		// TextView text = (TextView) dialog.findViewById(R.id.text);
		// text.setText("Do you know that guy?");
		// Button dialogButton = (Button)
		// dialog.findViewById(R.id.dialogButtonOK);
		// // if button is clicked, close the custom dialog
		// dialogButton.setOnClickListener(new OnClickListener()
		// {
		// @Override
		// public void onClick(View v)
		// {
		// dialog.dismiss();
		// }
		// });
		// dialog.show();

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
				String URL = "http://54.194.20.131:8080/webAPI/disconnect?";
				try
				{
					HttpGet httpget = new HttpGet(URL);
					ResponseHandler<String> responseHandler = new BasicResponseHandler();

					String SetServerString = "";
					SetServerString = Client.execute(httpget, responseHandler);
					content.setText(SetServerString);
				}
				catch (Exception ex)
				{
					// content.setText("Fail!");
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

		// Set On ClickListener
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
					password = URLEncoder.encode(password, "UTF-8");

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
					// TODO Auto-generated catch block
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

				Log.d("MyApp", "http://54.194.20.131:8080/webAPI/connect?" + login + editTextUserName.getText().toString() + "&pw=" + hashtext);

				HttpClient Client = new DefaultHttpClient();

				String URL = "http://54.194.20.131:8080/webAPI/connect?" + login + editTextUserName.getText().toString() + "&pw=" + hashtext;
				try
				{
					HttpGet httpget = new HttpGet(URL);

					ResponseHandler<String> responseHandler = new BasicResponseHandler();

					String SetServerString = "";
					SetServerString = Client.execute(httpget, responseHandler);
					content.setText(SetServerString);
					Log.d("MyApp", SetServerString);

					JSONObject obj = new JSONObject(SetServerString);

					JSONObject user = obj.getJSONObject("user");
					Log.d("MyApp", user.getString("login"));

				}
				catch (Exception ex)
				{
					// content.setText("Fail!");
				}

				session = new UserSessionManager(getApplicationContext());
				Intent list_f_intent = new Intent(getApplicationContext(), MainActivity.class);
				// session.createUserLoginSession(userName, user_mail,
				// firstname, lastname);
				list_f_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				// startActivity(list_f_intent);
				dialog.dismiss();

			}
		});

		dialog.show();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		// Close The Database
		loginDataBaseAdapter.close();
	}
}
