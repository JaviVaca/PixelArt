package com.example.pixelart;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;

public class AdaptadorGrid extends BaseAdapter {

    private Context context;
    private int linea;


    public AdaptadorGrid (Context context) {
        this.context = context;
    }


    @Override
    public int getCount() {

        return Casilla.ITEMS.size();
    }

    @Override
    public Casilla getItem(int position) {
        return Casilla.ITEMS.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Casilla.ITEMS.get(position).getId();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.grid_item, viewGroup, false);
        }


        TextView tvGrid = view.findViewById(R.id.tvGrid);

        final Casilla casilla = (Casilla) getItem(position);


        if ((position>0)&&(position%16==0)) {
            linea = position/16;
        }
        casilla.setLinea(linea);

        tvGrid.setTag(position+"/"+linea);
        if (linea%2==0) {
            if (casilla.getColor()==0) {
                tvGrid.setTag(0);
                tvGrid.setBackgroundColor(Color.WHITE);
            }
            else {
                tvGrid.setTag(1);
                tvGrid.setBackgroundColor(Color.LTGRAY);
            }
        }
        else {
            if (casilla.getColor()==0) {
                tvGrid.setTag(1);
                tvGrid.setBackgroundColor(Color.LTGRAY);
            }
            else {
                tvGrid.setTag(0);
                tvGrid.setBackgroundColor(Color.WHITE);
            }
        }


        View finalView = view;
        tvGrid.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

//                Context ctx = finalView.getContext();
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//
//                    putPref(ctx.getString(R.string.lineaSeleccionada), String.valueOf(position), ctx.getApplicationContext());
//                    putPref(ctx.getString(R.string.cuadradoSeleccionado), String.valueOf((position/casilla.getLinea())-1), ctx.getApplicationContext());
//                }
//                if(event.getAction() ==MotionEvent.ACTION_MOVE){
//                    putPref(ctx.getString(R.string.lineaSeleccionada), String.valueOf(position), ctx.getApplicationContext());
//                    putPref(ctx.getString(R.string.cuadradoSeleccionado), String.valueOf((position/casilla.getLinea())-1), ctx.getApplicationContext());
//                }
//                if(event.getAction() ==MotionEvent.ACTION_UP){
//                    putPref(ctx.getString(R.string.lineaSeleccionada), String.valueOf(position), ctx.getApplicationContext());
//                    putPref(ctx.getString(R.string.cuadradoSeleccionado), String.valueOf((position/casilla.getLinea())-1), ctx.getApplicationContext());
//                }
                return true;
            }
        });
//        LinearLayout padre =(LinearLayout)tvGrid.getParent();
//        GridView padreGridLayout ;
//        LinearLayout padrePadre =(LinearLayout)padre.getParent();
//
//        padreGridLayout = (GridView) padrePadre.getParent();
//        padreGridLayout.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                if ((event.getAction() == MotionEvent.ACTION_DOWN)||(event.getAction() ==MotionEvent.ACTION_MOVE)) {
//
//                    Context ctx = finalView.getContext();
//
//                    String valorSeleccionado = getPref(ctx.getString(R.string.seleccionado), ctx);
//
//                    //en caso de que se haya seleccionado el boton de pintar, se pintara del color seleccionado
//                    //anteriormente en la paleta
//                    if (valorSeleccionado.equalsIgnoreCase(ctx.getString(R.string.pintar))) {
//                        String colorSeleccionadoShared = getPref(ctx.getString(R.string.colorSeleccionado), ctx);
//                        Toast.makeText(finalView.getContext(), "color seleccionado grid->" + colorSeleccionadoShared, Toast.LENGTH_SHORT).show();
//                        GridLayout parent=(GridLayout)v;
//                        View vistaHija= parent.getChildAt(position);
//                        vistaHija.setBackgroundColor(Integer.parseInt(colorSeleccionadoShared));
//                    } else if (valorSeleccionado.equalsIgnoreCase(ctx.getString(R.string.borrar))) {
//
//                    }
//                }
//                return false;
//            }
//        });

        return view;
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
