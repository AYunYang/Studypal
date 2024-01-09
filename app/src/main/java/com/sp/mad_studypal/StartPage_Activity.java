package com.sp.mad_studypal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StartPage_Activity extends AppCompatActivity {

    private Button button_switch_to_signup;
    private TextView button_switch_to_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startpage);

        button_switch_to_signup = findViewById(R.id.button_switch1_id);
        button_switch_to_login = findViewById(R.id.button_switch2_id);

        button_switch_to_signup.setOnClickListener(switch_signup);
        button_switch_to_login.setOnClickListener(switch_login);
    }

    private View.OnClickListener switch_signup = new View.OnClickListener() { //Button, switch to signup page
        @Override
        public void onClick(View v) {
            startActivity(new Intent(StartPage_Activity.this, Signup_Activity.class));
        }
    };

    private View.OnClickListener switch_login = new View.OnClickListener() { //Button, switch to login page
        @Override
        public void onClick(View v) {
            startActivity(new Intent(StartPage_Activity.this, Login_Activity.class));
        }
    };

}