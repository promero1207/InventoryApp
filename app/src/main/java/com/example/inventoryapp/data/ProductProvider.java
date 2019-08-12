package com.example.inventoryapp.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.inventoryapp.data.InventoryContract.ProductEntry;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ProductProvider extends ContentProvider {

    public static final String LOG_TAG = ProductProvider.class.getSimpleName();

    // Uri matcher code for table
    private static final int PRODUCTS = 100;

    // Uri matcher code for sigle product
    private static final int PRODUCT_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY,
                InventoryContract.PATH_PRODUCTS, PRODUCTS);

        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY,
                InventoryContract.PATH_PRODUCTS + "/#", PRODUCT_ID);
    }

    private ProductDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new ProductDbHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                cursor = database.query(ProductEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case PRODUCT_ID:
                selection = ProductEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                cursor = database.query(ProductEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;

                default:
                    throw new IllegalArgumentException("Cannot query unknown URI" + uri);
        }

        ContentResolver cR = getContext().getContentResolver();
        if (cR != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                return ProductEntry.CONTENT_LIST_TYPE;
            case PRODUCT_ID:
                return ProductEntry.CONTENT_ITEM_TYPE;
                default:
                    throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                if (contentValues != null) {
                    return insertProduct(uri, contentValues);
                }

            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertProduct(Uri uri, ContentValues values) {
        String name = values.getAsString(ProductEntry.COLUMN_PRODUCT_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Product Requires a Name");
        }

        Integer availability = values.getAsInteger(ProductEntry.COLUMN_PRODUCT_AVAILABILITY);
        if (availability == null || availability < 0){
            throw new IllegalArgumentException("Product requires valid availability");
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        long id = database.insert(ProductEntry.TABLE_NAME, null, values);
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        if (getContext().getContentResolver() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                rowsDeleted = database.delete(ProductEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case PRODUCT_ID:
                selection = ProductEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(ProductEntry.TABLE_NAME, selection, selectionArgs);
                break;
                default:
                    throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        if (rowsDeleted != 0 && getContext().getContentResolver() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues,
                      @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                if (contentValues != null) {
                    return updateProduct(uri, contentValues, selection, selectionArgs);
                }
            case PRODUCT_ID:
                selection = ProductEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                if (contentValues != null) {
                    return updateProduct(uri, contentValues, selection, selectionArgs);
                }

                default:
                    throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateProduct(Uri uri, ContentValues values,
                              String selection, String[] selectionArgs) {

        if (values.containsKey(ProductEntry.COLUMN_PRODUCT_NAME)) {
            String name = values.getAsString(ProductEntry.COLUMN_PRODUCT_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Product Requires a Name");
            }
        }

        if (values.containsKey(ProductEntry.COLUMN_PRODUCT_AVAILABILITY)) {
            Integer availability = values.getAsInteger(ProductEntry.COLUMN_PRODUCT_AVAILABILITY);
            if (availability == null || availability < 0) {
                throw new IllegalArgumentException("Product requires valid availability");
            }
        }

        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsUpdate = database.update(ProductEntry.TABLE_NAME, values, selection, selectionArgs);

        if (rowsUpdate != 0 && getContext().getContentResolver() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdate;
    }
}
