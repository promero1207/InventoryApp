package com.example.inventoryapp.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class InventoryContract {

    private InventoryContract() {
        // empty constructor
    }

    // package name for app use as content authority
    public static final String CONTENT_AUTHORITY = "com.example.inventoryapp";

    // base of URI
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Used to look at products data.
    public static final String PATH_PRODUCTS = "products";

    // inner class defining constant values for product database.
    public static final class ProductEntry implements BaseColumns {

        // content URI to access the pet data in provider
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PRODUCTS);

        // MIME type for a list of products
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCTS;

        // MIME type for a single product
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCTS;

        public final static String TABLE_NAME = "products";

        public final static String _ID = BaseColumns._ID;

        public final static String COLUMN_PRODUCT_NAME = "product_name";

        public final static String COLUMN_PRODUCT_PRICE = "product_price";

        public final static String COLUMN_PRODUCT_AVAILABILITY = "product_availability";

        public final static String COLUMN_PROVIDER_PHONE = "provider_phone";

        public final static String COLUMN_PRODUCT_IMAGE = "product_image";
    }
}
