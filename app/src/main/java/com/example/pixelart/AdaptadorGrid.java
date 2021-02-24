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
    public static ArrayList<Casilla> ITEMS= new ArrayList<>();

    public AdaptadorGrid (Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        for (int i=0; i<256;i++) {
            if (i%2==0) {
                ITEMS.add(new Casilla(i, 0));
            }
            else {
                ITEMS.add(new Casilla(i, 1));
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

        /*if (position%16==0){
            final Casilla casillaAnterior = (Casilla) getItem(position-1);
            if(casillaAnterior.getColor()==0){
                color=Color.BLACK;
            }else{
                else {tvGrid.setBackgroundColor(Color.YELLOW);
            }
        }
            if (casilla.getColor()==color) {
                tvGrid.setBackgroundColor(Color.BLACK);
            }
            else {tvGrid.setBackgroundColor(Color.YELLOW);}
*/

        if


        return view;
    }
}
