package com.fisei.athanasiaapp.objects;

public class OrderDetail {
    public String Name;
    public int Quantity;
    public double UnitPrice;
    public String ImageURL;

    public OrderDetail(String name, int qty, double uP, String imageURL){
        this.Name = name;
        this.Quantity = qty;
        this.UnitPrice = uP;
        this.ImageURL = imageURL;
    }
}
