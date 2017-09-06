package com.example.k1756.listviewexcersice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class PhoneArrayAdapter extends ArrayAdapter<String> {
    // Application context
    private Context context;
    // phone data
    private ArrayList<String> phones;

    // get app context and phone data to adapter
    public PhoneArrayAdapter(Context context, ArrayList<String> phones) {
        super(context, R.layout.rowlayout, R.id.textView, phones);
        this.context = context;
        this.phones = phones;
    }

    // populate every row in ListView
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get row
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.rowlayout, parent, false);

        // show phone names
        TextView textView = (TextView) rowView.findViewById(R.id.textView);
        textView.setText(phones.get(position));

        // show phone icons
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView);
        switch (phones.get(position)) {
            case "Android": imageView.setImageResource(R.drawable.android); break;
            case "iPhone": imageView.setImageResource(R.drawable.ios); break;
            case "WindowsMobile": imageView.setImageResource(R.drawable.windows); break;
            case "Blackberry": imageView.setImageResource(R.drawable.blackberry); break;
            case "WebOS": imageView.setImageResource(R.drawable.webos); break;
            case "Ubuntu": imageView.setImageResource(R.drawable.ubuntu); break;
        }
        return rowView;
    }
}
