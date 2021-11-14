package com.example.viva;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
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

        LatLng Location1 = new LatLng(5.341593113888564, 100.28191361201861);
        mMap.addMarker(new MarkerOptions().position(Location1).title("INTI International Collage Penang"));

        LatLng Location2 = new LatLng(2.8112847935038188, 101.75880799667627);
        mMap.addMarker(new MarkerOptions().position(Location2).title("INTI International University Nilai"));

        LatLng Location3 = new LatLng(3.0744900647368634, 101.59116338133462);
        mMap.addMarker(new MarkerOptions().position(Location3).title("INTI International Collage Subang"));

        LatLng Location4 = new LatLng(5.892895885799898, 116.0467691390075);
        mMap.addMarker(new MarkerOptions().position(Location4).title("INTI Collage Sabah"));

        int location_id = getIntent().getIntExtra("Location",0);

        if(location_id == 1){
            mMap.moveCamera(CameraUpdateFactory.newLatLng(Location1));
        }
        else if(location_id ==2){
            mMap.moveCamera(CameraUpdateFactory.newLatLng(Location2));
        }
        else if(location_id ==3){
            mMap.moveCamera(CameraUpdateFactory.newLatLng(Location3));
        }
        else if(location_id ==4){
            mMap.moveCamera(CameraUpdateFactory.newLatLng(Location4));
        }

    }
}