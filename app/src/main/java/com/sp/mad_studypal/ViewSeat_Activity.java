package com.sp.mad_studypal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.widget.Toolbar;

public class ViewSeat_Activity extends AppCompatActivity {
    private Toolbar toolbar;
    private String pull_location;
    private ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewseat);

        toolbar = findViewById(R.id.toolbar_profile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        image = findViewById(R.id.image_of_seats);

        holder object = new holder(getApplicationContext());   //Pull Current location
        pull_location = object.getKeyLocation();

        if (pull_location.equals("Hilltop")){
            image.setImageResource(R.drawable.location1);
        }
        else if (pull_location.equals("Library_L1")){
            image.setImageResource(R.drawable.location2);
        }
        else if (pull_location.equals("Library_L2")){
            image.setImageResource(R.drawable.location3);
        }
        else{
            image.setImageResource(R.drawable.location4);
        }
}
}