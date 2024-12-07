package com.example.calmscope;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    ViewPager viewPager;
    NavAccessorAdapter navAccessorAdapter;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tabs);

        setViewPager();
        setTabLayout();
    }

    private void setViewPager(){
        navAccessorAdapter = new NavAccessorAdapter(getSupportFragmentManager());
        viewPager.setAdapter(navAccessorAdapter);
    }

    private void setTabLayout(){
        tabLayout.setupWithViewPager(viewPager);
        setTabIcons();
        setTabListener();
    }

    private void setTabIcons(){
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_tab_home);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_tab_photo);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_tab_log);
    }

    private void setTabListener(){
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}