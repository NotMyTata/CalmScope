package com.example.calmscope.Photo;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.calmscope.CalmDatabase.CalmDB;
import com.example.calmscope.CalmDatabase.Entities.Results;
import com.example.calmscope.R;

import java.sql.Date;

public class SadFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sad, container, false);

        CalmDB db = CalmDB.getInstance(getContext());
        SharedPreferences prefs = getContext().getSharedPreferences("com.example.calmscope", Context.MODE_PRIVATE);
        db.resultsDao().insertResult(new Results(
                db.usersDao().getIdByUsername(prefs.getString("currentUser","")),
                db.emotionsDao().findByType("Sad").getId(),
                new java.sql.Date(new java.util.Date().getTime()))
        );

        Button backBtn = view.findViewById(R.id.goBackBtn);
        Button nearYouBtn = view.findViewById(R.id.nearYouBtn);
        ImageView currentPhoto = view.findViewById(R.id.currentPhoto);

        String imageFilePath = requireArguments().getString("imageFilePath");

        Bitmap bm = BitmapFactory.decodeFile(imageFilePath);
        currentPhoto.setImageBitmap(bm);

        nearYouBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment);
                navController.navigate(R.id.mapFragment);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment);
                navController.navigate(R.id.homeFragment);
            }
        } );

        return view;
    }
}
