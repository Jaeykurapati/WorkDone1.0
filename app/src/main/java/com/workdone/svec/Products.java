package com.workdone.svec;

import java.util.ArrayList;

public class Products {
    private ArrayList<User> cartProducts = new ArrayList<User>();


    public User getProducts(int pPosition) {

        return cartProducts.get(pPosition);
    }
    public ArrayList<User> getAll() {

        return cartProducts;
    }
    public void setProducts(User Products) {

        cartProducts.add(Products);

    }

    public int getCartSize() {

        return cartProducts.size();

    }

    public boolean checkProductInCart(User aProduct) {

        return cartProducts.contains(aProduct);

    }
}
