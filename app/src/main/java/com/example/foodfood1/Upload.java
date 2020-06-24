package com.example.foodfood1;

public class Upload {
    private String name,price;
    private String mImageUri;
    public Upload(){//Empty constructor needed
        }
    public Upload(String name,String price,String mImageUri){
        this.name=name;
        this.price=price;
        this.mImageUri=mImageUri;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getmImageUri() {
        return mImageUri;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setmImageUri(String mImageUri) {
        this.mImageUri = mImageUri;
    }
}
