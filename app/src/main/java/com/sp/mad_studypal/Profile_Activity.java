package com.sp.mad_studypal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Profile_Activity extends AppCompatActivity {
    private Button switch_reservation;
    private Button switch_saved;
    private Toolbar toolbar;
    private BottomNavigationView bottom_menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        toolbar = findViewById(R.id.toolbar_profile);
        setSupportActionBar(toolbar);

        switch_reservation = findViewById(R.id.to_reservation_button);
        switch_reservation.setOnClickListener(switch_to_reservation);

        switch_saved = findViewById(R.id.to_saved_button);
        switch_saved.setOnClickListener(switch_to_saved);

        bottom_menu = findViewById(R.id.id_bottomNavigation);
        bottom_menu.setOnItemSelectedListener(bottom_menu_select);

        MenuItem item = bottom_menu.getMenu().findItem(R.id.bottom_item_profile);   //Set the profile tab selected
        item.setChecked(true);
    }

    @Override
    protected void onStart(){
        bottom_menu.setSelectedItemId(R.id.bottom_item_profile);   //Default input
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_profile, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.settings){
            startActivity(new Intent(Profile_Activity.this, Setting_Activity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    NavigationBarView.OnItemSelectedListener bottom_menu_select =  new NavigationBarView.OnItemSelectedListener() {      //Bottom Menu selected
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();

            if (id == R.id.bottom_item_scanner){    //Scanner Tab
                startActivity(new Intent(Profile_Activity.this, Scanner_Activity.class));
                return false;
            }
            else if (id == R.id.bottom_item_search){   //Search Tab
                startActivity(new Intent(Profile_Activity.this, Search_Activity.class));
                return false;
            }
            else if (id == R.id.bottom_item_profile){   //Profile Tab
                return false;
            }

            return false;
            //true to display the item as the selected item.
            //false if the item should not be selected.
        }
    };

    private View.OnClickListener switch_to_reservation = new View.OnClickListener() {    //Reservation button, switch to reservation
        @Override
        public void onClick(View v) {
            startActivity(new Intent(Profile_Activity.this, Reservation_Activity.class));
        }
    };

    private View.OnClickListener switch_to_saved = new View.OnClickListener() {    //Saved button, switch to saved
        @Override
        public void onClick(View v) {
            startActivity(new Intent(Profile_Activity.this, Saved_Activity.class));
        }
    };


}