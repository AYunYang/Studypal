package com.sp.mad_studypal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.reflect.Array;

public class Booking1_Activity extends AppCompatActivity {
    private Toolbar toolbar;
    private Button check_seats;
    ArrayList<String> studyarea = new ArrayList<>();
    private DatePickerDialog datePickerDialog;
    private Button datebutton;

    String[] timeslots = {"10am - 12pm","2pm - 4pm"};
    AutoCompleteTextView dropdown_studyarea;
    AutoCompleteTextView dropdown_timeslot;
    ArrayAdapter<String> adapter_studyareas;
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
        adapter_studyareas = new ArrayAdapter<String>(this,R.layout.dropdown_item, studyarea);
        dropdown_studyarea.setAdapter(adapter_studyareas);

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        String today =day+"/"+month+"/"+year;

        datebutton = findViewById(R.id.date_button);
        datebutton.setOnClickListener(showDate);
        datebutton.setText(today);

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = dayOfMonth + "/" + month + "/" + year;
                datebutton.setText(date);
            }
        };

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this,style, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000 );

        dropdown_timeslot = findViewById(R.id.dropdown_timeslot);
        adapter_timeslot = new ArrayAdapter<String>(this,R.layout.dropdown_item, timeslots);
        dropdown_timeslot.setAdapter(adapter_timeslot);

        study_info.document(location).collection("Seat-info").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {   //Loop through and get document name
                                Log.d("HERE FUCKER", document.getId());
                                studyarea.add(document.getId());
                            }

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

    private View.OnClickListener showDate = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            datePickerDialog.show();
        }
    };
}