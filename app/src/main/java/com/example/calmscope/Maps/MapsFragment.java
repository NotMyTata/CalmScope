package com.example.calmscope.Maps;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.calmscope.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

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
        // Get the user's location
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 12));
                        fetchNearbyPlaces(userLocation);
                        Log.d("Location", "location is zooming");
                    } else {
                        Log.d("Location", "location is not zooming");
                    }
                });

    }

    private void fetchNearbyPlaces(LatLng userLocation) {
        String apiKey = "AIzaSyBjN3UoOG_hV6TWXALl6XMnBYZaLdCUSDE";
        String url = "https://places.googleapis.com/v1/places:searchNearby";

        // JSON Payload
        String jsonPayload = "{\n" +
                "  \"includedTypes\": [\"consultant\", \"therapist\", \"psychologist\", \"mental health counselor\", \"depression specialist\"],\n" +                "  \"maxResultCount\": 10,\n" +
                "  \"locationRestriction\": {\n" +
                "    \"circle\": {\n" +
                "      \"center\": {\n" +
                "        \"latitude\": " + userLocation.latitude + ",\n" +
                "        \"longitude\": " + userLocation.longitude + "\n" +
                "      },\n" +
                "      \"radius\": 500.0\n" +
                "    }\n" +
                "  }\n" +
                "}";

        new Thread(() -> {
            try {
                // Setup the connection
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("X-Goog-Api-Key", apiKey);
                connection.setRequestProperty("X-Goog-FieldMask", "places.displayName");

                // Write the JSON payload
                connection.getOutputStream().write(jsonPayload.getBytes("UTF-8"));

                // Read the response
                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                Log.d("JSON Response", response.toString());

                // Parse and update the map
                JSONObject jsonObject = new JSONObject(response.toString());
                JSONArray places = jsonObject.getJSONArray("places");
                requireActivity().runOnUiThread(() -> {
                    for (int i = 0; i < places.length(); i++) {
                        try {
                            JSONObject place = places.getJSONObject(i);
                            String displayName = place.getString("displayName");
                            JSONObject center = place.getJSONObject("location").getJSONObject("center");
                            LatLng placeLatLng = new LatLng(center.getDouble("latitude"), center.getDouble("longitude"));

                            // Add a marker for each place
                            mMap.addMarker(new MarkerOptions()
                                    .position(placeLatLng)
                                    .title(displayName));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }


}
