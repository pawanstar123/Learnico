package com.example.learnico;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDatabaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "learnico_users.db";
	private static final int DATABASE_VERSION = 1;

	public static final String TABLE_USERS = "users";
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_EMAIL = "email";
	public static final String COLUMN_PASSWORD = "password";

	private static final String SQL_CREATE_TABLE =
			"CREATE TABLE IF NOT EXISTS " + TABLE_USERS + " (" +
					COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					COLUMN_NAME + " TEXT NOT NULL, " +
					COLUMN_EMAIL + " TEXT NOT NULL UNIQUE, " +
					COLUMN_PASSWORD + " TEXT NOT NULL" +
			")";

	public UserDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// For future schema changes
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
		onCreate(db);
	}

	public boolean registerUser(String name, String email, String password) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(COLUMN_NAME, name);
		values.put(COLUMN_EMAIL, email);
		values.put(COLUMN_PASSWORD, password);
		long rowId = -1;
		try {
			rowId = db.insert(TABLE_USERS, null, values);
		} catch (Exception ignored) {
			// Let the method return false for duplicates or errors
		} finally {
			db.close();
		}
		return rowId != -1;
	}

	public boolean isEmailTaken(String email) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = null;
		try {
			cursor = db.query(
					TABLE_USERS,
					new String[]{COLUMN_ID},
					COLUMN_EMAIL + " = ?",
					new String[]{email},
					null,
					null,
					null
			);
			return cursor != null && cursor.moveToFirst();
		} finally {
			if (cursor != null) cursor.close();
			db.close();
		}
	}

	public boolean checkUserCredentials(String email, String password) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = null;
		try {
			cursor = db.query(
					TABLE_USERS,
					new String[]{COLUMN_ID},
					COLUMN_EMAIL + " = ? AND " + COLUMN_PASSWORD + " = ?",
					new String[]{email, password},
					null,
					null,
					null
			);
			return cursor != null && cursor.moveToFirst();
		} finally {
			if (cursor != null) cursor.close();
			db.close();
		}
	}
}


