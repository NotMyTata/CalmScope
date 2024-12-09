package com.example.calmscope.Photo;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.calmscope.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.Date;
import java.util.Objects;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class PhotoFragment extends Fragment {
    String namaFile = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo, container, false);
        Button newPhotoBtn = view.findViewById(R.id.newPhotoBtn);
        Button analyzeBtn = view.findViewById(R.id.analyzeBtn);
        ImageView currentPhoto = view.findViewById(R.id.currentPhoto);
        TextView confidenceTxt = view.findViewById(R.id.confidenceTxt);
        TextView classTxt = view.findViewById(R.id.classTxt);

        if (ContextCompat.checkSelfPermission(this.requireContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Request the permission
            ActivityCompat.requestPermissions(this.requireActivity(), new String[]{Manifest.permission.CAMERA}, 1);
        }

        ActivityResultLauncher<Intent> takePictureLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Bitmap bm = BitmapFactory.decodeFile(namaFile, new BitmapFactory.Options());
                currentPhoto.setImageBitmap(bm);

                Toast.makeText(this.getContext(), "Image captured successfully!", Toast.LENGTH_LONG).show();
            }
        });

        newPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File imagesFolder = new File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "CalmScope");
                if (!imagesFolder.exists()) {
                    imagesFolder.mkdirs();
                }
                Date d = new Date();
                namaFile = imagesFolder.getAbsolutePath() + File.separator + DateFormat.format("MM-dd-yy_hh-mm-ss", d.getTime()) + ".jpg";
                File file = new File(namaFile);
                Uri uriSavedImage = FileProvider.getUriForFile(view.getContext(), requireContext().getPackageName() + ".provider", file);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
                takePictureLauncher.launch(intent);
            }
        });

        analyzeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (namaFile.isEmpty()) {
                    Toast.makeText(requireContext(), "No photo to analyze!", Toast.LENGTH_SHORT).show();
                    return;
                }

                new Thread(() -> {
                    try {
                        File file = new File(namaFile);

                        // Base64 Encode the image
                        String encodedFile = "";
                        FileInputStream fileInputStreamReader = new FileInputStream(file);
                        byte[] bytes = new byte[(int) file.length()];
                        fileInputStreamReader.read(bytes);
                        fileInputStreamReader.close();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            encodedFile = Base64.getEncoder().encodeToString(bytes);
                        }

                        // Define API Key and Endpoint
                        String API_KEY = "gHAEteoBBc8llY5NZkgV"; // Replace with your API key
                        String MODEL_ENDPOINT = "calmscope-slnhz"; // Replace with your model endpoint
                        String uploadURL = "https://detect.roboflow.com/calmscope-slnhz/1?api_key=gHAEteoBBc8llY5NZkgV&name=" + file.getName();

                        // Set up the HTTP connection
                        URL url = new URL(uploadURL);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("POST");
                        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                        connection.setRequestProperty("Content-Length", Integer.toString(encodedFile.getBytes().length));
                        connection.setRequestProperty("Content-Language", "en-US");
                        connection.setUseCaches(false);
                        connection.setDoOutput(true);

                        // Send the request
                        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
                        wr.writeBytes(encodedFile);
                        wr.flush();
                        wr.close();

                        // Read the response
                        InputStream stream = connection.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line).append("\n");
                        }
                        reader.close();

                        String jsonResponse = response.toString();
                        JSONObject jsonObject = new JSONObject(jsonResponse);
                        JSONArray predictions = jsonObject.getJSONArray("predictions");
                        if (predictions.length() > 0) {
                            JSONObject firstPrediction = predictions.getJSONObject(0);
                            String detectedClass = firstPrediction.getString("class");
                            double confidence = firstPrediction.getDouble("confidence");

                            // Update the UI with class and confidence
                            requireActivity().runOnUiThread(() -> {
                                classTxt.setText("Class: " + detectedClass);
                                confidenceTxt.setText("Confidence: " + (int) (confidence * 100) + "%");
                                Toast.makeText(requireContext(), "Analysis Complete!", Toast.LENGTH_LONG).show();
                            });
                        } else {
                            requireActivity().runOnUiThread(() ->
                                    Toast.makeText(requireContext(), "No predictions found.", Toast.LENGTH_LONG).show());
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        requireActivity().runOnUiThread(() ->
                                Toast.makeText(requireContext(), "Error during analysis", Toast.LENGTH_LONG).show());
                    }
                }).start();
            }
        });



        // Inflate the layout for this fragment
        return view;
    }
}