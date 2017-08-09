package com.rdhomeseeker.dharam.rdhomeseeker.Activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;



public class Update_User_Profile_Activity extends AppCompatActivity implements View.OnClickListener {
    EditText edt_profile_name;
    EditText edt_profile_phone_no;
    EditText edt_profile_cntry;
    EditText edt_profile_email;
    EditText edt_profile_city;
    Button btn_profile_update;
    ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener fbauthlistener;
    String name, email, phone,city, country;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update__user__profile_);

        mAuth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference();

        edt_profile_name = (EditText) findViewById(R.id.edt_profile_name);
        edt_profile_email = (EditText) findViewById(R.id.edt_profile_email);
        edt_profile_cntry = (EditText) findViewById(R.id.edt_profile_cntry);
        edt_profile_phone_no = (EditText) findViewById(R.id.edt_profile_phone_no);
        edt_profile_city = (EditText)findViewById(R.id.edt_profile_city);
        btn_profile_update = (Button) findViewById(R.id.btn_profile_update);


        btn_profile_update.setOnClickListener(this);


    }

    @Override
    protected void onStart() {
        super.onStart();
        Query query = databaseReference.child("user").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                Toast.makeText(UserProfileEditActivity.this, ""+dataSnapshot.getKey(), Toast.LENGTH_SHORT).show();
                Iterator iterator = dataSnapshot.getChildren().iterator();
                while (iterator.hasNext()) {
                    DataSnapshot dataSnapshot1 = (DataSnapshot) iterator.next();
                    if (dataSnapshot1.getKey().equals("name")) {
                        edt_profile_name.setText(dataSnapshot1.getValue().toString());
                    } else if (dataSnapshot1.getKey().equals("contact")) {
                        edt_profile_phone_no.setText(dataSnapshot1.getValue().toString());
                    } else if (dataSnapshot1.getKey().equals("country")) {
                        edt_profile_cntry.setText(dataSnapshot1.getValue().toString());
                    } else if (dataSnapshot1.getKey().equals("email")) {
                        edt_profile_email.setText(dataSnapshot1.getValue().toString());
                    }
                    else if (dataSnapshot1.getKey().equals("city")) {
                        edt_profile_city.setText(dataSnapshot1.getValue().toString());
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
        edt_profile_email.setEnabled(false);
        //edt_profile_phone_no.setEnabled(false);

    }

    @Override
    public void onClick(View view) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating");
        progressDialog.show();
        name = edt_profile_name.getText().toString().trim();
        phone = edt_profile_phone_no.getText().toString().trim();
        country = edt_profile_cntry.getText().toString().trim();
        city = edt_profile_city.getText().toString().trim();
        switch (view.getId()) {
            case R.id.btn_profile_update:
                databaseReference = FirebaseDatabase.getInstance().getReference().child("user").child(mAuth.getCurrentUser().getUid());
                if (!name.equals("")) {
                    databaseReference.child("name").setValue(name);
                } if (!phone.equals("")) {
                databaseReference.child("contact").setValue(phone);
            } if (!country.equals("")) {
                databaseReference.child("country").setValue(country);
            }
                if (!city.equals("")) {
                    databaseReference.child("city").setValue(city);
                }

                progressDialog.dismiss();
                Toast.makeText(this,"successful", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }
}

