package esgi.priva2peer.chat;

import java.net.Socket;
import java.util.Arrays;
import esgi.priva2peer.activity.ChatActivity;

public class Client implements Runnable
{

	@Override
	public void run()
	{
		try
		{
			Socket s = new Socket(ChatActivity.ADRESS_TO_CONNECT, ChatActivity.SAMPLE_PORT);
			byte[] b = new byte[256];
			int count;
			while ((count = s.getInputStream().read(b)) > 0)
			{
				System.out.println(new String(Arrays.copyOf(b, count)));
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	public static void main(String[] args)
	{
		new Thread(new Client()).start();
	}
}
