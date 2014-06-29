package esgi.priva2peer.communication;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import android.util.Log;

public class Connexion
{

	public void StayAlive()
	{
		try
		{
			HttpClient client = new DefaultHttpClient();
			String URL = "http://54.194.20.131:8080/webAPI/stayAlive";
			HttpGet get = new HttpGet(URL);
			get.setHeader("Content-Type", "application/x-zip");
			HttpResponse responseGet = client.execute(get);
			String fdf;

			Header[] headers = responseGet.getAllHeaders();

			for (int i = 0; i < headers.length; i++ )
			{
				Header header = headers[i];
				if (header.getValue().contains("sessId") == true)
				{
					String sessId = header.getValue();
					String[] sess = sessId.split(";");
					fdf = sess[0];

					String URL1 = "http://54.194.20.131:8080/webAPI/stayAlive";
					HttpGet gett = new HttpGet(URL1);
					gett.setHeader("Cookie", sess[0] + ';');
					HttpResponse send_sess = client.execute(gett);

					Log.i("HeaderName", sess[0]);
				}
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
