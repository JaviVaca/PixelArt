package com.proyectog4.pixelart;

import java.util.ArrayList;

public class Casilla {
    private final int id;
    private final int color;
    public static ArrayList<Casilla> ITEMS= new ArrayList<>();

    public Casilla(int id, int color) {
        this.id = id;
        this.color = color;
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

    public void setLinea() {
    }


}
