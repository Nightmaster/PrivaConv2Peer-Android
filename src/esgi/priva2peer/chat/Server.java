package esgi.priva2peer.chat;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Random;

public class Server extends Thread
{
	private DatagramSocket serverSocket;
	private byte[] receiveData;
	private DatagramPacket receivePacket;
	private String sentence;
	private String servPort;
	private static boolean packetStatus;
	private String temp;

	@Override
	public void run()
	{
		try
		{

			servPort = "Server";

			int port = Integer.parseInt(servPort);
			serverSocket = new DatagramSocket(port);
			receiveData = new byte[1024];

			while (true)
			{

				Thread.sleep(5 * 1000);

				receivePacket = new DatagramPacket(receiveData, receiveData.length);
				serverSocket.receive(receivePacket);
				temp = new String(receivePacket.getData());

				if (temp.equals("$")) // if our packet has only an "$" in it,
										// that is the other client letting us
										// know it did not receive our previous
										// packet and to resend
				{

					temp = "";
					Client.setShould_I_Re_Send(true);
				}

				else if (!temp.equals("$")) // the packet doesn't have an $
											// meaning it is a regular packet
											// from le client
				{
					Client.setShould_I_Re_Send(false);
					int rNum = theLossOfPacketsIsReal();
					if (rNum == 1 || rNum == 2)
					{
						// the incoming packet has been lost!!!
						// i must tell my client that i did not receive a packet
						// from the other peers client

						System.out.println("FROM USER2: packet was lost");
						temp = "";
						Client.setDid_I_Receive_The_Packet(false); // tell the
																	// my client
																	// that no
																	// you did
																	// not
																	// receive
																	// the
																	// packet

					}
					else
					{
						Client.setDid_I_Receive_The_Packet(true); // tell my
																	// client
																	// that i
																	// received
																	// the
																	// packet

						sentence = temp;

						System.out.println("FROM USER2: " + sentence);
					}
				}

			}
		}
		catch (Exception e)
		{
			System.out.println("There was an error with the Server");
		}
	}

	public int theLossOfPacketsIsReal() // chooses a random number between 1-10
	{
		Random randoom = new Random();
		int num = randoom.nextInt(10) + 1;
		return num;

	}

	public static boolean isPacketLost()
	{
		return packetStatus;
	}

	public static void setPacketStatus(boolean s)
	{
		packetStatus = s;
	}

}
