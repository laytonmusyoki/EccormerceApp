package com.example.ecormmerce;

import android.widget.ImageView;

public class ProductModel {
    String categories, name, description, price, digital;
    int image;

    public ProductModel(String categories, String name, String description, String price, String digital, int image) {
        this.categories = categories;
        this.name = name;
        this.description = description;
        this.price = price;
        this.digital = digital;
        this.image = image;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDigital() {
        return digital;
    }

    public void setDigital(String digital) {
        this.digital = digital;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}