package com.example.k1756.basicuicontrols1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void selectButtonClicked(View view) {
        // find radio group
        RadioGroup rg = (RadioGroup) findViewById(R.id.myRadioGroup);

        // find button
        int id = rg.getCheckedRadioButtonId();

        if (id != -1) {
            RadioButton rb = (RadioButton) findViewById(id);

            // get radio text
            String text = (String) rb.getText();

            // toast text

            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
        }
    }
}
