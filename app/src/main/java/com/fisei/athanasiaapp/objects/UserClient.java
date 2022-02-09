package com.fisei.athanasiaapp.objects;

public class UserClient {
    public int ID;
    public String Name;
    public String Email;
    public String Cedula;
    public String JWT;

    public UserClient(int id, String name, String email, String cedula, String jwt){
        this.ID = id;
        this.Name = name;
        this.Email = email;
        this.Cedula = cedula;
        this.JWT = jwt;
    }
    public UserClient(){
    }
}