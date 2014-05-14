package esgi.priva2peer.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import esgi.priva2peer.R;
import esgi.priva2peer.data.AddFriendDataBase;

public class AddFriend extends Activity
{
	EditText UserName;
	AutoCompleteTextView pseudo_auto;
	Button SearchMail;

	AddFriendDataBase addFriendDataBase;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_friend);

		addFriendDataBase = new AddFriendDataBase(this);
		addFriendDataBase = addFriendDataBase.open();

		UserName = (EditText) findViewById(R.id.userName);
		SearchMail = (Button) findViewById(R.id.SearchMail);
		pseudo_auto = (AutoCompleteTextView) findViewById(R.id.pseudo_auto);
		SearchMail.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				String userName = UserName.getText().toString();
				String Pseudo_auto = pseudo_auto.getText().toString();

				if (userName.equals("") || Pseudo_auto.equals(""))
				{
					Toast.makeText(getApplicationContext(), "Field Vaccant", Toast.LENGTH_LONG).show();
					return;
				}
				else
				{
					addFriendDataBase.insertEntry(userName, Pseudo_auto);
					Toast.makeText(getApplicationContext(), userName + Pseudo_auto, Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.layout.add_friend, menu);
		return true;
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		addFriendDataBase.close();
	}

}
