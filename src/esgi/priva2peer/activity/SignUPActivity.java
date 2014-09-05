package esgi.priva2peer.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.MessageDigest;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import esgi.priva2peer.R;

/**
 * @author Bruno Gb
 */
public class SignUPActivity extends Activity
{
	EditText editTextUserName, editTextPassword, editTextConfirmPassword, editTextUserMail, editTextFirstName, editTextLastName, securePassword, confirmSecurePassword;
	Spinner spinner;
	Button btnCreateAccount;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup);

		editTextUserName = (EditText) findViewById(R.id.editTextUserName);
		editTextUserMail = (EditText) findViewById(R.id.editTextUserMail);
		editTextFirstName = (EditText) findViewById(R.id.editTextFirstName);
		editTextLastName = (EditText) findViewById(R.id.editTextLastName);
		editTextPassword = (EditText) findViewById(R.id.editTextPassword);
		editTextConfirmPassword = (EditText) findViewById(R.id.editTextConfirmPassword);
		securePassword = (EditText) findViewById(R.id.SecurePassword);
		confirmSecurePassword = (EditText) findViewById(R.id.ConfirmSecurePassword);

		spinner = (Spinner) this.findViewById(R.id.spinner1);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sports, android.R.layout.simple_spinner_item);
		spinner.setAdapter(adapter);

		btnCreateAccount = (Button) findViewById(R.id.buttonCreateAccount);

		btnCreateAccount.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				String userName = editTextUserName.getText().toString();
				String userMail = editTextUserMail.getText().toString();
				String lastName = editTextLastName.getText().toString();
				String firstName = editTextFirstName.getText().toString();
				String password = editTextPassword.getText().toString();
				String confirmPassword = editTextConfirmPassword.getText().toString();
				String SecurePassword = securePassword.getText().toString();
				String ConfirmSecurePassword = confirmSecurePassword.getText().toString();

				String spinner_crypt = spinner.getSelectedItem().toString();
				// PasswordUtilities pass = new PasswordUtilities();
				// HashMap<String, Boolean> value =
				// PasswordUtilities.isStrongEnough(password);
				// Log.d("MyApp", "Registration success" + value);
				if (userName.equals("") || password.equals("") || confirmPassword.equals(""))
				{
					Toast.makeText(getApplicationContext(), "Field Vaccant", Toast.LENGTH_LONG).show();
					return;
				}
				if (!password.equals(confirmPassword) || !SecurePassword.equals(ConfirmSecurePassword))
				{
					Toast.makeText(getApplicationContext(), "Password does not match", Toast.LENGTH_LONG).show();
					return;
				}
				else
				{
					URL url = null;
					try
					{
						// Hash du pass
						MessageDigest m = MessageDigest.getInstance("MD5");
						m.reset();
						m.update(password.getBytes());
						byte[] digest = m.digest();
						BigInteger bigInt = new BigInteger(1, digest);
						String hashpass = bigInt.toString(16);
						while (hashpass.length() < 32)
						{
							hashpass = "0" + hashpass;
						}

						// Hash du 2em pass
						MessageDigest k = MessageDigest.getInstance("MD5");
						k.reset();
						k.update(SecurePassword.getBytes());
						byte[] digest_k = k.digest();
						BigInteger bigInt_k = new BigInteger(1, digest_k);
						String hash_k = bigInt_k.toString(16);
						while (hash_k.length() < 32)
						{
							hash_k = "0" + hash_k;
						}
						String registrationUrl = String.format("http://54.194.20.131:8080/webAPI/register?" + "username=" + userName + "&email=" + userMail + "&firstname=" + firstName + "&name=" + lastName + "&pw=" + hashpass + "&pwK=" + hash_k + "&length=" + spinner_crypt, userName, URLEncoder.encode(userName, "UTF-8"));
						System.out.println("http://54.194.20.131:8080/webAPI/register?" + "username=" + userName + "&email=" + userMail + "&firstname=" + firstName + "&name=" + lastName + "&pw=" + hashpass + "&pwK=" + hash_k + "&length=" + spinner_crypt);
						url = new URL(registrationUrl);
						URLConnection connection = url.openConnection();
						HttpURLConnection httpConnection = (HttpURLConnection) connection;
						int responseCode = httpConnection.getResponseCode();
						if (responseCode == HttpURLConnection.HTTP_OK)
						{
							Log.d("MyApp", "Registration success");
						}
						else
						{
							Log.w("MyApp", "Registration failed for: " + registrationUrl);
						}
					}
					catch (Exception ex)
					{
						ex.printStackTrace();
					}

					Toast.makeText(getApplicationContext(), "Account Successfully Created ", Toast.LENGTH_LONG).show();
				}
			}
		});

	}

	public String getJSON(String address)
	{
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(address);
		try
		{
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200)
			{
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null)
				{
					builder.append(line);
				}
			}
			else
			{
				Log.e(SignUPActivity.class.toString(), "Failed to get JSON object");
			}
		}
		catch (ClientProtocolException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return builder.toString();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}
}