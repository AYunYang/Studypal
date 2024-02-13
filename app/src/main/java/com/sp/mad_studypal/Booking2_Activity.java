package com.sp.mad_studypal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Booking2_Activity extends AppCompatActivity {
    private Toolbar toolbar;
    private String location;
    private String studyarea;
    private String date;
    private String timeslot;
    private String user;
    private String input_seat;

    private TextView result_location;
    private TextView result_studyarea;
    private TextView result_date;
    private TextView result_timeslot;
    private TextView errSeats;

    private Button book;
    private Button view_seatplan;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<String> seating_list = new ArrayList<>();
    AutoCompleteTextView dropdown_seating;
    ArrayAdapter<String> adapter_seating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking2);

        toolbar = findViewById(R.id.toolbar_profile);  //Toolbar settings
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        holder object = new holder(getApplicationContext());      //Pull information
        location = object.getKeyLocation();
        studyarea = object.getKeyStudyArea();
        date = object.getKeyDate();
        timeslot = object.getKeyTimeSlot();
        user = object.getKeyEmail();

        result_location = findViewById(R.id.result_location);   //Linking textview
        result_studyarea = findViewById(R.id.result_studyarea);
        result_date = findViewById(R.id.result_date);
        result_timeslot = findViewById(R.id.result_timeslot);

        result_location.setText(location);              //Set information as TextView
        result_studyarea.setText(studyarea);
        result_date.setText(date);
        result_timeslot.setText(timeslot);

        errSeats = findViewById(R.id.errSeat);          //For error message

        book = findViewById(R.id.book);                  //Book button
        book.setOnClickListener(booking);

        view_seatplan = findViewById(R.id.view_seats);      //View seat plan button
        view_seatplan.setOnClickListener(view_seating);

        dropdown_seating = findViewById(R.id.dropdown_seat);
        adapter_seating = new ArrayAdapter<String>(this,R.layout.dropdown_item,seating_list);
        dropdown_seating.setAdapter(adapter_seating);

        db.collection(location).document(studyarea).collection(date).document(timeslot).get()  //Pull empty seats
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String Seat_1 = documentSnapshot.getString("Seat1");
                        String Seat_2 = documentSnapshot.getString("Seat2");

                        if (Seat_1.equals("empty")){
                            seating_list.add("Seat1");
                        }

                        if (Seat_2.equals("empty")){
                            seating_list.add("Seat2");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() { //Failure
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Database down", Toast.LENGTH_SHORT).show();
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

            holder object = new holder(getApplicationContext());
            object.saveActivity("2");

            Intent intent = new Intent(Booking2_Activity.this, Info_Activity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private View.OnClickListener booking = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            errSeats.setText(" ");

            input_seat = dropdown_seating.getText().toString();
            if (input_seat.isEmpty()){
                errSeats.setText("Seat number is required");
            }

            else {
                //For daily quota
                db.collection("User_ID").document(user).collection("Saved_and_Reservation").document("quota").collection("quota").document(date).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                   @Override
                                                   public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                       if (task.isSuccessful()) {
                                                           DocumentSnapshot document = task.getResult();
                                                           if (document.exists()) { // Document with date exist


                                                               String hours = document.getString("hours");
                                                               int number = Integer.parseInt(hours);

                                                               if (number == 0){                            //If no more hours left
                                                                   Toast.makeText(getApplicationContext(), "No hours left", Toast.LENGTH_SHORT).show();
                                                                   return;
                                                               }

                                                               else {                                   //Minus off 2hours
                                                                   number = number-2;
                                                                   String results = Integer.toString(number);

                                                                   Map<String, Object> quota = new HashMap<>();
                                                                   quota.put("hours" , results);

                                                                   db.collection("User_ID").document(user).collection("Saved_and_Reservation").document("quota")
                                                                           .collection("quota").document(date).update(quota);

                                                                   booking_info();
                                                               }

                                                           }
                                                           else {     // Document does not exist, add in the default 4hrs per day (Minus off 2)
                                                               Map<String, Object> data_quota = new HashMap<>();
                                                               data_quota.put("hours", "2");

                                                               db.collection("User_ID").document(user).collection("Saved_and_Reservation").document("quota")
                                                                       .collection("quota").document(date).set(data_quota);

                                                               booking_info();
                                                           }
                                                       } else {
                                                           // Error getting the document
                                                           Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                                                       }
                                                   }
                                               });

            }
        }
    };


    void booking_info(){
        //Change to occupied, so others cannot book
        Map<String, Object> data = new HashMap<>();
        data.put(input_seat, "occupied");

        db.collection(location).document(studyarea).collection(date).document(timeslot).update(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {                    //Booking Successful
                        UUID uuid = UUID.randomUUID();
                        String uniqueId = uuid.toString();

                        Map<String, Object> data_in = new HashMap<>();          //Add booking information to user
                        data_in.put("confirm", "false");
                        data_in.put("date", date);
                        data_in.put("name", location);
                        data_in.put("studyarea", studyarea);
                        data_in.put("timeslot", timeslot);
                        data_in.put("qrcode", "12");

                        db.collection("User_ID").document(user).collection("Saved_and_Reservation").document("Reservation").collection("Bookings").document(uniqueId).set(data_in)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getApplicationContext(), "Booked", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(Booking2_Activity.this, Reservation_Activity.class);
                                        startActivity(intent);
                                    }
                                })

                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private View.OnClickListener view_seating = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Booking2_Activity.this, ViewSeat_Activity.class); // for the map button, scnner activity is a place holder
            startActivity(intent);
        }
    };

}