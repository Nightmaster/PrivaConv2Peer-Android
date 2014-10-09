package esgi.priva2peer.communication.parser;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.json.JSONException;
import org.json.JSONObject;

public class ClientIpJsonParser
{
	private String displayMessage = null;
	private boolean error;
	private int httpCode = 200;
	private InetAddress ipAdress = null;

	/**
	 * This class is made to parse the JSON returned by the server's web service when a user IP demand is done
	 *
	 * @param json {JSONObject}: the JSON returned by the server's web service
	 * @throws JSONException Can throw exceptions because of illegal arguments
	 **/
	ClientIpJsonParser(JSONObject json) throws JSONException
	{
		this.error = json.getBoolean("error");
		if (this.error)
		{
			this.displayMessage = json.getString("displayMessage");
			this.httpCode = json.getInt("httpErrorCode");
		}
		try
		{
			this.ipAdress = InetAddress.getByName(json.getString("ip"));
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
	}

	public String getDisplayMessage()
	{
		return this.displayMessage;
	}

	public int getHttpCode()
	{
		return this.httpCode;
	}

	public InetAddress getIpAdress()
	{
		return this.ipAdress;
	}

	public boolean isError()
	{
		return this.error;
	}
}