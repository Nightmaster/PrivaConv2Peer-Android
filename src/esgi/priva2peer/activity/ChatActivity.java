package esgi.priva2peer.activity;

import java.net.InetAddress;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import esgi.priva2peer.R;

public class ChatActivity extends Activity
{
	public static InetAddress BroadcastAddress;
	public static int SERVER_PORT;
	public static String MESSAGE_RECEIVED;
	public static String MESSAGE_STRING;
	// public static Object MainContext;
	protected String TAG;
	protected int MULTICAST_PORT;
	public Object multicastPort;
	protected Context context;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat);
		Button sendMessageButton;
		EditText message;

		message = (EditText) findViewById(R.id.message);
		sendMessageButton = (Button) findViewById(R.id.sendMessageButton);

		// final UDPMessenger UDPMessenger = new UDPMessenger(context, TAG,
		// MULTICAST_PORT);
		// int ip = 8080;
		boolean broadcast = false;
		// UDPMessenger.ipToString(ip, broadcast);

		sendMessageButton.setOnClickListener(new View.OnClickListener()
		{

			public void onClick(View v)
			{

				// UDPMessenger.sendMessage(message);
				// /UDPMessenger.startMessageReceiver();
				// UDPMessenger.stopMessageReceiver();

			}
		});
	}
}