package com.example.minus_1_update;

import android.content.Context;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.List;


public class Product implements Serializable {

    @Exclude private String id;
    String image, name, newPrice;
    int oldPrice;
    List<String> tags;
    List<String> quantity1;
    List<String> quantity2;

    public Product() {
    }

    public Product(List<String> tags, String image, String name, String newPrice, int oldPrice, List<String> quantity1, List<String> quantity2) {
        this.image = image;
        this.tags = tags;
        this.name = name;
        this.newPrice = newPrice;
        this.oldPrice = oldPrice;
        this.quantity1 = quantity1;
        this.quantity2 = quantity2;


    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getNewPrice() {
        return newPrice;
    }

    public int getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(int oldPrice) {
        this.oldPrice = oldPrice;
    }

    public List<String> getQuantity1() {
        return quantity1;
    }

    public void setQuantity1(List<String> quantity1) {
        this.quantity1 = quantity1;
    }

    public List<String> getQuantity2() {
        return quantity2;
    }

    public void setQuantity2(List<String> quantity2) {
        this.quantity2 = quantity2;
    }
}
