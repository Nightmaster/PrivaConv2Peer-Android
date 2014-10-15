package esgi.priva2peer.chat;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class Server implements Runnable
{

	@Override
	public void run()
	{
		try
		{
			ServerSocket server = new ServerSocket(6991);
			System.out.println("Start");
			while (true)
			{
				Socket socket = server.accept();
				System.out.println(socket.getInetAddress() + " " + socket.getPort());
				byte[] b = new byte[256];
				int count;
				while ((count = socket.getInputStream().read(b)) > 0)
				{
					System.out.println(new String(Arrays.copyOf(b, count)));
				}

				socket.close();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	public static void main(String[] args)
	{
		new Thread(new Server()).start();
	}

}
