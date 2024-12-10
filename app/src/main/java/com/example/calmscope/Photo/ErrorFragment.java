package com.example.calmscope.Photo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.calmscope.R;

public class ErrorFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment);
        try {
            wait(250);
            navController.navigate(R.id.homeFragment);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        return inflater.inflate(R.layout.fragment_error, container, false);
    }
}
