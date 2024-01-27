package com.sp.mad_studypal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Profile_Activity extends AppCompatActivity {
    private static String current_email;
    private Button switch_reservation;
    private Button switch_saved;
    private Toolbar toolbar;
    private TextView username;
    private TextView email;
    private BottomNavigationView bottom_menu;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference user_coll_ref = db.collection("User_ID");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        toolbar = findViewById(R.id.toolbar_profile);       //Back button
        setSupportActionBar(toolbar);

        switch_reservation = findViewById(R.id.to_reservation_button);
        switch_reservation.setOnClickListener(switch_to_reservation);

        switch_saved = findViewById(R.id.to_saved_button);
        switch_saved.setOnClickListener(switch_to_saved);

        bottom_menu = findViewById(R.id.id_bottomNavigation);
        bottom_menu.setOnItemSelectedListener(bottom_menu_select);

        MenuItem item = bottom_menu.getMenu().findItem(R.id.bottom_item_profile);   //Set the profile tab selected
        item.setChecked(true);

        holder object = new holder(getApplicationContext());
        current_email = object.getVariable();

        username = findViewById(R.id.profile_username);
        email = findViewById(R.id.profile_email);

        user_coll_ref.document(current_email).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {

                            String storedUsername = documentSnapshot.getString("username");
                            email.setText(current_email);
                            username.setText(storedUsername);

                        } else {
                            Toast.makeText(getApplicationContext(), "Error -No record", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() { //Failure
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error-Unable to pull", Toast.LENGTH_SHORT).show();
                    }
                });



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
            Intent intent = new Intent(Profile_Activity.this, Setting_Activity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    NavigationBarView.OnItemSelectedListener bottom_menu_select =  new NavigationBarView.OnItemSelectedListener() {      //Bottom Menu selected
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();

            if (id == R.id.bottom_item_scanner){    //Scanner Tab
                Intent intent = new Intent(Profile_Activity.this, Scanner_Activity.class);
                startActivity(intent);
                return false;
            }
            else if (id == R.id.bottom_item_search){   //Search Tab
                Intent intent = new Intent(Profile_Activity.this, Search_Activity.class);
                startActivity(intent);
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
            Intent intent = new Intent(Profile_Activity.this, Reservation_Activity.class).putExtra("Current_email",current_email);
            startActivity(intent);
        }
    };

    private View.OnClickListener switch_to_saved = new View.OnClickListener() {    //Saved button, switch to saved
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Profile_Activity.this, Saved_Activity.class).putExtra("Current_email", current_email);
            startActivity(intent);
        }
    };
}