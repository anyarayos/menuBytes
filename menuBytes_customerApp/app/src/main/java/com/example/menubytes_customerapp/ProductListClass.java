package com.example.menubytes_customerapp;

public class ProductListClass {
    int id;
    String Image;
    String Name;
    String Price;
    String Description;
    String productBundle;

    public ProductListClass(int id, String image, String name, String price, String des, String productBundle) {
        this.id = id;
        Image = image;
        Name = name;
        Price = price;
        Description = des;
        this.productBundle = productBundle;
    }

    public ProductListClass(String image, String name, String price, String des, String productBundle) {
        Image = image;
        Name = name;
        Price = price;
        Description = des;
        this.productBundle = productBundle;
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
