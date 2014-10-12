package esgi.priva2peer.communication.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import esgi.priva2peer.communication.parser.subclasses.Friend;
import esgi.priva2peer.communication.parser.subclasses.UserInfos;

public class ConnectionJsonParser
{
	private String[] askFriendship;
	private String displayMessage = null;
	private boolean error, connection;
	private Friend[] fl;
	private UserInfos userInfos;
	private int validity, httpCode = 200;

	/**
	 * This class is made to parse the JSON returned by the server's web service
	 * when a connection action is done
	 * 
	 * @param json {JSONObject}: the JSON returned by the server's web service
	 * @throws JSONException Can throw exceptions because of illegal arguments
	 **/
	ConnectionJsonParser(JSONObject json) throws JSONException
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
			this.validity = json.getInt("validity");
			if (!json.isNull("askFriend"))
			{
				ask = json.getJSONArray("askFriend");
				this.askFriendship = new String[ask.length()];
				for (int i = 0; i < ask.length(); i++ )
					this.askFriendship[i] = ask.get(i).toString();
			}
			if (!json.isNull("friends"))
			{
				fList = json.getJSONArray("friends");
				this.fl = new Friend[fList.length()];
				for (int i = 0; i < fList.length(); i++ )
					this.fl[i] = new Friend(fList.getJSONObject(i));
			}
			this.userInfos = new UserInfos(json.getJSONObject("user"));
		}
		this.connection = json.getBoolean("connection");
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

	public UserInfos getUserInfos()
	{
		return this.userInfos;
	}

	public int getValidity()
	{
		return this.validity;
	}

	public boolean isConnectionValidated()
	{
		return this.connection;
	}

	public boolean isError()
	{
		return this.error;
	}
}