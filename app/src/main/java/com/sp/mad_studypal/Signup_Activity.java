package com.sp.mad_studypal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

public class Signup_Activity extends AppCompatActivity {
    private TextView button_switch_to_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow); //Set the icon to back_arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        button_switch_to_login = findViewById(R.id.button_switch4_id);
        button_switch_to_login.setOnClickListener(switch_login);
    }

    private View.OnClickListener switch_login = new View.OnClickListener() {     //Button, switch to login page
        @Override
        public void onClick(View v) {
            startActivity(new Intent(Signup_Activity.this, Login_Activity.class));
        }
    };

}