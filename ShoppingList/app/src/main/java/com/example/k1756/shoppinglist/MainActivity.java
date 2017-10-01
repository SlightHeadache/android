package com.example.k1756.shoppinglist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TableLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TableLayout tableLayout;
    private ArrayList<Grocery> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tableLayout = (TableLayout) findViewById(R.id.itemTable);

        list = new ArrayList<>();

        ShoppingCartAdapter adapter = new ShoppingCartAdapter(this, list);

        // En teekkään koska menee hermot :c
        //setAdapter(adapter);
    }
}
