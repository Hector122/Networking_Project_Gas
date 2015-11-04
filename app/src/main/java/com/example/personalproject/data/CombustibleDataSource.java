package com.example.personalproject.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.example.personalproject.combustible.Combustible;
import com.example.personalproject.combustible.RssFeedMic;
import com.example.personalproject.data.FeedReaderContract.FeedDescription;
import com.example.personalproject.data.FeedReaderContract.FeedEntryFuel;

public class CombustibleDataSource {
	private SQLiteDatabase database;
	private DBContainer dbContainer;
	private String[] allColumns = { FeedEntryFuel.COLUMN_NAME_PUB_DATE,
			FeedEntryFuel.COLUMN_NAME_TITLE,
			FeedDescription.COLUMN_NAME_DESCRIPTION,
			FeedDescription.COLUMN_NAME_PRICE };

	public CombustibleDataSource(Context context) {
		dbContainer = new DBContainer(context);
	}

	public void open() {
		database = dbContainer.getWritableDatabase();
	}

	public void read() {
		database = dbContainer.getReadableDatabase();
	}                                 

	public void close() {
		dbContainer.close();
	}

	public long createFuelTable(RssFeedMic mic) {
		ContentValues values = new ContentValues();

		values.put(FeedEntryFuel.COLUMN_NAME_PUB_DATE, mic.getPublicationDate());
		values.put(FeedEntryFuel.COLUMN_NAME_TITLE, mic.getTitle());

		// insert row
		long insertId = database.insert(FeedEntryFuel.TABLE_NAME, null, values);

		// get the list with data
		List<Combustible> combustibles = mic.getCombustibles();

		for (Combustible combustible : combustibles) {
			// TODO: delete price after test
			double price = combustible.getPrice();
			String description = combustible.getDescription();
			String code = combustible.getCode();

			createDescriptionTable(price, description, code, insertId);
		}

		return insertId;
	}

	private long createDescriptionTable(double price, String description,
			String code, long fuel_id) {
		ContentValues values = new ContentValues();
		values.put(FeedDescription.COLUMN_NAME_FUEL_ID, fuel_id);
		values.put(FeedDescription.COLUMN_NAME_PRICE, price);
		values.put(FeedDescription.COLUMN_NAME_DESCRIPTION, description);
		values.put(FeedDescription.COLUMN_NAME_CODE, code);

		long insertId = database.insert(FeedDescription.TABLE_NAME, null,
				values);

		return insertId;
	}

	public void deleteFuelTable() {
		// TODO: make the query for this.

	}

	/**
	 * Check if the database exist
	 * 
	 * @return true if it exists, false if it doesn't
	 */
	private boolean checkDataBase() {
		SQLiteDatabase checkDB = null;
		try {
			checkDB = SQLiteDatabase.openDatabase("DB_FULL_PATH", null,
					SQLiteDatabase.OPEN_READONLY);
			checkDB.close();
		} catch (SQLiteException e) {
			// database doesn't exist yet.
		}
		return checkDB != null;
	}

	/*
	 * SELECT product_name, product_price FROM Description WHERE fuel_id_pk =
	 * (SELECT fuel_id_pk FROM Fuel ORDER BY publication_date DESC LIMIT 1)
	 */

	public RssFeedMic getLastInformation(RssFeedMic mic) {
		List<Combustible> currentCombustible = new ArrayList<Combustible>();

		String countQuery = "SELECT " + FeedDescription.COLUMN_NAME_CODE + ","
				+ FeedDescription.COLUMN_NAME_PRICE + " FROM "
				+ FeedDescription.TABLE_NAME + " WHERE "
				+ FeedDescription.COLUMN_NAME_FUEL_ID + " == (SELECT "
				+ FeedEntryFuel.COLUMN_NAME_ID + " FROM "
				+ FeedEntryFuel.TABLE_NAME + "ORDER BY"
				+ FeedEntryFuel.COLUMN_NAME_PUB_DATE + "DESC LIMIT 1 )";

		SQLiteDatabase database = dbContainer.getReadableDatabase();
		Cursor cursor = database.rawQuery(countQuery, null);

		Map<String, Double> map = new HashMap<String, Double>();

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			map.put(cursor.getString(cursor
					.getColumnIndex(FeedDescription.COLUMN_NAME_CODE)), cursor
					.getDouble(cursor
							.getColumnIndex(FeedDescription.COLUMN_NAME_PRICE)));

			cursor.moveToNext();
		}

		List<Combustible> combustibles = mic.getCombustibles();

		for (Combustible combustible : combustibles) {
			combustible.setLastPrice(map.get(combustible.getCode()));
			currentCombustible.add(combustible);
		}

		mic.setCombustibles(currentCombustible);

		return mic;
	}
}
