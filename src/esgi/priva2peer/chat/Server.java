package esgi.priva2peer.chat;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

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
				Scanner sc = new Scanner(System.in);
				String st = new String();
				while ((st = sc.nextLine()).trim().length() > 0)
				{
					socket.getOutputStream().write(st.getBytes());
				}
				sc.close();
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
