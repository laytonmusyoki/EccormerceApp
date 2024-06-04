package com.example.ecormmerce;

import java.util.List;

public class CartList {

    String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    List<CartModel> cartProducts;

    public List<CartModel> getCartProducts() {
        return cartProducts;
    }

    public void setCartProducts(List<CartModel> cartProducts) {
        this.cartProducts = cartProducts;
    }
}
