package esgi.priva2peer.communication.parser.subclasses;

import org.json.JSONException;
import org.json.JSONObject;

public class UserInfos
{
	private String login, email, name, firstname;

	public UserInfos(JSONObject json) throws JSONException
	{
		try
		{
			this.login = json.getString("username");
		}
		catch (JSONException e)
		{
			this.login = json.getString("login");
		}
		try
		{
			this.email = json.getString("email");
		}
		catch (JSONException e)
		{
			this.email = null;
		}
		this.name = json.getString("name");
		this.firstname = json.getString("firstname");
	}

	public String getEmail()
	{
		return this.email;
	}

	public String getFirstname()
	{
		return this.firstname;
	}

	public String getLogin()
	{
		return this.login;
	}

	public String getName()
	{
		return this.name;
	}

	@Override
	public String toString()
	{
		return "Login : " + this.login + ((null == this.email) ? "" : "\nEmail: " + this.email) + "\nNom : " + this.name + "\nPr\u00E9nom : " + this.firstname;
	}
}