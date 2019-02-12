package com.example.weatherapplication;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Random;


//Siyu wrote this part, just for a testing of the difference between mapView and mapFragment
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    double lat;
    double log;
    String address;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Intent intent = getIntent();
        ArrayList<String> result= intent.getStringArrayListExtra("location");
        lat=Double.valueOf(result.get(0));
        log=Double.valueOf(result.get(1));
        address=intent.getStringExtra("address");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
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
        // Add a marker in location and move the camera
        LatLng location = new LatLng(lat,log);
        googleMap.addMarker(new MarkerOptions().position(location).title(address));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));


        // 添加标记到指定经纬度
        googleMap.addMarker(new MarkerOptions().position(new LatLng(lat, log)).title("Marker")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

         //点击标记点，默认点击弹出跳转google地图或导航选择，返回true则不弹出
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return true;
            }
        });

         //单击
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

            }
        });

        // 允许手势缩放
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        //googleMap.getUiSettings().setMapToolbarEnabled(false); 禁用精简模式下右下角的工具栏

        // 允许拖动地图
        googleMap.getUiSettings().setScrollGesturesEnabled(true);

        // 设置缩放级别
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(13);
        googleMap.animateCamera(zoom);
    }
}
