package com.example.pixelart;

public class Casilla {
    private int id;
    private int color;
    int size= 256;

    public Casilla (int id, int color) {
        this.id = id;
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public int getColor() {
        return color;
    }




}
