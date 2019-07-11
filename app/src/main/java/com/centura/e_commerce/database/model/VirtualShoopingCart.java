package com.centura.e_commerce.database.model;

public class VirtualShoopingCart {
    public static final String TABLE_NAME="product_details";
    public static final String USER_ID = "id";
    public static final String PRODUCT = "product";

    private String id;
    private String product;

    //CREATE TABLE
    public static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+"("
            + USER_ID + " TEXT,"
            +PRODUCT+" TEXT "
            +")";
    public VirtualShoopingCart(){

    }
    public VirtualShoopingCart(String id,String product){
        this.id=id;
        this.product=product;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }
}
