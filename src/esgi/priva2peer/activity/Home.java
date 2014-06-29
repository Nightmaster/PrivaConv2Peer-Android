package esgi.priva2peer.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import esgi.priva2peer.R;
import esgi.priva2peer.communication.Connexion;
import esgi.priva2peer.communication.UserSessionManager;
import esgi.priva2peer.data.LoginDataBaseAdapter;

public class Home extends Activity
{
	protected static final String LOGTAG = null;

	final Context context = this;

	private DefaultHttpClient httpClient;
	private String someVariable;
	public static org.apache.http.cookie.Cookie cookie = null;

	UserSessionManager session;
	TextView content;

	Button btnSignIn, btnSignUp, btnLogout;
	LoginDataBaseAdapter loginDataBaseAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		session = new UserSessionManager(getApplicationContext());
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		Connexion con = new Connexion();
		con.StayAlive();

		// create a instance of SQLite Database
		loginDataBaseAdapter = new LoginDataBaseAdapter(this);
		loginDataBaseAdapter = loginDataBaseAdapter.open();

		btnSignIn = (Button) findViewById(R.id.buttonSignIN);
		btnSignUp = (Button) findViewById(R.id.buttonSignUP);
		btnLogout = (Button) findViewById(R.id.btnLogout);
		content = (TextView) findViewById(R.id.content);

		btnSignUp.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{

				Intent intentSignUP = new Intent(getApplicationContext(), SignUPActivity.class);
				startActivity(intentSignUP);
			}
		});

		btnLogout.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				session.logoutUser();
				HttpClient Client = new DefaultHttpClient();
				String URL = "http://54.194.20.131:8080/webAPI/disconnect";
				try
				{
					HttpGet httpget = new HttpGet(URL);
					ResponseHandler<String> responseHandler = new BasicResponseHandler();

					String SetServerString = "";
					SetServerString = Client.execute(httpget, responseHandler);
					content.setText(SetServerString);
				}
				catch (Exception ex)
				{
					content.setText("Fail!");
				}

			}
		});

	}

	public void signIn(View V)
	{
		final Dialog dialog = new Dialog(Home.this);
		dialog.setContentView(R.layout.login);
		dialog.setTitle("Login");

		final EditText editTextUserName = (EditText) dialog.findViewById(R.id.editTextUserNameToLogin);
		final EditText editTextPassword = (EditText) dialog.findViewById(R.id.editTextPasswordToLogin);

		Button btnSignIn = (Button) dialog.findViewById(R.id.buttonSignIn);
		btnSignIn.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				String userName = editTextUserName.getText().toString();
				String password = editTextPassword.getText().toString();
				try
				{
					userName = URLEncoder.encode(userName, "UTF-8");
					password = URLEncoder.encode(password, "UTF-8");

				}
				catch (Exception e)
				{}

				MessageDigest m = null;
				try
				{
					m = MessageDigest.getInstance("MD5");
				}
				catch (NoSuchAlgorithmException e)
				{
					e.printStackTrace();
				}
				m.reset();
				m.update(password.getBytes());
				byte[] digest = m.digest();
				BigInteger bigInt = new BigInteger(1, digest);
				String hashtext = bigInt.toString(16);
				while (hashtext.length() < 32)
				{
					hashtext = "0" + hashtext;
				}

				String caractere = "@";
				String login = "";
				boolean trouve = editTextUserName.getText().toString().contains(caractere);
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
					HttpClient client = new DefaultHttpClient();
					ResponseHandler<String> responseHandler = new BasicResponseHandler();
					String URL = "http://54.194.20.131:8080/webAPI/connect?" + login + editTextUserName.getText().toString() + "&pw=" + hashtext;

					HttpGet get = new HttpGet(URL);
					get.setHeader("Cookie", "application/x-zip");
					HttpResponse responseGet = client.execute(get);
					String SetServerString = "";
					SetServerString = client.execute(get, responseHandler);

					JSONObject JSONObject = new JSONObject(SetServerString);
					String name = JSONObject.get("user").toString();
					String[] parts = name.split("\"");
					Log.d("MyApp", parts[7]);

					session = new UserSessionManager(getApplicationContext());
					session.createUserLoginSession(parts[7], parts[3], parts[11], parts[15]);
					Header[] headers = responseGet.getAllHeaders();

					for (int i = 0; i < headers.length; i++ )
					{
						Header header = headers[i];
						if (header.getValue().contains("sessId") == true)
						{
							String sessId = header.getValue();
							String[] sess = sessId.split(";");

							Log.i("HeaderName", header.getValue());
							Log.i("HeaderName", sess[0]);

							// get.setHeader("Cookie", sess[0] + ";");
							// saveCookie(header.getValue());
						}
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}

				Intent list_f_intent = new Intent(getApplicationContext(), MainActivity.class);
				list_f_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(list_f_intent);
				dialog.dismiss();

			}
		});

		dialog.show();
	}

	public void saveCookie(String content)
	{
		FileOutputStream fop = null;
		File file;

		try
		{

			file = new File("cookie_session.txt");
			fop = new FileOutputStream(file);

			if (!file.exists())
			{
				file.createNewFile();
			}

			byte[] contentInBytes = content.getBytes();

			fop.write(contentInBytes);
			fop.flush();
			fop.close();

		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		// Close The Database
		loginDataBaseAdapter.close();
	}
}
