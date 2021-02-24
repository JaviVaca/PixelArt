package com.example.pixelart;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AdaptadorGrid extends BaseAdapter {

    private Context context;
    private int linea;
    public static ArrayList<Casilla> ITEMS= new ArrayList<>();

    public AdaptadorGrid (Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        for (int i=0; i<256;i++) {
            if (i%2==0) {
                ITEMS.add(new Casilla(i, 0, 0));
            }
            else {
                ITEMS.add(new Casilla(i, 1, 0));
            }

        }
        return ITEMS.size();
    }

    @Override
    public Object getItem(int position) {
        return ITEMS.get(position);
    }

    @Override
    public long getItemId(int position) {
        return ITEMS.get(position).getId();
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
                tvGrid.setBackgroundColor(Color.BLACK);
            }
            else {tvGrid.setBackgroundColor(Color.YELLOW);}
        }
        else {
            if (casilla.getColor()==0) {
                tvGrid.setBackgroundColor(Color.YELLOW);
            }
            else {tvGrid.setBackgroundColor(Color.BLACK);}
        }

        return view;
    }
}
