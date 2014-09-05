package esgi.priva2peer.communication.parser;

import org.json.JSONException;
import org.json.JSONObject;

public class ModifyProfileJSONParser
{
	private boolean error, profileModified;
	private int httpCode = 200;
	private String displayMessage = null;
	private ChangedValues newValues;

	public ModifyProfileJSONParser(JSONObject json) throws JSONException
	{
		this.error = json.getBoolean("error");
		if (true == this.error)
		{
			this.displayMessage = json.getString("displayMessage");
			this.httpCode = json.getInt("httpErrorCode");
		}
		else
		{
			this.profileModified = json.getBoolean("modifications");
			this.newValues = new ChangedValues(json.getJSONObject("newValues"));
		}
	}

	public String getDisplayMessage()
	{
		return this.displayMessage;
	}

	public int getHttpCode()
	{
		return this.httpCode;
	}

	public ChangedValues getNewValues()
	{
		return this.newValues;
	}

	public boolean isError()
	{
		return this.error;
	}

	public boolean isProfileModified()
	{
		return this.profileModified;
	}
}

class ChangedValues
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
