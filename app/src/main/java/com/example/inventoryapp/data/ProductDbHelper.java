package com.example.inventoryapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.inventoryapp.data.InventoryContract.ProductEntry;

public class ProductDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "inventory.db";

    private static final int DATABASE_VERSION = 1;

    public ProductDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_PRODUCTS_TABLE = "CREATE TABLE " + ProductEntry.TABLE_NAME + " ("
            + ProductEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + ProductEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL, "
            + ProductEntry.COLUMN_PRODUCT_PRICE + " TEXT, "
            + ProductEntry.COLUMN_PRODUCT_AVAILABILITY + " INTEGER NOT NULL DEFAULT 0, "
            + ProductEntry.COLUMN_PROVIDER_PHONE + " TEXT, "
            + ProductEntry.COLUMN_PRODUCT_IMAGE + " TEXT);";

        db.execSQL(SQL_CREATE_PRODUCTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // database at version 1
    }
}
