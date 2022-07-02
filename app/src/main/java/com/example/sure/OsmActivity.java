package com.example.sure;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;

import org.json.JSONObject;
import org.osmdroid.api.IMapController;
//import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class OsmActivity extends AppCompatActivity {
    private MapView map;
    private IMapController mapController;
    private MyLocationNewOverlay mLocationOverlay;
    ProgressDialog progressDialog;

    private static final String TAG = "OsmActivity";

    private static final int PERMISSION_REQUEST_CODE = 1;

    private FusedLocationProviderClient fusedLocationClient;

    public void openActivity(View v){
        startActivity(new Intent(this, ListActivity.class));

    }

    public void openMarkerActivity(){
        startActivity(new Intent(this, ListActivity.class));

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init osmdroid configuration
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        //init map
        setContentView(R.layout.activity_main);
        map = (MapView) findViewById(R.id.mapView);
        map.setTileSource(TileSourceFactory.MAPNIK);

        //set controls
        map.setBuiltInZoomControls(false);
        map.setMultiTouchControls(true);
        mapController = map.getController();

        //set Marker DEMO
        createMarker(51.510858, 7.463873);
        createMarker(51.511858, 7.463123);
        createMarker(51.510128, 7.463873);

        //init center & Zoom
        mapController.setCenter(new GeoPoint(51.518035, 7.459180));
        mapController.setZoom(16.5);

        //init center to location
        GpsMyLocationProvider gmlp = new GpsMyLocationProvider(ctx);
        this.mLocationOverlay = new MyLocationNewOverlay(gmlp,map);
        this.mLocationOverlay.enableMyLocation();
        this.mLocationOverlay.enableFollowLocation();
        map.getOverlays().add(this.mLocationOverlay);

        this.mLocationOverlay.enableMyLocation();
        this.mLocationOverlay.enableFollowLocation();
        //center to location after load of gps data
        mLocationOverlay.runOnFirstFix(new Runnable() {
            @Override
            public void run() {
                final GeoPoint myLocation = mLocationOverlay.getMyLocation();
                if (myLocation != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            map.getController().animateTo(myLocation);
                        }
                    });
                }
                ;
            }
        });

        //Api Request

        //set img
        //URL url = new URL("http://localhost/img/sure/DAF-Hauptbahnhof-2.jpg");
    }

    public void onResume() {
        super.onResume();
        if (map != null)
            map.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    public void onPause() {
        super.onPause();

        if (map != null)
            map.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }

    private void createMarker(double lat, double lan) {
        GeoPoint point = new GeoPoint(lat, lan);
        Marker marker = new Marker(map);
        marker.setPosition(point);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setInfoWindow(null);
        map.getOverlays().add(marker);
        navigateToMarkers();
    }

    public void centerToLocation(View v) {
        final GeoPoint myLocation = mLocationOverlay.getMyLocation();
        if (myLocation != null) {
            map.getController().animateTo(myLocation);
        }
    }

    private void navigateToMarkers(){
        for(int i=0; i<map.getOverlays().size(); i++){
            if(map.getOverlays().get(i) instanceof Marker){
                ((Marker) map.getOverlays().get(i)).setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker, MapView mapView) {
                        openMarkerActivity();
                        getPlaces();
                        return true;
                    }
                });
            }
        }
    }

    private void getPlaces() {
        RequestQueue queue = Volley.newRequestQueue(OsmActivity.this);
        String url = "http://10.0.2.2:8080/places";//emulator nutzt virtual router zum dev device

        // Request a string response from the provided URL.
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        Log.d("Response", response.toString());
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error ", error.toString());
                    }
                }
        );
        queue.add(getRequest);

    }

}