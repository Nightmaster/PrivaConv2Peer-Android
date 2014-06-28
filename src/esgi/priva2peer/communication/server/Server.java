package esgi.priva2peer.communication.server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Date;
import esgi.priva2peer.communication.message.Message;
import esgi.priva2peer.communication.message.MessageQueue;

public class Server implements Runnable
{

	static DatagramSocket serverSocket;
	Message mess = new Message();

	@Override
	public void run()
	{
		System.out.println("SERVER STARTED");
		try
		{
			serverSocket = new DatagramSocket(1112);

			byte[] receiveData = new byte[1024];
			byte[] sendData = new byte[1024];
			while (true)
			{
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				serverSocket.receive(receivePacket);
				String s = new String(receivePacket.getData()).substring(0, receivePacket.getLength());
				mess.setMessage(s);
				mess.setReceiveDate(new Date());
				MessageQueue.addMessageToPrint("stephen", mess);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

}
