package com.sp.mad_studypal;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sp.mad_studypal.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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

        // Set the coordinates for Singapore Polytechnic
        LatLng singaporePoly = new LatLng(1.309167, 103.77744);
        LatLng Spectrum = new LatLng(1.3100055122991325, 103.77835545045362);
        LatLng Library_L1 = new LatLng(1.3086498522783239, 103.77970745142775);
        LatLng Library_L2 = new LatLng(1.308525162089529, 103.77999042447183);
        LatLng Hilltop= new LatLng(1.311378286608928, 103.77452458287891);

        // Add a marker for Singapore Polytechnic
        mMap.addMarker(new MarkerOptions().position(Spectrum).title("Spectrum"));
        mMap.addMarker(new MarkerOptions().position(Library_L1).title("Library_L1"));
        mMap.addMarker(new MarkerOptions().position(Library_L2).title("Library_L2"));
        mMap.addMarker(new MarkerOptions().position(Hilltop).title("Hilltop"));


        // Move the camera to Singapore Polytechnic
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(singaporePoly, 16.9f)); // Adjust the zoom level as needed
        mMap.setMaxZoomPreference(16.9f);


        // Add a ground overlay
        GroundOverlayOptions staticMapOverlay = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.spmap))
                .position(singaporePoly, 1032f*1.2f, 639f*1.2f); // Adjust width and height as per your image dimensions

        mMap.addGroundOverlay(staticMapOverlay);
        // Set padding to avoid overlap of Google Maps controls with the static map

    }
}