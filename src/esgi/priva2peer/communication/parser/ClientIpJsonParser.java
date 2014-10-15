package esgi.priva2peer.communication.parser;

import java.io.UnsupportedEncodingException;
import org.json.JSONException;
import org.json.JSONObject;
import esgi.priva2peer.communication.parser.subclasses.IpAndPort;

public class ClientIpJsonParser
{
	private String displayMessage = null;
	private boolean error;
	private int httpCode = 200;
	private IpAndPort ipAndPort = null;

	/**
	 * This class is made to parse the JSON returned by the server's web service
	 * when a user IP demand is done
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
		else
			this.ipAndPort = new IpAndPort(json.getJSONObject("infos"));
	}

	public String getDisplayMessage()
	{
		try
		{
			return new String(this.displayMessage.getBytes("ISO-8859-1"), "UTF-8");
		}
		catch (UnsupportedEncodingException e)
		{
			return this.displayMessage;
		}
	}

	public int getHttpCode()
	{
		return this.httpCode;
	}

	public IpAndPort getIpAndPort()
	{
		return this.ipAndPort;
	}

	public boolean isError()
	{
		return this.error;
	}
}