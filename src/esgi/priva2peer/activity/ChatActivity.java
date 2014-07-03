package esgi.priva2peer.activity;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import esgi.priva2peer.R;
import esgi.priva2peer.UserSessionManager;
import esgi.priva2peer.communication.client.Client;
import esgi.priva2peer.communication.server.Server;

/**
 * @author Bruno Gb
 */
public class ChatActivity extends Activity
{
	UserSessionManager session;

	private LinearLayout mMainLayout;
	private EditText mMessageField;
	private ArrayList<String> mMessages = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat);

		Map<String, String> talk = new HashMap<String, String>();

		mMainLayout = (LinearLayout) findViewById(R.id.mainLayout);
		mMessageField = (EditText) findViewById(R.id.message_field);

		new Thread(new Server()).start();
		new Thread(new Client()).start();

	}

	public void client() throws IOException
	{
		InetAddress ip = InetAddress.getByName("192.168.1.102");
		DatagramSocket socket = new DatagramSocket();
		byte[] outData = ("helloooo").getBytes();

		DatagramPacket out = new DatagramPacket(outData, outData.length, ip, 2060);
		socket.send(out);
		System.out.println("Send >>> ");
	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		outState.putStringArrayList("messages", mMessages);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState)
	{
		super.onRestoreInstanceState(savedInstanceState);
		mMessages = savedInstanceState.getStringArrayList("messages");
		session = new UserSessionManager(getApplicationContext());
		HashMap<String, String> user = session.getUserDetails();
		String name = user.get(UserSessionManager.KEY_NAME);

		for (String message : mMessages)
		{
			addMessage(name + " says : " + message);

		}

	}

	@SuppressLint("NewApi")
	public void addMessageButtonHandler(View clickedButton)
	{
		String message = mMessageField.getText().toString();
		session = new UserSessionManager(getApplicationContext());
		HashMap<String, String> user = session.getUserDetails();
		String name = user.get(UserSessionManager.KEY_NAME);
		if (!message.isEmpty())
		{
			mMessages.add(name + " says : " + message);
			addMessage(name + " says : " + message);

		}
	}

	private void addMessage(String message)
	{
		TextView textV = new TextView(this);
		textV.setText(message);
		mMainLayout.addView(textV);
	}
}