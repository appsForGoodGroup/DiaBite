package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.ui.CalendarFragment;
import com.example.myapplication.ui.IngredientsFragment;
import com.example.myapplication.ui.RecipeFragment;


import com.example.myapplication.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new CalendarFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.calendar) {
                replaceFragment(new CalendarFragment());
            } else if (id == R.id.ingredients) {
                replaceFragment(new IngredientsFragment());
            } else if (id == R.id.recipes) {
                replaceFragment(new RecipeFragment());
            }
            return true;
        });

    }

    /**
     * This will move between fragments
     * @param fragment the destination the user needs to go to.
     */
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction(); // Corrected
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}