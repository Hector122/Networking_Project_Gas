package com.example.personalproject.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.personalproject.data.FeedReaderContract.FeedDescription;
import com.example.personalproject.data.FeedReaderContract.FeedEntryFuel;

/**
 * This class is responsible for creating the database.
 * 
 * Note: If you change the database schema, you must increment the database
 * version.
 * */
public class DBContainer extends SQLiteOpenHelper {
	public static final int DATABASE_VERSION = 1;
	
	public static final String DATABASE_NAME = "Combustible_RD.db";

	// Database creation SQL statement
	private static final String DATABASE_CREATE_TABLE_FUEL = "CREATE TABLE "
			+ FeedEntryFuel.TABLE_NAME + "(" + FeedEntryFuel.COLUMN_NAME_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ FeedEntryFuel.COLUMN_NAME_PUB_DATE + " DATETIME NOT NULL, "
			+ FeedEntryFuel.COLUMN_NAME_TITLE + " TEXT NOT NULL, " + " UNIQUE("
			+ FeedEntryFuel.COLUMN_NAME_PUB_DATE + ")";

	private static String DATABASE_CREATE_TABLE_DESCRIPTION = "CREATE TABLE "
			+ FeedDescription.TABLE_NAME + "("
			+ FeedDescription.COLUMN_NAME_DESCRIPTION_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ FeedDescription.COLUMN_NAME_DESCRIPTION + " TEXT NOT NULL, "
			+ FeedDescription.COLUMN_NAME_PRICE + " REAL NOT NULL,"
			+ FeedDescription.COLUMN_NAME_CODE + "TEXT, "
			+ FeedDescription.COLUMN_NAME_FUEL_ID + "INTEGER, FOREIGN KEY ("
			+ FeedDescription.COLUMN_NAME_FUEL_ID + ") REFERENCES "
			+ FeedEntryFuel.TABLE_NAME + "(" + FeedEntryFuel.COLUMN_NAME_ID
			+ ") )";

	// Constructors of the class
	public DBContainer(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE_TABLE_FUEL);
		db.execSQL(DATABASE_CREATE_TABLE_DESCRIPTION);

	}

	// TODO: check the logic of this table.
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS" + FeedEntryFuel.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS" + FeedDescription.TABLE_NAME);

		// create new tables
		onCreate(db);
	}

	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onUpgrade(db, oldVersion, newVersion);
	}

}
