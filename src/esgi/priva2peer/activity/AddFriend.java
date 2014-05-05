package esgi.priva2peer.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import esgi.priva2peer.R;

public class AddFriend extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_friend);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.layout.add_friend, menu);
		return true;
	}

}
