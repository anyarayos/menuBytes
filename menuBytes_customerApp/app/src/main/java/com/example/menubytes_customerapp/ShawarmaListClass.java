package com.example.menubytes_customerapp;

public class ShawarmaListClass {
    int ImageShawarma;
    String NameShawarma;
    String PriceShawarma;
    String DesShawarma;

    public ShawarmaListClass(int image, String name, String price, String des) {
        ImageShawarma = image;
        NameShawarma = name;
        PriceShawarma = price;
        DesShawarma = des;
    }

    public int getImageShawarma() {
        return ImageShawarma;
    }

    public void setImageShawarma(int imageShawarma) {
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
