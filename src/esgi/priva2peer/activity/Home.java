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
	final Context context = this;

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

		// Session value

		Toast.makeText(getApplicationContext(), "Pseudo : " + name + "\nMail : " + email + "\nprenom : " + first_n + "\nnom : " + last_n, Toast.LENGTH_LONG).show();

		// Set OnClick Listener on SignUp button
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

				String storedPassword = loginDataBaseAdapter.getPseudo(userName);
				String user_mail = loginDataBaseAdapter.getMail(userName);
				String firstname = loginDataBaseAdapter.getFirstname(userName);
				String lastname = loginDataBaseAdapter.getLastname(userName);
				try
				{
					userName = URLEncoder.encode(userName, "UTF-8");
					user_mail = URLEncoder.encode(user_mail, "UTF-8");
					firstname = URLEncoder.encode(firstname, "UTF-8");
					lastname = URLEncoder.encode(lastname, "UTF-8");
				}
				catch (Exception e)
				{}

				if (password.equals(storedPassword))
				{

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
					boolean trouve = (password.indexOf(caractere) != -1);
					if (trouve == true)
					{
						login += "email=";
					}
					else
					{
						login += "username=";
					}

					Log.d("MyApp", "http://54.194.20.131:8080/webAPI/connect?" + login + userName + "&pw=" + hashtext);

					HttpClient Client = new DefaultHttpClient();
					String URL = "http://54.194.20.131:8080/webAPI/connect?" + login + userName + "&pw=" + hashtext;
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

					session = new UserSessionManager(getApplicationContext());
					Intent list_f_intent = new Intent(getApplicationContext(), MainActivity.class);
					session.createUserLoginSession(userName, user_mail, firstname, lastname);
					list_f_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(list_f_intent);
					dialog.dismiss();
				}
				else
				{
					Toast.makeText(Home.this, "User Name or Password does not match", Toast.LENGTH_LONG).show();
				}
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
