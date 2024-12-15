package com.example.calmscope.Maps;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.calmscope.BuildConfig;
import com.example.calmscope.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.SearchNearbyRequest;
import com.google.android.libraries.places.api.model.CircularBounds;

import java.util.Arrays;
import java.util.List;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private PlacesClient placesClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        // Initialize the Places API
        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), BuildConfig.API_KEY);
        }
        placesClient = Places.createClient(requireContext());

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.maps_fragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        // Get user's location
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 12));

                        // Fetch nearby places
                        fetchNearbyPlaces(userLocation);
                    } else {
                        Log.d("Location", "Unable to retrieve location");
                    }
                });
    }

    private void fetchNearbyPlaces(LatLng userLocation) {
        CircularBounds circle = CircularBounds.newInstance(userLocation, /* radius in meters */ 20000);

        // Define place fields
        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);

        // Define included and excluded types
        List<String> includedTypes = Arrays.asList("doctor", "wellness_center");
        List<String> excludedTypes = Arrays.asList("restaurant", "hotel");

        // Build the request
        SearchNearbyRequest searchNearbyRequest = SearchNearbyRequest.builder(circle, placeFields)
                .setIncludedTypes(includedTypes)
                .setExcludedTypes(excludedTypes)
                .setMaxResultCount(10)
                .build();

        // Perform the search
        placesClient.searchNearby(searchNearbyRequest)
                .addOnSuccessListener(response -> {
                    List<Place> places = response.getPlaces();
                    for (Place place : places) {
                        if (place.getLatLng() != null) {
                            mMap.addMarker(new MarkerOptions()
                                    .position(place.getLatLng())
                                    .title(place.getName()));
                        }
                    }
                })
                .addOnFailureListener(e -> Log.e("Places API", "Error fetching places", e));
    }
}
