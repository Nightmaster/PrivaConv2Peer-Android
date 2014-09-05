package esgi.priva2peer.communication.parser.subclasses;

import org.json.JSONObject;

public class ChangedValues
{
	private String login = null, email = null, firstName = null, name = null;
	private boolean passwordChanged;

	public ChangedValues(JSONObject json)
	{
		try
		{
			this.login = json.getString("login");
		}
		catch (Exception e)
		{
			this.login = null;
		}

		try
		{
			this.email = json.getString("email");
		}
		catch (Exception e)
		{
			this.email = null;
		}

		try
		{
			this.firstName = json.getString("firstname");
		}
		catch (Exception e)
		{
			this.firstName = null;
		}

		try
		{
			this.name = json.getString("name");
		}
		catch (Exception e)
		{
			this.name = null;
		}

		try
		{
			this.passwordChanged = json.getBoolean("pwChanged");
		}
		catch (Exception e)
		{
			this.passwordChanged = false;
		}
	}

	public String getEmail()
	{
		return this.email;
	}

	public String getFirstName()
	{
		return this.firstName;
	}

	public String getLogin()
	{
		return this.login;
	}

	public String getName()
	{
		return this.name;
	}

	public boolean isPasswordChanged()
	{
		return this.passwordChanged;
	}
}