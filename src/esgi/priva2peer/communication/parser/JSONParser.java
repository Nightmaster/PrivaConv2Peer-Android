package esgi.priva2peer.communication.parser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * The factory for all JSON Parsers
 *
 * @author Gael B.
 */
public class JSONParser
{
	private static final String DISCONNECTION = "Disconnection", REGISTRATION = "Registration";

	/**
	 * Create an instance of {@link AddFriendJsonParser}
	 *
	 * @param json {JSONObject}: the JSON returned by the web service on this action
	 * @return {AddFriendJSONParser} the add friend parser
	 * @throws JSONException
	 **/
	public static AddFriendJsonParser getAddFriendParser(String json) throws JSONException
	{
		return new AddFriendJsonParser(new JSONObject(json));
	}

	/**
	 * Create an instance of {@link AnswerRequestJsonParser}
	 *
	 * @param json {JSONObject}: the JSON returned by the web service on this action
	 * @return {AnswerRequestJSONParser} the answer friend request parser
	 * @throws JSONException
	 **/
	public static AnswerRequestJsonParser getAnswerRequestParser(String json) throws JSONException
	{
		return new AnswerRequestJsonParser(new JSONObject(json));
	}

	/**
	 * Create an instance of {@link ClientIpJsonParser}
	 *
	 * @param json {JSONObject}: the JSON returned by the web service on this action
	 * @return {ClientIPJSONParser} the client IP parser
	 * @throws JSONException
	 **/
	public static ClientIpJsonParser getClientIpParser(String json) throws JSONException
	{
		return new ClientIpJsonParser(new JSONObject(json));
	}

	/**
	 * Create an instance of {@link ConnectionJsonParser}
	 *
	 * @param json {JSONObject}: the JSON returned by the web service on this action
	 * @return {ConnectionJSONParser} the connection parser
	 * @throws JSONException
	 **/
	public static ConnectionJsonParser getConnectionParser(String json) throws JSONException
	{
		return new ConnectionJsonParser(new JSONObject(json));
	}

	/**
	 * Create an instance of {@link SimpleJsonParser}
	 *
	 * @param json {JSONObject}: the JSON returned by the web service on this action
	 * @return {SimpleJSONParser} the disconnection parser
	 * @throws JSONException
	 **/
	public static SimpleJsonParser getDisconnectionParser(String json) throws JSONException
	{
		return new SimpleJsonParser(new JSONObject(json), DISCONNECTION);
	}

	/**
	 * Create an instance of {@link ModifyProfileJsonParser}
	 *
	 * @param json {JSONObject}: the JSON returned by the web service on this action
	 * @return {ModifyProfileJSONParser} the modify profile parser
	 * @throws JSONException
	 **/
	public static ModifyProfileJsonParser getodifyProfileParser(String json) throws JSONException
	{
		return new ModifyProfileJsonParser(new JSONObject(json));
	}

	/**
	 * Create an instance of {@link PrivateKeyJsonParser}
	 *
	 * @param json {JSONObject}: the JSON returned by the web service on this action
	 * @return {PrivateKeyJSONParser} the private key parser
	 * @throws JSONException
	 **/
	public static PrivateKeyJsonParser getPrivateKeyParser(String json) throws JSONException
	{
		return new PrivateKeyJsonParser(new JSONObject(json));
	}

	/**
	 * Create an instance of {@link PublicKeyJsonParser}
	 *
	 * @param json {JSONObject}: the JSON returned by the web service on this action
	 * @return {PublicKeyJSONParser} the public key parser
	 * @throws JSONException
	 **/
	public static PublicKeyJsonParser getPublicKeyParser(String json) throws JSONException
	{
		return new PublicKeyJsonParser(new JSONObject(json));
	}

	/**
	 * Create an instance of {@link SimpleJsonParser}
	 *
	 * @param json {JSONObject}: the JSON returned by the web service on this action
	 * @return {SimpleJSONParser} the registration parser
	 * @throws JSONException
	 **/
	public static SimpleJsonParser getRegistrationParser(String json) throws JSONException
	{
		return new SimpleJsonParser(new JSONObject(json), REGISTRATION);
	}

	/**
	 * Create an instance of {@link SearchJsonParser}
	 *
	 * @param json {JSONObject}: the JSON returned by the web service on this action
	 * @return {SearchJSONParser} the search parser
	 * @throws JSONException
	 **/
	public static SearchJsonParser getSearchParser(String json) throws JSONException
	{
		return new SearchJsonParser(new JSONObject(json));
	}

	/**
	 * Create an instance of {@link ShowProfileJsonParser}
	 *
	 * @param json {JSONObject}: the JSON returned by the web service on this action
	 * @return {ShowProfileJSONParser} the show profile parser
	 * @throws JSONException
	 **/
	public static ShowProfileJsonParser getShowProfileParser(String json) throws JSONException
	{
		return new ShowProfileJsonParser(new JSONObject(json));
	}

	/**
	 * Create an instance of {@link StayAliveJsonParser}
	 *
	 * @param json {JSONObject}: the JSON returned by the web service on this action
	 * @return {StayAliveJSONParser} the stay alive parser
	 * @throws JSONException
	 **/
	public static StayAliveJsonParser getStayAliveParser(String json) throws JSONException
	{
		return new StayAliveJsonParser(new JSONObject(json));
	}
}