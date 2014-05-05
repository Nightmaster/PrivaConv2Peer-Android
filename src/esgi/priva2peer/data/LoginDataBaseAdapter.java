package esgi.priva2peer.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class LoginDataBaseAdapter
{
	static final String DATABASE_NAME = "login.db";
	static final int DATABASE_VERSION = 1;
	public static final int NAME_COLUMN = 1;
	// TODO: Create public field for each column in your table.
	// SQL Statement to create a new database.
	static final String DATABASE_CREATE = "create table " + "LOGIN" + "( " + "ID" + " integer primary key autoincrement," + "USERNAME  text,PASSWORD text,USERMAIL text); ";
	// Variable to hold the database instance
	public SQLiteDatabase db;
	// Context of the application using the database.
	private final Context context;
	// Database open/upgrade helper
	private DataBaseHelper dbHelper;

	public LoginDataBaseAdapter(Context _context)
	{
		context = _context;
		dbHelper = new DataBaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public LoginDataBaseAdapter open() throws SQLException
	{
		db = dbHelper.getWritableDatabase();
		return this;
	}

	public void close()
	{
		db.close();
	}

	public SQLiteDatabase getDatabaseInstance()
	{
		return db;
	}

	public void insertEntry(String userName, String password, String userMail)
	{
		ContentValues newValues = new ContentValues();
		newValues.put("USERNAME", userName);
		newValues.put("USERMAIL", userMail);
		newValues.put("PASSWORD", password);

		db.insert("LOGIN", null, newValues);
		Toast.makeText(context, "Reminder Is Successfully Saved", Toast.LENGTH_LONG).show();
	}

	public int deleteEntry(String UserName)
	{
		String where = "USERNAME=?";
		int numberOFEntriesDeleted = db.delete("LOGIN", where, new String[] {UserName});
		return numberOFEntriesDeleted;
	}

	public String getSinlgeEntry(String userName)
	{
		Cursor cursor = db.query("LOGIN", null, " USERNAME=?", new String[] {userName}, null, null, null);
		if (cursor.getCount() < 1) // UserName Not Exist
		{
			cursor.close();
			return "NOT EXIST";
		}
		cursor.moveToFirst();
		String password = cursor.getString(cursor.getColumnIndex("PASSWORD"));
		cursor.close();
		return password;
	}

	public void updateEntry(String userName, String password, String userMail)
	{
		// Define the updated row content.
		ContentValues updatedValues = new ContentValues();
		// Assign values for each row.
		updatedValues.put("USERNAME", userName);
		updatedValues.put("USERMAIL", userMail);
		updatedValues.put("PASSWORD", password);

		String where = "USERNAME = ?";
		db.update("LOGIN", updatedValues, where, new String[] {userName});
		// db.update("LOGIN", updatedValues, where, new String[] {userMail});
	}
}