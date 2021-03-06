package com.workdone.svec;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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

public class RegActivity extends AppCompatActivity{
    private EditText textemail;
    private EditText pass;
    private EditText pass1;
    private Button buttonsignup;
    private TextView textlogin;

    //firebase auth object
    private FirebaseAuth firebaseAuth;

    //progress dialog
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        //getting firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();

        //if the objects getcurrentuser method is not null
        //means user is already logged in
        if(firebaseAuth.getCurrentUser() != null){
            //close this activity
            finish();
            //opening profile activity
            startActivity(new Intent(getApplicationContext(), Dashboard.class));
        }

        //initializing views
        textemail = (EditText) findViewById(R.id.email);
        pass = (EditText) findViewById(R.id.pwd);
        pass1 = (EditText) findViewById(R.id.pwd1);
        textlogin = (TextView) findViewById(R.id.login);
        buttonsignup = (Button) findViewById(R.id.reg);
        progressDialog = new ProgressDialog(this);
        //attaching click listener
        buttonsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = textemail.getText().toString().trim();
                String password = pass.getText().toString().trim();
                String password1 = pass1.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    textemail.setError("Enter Email");
                    progressDialog.dismiss();
                    return;
                }
                String emailPattern = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
                Pattern p = Pattern.compile(emailPattern);
                Matcher m = p.matcher(email);
                if(m.matches()==false){
                    textemail.setError("Enter valid email");
                    progressDialog.dismiss();
                }
                if(TextUtils.equals(password,password1)==false){
                    pass.setError("Password didn`t match");
                    progressDialog.dismiss();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    pass1.setError("Enter Password");
                    progressDialog.dismiss();
                    return;
                }

                if (password.length() < 6) {
                    pass.setError("Min 6 characters");
                    progressDialog.dismiss();
                    return;
                }

                progressDialog.setMessage("Registering Please Wait...");
                progressDialog.show();
                //create user
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(RegActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(RegActivity.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                } else {
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    user.sendEmailVerification()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        // email sent
                                                        progressDialog.dismiss();
                                                        Toast.makeText(RegActivity.this, "Please verify your Email", Toast.LENGTH_SHORT).show();
                                                        // after email is sent just logout the user and finish this activity
                                                        FirebaseAuth.getInstance().signOut();
                                                        startActivity(new Intent(RegActivity.this, MainActivity.class));
                                                        finish();
                                                    }
                                                    else
                                                    {
                                                        progressDialog.dismiss();
                                                        // email not sent, so display message and restart the activity or do whatever you wish to do
                                                        textemail.setError("Invalid Email");//restart this activity
                                                        startActivity(new Intent(RegActivity.this, Dashboard.class));
                                                        finish();

                                                    }
                                                }
                                            });
                                }
                            }
                        });

            }
        });
        textlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
