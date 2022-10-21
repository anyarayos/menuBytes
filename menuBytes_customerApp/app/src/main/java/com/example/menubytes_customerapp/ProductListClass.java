package com.example.menubytes_customerapp;

public class ProductListClass {
    int id;
    String ImageShawarma;
    String NameShawarma;
    String PriceShawarma;
    String DesShawarma;
    String productBundle;

    public ProductListClass(int id, String image, String name, String price, String des, String productBundle) {
        this.id = id;
        ImageShawarma = image;
        NameShawarma = name;
        PriceShawarma = price;
        DesShawarma = des;
        this.productBundle = productBundle;
    }

    public ProductListClass(String image, String name, String price, String des, String productBundle) {
        ImageShawarma = image;
        NameShawarma = name;
        PriceShawarma = price;
        DesShawarma = des;
        productBundle = productBundle;
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

    public String getImageShawarma() {
        return ImageShawarma;
    }

    public void setImageShawarma(String imageShawarma) {
        ImageShawarma = imageShawarma;
    }

    public String getNameShawarma() {
        return NameShawarma;
    }

    public void setNameShawarma(String nameShawarma) {
        NameShawarma = nameShawarma;
    }

    public String getPriceShawarma() {
        return PriceShawarma;
    }

    public void setPriceShawarma(String priceShawarma) {
        PriceShawarma = priceShawarma;
    }

    public String getDesShawarma() {
        return DesShawarma;
    }

    public void setDesShawarma(String desShawarma) {
        DesShawarma = desShawarma;
    }
}
