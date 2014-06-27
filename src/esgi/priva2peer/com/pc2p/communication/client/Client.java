package esgi.priva2peer.com.pc2p.communication.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;
import esgi.priva2peer.com.pc2p.communication.message.Message;
import esgi.priva2peer.com.pc2p.communication.message.MessageQueue;

public class Client implements Runnable
{

	public Client()
	{}

	@Override
	public void run()
	{
		System.out.println("CLIENT STARTED");

		try
		{
			String pseudo = "stephen";
			ClientInfo cli = new ClientInfo(pseudo);
			BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
			DatagramSocket clientSocket = new DatagramSocket();
			InetAddress IPAddress = InetAddress.getByName("192.168.0.26");
			// InetAddress IPAddress = cli.clientAdress;
			byte[] sendData = new byte[1024];
			byte[] receiveData = new byte[1024];

			while (true)
			{
				List<Message> listM = MessageQueue.getAllMessages(cli.getStrName());
				if (listM != null && !listM.isEmpty())
				{
					for (Message message : listM)
					{
						StringBuilder sb = new StringBuilder();
						sb.append(cli.strName + " : ");
						sb.append(message.getMessage());
						sendData = sb.toString().getBytes();
						DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 1112);
						clientSocket.send(sendPacket);
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

}
