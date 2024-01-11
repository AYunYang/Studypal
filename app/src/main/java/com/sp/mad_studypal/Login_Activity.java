package com.sp.mad_studypal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

public class Login_Activity extends AppCompatActivity {
    private TextView button_switch_to_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow); //Set the icon to back_arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        button_switch_to_signup = findViewById(R.id.button_switch4_id);
        button_switch_to_signup.setOnClickListener(switch_login);
    }

    private View.OnClickListener switch_login = new View.OnClickListener() {     //Button, switch to signup page
        @Override
        public void onClick(View v) {
            startActivity(new Intent(Login_Activity.this, StartPage_Activity.class));
        }
    };
}