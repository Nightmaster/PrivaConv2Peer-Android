package esgi.priva2peer.activity;

import java.util.HashMap;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import esgi.priva2peer.R;
import esgi.priva2peer.communication.UserSessionManager;
import esgi.priva2peer.data.LoginDataBaseAdapter;

public class Home extends Activity
{

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

		HashMap<String, String> user = session.getUserDetails();
		String name = user.get(UserSessionManager.KEY_NAME);
		String email = user.get(UserSessionManager.KEY_EMAIL);
		String last_n = user.get(UserSessionManager.KEY_FirstName);
		String first_n = user.get(UserSessionManager.KEY_LastName);

		// Session value

		Toast.makeText(getApplicationContext(), "Pseudo : " + name + " MAil : " + email + " prenom : " + first_n + " nom : " + last_n, Toast.LENGTH_LONG).show();

		// Set OnClick Listener on SignUp button
		btnSignUp.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				// connexion serveur

				Intent intentSignUP = new Intent(getApplicationContext(), SignUPActivity.class);
				startActivity(intentSignUP);
			}
		});

		btnLogout.setOnClickListener(new View.OnClickListener()
		{

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
