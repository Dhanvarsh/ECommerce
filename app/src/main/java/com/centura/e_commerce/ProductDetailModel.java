package com.centura.e_commerce;

public class ProductDetailModel {
    String name,mrp,price,weight;
    int image;

    public  ProductDetailModel(String name,String price,String mrp,String weight,int image){
        this.name=name;
        this.price=price;
        this.image=image;
        this.mrp=mrp;
        this.weight=weight;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice() {
        return price;
    }
    public void setImage(int image) {
        this.image = image;
    }

    public int getImage() {
        return image;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}
