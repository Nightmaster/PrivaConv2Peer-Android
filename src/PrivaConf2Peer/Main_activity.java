package PrivaConf2Peer;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;


public class Main_activity extends TabActivity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		TabHost tabHost = getTabHost();

		TabSpec connexspec = tabHost.newTabSpec("Connexion");
		connexspec.setIndicator("Connexion");
		Intent connexIntent = new Intent(this, Sign_activity.class);
		connexspec.setContent(connexIntent);

		TabSpec listspec = tabHost.newTabSpec("Liste d'amis");
		listspec.setIndicator("Liste d'amis");
		Intent listIntent = new Intent(this, Friends_list.class);
		listspec.setContent(listIntent);

		TabSpec chatspec = tabHost.newTabSpec("Chat");
		chatspec.setIndicator("Chat");
		Intent chatIntent = new Intent(this, Chat_View.class);
		chatspec.setContent(chatIntent);

		// Adding all TabSpec to TabHost
		tabHost.addTab(connexspec); // Adding photos tab
		tabHost.addTab(listspec); // Adding songs tab
		tabHost.addTab(chatspec); // Adding videos tab
	}
}