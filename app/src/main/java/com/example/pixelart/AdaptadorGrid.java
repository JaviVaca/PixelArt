package com.example.pixelart;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
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
        int numeroColumnas = 16;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.grid_item, viewGroup, false);
        }

//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        context.getResources().getDisplayMetrics();
//        this.context.getResources().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthPixels = context.getResources().getDisplayMetrics().widthPixels;

        TextView tvGrid = view.findViewById(R.id.tvGrid);
        int ancho=widthPixels/numeroColumnas;
        tvGrid.setWidth(ancho);
//        tvGrid.setLayoutParams(new ViewGroup.LayoutParams(
//                ancho,
//                ViewGroup.LayoutParams.MATCH_PARENT));
        final Casilla casilla = (Casilla) getItem(position);


        if ((position>0)&&(position%numeroColumnas==0)) {
            linea = position/numeroColumnas;
        }
        casilla.setLinea(linea);

//        tvGrid.setTag(position+"/"+linea);
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
