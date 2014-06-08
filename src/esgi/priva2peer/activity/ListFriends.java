package esgi.priva2peer.activity;

import java.util.HashMap;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import esgi.priva2peer.R;
import esgi.priva2peer.communication.UserSessionManager;

public class ListFriends extends Activity
{
	// User Session Manager Class
	UserSessionManager session;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friendlist);
		Button btnAddFriends, btnchangeProfile;
		final ListView listfriends;
		// TextView pseudo;

		// Session value

		session = new UserSessionManager(getApplicationContext());

		HashMap<String, String> user = session.getUserDetails();
		String name = user.get(UserSessionManager.KEY_NAME);
		String email = user.get(UserSessionManager.KEY_EMAIL);
		String last_n = user.get(UserSessionManager.KEY_FirstName);
		String first_n = user.get(UserSessionManager.KEY_LastName);

		// Session value

		Toast.makeText(getApplicationContext(), "Pseudo : " + name + " MAil : " + email + " prenom : " + first_n + " nom : " + last_n, Toast.LENGTH_LONG).show();

		// pseudo = (TextView) findViewById(R.id.pseudo);
		btnAddFriends = (Button) findViewById(R.id.buttonAddFriends);
		btnchangeProfile = (Button) findViewById(R.id.changeProfile);
		listfriends = (ListView) findViewById(R.id.friends_row);

		String[] listeStrings = {"Marie", "Stéphane"};

		listfriends.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listeStrings));

		btnAddFriends.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{

				Intent add_f_intent = new Intent(getApplicationContext(), AddFriend.class);
				startActivity(add_f_intent);
			}
		});
		btnchangeProfile.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				Intent add_f_intent = new Intent(getApplicationContext(), ChangeProfile.class);
				startActivity(add_f_intent);
			}
		});
		listfriends.setOnItemClickListener(new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				String selectedFromList = (listfriends.getItemAtPosition(position).toString());

				Intent add_f_intent = new Intent(view.getContext(), ChatActivity.class);
				add_f_intent.putExtra("mytext", selectedFromList);
				startActivity(add_f_intent);
			}
		});
	}
}