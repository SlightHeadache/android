package com.example.k1756.listviewexcersice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.k1756.listviewexcersice.R;

/**
 * Created by Juuso_2 on 7.9.2017.
 */

public class DetailActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Draw activity detail layout
        setContentView(R.layout.activity_detail);

        // Get intent
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String phone = bundle.getString("phone");

        // Update text and image views to show data
        TextView textView = (TextView) findViewById(R.id.phoneTextView);
        textView.setText(phone);
        ImageView imageView = (ImageView) findViewById(R.id.phoneImageView);
        switch (phone) {
            case "Android": imageView.setImageResource(R.drawable.android); break;
            case "iPhone": imageView.setImageResource(R.drawable.ios); break;
            case "WindowsMobile": imageView.setImageResource(R.drawable.windows); break;
            case "Blackberry": imageView.setImageResource(R.drawable.blackberry); break;
            case "WebOS": imageView.setImageResource(R.drawable.webos); break;
            case "Ubuntu": imageView.setImageResource(R.drawable.ubuntu); break;
        }
    }

    public void backButtonPressed(View view) {
        // finish and close activity
        finish();
    }
}
