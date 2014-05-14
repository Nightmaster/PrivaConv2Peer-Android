package esgi.priva2peer.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class LoginDataBaseAdapter
{
	static final String DATABASE_NAME = "user.db";
	static final int DATABASE_VERSION = 1;
	public static final int NAME_COLUMN = 1;

	static final String DATABASE_CREATE = "create table " + "USER" + "( " + "ID" + " integer primary key autoincrement," + "USERNAME  text,USERMAIL text,FIRSTNAME  text,LASTNAME text,PASSWORD text); ";

	public SQLiteDatabase db;
	private final Context context;
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

	public void insertEntry(String userName, String password, String userMail, String firstName, String lastName)
	{
		ContentValues newValues = new ContentValues();
		newValues.put("USERNAME", userName);
		newValues.put("PASSWORD", password);
		newValues.put("USERMAIL", userMail);
		newValues.put("FIRSTNAME", firstName);
		newValues.put("LASTNAME", lastName);
		db.insert("USER", null, newValues);
		Toast.makeText(context, "Reminder Is Successfully Saved", Toast.LENGTH_LONG).show();
	}

	public int deleteEntry(String UserName)
	{
		// String id=String.valueOf(ID);
		String where = "USERNAME=?";
		int numberOFEntriesDeleted = db.delete("USER", where, new String[] {UserName});
		return numberOFEntriesDeleted;
	}

	public String getSinlgeEntry(String userName)
	{
		Cursor cursor = db.query("USER", null, " USERNAME=?", new String[] {userName}, null, null, null);
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

	public void updateEntry(String userName, String password, String userMail, String firstName, String lastName)
	{
		// Define the updated row content.
		ContentValues updatedValues = new ContentValues();
		// Assign values for each row.
		updatedValues.put("USERNAME", userName);
		updatedValues.put("PASSWORD", password);
		updatedValues.put("USERMAIL", userMail);
		updatedValues.put("FIRSTNAME", firstName);
		updatedValues.put("LASTNAME", lastName);

		String where = "USERNAME = ?";
		db.update("USER", updatedValues, where, new String[] {userName});
	}
}