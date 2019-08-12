package com.example.inventoryapp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventoryapp.data.ArticleModel;
import com.example.inventoryapp.data.InventoryContract;
import com.example.inventoryapp.data.InventoryContract.ProductEntry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks, ProductCursorAdapter.OnItemClickListener {

    // identifier for pet data loader
    private static final int PRODUCT_LOADER = -1;

    //Adapter for RecyclerView
    ProductCursorAdapter mCursorAdapter;

    ArrayList<ArticleModel> mList;

    TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startDetail = new Intent(MainActivity.this, AddArticleActivity.class);
                startActivityForResult(startDetail, 100);
            }
        });

        // RecyclerView to be Populated
        RecyclerView productLV = findViewById(R.id.product_lv);
        mList = new ArrayList<>();

        productLV.setLayoutManager(new LinearLayoutManager(this));

        // set empty view
        emptyView = findViewById(R.id.empty_title_text);
        //productLV.setEmptyView(emptyView);

        mCursorAdapter = new ProductCursorAdapter(mList, this);
        productLV.setAdapter(mCursorAdapter);

        LoaderManager loaderManager = LoaderManager.getInstance(this);

        loaderManager.initLoader(PRODUCT_LOADER, null, this);

        // item click listener
        /*productLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent =
                        new Intent(MainActivity.this, AddArticleActivity.class);

                Uri currentProductUri = ContentUris.withAppendedId(ProductEntry.CONTENT_URI, id);
                intent.setData(currentProductUri);
                startActivity(intent);
            }
        });*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public Loader onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection = {
                ProductEntry._ID,
                ProductEntry.COLUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductEntry.COLUMN_PRODUCT_AVAILABILITY,
                ProductEntry.COLUMN_PROVIDER_PHONE,
                ProductEntry.COLUMN_PRODUCT_IMAGE};

        return new CursorLoader(this, ProductEntry.CONTENT_URI, projection,
                null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader loader, Object data) {
        Cursor cursor = (Cursor) data;
        loadDataBase(cursor);
    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {
        mList.clear();
        mCursorAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(Integer position) {

    }

    private void loadDataBase(Cursor cursor) {
        int idColumnIndex = cursor.getColumnIndex(InventoryContract.ProductEntry._ID);
        int productNameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
        int availabilityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_AVAILABILITY);
        int providerPhoneIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PROVIDER_PHONE);
        int productImageIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_IMAGE);

        Log.d("INDEXES", "" + idColumnIndex + " " + productNameColumnIndex + " " + priceColumnIndex
                + " " + availabilityColumnIndex + " " + providerPhoneIndex + " " + productImageIndex);

        try {
            //mList.clear();
            while (cursor.moveToNext()) {
                String id = cursor.getString(idColumnIndex);
                String productName = cursor.getString(productNameColumnIndex);
                String price = cursor.getString(priceColumnIndex);
                int availability = cursor.getInt(availabilityColumnIndex);
                String phone = cursor.getString(providerPhoneIndex);
                String image = cursor.getString(productImageIndex);
                mList.add(new ArticleModel(id, image, productName, price, Integer.toString(availability), phone));
            }
            mCursorAdapter.notifyDataSetChanged();

            if (!mList.isEmpty()) {
                emptyView.setVisibility(View.GONE);
            }
        } finally {
            cursor.close();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
            if (resultCode == Activity.RESULT_OK) {
                //Toast.makeText(this, "RESTART", Toast.LENGTH_SHORT).show();
                mList.clear();
                LoaderManager.getInstance(this).restartLoader(PRODUCT_LOADER, null, this);
                //LoaderManager.getInstance(this).initLoader(PRODUCT_LOADER, null, this);
            }
        }
    }


    /* @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        // projection columns from table
        String[] projection = {
                ProductEntry._ID, ProductEntry.COLUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_PRODUCT_PRICE, ProductEntry.COLUMN_PRODUCT_AVAILABILITY,
                ProductEntry.COLUMN_PRODUCT_IMAGE};

        return new CursorLoader(this, ProductEntry.CONTENT_URI, projection,
                null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }*/


}
