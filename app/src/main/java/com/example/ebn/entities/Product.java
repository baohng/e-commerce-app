package com.example.ebn.entities;

public class Product {
    private int resourceImage, resourceStatus;
    String name, cost;

    public Product(int resourceImage, int resourceStatus, String name, String cost) {
        this.resourceImage = resourceImage;
        this.resourceStatus = resourceStatus;
        this.name = name;
        this.cost = cost;
    }

    public int getResourceImage() {
        return resourceImage;
    }

    public void setResourceImage(int resourceImage) {
        this.resourceImage = resourceImage;
    }

    public int getResourceStatus() {
        return resourceStatus;
    }

    public void setResourceStatus(int resourceStatus) {
        this.resourceStatus = resourceStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }
}
