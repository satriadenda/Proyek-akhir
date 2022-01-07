package com.udinus.newsahkesport.model;

public class Product {

    private String id;
    private int price, rating;
    private String name, description, imgUrl;

    public Product(String id, String name, String description, String imgUrl, int price, int rating){
        this.id = id;
        this.name = name;
        this.description = description;
        this.imgUrl = imgUrl;
        this.price = price;
        this.rating = rating;
    }

    public String getId(){ return id; }

    public String getName(){ return name;}

    public String getDescription() {
        return description;
    }

    public int getPrice(){
        return price;
    }

    public int getRating(){
        return rating;
    }

    public String getImgUrl(){
        return imgUrl;
    }
}
