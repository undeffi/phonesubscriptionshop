package com.example.ppshop;

public class ShoppingItem {
    private String name;
    private String info;
    private String price;
    private final int imageResource;

    public ShoppingItem(String name, String info, String price, int imageResource) {
        this.name = name;
        this.info = info;
        this.price = price;
        this.imageResource = imageResource;
    }

    String getName() {
        return name;
    }
    String getInfo() {
        return info;
    }
    String getPrice() {
        return price;
    }
    public int getImageResource() {
        return imageResource;
    }
}
