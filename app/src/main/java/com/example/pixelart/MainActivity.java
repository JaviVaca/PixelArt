package com.example.pixelart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;

import android.app.usage.UsageEvents;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.EventLog;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private GridView gridView;
    private GridLayout paleta;
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

//        LinearLayout padre =(LinearLayout)tvGrid.getParent();
//        GridView padreGridLayout ;
//        LinearLayout padrePadre =(LinearLayout)padre.getParent();
//
//        padreGridLayout = (GridView) padrePadre.getParent();
        gridView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    pintarBorrar(v,event);
                }
                if(event.getAction() ==MotionEvent.ACTION_MOVE){
                    pintarBorrar(v,event);
                }
                if(event.getAction() ==MotionEvent.ACTION_UP){
                    pintarBorrar(v,event);
                }
                return true;
            }
        });

        paleta= findViewById(R.id.paleta);
        fabPintar= findViewById(R.id.fabPintar);
        fabBorrar= findViewById(R.id.fabBorrar);
        fabNuevo = findViewById(R.id.fabNuevo);

        for (int i =0;i<paleta.getChildCount();i++){
            paleta.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Drawable colorSeleccionadoBck;
                    colorSeleccionadoBck =v.getBackground();
                    int colorSeleccionado=-1;
                    if (colorSeleccionadoBck instanceof ColorDrawable) {
                        colorSeleccionado = ((ColorDrawable) colorSeleccionadoBck).getColor();
                    }

                    putPref(getString(R.string.colorSeleccionado), String.valueOf(colorSeleccionado), getApplicationContext());
                    String colorSeleccionadoShared = getPref(getApplicationContext().getString(R.string.colorSeleccionado), getApplicationContext());
                    Toast.makeText(getApplicationContext(), "valor seleccionado->"+colorSeleccionadoShared, Toast.LENGTH_SHORT).show();
                }
            });
        }
        
        
        fabBorrar.setOnClickListener(v -> {
            borrar=true;
            pintar=false;
            putPref(getString(R.string.seleccionado), getString(R.string.borrar), getApplicationContext());
            String valorSeleccionado = getPref(getApplicationContext().getString(R.string.seleccionado), getApplicationContext());
            Toast.makeText(this, "valor seleccionado->"+valorSeleccionado, Toast.LENGTH_SHORT).show();
        });

        fabPintar.setOnClickListener(v -> {
            pintar=true;
            borrar=false;
            putPref(getString(R.string.seleccionado), getString(R.string.pintar), getApplicationContext());
            String valorSeleccionado = getPref(getApplicationContext().getString(R.string.seleccionado), getApplicationContext());
            Toast.makeText(this, "valor seleccionado->"+valorSeleccionado, Toast.LENGTH_SHORT).show();

        });

        SharedPreferences pref = getApplicationContext().getSharedPreferences("seleccionado", 0); // 0 - for private mode


    }

    private void pintarBorrar(View v, MotionEvent event) {
        Context ctx = getApplicationContext();

        String valorSeleccionado = getPref(ctx.getString(R.string.seleccionado), ctx);

        //en caso de que se haya seleccionado el boton de pintar, se pintara del color seleccionado
        //anteriormente en la paleta
        if (valorSeleccionado.equalsIgnoreCase(ctx.getString(R.string.pintar))) {
            String colorSeleccionadoShared = getPref(ctx.getString(R.string.colorSeleccionado), ctx);
            Toast.makeText(getApplicationContext(), "color seleccionado grid->" + colorSeleccionadoShared, Toast.LENGTH_SHORT).show();
            GridView parent=(GridView)v;
            View vistaHija= parent.getChildAt(Integer.parseInt(getPref(getApplicationContext().getString(R.string.lineaSeleccionada), getApplicationContext())));
            LinearLayout lnHija=(LinearLayout)vistaHija;
            for(int i=0;i<lnHija.getChildCount();i++){
                lnHija.getChildAt(i).getTag();


                if(event.getX()==lnHija.getX()&&lnHija.getY()==event.getY()){

                }
            }
            View vistaNieta= lnHija.getChildAt(0);
            Toast.makeText(getApplicationContext(), "casilla seleccionada->"+getPref(getApplicationContext().getString(R.string.cuadradoSeleccionado), getApplicationContext()), Toast.LENGTH_SHORT).show();

            vistaNieta.setBackgroundColor(Integer.parseInt(colorSeleccionadoShared));
        } else if (valorSeleccionado.equalsIgnoreCase(ctx.getString(R.string.borrar))) {

        }

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
    public static String getPref(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }
    public static void putPref(String key, String value, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }
}