package com.example.pixelart;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.GridView;

public class MainActivity extends AppCompatActivity {

    private GridView gridView;
    private AdaptadorGrid adaptadorGrid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = findViewById(R.id.GridView);
        adaptadorGrid = new AdaptadorGrid(this);
        gridView.setAdapter(adaptadorGrid);

    }
}