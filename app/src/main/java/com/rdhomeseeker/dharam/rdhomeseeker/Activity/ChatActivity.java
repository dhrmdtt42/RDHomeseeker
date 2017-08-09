package com.rdhomeseeker.dharam.rdhomeseeker.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ChatActivity extends AppCompatActivity {

    String other_uid;
    String name="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();

        if (intent != null){

            other_uid =getIntent().getStringExtra("otherUserId");
            name = getIntent().getStringExtra("name");
        }
        if(name.equals("")){
            setTitle(name);
        }
    }
}
