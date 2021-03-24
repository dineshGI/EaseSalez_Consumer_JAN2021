package com.gieasesales.activity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.gieasesales.R;
import com.gieasesales.utils.GpsTracker;
import com.gieasesales.utils.Util;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.LocationComponentOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.OnSymbolClickListener;
import com.mapbox.mapboxsdk.plugins.annotation.OnSymbolDragListener;
import com.mapbox.mapboxsdk.plugins.annotation.OnSymbolLongClickListener;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;

public class PickLocation extends AppCompatActivity implements
        OnMapReadyCallback {
    private MapView mapView;
    // private static final String MAKI_ICON_CAFE = "cafe-15";
    private static final String MAKI_ICON_HARBOR = "harbor-15";
    //private static final String MAKI_ICON_AIRPORT = "airport-15";
    private SymbolManager symbolManager;
    private Symbol symbol;
    private MapboxMap mapboxMap;
    GpsTracker tracker;
    Button getlocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Mapbox access token is configured here. This needs to be called either in your application
        // object or in the same activity which contains the mapview.

        Mapbox.getInstance(this, getString(R.string.access_token));

        // This contains the MapView in XML and needs to be called after the access token is configured.
        setContentView(R.layout.pick_location);
        tracker = new GpsTracker(this);
        mapView = findViewById(R.id.mapView);
        getlocation = findViewById(R.id.getlocation);

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        getlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {

        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                PickLocation.this.mapboxMap = mapboxMap;
                enableLocationComponent(style);

                mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(tracker.getLatitude(), tracker.getLongitude()), 12), 12000);
                // Set up a SymbolManager instance
                symbolManager = new SymbolManager(mapView, mapboxMap, style);

                symbolManager.setIconAllowOverlap(true);
                symbolManager.setTextAllowOverlap(true);

                // Add symbol at specified lat/lon
                symbol = symbolManager.create(new SymbolOptions()
                        .withLatLng(new LatLng(tracker.getLatitude(), tracker.getLongitude()))
                        .withIconImage(MAKI_ICON_HARBOR)
                        //.withIconImage(getResources()getDrawable(R.drawable.ic_add_location_black_24dp))
                        .withIconSize(2.0f)
                        .withDraggable(true));

                // Add click listener and change the symbol to a cafe icon on click
                symbolManager.addClickListener(new OnSymbolClickListener() {
                    @Override
                    public void onAnnotationClick(Symbol symbol) {
                        LatLng latlng = symbol.getLatLng();
                        Toast.makeText(PickLocation.this, Util.getAddress(latlng.getLatitude(), latlng.getLongitude(), PickLocation.this), Toast.LENGTH_SHORT).show();
                        symbol.setIconImage(MAKI_ICON_HARBOR);
                        symbolManager.update(symbol);
                    }
                });

                // Add long click listener and change the symbol to an airport icon on long click
                symbolManager.addLongClickListener((new OnSymbolLongClickListener() {
                    @Override
                    public void onAnnotationLongClick(Symbol symbol) {
                        LatLng latlng = symbol.getLatLng();
                        Toast.makeText(PickLocation.this, Util.getAddress(latlng.getLatitude(), latlng.getLongitude(), PickLocation.this), Toast.LENGTH_SHORT).show();
                        symbol.setIconImage(MAKI_ICON_HARBOR);
                        symbolManager.update(symbol);
                    }
                }));

                symbolManager.addDragListener(new OnSymbolDragListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    // Left empty on purpose
                    public void onAnnotationDragStarted(Symbol annotation) {

                  //      Log.e("LATLNG", String.valueOf(annotation.getLatLng()));

                        LatLng latlng = annotation.getLatLng();
                        Log.e("LAT", String.valueOf(latlng.getLatitude()));
                        Log.e("LNG", String.valueOf(latlng.getLongitude()));
                        Util.saveData("latitude", String.valueOf(latlng.getLatitude()), getApplicationContext());
                        Util.saveData("longitude", String.valueOf(latlng.getLongitude()), getApplicationContext());

                      //  Log.e("LNG", Util.getAddress(latlng.getLatitude(), latlng.getLongitude(), PickLocation.this));
                    }

                    @Override
                    // Left empty on purpose
                    public void onAnnotationDrag(Symbol symbol) {
                    }

                    @Override
                    // Left empty on purpose
                    public void onAnnotationDragFinished(Symbol annotation) {
                    }
                });
                //Toast.makeText(PickLocation.this,Util.getAddress(latlng.getLatitude(),latlng.getLongitude(),PickLocation.this), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
// Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

            LocationComponentOptions customLocationComponentOptions = LocationComponentOptions.builder(this)
                    .pulseEnabled(true)
                    .build();

// Get an instance of the component
            LocationComponent locationComponent = mapboxMap.getLocationComponent();


// Activate with options
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(this, loadedMapStyle)
                            .locationComponentOptions(customLocationComponentOptions)
                            .build());

// Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);

// Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);

// Set the component's render mode
            locationComponent.setRenderMode(RenderMode.NORMAL);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}
