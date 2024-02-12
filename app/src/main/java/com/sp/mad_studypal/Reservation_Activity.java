package com.sp.mad_studypal;

import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class Reservation_Activity extends AppCompatActivity {
    private String current_email;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference reservationDocumentRef;

    private RecyclerView resLocationsRecyclerView;
    private ResLocationAdapter adapter;
    private List<ReservationModel> resLocations = new ArrayList<>();
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        toolbar = findViewById(R.id.toolbar_profile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        holder object = new holder(getApplicationContext());
        current_email = object.getVariable();
        reservationDocumentRef = db.collection("User_ID")
                .document(current_email)
                .collection("Saved_and_Reservation")
                .document("Reservation")
                .collection("Bookings").document("Booking_1");//for now is just yy acc booking_1

        resLocationsRecyclerView = findViewById(R.id.reservation_view);
        resLocationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ResLocationAdapter(resLocations);
        resLocationsRecyclerView.setAdapter(adapter);

        // Retrieve saved locations from Firestore
        retrieveResLocations();
    }

    private void retrieveResLocations() {
        reservationDocumentRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Map<String, Object> reservationData = documentSnapshot.getData();
                // Assuming you have reservation data stored as a map under the document

                // Extracting fields from the reservationData map
                String name = (String) reservationData.get("name");
                String date = (String) reservationData.get("date");
                String time = (String) reservationData.get("time");
                Long seatNoLong = (Long) reservationData.get("seat_no");
                int seatNo = seatNoLong != null ? seatNoLong.intValue() : 0;
                boolean comfirmstatus =(boolean) reservationData.get("confirm");

                ReservationModel reservation = new ReservationModel(name, date, time, seatNo,comfirmstatus);
                resLocations.add(reservation);
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "Document does not exist", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Failed to fetch document: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
            holder.seatNoTextView.setText(String.valueOf(reservation.getSeatNo()));
            
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

            if(reservation.getConfirmstatus() == false){
                holder.status.setBackgroundColor(Color.parseColor("#F19C1B"));
            }
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
            private View status;
            private ImageButton removeresbtn;


            public ReservationViewHolder(@NonNull View itemView) {
                super(itemView);
                nameTextView = itemView.findViewById(R.id.id_name);
                dateTextView = itemView.findViewById(R.id.id_date);
                timeTextView = itemView.findViewById(R.id.id_time);
                seatNoTextView = itemView.findViewById(R.id.id_seat);
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
                        // Update the RecyclerView
                        notifyItemRemoved(position);
                        // Delete the corresponding document from Firestore
                        deleteReservationFromFirestore();
                    }
                }
            };

            // Method to delete the corresponding document from Firestore
            private void deleteReservationFromFirestore() {

                // Access the document reference and delete it
                db.collection("User_ID")
                        .document(current_email)
                        .collection("Saved_and_Reservation")
                        .document("Reservation")
                        .collection("Bookings")
                        .document("Booking_1") //this must change "booking_1"
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(itemView.getContext(), "Document deleted successfully", Toast.LENGTH_SHORT).show();
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

