package com.example.k1756.shoppinglist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Juuso_2 on 1.10.2017.
 */

public class ShoppingCartAdapter extends ArrayAdapter<Grocery> {
    private Context context;
    private ArrayList<Grocery> groceries;

    public ShoppingCartAdapter(Context context, ArrayList<Grocery> groceries) {
        super(context, R.layout.columnlayout, groceries);
        this.context = context;
        this.groceries = groceries;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.columnlayout, parent, false);

            TextView nameDisplay    = (TextView) convertView.findViewById(R.id.name_display);
            TextView countDisplay   = (TextView) convertView.findViewById(R.id.count_display);
            TextView priceDisplay   = (TextView) convertView.findViewById(R.id.price_display);
            TextView totalDisplay   = (TextView) convertView.findViewById(R.id.total_display);

            nameDisplay.setText(groceries.get(position).name());
            countDisplay.setText(groceries.get(position).count());
            priceDisplay.setText(String.valueOf(groceries.get(position).price()));
            totalDisplay.setText(String.valueOf(groceries.get(position).total()));
        }
        return convertView;
    }
}
