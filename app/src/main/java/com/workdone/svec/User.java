package com.workdone.svec;

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
    public Long amount;
    public String location="";
    private DatabaseReference mDatabase=FirebaseDatabase.getInstance().getReference();
    public User(){

    }
    public User(String username,String gender,String date, List<String> cate,String adr,long exp,String location,long amount,String email){
        this.username=username;
        this.gender=gender;
        this.dob=date;
        this.categories=cate;
        this.address=adr;
        this.email=email;
        this.exp=new Long(exp);
        this.amount=new Long(amount);
        this.location=location;
    }
    public void writeUser(String userId,String username,String gender,String date, List<String> cate,String adr,long exp,String location, long amount,String email){
        User user=new User(username,gender,date,cate,adr,exp,location,amount,email);
        mDatabase.child("Users").child(userId).setValue(user);

    }
}
