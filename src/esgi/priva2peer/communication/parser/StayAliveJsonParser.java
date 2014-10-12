package esgi.priva2peer.communication.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import esgi.priva2peer.communication.parser.subclasses.Friend;

public class StayAliveJsonParser
{
	private String[] askFriendship;
	private String displayMessage = null;
	private boolean error, statusOk;
	private Friend[] fl;
	private int httpCode = 200, validity;

	/**
	 * This class is made to parse the JSON returned by the server's web service
	 * when a stay alive action is performed
	 * 
	 * @param json {JSONObject}: the JSON returned by the server's web service
	 * @throws JSONException Can throw exceptions because of illegal arguments
	 **/
	StayAliveJsonParser(JSONObject json) throws JSONException
	{
		JSONArray ask = null, fList = null;
		this.error = json.getBoolean("error");
		if (this.error)
		{
			this.displayMessage = json.getString("displayMessage");
			this.httpCode = json.getInt("httpErrorCode");
		}
		else
		{
			if (json.isNull("askFriend"))
				this.askFriendship = null;
			else
			{
				this.askFriendship = new String[ask.length()];
				for (int i = 0; i < ask.length(); i++ )
					this.askFriendship[i] = ask.get(i).toString();
			}
			if (json.isNull("friends"))
				this.fl = null;
			else
			{
				fList = json.getJSONArray("friends");
				this.fl = new Friend[fList.length()];
				for (int i = 0; i < fList.length(); i++ )
					this.fl[i] = new Friend(fList.getJSONObject(i));
			}
		}
		this.statusOk = json.getBoolean("stayAlive");
		this.validity = json.getInt("validity");
	}

	public String[] getAskList()
	{
		return this.askFriendship;
	}

	public String getDisplayMessage()
	{
		return this.displayMessage;
	}

	public Friend[] getFriendList()
	{
		return this.fl;
	}

	public int getHttpCode()
	{
		return this.httpCode;
	}

	public int getValidity()
	{
		return this.validity;
	}

	public boolean isError()
	{
		return this.error;
	}

	public boolean isStatusOk()
	{
		return this.statusOk;
	}
}