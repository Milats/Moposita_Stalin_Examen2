package com.fisei.athanasiaapp.models;

import java.util.List;

public class SaleRequest {
    public int UserClientID;
    public List<SaleDetails> SaleDetails;

    public SaleRequest(int id, List<SaleDetails> details){
        this.UserClientID = id;
        this.SaleDetails = details;
    }
    public SaleRequest(){}
}