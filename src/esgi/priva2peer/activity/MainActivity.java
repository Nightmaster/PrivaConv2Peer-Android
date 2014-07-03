package esgi.priva2peer.activity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import esgi.priva2peer.R;

/**
 * @author Bruno Gb
 */
public class MainActivity extends TabActivity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		TabHost tabHost = getTabHost();

		TabSpec loginspec = tabHost.newTabSpec("Accueil");
		loginspec.setIndicator("Accueil");
		Intent loginIntent = new Intent(this, Home.class);
		loginspec.setContent(loginIntent);

		TabSpec friendspec = tabHost.newTabSpec("Liste d'amis");
		friendspec.setIndicator("Liste d'amis");
		Intent friendIntent = new Intent(this, ListFriends.class);
		friendspec.setContent(friendIntent);

		TabSpec chatspec = tabHost.newTabSpec("Conversation");
		chatspec.setIndicator("Conversation");
		Intent chatIntent = new Intent(this, ChatActivity.class);
		chatspec.setContent(chatIntent);

		tabHost.addTab(loginspec);
		tabHost.addTab(friendspec);
		tabHost.addTab(chatspec);
	}
}