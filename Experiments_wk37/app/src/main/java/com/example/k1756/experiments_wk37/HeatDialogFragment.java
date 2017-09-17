package com.example.k1756.experiments_wk37;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Juuso_2 on 17.9.2017.
 */

public class HeatDialogFragment extends DialogFragment {

    public interface HeatDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog, double heat);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    HeatDialogListener myListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            myListener = (HeatDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "; Error: " + e.toString());
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle saveState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.change_temperature_dialog, null);
        builder.setView(dialogView)
            .setTitle(getString(R.string.dialog_title))
            .setPositiveButton(getString(R.string.dialog_positive), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    EditText et = (EditText) dialogView.findViewById(R.id.heat_input);
                    double d;
                    try {
                        d = Double.parseDouble(et.getText().toString());
                        myListener.onDialogPositiveClick(HeatDialogFragment.this,d);
                    }
                    catch (NumberFormatException e) {
                        throw new NumberFormatException(getString(R.string.input_error) + " " + e.toString());
                    }
                }
            })
            .setNegativeButton(getString(R.string.dialog_negative), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    myListener.onDialogNegativeClick(HeatDialogFragment.this);
                }
            });
        return builder.create();
    }
}
