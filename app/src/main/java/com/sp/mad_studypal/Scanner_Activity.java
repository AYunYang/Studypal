package com.sp.mad_studypal;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class Scanner_Activity extends AppCompatActivity {
    private BottomNavigationView bottom_menu;
    private String current_email;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference bookingsCollectionRef;

    private Button btn_scan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        bottom_menu = findViewById(R.id.id_bottomNavigation);
        bottom_menu.setOnItemSelectedListener(bottom_menu_select);

        MenuItem item = bottom_menu.getMenu().findItem(R.id.bottom_item_scanner);   //Set the profile tab selected
        item.setChecked(true);

        holder object = new holder(getApplicationContext());
        current_email = object.getKeyEmail();

        btn_scan = findViewById(R.id.btn_scan);
        btn_scan.setOnClickListener(v -> {scanCode();});

        bookingsCollectionRef = db.collection("User_ID")
                .document(current_email)
                .collection("Saved_and_Reservation")
                .document("Reservation")
                .collection("Bookings");


    }

    protected void onStart(){
        bottom_menu.setSelectedItemId(R.id.bottom_item_scanner);   //Default input
        super.onStart();
    }



    private void scanCode(){
        ScanOptions options = new ScanOptions();
        options.setPrompt("Scan a QRcode");
        options.setBeepEnabled(true);
        options.setBarcodeImageEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(capture_activity.class);
        barLauncher.launch(options);

    }

    //launch the camera scanning
    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if(result.getContents() !=null){
            //handleScanResult(result.getContents());
            updateFirebase(result.getContents());
        }
    });

    private void updateFirebase(String qrcode) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy,HH:mm");
        String formattedDateTime = currentDateTime.format(formatter);

        String[] dateAndTime = formattedDateTime.split(",");

        // Extract the current date and time components
        String currentdateString = dateAndTime[0]; // Contains "dd-mm-yyyy"

        String[] parts = currentdateString.split("-"); // Splitting the current date string
        // Adjust the month value to be 1-based
        int monthValue = Integer.parseInt(parts[1]) - 1; // Incrementing the month value by 1
        // Construct the new date string with the adjusted month value
        String adjustedDateString = parts[0] + "-" + monthValue + "-" + parts[2]; //13-02-2024 will be changed to 13-1-2024 (Both still 13Feb)

        String currenttimeString = dateAndTime[1]; // Contains "hh:mm"

        AtomicBoolean atLeastOneBookingInRange = new AtomicBoolean(false); // Flag to track if at least one booking is in range

        bookingsCollectionRef.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        // Access each document
                        String bookingId = documentSnapshot.getId();
                        Map<String, Object> data = documentSnapshot.getData();

                        // Access data fields as needed
                        String date = (String) data.get("date");
                        String timeslot = (String) data.get("timeslot");
                        String dbqrcode = (String) data.get("qrcode");
                        String[] splittimeslot = timeslot.split("-"); //eg 2pm-4pm -> 2pm and 4pm
                        String startTimeString = splittimeslot[0].trim(); // eg. 2pm
                        String endTimeString = splittimeslot[1].trim(); // eg. 4pm

                        // convert start time into LocalTime objects (if you want to do testing change the date using x:xx)
                        LocalTime startTime = LocalTime.parse(startTimeString, DateTimeFormatter.ofPattern("h:mma")); //2:00-->h:mma 2.00-->h.mma
                        // convert current time into LocalTime object
                        LocalTime currentTime = LocalTime.parse(currenttimeString, DateTimeFormatter.ofPattern("HH:mm"));

                        if (date.equals(adjustedDateString) &&
                                currentTime.isAfter(startTime) &&
                                currentTime.isBefore(startTime.plusMinutes(15)) &&
                                qrcode.equals(dbqrcode)) {
                                // Booking is in the current range

                            AlertDialog.Builder builder = new AlertDialog.Builder(Scanner_Activity.this);
                            builder.setTitle("Result");
                            builder.setMessage("You have Reached");
                            builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss()).show();

                            bookingsCollectionRef.document(bookingId).update("confirm", "true")
                                    .addOnSuccessListener(aVoid -> {
                                        // Update successful
                                        Toast.makeText(Scanner_Activity.this, "Firebase updated successfully", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        // Update failed
                                        Toast.makeText(Scanner_Activity.this, "Firebase update failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });

                            // Set the flag to true as at least one booking is in range
                            atLeastOneBookingInRange.set(true);
                            break; // No need to continue checking other bookings once one is found
                        }
                    }

                    // Check if no booking was in range
                    if (!atLeastOneBookingInRange.get()) {
                        // Current time is not within the booking time range
                        Toast.makeText(Scanner_Activity.this, "Current time is not within the booking time range", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle any potential errors
                    Toast.makeText(Scanner_Activity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


    NavigationBarView.OnItemSelectedListener bottom_menu_select =  new NavigationBarView.OnItemSelectedListener() {      //Bottom Menu selected
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();

            if (id == R.id.bottom_item_scanner){    //Scanner Tab
                return false;
            }
            else if (id == R.id.bottom_item_search){   //Search Tab
                Intent intent = new Intent(Scanner_Activity.this, Search_Activity.class);
                startActivity(intent);
                return false;
            }
            else if (id == R.id.bottom_item_profile){   //Profile Tab
                Intent intent = new Intent(Scanner_Activity.this, Profile_Activity.class);
                startActivity(intent);
                return false;
            }

            return false;
            //true to display the item as the selected item.
            //false if the item should not be selected.
        }
    };
}