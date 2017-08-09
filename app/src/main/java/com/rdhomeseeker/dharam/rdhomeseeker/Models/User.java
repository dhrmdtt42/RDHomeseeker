package com.rdhomeseeker.dharam.rdhomeseeker.Models;


public class User {



    String mobno = "";

    public String name = "";
    public String email = "";
    public String contact = "";
    public String uid = "";
    public String picture_url = "";

    public User(){

    }
   public User(String name,String email,String mobno,String uid,String picture_url){
        this.name = name;
        this.email = email;
        this.mobno = mobno;
        this.uid = uid;
       this.picture_url = picture_url;
    }
}
