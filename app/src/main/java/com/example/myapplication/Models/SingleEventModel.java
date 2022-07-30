package com.example.myapplication.Models;

public class SingleEventModel {

    private String description;
    private String going;

    public SingleEventModel() {
    }

    public SingleEventModel(String description, String going) {
        this.description = description;
        this.going = going;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGoing() {
        return going;
    }

    public void setGoing(String going) {
        this.going = going;
    }
}
