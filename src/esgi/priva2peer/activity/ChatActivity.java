package esgi.priva2peer.activity;

import java.io.IOException;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import esgi.priva2peer.R;
import esgi.priva2peer.UserSessionManager;
import esgi.priva2peer.data.Constants;

/**
 * @author Bruno Gb
 */
@SuppressLint("SimpleDateFormat")
public class ChatActivity extends Activity
{
	UserSessionManager session;

	private LinearLayout mMainLayout;
	private EditText mMessageField;
	private ArrayList<String> mMessages = new ArrayList<String>();
	private TextView mMessage_prompt;
	final Context context = this;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat);

		Map<String, String> talk = new HashMap<String, String>();

		mMainLayout = (LinearLayout) findViewById(R.id.mainLayout);
		mMessageField = (EditText) findViewById(R.id.message_field);
		mMessage_prompt = (TextView) findViewById(R.id.message_prompt);

		Bundle extras = getIntent().getExtras();
		String selected_item = extras.getString("selected_item");
		if (selected_item != "")
		{
			mMessage_prompt.setText(selected_item + " ");
		}
		else
		{
			Toast.makeText(getApplicationContext(), "Aucun ami selectionné", Toast.LENGTH_LONG).show();
			Intent list_f_intent = new Intent(getApplicationContext(), ListFriends.class);
			startActivity(list_f_intent);
		}
		mMessage_prompt.setText(selected_item + " ");
		HttpClient Client = new DefaultHttpClient();
		String URL = Constants.SRV_URL + Constants.SRV_API + "GetCliIp/" + selected_item;

		try
		{

			HttpGet httpget = new HttpGet(URL);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			if (PreferenceManager.getDefaultSharedPreferences(context).getString("MYLABEL", "defaultStringIfNothingFound") != "IfNothingFound")
			{
				httpget.setHeader("Cookie", PreferenceManager.getDefaultSharedPreferences(context).getString("MYLABEL", ""));
			}
			String SetServerString = "";
			SetServerString = Client.execute(httpget, responseHandler);
			String[] ip_friend_selected = SetServerString.split("\"");
			Log.d("amis", ip_friend_selected[5] + "receiver");

		}
		catch (Exception ex)
		{}
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

	public String wifiIpAddress(Context context)
	{
		WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
		int ipAddress = wifiManager.getConnectionInfo().getIpAddress();

		if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN))
		{
			ipAddress = Integer.reverseBytes(ipAddress);
		}

		byte[] ipByteArray = BigInteger.valueOf(ipAddress).toByteArray();

		String ipAddressString;
		try
		{
			ipAddressString = InetAddress.getByAddress(ipByteArray).getHostAddress();
		}
		catch (UnknownHostException ex)
		{
			Log.e("WIFIIP", "Unable to get host address.");
			ipAddressString = null;
		}

		return ipAddressString;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		Log.d("amis", "onSaveInstanceState");
		outState.putStringArrayList("messages", mMessages);
	}

	protected void onPause(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		Log.d("amis", "onPause");

		super.onPause();
	}

	protected void onStart(Bundle savedInstanceState)
	{
		WifiManager wifiMgr = (WifiManager) getSystemService(WIFI_SERVICE);
		WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
		int ip = wifiInfo.getIpAddress();
		Log.d("tag", " /" + ip + " /");

	}

	protected void onResume(Bundle savedInstanceState)
	{
		super.onResume();
		mMessages = savedInstanceState.getStringArrayList("messages");
		Log.d("amis", "onResume");

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState)
	{
		super.onRestoreInstanceState(savedInstanceState);
		mMessages = savedInstanceState.getStringArrayList("messages");
		session = new UserSessionManager(getApplicationContext());
		HashMap<String, String> user = session.getUserDetails();
		String name = user.get(UserSessionManager.KEY_NAME);

		if (mMessages == null)
		{
			for (String message : mMessages)
			{
				Date d = new Date();
				SimpleDateFormat f = new SimpleDateFormat("HH:mm:ss");
				String s = f.format(d);
				addMessage(name + " (" + s + ")" + " : " + message);
			}
		}
		Log.d("amis", "onRestoreInstanceState");
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
			Date d = new Date();
			SimpleDateFormat f = new SimpleDateFormat("HH:mm");
			String s = f.format(d);
			addMessage(name + " (" + s + ")" + " : " + message);
			mMessages.add(name + " (" + s + ")" + " : " + message);
		}
	}

	private void addMessage(String message)
	{
		TextView textV = new TextView(this);
		textV.setText(message);
		mMainLayout.addView(textV);
	}
}