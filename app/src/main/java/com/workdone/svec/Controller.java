package com.workdone.svec;

import android.app.Application;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Controller extends Application{

    private  ArrayList<User> myProducts = new ArrayList<User>();
    private Set<User> set = new HashSet<User>();
    private  Products myCart = new Products();


    public User getProducts(int pPosition) {
        return myProducts.get(pPosition);
    }

    public void setProducts(User Products) {

        myProducts.add(Products);
    commit();
    }

    public void removeProducts(User Products) {

        myProducts.remove(Products);
    }
    public void reset(){
        myProducts.clear();
    }
    public boolean find(User Products) {

        if(myProducts.contains(Products))
            return true;
        else
            return false;

    }
    public Products getCart() {

        return myCart;

    }
    public ArrayList<User> getItems() {

        return myProducts;

    }

    public int getProductsArraylistSize() {

        return myProducts.size();
    }
    public void commit(){
        set.clear();
        set.addAll(myProducts);
        myProducts.clear();
        myProducts.addAll(set);
    }

}
