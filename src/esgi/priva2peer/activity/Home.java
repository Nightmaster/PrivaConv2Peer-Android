package esgi.priva2peer.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import esgi.priva2peer.R;
import esgi.priva2peer.data.LoginDataBaseAdapter;

public class Home extends Activity
{

	Button btnSignIn, btnSignUp;
	LoginDataBaseAdapter loginDataBaseAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		// create a instance of SQLite Database
		loginDataBaseAdapter = new LoginDataBaseAdapter(this);
		loginDataBaseAdapter = loginDataBaseAdapter.open();

		// Get The Refference Of Buttons
		btnSignIn = (Button) findViewById(R.id.buttonSignIN);
		btnSignUp = (Button) findViewById(R.id.buttonSignUP);

		// Set OnClick Listener on SignUp button
		btnSignUp.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				// TODO Auto-generated method stub

				// / Create Intent for SignUpActivity abd Start The Activity
				Intent intentSignUP = new Intent(getApplicationContext(), SignUPActivity.class);
				startActivity(intentSignUP);
			}
		});
	}

	public void signIn(View V)
	{
		final Dialog dialog = new Dialog(Home.this);
		dialog.setContentView(R.layout.login);
		dialog.setTitle("Login");

		// get the Refferences of views
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

				String storedPassword = loginDataBaseAdapter.getSinlgeEntry(userName);

				if (password.equals(storedPassword))
				{
					Intent list_f_intent = new Intent(getApplicationContext(), ListActivity.class);
					startActivity(list_f_intent);
					Toast.makeText(Home.this, "Login Successfull ! Hello" + userName, Toast.LENGTH_LONG).show();
					System.out.println("Login Successfull ! Hello" + userName);
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
