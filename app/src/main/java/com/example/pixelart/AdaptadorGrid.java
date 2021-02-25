package com.example.pixelart;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

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

        if (linea%2==0) {
            if (casilla.getColor()==0) {
                tvGrid.setBackgroundColor(Color.WHITE);
            }
            else {tvGrid.setBackgroundColor(Color.LTGRAY);}
        }
        else {
            if (casilla.getColor()==0) {
                tvGrid.setBackgroundColor(Color.LTGRAY);
            }
            else {tvGrid.setBackgroundColor(Color.WHITE);}
        }

        return view;
    }
}
