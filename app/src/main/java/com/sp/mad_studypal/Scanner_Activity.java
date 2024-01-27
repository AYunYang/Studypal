package com.sp.mad_studypal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Scanner_Activity extends AppCompatActivity {
    private BottomNavigationView bottom_menu;
    private String current_email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        bottom_menu = findViewById(R.id.id_bottomNavigation);
        bottom_menu.setOnItemSelectedListener(bottom_menu_select);

        MenuItem item = bottom_menu.getMenu().findItem(R.id.bottom_item_scanner);   //Set the profile tab selected
        item.setChecked(true);

        holder object = new holder(getApplicationContext());
        current_email = object.getVariable();
    }

    protected void onStart(){
        bottom_menu.setSelectedItemId(R.id.bottom_item_scanner);   //Default input
        super.onStart();
    }

    NavigationBarView.OnItemSelectedListener bottom_menu_select =  new NavigationBarView.OnItemSelectedListener() {      //Bottom Menu selected
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();

            if (id == R.id.bottom_item_scanner){    //Scanner Tab
                return false;
            }
            else if (id == R.id.bottom_item_search){   //Search Tab
                Intent intent = new Intent(Scanner_Activity.this, Search_Activity.class);
                startActivity(intent);
                return false;
            }
            else if (id == R.id.bottom_item_profile){   //Profile Tab
                Intent intent = new Intent(Scanner_Activity.this, Profile_Activity.class);
                startActivity(intent);
                return false;
            }

            return false;
            //true to display the item as the selected item.
            //false if the item should not be selected.
        }
    };
}