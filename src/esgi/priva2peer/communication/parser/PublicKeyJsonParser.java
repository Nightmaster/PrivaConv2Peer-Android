package esgi.priva2peer.communication.parser;

import org.json.JSONException;
import org.json.JSONObject;
import esgi.priva2peer.communication.parser.subclasses.UserPrivateKeyInfos;

public class PublicKeyJsonParser
{
	private String displayMessage = null;
	private boolean error;
	private int httpCode = 200;
	private UserPrivateKeyInfos userInfos = null;

	/**
	 * This class is made to parse the JSON returned by the server's web service when a private key demand is done
	 *
	 * @param json {JSONObject}: the JSON returned by the server's web service
	 * @throws JSONException Can throw exceptions because of illegal arguments
	 **/
	PublicKeyJsonParser(JSONObject json) throws JSONException
	{
		this.error = json.getBoolean("error");
		if (true == this.error)
		{
			this.displayMessage = json.getString("displayMessage");
			this.httpCode = json.getInt("httpErrorCode");
		}
		else
			this.userInfos = new UserPrivateKeyInfos(json.getJSONObject("user"));
	}

	public String getDisplayMessage()
	{
		return this.displayMessage;
	}

	public int getHttpCode()
	{
		return this.httpCode;
	}

	public UserPrivateKeyInfos getUserInfos()
	{
		return this.userInfos;
	}

	public boolean isError()
	{
		return this.error;
	}
}