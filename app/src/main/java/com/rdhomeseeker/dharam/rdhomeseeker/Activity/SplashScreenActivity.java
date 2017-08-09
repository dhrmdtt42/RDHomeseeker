package com.rdhomeseeker.dharam.rdhomeseeker.Activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class SplashScreenActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Intent i = new Intent(SplashScreenActivity.this, Login_Activity.class);

        startActivity(i);




// btn_rb = (Button) findViewById(R.id.btn_rb);

//    try
//
//    {
//        PackageInfo info = getPackageManager().getPackageInfo("com.rdhomeseeker.dharam.rdhomeseeker", PackageManager.GET_SIGNATURES);
//        for (Signature signature : info.signatures) {
//            MessageDigest md = MessageDigest.getInstance("SHA");
//            md.update(signature.toByteArray());
//            Log.d("keyhash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//        }
//    }
//
//    catch(
//    PackageManager.NameNotFoundException e
//    )
//
//    {
//
//    }
//
//    catch(NoSuchAlgorithmException e)
//
//    {
//
//    }
}

}