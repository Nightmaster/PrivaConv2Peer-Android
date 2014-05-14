package esgi.priva2peer.activity;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import esgi.priva2peer.R;

public class ChatActivity extends Activity
{

	private LinearLayout mMainLayout;
	private EditText mMessageField;
	private ArrayList<String> mMessages = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat);

		mMainLayout = (LinearLayout) findViewById(R.id.mainLayout);
		mMessageField = (EditText) findViewById(R.id.message_field);
		/*
		 * Server server = new Server(); server.run(); try { client(); } catch
		 * (IOException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */

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
		for (String message : mMessages)
		{
			addMessage("user : " + message);
		}
	}

	@SuppressLint("NewApi")
	public void addMessageButtonHandler(View clickedButton)
	{
		String message = mMessageField.getText().toString();
		if (!message.isEmpty())
		{
			mMessages.add("user : " + message);
			addMessage("user : " + message);
		}
	}

	private void addMessage(String message)
	{
		TextView textV = new TextView(this);
		textV.setText(message);
		mMainLayout.addView(textV);
	}
}