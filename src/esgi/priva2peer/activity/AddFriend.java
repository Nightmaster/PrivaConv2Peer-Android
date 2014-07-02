package esgi.priva2peer.activity;

import java.net.URLEncoder;
import java.util.ArrayList;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import esgi.priva2peer.R;
import esgi.priva2peer.communication.Connexion;

public class AddFriend extends Activity
{
	EditText UserName;
	TextView content;
	Button SearchMail;

	ArrayList<String> list = new ArrayList<String>();

	ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_friend);

		content = (TextView) findViewById(R.id.content);
		UserName = (EditText) findViewById(R.id.userName);
		SearchMail = (Button) findViewById(R.id.SearchMail);

		Connexion con = new Connexion();
		con.StayAlive();

		SearchMail.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				String userName = UserName.getText().toString();

				if (userName.equals(""))
				{
					Toast.makeText(getApplicationContext(), "Field Vaccant", Toast.LENGTH_LONG).show();
					return;
				}
				else
				{
					String caractere = "@";
					String login = "";
					boolean trouve = userName.contains(caractere);
					if (trouve == true)
					{
						login += "email=";
					}
					else
					{
						login += "username=";
					}
					try
					{
						userName = URLEncoder.encode(userName, "UTF-8");

					}
					catch (Exception e)
					{}
					String URL = "http://54.194.20.131:8080/webAPI/addFriend?" + login + userName;
					try
					{
						HttpClient client = new DefaultHttpClient();
						HttpGet get = new HttpGet(URL);

						HttpResponse responseGet = client.execute(get);

						Header[] headers = responseGet.getAllHeaders();

						for (int i = 0; i < headers.length; i++ )
						{
							Header header = headers[i];
							if (header.getValue().contains("sessId") == true)
							{
								String sessId = header.getValue();
								String[] sess = sessId.split(";");
								get.setHeader("Cookie", sess[0]);

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
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.layout.add_friend, menu);
		return true;
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}

}
