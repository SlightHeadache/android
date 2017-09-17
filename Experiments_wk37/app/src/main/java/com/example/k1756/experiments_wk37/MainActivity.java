package com.example.k1756.experiments_wk37;

import android.app.DialogFragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements HeatDialogFragment.HeatDialogListener {

    private double pd_Heat;
    private int notification_id = 1;
    private TextView heatView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        heatView = (TextView) findViewById(R.id.heat_display);
        pd_Heat = 22.5;
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        displayHeat();
        launchNotification();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public void heatDialog(View view) {
        HeatDialogFragment myDialog = new HeatDialogFragment();
        myDialog.show(getFragmentManager(), getString(R.string.dialog_title));
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, double heat) {
        changeHeat(heat);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        //Toast.makeText(getApplicationContext(), getString(R.string.heat_unchanged), Toast.LENGTH_SHORT).show();
        showCustomToast(getString(R.string.heat_unchanged));
    }

    public void displayHeat() {
        //TextView tv = (TextView) findViewById(R.id.heat_display);
        heatView.setText(getString(R.string.display_temperature) + ": " + pd_Heat + "°C");
    }

    public void changeHeat(double heat) {
        pd_Heat = heat;
        //Toast.makeText(getApplicationContext(), getString(R.string.heat_changed) + ": " + pd_Heat + "°C", Toast.LENGTH_SHORT).show();
        showCustomToast(getString(R.string.heat_changed) + ": " + pd_Heat + "°C");
        displayHeat();
        launchNotification();
    }

    public void showCustomToast(String message) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast, (ViewGroup) findViewById(R.id.toast));
        TextView tv = (TextView) layout.findViewById(R.id.toast_textView);
        tv.setText(message);
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 600);
        toast.setDuration(toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    public void launchNotification() {
        // Creating an intent to open the main activity
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent intent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this)
            .setCategory(Notification.CATEGORY_MESSAGE)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(getString(R.string.notification) + ": " + pd_Heat + "°C")
            .setSmallIcon(R.drawable.heat)
            .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.heat))
            .setAutoCancel(true).setContentIntent(intent)
            .setVisibility(Notification.VISIBILITY_PUBLIC).build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(notification_id, notification);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_toast:
                showCustomToast(getString(R.string.notification) + ": " + pd_Heat + "°C");
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
