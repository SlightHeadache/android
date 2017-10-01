package com.example.k1756.restoringactivitystate;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

/**
 * Created by Juuso_2 on 1.10.2017.
 */

public class TextEntryDialogFragment extends DialogFragment {
    // sends dara to host with listeners
    public interface TextEntryDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog, String str);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    // This instance is used for delivering action events
    TextEntryDialogListener myListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity;

        if (context instanceof Activity)
        {
            activity = (Activity) context;
            // check if the host activity implements necessary interfaces
            try {
                myListener = (TextEntryDialogListener) activity;
            }
            catch (ClassCastException e){
                throw new ClassCastException(activity.toString() + " must implement TextEntryDialogListener!");
            }
        }

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // creating a new alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because it's going in the dialog layout
        final View dialogView = inflater.inflate(R.layout.textentry_dialog, null);
        builder.setView(dialogView).setTitle(getString(R.string.string_prompt)).setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // find the text box
                EditText editText = (EditText) dialogView.findViewById(R.id.editDialog);
                String text = editText.getText().toString();

                // Send the positive button event back to the host
                myListener.onDialogPositiveClick(TextEntryDialogFragment.this,text);
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Send the negative button back to the host activity
                myListener.onDialogNegativeClick(TextEntryDialogFragment.this);
            }
        });
        //return builder.create();
        Dialog d = builder.create();
        try {
            d.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }
        catch (NullPointerException e) {
            throw new  NullPointerException("Null pointer exception in input mode selection!: " + e.toString());
        }
        return d;
    }
}
