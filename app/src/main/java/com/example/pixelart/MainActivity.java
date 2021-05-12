package com.example.pixelart;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private GridView gridView;
    private AdaptadorGrid adaptadorGrid;
    private Button btnrandom, btnreiniciar;
    private TextView txtrandom;

    int m;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        insertarDatos();

        gridView = findViewById(R.id.GridView);
        adaptadorGrid = new AdaptadorGrid(this);
        gridView.setAdapter(adaptadorGrid);

        btnrandom = findViewById(R.id.btnrandom);
        btnreiniciar = findViewById(R.id.btnreiniciar);
        txtrandom = findViewById(R.id.txtrandom);




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

    // Al hacer click dependiendo del boton cambia a una activity diferente
    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.btnrandom): //Pestaña del ranking
                cargarDatos();

            case (R.id.btnreiniciar): //Pestaña de creditos
               /* AlertDialog.Builder alert = new AlertDialog.Builder(this);
                LayoutInflater inflaterDialog = this.getLayoutInflater();
                View dialogViewCreditos = inflaterDialog.inflate(R.layout.creditos_dialog, null);
                alert.setView(dialogViewCreditos);

//                alert.setTitle("Kredituak");
//                alert.setMessage("Majisterioko ikasleak"+"\n"
//                                    +"Nerea Aristondo"+"\n"
//                                    +"Ane Bizkarra"+"\n"
//                                    +"Itsaso Rio"+"\n"
//                                    +"Martin Gabria"+"\n");



                alert.show();*/
                break;


        }
    }

    private void cargarDatos() {
        Random rand = new Random();
        m = rand.nextInt(4);

        if (m == 0) {
            cargarDatos1();
        }

        else if(m ==1) {

            cargarDatos2();
        }

        else if(m ==2) {
            cargarDatos3();

        }

        else if(m ==3){
            cargarDatos4();

        }
    }

    private void cargarDatos4() {

        txtrandom.setText(R.string.arbol);
    }

    private void cargarDatos3() {

        txtrandom.setText(R.string.avion);
    }

    private void cargarDatos2() {

        txtrandom.setText(R.string.flor);
    }

    private void cargarDatos1() {

        txtrandom.setText(R.string.casa);


    }

}