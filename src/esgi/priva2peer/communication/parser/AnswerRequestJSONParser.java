package esgi.priva2peer.communication.parser;

import org.json.JSONException;
import org.json.JSONObject;

class AnswerRequestJSONParser
{
	private boolean error;
	private int httpCode = 200;
	private String displayMessage = null, friendshipStatus = null;

	/**
	 * This class is made to parse the JSON returned by the server's web service
	 * when a answering action to a friendship demand is done
	 * 
	 * @param json {JSONObject}: the JSON returned by the server's web service
	 * @throws JSONException Can throw exceptions because of illegal arguments
	 **/
	public AnswerRequestJSONParser(JSONObject json) throws JSONException
	{
		this.error = json.getBoolean("error");
		if (true == this.error)
		{
			this.displayMessage = json.getString("displayMessage");
			this.httpCode = json.getInt("httpErrorCode");
		}
		this.friendshipStatus = json.getString("friendshipStatus");
	}

	public String getDisplayMessage()
	{
		return this.displayMessage;
	}

	public String getFriendshipStatus()
	{
		return this.friendshipStatus;
	}

	public int getHttpCode()
	{
		return this.httpCode;
	}

	public boolean isError()
	{
		return this.error;
	}
}