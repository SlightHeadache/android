package com.example.k1756.listviewexcersice;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // find list view
        ListView myListView = (ListView) findViewById(R.id.list_view);

        // generating dummy data
        String[] phones = new String[]{
                "Android", "iPhone", "WindowsMobile", "Blackberry", "WebOS", "Ubuntu",
                "Android", "iPhone", "WindowsMobile", "Blackberry", "WebOS", "Ubuntu"
        };

        // add strings to ArrayList
        final ArrayList<String> list = new ArrayList<>();
        /*
        for (int i = 0; i < phones.length; ++i) {
            list.add(phones[i]);
        }
        for (String phone : phones) {
            list.add(phone);
        }
        */
        Collections.addAll(list, phones);

        // add data to ArrayAdapter, which is the default Android ListView style/layout
        //ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.rowlayout, R.id.textView, list);
        PhoneArrayAdapter adapter = new PhoneArrayAdapter(this, list);

        // set data to list view with adapter
        myListView.setAdapter(adapter);
    }
}

