package com.example.calmscope;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.calmscope.Photo.PhotoFragment;
import com.example.calmscope.Home.HomeFragment;
import com.example.calmscope.Log.LogFragment;

public class NavAccessorAdapter extends FragmentPagerAdapter {

    public NavAccessorAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public NavAccessorAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0: return new HomeFragment();
            case 1: return new PhotoFragment();
            case 2: return new LogFragment();
            default: return null; // TODO: resolve this by making a null fragment or something
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position){
        switch(position){
            case 0: return "Home";
            case 1: return "Photo";
            case 2: return "Log";
            default: return null;
        }
    }
}
