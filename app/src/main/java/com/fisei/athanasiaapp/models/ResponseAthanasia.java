package com.fisei.athanasiaapp.models;

public class ResponseAthanasia {
    public boolean Success;
    public String Message;

    public ResponseAthanasia(Boolean success, String message){
        this.Success = success;
        this.Message = message;
    }
}