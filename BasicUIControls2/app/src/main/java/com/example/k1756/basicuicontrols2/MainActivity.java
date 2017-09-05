package com.example.k1756.basicuicontrols2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AutoCompleteTextView actv = (AutoCompleteTextView) findViewById(R.id.login_field);
        ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,new String[]{"Pasi","Juha","Kari","Jouni","Esa","Hannu"});
        actv.setAdapter(aa);
    }

    public void loginButtonClicked(View view) {
        // find radio group
        //RadioGroup rg = (RadioGroup) findViewById(R.id.myRadioGroup);

        // find login fields
        EditText et = (EditText)findViewById(R.id.login_field);
        String uid = et.getText().toString();

        et = (EditText)findViewById(R.id.password_field);
        String pw = et.getText().toString();

        Toast.makeText(getApplicationContext(), uid + " " + pw, Toast.LENGTH_SHORT).show();
    }
}
