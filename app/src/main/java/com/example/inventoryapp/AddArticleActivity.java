package com.example.inventoryapp;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.bumptech.glide.Glide;
import com.example.inventoryapp.data.InventoryContract.ProductEntry;

public class AddArticleActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXISTING_PRODUCT_LOADER = 0;

    private EditText mProductNameET;

    private EditText mProductPriceET;

    private EditText mProductAvailabilityET;

    private EditText mProductProviderET;

    private Button mAddPhotoBt;

    private Button mCallProvider;

    private ImageView mProductIV;

    private boolean mProductHasChanged = false;

    private Uri mCurrentProductUri;

    private static final int PICK_IMAGE = 1001;

    private static String imageUri;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mProductHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_article);

        final Intent intent = getIntent();
        mCurrentProductUri = ((Intent) intent).getData();

        if (mCurrentProductUri == null) {
            setTitle(R.string.add_product);
            invalidateOptionsMenu();
        } else {
            setTitle(R.string.edit_pet);
            LoaderManager.getInstance(this)
                    .initLoader(EXISTING_PRODUCT_LOADER, null, this);
        }

        mProductNameET = findViewById(R.id.editTextName);
        mProductPriceET = findViewById(R.id.editTextPrice);
        mProductAvailabilityET = findViewById(R.id.editTextAvailability);
        mProductProviderET = findViewById(R.id.editTextPhone);
        mAddPhotoBt = findViewById(R.id.buttonAddPhoto);
        mProductIV = findViewById(R.id.imageView2);
        mCallProvider = findViewById(R.id.call_supplier);

        mAddPhotoBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galeryIntent = new Intent();
                galeryIntent.setType("image/*");
                galeryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galeryIntent,
                        getString(R.string.add_photo)), PICK_IMAGE);
            }
        });

        // OnTouchListener to know if user has modify a field
        mProductNameET.setOnTouchListener(mTouchListener);
        mProductPriceET.setOnTouchListener(mTouchListener);
        mProductAvailabilityET.setOnTouchListener(mTouchListener);
        mProductProviderET.setOnTouchListener(mTouchListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICK_IMAGE) {
            Uri uri = data.getData();
            if (uri != null) {
                imageUri = uri.toString();
            }
            Glide.with(this).load(uri).into(mProductIV);
            mProductIV.setVisibility(View.VISIBLE);
        }
    }

    private void saveProduct() {
        String productName = mProductNameET.getText().toString().trim();
        String productPrice = mProductPriceET.getText().toString().trim();
        Integer productAvailability = Integer.parseInt(mProductAvailabilityET.getText().toString());
        String productProvider = mProductProviderET.getText().toString().trim();

        // check if no info was enter
        if (TextUtils.isEmpty(productName) && TextUtils.isEmpty(productPrice) &&
                productAvailability != null && TextUtils.isEmpty(productProvider)) {
            return;
        }

        // ContentValues object with keys and atributes from the edit text
        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_PRODUCT_NAME, productName);
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, productPrice);
        values.put(ProductEntry.COLUMN_PRODUCT_AVAILABILITY, productAvailability);
        values.put(ProductEntry.COLUMN_PROVIDER_PHONE, productProvider);
        values.put(ProductEntry.COLUMN_PRODUCT_IMAGE, imageUri);

        if (mCurrentProductUri == null) {

            Uri newUri = getContentResolver().insert(ProductEntry.CONTENT_URI, values);

            if (newUri == null) {
                Toast.makeText(this, "Error with saving product",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Product Saved",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            int rowsAffected = getContentResolver().update(mCurrentProductUri, values,
                    null, null);
            if (rowsAffected == 0) {
                Toast.makeText(this, "Error with updating product", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Product updated", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_product, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                setResult(Activity.RESULT_OK);
                saveProduct();
                finish();
                return true;
            case android.R.id.home:
                if (!mProductHasChanged) {
                    NavUtils.navigateUpFromSameTask(AddArticleActivity.this);
                    return true;
                }

                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(AddArticleActivity.this);
                            }
                        };
                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // If the product hasn't changed, continue with handling back button press
        if (!mProductHasChanged) {
            super.onBackPressed();
            return;
        }
        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };
        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create AlertDialog.Builder and set message
        androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        androidx.appcompat.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        // Since the detail article shows all product attributes, define a projection that contains
        // all columns from the product table
        String[] projection = {
                ProductEntry._ID,
                ProductEntry.COLUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductEntry.COLUMN_PRODUCT_AVAILABILITY,
                ProductEntry.COLUMN_PROVIDER_PHONE,
                ProductEntry.COLUMN_PRODUCT_IMAGE};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this, mCurrentProductUri, projection, null,
                null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if (data != null) {
            // find columns of products attributes
            int nameColumnIndex = data.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = data.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
            int availabilityColumnIndex =
                    data.getColumnIndex(ProductEntry.COLUMN_PRODUCT_AVAILABILITY);
            int providerColumnIndex = data.getColumnIndex(ProductEntry.COLUMN_PROVIDER_PHONE);
            int imageColumnIndex = data.getColumnIndex(ProductEntry.COLUMN_PRODUCT_IMAGE);

            String pName = data.getString(nameColumnIndex);
            String pPrice = data.getString(priceColumnIndex);
            int pAvailability = data.getInt(availabilityColumnIndex);
            final String pProvider = data.getString(providerColumnIndex);
            String pImage = data.getString(imageColumnIndex);

            // update views on screen with values from database
            mProductNameET.setText(pName);
            mProductPriceET.setText(pPrice);
            mProductAvailabilityET.setText(Integer.toString(pAvailability));
            mProductProviderET.setText(pProvider);
            if (pImage != null) {
                Glide.with(this).load(Uri.parse(pImage)).into(mProductIV);
                mProductIV.setVisibility(View.VISIBLE);
            }

            mCallProvider.setVisibility(View.VISIBLE);
            mCallProvider.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + pProvider));
                    startActivity(intent);
                }
            });


        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        // if loader is invalidated, clear out all data
        mProductNameET.setText("");
        mProductPriceET.setText("");
        mProductAvailabilityET.setText("");
        mProductProviderET.setText("");
    }
}
