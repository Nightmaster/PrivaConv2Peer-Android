package esgi.priva2peer.communication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import android.util.Log;

public class Server implements Runnable
{

	public static final String SERVERIP = "54.194.20.131"; // 'Within' the
															// emulator!
	public static final int SERVERPORT = 2060;

	public void run()
	{

		Log.e("receive", "new socket");
		DatagramSocket socket = null;
		try
		{
			socket = new DatagramSocket(SERVERPORT);
		}
		catch (SocketException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		byte[] buf = new byte[1024];
		Log.e("receive", "new packet");
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		try
		{
			socket.setSoTimeout(10000);
		}
		catch (SocketException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Log.e("receive", "receive !");
		try
		{
			socket.receive(packet);
			Log.e("receive", new String(packet.getData()));

		}
		catch (SocketTimeoutException e)
		{
			Log.e("receive", "time out");
		}
		catch (IOException e)
		{
			Log.e("receive", "error 1");
			e.printStackTrace();
		}

		Log.e("receive", "end");

	}
}
