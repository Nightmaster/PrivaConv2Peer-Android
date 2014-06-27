package esgi.priva2peer.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import esgi.priva2peer.R;
import esgi.priva2peer.com.pc2p.communication.client.Client;
import esgi.priva2peer.com.pc2p.communication.message.Message;
import esgi.priva2peer.com.pc2p.communication.server.Server;
import esgi.priva2peer.communication.UserSessionManager;

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

		session = new UserSessionManager(getApplicationContext());
		HashMap<String, String> user = session.getUserDetails();
		String name = user.get(UserSessionManager.KEY_NAME);
		String email = user.get(UserSessionManager.KEY_EMAIL);
		String last_n = user.get(UserSessionManager.KEY_FirstName);
		String first_n = user.get(UserSessionManager.KEY_LastName);

		Toast.makeText(getApplicationContext(), "Pseudo : " + name + " MAil : " + email + " prenom : " + first_n + " nom : " + last_n, Toast.LENGTH_LONG).show();

		mMainLayout = (LinearLayout) findViewById(R.id.mainLayout);
		mMessageField = (EditText) findViewById(R.id.message_field);
		/*
		 * Server server = new Server(); server.run(); try { client(); } catch
		 * (IOException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */
		try
		{
			new Thread(new Server()).start();
			new Thread(new Client()).start();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

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
			Message mess = new Message();
			mess.setMessage(mMessageField.getText());
			mess.setReceiveDate(new Date());
			boolean clearArea = true;
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

			String dri = "C:\\Users\\Unlucky luke\\Desktop\\user.db";
			String url = "http://54.194.20.131:8080/";
			File file = new File(Environment.getExternalStorageDirectory(), dri);
			try
			{
				HttpClient httpclient = new DefaultHttpClient();

				HttpPost httppost = new HttpPost(url);

				InputStreamEntity reqEntity = new InputStreamEntity(new FileInputStream(file), -1);
				reqEntity.setContentType("binary/octet-stream");
				reqEntity.setChunked(true); // Send in multiple parts if needed
				httppost.setEntity(reqEntity);
				HttpResponse response = httpclient.execute(httppost);
				// Do something with response...
				Toast.makeText(getApplicationContext(), " nom : " + response, Toast.LENGTH_LONG).show();

			}
			catch (Exception e)
			{
				// show error
			}
		}
	}

	private void addMessage(String message)
	{
		TextView textV = new TextView(this);
		textV.setText(message);
		mMainLayout.addView(textV);
	}
}