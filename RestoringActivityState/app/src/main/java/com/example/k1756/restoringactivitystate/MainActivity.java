package com.example.k1756.restoringactivitystate;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

// from http://ptm.fi/coursesNew/Android/exercises/Saving%20and%20restoring%20activity%20state.pdf
public class MainActivity extends AppCompatActivity
        implements TextEntryDialogFragment.TextEntryDialogListener {

    private final String TEXTVIEW_STATEKEY = "TEXTVIEW_STATEKEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = (TextView) findViewById(R.id.textView1);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(TEXTVIEW_STATEKEY)) {
                String text = savedInstanceState.getString(TEXTVIEW_STATEKEY);
                textView.setText(text);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle saveInstanceState) {
        // get text view
        TextView textView = (TextView) findViewById(R.id.textView1);
        // save text view state
        saveInstanceState.putString(TEXTVIEW_STATEKEY, textView.getText().toString());
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialogFragment, String string) {
        TextView textView = (TextView) findViewById(R.id.textView1);
        textView.setText(string);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialogFragment) {
        Toast.makeText(getApplicationContext(), "Cancel", Toast.LENGTH_SHORT).show();
    }

    public void buttonClicked(View view) {
        TextEntryDialogFragment eDialog = new TextEntryDialogFragment();
        eDialog.show(getFragmentManager(), "Text dialog");
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds item to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    */
}
