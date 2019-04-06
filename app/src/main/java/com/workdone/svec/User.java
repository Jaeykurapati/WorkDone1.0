package com.workdone.svec;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class User  {
    public String username="";
    public String email="";
    public String gender="";
    public String dob="";
    public List<String> categories=new ArrayList<String>();
    public String address="";
    public Long exp;
    public String no;
    public boolean flag=true;
    public boolean isProfile=false;
    public Long amount;
    public String location="";
    private DatabaseReference mDatabase=FirebaseDatabase.getInstance().getReference();

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
    public String getNo(){
            return no;
    }

    public User(String username, String gender, String date, List<String> cate, String adr,String no ,long exp, String location,  String email){
        this.username=username;
        this.gender=gender;
        this.dob=date;
        this.categories=cate;
        this.address=adr;
        this.no=no;
        this.email=email;
        this.exp=new Long(exp);
        //this.amount=new Long(amount);
        this.location=location;
    }

    public Long getAmount() {
        return amount;
    }

    public User(){

    }

    public String getLocation() {
        return location;
    }

    public String getAddress() {
        return address;
    }

    public Long getExp() {
        return exp;
    }

    public boolean writeUser(String userId, String username, String gender, String date, List<String> cate, String adr, String no, long exp, String location,  String email){
        User user=new User(username,gender,date,cate,adr,no,exp,location,email);
        user.isProfile=true;
        mDatabase.child("Users").child(userId).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Write was successful!
                // ...
                flag=true;
                isProfile=true;
            }
        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        // ...
                        flag=false;
                    }
                });
        return flag;
    }
}
