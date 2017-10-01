package com.example.k1756.shoppinglist;

/**
 * Created by Juuso_2 on 1.10.2017.
 */

public class Grocery {

    private String name;
    private int count;
    private double price;

    public Grocery(String name, int count, double price) {
        this.name = name;
        this.count = count;
        this.price = price;
    }

    public String name() { return name; }
    public int count() { return count; }
    public double price() { return price; }
    public double total() { return price * count; }
}
