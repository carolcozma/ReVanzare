package com.project.second_hand_ecommerce_backend.api.model;

import com.project.second_hand_ecommerce_backend.model.LocalUser;
import org.springframework.cglib.core.Local;

import java.sql.Blob;
import java.time.LocalDateTime;
import java.util.Date;

public class ProductBody {
    private Date date;
    private String description;
    private String name;
    private float price;
    private LocalUser userID;

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    private String imagePath;

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setUserID(LocalUser userID) {
        this.userID = userID;
    }

    public Date getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public float getPrice() {
        return price;
    }

    public LocalUser getUserID() {
        return userID;
    }
}
