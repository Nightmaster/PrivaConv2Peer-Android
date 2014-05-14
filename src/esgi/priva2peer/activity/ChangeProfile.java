package esgi.priva2peer.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import esgi.priva2peer.R;
import esgi.priva2peer.data.LoginDataBaseAdapter;

public class ChangeProfile extends Activity
{
	EditText editTextUserName, editTextPassword, editTextConfirmPassword, editTextUserMail, editTextFirstName, editTextLastName;
	LoginDataBaseAdapter loginDataBaseAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_profile);

		loginDataBaseAdapter = new LoginDataBaseAdapter(this);
		loginDataBaseAdapter = loginDataBaseAdapter.open();

		editTextUserName = (EditText) findViewById(R.id.editTextUserName);
		editTextUserMail = (EditText) findViewById(R.id.editTextUserMail);
		editTextFirstName = (EditText) findViewById(R.id.editTextFirstName);
		editTextLastName = (EditText) findViewById(R.id.editTextLastName);

		editTextPassword = (EditText) findViewById(R.id.editTextPassword);
		editTextConfirmPassword = (EditText) findViewById(R.id.editTextConfirmPassword);

		Button btnchangedProfile;

		btnchangedProfile = (Button) findViewById(R.id.profile_changed);

		btnchangedProfile.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				String userName = editTextUserName.getText().toString();
				String userMail = editTextUserMail.getText().toString();

				String lastName = editTextLastName.getText().toString();
				String firstName = editTextFirstName.getText().toString();

				String password = editTextPassword.getText().toString();
				String confirmPassword = editTextConfirmPassword.getText().toString();

				if (userName.equals("") || password.equals("") || confirmPassword.equals(""))
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
					// Save the Data in Database
					loginDataBaseAdapter.insertEntry(userName, password, userMail, firstName, lastName);
					Toast.makeText(getApplicationContext(), "Account Successfully Created ", Toast.LENGTH_LONG).show();
				}

				Intent Listintent = new Intent(getApplicationContext(), ListActivity.class);
				startActivity(Listintent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.layout.change_profile, menu);
		return true;
	}

}
