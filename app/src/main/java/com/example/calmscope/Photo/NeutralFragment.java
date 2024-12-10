package com.example.calmscope.Photo;

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

import com.example.calmscope.R;

public class NeutralFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result, container, false);

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
