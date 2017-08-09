package com.rdhomeseeker.dharam.rdhomeseeker.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rdhomeseeker.dharam.rdhomeseeker.Activity.Update_User_Profile_Activity;
import com.rdhomeseeker.dharam.rdhomeseeker.Adapter.PagerAdapter;
import com.rdhomeseeker.dharam.rdhomeseeker.Utils.AppController;

import java.util.Iterator;

public class Home_Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {

    NetworkImageView img_logged_user_dp;
    NetworkImageView top_network_imageview;
    TextView edt_loged_user_name;
    TextView edt_logged_user_email;
    String providerId;
    FirebaseUser firebaseUser;
    GoogleApiClient mGoogleApiClient;
    FirebaseAuth fbAuth;
    DatabaseReference databaseReference;
    ImageLoader imageLoader;
    TabLayout tab_layout;
    ViewPager viewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imageLoader = AppController.getInstance().getImageLoader();

        fbAuth = FirebaseAuth.getInstance();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        View navHeaderView = navigationView.inflateHeaderView(R.layout.nav_header_home_);
        img_logged_user_dp = (NetworkImageView) navHeaderView.findViewById(R.id.img_logged_user_dp);
        edt_loged_user_name = (TextView)navHeaderView.findViewById(R.id.edt_loged_user_name);
        edt_logged_user_email = (TextView)navHeaderView.findViewById(R.id.edt_loged_user_email);


        top_network_imageview = (NetworkImageView)findViewById(R.id.top_network_imageview);
        tab_layout = (TabLayout)findViewById(R.id.tab_layout);
        viewpager = (ViewPager)findViewById(R.id .viewpager);
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewpager.setAdapter(pagerAdapter);
        tab_layout.setupWithViewPager(viewpager);




        if(firebaseUser != null){
            for(UserInfo userInfo : firebaseUser.getProviderData()){
                if(userInfo.getProviderId() != null){
                    providerId = userInfo.getProviderId();
                }
                if(userInfo.getPhotoUrl() !=null){
                    img_logged_user_dp.setImageUrl(userInfo.getPhotoUrl().toString(), imageLoader);
                    top_network_imageview.setImageUrl(userInfo.getPhotoUrl().toString(),imageLoader);
                    databaseReference.child("user").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("picture_url").setValue(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString());
                }else {
                    img_logged_user_dp.setDefaultImageResId(R.drawable.qas);
                    top_network_imageview.setDefaultImageResId(R.drawable.qas);
                }
            }
        }


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.profile_update) {
            startActivity(new Intent(this,Update_User_Profile_Activity.class));
        } else if(id== R.id.nav_SignOut){
            signout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        Query query = databaseReference.child("user").child(fbAuth.getCurrentUser().getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator iterator = dataSnapshot.getChildren().iterator();
                while (iterator.hasNext()){
                    DataSnapshot dataSnapshot1 = (DataSnapshot) iterator.next();
                    if(dataSnapshot1.getKey().equals("name")){
                        edt_loged_user_name.setText(dataSnapshot1.getValue().toString());
                    }else if(dataSnapshot1.getKey().equals("email")){
                        edt_logged_user_email.setText(dataSnapshot1.getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void signout()
    {
        final Intent i = new Intent(this,Login_Activity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        if(providerId.equals("google.com")){
            fbAuth.signOut();
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    startActivity(i);
                    finish();
                }
            });

        }else if(providerId.equals("facebook.com") && AccessToken.getCurrentAccessToken() != null){
            fbAuth.signOut();
            LoginManager.getInstance().logOut();
            startActivity(i);
            finish();
        }else {
            fbAuth.signOut();
            startActivity(i);
            finish();
        }
    }
}
