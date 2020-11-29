package com.mkg.orderfood.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mkg.foodorder.R;
import com.mkg.orderfood.Constants;
import com.mkg.orderfood.customviews.UbuntuTextView;

import static com.mkg.orderfood.Constants.RESULT_MAPS;

public class LocationActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap googleMap;
    private LocationManager locationManager;
    private UbuntuTextView utvSelectLocation;
    public Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.location_activity);

        utvSelectLocation = findViewById(R.id.utvSelectLocation);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);



        if(getPermissions()){

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            checkLocationAccess();
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
             SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                     .findFragmentById(R.id.map);
             mapFragment.getMapAsync(this);
         } else{
            getPermissions();
         }


        utvSelectLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(location != null){
                    Intent intent = new Intent();
                    intent.putExtra("latitude", location.getLatitude());
                    intent.putExtra("longitude", location.getLongitude());
                    Constants.CURRENT_LAT = location.getLatitude();
                    Constants.CURRENT_LONG = location.getLongitude();
                    setResult(RESULT_MAPS, intent);
                    finish();
                }
            }
        });


    }

    private void checkLocationAccess(){
        boolean gpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(!gpsStatus){
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setIcon(getResources().getDrawable(R.drawable.location_fill))
                    .setTitle("Location Access")
                    .setMessage("Location is important to get all restaurants near you. You will be redirected to location screen, please turn on the location")
                    .setPositiveButton("Open", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    }).setNegativeButton("Cancel", null);
            builder.create().show();
        }
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
    public void onMapReady(final GoogleMap gMap) {

        if(getPermissions()){

            this.googleMap = gMap;
            this.googleMap.getUiSettings().setZoomControlsEnabled(true);

            this.googleMap.setMyLocationEnabled(true);
            // Add a marker at user's current location and move the camera
            if (this.googleMap != null) {
                this.googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

                    @Override
                    public void onMyLocationChange(Location loc) {
                        LatLng currentLocation = new LatLng(loc.getLatitude(), loc.getLongitude());
                        googleMap.clear();
                        googleMap.addMarker(new MarkerOptions().position(currentLocation).title("Your Location"));
                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
                        location = loc;
                    }
                });
            }

            if(this.googleMap != null){
                this.googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                    @Override
                    public boolean onMyLocationButtonClick() {
                        checkLocationAccess();
                        return true;
                    }
                });
            }

        }

    }


    private boolean getPermissions(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return true;
        } else {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},1);
        }
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        if(getPermissions() && googleMap != null){
            this.location = location;
            googleMap.clear();
            LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
            googleMap.addMarker(new MarkerOptions().position(currentLocation).title("Your Location"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}