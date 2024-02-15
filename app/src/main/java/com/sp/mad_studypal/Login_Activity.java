package com.sp.mad_studypal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class Login_Activity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextInputLayout login_username_layout;
    private TextInputLayout login_password_layout;
    private TextInputEditText login_username;
    private TextInputEditText login_password;
    private Button button_login;
    private TextView button_switch_to_signup;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference user_coll_ref = db.collection("User_ID");    //Shortcut

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        toolbar = findViewById(R.id.toolbar_profile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        login_username_layout = findViewById(R.id.id_login_username_layout);
        login_password_layout = findViewById(R.id.id_login_password_layout);
        login_username = findViewById(R.id.id_login_username);
        login_password = findViewById(R.id.id_login_password);

        button_login = findViewById(R.id.button_login_id);
        button_login.setOnClickListener(login);

        button_switch_to_signup = findViewById(R.id.button_switch4_id);
        button_switch_to_signup.setOnClickListener(switch_to_signup);


    }

    private View.OnClickListener switch_to_signup = new View.OnClickListener() {     //Button, switch to signup page
        @Override
        public void onClick(View v) {
            startActivity(new Intent(Login_Activity.this, Signup_Activity.class));
        }
    };

    private View.OnClickListener login = new View.OnClickListener() {    //Login button
        @Override
        public void onClick(View v) {
            String emailStr = login_username.getText().toString().trim();
            String passwordStr = login_password.getText().toString().trim();

            if (emailStr.isEmpty()) {                                 //If username is empty
                login_username_layout.setHelperText("Email Required !");
                login_password_layout.setHelperText(" ");
                return;
            }
            if (passwordStr.isEmpty()) {                                 //If Password is empty
                login_username_layout.setHelperText(" ");
                login_password_layout.setHelperText("Password Required !");
                return;
            } else {
                login_username_layout.setHelperText(" ");
                login_password_layout.setHelperText(" ");

                user_coll_ref.document(emailStr).get()    //Pull document with same username from USER_ID collection
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {  //If managed to pull/Document exist
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {

                                    String storedPassword = documentSnapshot.getString("password");  //Pull Password
                                    String storedUsername = documentSnapshot.getString("username");

                                    if (storedPassword.equals(passwordStr)){     //Correct Password
                                        holder object = new holder(getApplicationContext());
                                        object.saveEmail(emailStr);

                                        cancel_overdued(emailStr);

                                        Toast.makeText(getApplicationContext(), "Welcome back "+storedUsername, Toast.LENGTH_LONG).show();

                                        Intent intent = new Intent(Login_Activity.this, Search_Activity.class);
                                        startActivity(intent);
                                    }

                                    else {          //Incorrect Password
                                        login_password_layout.setHelperText("Incorrect Password");

                                    }
                                }
                                else {
                                    login_username_layout.setHelperText("Account does not exist");
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() { //Failure
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }
    };

    private void cancel_overdued(String user) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy,HH:mm");
        String formattedDateTime = currentDateTime.format(formatter);

        String[] dateAndTime = formattedDateTime.split(",");

        String currentdateString = dateAndTime[0];

        String[] parts = currentdateString.split("-");

        int monthValue = Integer.parseInt(parts[1]) - 1;

        String adjustedDateString = parts[0] + "-" + monthValue + "-" + parts[2];

        String currenttimeString = dateAndTime[1];

        user_coll_ref.document(user).collection("Saved_and_Reservation")
                .document("Reservation")
                .collection("Bookings").get()

                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        // Access each document
                        String bookingId = documentSnapshot.getId();
                        Map<String, Object> data = documentSnapshot.getData();

                        // Access data fields as needed
                        String location = (String) data.get("name");
                        String studyarea = (String) data.get("studyarea");
                        String date = (String) data.get("date");
                        String confirmmstatus = (String) data.get("confirm");

                        String timeslot = (String) data.get("timeslot");
                        String[] splittimeslot = timeslot.split("-");
                        String startTimeString = splittimeslot[0].trim().toLowerCase();
                        String endTimeString = splittimeslot[1].trim().toLowerCase();
                        String complete_seat ="";

                        String  qrcode = (String) data.get("qrcode");
                        if (Integer.parseInt(qrcode) %2 == 0){
                            complete_seat = "Seat2";
                        }
                        else {
                            complete_seat = "Seat1";
                        }

                        LocalDate date_changed = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-M-yyyy"));    //Date of reservation
                        LocalDate current_dated_changed = LocalDate.parse(adjustedDateString, DateTimeFormatter.ofPattern("dd-M-yyyy"));  //Date of current

                        LocalTime startTime = LocalTime.parse(startTimeString, DateTimeFormatter.ofPattern("h:mma"));
                        LocalTime endTime = LocalTime.parse(endTimeString, DateTimeFormatter.ofPattern("h:mma"));
                        LocalTime currentTime = LocalTime.parse(currenttimeString, DateTimeFormatter.ofPattern("HH:mm"));

                        if (current_dated_changed.isAfter(date_changed)){
                            return_seats(location, studyarea, date, timeslot, complete_seat);

                            // Cancel booking from user account
                            db.collection("User_ID")
                                    .document(user)
                                    .collection("Saved_and_Reservation")
                                    .document("Reservation")
                                    .collection("Bookings").document(bookingId).delete();

                        }

                        else if(current_dated_changed.isEqual(date_changed)  && currentTime.isAfter(startTime.plusMinutes(15)) && confirmmstatus.equals("false") ){
                            return_seats(location, studyarea, date, timeslot, complete_seat);

                            // Cancel booking from user account
                            db.collection("User_ID")
                                    .document(user)
                                    .collection("Saved_and_Reservation")
                                    .document("Reservation")
                                    .collection("Bookings").document(bookingId).delete();


                        }
                        else if(current_dated_changed.isEqual(date_changed)  && currentTime.isAfter(endTime)){
                            // Cancel booking from user account
                            db.collection("User_ID")
                                    .document(user)
                                    .collection("Saved_and_Reservation")
                                    .document("Reservation")
                                    .collection("Bookings").document(bookingId).delete();


                        }
                        else {

                        }
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle any potential errors
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                });

    }
    private void return_seats(String location, String studyarea, String date, String timeslot, String seatno) {
        String complete_seat = "Seat"+seatno;
        Map<String, Object> data = new HashMap<>();
        data.put(complete_seat, "empty");

        // Access the document reference and delete it
        db.collection(location)
                .document(studyarea)
                .collection(date)
                .document(timeslot)
                .update(data);
    }
}