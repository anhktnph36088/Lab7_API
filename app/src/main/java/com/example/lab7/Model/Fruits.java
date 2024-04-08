package com.example.lab7.Model;

public class Fruits {
    private String _id;
    private String name;
    private int price;

    public Fruits() {
    }

    public Fruits(String _id, String name, int price) {
        this._id = _id;
        this.name = name;
        this.price = price;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
