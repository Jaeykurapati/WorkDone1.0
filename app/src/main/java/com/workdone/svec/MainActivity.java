package com.workdone.svec;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private Button buttonSignIn;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView Signup;
    private TextView reset;

    //firebase auth object
    private FirebaseAuth firebaseAuth;

    //progress dialog
    private ProgressDialog progressDialog;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getting firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();

        //if the objects getcurrentuser method is not null
        //means user is already logged in
        if (firebaseAuth.getCurrentUser() != null) {
            //close this activity
            finish();
            //opening profile activity
            startActivity(new Intent(getApplicationContext(), Dashboard.class));
        }

        //initializing views
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.pwd);
        buttonSignIn = (Button) findViewById(R.id.login);
        Signup = (TextView) findViewById(R.id.signup);
        progressDialog = new ProgressDialog(this);
        reset = (TextView) findViewById(R.id.reset);
        editTextPassword.setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event){
                if(keyCode == event.KEYCODE_ENTER){
                    String email = editTextEmail.getText().toString().trim();
                    final String password = editTextPassword.getText().toString().trim();

                    //checking if email and passwords are empty
                    if (TextUtils.isEmpty(email)) {

                        editTextEmail.setError("Enter email");
                        progressDialog.dismiss();
                        return false;
                    }
                    String emailPattern = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
                    Pattern p = Pattern.compile(emailPattern);
                    Matcher m = p.matcher(email);
                    if(m.matches()==false){
                        editTextEmail.setError("Enter valid email");
                        progressDialog.dismiss();
                    }
                    if (TextUtils.isEmpty(password)) {
                        editTextPassword.setError("Enter Password");
                        progressDialog.dismiss();
                        return false;
                    }

                    //if the email and password are not empty
                    //displaying a progress dialog
                    progressDialog.setMessage("Please Wait...");
                    progressDialog.show();
                    //logging in the user
                    firebaseAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.
                                    if (!task.isSuccessful()) {
                                        // there was an error
                                        progressDialog.dismiss();
                                        if (password.length() < 6) {
                                            editTextPassword.setError(getString(R.string.minimum_password));
                                        } else {
                                            Toast.makeText(MainActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        progressDialog.setMessage("Logging Please Wait...");
                                        progressDialog.show();
                                        Intent intent = new Intent(MainActivity.this, Dashboard.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                }
                return false;
            }
        });
        //attaching click listener
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                String email = editTextEmail.getText().toString().trim();
                                                final String password = editTextPassword.getText().toString().trim();


                                                //checking if email and passwords are empty
                                                if (TextUtils.isEmpty(email)) {

                                                    editTextEmail.setError("Enter email");
                                                    progressDialog.dismiss();
                                                    return;
                                                }
                                                String emailPattern = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
                                                Pattern p = Pattern.compile(emailPattern);
                                                Matcher m = p.matcher(email);
                                                if(m.matches()==false){
                                                    editTextEmail.setError("Enter valid email");
                                                    progressDialog.dismiss();
                                                }
                                                if (TextUtils.isEmpty(password)) {
                                                    editTextPassword.setError("Enter Password");
                                                    progressDialog.dismiss();
                                                    return;
                                                }

                                                //if the email and password are not empty
                                                //displaying a progress dialog
                                                progressDialog.setMessage("Please Wait...");
                                                progressDialog.show();
                                                //logging in the user
                                                firebaseAuth.signInWithEmailAndPassword(email, password)
                                                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                                // If sign in fails, display a message to the user. If sign in succeeds
                                                                // the auth state listener will be notified and logic to handle the
                                                                // signed in user can be handled in the listener.
                                                                if (!task.isSuccessful()) {
                                                                    // there was an error
                                                                    if (password.length() < 6) {
                                                                        editTextPassword.setError("Inavlid Details");
                                                                        progressDialog.dismiss();
                                                                    } else {
                                                                        Toast.makeText(MainActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                                                        progressDialog.dismiss();
                                                                    }
                                                                } else {
                                                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                                    if (user.isEmailVerified())
                                                                    {
                                                                        // user is verified, so you can finish this activity or send user to activity which you want.
                                                                        Intent intent = new Intent(MainActivity.this, Dashboard.class);
                                                                        startActivity(intent);
                                                                        finish();
                                                                        Toast.makeText(MainActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
                                                                        progressDialog.dismiss();
                                                                    }
                                                                    else
                                                                    {
                                                                        // email is not verified, so just prompt the message to the user and restart this activity.
                                                                        // NOTE: don't forget to log out the user.
                                                                        Toast.makeText(MainActivity.this, "Please verify your Email", Toast.LENGTH_SHORT).show();
                                                                        progressDialog.dismiss();
                                                                        FirebaseAuth.getInstance().signOut();

                                                                        //restart this activity

                                                                    }

                                                                }
                                                            }
                                                        });
                                            }
                                        });
        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegActivity.class));
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ResetActivity.class));
            }
        });
    }
    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK);
            FragmentManager fragmentManager = getSupportFragmentManager();
            //this will clear the back stack and displays no animation on the screen
            fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            finishAffinity();
            finish();
            System.exit(0);
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
    }