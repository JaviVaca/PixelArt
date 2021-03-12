package com.example.pixelart;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private GridView gridView;
    private AdaptadorGrid adaptadorGrid;
    private FloatingActionButton fabPintar, fabBorrar, fabNuevo;
    private Boolean pintar=true, borrar=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        insertarDatos();

        gridView = findViewById(R.id.GridView);
        adaptadorGrid = new AdaptadorGrid(this);
        gridView.setAdapter(adaptadorGrid);

        fabPintar= findViewById(R.id.fabPintar);
        fabBorrar= findViewById(R.id.fabBorrar);
        fabNuevo = findViewById(R.id.fabNuevo);

        fabBorrar.setOnClickListener(v -> {
            borrar=true;
            pintar=false;
        });

        fabPintar.setOnClickListener(v -> {
            pintar=true;
            borrar=false;
        });


    }

    public void insertarDatos(){
        for (int i=0; i<256;i++) {
            if (i%2==0) {
                Casilla.ITEMS.add(new Casilla(i, 0, 0));
            }
            else {
                Casilla.ITEMS.add(new Casilla(i, 1, 0));
            }

        }

    }
}