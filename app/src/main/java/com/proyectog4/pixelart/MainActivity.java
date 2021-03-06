package com.proyectog4.pixelart;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

import top.defaults.colorpicker.ColorPickerPopup;

public class MainActivity extends AppCompatActivity {

    private GridView gridView;
    ArrayList<LinearLayout> arrayCuadrados=new ArrayList<>();
    ArrayList<String> arrayLimites=new ArrayList<>();
    ArrayList<String> arrayInicioFin=new ArrayList<>();
    Context ctx;
    GridLayout paleta;
    ImageView imgArrow;
    LinearLayout lnAcciones2;
    int m;
    private TextView txtrandom;
    private ImageView imagenDibujo;
    private final ArrayList<Integer> colores=new ArrayList<>();
    FloatingActionButton fabColorPalette, fabColorPicker, fabPintar, fabBorrar, fabNuevo, fabRandom;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint({"ClickableViewAccessibility", "UseCompatLoadingForDrawables", "SourceLockedOrientationActivity"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Verificamos si tenemos permisos de almacenamiento
        verifyStoragePermissions(this);

        // Inserta las casillas del lienzo
        insertarDatos();

        // Declaracion de variables
        gridView = findViewById(R.id.GridView);
        lnAcciones2 = findViewById(R.id.lnAcciones2);
        lnAcciones2.setVisibility(View.GONE);
        imgArrow = findViewById(R.id.imgArrow);
        AdaptadorGrid adaptadorGrid = new AdaptadorGrid(this);
        gridView.setAdapter(adaptadorGrid);
        gridView.setPadding(100,50,100,50);
        paleta = findViewById(R.id.paleta);
        ctx = getApplicationContext();
        txtrandom = findViewById(R.id.txtDibujoRandom);
        imagenDibujo = findViewById(R.id.imagenDibujo);
        fabPintar = findViewById(R.id.fabPintar);
        fabBorrar = findViewById(R.id.fabBorrar);
        fabNuevo = findViewById(R.id.fabNuevo);
        fabRandom = findViewById(R.id.fabRandom);
        FloatingActionButton fabScreenshot = findViewById(R.id.fabScreenshot);

        // Listener de la flecha que despliega mas botones del menu
        imgArrow.setOnClickListener(v -> {
            if(lnAcciones2.getVisibility()==View.GONE){
                imgArrow.setImageDrawable(getDrawable(R.drawable.up_arrow));
                lnAcciones2.setVisibility(View.VISIBLE);

            }else{
                imgArrow.setImageDrawable(getDrawable(R.drawable.down_arrow));
                lnAcciones2.setVisibility(View.GONE);

            }
        });

        //Listener del lienzo
        gridView.setOnTouchListener((v, event) -> {
            arrayCuadrados.clear();
            arrayLimites.clear();
            arrayInicioFin.clear();
            gridView.getX();
            for(int i=0;i<gridView.getChildCount();i++){
                LinearLayout lnHijo=(LinearLayout)gridView.getChildAt(i);
                String altura="";
                String anchura="";
                int[] outLocation = new int[2];
                gridView.getLocationOnScreen(outLocation);

                int marginleftGridView=0;
                int margintopGridView=0;

                float xHijo = lnHijo.getX();
                float yHijo = lnHijo.getY();
                float alturaHijo = lnHijo.getHeight();
                float anchuraHijo = lnHijo.getWidth();


                if(yHijo<alturaHijo){
                    altura=(margintopGridView+alturaHijo)+"/"+(margintopGridView);
                }else if(yHijo>=alturaHijo){
                    altura=(margintopGridView+alturaHijo+yHijo)+"/"+(margintopGridView+yHijo);
                }
                if(xHijo<anchuraHijo){
                    anchura="->"+(marginleftGridView)+"/"+(marginleftGridView+anchuraHijo);
                }else if(xHijo>=anchuraHijo){
                    anchura="->"+(marginleftGridView+anchuraHijo+xHijo)+"/"+(marginleftGridView+xHijo);
                }

                arrayInicioFin.add(altura+".."+anchura);
                arrayLimites.add("altura->"+alturaHijo+"->anchura->"+anchuraHijo+"->y->"+yHijo+"->x->"+xHijo);
                arrayCuadrados.add(lnHijo);
            }
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if(getPref(getString(R.string.seleccionado),ctx).equalsIgnoreCase(getString(R.string.pintar))||
                        getPref(getString(R.string.seleccionado),ctx).equalsIgnoreCase(getString(R.string.borrar))&&!getPref(getString(R.string.colorSeleccionadoCasilla),ctx).isEmpty()){
                    pintarBorrar(event);
                }else if(getPref(getString(R.string.seleccionado),ctx).equalsIgnoreCase(getString(R.string.color_picker))&&!getPref(getString(R.string.colorSeleccionadoCasilla),ctx).isEmpty()){
                    elegirColor(event);
                }
            }
            if(event.getAction() ==MotionEvent.ACTION_MOVE){
                if(getPref(getString(R.string.seleccionado),ctx).equalsIgnoreCase(getString(R.string.pintar))||
                        getPref(getString(R.string.seleccionado),ctx).equalsIgnoreCase(getString(R.string.borrar))&&!getPref(getString(R.string.colorSeleccionadoCasilla),ctx).isEmpty()){
                    pintarBorrar(event);

                }else if(getPref(getString(R.string.seleccionado),ctx).equalsIgnoreCase(getString(R.string.color_picker))&&!getPref(getString(R.string.colorSeleccionadoCasilla),ctx).isEmpty()){
                    elegirColor(event);
                }
            }
            if(event.getAction() ==MotionEvent.ACTION_UP){
                if(getPref(getString(R.string.seleccionado),ctx).equalsIgnoreCase(getString(R.string.pintar))||
                        getPref(getString(R.string.seleccionado),ctx).equalsIgnoreCase(getString(R.string.borrar))&&!getPref(getString(R.string.colorSeleccionadoCasilla),ctx).isEmpty()){
                    pintarBorrar(event);
                }else if(getPref(getString(R.string.seleccionado),ctx).equalsIgnoreCase(getString(R.string.color_picker))&&!getPref(getString(R.string.colorSeleccionadoCasilla),ctx).isEmpty()){
                    elegirColor(event);
                }
            }
            return true;
        });

        // Listener del boton de elegir color personalizado
        fabColorPalette = findViewById(R.id.fabColorPalette);

        // Listener del boton de color
        fabColorPicker = findViewById(R.id.fabColorPicker);



        for (int i = 0; i< paleta.getChildCount(); i++){
            int finalI2 = i;

            if(getPref(getString(R.string.colorArrayList),ctx)!=null){
                if(getPref(getString(R.string.colorArrayList),ctx).isEmpty()) {
                    Drawable drawable = paleta.getChildAt(i).getBackground();
                    if (drawable instanceof ColorDrawable) {
                        int color = ((ColorDrawable) drawable).getColor();
                        colores.add(color);
                    }
                }else if(!getPref(getString(R.string.colorArrayList),ctx).isEmpty()){
                    ArrayList<String> myList = new ArrayList<>(Arrays.asList(String.valueOf(getPref(getString(R.string.colorArrayList), ctx)).split(",")));

                    if(!getPref(getString(R.string.colorSeleccionadoCasilla),ctx).isEmpty()){

                        for (int q=0;q<myList.size();q++){
                            if(myList.get(q).contains("[")){
                                myList.set(q,myList.get(q).replace("[",""));
                            }else if(myList.get(q).contains("]")){
                                myList.set(q,myList.get(q).replace("]",""));
                            }else if(myList.get(q).contains("\"")){
                                myList.set(q,myList.get(q).replace("\"",""));
                            }
                            if(myList.get(q).contains(" ")){
                                myList.set(q,myList.get(q).replace(" ",""));
                            }
                            colores.add(Integer.parseInt(myList.get(q)));
                        }


                        GradientDrawable shape = new GradientDrawable();
                        shape.setShape(GradientDrawable.RECTANGLE);

                        shape.setColor((Integer.parseInt(String.valueOf(colores.get(Integer.parseInt(getPref(getString(R.string.colorSeleccionadoCasilla),ctx)))))));
                        shape.setStroke(8, Color.YELLOW);

                        shape.setCornerRadius(25);
                        paleta.getChildAt(Integer.parseInt(getPref(getString(R.string.colorSeleccionadoCasilla),ctx))).setBackgroundResource(0);
                        paleta.getChildAt(Integer.parseInt(getPref(getString(R.string.colorSeleccionadoCasilla),ctx))).setBackground(shape);

                    }


                }

            }else{
                Drawable drawable = paleta.getChildAt(i).getBackground();
                if (drawable instanceof ColorDrawable) {
                    int color = ((ColorDrawable) drawable).getColor();
                    colores.add(color);
                }

            }
            paleta.getChildAt(i).setOnClickListener(v -> {
                Drawable colorSeleccionadoBck;
                colorSeleccionadoBck =v.getBackground();

                if (colorSeleccionadoBck instanceof ColorDrawable) {
                    ((ColorDrawable) colorSeleccionadoBck).getColor();
                }

                int color;
                String hexColor;

                for(int z=0;z<paleta.getChildCount();z++) {
                    GradientDrawable shape = new GradientDrawable();
                    shape.setShape(GradientDrawable.RECTANGLE);

                    LinearLayout lnC = (LinearLayout) paleta.getChildAt(z);
                    Drawable background0 = lnC.getBackground();

                    if (background0 instanceof GradientDrawable) {
                        int indexOfMyView = ((ViewGroup) paleta.getChildAt(z).getParent()).indexOfChild(paleta.getChildAt(z));
                        View viewOfMyViewPref = ((ViewGroup) v.getParent()).getChildAt(Integer.parseInt(String.valueOf(getPref(getString(R.string.colorSeleccionadoCasilla),ctx))));
                        int indexOfMyViewPref = ((ViewGroup) viewOfMyViewPref.getParent()).indexOfChild(viewOfMyViewPref);

                        if(indexOfMyView==indexOfMyViewPref){
                            String hexColors = String.format("#%06X", (0xFFFFFF & (colores.get(((ViewGroup) paleta.getChildAt(z).getParent()).indexOfChild(paleta.getChildAt(z))))));

                            shape.setColor(Color.parseColor(hexColors));
                            shape.setStroke(0, Color.TRANSPARENT);

                            shape.setCornerRadius(5);
                            shape.setCornerRadii(new float[] { 0, 0, 0, 0, 0, 0, 0, 0 });
                            lnC.setBackgroundResource(0);
                            lnC.setBackground(shape);

                        }

                    }

                    if (paleta.getChildAt(z) instanceof LinearLayout) {
                        LinearLayout lns = (LinearLayout) paleta.getChildAt(z);
                        Drawable background = lns.getBackground();
                        if (background instanceof ColorDrawable) {

                            ColorDrawable viewColor = (ColorDrawable) lns.getBackground();
                            int colorId = viewColor.getColor();
                            hexColor = String.format("#%06X", (0xFFFFFF & colorId));


                            GradientDrawable shape3 = new GradientDrawable();
                            shape3.setShape(GradientDrawable.RECTANGLE);
                            shape3.setColor(Color.parseColor(hexColor));
                            shape3.setStroke(2, Color.BLACK);

                            shape3.setCornerRadius(0);
                            shape3.setCornerRadii(new float[] { 0, 0, 0, 0, 0, 0, 0, 0 });
                                //--lns.setBackgroundDrawable(shape3);
                                lns.setBackground(shape3);
                                lns.setBackgroundResource(0);
                                lns.setBackgroundColor(Color.parseColor(hexColor));
//                            }

                        }
                    }
                }

                LinearLayout ln = (LinearLayout) v;
                Drawable background = ln.getBackground();
                if (background instanceof ColorDrawable) {
                    color = ((ColorDrawable) background).getColor();

                    String rgbColor= String.format("#%06X", (0xFFFFFF & color));
                    int[] rgba = (getRGB(Color.parseColor(rgbColor)));
                    ln.getBackground().setColorFilter(Color.rgb(rgba[0],rgba[1],rgba[2]), PorterDuff.Mode.SRC_ATOP);
                    GradientDrawable shape = new GradientDrawable();
                    shape.setShape(GradientDrawable.RECTANGLE);
                    shape.setColor(Color.parseColor(rgbColor));
//                    shape.setColor(colorSeleccionado);
                    shape.setStroke(8, Color.YELLOW);

                    shape.setCornerRadius(25);
                    ln.setBackgroundResource(0);
                    ln.setBackground(shape);

                    putPref(getString(R.string.colorSeleccionado), String.valueOf(colores.get(((ViewGroup) paleta.getChildAt(((ViewGroup) v.getParent()).indexOfChild(v)).getParent()).indexOfChild(paleta.getChildAt(((ViewGroup) v.getParent()).indexOfChild(v))))), getApplicationContext());
                    putPref(getString(R.string.colorSeleccionadoCasilla), String.valueOf(finalI2), getApplicationContext());

                    for(int q=0;q<paleta.getChildCount();q++){
                        paleta.getChildAt(q).setClickable(false);
                    }
                    new Handler().postDelayed(() -> {
                        for(int q=0;q<paleta.getChildCount();q++){
                            paleta.getChildAt(q).setClickable(true);
                        }
                    }, 500);



                }

                if (background instanceof GradientDrawable) {
                    ColorStateList colorGradient = ((GradientDrawable) background).getColor();

                    colorGradient.getDefaultColor();

                    GradientDrawable shape = new GradientDrawable();
                    shape.setShape(GradientDrawable.RECTANGLE);
                    String hexColors = String.format("#%06X", (0xFFFFFF & (colores.get(((ViewGroup) v.getParent()).indexOfChild(v)))));
                    shape.setColor(Color.parseColor(hexColors));
                    shape.setStroke(8, Color.YELLOW);

                    shape.setCornerRadius(25);
                    ln.setBackgroundResource(0);
                    ln.setBackground(shape);

                    putPref(getString(R.string.colorSeleccionado), String.valueOf(colores.get(((ViewGroup) paleta.getChildAt(((ViewGroup) v.getParent()).indexOfChild(v)).getParent()).indexOfChild(paleta.getChildAt(((ViewGroup) v.getParent()).indexOfChild(v))))), getApplicationContext());
                    putPref(getString(R.string.colorSeleccionadoCasilla), String.valueOf(finalI2), getApplicationContext());

                    for(int q=0;q<paleta.getChildCount();q++){
                        paleta.getChildAt(q).setClickable(false);
                    }
                    new Handler().postDelayed(() -> {
                        for(int q=0;q<paleta.getChildCount();q++){
                            paleta.getChildAt(q).setClickable(true);
                        }
                    }, 500);



                }

            });
        }


        /* Listeners para cada botón. Hacen lo que tienen que hacer y ademas
           se cambia color y tamaño del boton seleccionado. El resto de botones vuelven a su estado inicial*/

        // Boton Borrar
        fabBorrar.setOnClickListener(v -> {
            putPref(getString(R.string.seleccionado), getString(R.string.borrar), getApplicationContext());
            getPref(getApplicationContext().getString(R.string.seleccionado), getApplicationContext());

            fabBorrar.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));
            fabPintar.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.teal_200));
            fabNuevo.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.teal_200));
            fabRandom.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.teal_200));
            fabColorPicker.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.teal_200));

            fabColorPicker.setCustomSize(150);
            fabBorrar.setCustomSize(200);
            fabPintar.setCustomSize(150);
            fabNuevo.setCustomSize(150);
            fabRandom.setCustomSize(150);
        });

        // Boton Pintar
        fabPintar.setOnClickListener(v -> {
            putPref(getString(R.string.seleccionado), getString(R.string.pintar), getApplicationContext());
            getPref(getApplicationContext().getString(R.string.seleccionado), getApplicationContext());

            fabBorrar.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.teal_200));
            fabPintar.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));
            fabNuevo.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.teal_200));
            fabRandom.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.teal_200));
            fabColorPicker.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.teal_200));


            fabColorPicker.setCustomSize(150);
            fabBorrar.setCustomSize(150);
            fabPintar.setCustomSize(200);
            fabNuevo.setCustomSize(150);
            fabRandom.setCustomSize(150);

        });

        // Al iniciar la aplicacion queda seleccionado el boton pintar
        fabPintar.performClick();

        // Boton Nuevo lienzo
        fabNuevo.setOnClickListener(v -> {
            putPref(getString(R.string.seleccionado), getString(R.string.nuevo), getApplicationContext());
            getPref(getApplicationContext().getString(R.string.seleccionado), getApplicationContext());

            Nuevo();

            fabBorrar.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.teal_200));
            fabPintar.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.teal_200));
            fabNuevo.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));
            fabRandom.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.teal_200));
            fabColorPicker.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.teal_200));

            fabColorPicker.setCustomSize(150);
            fabBorrar.setCustomSize(150);
            fabPintar.setCustomSize(150);
            fabNuevo.setCustomSize(200);
            fabRandom.setCustomSize(150);
        });

        // Boton imagen random, que muestra una imagen para que el usuario se base en ella a la hora de crear un nuevo dibujo
        fabRandom.setOnClickListener(v -> {
            lnAcciones2.setVisibility(View.GONE);
            imgArrow.setImageDrawable(getDrawable(R.drawable.down_arrow));

            fabBorrar.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.teal_200));
            fabPintar.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.teal_200));
            fabNuevo.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.teal_200));
            fabRandom.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));
            fabColorPicker.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.teal_200));

            fabColorPicker.setCustomSize(150);
            fabBorrar.setCustomSize(150);
            fabPintar.setCustomSize(150);
            fabNuevo.setCustomSize(150);
            fabRandom.setCustomSize(200);
            cargarDibujoDatos();
        });


        // Boton seleccionar color
        fabColorPicker.setOnClickListener(v -> {
            putPref(getString(R.string.seleccionado), getString(R.string.color_picker), getApplicationContext());
            getPref(getApplicationContext().getString(R.string.seleccionado), getApplicationContext());

            fabBorrar.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.teal_200));
            fabPintar.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.teal_200));
            fabNuevo.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.teal_200));
            fabRandom.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.teal_200));
            fabColorPicker.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));
            fabBorrar.setCustomSize(150);
            fabColorPicker.setCustomSize(200);
            fabPintar.setCustomSize(150);
            fabNuevo.setCustomSize(150);
            fabRandom.setCustomSize(150);

            putPref(getString(R.string.colorArrayList), colores.toString(), getApplicationContext());
            lnAcciones2.setVisibility(View.GONE);
            imgArrow.setImageDrawable(getDrawable(R.drawable.down_arrow));

        });

        // Boton elegir color personalizado
        fabColorPalette.setOnClickListener(v -> {

            if(!getPref(getString(R.string.colorSeleccionadoCasilla),ctx).isEmpty()){
                new ColorPickerPopup.Builder(MainActivity.this).initialColor(
                        Color.RED) // set initial color// of the color// picker dialog
                        .enableBrightness(true) // enable color brightness
                        .enableAlpha(true) // enable color alpha// changer on slider or // not
                        .okTitle("OK") // this is top right// Choose button
                        .cancelTitle("Cancelar") // this is top left // Cancel button which  // closes the
                        .showIndicator(true) // this is the small box// which shows the chosen// color by user at the// bottom of the cancel// button
                        .showValue(true) // this is the value which// shows the selected// color hex code// the above all values can be made// false to disable them on the// color picker dialog.
                        .build()
                        .show(v,new ColorPickerPopup.ColorPickerObserver() {
                            @Override
                            public void
                            onColorPicked(int color) {


                                colores.set(Integer.parseInt(getPref(getString(R.string.colorSeleccionadoCasilla),ctx)),color);
                                GradientDrawable shape = new GradientDrawable();
                                shape.setShape(GradientDrawable.RECTANGLE);


                                shape.setColor((Integer.parseInt(String.valueOf(colores.get(Integer.parseInt(getPref(getString(R.string.colorSeleccionadoCasilla),ctx)))))));
                                shape.setStroke(8, Color.YELLOW);

                                shape.setCornerRadius(25);
                                paleta.getChildAt(Integer.parseInt(getPref(getString(R.string.colorSeleccionadoCasilla),ctx))).setBackgroundResource(0);
                                //--paleta.getChildAt(Integer.parseInt(getPref(getString(R.string.colorSeleccionadoCasilla),ctx))).setBackgroundDrawable(shape);
                                paleta.getChildAt(Integer.parseInt(getPref(getString(R.string.colorSeleccionadoCasilla),ctx))).setBackground(shape);

                                putPref(getString(R.string.colorSeleccionado), String.valueOf(colores.get(Integer.parseInt(getPref(getString(R.string.colorSeleccionadoCasilla),ctx)))), getApplicationContext());
                                putPref(getString(R.string.colorSeleccionadoCasilla), String.valueOf(Integer.parseInt(getPref(getString(R.string.colorSeleccionadoCasilla),ctx))), getApplicationContext());
                                putPref(getString(R.string.seleccionado), getString(R.string.pintar), getApplicationContext());
                                getPref(getApplicationContext().getString(R.string.seleccionado), getApplicationContext());



                                fabBorrar.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.teal_200));
                                fabPintar.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));
                                fabNuevo.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.teal_200));
                                fabRandom.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.teal_200));
                                fabColorPicker.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.teal_200));
                                fabColorPicker.setCustomSize(150);
                                fabBorrar.setCustomSize(150);
                                fabPintar.setCustomSize(200);
                                fabNuevo.setCustomSize(150);
                                fabRandom.setCustomSize(150);

                                putPref(getString(R.string.colorArrayList), colores.toString(), getApplicationContext());
                                imgArrow.setImageDrawable(getDrawable(R.drawable.down_arrow));
                                lnAcciones2.setVisibility(View.GONE);

                            }
                        });
            }
        });

        // Boton captura de pantalla
        fabScreenshot.setOnClickListener(v -> {
            takeScreenshot();
            lnAcciones2.setVisibility(View.GONE);
            imgArrow.setImageDrawable(getDrawable(R.drawable.down_arrow));
        });

        ajustarVista();

    }

    // Metodo para elegir color
    private void elegirColor(MotionEvent event) {
        for(int i=0;i<arrayInicioFin.size();i++){
            Log.d("miFiltro",arrayInicioFin.get(i));
            String[] largoyancho = arrayInicioFin.get(i).split("..->");
            String[] largo = largoyancho[0].split("/");
            String[] ancho=largoyancho[1].split("/");
            int largoLimite;
            int largoInicio;
            int anchoLimite;
            int anchoInicio;

            String eventoX = String.valueOf(event.getX()).substring(0, String.valueOf(event.getX()).indexOf("."));
            String eventoY = String.valueOf(event.getY()).substring(0, String.valueOf(event.getY()).indexOf("."));
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

            if(Integer.parseInt(eventoX)<=anchoLimite&&Integer.parseInt(eventoX)>=anchoInicio&&Integer.parseInt(eventoY)<=largoLimite&&Integer.parseInt(eventoY)>=largoInicio){
                TextView tv=(TextView) arrayCuadrados.get(i).getChildAt(0);
//                String colorSeleccionadoShaa = String.valueOf(getPref(ctx.getString(R.string.colorSeleccionado), ctx));
//                tv.setBackgroundColor(Integer.parseInt(colorSeleccionadoShaa));


                Drawable colorPicked=tv.getBackground();
                if (colorPicked instanceof ColorDrawable) {
                    int color = ((ColorDrawable) colorPicked).getColor();
                    colores.set(Integer.parseInt(getPref(getString(R.string.colorSeleccionadoCasilla),ctx)),color);
                }

                GradientDrawable shape = new GradientDrawable();
                shape.setShape(GradientDrawable.RECTANGLE);
//                                    Integer.toHexString(Integer.parseInt("String"));

                shape.setColor((Integer.parseInt(String.valueOf(colores.get(Integer.parseInt(getPref(getString(R.string.colorSeleccionadoCasilla),ctx)))))));
                shape.setStroke(8, Color.YELLOW);

                shape.setCornerRadius(25);
                paleta.getChildAt(Integer.parseInt(getPref(getString(R.string.colorSeleccionadoCasilla),ctx))).setBackgroundResource(0);
                paleta.getChildAt(Integer.parseInt(getPref(getString(R.string.colorSeleccionadoCasilla),ctx))).setBackground(shape);

                putPref(getString(R.string.colorSeleccionado), String.valueOf(colores.get(Integer.parseInt(getPref(getString(R.string.colorSeleccionadoCasilla),ctx)))), getApplicationContext());
                putPref(getString(R.string.colorSeleccionadoCasilla), String.valueOf(Integer.parseInt(getPref(getString(R.string.colorSeleccionadoCasilla),ctx))), getApplicationContext());
                lnAcciones2.setVisibility(View.GONE);
                putPref(getString(R.string.colorSeleccionado), String.valueOf(colores.get(Integer.parseInt(getPref(getString(R.string.colorSeleccionadoCasilla),ctx)))), getApplicationContext());
                putPref(getString(R.string.colorSeleccionadoCasilla), String.valueOf(Integer.parseInt(getPref(getString(R.string.colorSeleccionadoCasilla),ctx))), getApplicationContext());
                putPref(getString(R.string.seleccionado), getString(R.string.pintar), getApplicationContext());

                fabBorrar.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.teal_200));
                fabPintar.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));
                fabNuevo.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.teal_200));
                fabRandom.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.teal_200));
                fabColorPicker.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.teal_200));
                fabColorPicker.setCustomSize(150);
                fabBorrar.setCustomSize(150);
                fabPintar.setCustomSize(200);
                fabNuevo.setCustomSize(150);
                fabRandom.setCustomSize(150);

                lnAcciones2.setVisibility(View.GONE);
            }
        }


    }


    public static int[] getRGB(final int hex) {
        int r = (hex & 0xFF0000) >> 16;
        int g = (hex & 0xFF00) >> 8;
        int b = (hex & 0xFF);
        return new int[] {r, g, b};
    }

    //Esta funcion elige la imagen random a mostrar
    //para asi saber que debes dibujar
    private void cargarDibujoDatos() {

        Random rand = new Random();
        m = rand.nextInt(4);

        if (m == 0) {
            cargarDibujo1();
        }

        else if(m ==1) {

            cargarDibujo2();
        }

        else if(m ==2) {
            cargarDibujo3();

        }

        else {
            cargarDibujo4();

        }
    }

    //Estas funciones de cargar dibujo muestran el texto y el
    //dibujo a lo de que se debe dibujar
    private void cargarDibujo4() {

        txtrandom.setText(R.string.arbol);
        imagenDibujo.setImageResource(R.drawable.arbol);
        ocultarDibujo();

    }
    private void cargarDibujo3() {

        txtrandom.setText(R.string.avion);
        imagenDibujo.setImageResource(R.drawable.arbol);
        ocultarDibujo();
    }
    private void cargarDibujo2() {

        txtrandom.setText(R.string.flor);
        imagenDibujo.setImageResource(R.drawable.flor);
        ocultarDibujo();
    }
    private void cargarDibujo1() {

        txtrandom.setText(R.string.casa);
        imagenDibujo.setImageResource(R.drawable.casa);
        ocultarDibujo();

    }

    //Esta funcion oculta la imagen a dibujar pasados los 2 segundos
    //tambien oculta el texto a lo que se debe dibujar
    private void ocultarDibujo() {

        new Handler().postDelayed(() -> {
            imagenDibujo.setImageResource(android.R.color.transparent);
            //finish();
        },23250);

        new Handler().postDelayed(() -> {
            txtrandom.setText("");
            //finish();
        },5250);
    }

    // Limpia el lienzo para empezar de nuevo
    private void Nuevo() {
        String valorSeleccionado = String.valueOf(getPref(getApplicationContext().getString(R.string.seleccionado), getApplicationContext()));

        if (valorSeleccionado.equalsIgnoreCase(ctx.getString(R.string.nuevo))) {
            for(int i=0;i<gridView.getChildCount();i++) {
                LinearLayout lnHijo=(LinearLayout)gridView.getChildAt(i);
                TextView tvHijo=(TextView) lnHijo.getChildAt(0);

                if(tvHijo.getTag()!=null){
                    if(Integer.parseInt(tvHijo.getTag().toString())==0){
                        int colorSeleccionadoSha = Color.WHITE;
                        tvHijo.setBackgroundColor(colorSeleccionadoSha);

                    }else if(Integer.parseInt(tvHijo.getTag().toString())==1){
                        int colorSeleccionadoSha = Color.LTGRAY;
                        tvHijo.setBackgroundColor(colorSeleccionadoSha);
                    }
                }

            }


        }
    }

    // Metodo para pintar en el lienzo
    private void pintarBorrar(MotionEvent event) {

        getPref(ctx.getString(R.string.colorSeleccionado), ctx);
        getPref(ctx.getString(R.string.seleccionado), ctx);


        String valorSeleccionado = String.valueOf(getPref(ctx.getString(R.string.seleccionado), ctx));

        //en caso de que se haya seleccionado el boton de pintar, se pintara del color seleccionado
        //anteriormente en la paleta
        if (valorSeleccionado.equalsIgnoreCase(ctx.getString(R.string.pintar)) && getPref(ctx.getString(R.string.colorSeleccionado), ctx) != null) {

            Log.d("miFiltro", "esto es arrayCuadrados");

            for (int i = 0; i < arrayCuadrados.size(); i++) {
                Log.d("miFiltro", arrayCuadrados.get(i).toString());
            }
            Log.d("miFiltro", "esto es arrayLimites");

            for (int i = 0; i < arrayLimites.size(); i++) {
                Log.d("miFiltro", arrayLimites.get(i));
            }
            Log.d("miFiltro", "esto es arrayInicioFin");

            for (int i = 0; i < arrayInicioFin.size(); i++) {
                Log.d("miFiltro", arrayInicioFin.get(i));
                String[] largoyancho = arrayInicioFin.get(i).split("..->");
                String[] largo = largoyancho[0].split("/");
                String[] ancho = largoyancho[1].split("/");
                int largoLimite;
                int largoInicio;
                int anchoLimite;
                int anchoInicio;

                String eventoX = String.valueOf(event.getX()).substring(0, String.valueOf(event.getX()).indexOf("."));
                String eventoY = String.valueOf(event.getY()).substring(0, String.valueOf(event.getY()).indexOf("."));
                if (largo[0].contains(".")) {
                    String[] largoConPunto = largo[0].split(",");
                    if (largoConPunto.length == 0) {
                        largoLimite = 0;
                    } else {
                        largoLimite = Integer.parseInt(largo[0].replace(".0", ""));
                    }
                } else {
                    largoLimite = Integer.parseInt(largo[0].replace(".0", ""));
                }
                if (largo[1].contains(".")) {
                    String[] largoConPunto = largo[1].split(",");
                    if (largoConPunto.length == 0) {
                        largoInicio = 0;
                    } else {
                        largoInicio = Integer.parseInt(largo[1].replace(".0", ""));
                    }
                } else {
                    largoInicio = Integer.parseInt(largo[1].replace(".0", ""));
                }
                if (ancho[0].contains(".")) {
                    String[] anchoConPunto = ancho[0].split(",");
                    if (anchoConPunto.length == 0) {
                        anchoLimite = 0;
                    } else {
                        anchoLimite = Integer.parseInt(ancho[0].replace(".0", ""));
                    }
                } else {
                    anchoLimite = Integer.parseInt(ancho[0].replace(".0", ""));
                }
                if (ancho[1].contains(".")) {
                    String[] anchoConPunto = ancho[1].split(",");
                    if (anchoConPunto.length == 0) {
                        anchoInicio = 0;
                    } else {
                        anchoInicio = Integer.parseInt(ancho[1].replace(".0", ""));
                    }
                } else {
                    anchoInicio = Integer.parseInt(ancho[1].replace(".0", ""));
                }

                if (Integer.parseInt(eventoX) <= anchoLimite && Integer.parseInt(eventoX) >= anchoInicio && Integer.parseInt(eventoY) <= largoLimite && Integer.parseInt(eventoY) >= largoInicio) {
                    TextView tv = (TextView) arrayCuadrados.get(i).getChildAt(0);
                    String colorSeleccionadoShaa = String.valueOf(getPref(ctx.getString(R.string.colorSeleccionado), ctx));
                    tv.setBackgroundColor(Integer.parseInt(colorSeleccionadoShaa));
                }
            }


        } else if (valorSeleccionado.equalsIgnoreCase(ctx.getString(R.string.borrar)) && getPref(ctx.getString(R.string.colorSeleccionado), ctx) != null) {
            for (int i = 0; i < arrayInicioFin.size(); i++) {
                Log.d("miFiltro", arrayInicioFin.get(i));
                String[] largoyancho = arrayInicioFin.get(i).split("..->");
                String[] largo = largoyancho[0].split("/");
                String[] ancho = largoyancho[1].split("/");
                int largoLimite;
                int largoInicio;
                int anchoLimite;
                int anchoInicio;

                String eventoX = String.valueOf(event.getX()).substring(0, String.valueOf(event.getX()).indexOf("."));
                String eventoY = String.valueOf(event.getY()).substring(0, String.valueOf(event.getY()).indexOf("."));
                if (largo[0].contains(".")) {
                    String[] largoConPunto = largo[0].split(",");
                    if (largoConPunto.length == 0) {
                        largoLimite = 0;
                    } else {
                        largoLimite = Integer.parseInt(largo[0].replace(".0", ""));
                    }
                } else {
                    largoLimite = Integer.parseInt(largo[0].replace(".0", ""));
                }
                if (largo[1].contains(".")) {
                    String[] largoConPunto = largo[1].split(",");
                    if (largoConPunto.length == 0) {
                        largoInicio = 0;
                    } else {
                        largoInicio = Integer.parseInt(largo[1].replace(".0", ""));
                    }
                } else {
                    largoInicio = Integer.parseInt(largo[1].replace(".0", ""));
                }
                if (ancho[0].contains(".")) {
                    String[] anchoConPunto = ancho[0].split(",");
                    if (anchoConPunto.length == 0) {
                        anchoLimite = 0;
                    } else {
                        anchoLimite = Integer.parseInt(ancho[0].replace(".0", ""));
                    }
                } else {
                    anchoLimite = Integer.parseInt(ancho[0].replace(".0", ""));
                }
                if (ancho[1].contains(".")) {
                    String[] anchoConPunto = ancho[1].split(",");
                    if (anchoConPunto.length == 0) {
                        anchoInicio = 0;
                    } else {
                        anchoInicio = Integer.parseInt(ancho[1].replace(".0", ""));
                    }
                } else {
                    anchoInicio = Integer.parseInt(ancho[1].replace(".0", ""));
                }

                if (Integer.parseInt(eventoX) <= anchoLimite && Integer.parseInt(eventoX) >= anchoInicio && Integer.parseInt(eventoY) <= largoLimite && Integer.parseInt(eventoY) >= largoInicio) {
                    TextView tv = (TextView) arrayCuadrados.get(i).getChildAt(0);
                    if (arrayCuadrados.get(i).getChildAt(0).getTag() != null) {
                        if (Integer.parseInt(arrayCuadrados.get(i).getChildAt(0).getTag().toString()) == (0)) {
                            int colorSeleccionadoShaa = Color.WHITE;
                            tv.setBackgroundColor(colorSeleccionadoShaa);

                        } else if (Integer.parseInt(arrayCuadrados.get(i).getChildAt(0).getTag().toString()) == (1)) {
                            int colorSeleccionadoShaa = Color.LTGRAY;
                            tv.setBackgroundColor(colorSeleccionadoShaa);

                        }
                    }

                }
            }
        }
    }

    // Ajusta el grid a la anchura de la pantalla del dispositivo
    private void ajustarVista() {
        int widthPixels = getResources().getDisplayMetrics().widthPixels;
        int numeroColumnas=14;
        for (int i=0;i<paleta.getChildCount();i++){
            LinearLayout lnGrid = (LinearLayout)paleta.getChildAt(i);
            int ancho=widthPixels/numeroColumnas;

            ViewGroup.LayoutParams paramsLnGrid = lnGrid.getLayoutParams();
            paramsLnGrid.height = ancho;
            paramsLnGrid.width = ancho;
            lnGrid.setLayoutParams(paramsLnGrid);
            int padding=10;
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) lnGrid.getLayoutParams();
            p.setMargins(padding,padding,padding,padding);

        }
        LinearLayout ln = findViewById(R.id.lnAcciones);
        ln.getLayoutParams();



    }

    // Inserta el grid de casillas que sirven de lienzo
    public void insertarDatos(){
        int widthPixels = getResources().getDisplayMetrics().widthPixels;
        int heightPixels = getResources().getDisplayMetrics().heightPixels;
        int numeroColumnas = 10;

        int ancho=widthPixels/ numeroColumnas;
        int numCasillas=heightPixels/ancho;
        numeroColumnas =16;

        numCasillas=numCasillas* numeroColumnas;

//        384/

        for (int i=0; i<numCasillas;i++) {
            if (i%2==0) {
                Casilla.ITEMS.add(new Casilla(i, 0));
            }
            else {
                Casilla.ITEMS.add(new Casilla(i, 1));
            }

        }

    }

    // Shared Preferences que guarda el color elegido
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

    // Permisos de almacenamiento y lectura
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    // Verificacion de los permisos
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    // Captura de pantalla. La captura se guarda en la carpeta de la app (Android/data/com.proyectog4.pixelart)
    private void takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // nombre de la imagen y de la ruta
            String mPath = (this.getExternalFilesDir(null)+ "/" + now + ".jpg");

            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);


            try (FileOutputStream out = new FileOutputStream(imageFile)) {

                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                Toast.makeText(this, R.string.screen,Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();
            }


        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


}