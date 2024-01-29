package com.sp.mad_studypal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Splash_Activity extends AppCompatActivity {
    private String current_email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                holder object = new holder(getApplicationContext());
                current_email = object.getVariable();

                if(current_email.equals("Blank")){
                    Intent intent = new Intent(Splash_Activity.this, StartPage_Activity.class);
                    startActivity(intent);
                }

                else {
                    Intent intent = new Intent(Splash_Activity.this, Search_Activity.class);
                    startActivity(intent);
                }

                finish();
            }
        }, 500);   //Delay Timing
    }
}