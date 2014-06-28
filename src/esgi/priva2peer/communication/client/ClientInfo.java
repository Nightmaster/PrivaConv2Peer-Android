package esgi.priva2peer.communication.client;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ClientInfo
{
	public String strName; // Name by which the user logged into the chat room
	InetAddress clientAdress;

	public ClientInfo(String pseudo)
	{
		strName = pseudo;
		try
		{
			clientAdress = InetAddress.getLocalHost();
		}
		catch (UnknownHostException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getStrName()
	{
		return strName;
	}

	public void setStrName(String strName)
	{
		this.strName = strName;
	}

	public InetAddress getClientAdress()
	{
		return clientAdress;
	}

	public void setClientAdress(InetAddress clientAdress)
	{
		this.clientAdress = clientAdress;
	}

}
