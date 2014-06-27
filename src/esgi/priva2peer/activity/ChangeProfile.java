package esgi.priva2peer.activity;

import java.net.URLEncoder;
import java.util.HashMap;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import esgi.priva2peer.R;
import esgi.priva2peer.communication.UserSessionManager;
import esgi.priva2peer.data.LoginDataBaseAdapter;

public class ChangeProfile extends Activity
{

	UserSessionManager session;

	EditText editTextUserName, editTextPassword, editTextConfirmPassword, editTextUserMail, editTextFirstName, editTextLastName, SecurePassword, ConfirmSecurePassword;
	LoginDataBaseAdapter loginDataBaseAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_profile);

		session = new UserSessionManager(getApplicationContext());

		HashMap<String, String> user = session.getUserDetails();
		String name = user.get(UserSessionManager.KEY_NAME);
		String email = user.get(UserSessionManager.KEY_EMAIL);
		String last_n = user.get(UserSessionManager.KEY_FirstName);
		String first_n = user.get(UserSessionManager.KEY_LastName);

		Toast.makeText(getApplicationContext(), "Pseudo : " + name + " MAil : " + email + " prenom : " + first_n + " nom : " + last_n, Toast.LENGTH_LONG).show();

		loginDataBaseAdapter = new LoginDataBaseAdapter(this);
		loginDataBaseAdapter = loginDataBaseAdapter.open();
		editTextUserMail = (EditText) findViewById(R.id.editTextUserMail);
		editTextFirstName = (EditText) findViewById(R.id.editTextFirstName);
		editTextLastName = (EditText) findViewById(R.id.editTextLastName);
		editTextPassword = (EditText) findViewById(R.id.editTextPassword);
		editTextConfirmPassword = (EditText) findViewById(R.id.editTextConfirmPassword);
		SecurePassword = (EditText) findViewById(R.id.SecurePassword);
		ConfirmSecurePassword = (EditText) findViewById(R.id.ConfirmSecurePassword);

		Button btnchangedProfile;

		btnchangedProfile = (Button) findViewById(R.id.profile_changed);

		btnchangedProfile.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				String userMail = editTextUserMail.getText().toString();

				String lastName = editTextLastName.getText().toString();
				String firstName = editTextFirstName.getText().toString();

				String password = editTextPassword.getText().toString();
				String confirmPassword = editTextConfirmPassword.getText().toString();

				String securePassword = SecurePassword.getText().toString();
				String confirmSecurePassword = ConfirmSecurePassword.getText().toString();

				try
				{
					userMail = URLEncoder.encode(userMail, "UTF-8");
					firstName = URLEncoder.encode(firstName, "UTF-8");
					lastName = URLEncoder.encode(lastName, "UTF-8");
					password = URLEncoder.encode(password, "UTF-8");
					confirmPassword = URLEncoder.encode(confirmPassword, "UTF-8");
					securePassword = URLEncoder.encode(securePassword, "UTF-8");
					confirmSecurePassword = URLEncoder.encode(confirmSecurePassword, "UTF-8");
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
					// Save the Data in Database
					// /webAPI/:user/updateInfos
					//
					// loginDataBaseAdapter.updateEntry(userName, password,
					// userMail, firstName, lastName);
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
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.layout.change_profile, menu);
		return true;
	}

}
