package esgi.priva2peer.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import esgi.priva2peer.R;

public class ListActivity extends Activity
{

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friendlist);
		Button btnAddFriends, btnchangeProfile;
		ListView listfriends;
		TextView pseudo;

		// pseudo = (TextView) findViewById(R.id.pseudo);
		btnAddFriends = (Button) findViewById(R.id.buttonAddFriends);
		btnchangeProfile = (Button) findViewById(R.id.changeProfile);
		listfriends = (ListView) findViewById(R.id.friends_row);

		String[] listeStrings = {"Albert", "Henri", "Julie", "Mathias", "Marie", "Stéphane"};

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

	}

	/*
	 * String[] values = new String[] { "Device", "Géo localisation",
	 * "Accéléromètre", "Navigateur internet", "Dialogues", "Album photos",
	 * "Connexion réseau", "Gestion des fichiers", "Carnet de contacts" };
	 * ArrayAdapter<String> adapter = new
	 * ArrayAdapter<String>(this,android.R.id.list, values);
	 */

}