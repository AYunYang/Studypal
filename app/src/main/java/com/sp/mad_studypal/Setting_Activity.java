package com.sp.mad_studypal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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

    private View.OnClickListener switch_to_editprofile = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Setting_Activity.this, EditProfile_Activity.class);
            startActivity(intent);
        }
    };

    private View.OnClickListener switch_to_about = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(Setting_Activity.this, About_Activity.class));
        }
    };

    private View.OnClickListener logout = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            holder object = new holder(getApplicationContext());
            object.saveVariable("Blank");

            startActivity(new Intent(Setting_Activity.this, StartPage_Activity.class));
        }
    };
}