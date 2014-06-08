package esgi.priva2peer.activity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import esgi.priva2peer.R;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		TabHost tabHost = getTabHost();

		TabSpec loginspec = tabHost.newTabSpec("Login");
		loginspec.setIndicator("Login");
		Intent loginIntent = new Intent(this, Home.class);
		loginspec.setContent(loginIntent);


		TabSpec friendspec = tabHost.newTabSpec("Friends");
		friendspec.setIndicator("Friends");
		Intent friendIntent = new Intent(this, ListFriends.class);
		friendspec.setContent(friendIntent);

		TabSpec chatspec = tabHost.newTabSpec("Chat");
		chatspec.setIndicator("Chat");
		Intent chatIntent = new Intent(this, ChatActivity.class);
		chatspec.setContent(chatIntent);


		tabHost.addTab(loginspec);
		tabHost.addTab(friendspec);
		tabHost.addTab(chatspec);
	}
}