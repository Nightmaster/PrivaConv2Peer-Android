package esgi.priva2peer.communication.parser.subclasses;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Gaël B.
 */
public class IpAndPort
{
	private String ipAdress;
	private int port;

	public IpAndPort(JSONObject json)
	{
		try
		{
			this.ipAdress = json.getString("ip");
		}
		catch (JSONException ignored)
		{
			this.ipAdress = null;
		}
		try
		{
			this.port = json.getInt("port");
		}
		catch (JSONException ignored)
		{
			this.port = -1;
		}
	}

	public InetAddress getInetAdress() throws UnknownHostException
	{
		return InetAddress.getByName(this.ipAdress);
	}

	public String getIpAdress()
	{
		return this.ipAdress;
	}

	public int getPort()
	{
		return this.port;
	}
}
