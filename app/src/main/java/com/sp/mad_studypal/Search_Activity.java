package com.sp.mad_studypal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Search_Activity extends AppCompatActivity {
    private BottomNavigationView bottom_menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        bottom_menu = findViewById(R.id.id_bottomNavigation);
        bottom_menu.setOnItemSelectedListener(bottom_menu_select);

        MenuItem item = bottom_menu.getMenu().findItem(R.id.bottom_item_search);   //Set the profile tab selected
        item.setChecked(true);
    }

    protected void onStart(){
        bottom_menu.setSelectedItemId(R.id.bottom_item_search);   //Default input
        super.onStart();
    }

    NavigationBarView.OnItemSelectedListener bottom_menu_select =  new NavigationBarView.OnItemSelectedListener() {      //Bottom Menu selected
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();

            if (id == R.id.bottom_item_scanner){    //Scanner Tab
                startActivity(new Intent(Search_Activity.this, Scanner_Activity.class));
                return true;
            }
            else if (id == R.id.bottom_item_search){   //Search Tab
                return true;
            }
            else if (id == R.id.bottom_item_profile){   //Profile Tab
                startActivity(new Intent(Search_Activity.this, Profile_Activity.class));
                return true;
            }

            return false;
            //true to display the item as the selected item.
            //false if the item should not be selected.
        }
    };
}