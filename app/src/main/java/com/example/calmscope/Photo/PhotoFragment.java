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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

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

import com.example.calmscope.Maps.MapsFragment;
import com.example.calmscope.R;
import com.google.android.gms.maps.SupportMapFragment;

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
        ImageView currentPhoto = view.findViewById(R.id.placeholderPhoto);

        if (ContextCompat.checkSelfPermission(this.requireContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
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
                NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment);
                Bundle bundle = new Bundle();
                bundle.putString("imageFilePath", namaFile);
                navController.navigate(R.id.loadJsonFragment, bundle);
            }
        });


        // Inflate the layout for this fragment
        return view;
    }
}