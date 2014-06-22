package esgi.priva2peer.activity;

import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.MessageDigest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import esgi.priva2peer.R;
import esgi.priva2peer.communication.UserSessionManager;
import esgi.priva2peer.data.LoginDataBaseAdapter;

public class Home extends Activity
{
	final Context context = this;

	UserSessionManager session;

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

		session = new UserSessionManager(getApplicationContext());

		/*
		 * HashMap<String, String> user = session.getUserDetails(); String name
		 * = user.get(UserSessionManager.KEY_NAME); String email =
		 * user.get(UserSessionManager.KEY_EMAIL); String last_n =
		 * user.get(UserSessionManager.KEY_FirstName); String first_n =
		 * user.get(UserSessionManager.KEY_LastName);
		 */
		// Session value

		// Toast.makeText(getApplicationContext(), "Pseudo : " + name +
		// " MAil : " + email + " prenom : " + first_n + " nom : " + last_n,
		// Toast.LENGTH_LONG).show();

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

				if (password.equals(storedPassword))
				{

					// TODO envoie requete pour se loguer + session de connexion
					// serveur + response json

					URL url = null;
					try
					{
						MessageDigest m = MessageDigest.getInstance("MD5");
						m.reset();
						m.update(userName.getBytes());
						byte[] digest = m.digest();
						BigInteger bigInt = new BigInteger(1, digest);
						String hashtext = bigInt.toString(16);
						while (hashtext.length() < 32)
						{
							hashtext = "0" + hashtext;
						}

						String caractere = "@";
						String truc;
						boolean trouve = (userName.indexOf(caractere) != -1);

						System.out.println("http://54.194.20.131:8080/webAPI/connect?" + "username=" + userName + "&pw=" + hashtext);
						String registrationUrl = String.format("http://54.194.20.131:8080/webAPI/connect?" + "username=" + userName + "&pw=" + hashtext, userName, URLEncoder.encode(userName, "UTF-8"));
						url = new URL(registrationUrl);
						URLConnection connection = url.openConnection();
						HttpURLConnection httpConnection = (HttpURLConnection) connection;
						int responseCode = httpConnection.getResponseCode();
						if (responseCode == HttpURLConnection.HTTP_OK)
						{
							Log.d("MyApp", "Registration success");
						}
						else
						{
							Log.w("MyApp", "Registration failed for: " + registrationUrl);
						}
					}
					catch (Exception ex)
					{
						ex.printStackTrace();
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
