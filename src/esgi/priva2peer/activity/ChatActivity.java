package esgi.priva2peer.activity;

import java.math.BigInteger;
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
import org.bitlet.weupnp.GatewayDevice;
import org.bitlet.weupnp.GatewayDiscover;
import org.bitlet.weupnp.PortMappingEntry;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import esgi.priva2peer.R;
import esgi.priva2peer.UserSessionManager;
import esgi.priva2peer.communication.parser.ClientIpJsonParser;
import esgi.priva2peer.communication.parser.JSONParser;
import esgi.priva2peer.communication.parser.PublicKeyJsonParser;
import esgi.priva2peer.data.Constants;

/**
 * @author Bruno Gb
 */
@SuppressLint("SimpleDateFormat")
public class ChatActivity extends Activity
{

	public static String ADRESS_TO_CONNECT = "";
	public static int SAMPLE_PORT;

	private static short WAIT_TIME = 120;

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
		String privateKey = extras.getString("key_private");

		Log.d("amis", "onCreate");
		if (savedInstanceState != null)
		{
			mMessages = savedInstanceState.getStringArrayList("mMessages");
			for (int i = 0; i < mMessages.size(); i++ )
			{
				Log.d("fd", mMessages.get(i));
				mMessages.get(i);
				addMessage(mMessages.get(i));
			}
		}

		if (selected_item != "")
		{
			Log.d("ami", selected_item);
			mMessage_prompt.setText(selected_item + " ");

		}
		else
		{
			Log.d("fdsf", "rien");

		}
		for (String message : mMessages)
		{
			addMessage(message);
			Log.d("amis", message);

		}

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
			ClientIpJsonParser stAlJson = JSONParser.getClientIpParser(SetServerString);
			stAlJson.getIpAndPort();

			String[] parts = SetServerString.split("\"");
			Log.d("port = ", "vfdsf" + stAlJson.getIpAndPort().getPort()); // IP
			Log.d("ip = ", parts[7]); // IP
			ADRESS_TO_CONNECT = parts[7];
			SAMPLE_PORT = stAlJson.getIpAndPort().getPort();
			GatewayDiscover gatewayDiscover = new GatewayDiscover();
			//
			// // choose the first active gateway for the tests
			Map<InetAddress, GatewayDevice> gateways = gatewayDiscover.discover();
			// // prend la box
			GatewayDevice activeGW = gatewayDiscover.getValidGateway();

			// testing getGenericPortMappingEntry
			PortMappingEntry portMapping = new PortMappingEntry();
			// local address = adresse ip machine
			InetAddress localAddress = activeGW.getLocalAddress();
			if (activeGW.getSpecificPortMappingEntry(SAMPLE_PORT, "TCP", portMapping))
			{
				System.out.println("Port " + SAMPLE_PORT + " is already mapped.");
				// delete s'il est utilis�
				activeGW.deletePortMapping(SAMPLE_PORT, "TCP");
				return;
			}
			else
			{
				// test static lease duration mapping
				// vers ou on map
				// SAMPLE_PORT box, SAMPLE_PORT machine, adresse machine,
				// Protocole,
				// description
				if (activeGW.addPortMapping(SAMPLE_PORT, SAMPLE_PORT, localAddress.getHostAddress(), "TCP", "test"))
				{
					Thread t = new Thread(new esgi.priva2peer.chat.Server());
					t.start();
					Thread.sleep(1000 * WAIT_TIME);
					t.interrupt();
					activeGW.deletePortMapping(SAMPLE_PORT, "TCP");
					System.out.println("get IP and port");
				}
			}
		}
		catch (Exception ex)
		{}

	}

	public void getPublickey(String username)
	{
		HttpClient Client = new DefaultHttpClient();
		String URL = Constants.SRV_URL + Constants.SRV_API + "getPubKey/" + username;

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
			PublicKeyJsonParser stAlJson = JSONParser.getPublicKeyParser(SetServerString);
			if (!stAlJson.isError())
			{
				String[] parts = SetServerString.split("\"");
				Log.d("PubK", parts[5]); // clef public de l'ami
			}

		}
		catch (Exception ex)
		{}
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
		Bundle extras = getIntent().getExtras();
		String selected_item = extras.getString("selected_item");
		outState.putString("selected_item", selected_item);
	}

	protected void onPause(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		Log.d("amis", "onPause");

		super.onPause();
	}

	protected void onStart(Bundle savedInstanceState)
	{
		Log.d("amis", "onStart");
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
		if (mMessages != null)
		{
			for (String message : mMessages)
			{
				Date d = new Date();
				SimpleDateFormat f = new SimpleDateFormat("HH:mm");
				String s = f.format(d);
				addMessage(name + " (" + s + ")" + " : " + message);
			}
		}
		Log.d("amis", "onRestoreInstanceState");

		for (int i = 0; i < mMessages.size(); i++ )
		{
			Log.d("fd", mMessages.get(i));
			mMessages.get(i);
			addMessage(mMessages.get(i));
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