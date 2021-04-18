package com.example.pixelart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;

import android.annotation.SuppressLint;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private GridView gridView;
    private GridLayout paleta;
    private AdaptadorGrid adaptadorGrid;
    private FloatingActionButton fabPintar, fabBorrar, fabNuevo;
    ArrayList<LinearLayout> arrayCuadrados=new ArrayList<>();
    ArrayList<String> arrayLimites=new ArrayList<>();
    ArrayList<String> arrayInicioFin=new ArrayList<>();
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
            @SuppressLint({"ClickableViewAccessibility", "Range"})
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                arrayCuadrados.clear();
                arrayLimites.clear();
                arrayInicioFin.clear();
                gridView.getX();
                for(int i=0;i<gridView.getChildCount();i++){
                    LinearLayout lnHijo=(LinearLayout)gridView.getChildAt(i);
                    TextView tvHijo=(TextView) lnHijo.getChildAt(0);
                    String altura="";
                    String anchura="";
                    int[] outLocation = new int[2];
                    gridView.getLocationOnScreen(outLocation);

                    int marginleftGridView=0;
                    int margintopGridView=0;
//                    int marginleftGridView=outLocation[0];
//                    int margintopGridView=outLocation[1];

                    float xHijo = lnHijo.getX();
                    float yHijo = lnHijo.getY();
                    float alturaHijo = lnHijo.getHeight();
                    float anchuraHijo = lnHijo.getWidth();

                    if(yHijo<alturaHijo){
                        altura=(margintopGridView+alturaHijo)+"/"+(margintopGridView+yHijo);
                    }else if(yHijo>=alturaHijo){
                        altura=(margintopGridView+alturaHijo+yHijo)+"/"+(margintopGridView+yHijo);
                    }
                    if(xHijo<anchuraHijo){
                        anchura="->"+(marginleftGridView+anchuraHijo)+"/"+(marginleftGridView+xHijo);
                    }else if(xHijo>=anchuraHijo){
                        anchura="->"+(marginleftGridView+anchuraHijo+xHijo)+"/"+(marginleftGridView+xHijo);
                    }

                    arrayInicioFin.add(altura+".."+anchura);
                    arrayLimites.add("altura->"+alturaHijo+"->anchura->"+anchuraHijo+"->y->"+yHijo+"->x->"+xHijo);
                    arrayCuadrados.add(lnHijo);
                }
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

            Log.d("miFiltro","esto es arrayCuadrados");

            for(int i=0;i<arrayCuadrados.size();i++){
               Log.d("miFiltro",arrayCuadrados.get(i).toString());
            }
            Log.d("miFiltro","esto es arrayLimites");

            for(int i=0;i<arrayLimites.size();i++){
               Log.d("miFiltro",arrayLimites.get(i));
            }
            Log.d("miFiltro","esto es arrayInicioFin");

            for(int i=0;i<arrayInicioFin.size();i++){
                Log.d("miFiltro",arrayInicioFin.get(i));
                String[] largoyancho = arrayInicioFin.get(i).split("..->");
                String[] largo = largoyancho[0].split("/");
                String[] ancho=largoyancho[1].split("/");
                Integer largoLimite=-1;
                Integer largoInicio=-1;
                Integer anchoLimite=-1;
                Integer anchoInicio=-1;
                String[] eventoX = String.valueOf(event.getX()).split(".");
                String[] eventoY = String.valueOf(event.getY()).split(".");
                if(largo[0].contains(".")){
                    String[] largoConPunto = largo[0].split(",");
                    if(largoConPunto.length==0){
                        largoLimite =0;
                    }else{
                        largoLimite = Integer.parseInt(largo[0].replace(".0",""));
                    }
                }else{
                    largoLimite = Integer.parseInt(largo[0].replace(".0",""));
                }
                if(largo[1].contains(".")){
                    String[] largoConPunto = largo[1].split(",");
                    if(largoConPunto.length==0){
                        largoInicio =0;
                    }else{
                        largoInicio = Integer.parseInt(largo[1].replace(".0",""));
                    }
                }else{
                    largoInicio = Integer.parseInt(largo[1].replace(".0",""));
                }
                if(ancho[0].contains(".")){
                    String[] anchoConPunto = ancho[0].split(",");
                    if(anchoConPunto.length==0){
                        anchoLimite =0;
                    }else{
                        anchoLimite = Integer.parseInt(ancho[0].replace(".0",""));
                    }
                }else{
                    anchoLimite = Integer.parseInt(ancho[0].replace(".0",""));
                }
                if(ancho[1].contains(".")){
                    String[] anchoConPunto = ancho[1].split(",");
                    if(anchoConPunto.length==0){
                        anchoInicio =0;
                    }else{
                        anchoInicio = Integer.parseInt(ancho[1].replace(".0",""));
                    }
                }else{
                    anchoInicio = Integer.parseInt(ancho[1].replace(".0",""));
                }

                if(Integer.parseInt(eventoX[0])<=anchoLimite&&Integer.parseInt(eventoX[0])>=anchoInicio&&Integer.parseInt(eventoY[0])<=largoLimite&&Integer.parseInt(eventoY[0])>=largoInicio){
                    TextView tv=(TextView) arrayCuadrados.get(i).getChildAt(0);
                    String colorSeleccionadoSha = getPref(ctx.getString(R.string.colorSeleccionado), ctx);
                    tv.setBackgroundColor(Integer.parseInt(colorSeleccionadoSha));
                }
            }


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