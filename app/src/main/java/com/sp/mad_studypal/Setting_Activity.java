package com.sp.mad_studypal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Setting_Activity extends AppCompatActivity {
    private Toolbar toolbar;
    private Button switch_edit_profile;
    private Button switch_about;
    private Button logout_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        toolbar = findViewById(R.id.toolbar_profile);       //Link the toolbar in xml to actionbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  //For back button, rmb to set in manifest the parent activity


        switch_edit_profile = findViewById(R.id.to_edit_profile);
        switch_edit_profile.setOnClickListener(switch_to_editprofile);

        switch_about = findViewById(R.id.to_about);
        switch_about.setOnClickListener(switch_to_about);

        logout_button = findViewById(R.id.logout_button);
        logout_button.setOnClickListener(logout);
    }

    private View.OnClickListener switch_to_editprofile = new View.OnClickListener() {    //Reservation button, switch to reservation
        @Override
        public void onClick(View v) {
            startActivity(new Intent(Setting_Activity.this, EditProfile_Activity.class));
        }
    };

    private View.OnClickListener switch_to_about = new View.OnClickListener() {    //Reservation button, switch to reservation
        @Override
        public void onClick(View v) {
            startActivity(new Intent(Setting_Activity.this, About_Activity.class));
        }
    };

    private View.OnClickListener logout = new View.OnClickListener() {    //Reservation button, switch to reservation
        @Override
        public void onClick(View v) {
            startActivity(new Intent(Setting_Activity.this, StartPage_Activity.class));
        }
    };
}