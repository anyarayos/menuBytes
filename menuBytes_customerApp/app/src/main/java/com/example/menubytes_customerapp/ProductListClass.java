package com.example.menubytes_customerapp;

public class ProductListClass {
    int id;
    String Image;
    String Name;
    String Price;
    String Description;
    String productBundle;
    byte[] bytes;
    String availability;

    /*By Category*/
    public ProductListClass(int id, String image, String name, String price, String des, String productBundle) {
        this.id = id;
        Image = image;
        Name = name;
        Price = price;
        Description = des;
        this.productBundle = productBundle;
    }

    public ProductListClass(int id, byte[] bytes, String name, String price, String des, String productBundle, String availability) {
        this.id = id;
        this.bytes = bytes;
        Name = name;
        Price = price;
        Description = des;
        this.productBundle = productBundle;
        this.availability = availability;
    }

    /*By ID*/
    public ProductListClass(String image, String name, String price, String des, String productBundle) {
        Image = image;
        Name = name;
        Price = price;
        Description = des;
        this.productBundle = productBundle;
    }

    public ProductListClass(byte[] bytes, String name, String price, String des, String productBundle) {
        this.bytes = bytes;
        Name = name;
        Price = price;
        Description = des;
        this.productBundle = productBundle;
    }

    public String getAvailability() {
        return availability;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductBundle() {
        return productBundle;
    }

    public void setProductBundle(String productBundle) {
        this.productBundle = productBundle;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String priceShawarma) {
        Price = priceShawarma;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
