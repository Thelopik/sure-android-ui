package com.example.sure;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
import java.util.ArrayList;

public class OsmActivity extends AppCompatActivity {
    private MapView map;
    private IMapController mapController;
    private MyLocationNewOverlay mLocationOverlay;
    private ArrayList<Place> globalPlaceArrayList;

    private static final String TAG = "OsmActivity";

    private static final int PERMISSION_REQUEST_CODE = 1;

    private FusedLocationProviderClient fusedLocationClient;

    public void openActivity(View v){
        startActivity(new Intent(this, ListActivity.class));
    }

    public void openInfoX(Place p){
        Intent intent = new Intent(this, InfoActivity.class);

        intent.putExtra("ID", String.valueOf(p.getId()));
        intent.putExtra("NAME", p.getName());
        intent.putExtra("IMG", p.getImg());
        intent.putExtra("TEXT", p.getText());
        intent.putExtra("LAT", String.valueOf(p.getLat()));
        intent.putExtra("LON", String.valueOf(p.getLon()));

        startActivity(intent);
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
        RequestHandler.getPlaces(this, new VolleyCallback(){
            @Override
            public void onSuccess(ArrayList<Place> result) {
                for (Place p : result) {
                    createMarker(p);
                }
                Log.d("Set Marker ", result.toString());
            }
            @Override
            public void onSuccess(Place place) {
            }
        });

        //init center & Zoom
        mapController.setCenter(new GeoPoint(51.518035, 7.459180));
        mapController.setZoom(16.5);

        //init center to location
        GpsMyLocationProvider gmlp = new GpsMyLocationProvider(ctx);
        this.mLocationOverlay = new MyLocationNewOverlay(gmlp,map);
        this.mLocationOverlay.enableMyLocation();
        this.mLocationOverlay.enableFollowLocation();
        map.getOverlays().add(this.mLocationOverlay);

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

    private void createMarker(Place p) {
        GeoPoint point = new GeoPoint(p.getLat(), p.getLon());
        Marker marker = new Marker(map);
        marker.setPosition(point);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setInfoWindow(null);
        Drawable d = ResourcesCompat.getDrawable(getResources(), R.drawable.location, null);
        marker.setIcon(d);
        map.getOverlays().add(marker);
        setOnClickForMarker(marker, p);
    }

    public void centerToLocation(View v) {
        final GeoPoint myLocation = mLocationOverlay.getMyLocation();
        if (myLocation != null) {
            map.getController().animateTo(myLocation);
        }
    }

    private void setOnClickForMarker(Marker marker, Place p){

        marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                openInfoX(p);
                return true;
            }
        });
    }
}