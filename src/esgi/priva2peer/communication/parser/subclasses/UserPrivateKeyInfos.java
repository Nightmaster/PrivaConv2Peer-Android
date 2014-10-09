package esgi.priva2peer.communication.parser.subclasses;

import org.json.JSONException;
import org.json.JSONObject;

public class UserPrivateKeyInfos
{
	private String login, publicKey;

	public UserPrivateKeyInfos(JSONObject json) throws JSONException
	{
		this.login = json.getString("login");
		this.publicKey = json.getString("pubKey");
	}

	public String getLogin()
	{
		return this.login;
	}

	public String getPublicKey()
	{
		return this.publicKey;
	}
}