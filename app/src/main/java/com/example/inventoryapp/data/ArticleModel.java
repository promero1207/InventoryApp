package com.example.inventoryapp.data;

public class ArticleModel {

    private String id;
    private String image;
    private String productName;
    private String productPrice;
    private String availability;
    private String phone;

    public ArticleModel(String id, String image, String productName, String productPrice, String availability, String phone) {
        this.id = id;
        this.image = image;
        this.productName = productName;
        this.productPrice = productPrice;
        this.availability = availability;
        this.phone = phone;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrductName() {
        return productName;
    }

    public void setPrductName(String prductName) {
        this.productName = prductName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }
}
