package com.example.pixelart;

public class Casilla {
    private int id;
    private int color;
    private int linea;

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

    public int getLinea() {return linea;}

    public void setColor(int color) {
        this.color = color;
    }

    public void setLinea(int linea) {
        this.linea = linea;
    }
}
