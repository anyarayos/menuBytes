package com.example.menubytes_customerapp;

public class SqlStatements {
    public String getRetrieveProductsByCategory() {
        return retrieveProductsByCategory;
    }

    public String getRetrieveProductsByID() {
        return retrieveProductsByID;
    }

    private String retrieveProductsByID = "SELECT PRODUCT_IMG, PRODUCT_NAME, PRODUCT_PRICE, " +
            "PRODUCT_DESCRIPTION, PRODUCT_BUNDLE FROM product WHERE PRODUCT_ID = (?)";

    private String retrieveProductsByCategory = "SELECT PRODUCT_ID, PRODUCT_IMG, PRODUCT_NAME, " +
            "PRODUCT_PRICE, PRODUCT_DESCRIPTION, PRODUCT_BUNDLE FROM product WHERE PRODUCT_CATEGORY = 'shawarma'";
}
