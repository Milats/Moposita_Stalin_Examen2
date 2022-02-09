package com.fisei.athanasiaapp.objects;

public class ShopCartItem {
    public int Id;
    public String Name;
    public String ImageURL;
    public int Quantity;
    public double UnitPrice;
    public int MaxQty;

    public ShopCartItem(int id, String name, String icon, int qty, double unitPrice, int maxQty){
        this.Id = id;
        this.Name = name;
        this.ImageURL = icon;
        this.Quantity = qty;
        this.UnitPrice = unitPrice;
        this.MaxQty = maxQty;
    }

    public ShopCartItem(int id, int qty){
        this.Id = id;
        this.Quantity = qty;
    }
}