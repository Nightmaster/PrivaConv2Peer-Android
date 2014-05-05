package esgi.priva2peer.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import esgi.priva2peer.R;

public class ChangeProfile extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_profile);
		Button btnchangedProfile;

		btnchangedProfile = (Button) findViewById(R.id.profile_changed);

		btnchangedProfile.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{

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
