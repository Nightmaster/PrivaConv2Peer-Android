package esgi.priva2peer.chat;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Client extends Thread
{
	private String sentence;
	private byte[] sendData = new byte[1024];
	private DatagramSocket clientSocket;
	private InetAddress IPAddress;
	private DatagramPacket sendPacket;
	private String destPort;
	private List<String> randomGG;
	private static boolean did_I_Receive_The_Packet = true;
	private static boolean should_I_Re_Send = false;
	private String mMessages = "";

	@Override
	public void run()
	{

		destPort = "83.202.112.154";

		int port = Integer.parseInt(destPort);

		try
		{
			read();
			clientSocket = new DatagramSocket();

			while (true)
			{
				Thread.sleep(5 * 1000);

				if (!should_I_Re_Send)// There is no need to resend the previous
										// packet
				{

					if (!did_I_Receive_The_Packet) // i have not received the
													// packet, i must tell the
													// other client's server to
													// resend
					{
						IPAddress = InetAddress.getByName("localhost");
						String resendPLZ = "$";
						sendData = resendPLZ.getBytes();
						sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
						clientSocket.send(sendPacket);

					}
					else if (did_I_Receive_The_Packet)// send a new packet with
														// a random line from le
														// file
					{

						IPAddress = InetAddress.getByName("localhost");

						sentence = getLine();

						sendData = sentence.getBytes();
						sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
						clientSocket.send(sendPacket);

						System.out.println("FROM USER1: " + sentence);

					}
				}

				else if (should_I_Re_Send) // I must resend the previous packet
											// or all will be lost
				{
					IPAddress = InetAddress.getByName("localhost");
					sendData = sentence.getBytes();
					sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
					clientSocket.send(sendPacket);

					System.out.println("FROM USER1: Resending previous packet " + sentence);
				}
			}

		}
		catch (Exception e)
		{
			System.out.println("There was an error with the Client");
		}
	}

	public void read() throws IOException
	{

		FileReader file = new FileReader("lol.txt");
		BufferedReader reader = new BufferedReader(file);
		String line = reader.readLine();
		List<String> lines = new ArrayList<String>();
		while (line != null)
		{
			lines.add(line);
			line = reader.readLine();
		}

		randomGG = lines;
		reader.close();
	}

	public String getLine()
	{
		Random r = new Random();
		String randomLine = randomGG.get(r.nextInt(randomGG.size()));
		return randomLine;
	}

	public static void setDid_I_Receive_The_Packet(boolean s)
	{
		did_I_Receive_The_Packet = s;
	}

	public static void setShould_I_Re_Send(boolean rs)
	{
		should_I_Re_Send = rs;
	}

}
