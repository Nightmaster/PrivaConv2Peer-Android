package esgi.priva2peer.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class AddFriendDataBase
{
	static final String DATABASE_NAME = "friend_user.db";
	static final int DATABASE_VERSION = 1;
	public static final int NAME_COLUMN = 1;

	static final String DATABASE_CREATE = "create table " + "friends" + "( " + "ID_friends" + " integer primary key autoincrement," + "friends_NAME  text,friends_MAIL  text); ";
	public SQLiteDatabase db;
	private final Context context;
	private DataBaseHelper dbHelper;

	public AddFriendDataBase(Context _context)
	{
		context = _context;
		dbHelper = new DataBaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public AddFriendDataBase open() throws SQLException
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

	public void insertEntry(String friendsName, String friendsMail)
	{
		ContentValues newValues = new ContentValues();
		newValues.put("friends_NAME", friendsName);
		newValues.put("friends_MAIL", friendsMail);

		db.insert("friends", null, newValues);
	}

	public int deleteEntry(String friendsName)
	{
		// String id=String.valueOf(ID);
		String where = "friends_NAME=?";
		int numberOFEntriesDeleted = db.delete("friends", where, new String[] {friendsName});
		return numberOFEntriesDeleted;
	}

	public String getSinlgeEntry(String friendsName)
	{
		Cursor cursor = db.query("friends", null, " friends_NAME=?", new String[] {friendsName}, null, null, null);
		if (cursor.getCount() < 1) // UserName Not Exist
		{
			cursor.close();
			return "NOT EXIST";
		}
		cursor.moveToFirst();
		String mail = cursor.getString(cursor.getColumnIndex("friends_MAIL"));
		cursor.close();
		return mail;
	}

	public void updateEntry(String friendsName, String friendsMail)
	{
		// Define the updated row content.
		ContentValues updatedValues = new ContentValues();
		updatedValues.put("friends_NAME", friendsName);
		updatedValues.put("friends_MAIL", friendsMail);

		String where = "friends_NAME = ?";
		db.update("friends", updatedValues, where, new String[] {friendsName});
	}

}