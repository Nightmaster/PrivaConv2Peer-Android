package esgi.priva2peer.communication.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import esgi.priva2peer.communication.parser.subclasses.UserInfos;

/**
 * Parse search JSON
 * 
 * @author Gael B.
 **/
public class SearchJsonParser
{
	private String displayMessage = null;
	private boolean error;
	private int httpCode = 200;
	private UserInfos[] profiles = null;

	/**
	 * This class is made to parse the JSON returned by the server's web service
	 * when a search is performed
	 * 
	 * @param json {JSONObject}: the JSON returned by the server's web service
	 * @throws JSONException Can throw exceptions because of illegal arguments
	 **/
	SearchJsonParser(JSONObject json) throws JSONException
	{
		this.error = json.getBoolean("error");
		if (this.error)
		{
			this.displayMessage = json.getString("displayMessage");
			this.httpCode = json.getInt("httpErrorCode");
		}
		if (json.isNull("profiles"))
		{
			this.profiles = null;
		}
		else
		{
			JSONArray profiles = json.getJSONArray("profiles");
			this.profiles = new UserInfos[profiles.length()];
			for (int i = 0; i < profiles.length(); i++ )
				this.profiles[i] = new UserInfos(profiles.getJSONObject(i));
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

	public UserInfos[] getProfiles()
	{
		return this.profiles;
	}

	public boolean isError()
	{
		return this.error;
	}
}