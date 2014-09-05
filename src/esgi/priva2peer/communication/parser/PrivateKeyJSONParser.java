package esgi.priva2peer.communication.parser;

import org.json.JSONException;
import org.json.JSONObject;

class PrivateKeyJSONParser
{
	private boolean error;
	private int httpCode = 200;
	private String displayMessage = null, privateKey;

	/**
	 * This class is made to parse the JSON returned by the server's web service
	 * when the private key is asked to the server
	 * 
	 * @param json {JSONObject}: the JSON returned by the server's web service
	 * @throws JSONException Can throw exceptions because of illegal arguments
	 **/
	public PrivateKeyJSONParser(JSONObject json) throws JSONException
	{
		this.error = json.getBoolean("error");
		if (true == this.error)
		{
			this.displayMessage = json.getString("displayMessage");
			this.httpCode = json.getInt("httpErrorCode");
		}
		this.privateKey = json.getString("prKey");
	}

	public String getDisplayMessage()
	{
		return this.displayMessage;
	}

	public int getHttpCode()
	{
		return this.httpCode;
	}

	public String getPrivateKey()
	{
		return this.privateKey;
	}

	public boolean isError()
	{
		return this.error;
	}
}
