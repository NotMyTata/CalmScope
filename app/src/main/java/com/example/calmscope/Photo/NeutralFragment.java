package com.example.calmscope.Photo;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

public class NeutralFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result, container, false);

        CalmDB db = CalmDB.getInstance(getContext());
        SharedPreferences prefs = getContext().getSharedPreferences("com.example.calmscope", Context.MODE_PRIVATE);
        db.resultsDao().insertResult(new Results(
                db.usersDao().getIdByUsername(prefs.getString("currentUser","")),
                db.emotionsDao().findByType("Calm").getId(),
                new java.sql.Date(new java.util.Date().getTime()))
        );

        Button backBtn = view.findViewById(R.id.goBackBtn);
        ImageView currentPhoto = view.findViewById(R.id.currentPhoto);

        String imageFilePath = requireArguments().getString("imageFilePath");

        Bitmap bm = BitmapFactory.decodeFile(imageFilePath);
        currentPhoto.setImageBitmap(bm);

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
