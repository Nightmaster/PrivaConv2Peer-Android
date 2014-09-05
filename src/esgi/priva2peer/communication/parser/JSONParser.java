package esgi.priva2peer.communication.parser;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONParser
{
	private static final String DISCONNECTION = "Disconnection", REGISTRATION = "Registration";

	public static AddFriendJSONParser getAddFriendParser(JSONObject json) throws JSONException
	{
		return new AddFriendJSONParser(json);
	}

	public static AnswerRequestJSONParser getAnswerRequestParser(JSONObject json) throws JSONException
	{
		return new AnswerRequestJSONParser(json);
	}

	public static ClientIPJSONParser getClientIPParser(JSONObject json) throws JSONException
	{
		return new ClientIPJSONParser(json);
	}

	public static ConnectionJSONParser getConnectionParser(JSONObject json) throws JSONException
	{
		return new ConnectionJSONParser(json);
	}

	public static SimpleJSONParser getDisconnectionParser(JSONObject json) throws JSONException
	{
		return new SimpleJSONParser(json, DISCONNECTION);
	}

	public static ModifyProfileJSONParser getodifyProfileParser(JSONObject json) throws JSONException
	{
		return new ModifyProfileJSONParser(json);
	}

	public static PrivateKeyJSONParser getPrivateKeyParser(JSONObject json) throws JSONException
	{
		return new PrivateKeyJSONParser(json);
	}

	public static PublicKeyJSONParser getPublicKeyParser(JSONObject json) throws JSONException
	{
		return new PublicKeyJSONParser(json);
	}

	public static SimpleJSONParser getRegistrationParser(JSONObject json) throws JSONException
	{
		return new SimpleJSONParser(json, REGISTRATION);
	}

	public static SearchJSONParser getSearchParser(JSONObject json) throws JSONException
	{
		return new SearchJSONParser(json);
	}

	public static ShowProfileJSONParser getShowProfileParser(JSONObject json) throws JSONException
	{
		return new ShowProfileJSONParser(json);
	}

	public static StayAliveJSONParser getStayAliveParser(JSONObject json) throws JSONException
	{
		return new StayAliveJSONParser(json);
	}
}
