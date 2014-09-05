package esgi.priva2peer.communication.parser.subclasses;

import org.json.JSONException;
import org.json.JSONObject;

public class Friend
{
	private String username;
	private boolean connected;

	public Friend(JSONObject json) throws JSONException
	{
		this.username = json.getString("displayLogin");
		this.connected = json.getBoolean("connected");
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