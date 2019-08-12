package com.example.inventoryapp.model;

public class InventoryItem {
    private String mProductName;
    private String mProductPrice;
    private int mProductAvailability;
    private String mProductProviderNumber;
    private String mProductImageUri;

    public InventoryItem(String productName, String productPrice,
                         int productAvailability, String productProvider, String productImageUri){
        mProductName = productName;
        mProductPrice = productPrice;
        mProductAvailability = productAvailability;
        mProductProviderNumber = productProvider;
        mProductImageUri = productImageUri;
    }

    public String getmProductName() {
        return mProductName;
    }

    public String getmProductPrice() {
        return mProductPrice;
    }

    public int getmProductAvailability() {
        return mProductAvailability;
    }

    public String getmProductProviderNumber() {
        return mProductProviderNumber;
    }

    public String getmProductImageUri() {
        return mProductImageUri;
    }
}
