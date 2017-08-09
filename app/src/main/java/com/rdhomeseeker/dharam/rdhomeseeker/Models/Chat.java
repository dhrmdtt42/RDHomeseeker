package com.rdhomeseeker.dharam.rdhomeseeker.Models;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dharam on 1/13/2017.
 */

public class Chat {
    public String author_uid="";
    public String message="";
    public  String timestamp="";
    public String photoUrl;
    public String name;
    public boolean Ismine =false;

    public Chat(){

    }
    public Chat(String author_uid , String message, String timestamp,String name,String photoUrl){
        this.author_uid= author_uid;
        this.message=message;
        this.timestamp =timestamp;
        this.name =name;
        this.photoUrl =photoUrl;
    }
    @Exclude
    public Map<String,Object>tomap(){
        HashMap<String,Object>result= new HashMap<String, Object>();
        result.put("message",message);
        result.put("author",author_uid);
        result.put("timestamp",timestamp);
        result.put("ismine",Ismine);
        return result;
    }
}
