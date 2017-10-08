package com.example.k1756.otto_etsin;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private final int REQUEST_LOCATION = 1;
    private GoogleMap mMap;
    private List<Coordinate> coordinates;
    private FusedLocationProviderClient mFusedLocationClient;
    private LatLng currentPos;
    private final LatLng mDefaultLocation = new LatLng(62.2416223, 25.7597309);
    private boolean mLocationPermissionGranted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        FetchDataTask task = new FetchDataTask();
        task.execute("http://student.labranet.jamk.fi/~K1756/junk/otto.json");
        checkPermissions();
    }

    private void checkPermissions() {
        // check permission
        int hasLocationPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        // permission is not granted yet
        if (hasLocationPermission != PackageManager.PERMISSION_GRANTED) {
            // ask it -> a dialog will be opened
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            // permission is already granted, start get location information
            start();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // accepted, start getting location information
                    mLocationPermissionGranted = true;
                    start();
                } else {
                    // denied
                    Toast.makeText(this, "Location access denied by the user!", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void start()
    {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Default camera position, if current location can't be found
        try {
            mMap.setMyLocationEnabled(true);
            mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        double lat = location.getLatitude();
                        double lon = location.getLongitude();
                        LatLng temp = new LatLng(lat, lon);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(temp, 14));
                    }
                    else {
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, 14));
                    }
                }
            });
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    FetchDistanceTask distanceTask = new FetchDistanceTask();
                    distanceTask.execute(marker.getPosition());
                    return false;
                }
            });
        }
        catch (SecurityException e) {
            Log.e("Security Exception", "Could not use fused location client");
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, 14));
        }
        //mMap.addMarker(new MarkerOptions().position(ICT).title("Marker in ICT"));
    }

    private class FetchDataTask extends AsyncTask<String, Void, JSONArray> {
        @Override
        protected JSONArray doInBackground(String... urls) {
            HttpURLConnection urlConnection = null;
            JSONArray jsonObject = null;
            try {
                URL url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();

                /*
                JsonParser jp = new JsonParser();
                JsonElement root = jp.parse(new InputStreamReader((InputStream) urlConnection.getContent()));
                jsonObject = root.getAsJsonObject();
                */

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                //Log.d("Stringbuilder: ", stringBuilder.toString());
                jsonObject = new JSONArray(stringBuilder.toString());

            }
            catch (IOException | JSONException e) {
                Log.e("Error reading JSON", e.getMessage());
            }
            /* Added to IOException branch due to it being identical
            catch (JSONException e) {
                e.printStackTrace();
            }
            */
            /* Is caught by IOException
            catch (MalformedURLException e) {
                e.printStackTrace();
            }
            */
            finally {
                if (urlConnection != null) urlConnection.disconnect();
            }
            return jsonObject;
        }

        protected void onPostExecute(JSONArray json) {
            try {
                Toast.makeText(getApplicationContext(), "Data loaded!", Toast.LENGTH_SHORT).show();
                //JsonArray otot = json.getAsJsonArray();
                /*
                for (JsonElement jsonElement : otot) {
                    double lat = ((JsonObject) jsonElement).get("Koordinaatti LAT").getAsDouble();
                    double lon = ((JsonObject) jsonElement).get("Koordinaatti LON").getAsDouble();

                    LatLng xy = new LatLng(lat, lon);
                    mMap.addMarker(new MarkerOptions()
                            .position(xy));
                }
                */
                /*
                Iterator itr = otot.iterator();
                while (itr.hasNext()) {
                    Object temp = itr.next();
                    if (temp instanceof JsonObject) {
                        double lat = ((JsonObject) temp).get("Koordinaatti LAT").getAsDouble();
                        double lon = ((JsonObject) temp).get("Koordinaatti LON").getAsDouble();

                        LatLng xy = new LatLng(lat, lon);
                        mMap.addMarker(new MarkerOptions()
                                .position(xy));
                    }
                }
                */

                for(int i = 0; i < json.length(); i++)
                {
                    JSONObject temp = json.getJSONObject(i);
                    double lat = temp.getDouble("Koordinaatti LAT");  //temp.get("Koordinaatti LAT").getAsDouble();
                    double lon = temp.getDouble("Koordinaatti LON");   //temp.get("Koordinaatti LON").getAsDouble();
                    //coordinates.add(new Coordinate(lat,lon));
                    LatLng xy = new LatLng(lat, lon);
                    mMap.addMarker(new MarkerOptions()
                        .position(xy)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.otto_logo)));
                }

            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class FetchDistanceTask extends AsyncTask<LatLng, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(LatLng... positions) {
            HttpsURLConnection urlConnection = null;
            JSONObject jsonObject = null;
            getDeviceLocation();
            try {
                LatLng pos = positions[0];
                Log.e("POS", pos.toString());
                Log.e("Current", currentPos.toString());
                URL url = new URL("https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + currentPos.latitude + "," + currentPos.longitude + "&destinations=" + pos.latitude + "," + pos.longitude + "&key=AIzaSyBt8QMsQJv45WieSbEzYdjJwRE9JQf5Oiw");

                urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.connect();

                /*
                JsonParser jp = new JsonParser();
                JsonElement root = jp.parse(new InputStreamReader((InputStream) urlConnection.getContent()));
                jsonObject = root.getAsJsonObject();
                */

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                //Log.d("Stringbuilder: ", stringBuilder.toString());
                jsonObject = new JSONObject(stringBuilder.toString());

            }
            catch (IOException | JSONException e) {
                Log.e("Error reading JSON", e.getMessage());
            }
            finally {
                if (urlConnection != null) urlConnection.disconnect();
            }
            return jsonObject;
        }
        protected void onPostExecute(JSONObject json) {
            try {
                int distance = json.getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0).getJSONObject("distance").getInt("value");
                Toast.makeText(getApplicationContext(), "Distance: " + distance + " meters", Toast.LENGTH_SHORT).show();
            }
            catch (JSONException e) {
                e.getStackTrace();
            }
        }
    }

    private void getDeviceLocation() {
    /*
     * Get the best and most recent location of the device, which may be null in rare
     * cases when a location is not available.
     */
    /*
        try {
            if (mLocationPermissionGranted) {
                Task locationResult = mFusedLocationClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            Object mLastKnownLocation = task.getResult();
                            if (mLastKnownLocation instanceof Location)
                            {
                                double lat = ((Location) mLastKnownLocation).getLatitude();
                                double lon = ((Location) mLastKnownLocation).getLongitude();
                                LatLng temp = new LatLng(lat,lon);
                                Log.e("Set CPOS", temp.toString());
                                currentPos = temp;
                            }
                        } else {
                            Log.d("Null!", "Current location is null. Using defaults.");
                            Log.e("Log:", "Exception: %s", task.getException());
                            currentPos = mDefaultLocation;
                        }
                    }
                });
            }
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
        */
        try {
            mMap.setMyLocationEnabled(true);
            mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        double lat = location.getLatitude();
                        double lon = location.getLongitude();
                        LatLng temp = new LatLng(lat, lon);
                        setPosition(temp);
                    }
                    else {
                        Log.d("Null!", "Current location is null. Using defaults.");
                        setPosition(mDefaultLocation);
                    }
                }
            });
        }
        catch (SecurityException e) {
            Log.e("Security Exception", "Could not use fused location client");
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, 14));
        }
    }

    private void setPosition(LatLng pos) {
        Log.e("Set CPOS", pos.toString());
        currentPos = pos;
    }
}
