package com.sp.mad_studypal;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class Reservation_Activity extends AppCompatActivity {
    private String current_email;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference reservationCollectionRef;

    private RecyclerView resLocationsRecyclerView;
    private ResLocationAdapter adapter;
    private List<ReservationModel> resLocations = new ArrayList<>();
    private Toolbar toolbar;
    private String bookingId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        toolbar = findViewById(R.id.toolbar_profile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        holder object = new holder(getApplicationContext());
        current_email = object.getKeyEmail();
        reservationCollectionRef = db.collection("User_ID")
                .document(current_email)
                .collection("Saved_and_Reservation")
                .document("Reservation")
                .collection("Bookings");

        resLocationsRecyclerView = findViewById(R.id.reservation_view);
        resLocationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ResLocationAdapter(resLocations);
        resLocationsRecyclerView.setAdapter(adapter);

        // Retrieve saved locations from Firestore
        retrieveResLocations();
    }

    private void retrieveResLocations() {
        reservationCollectionRef.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        // Access each document
                        bookingId = documentSnapshot.getId();
                        Map<String, Object> data = documentSnapshot.getData();

                        // Access data fields as needed
                        String name = (String) data.get("name");
                        String date = (String) data.get("date");
                        String time = (String) data.get("timeslot");
                        String confirmStatus = (String) data.get("confirm");
                        String qrcode =(String) data.get("qrcode");
                        String booking_id = bookingId;

                        ReservationModel reservation = new ReservationModel(name, date, time,confirmStatus,qrcode, booking_id);
                        resLocations.add(reservation);
                    }

                    // Notify the adapter that data has changed
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    // Handle any potential errors
                    Toast.makeText(Reservation_Activity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    public class ResLocationAdapter extends RecyclerView.Adapter<ResLocationAdapter.ReservationViewHolder> {
        private List<ReservationModel> resLocations;

        public ResLocationAdapter(List<ReservationModel> resLocations) {
            this.resLocations = resLocations;
        }

        @NonNull
        @Override
        public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reservation_row, parent, false);
            return new ReservationViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
            ReservationModel reservation = resLocations.get(position);
            holder.nameTextView.setText(reservation.getName());
            holder.dateTextView.setText(reservation.getDate());
            holder.timeTextView.setText(reservation.getTime());
            holder.bookingId_hidden.setText(reservation.getBooking_id());  //Set the hidden booking id

            if(reservation.getName().equals("Hilltop")){
                holder.studyarea_image.setImageResource(R.drawable.hilltop_image);
            } else if (reservation.getName().equals("Library_L1")) {
                holder.studyarea_image.setImageResource(R.drawable.library1);
            } else if (reservation.getName().equals("Library_L2")) {
                holder.studyarea_image.setImageResource(R.drawable.library2);
            } else if (reservation.getName().equals("Spectrum")) {
                holder.studyarea_image.setImageResource(R.drawable.spectrum_image);
            }else{
                holder.studyarea_image.setImageResource(R.drawable.noimage);
            }

            if(reservation.getConfirmstatus().equals("false")){
                holder.status.setBackgroundColor(Color.parseColor("#F19C1B"));
            }
            if(Integer.parseInt(reservation.getQrcode())%2==0){
                holder.seatNoTextView.setText("2");
            }else
                holder.seatNoTextView.setText("1");
        }

        @Override
        public int getItemCount() {
            return resLocations.size();
        }

        public class ReservationViewHolder extends RecyclerView.ViewHolder {
            private TextView nameTextView;
            private TextView dateTextView;
            private TextView timeTextView;
            private TextView seatNoTextView;
            private ImageView studyarea_image;
            private TextView bookingId_hidden;
            private View status;
            private ImageButton removeresbtn;


            public ReservationViewHolder(@NonNull View itemView) {
                super(itemView);
                nameTextView = itemView.findViewById(R.id.id_name);
                dateTextView = itemView.findViewById(R.id.id_date);
                timeTextView = itemView.findViewById(R.id.id_time);
                seatNoTextView = itemView.findViewById(R.id.id_seat);
                bookingId_hidden = itemView.findViewById(R.id.hidden);
                studyarea_image = itemView.findViewById(R.id.studyarea_image);
                status = itemView.findViewById(R.id.status);
                removeresbtn = itemView.findViewById(R.id.cancel_button);
                removeresbtn.setOnClickListener(onclickcancel);
            }

            private View.OnClickListener onclickcancel = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        // Remove the item from the list
                        resLocations.remove(position);

                        //Get booking id for that item
                        ReservationViewHolder holder = (ReservationViewHolder) resLocationsRecyclerView.findViewHolderForAdapterPosition(position);
                        View itemView = holder.itemView;
                        TextView hidden_textview = itemView.findViewById(R.id.hidden);
                        String booking_id_tmp = hidden_textview.getText().toString();

                        // Update the RecyclerView
                        notifyItemRemoved(position);

                        // Delete the corresponding document from Firestore
                        deleteReservationFromFirestore(booking_id_tmp);
                    }
                }
            };

            // Method to delete the corresponding document from Firestore
            private void deleteReservationFromFirestore(String booking_id) {

                // Access the document reference and delete it
                db.collection("User_ID")
                        .document(current_email)
                        .collection("Saved_and_Reservation")
                        .document("Reservation")
                        .collection("Bookings")
                        .document(booking_id)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(itemView.getContext(), "Reservation deleted successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(itemView.getContext(), "Failed to delete document: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }

    }
}

