package esgi.priva2peer.test;
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
			@SuppressWarnings("resource")
			ServerSocket server = new ServerSocket(6991);
			System.out.println("Start");
			while (true)
			{
				Socket socket = server.accept();
				System.out.println(socket.getInetAddress() + " " + socket.getPort() + "\r");
				Scanner sc = new Scanner(System.in);
				String st = new String();
				System.out.println("Communicate \n\r");

				while ((st = sc.nextLine()).trim().length() > 0)
				{
					System.out.println(st.toString() + "avant");
					socket.getOutputStream().write(st.getBytes()); // transmission
					// socket.getInputStream()
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
