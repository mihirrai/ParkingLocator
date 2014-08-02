package com.parkinglocator.mihir;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class ParkingLocator extends Activity implements View.OnClickListener {

    GPSTracker gps;
    GoogleMap gm;
    final int RQS_GooglePlayServices = 1;
    Button set, remove, nav;
    LocationManager locationManager;
    SharedPreferences prefs;
    double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        checkGPServices();
        initUI();
        initGM();
        prefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        checkPrefs();
    }

    private void checkPrefs() {
        if (prefs.contains("Latitude") && prefs.contains("Longitude")) {
            latitude = Double.longBitsToDouble(prefs.getLong("Latitude", 0));
            longitude = Double.longBitsToDouble(prefs.getLong("Longitude", 0));
            setMarker(latitude, longitude);
        }
    }

    private void setMarker(double lat, double longi) {
        gm.clear();
        gm.addMarker(new MarkerOptions().position(new LatLng(lat, longi))).setAlpha(0.7f);
    }

    private void initUI() {
        // TODO Auto-generated method stub
        set = (Button) findViewById(R.id.button1);
        nav = (Button) findViewById(R.id.button2);
        remove = (Button) findViewById(R.id.button3);
        set.setOnClickListener(this);
        nav.setOnClickListener(this);
        remove.setOnClickListener(this);
    }

    private void initGM() {
        // TODO Auto-generated method stub
        gm = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        gm.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        gm.setMyLocationEnabled(true);
    }

    private void checkGPServices() {
        // TODO Auto-generated method stub
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
        if (resultCode == ConnectionResult.SUCCESS) {
        } else {
            GooglePlayServicesUtil.getErrorDialog(resultCode, this, RQS_GooglePlayServices);
        }
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        SharedPreferences.Editor editor = prefs.edit();
        switch (arg0.getId()) {
            case R.id.button1:
                editor.clear();
                gps = new GPSTracker(ParkingLocator.this);
                if (gps.canGetLocation()) {
                    gm.clear();
                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();

                    editor.putLong("Latitude", Double.doubleToLongBits(gps.getLatitude()));
                    editor.putLong("Longitude", Double.doubleToLongBits(gps.getLongitude()));
                    editor.apply();
                    gm.addMarker(new MarkerOptions().title("Parked Location").position(new LatLng(latitude, longitude))).setAlpha(0.7f);
                    // \n is for new line
                    Toast.makeText(getApplicationContext(), "Parking Location at - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                } else {
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }
                gps.stopUsingGPS();
                break;
            case R.id.button2:
                if (prefs.contains("Latitude") && prefs.contains("Longitude")) {

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + Double.longBitsToDouble(prefs.getLong("Latitude", 0)) + "," + Double.longBitsToDouble(prefs.getLong("Longitude", 0))));
                    startActivity(intent);
                } else
                    Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button3:
                gm.clear();
                editor.clear().apply();
                break;
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.mapMode) {
            final CharSequence[] items = {"Normal", "Hybrid", "Terrain", "Satellite"};
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Colors");
            builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    switch (item) {
                        case 0:
                            gm.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                            break;
                        case 1:
                            gm.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                            break;
                        case 2:
                            gm.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                            break;
                        case 3:
                            gm.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                            break;
                    }
                    dialog.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
        return super.onOptionsItemSelected(item);
    }
}

