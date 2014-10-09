package esgi.priva2peer.communication.parser.subclasses;

import org.json.JSONException;
import org.json.JSONObject;

public class Friend
{
	private boolean connected;
	private String username;

	public Friend(String username, boolean connected)
	{
		this.username = username;
		this.connected = connected;
	}

	public Friend(JSONObject json) throws JSONException
	{
		this(json.getString("displayLogin"), json.getBoolean("connected"));
	}

	public String getUsername()
	{
		return this.username;
	}

	public boolean isConnected()
	{
		return this.connected;
	}
}