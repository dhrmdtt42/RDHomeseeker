package com.rdhomeseeker.dharam.rdhomeseeker.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rdhomeseeker.dharam.rdhomeseeker.Activity.Home_Activity;

import java.util.Arrays;


public class Login_Activity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    private static final int RC_SIGN_IN = 9001;
    private static final int FB_SIGN_IN = 64206;

    EditText edt_name;
    EditText edt_pswrd;
    Button btn_login;
    Button btn_signUp;
    TextView tv;
    Button login_button;
    FirebaseAuth mAuth;
    Button sign_in_button;
    String login_user, login_password;
    CallbackManager callbackManager;
    FirebaseAuth.AuthStateListener mAuthListener;
    GoogleApiClient mGoogleApiClient;
    GoogleSignInOptions gso;
    static int f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        //callbackManager = CallbackManager.Factory.create();
        callbackManager = CallbackManager.Factory.create();
        if (AccessToken.getCurrentAccessToken() != null){
            LoginManager.getInstance().logOut();
        }

        setContentView(R.layout.activity_login_);
        mAuth = FirebaseAuth.getInstance();



        edt_name = (EditText) findViewById(R.id.edt_name);
        edt_pswrd = (EditText) findViewById(R.id.edt_pswrd);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_signUp = (Button) findViewById(R.id.btn_signUp);
        tv = (TextView) findViewById(R.id.tv);
        login_button = (Button) findViewById(R.id.login_button);
        sign_in_button = (Button) findViewById(R.id.sign_in_button);


        btn_signUp.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        login_button.setOnClickListener(this);
        sign_in_button.setOnClickListener(this);


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("fbbbbbb", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("fbbbbb", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

         gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

         mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }




    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        } else if (requestCode == FB_SIGN_IN) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }


    }


    @Override
    public void onClick(View view) {
        login_user = edt_name.getText().toString().trim();
        login_password = edt_pswrd.getText().toString().trim();
        switch (view.getId()) {

            case R.id.btn_login:
                if (login_user.equals("")) {
                    edt_name.setError("empty field");
                } else if (login_password.equals("")) {
                    edt_pswrd.setError("empty field");
                } else {
                    mAuth.signInWithEmailAndPassword(login_user, login_password)
                            .addOnCompleteListener(Login_Activity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.d("", "signInWithEmail:onComplete:" + task.isSuccessful());

                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.
                                    if (!task.isSuccessful()) {
                                        Log.w("", "signInWithEmail:failed", task.getException());
                                        Toast.makeText(Login_Activity.this, "Wrong Credential",
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        startActivity(new Intent(Login_Activity.this, Home_Activity.class));
                                    }

                                    // ...
                                }
                            });

                }


                break;
            case R.id.btn_signUp:
                Toast.makeText(this, "Signup", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Login_Activity.this, SignUpActivity.class));
                break;
            case R.id.tv:
                Toast.makeText(this, "forgot Password", Toast.LENGTH_SHORT).show();
                break;
            case R.id.login_button:
                facebook_login();
                Toast.makeText(this, "FB Login", Toast.LENGTH_SHORT).show();
                break;
            case R.id.sign_in_button:
                signIn();
                Toast.makeText(this, "Google Login", Toast.LENGTH_SHORT).show();
                break;
        }
    }


// facebook integration start from here...............


    public void facebook_login() {

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends", "email"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(Login_Activity.this, "User Cancel", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(Login_Activity.this, "Error", Toast.LENGTH_LONG).show();
            }
        });

//        LoginManager.getInstance().registerCallback(, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                handleFacebookAccessToken(loginResult.getAccessToken());
//            }
//
//            @Override
//            public void onCancel() {
//                // progressDialog.dismiss();
//                Toast.makeText(Login_Activity.this, "User Cancel", Toast.LENGTH_LONG).show();
//                //rlayout_parent.setVisibility(View.VISIBLE);
//
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//                // progressDialog.dismiss();
//                Log.d("error:", error.toString());
//                Toast.makeText(Login_Activity.this, "Something Wrong", Toast.LENGTH_LONG).show();
//
//            }
//        });
    }


    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("fbbbbb", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("fbbbbb", "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("fbbbbb", "signInWithCredential", task.getException());
                            Toast.makeText(Login_Activity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //progressDialog.dismiss();
                        } else {
                            // progressDialog.show();
                            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                            final DatabaseReference databaseReference = firebaseDatabase.getReference().child("user").child(uid);
                            databaseReference.child("name").setValue(FirebaseAuth.getInstance().getCurrentUser().getDisplayName()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        databaseReference.child("email").setValue(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                                        databaseReference.child("name").setValue("");
                                        databaseReference.child("country").setValue("");
                                        Toast.makeText(Login_Activity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(Login_Activity.this, Home_Activity.class));
                                    } else {
                                        //progressDialog.dismiss();
                                        Toast.makeText(Login_Activity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }


                    }
                });

    }



    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


// google sign in start from here............

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        Log.d("Google", "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        // [END_EXCLUDE]
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        Log.d("Google", "signInWithCredential:onComplete:" + task.isSuccessful());
                        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        final DatabaseReference databaseReference = firebaseDatabase.getReference().child("user").child(uid);

                        databaseReference.child("name").setValue(FirebaseAuth.getInstance().getCurrentUser().getDisplayName()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {

                                    databaseReference.child("city").setValue("null");
                                    databaseReference.child("country").setValue("null");
                                    databaseReference.child("email").setValue(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                                    task.getResult();
                                    Toast.makeText(Login_Activity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Login_Activity.this, Home_Activity.class));
                                } else {
                                    //progressDialog.dismiss();
                                    Toast.makeText(Login_Activity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("Google", "signInWithCredential", task.getException());
                            Toast.makeText(Login_Activity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // [START_EXCLUDE]
//                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });

                            }
    }




