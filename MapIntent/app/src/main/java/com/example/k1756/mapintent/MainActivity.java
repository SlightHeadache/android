package com.example.k1756.mapintent;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void showMap(View view) {
        // get lat and lng values
        EditText et1 = (EditText) findViewById(R.id.lat);
        EditText et2 = (EditText) findViewById(R.id.lng);
        String str1 = et1.getText().toString();
        String str2 = et2.getText().toString();

        try {
            double lat = Double.parseDouble(str1);
            double lng = Double.parseDouble(str2);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("geo:"+lat+","+lng));
            startActivity(intent);
        }
        catch(Exception ex) {
            Toast.makeText(getApplicationContext(), getString(R.string.error_text), Toast.LENGTH_SHORT).show();
        }
    }
}
