package com.example.personalproject.data;

import android.provider.BaseColumns;

public class FeedReaderContract {

	public FeedReaderContract() {
	}

	public static abstract class FeedEntryFuel implements BaseColumns {
		public static final String TABLE_NAME = "fuel";
		public static final String COLUMN_NAME_ID = "fuel_id_pk";
		public static final String COLUMN_NAME_PUB_DATE = "publication_date";
		public static final String COLUMN_NAME_TITLE = "title";
	}

	public static abstract class FeedDescription implements BaseColumns {
		public static final String TABLE_NAME = "description";
		public static final String COLUMN_NAME_DESCRIPTION_ID = "description_id_pk";
		public static final String COLUMN_NAME_FUEL_ID = "fuel_id_pk";
		public static final String COLUMN_NAME_CODE = "product_code";
		public static final String COLUMN_NAME_DESCRIPTION = "product_name";
		public static final String COLUMN_NAME_PRICE = "product_price";

	}
}
