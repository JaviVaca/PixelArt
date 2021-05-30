package com.example.pixelart;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AdaptadorGrid extends BaseAdapter {

    private final Context context;
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
        int numCol = 20;
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
//        int heightPixels = context.getResources().getDisplayMetrics().heightPixels;
//        int porcentajePantalla=60;
        TextView tvGrid = view.findViewById(R.id.tvGrid);
        int ancho=widthPixels/numCol;
//        int alto=heightPixels*porcentajePantalla/100;
        tvGrid.setWidth(ancho);
        tvGrid.setHeight(ancho);
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

}
