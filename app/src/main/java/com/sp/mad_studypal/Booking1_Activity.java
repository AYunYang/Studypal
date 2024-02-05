package com.sp.mad_studypal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.reflect.Array;

public class Booking1_Activity extends AppCompatActivity {
    private Toolbar toolbar;
    private Button check_seats;
    String[] studyareas = {"StudyArea1","StudyArea2"};
    String[] dates = {"Date1","Date2"};
    String[] timeslots = {"Timeslots_1","Timeslots_2"};
    AutoCompleteTextView dropdown_studyarea;
    AutoCompleteTextView dropdown_date;
    AutoCompleteTextView dropdown_timeslot;
    ArrayAdapter<String> adapter_studyareas;
    ArrayAdapter<String> adapter_date;
    ArrayAdapter<String> adapter_timeslot;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference study_info = db.collection("Study_Area");    //Shortcut
    private String location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking1);

        location = getIntent().getStringExtra("Location");

        toolbar = findViewById(R.id.toolbar_profile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        check_seats = findViewById(R.id.button_check_seats);
        check_seats.setOnClickListener(checkseats);

        dropdown_studyarea = findViewById(R.id.dropdown_studyarea);
        dropdown_date = findViewById(R.id.dropdown_date);
        dropdown_timeslot = findViewById(R.id.dropdown_timeslot);

        adapter_studyareas = new ArrayAdapter<String>(this,R.layout.dropdown_item, studyareas);
        adapter_date = new ArrayAdapter<String>(this,R.layout.dropdown_item, dates);
        adapter_timeslot = new ArrayAdapter<String>(this,R.layout.dropdown_item, timeslots);

        dropdown_studyarea.setAdapter(adapter_studyareas);
        dropdown_date.setAdapter(adapter_date);
        dropdown_timeslot.setAdapter(adapter_timeslot);

        List arrList = new ArrayList(Arrays.asList(studyareas));

        study_info.document(location).collection("Seat-info").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {   //Loop through and get document name
                                Log.d("HERE FUCKER", document.getId());
                                arrList.add(document.getId());
                            }

                            Log.d("HERE FUCKER", arrList.toString());

                        } else {
                            Toast.makeText(getApplicationContext(), "Cannot pull", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_booking, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.info) {
            Intent intent = new Intent(Booking1_Activity.this, Info_Activity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private View.OnClickListener checkseats = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(getApplicationContext(), "Pressed", Toast.LENGTH_SHORT).show();
        }
    };
}