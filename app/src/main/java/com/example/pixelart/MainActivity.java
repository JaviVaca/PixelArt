package com.example.pixelart;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private GridView gridView;
    private boolean tienePermisoAlmacenamiento = false;
    private static final int permisoAlmacenamiento = 1;
    ArrayList<LinearLayout> arrayCuadrados=new ArrayList<>();
    ArrayList<String> arrayLimites=new ArrayList<>();
    ArrayList<String> arrayInicioFin=new ArrayList<>();
    Context ctx;
    GridLayout paleta;
    int m;
    private TextView txtrandom;
    private ImageView imagenDibujo;
    FloatingActionButton fabPintar, fabBorrar, fabNuevo;

    @SuppressLint({"ClickableViewAccessibility", "UseCompatLoadingForDrawables"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        insertarDatos();
        int estadoDePermiso = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (estadoDePermiso == PackageManager.PERMISSION_GRANTED) {
            tienePermisoAlmacenamiento=true;
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, permisoAlmacenamiento);
        }
        gridView = findViewById(R.id.GridView);
        LinearLayout menu= findViewById(R.id.menu);
        Button captura=findViewById(R.id.captura);
        Button compartir=findViewById(R.id.compartir);
        Button nuevo=findViewById(R.id.nuevoLienzo);
        Button aleatorio=findViewById(R.id.aleatorio);
        AdaptadorGrid adaptadorGrid = new AdaptadorGrid(this);
        gridView.setAdapter(adaptadorGrid);
        gridView.setPadding(100,50,100,50);
        paleta = findViewById(R.id.paleta);

//        for(int i=0;i<paleta.getChildCount();i++) {
//            if (paleta.getChildAt(i) instanceof LinearLayout) {
//                LinearLayout ln = (LinearLayout) paleta.getChildAt(i);
//                ln.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        for(int z=0;z<paleta.getChildCount();z++) {
//                            if (paleta.getChildAt(z) instanceof LinearLayout) {
//                                LinearLayout ln = (LinearLayout) paleta.getChildAt(z);
//                                ln.setBackgroundResource(0);
//                            }
//                        }
//                        LinearLayout ln = (LinearLayout) v;
//                        ln.setBackground(getDrawable(R.drawable.color_seleccionado));
//                    }
//                });
//            }
//        }

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

        txtrandom = findViewById(R.id.txtDibujoRandom);
        imagenDibujo = findViewById(R.id.imagenDibujo);

        for (int i = 0; i< paleta.getChildCount(); i++){
            int finalI = i;
            paleta.getChildAt(i).setOnClickListener(v -> {
                Drawable colorSeleccionadoBck;
                colorSeleccionadoBck =v.getBackground();
                int colorSeleccionado=-1;
                if (colorSeleccionadoBck instanceof ColorDrawable) {
                    colorSeleccionado = ((ColorDrawable) colorSeleccionadoBck).getColor();
                }

                putPref(getString(R.string.colorSeleccionado), String.valueOf(colorSeleccionado), getApplicationContext());
                String colorSeleccionadoShared = getPref(getApplicationContext().getString(R.string.colorSeleccionado), getApplicationContext());
                Toast.makeText(getApplicationContext(), "valor seleccionado->"+colorSeleccionadoShared, Toast.LENGTH_SHORT).show();
                int color=Color.TRANSPARENT;
                for(int z=0;z<paleta.getChildCount();z++) {
                    if (paleta.getChildAt(z) instanceof LinearLayout) {
                        LinearLayout lns = (LinearLayout) paleta.getChildAt(z);
                        Drawable background = lns.getBackground();
                        if (background instanceof ColorDrawable) {
                            color = ((ColorDrawable) background).getColor();

                            ColorDrawable viewColor = (ColorDrawable) lns.getBackground();
                            int colorId = viewColor.getColor();
                            String hexColor = String.format("#%06X", (0xFFFFFF & colorId));

                            lns.setBackgroundColor(Color.parseColor(hexColor));

                        }
                    }
                }
                LinearLayout ln = (LinearLayout) v;
                Drawable background = ln.getBackground();
                if (background instanceof ColorDrawable) {
                    color = ((ColorDrawable) background).getColor();

                    String rgbColor= String.format("#%06X", (0xFFFFFF & color));
                    ln.getBackground().setColorFilter(Color.rgb(40, 50, 60), PorterDuff.Mode.SRC_ATOP);
                }
                ln.setBackground(getDrawable(R.drawable.color_seleccionado));

            });
        }

        ctx = getApplicationContext();

        /* Listeners para cada botón. Hacen lo que tienen que hacer y ademas
           se cambia color y tamaño del boton seleccionado. El resto de botones vuelven a su estado inicial*/

        fabBorrar.setOnClickListener(v -> {
            putPref(getString(R.string.seleccionado), getString(R.string.borrar), getApplicationContext());
            String valorSeleccionado = getPref(getApplicationContext().getString(R.string.seleccionado), getApplicationContext());
            //Toast.makeText(this, "valor seleccionado->"+valorSeleccionado, Toast.LENGTH_SHORT).show();


            fabBorrar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            fabPintar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.teal_200)));
            fabNuevo.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.teal_200)));
            fabBorrar.setCustomSize(200);
            fabPintar.setCustomSize(150);
            fabNuevo.setCustomSize(150);
        });
        fabPintar.setOnClickListener(v -> {
            putPref(getString(R.string.seleccionado), getString(R.string.pintar), getApplicationContext());
            String valorSeleccionado = getPref(getApplicationContext().getString(R.string.seleccionado), getApplicationContext());
            //Toast.makeText(this, "valor seleccionado->"+valorSeleccionado, Toast.LENGTH_SHORT).show();

            fabBorrar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.teal_200)));
            fabPintar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            fabNuevo.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.teal_200)));
            fabBorrar.setCustomSize(150);
            fabPintar.setCustomSize(200);
            fabNuevo.setCustomSize(150);

        });
         fabNuevo.setOnClickListener(v -> {
             if (menu.getVisibility()==View.GONE) {
                 menu.setVisibility(View.VISIBLE);
                 fabBorrar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.teal_200)));
                 fabPintar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.teal_200)));
                 fabNuevo.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                 fabBorrar.setCustomSize(150);
                 fabPintar.setCustomSize(150);
                 fabNuevo.setCustomSize(200);
             } else {
                menu.setVisibility(View.GONE);
                 fabNuevo.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.teal_200)));
                 fabBorrar.setCustomSize(150);
                 fabPintar.setCustomSize(150);
                 fabNuevo.setCustomSize(150);
             }
           /* putPref(getString(R.string.seleccionado), getString(R.string.nuevo), getApplicationContext());
            String valorSeleccionado = getPref(getApplicationContext().getString(R.string.seleccionado), getApplicationContext());
            //Toast.makeText(this, "valor seleccionado->"+valorSeleccionado, Toast.LENGTH_SHORT).show();

            Nuevo();

            fabBorrar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.teal_200)));
            fabPintar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.teal_200)));
            fabNuevo.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            fabBorrar.setCustomSize(150);
            fabPintar.setCustomSize(150);
            fabNuevo.setCustomSize(200);*/

        });
             captura.setOnClickListener(v -> {
                 Bitmap lienzo = loadBitmapFromView(gridView);
                 saveImage(lienzo);
             });
             compartir.setOnClickListener(v -> {
             });
             nuevo.setOnClickListener(v -> {
                 String valorSeleccionado = getPref(getApplicationContext().getString(R.string.seleccionado), getApplicationContext());
                 Nuevo();
             });
             aleatorio.setOnClickListener(v -> cargarDibujoDatos());

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case permisoAlmacenamiento:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                break;
            // Aquí más casos dependiendo de los permisos
            // case OTRO_CODIGO_DE_PERMISOS...
        }
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


    }


    public static Bitmap loadBitmapFromView(View v) {
        Bitmap b = Bitmap.createBitmap(v.getWidth() , v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);
        return b;
    }

    private void saveImage(Bitmap finalBitmap) {
        if (tienePermisoAlmacenamiento) {
            Date date = new Date();
            @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyyHH:mm:ss");
            dateFormat.format(date);
            String fechaHoy = date.toString();
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root);
            myDir.mkdirs();
            String fname = "Image-" + "Captura" + fechaHoy + ".jpg";
            File file = new File(myDir, fname);
            if (file.exists()) file.delete();
            Log.i("LOAD", root + fname);
            try {
                FileOutputStream out = new FileOutputStream(file);
                finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
        String valorSeleccionado = getPref(getApplicationContext().getString(R.string.seleccionado), getApplicationContext());

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

    private void pintarBorrar(MotionEvent event) {
        String colorSeleccionadoSha = getPref(ctx.getString(R.string.colorSeleccionado), ctx);

        if(getPref(ctx.getString(R.string.colorSeleccionado), ctx)!=null&&getPref(ctx.getString(R.string.seleccionado), ctx)!=null){


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
                        String colorSeleccionadoShaa = getPref(ctx.getString(R.string.colorSeleccionado), ctx);
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

    public void insertarDatos(){
        for (int i=0; i<384;i++) {
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