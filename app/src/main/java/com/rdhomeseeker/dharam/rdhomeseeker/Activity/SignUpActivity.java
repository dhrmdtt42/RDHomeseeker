package com.rdhomeseeker.dharam.rdhomeseeker.Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener
{
     EditText sign_up_edt_name;
     EditText sign_up_edt_email;
    EditText sign_up_edt_Cpassword;
  EditText sign_up_edt_password;
     EditText sign_up_edt_city;
     EditText sign_up_edt_cntry;
   FirebaseAuth mAuth;
    Button btn_sbmt;
    private FirebaseAuth.AuthStateListener mAuthListener;
    //public static final TAG="ankit";
    String name,email, pswrd,cpswrd,city,country;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        //view
        sign_up_edt_name = (EditText) findViewById(R.id.sign_up_edt_name);
        sign_up_edt_email=(EditText)findViewById(R.id.sign_up_edt_email);
        sign_up_edt_password = (EditText) findViewById(R.id.sign_up_edt_password);
        sign_up_edt_Cpassword = (EditText) findViewById(R.id.sign_up_edt_Cpassword);
        sign_up_edt_city =(EditText)findViewById(R.id.sign_up_edt_city);
        sign_up_edt_cntry =(EditText)findViewById(R.id.sign_up_edt_cntry);
        //Button
        btn_sbmt = (Button) findViewById(R.id.btn_sbmt);

        //clickable

        btn_sbmt.setOnClickListener(this);
        //start initialize mAuth



        //start AuthListener

        mAuthListener = new FirebaseAuth.AuthStateListener()
        {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        // ...
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }




    @Override
    public void onClick(View v) {
        name=sign_up_edt_name.getText().toString().trim();
        email=sign_up_edt_email.getText().toString().trim();
        pswrd=sign_up_edt_password.getText().toString().trim();
        cpswrd=sign_up_edt_Cpassword.getText().toString().trim();
        city = sign_up_edt_city.getText().toString().trim();
        country = sign_up_edt_cntry.getText().toString().trim();
        switch (v.getId()){

            case R.id.btn_sbmt:
                if(name.equals("")){
                    sign_up_edt_name.setError("empty field");
                }
                else if(email.equals(""))
                {
                    sign_up_edt_email.setError("empty field");
                }
                else if(city.equals(""))
                {
                    sign_up_edt_city.setError("empty field");
                }
                else if(country.equals(""))
                {
                    sign_up_edt_cntry.setError("empty field");
                }
                else if(pswrd.equals(""))
                {
                    sign_up_edt_password.setError("empty field");
                }
                else if(!pswrd.equals(cpswrd))
                {

                    //Toast.makeText(this, "congratulation you r registerd!!!!!!!!!! :) :) :)", Toast.LENGTH_SHORT).show();
                    sign_up_edt_Cpassword.setError(" you r not registerd!!!");
                }

                else {
                    CreateUser(email,pswrd);
//                    Toast.makeText(this, "you r registered", Toast.LENGTH_SHORT).show();
                }
                break;


        }


    }
    private void CreateUser(final String email, String pswrd){

        mAuth.createUserWithEmailAndPassword(email,pswrd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    Toast.makeText(SignUpActivity.this, "SignUp Successfull", Toast.LENGTH_LONG).show();
                    saveUserDataToDatabase(task.getResult().getUser(),name,city,country,email);
                }
                else{
                    Toast.makeText(SignUpActivity.this,"SignUp UnSuccessfull",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void saveUserDataToDatabase(FirebaseUser firebaseUser,final String name, final String city, final String country, final String email){
        String mail=firebaseUser.getEmail();
        String uid =firebaseUser.getUid();
        FirebaseDatabase firebaseDatabse = FirebaseDatabase.getInstance();
        final DatabaseReference databaseRefence = firebaseDatabse.getReference().child("user").child(uid);
        databaseRefence.child("email").setValue(mail).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    databaseRefence.child("name").setValue(name);
                    // databaseRefence.child("email").child(email);
                    databaseRefence.child("city").setValue(city);
                    databaseRefence.child("country").setValue(country);

                    Toast.makeText(SignUpActivity.this,"data inserted",Toast.LENGTH_LONG);
                }
                else
                {
                    Toast.makeText(SignUpActivity.this,"data not Inserted",Toast.LENGTH_LONG);
                }

            }
        });

    }


}

