package com.sp.mad_studypal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import android.view.MenuItem;
public class MainActivity extends AppCompatActivity {
    private Scanner_Fragment scanner_fragment;
    private Search_Fragment search_fragment;
    private Profile_Fragment profile_fragment;

    private FragmentManager fragmentManager = getSupportFragmentManager();

    private BottomNavigationView bottom_menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scanner_fragment = new Scanner_Fragment();
        search_fragment = new Search_Fragment();
        profile_fragment = new Profile_Fragment();

        bottom_menu = findViewById(R.id.id_bottomNavigation);
        bottom_menu.setOnItemSelectedListener(bottom_menu_select);
    }
    @Override
    protected void onStart(){
        bottom_menu.setSelectedItemId(R.id.bottom_item_search);   //Default input
        super.onStart();
    }

    NavigationBarView.OnItemSelectedListener bottom_menu_select =  new NavigationBarView.OnItemSelectedListener() {      //Bottom Menu selected
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();

            if (id == R.id.bottom_item_scanner){    //Scanner Tab
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_View,scanner_fragment)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
                return true;
            }
            else if (id == R.id.bottom_item_search){   //Search Tab
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_View,search_fragment)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
                return true;
            }
            else if (id == R.id.bottom_item_profile){   //Profile Tab
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_View,profile_fragment)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
                return true;
            }

            return false;
            //true to display the item as the selected item.
            //false if the item should not be selected.
        }
    };
}