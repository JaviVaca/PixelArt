package com.example.pixelart;

import java.util.ArrayList;

public class Casilla {
    private final int id;
    private final int color;
    private int linea;
    public static ArrayList<Casilla> ITEMS= new ArrayList<>();

    public Casilla (int id, int color, int linea) {
        this.id = id;
        this.color = color;
        this.linea = linea;
    }

    public int getId() {
        return id;
    }

    public int getColor() {
        return color;
    }

    //public int getLinea() {return linea;}

    /*public void setColor(int color) {
        this.color = color;
    }*/

    public void setLinea(int linea) {
        this.linea = linea;
    }


}
