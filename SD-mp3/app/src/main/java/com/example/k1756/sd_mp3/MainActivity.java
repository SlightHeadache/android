package com.example.k1756.sd_mp3;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Debug;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.os.Environment.DIRECTORY_MUSIC;
import static android.os.Environment.MEDIA_MOUNTED;
import static android.os.Environment.MEDIA_SHARED;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private String mediaPath;
    private List<String> songs = new ArrayList<>();
    private MediaPlayer mediaPlayer = new MediaPlayer();
    //private LoadSongTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);
        //mediaPath = Environment.getExternalStorageDirectory().getPath() + "/moto g muistikortti/Känny/Musaa";
        //mediaPath = Environment.getExternalStoragePublicDirectory(MEDIA_SHARED).getPath();
        mediaPath = "/storage/FD76-11FB/moto g muistikortti/Känny/Musaa/";  // On my phone, none of the other mediaPaths worked properly, I think I have a bit messy file system set ups on my phone, idk. This hard-coded path works, though
        //mediaPath = Environment.getExternalStorageDirectory().getPath() + "/Music/";
        //mediaPath = Environment.getExternalStorageDirectory().getPath();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(songs.get(position));
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                }
                catch (IOException e) {
                    Toast.makeText(getBaseContext(), "Cannot start audio!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // This set-up is not the best possible one, since the app has to be restarted after granting permissions, but my patience is running thin. This at least works.
        if (isReadStoragePermissionGranted()) {
            LoadSongTask task = new LoadSongTask();
            task.execute();
        }
        else {
            TextView textView = (TextView) findViewById(R.id.textView);
            textView.setText("Can't access storage!");
        }
    }

    public  boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else {
            return true;
        }
    }

    private class LoadSongTask extends AsyncTask<Void, String, Void> {
        private List<String> loadedSongs = new ArrayList<>();

        protected void onPreExecute() {
            Toast.makeText(getApplicationContext(), "Loading...", Toast.LENGTH_LONG).show();
        }

        protected Void doInBackground(Void... url) {
            updateSongListRecursive(new File(mediaPath));
            Log.d("BG", "Rec done");
            return null;
        }

        private void updateSongListRecursive(File path) {
            // Bunch of junk for debugging; Helped with finding out the proper pathing
            Log.d("SONG_LIST", path.toString());
            if (path.listFiles() != null)
            {
                Log.d("DUMP", "Start:");
                for (File file : path.listFiles())
                {
                    if (path.isDirectory()) Log.d("DIR", file.getAbsolutePath());
                    else Log.d("FILE", file.getAbsolutePath());
                }
                Log.d("DUMP", "End");
            }
            else Log.d("DUMP", "Empty dump");
            // End of debug chunk

            if (path.isDirectory()) {
                for (int i = 0; i < path.listFiles().length; i++) {
                    File file = path.listFiles()[i];
                    updateSongListRecursive(file);
                }
            }
            else {
                // Final is required for path display, which is used for debug purposes. Without that it can't be shown in the UI thread, and I can't be asked to research passing arguments to Runnables at this moment
                final String name = path.getAbsolutePath();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView textView = (TextView) findViewById(R.id.textView);
                        textView.setText("Scanning...: " + name);   // Adding a raw string to a text view breaks style guides etc. but here it's done due to laziness
                    }
                });

                publishProgress(name);
                if (name.endsWith(".mp3")) {
                    loadedSongs.add(name);
                }
            }
        }

        protected void onPostExecute(Void args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ArrayAdapter<String> songList = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, loadedSongs);
                    listView.setAdapter(songList);
                    songs = loadedSongs;
                }
            });

            Toast.makeText(getApplicationContext(), "Songs: " + songs.size(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mediaPlayer.isPlaying()) mediaPlayer.reset();
    }
}
