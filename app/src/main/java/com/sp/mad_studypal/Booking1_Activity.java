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
import android.widget.TextView;
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

    String[] timeslots = {"10:00AM - 12:00PM","2:00PM - 4:00PM"};
    AutoCompleteTextView dropdown_studyarea;
    AutoCompleteTextView dropdown_timeslot;
    ArrayAdapter<String> adapter_studyareas;
    ArrayAdapter<String> adapter_timeslot;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference study_info = db.collection("Study_Area");    //Shortcut
    private String pull_location;

    private TextView errStudyArea;
    private TextView errDate;
    private TextView errTimeslot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking1);

        toolbar = findViewById(R.id.toolbar_profile);           //Toolbar settings
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        holder object = new holder(getApplicationContext());   //Pull Current location
        pull_location = object.getKeyLocation();

        check_seats = findViewById(R.id.button_check_seats);    //For check seats button
        check_seats.setOnClickListener(checkseats);

        errStudyArea = findViewById(R.id.errStudyArea);         //For error message
        errDate = findViewById(R.id.errDate);
        errTimeslot = findViewById(R.id.errTimeSlot);

        dropdown_studyarea = findViewById(R.id.dropdown_studyarea);             //Dropdown for studyarea
        adapter_studyareas = new ArrayAdapter<String>(this,R.layout.dropdown_item, studyarea);
        dropdown_studyarea.setAdapter(adapter_studyareas);

        Calendar cal = Calendar.getInstance();          //For the date button
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        String today =day+"-"+month+"-"+year;

        datebutton = findViewById(R.id.date_button);
        datebutton.setOnClickListener(showDate);
        datebutton.setText(today);

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = dayOfMonth + "-" + month + "-" + year;
                datebutton.setText(date);
            }
        };

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this,style, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000 );

        dropdown_timeslot = findViewById(R.id.dropdown_timeslot);                                               //Dropdown for timeslot
        adapter_timeslot = new ArrayAdapter<String>(this,R.layout.dropdown_item, timeslots);
        dropdown_timeslot.setAdapter(adapter_timeslot);

        study_info.document(pull_location).collection("Seat-info").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {   //Loop through and get document name
                                studyarea.add(document.getId());
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



    }

    private View.OnClickListener checkseats = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Pull study area selected
            String input_studyarea = dropdown_studyarea.getText().toString();
            String input_date = datebutton.getText().toString();
            String input_timeslot = dropdown_timeslot.getText().toString();

            errStudyArea.setText(" ");
            errDate.setText(" ");
            errTimeslot.setText(" ");

            if (input_studyarea.isEmpty()){
                errStudyArea.setText("Study Area required");
            }

            else if (input_timeslot.isEmpty()){
                errTimeslot.setText("Time Slot required ");
            }

            else {
                db.collection(pull_location).document(input_studyarea).collection(input_date).get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                Intent intent = new Intent(Booking1_Activity.this, Booking2_Activity.class);

                                holder object = new holder(getApplicationContext());
                                object.saveStudyArea(input_studyarea);
                                object.saveDate(input_date);
                                object.saveTimeSlot(input_timeslot);

                                if (!queryDocumentSnapshots.isEmpty()) {                                                //If date is created already
                                    startActivity(intent);

                                } else {                                                                                //If date is not created yet, create the document

                                    Map<String, Object> data = new HashMap<>();
                                    data.put("Seat1", "empty");
                                    data.put("Seat2", "empty");

                                    db.collection(pull_location).document(input_studyarea).collection(input_date).document("10:00AM - 12:00PM").set(data);
                                    db.collection(pull_location).document(input_studyarea).collection(input_date).document("2:00PM - 4:00PM").set(data);

                                    startActivity(intent);
                                }
                            }
                        })

                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }
    };

    private View.OnClickListener showDate = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            datePickerDialog.show();
        }
    };
}