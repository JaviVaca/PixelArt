package com.example.pixelart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Random;

import top.defaults.colorpicker.ColorPickerPopup;

public class MainActivity extends AppCompatActivity {

    private GridView gridView;
    ArrayList<LinearLayout> arrayCuadrados=new ArrayList<>();
    ArrayList<String> arrayLimites=new ArrayList<>();
    ArrayList<String> arrayInicioFin=new ArrayList<>();
    Context ctx;
    GridLayout paleta;
    int m;
    private TextView txtrandom;
    private ImageView imagenDibujo;
    private ArrayList<Integer> colores=new ArrayList<>();
    private int numeroColumnas;
    FloatingActionButton fabColorPalette ;
    FloatingActionButton fabColorPicker ;
    private LinearLayout ln;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint({"ClickableViewAccessibility", "UseCompatLoadingForDrawables"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        insertarDatos();

        gridView = findViewById(R.id.GridView);
        AdaptadorGrid adaptadorGrid = new AdaptadorGrid(this);
        gridView.setAdapter(adaptadorGrid);
        gridView.setPadding(100,50,100,50);
        paleta = findViewById(R.id.paleta);

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

                pintarBorrar(event);
            }
            if(event.getAction() ==MotionEvent.ACTION_MOVE){
                pintarBorrar(event);
            }
            if(event.getAction() ==MotionEvent.ACTION_UP){
                pintarBorrar(event);
            }
            return true;
        });


        FloatingActionButton fabPintar = findViewById(R.id.fabPintar);
        FloatingActionButton fabBorrar = findViewById(R.id.fabBorrar);
        FloatingActionButton fabNuevo = findViewById(R.id.fabNuevo);
        FloatingActionButton fabRandom = findViewById(R.id.fabRandom);
        fabColorPalette = findViewById(R.id.fabColorPalette);
        fabColorPicker = findViewById(R.id.fabColorPicker);

        txtrandom = findViewById(R.id.txtDibujoRandom);
        imagenDibujo = findViewById(R.id.imagenDibujo);

        for (int i = 0; i< paleta.getChildCount(); i++){
            int finalI = i;
            int finalI1 = i;
            int finalI2 = i;

            Drawable drawable = paleta.getChildAt(i).getBackground();
            if (drawable instanceof ColorDrawable) {
                int color = ((ColorDrawable) drawable).getColor();
                colores.add(color);
            }

            paleta.getChildAt(i).setOnClickListener(v -> {
                Drawable colorSeleccionadoBck;
                colorSeleccionadoBck =v.getBackground();


//                int colorSeleccionado= Integer.parseInt(getPref(getString(R.string.colorSeleccionado),ctx));
                int colorSeleccionado=0;
                if (colorSeleccionadoBck instanceof ColorDrawable) {
                    colorSeleccionado = ((ColorDrawable) colorSeleccionadoBck).getColor();
                }

                int color=Color.TRANSPARENT;
                String hexColor = null;

                for(int z=0;z<paleta.getChildCount();z++) {
                    GradientDrawable shape = new GradientDrawable();
                    shape.setShape(GradientDrawable.RECTANGLE);

                    LinearLayout lnC = (LinearLayout) paleta.getChildAt(z);
                    Drawable background0 = lnC.getBackground();
                    if (background0 instanceof ColorDrawable) {

                        ColorDrawable viewColor = (ColorDrawable) lnC.getBackground();
                        int colorId = viewColor.getColor();
                        hexColor = String.format("#%06X", (0xFFFFFF & colorId));
                    }
                    if (background0 instanceof GradientDrawable) {
                        int indexOfMyView = ((ViewGroup) paleta.getChildAt(z).getParent()).indexOfChild(paleta.getChildAt(z));
                        View viewOfMyViewPref = ((ViewGroup) v.getParent()).getChildAt(Integer.parseInt(String.valueOf(getPref(getString(R.string.colorSeleccionadoCasilla),ctx))));
                        int indexOfMyViewPref = ((ViewGroup) viewOfMyViewPref.getParent()).indexOfChild(viewOfMyViewPref);

                        if(indexOfMyView==indexOfMyViewPref){
                            String hexColors = String.format("#%06X", (0xFFFFFF & (colores.get(((ViewGroup) paleta.getChildAt(z).getParent()).indexOfChild(paleta.getChildAt(z))))));

//                            int colorIdpref = Integer.parseInt(getPref(getString(R.string.colorSeleccionado),ctx));
//                            hexColor=String.format("#%06X", (0xFFFFFF & colorIdpref));
                            ArrayList<String> item = new ArrayList<>();
                            item.add(hexColor);


//                            shape.setColor(Color.parseColor(item.get(0)));
                            shape.setColor(Color.parseColor(hexColors));
                            shape.setStroke(0, Color.TRANSPARENT);

                            shape.setCornerRadius(5);
                            shape.setCornerRadii(new float[] { 0, 0, 0, 0, 0, 0, 0, 0 });
                            lnC.setBackgroundResource(0);
                            lnC.setBackgroundDrawable(shape);

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
                                lns.setBackgroundDrawable(shape3);
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
//                shape.setCornerRadii(new float[] { 8, 8, 8, 8, 0, 0, 0, 0 });
                    ln.setBackgroundResource(0);
                    ln.setBackgroundDrawable(shape);


//                ln.setBackground(getDrawable(R.drawable.color_seleccionado));
                    String hexColors = String.format("#%06X", (0xFFFFFF & (colores.get(((ViewGroup) paleta.getChildAt(((ViewGroup) v.getParent()).indexOfChild(v)).getParent()).indexOfChild(paleta.getChildAt(((ViewGroup) v.getParent()).indexOfChild(v)))))));

                    putPref(getString(R.string.colorSeleccionado), String.valueOf(colores.get(((ViewGroup) paleta.getChildAt(((ViewGroup) v.getParent()).indexOfChild(v)).getParent()).indexOfChild(paleta.getChildAt(((ViewGroup) v.getParent()).indexOfChild(v))))), getApplicationContext());
                    putPref(getString(R.string.colorSeleccionadoCasilla), String.valueOf(finalI2), getApplicationContext());
                    String colorSeleccionadoShared = String.valueOf(getPref(getApplicationContext().getString(R.string.colorSeleccionado), getApplicationContext()));
                    Toast.makeText(getApplicationContext(), "valor seleccionado click->"+colorSeleccionadoShared, Toast.LENGTH_SHORT).show();

                    for(int q=0;q<paleta.getChildCount();q++){
                        paleta.getChildAt(q).setClickable(false);
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            for(int q=0;q<paleta.getChildCount();q++){
                                paleta.getChildAt(q).setClickable(true);
                            }
                        }
                    }, 500);



                }
//                PorterDuff.Mode backgroundGrad = ln.getBackgroundTintMode();

                if (background instanceof GradientDrawable) {
                    ColorStateList colorGradient = ((GradientDrawable) background).getColor();

                   color=colorGradient.getDefaultColor();

                    String rgbColor= String.format("#%06X", (0xFFFFFF & color));
//                    int[] rgba = (getRGB(Color.parseColor(String.valueOf(colores.get(((ViewGroup) v.getParent()).indexOfChild(v))))));
//                    ln.getBackground().setColorFilter(Color.rgb(rgba[0],rgba[1],rgba[2]), PorterDuff.Mode.SRC_ATOP);
                    GradientDrawable shape = new GradientDrawable();
                    shape.setShape(GradientDrawable.RECTANGLE);
                    String hexColors = String.format("#%06X", (0xFFFFFF & (colores.get(((ViewGroup) v.getParent()).indexOfChild(v)))));
                    shape.setColor(Color.parseColor(hexColors));
//                    shape.setColor(colorSeleccionado);
                    shape.setStroke(8, Color.YELLOW);

                    shape.setCornerRadius(25);
//                shape.setCornerRadii(new float[] { 8, 8, 8, 8, 0, 0, 0, 0 });
                    ln.setBackgroundResource(0);
                    ln.setBackgroundDrawable(shape);


//                ln.setBackground(getDrawable(R.drawable.color_seleccionado));
//                    putPref(getString(R.string.colorSeleccionado), String.valueOf(colorSeleccionado), getApplicationContext());
                    putPref(getString(R.string.colorSeleccionado), String.valueOf(colores.get(((ViewGroup) paleta.getChildAt(((ViewGroup) v.getParent()).indexOfChild(v)).getParent()).indexOfChild(paleta.getChildAt(((ViewGroup) v.getParent()).indexOfChild(v))))), getApplicationContext());
                    putPref(getString(R.string.colorSeleccionadoCasilla), String.valueOf(finalI2), getApplicationContext());
                    String colorSeleccionadoShared = String.valueOf(getPref(getApplicationContext().getString(R.string.colorSeleccionado), getApplicationContext()));
                    Toast.makeText(getApplicationContext(), "valor seleccionado putpref->"+colorSeleccionadoShared, Toast.LENGTH_SHORT).show();

                    for(int q=0;q<paleta.getChildCount();q++){
                        paleta.getChildAt(q).setClickable(false);
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            for(int q=0;q<paleta.getChildCount();q++){
                                paleta.getChildAt(q).setClickable(true);
                            }
                        }
                    }, 500);



                }

            });
        }

        ctx = getApplicationContext();

        /* Listeners para cada botón. Hacen lo que tienen que hacer y ademas
           se cambia color y tamaño del boton seleccionado. El resto de botones vuelven a su estado inicial*/

        fabBorrar.setOnClickListener(v -> {
            putPref(getString(R.string.seleccionado), getString(R.string.borrar), getApplicationContext());
            String valorSeleccionado = String.valueOf(getPref(getApplicationContext().getString(R.string.seleccionado), getApplicationContext()));
            //Toast.makeText(this, "valor seleccionado->"+valorSeleccionado, Toast.LENGTH_SHORT).show();


            fabBorrar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            fabPintar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.teal_200)));
            fabNuevo.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.teal_200)));
            fabRandom.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.teal_200)));
            fabBorrar.setCustomSize(200);
            fabPintar.setCustomSize(150);
            fabNuevo.setCustomSize(150);
            fabRandom.setCustomSize(150);
        });
        fabPintar.setOnClickListener(v -> {
            putPref(getString(R.string.seleccionado), getString(R.string.pintar), getApplicationContext());
            String valorSeleccionado = String.valueOf(getPref(getApplicationContext().getString(R.string.seleccionado), getApplicationContext()));
            //Toast.makeText(this, "valor seleccionado->"+valorSeleccionado, Toast.LENGTH_SHORT).show();

            fabBorrar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.teal_200)));
            fabPintar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            fabNuevo.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.teal_200)));
            fabRandom.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.teal_200)));
            fabBorrar.setCustomSize(150);
            fabPintar.setCustomSize(200);
            fabNuevo.setCustomSize(150);
            fabRandom.setCustomSize(150);

        });
        fabNuevo.setOnClickListener(v -> {
            putPref(getString(R.string.seleccionado), getString(R.string.nuevo), getApplicationContext());
            String valorSeleccionado = String.valueOf(getPref(getApplicationContext().getString(R.string.seleccionado), getApplicationContext()));
            //Toast.makeText(this, "valor seleccionado->"+valorSeleccionado, Toast.LENGTH_SHORT).show();
            Nuevo();

            fabBorrar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.teal_200)));
            fabPintar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.teal_200)));
            fabNuevo.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            fabRandom.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.teal_200)));
            fabBorrar.setCustomSize(150);
            fabPintar.setCustomSize(150);
            fabNuevo.setCustomSize(200);
            fabRandom.setCustomSize(150);
        });
        fabRandom.setOnClickListener(v -> {
            fabBorrar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.teal_200)));
            fabPintar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.teal_200)));
            fabNuevo.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.teal_200)));
            fabRandom.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            fabBorrar.setCustomSize(150);
            fabPintar.setCustomSize(150);
            fabNuevo.setCustomSize(150);
            fabRandom.setCustomSize(200);
            cargarDibujoDatos();
        });
        //SharedPreferences pref = getApplicationContext().getSharedPreferences("seleccionado", 0); // 0 - for private mode

        fabColorPicker.setOnClickListener(v -> {

        });
        final int[] mDefaultColor = {0};
        fabColorPalette.setOnClickListener(v -> {

            new ColorPickerPopup.Builder(MainActivity.this).initialColor(
                    Color.RED) // set initial color
                    // of the color
                    // picker dialog
                    .enableBrightness(
                            true) // enable color brightness
                    // slider or not
                    .enableAlpha(
                            true) // enable color alpha
                    // changer on slider or
                    // not
                    .okTitle(
                            "Choose") // this is top right
                    // Choose button
                    .cancelTitle(
                            "Cancel") // this is top left
                    // Cancel button which
                    // closes the
                    .showIndicator(
                            true) // this is the small box
                    // which shows the chosen
                    // color by user at the
                    // bottom of the cancel
                    // button
                    .showValue(
                            true) // this is the value which
                    // shows the selected
                    // color hex code
                    // the above all values can be made
                    // false to disable them on the
                    // color picker dialog.
                    .build()
                    .show(
                            v,
                            new ColorPickerPopup.ColorPickerObserver() {
                                @Override
                                public void
                                onColorPicked(int color) {
                                    // set the color
                                    // which is returned
                                    // by the color
                                    // picker
//                                    mDefaultColor[0] = color;

                                    // now as soon as
                                    // the dialog closes
                                    // set the preview
                                    // box to returned
                                    // color
//                                    int indexOfMyView = ((ViewGroup) paleta.getChildAt(z).getParent()).indexOfChild(paleta.getChildAt(z));

                                    colores.set(Integer.parseInt(getPref(getString(R.string.colorSeleccionadoCasilla),ctx)),color);
                                    GradientDrawable shape = new GradientDrawable();
                                    shape.setShape(GradientDrawable.RECTANGLE);
//                                    Integer.toHexString(Integer.parseInt("String"));

                                    shape.setColor((Integer.parseInt(String.valueOf(colores.get(Integer.parseInt(getPref(getString(R.string.colorSeleccionadoCasilla),ctx)))))));
                                    shape.setStroke(8, Color.YELLOW);

                                    shape.setCornerRadius(25);
                                    paleta.getChildAt(Integer.parseInt(getPref(getString(R.string.colorSeleccionadoCasilla),ctx))).setBackgroundResource(0);
                                    paleta.getChildAt(Integer.parseInt(getPref(getString(R.string.colorSeleccionadoCasilla),ctx))).setBackgroundDrawable(shape);

                                    putPref(getString(R.string.colorSeleccionado), String.valueOf(colores.get(Integer.parseInt(getPref(getString(R.string.colorSeleccionadoCasilla),ctx)))), getApplicationContext());
                                    putPref(getString(R.string.colorSeleccionadoCasilla), String.valueOf(Integer.parseInt(getPref(getString(R.string.colorSeleccionadoCasilla),ctx))), getApplicationContext());

//                                    paleta.getChildAt(Integer.parseInt(getPref(getString(R.string.colorSeleccionadoCasilla),ctx))).setBackgroundColor(color);
                                }
                            });

        });

        ajustarVista();
    }



    public static int[] getRGB(final int hex) {
        int r = (hex & 0xFF0000) >> 16;
        int g = (hex & 0xFF00) >> 8;
        int b = (hex & 0xFF);
        return new int[] {r, g, b};
    }


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

        else if(m ==3){
            cargarDibujo4();

        }
    }

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

    private void pintarBorrar(MotionEvent event) {
        String colorSeleccionadoSha = String.valueOf(getPref(ctx.getString(R.string.colorSeleccionado), ctx));

        if(String.valueOf(getPref(ctx.getString(R.string.colorSeleccionado), ctx))!=null&&String.valueOf(getPref(ctx.getString(R.string.seleccionado), ctx))!=null){


            String valorSeleccionado = String.valueOf(getPref(ctx.getString(R.string.seleccionado), ctx));

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
                    int largoLimite;
                    int largoInicio;
                    int anchoLimite;
                    int anchoInicio;
    //                String[] eventoX = String.valueOf(event.getX()).split(".");
    //                String[] eventoY = String.valueOf(event.getY()).split(".");

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
                        String colorSeleccionadoShaa = String.valueOf(getPref(ctx.getString(R.string.colorSeleccionado), ctx));
                        tv.setBackgroundColor(Integer.parseInt(colorSeleccionadoShaa));
                    }
                }


            } else if (valorSeleccionado.equalsIgnoreCase(ctx.getString(R.string.borrar))) {
                for(int i=0;i<arrayInicioFin.size();i++){
                    Log.d("miFiltro",arrayInicioFin.get(i));
                    String[] largoyancho = arrayInicioFin.get(i).split("..->");
                    String[] largo = largoyancho[0].split("/");
                    String[] ancho=largoyancho[1].split("/");
                    int largoLimite;
                    int largoInicio;
                    int anchoLimite;
                    int anchoInicio;
    //                String[] eventoX = String.valueOf(event.getX()).split(".");
    //                String[] eventoY = String.valueOf(event.getY()).split(".");

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
                        if( arrayCuadrados.get(i).getChildAt(0).getTag()!=null){
                            if( Integer.parseInt(arrayCuadrados.get(i).getChildAt(0).getTag().toString())==(0)){
                                int colorSeleccionadoShaa = Color.WHITE;
                                tv.setBackgroundColor(colorSeleccionadoShaa);

                            }else if( Integer.parseInt(arrayCuadrados.get(i).getChildAt(0).getTag().toString())==(1)){
                                int colorSeleccionadoShaa = Color.LTGRAY;
                                tv.setBackgroundColor(colorSeleccionadoShaa);

                            }
                        }

                    }
                }
            }
        }
    }
    private void ajustarVista() {
        int widthPixels = getResources().getDisplayMetrics().widthPixels;
        int numeroColumnas=14;
        for (int i=0;i<paleta.getChildCount();i++){
            LinearLayout lnGrid = (LinearLayout)paleta.getChildAt(i);
            int ancho=widthPixels/numeroColumnas;
// Changes the height and width to the specified *pixels*


            ViewGroup.LayoutParams paramsLnGrid = lnGrid.getLayoutParams();
            paramsLnGrid.height = ancho;
            paramsLnGrid.width = ancho;
            lnGrid.setLayoutParams(paramsLnGrid);
            int padding=10;
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) lnGrid.getLayoutParams();
            p.setMargins(padding,padding,padding,padding);
//            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
//                    ancho, ancho);
//
////            layoutParams.setMargins(30, 20, 30, 0);
//            layoutParams.setMargins(4,4,4,4);
//            lnGrid.setLayoutParams(layoutParams);
        }
        ln=(LinearLayout)findViewById(R.id.lnAcciones);
        ViewGroup.LayoutParams paramsLnGrid = ln.getLayoutParams();


//        if(widthPixels<paramsLnGrid.width){
//            ln.removeView(fabColorPicker);
//            ln.removeView(fabColorPalette);
//            LinearLayout parent = new LinearLayout(MainActivity.this);
//            int index = ((ViewGroup) ln.getParent()).indexOfChild(ln);
//
//            parent.setLayoutParams(new LinearLayout.LayoutParams(ln.getHeight(), ln.getWidth()));
//            parent.setOrientation(LinearLayout.HORIZONTAL);
//            ((ViewGroup) ln.getParent()).addView(parent, index);
//        }

    }
    public void insertarDatos(){
        int widthPixels = getResources().getDisplayMetrics().widthPixels;
        int heightPixels = getResources().getDisplayMetrics().heightPixels;
        int porcentajePantalla=40;
        numeroColumnas=10;

        int ancho=widthPixels/numeroColumnas;
        int numCasillas=heightPixels/ancho;
        numeroColumnas=16;

        numCasillas=numCasillas*numeroColumnas;

//        384/

        if(numCasillas%16!=0){
            int resultado=numCasillas%16;
            int resyutl=resultado;
        }
        for (int i=0; i<numCasillas;i++) {
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