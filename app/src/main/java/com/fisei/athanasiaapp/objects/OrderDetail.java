package com.fisei.athanasiaapp.objects;

public class OrderDetail {
    public int ID;
    public String Name;
    public int Quantity;
    public double UnitPrice;
    public String ImageURL;

    public OrderDetail(int id, String name, int qty, double uP, String imageURL){
        this.ID = id;
        this.Name = name;
        this.Quantity = qty;
        this.UnitPrice = uP;
        this.ImageURL = imageURL;
    }
}
