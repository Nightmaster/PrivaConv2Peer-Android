package esgi.priva2peer.communication;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;

public class Connexion
{

	private Context context;

	public void StayAlive()
	{

		try
		{
			HttpClient Client = new DefaultHttpClient();
			String URL = "http://54.194.20.131:8080/webAPI/stayAlive";
			HttpGet httpget = new HttpGet(URL);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			Log.d("sessId", PreferenceManager.getDefaultSharedPreferences(context).getString("MYLABEL", "defaultStringIfNothingFound"));

			if (PreferenceManager.getDefaultSharedPreferences(context).getString("MYLABEL", "defaultStringIfNothingFound") != "defaultStringIfNothingFound")
			{
				httpget.setHeader("Cookie", PreferenceManager.getDefaultSharedPreferences(context).getString("MYLABEL", "defaultStringIfNothingFound"));
			}

			String SetServerString = "";
			SetServerString = Client.execute(httpget, responseHandler);
		}
		catch (Exception ex)
		{
			Log.d("fdf", "Fail!");
		}
	}
}
