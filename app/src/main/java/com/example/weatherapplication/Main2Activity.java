package com.example.weatherapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;


public class Main2Activity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mMapView;
    double lat;
    double log;
    String address;
    String wea_temperature;
    String wea_humidity;
    String wea_windspeed;
    String wea_precipintensity;
    String wea_precipprob;
    String wea_summary;


    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Intent intent= getIntent();
        ArrayList<String> result_location=intent.getStringArrayListExtra("location");
        lat=Double.valueOf(result_location.get(0));
        log=Double.valueOf(result_location.get(1));
        address=intent.getStringExtra("address");

        ArrayList<String> result_weather=intent.getStringArrayListExtra("weather");
        wea_temperature=result_weather.get(0);
        wea_humidity=result_weather.get(1);
        wea_windspeed=result_weather.get(2);
        wea_precipintensity=result_weather.get(3);
        wea_precipprob=result_weather.get(4);
        wea_summary=result_weather.get(5);
        TextView temperatureView= findViewById(R.id.temperature_text);
        TextView humidityView=findViewById(R.id.humidity_text);
        TextView windspeedView=findViewById(R.id.windspeed_text);
        TextView intensityView=findViewById(R.id.precipintensity_text);
        TextView proabilityView=findViewById(R.id.precipprob_text);
        TextView summaryView=findViewById(R.id.summary_text);

        temperatureView.setText(wea_temperature);
        humidityView.setText(wea_humidity);
        windspeedView.setText(wea_windspeed);
        intensityView.setText(wea_precipintensity);
        proabilityView.setText(wea_precipprob);
        summaryView.setText(wea_summary);

        // *** IMPORTANT ***
        // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
        // objects or sub-Bundles.
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        mMapView = (MapView) findViewById(R.id.mapView);
        mMapView.onCreate(mapViewBundle);
        mMapView.getMapAsync(this);
    }


    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in the specific location
        // and move the map's camera to the same location.

        LatLng location = new LatLng(lat, log);
        googleMap.addMarker(new MarkerOptions().position(location).title(address));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
