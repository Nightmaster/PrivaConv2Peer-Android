package esgi.priva2peer.communication.parser;

import org.json.JSONException;
import org.json.JSONObject;

class SimpleJSONParser
{
	private boolean error, status;
	private int httpCode = 200;
	private String displayMessage = null;

	/**
	 * This parser is made to return informations on simple actions: action is
	 * done or not. If the action is not done, because of an error, then it's
	 * possible to get informations on it
	 * 
	 * @param json {JSONObject}: the JSON returned by the server's web service
	 * @param action {String}: the name of the current action
	 * @throws JSONException Can throw exceptions because of illegal arguments
	 **/
	public SimpleJSONParser(JSONObject json, String action) throws JSONException
	{
		if ("registration" != action.toLowerCase() && "disconnection" != action.toLowerCase())
			throw new IllegalArgumentException("action parameter must be: \"Registration\" or \"Disconnection\" (not case sensitive)!");
		this.error = json.getBoolean("error");
		if (true == this.error)
		{
			this.displayMessage = json.getString("displayMessage");
			this.httpCode = json.getInt("httpErrorCode");
		}
		this.status = json.getBoolean("validation");
	}

	public String getDisplayMessage()
	{
		return this.displayMessage;
	}

	public int getHttpCode()
	{
		return this.httpCode;
	}

	public boolean isActionValidated()
	{
		return this.status;
	}

	public boolean isError()
	{
		return this.error;
	}

	public boolean isRegistred()
	{
		return this.status;
	}
}
