package com.fisei.athanasiaapp.objects;

public class Product {
    public final int id;
    public final String name;
    public final String genre;
    public final int quantity;
    public final double unitPrice;
    public final double cost;
    public final String imageURL;

    public Product(int id, String name, String genre, int quantity,
                   double unitPrice, double cost, String iconUrl){
        this.id = id;
        this.name = name;
        this.genre = genre;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.cost = cost;
        this.imageURL = iconUrl;
    }
}