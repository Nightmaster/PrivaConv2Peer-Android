package esgi.priva2peer.communication.parser;

import esgi.priva2peer.communication.parser.subclasses.ChangedValues;
import org.json.JSONException;
import org.json.JSONObject;

public class ModifyProfileJsonParser
{
	private String displayMessage = null;
	private boolean error, profileModified;
	private int httpCode = 200;
	private ChangedValues newValues;

	ModifyProfileJsonParser(JSONObject json) throws JSONException
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