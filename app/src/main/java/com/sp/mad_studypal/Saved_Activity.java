package com.sp.mad_studypal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class Saved_Activity extends AppCompatActivity {

    private String current_email;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference savedarea_doc_ref;

    private RecyclerView savedLocationsRecyclerView;
    private SavedLocationAdapter adapter;
    private List<String> savedLocations = new ArrayList<>();
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved);

        holder object = new holder(getApplicationContext());
        current_email = object.getKeyEmail();

        savedarea_doc_ref = db.collection("User_ID").document(current_email)
                .collection("Saved_and_Reservation").document("Saved");

        savedLocationsRecyclerView = findViewById(R.id.saved_view);
        savedLocationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SavedLocationAdapter(savedLocations);
        savedLocationsRecyclerView.setAdapter(adapter);

        // Retrieve saved locations from Firestore
        retrieveSavedLocations();

        toolbar = findViewById(R.id.toolbar_profile);       //Link the toolbar in xml to actionbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void retrieveSavedLocations() {
        savedarea_doc_ref.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            List<String> savedLocationList = (List<String>) documentSnapshot.get("saved_location");
                            if (savedLocationList != null && !savedLocationList.isEmpty()) {
                                savedLocations.addAll(savedLocationList);
                                adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(Saved_Activity.this, "No saved locations found", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Saved_Activity.this, "Document does not exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Saved_Activity.this, "Failed to retrieve saved locations", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void removeItemFromFirestoreAndList(String savedName, int position) {
        // Remove item from Firestore
        savedarea_doc_ref.update("saved_location", FieldValue.arrayRemove(savedName))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Item removed from Firestore, now remove from the list
                        savedLocations.remove(position);
                        // Notify the adapter of the item removal
                        adapter.notifyItemRemoved(position);
                        // Show a toast indicating successful removal
                        Toast.makeText(Saved_Activity.this, "Item removed", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle errors if removal from Firestore fails
                        Toast.makeText(Saved_Activity.this, "Failed to remove item", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private class SavedLocationAdapter extends RecyclerView.Adapter<SavedLocationAdapter.SavedLocationViewHolder> {

        private List<String> savedNames;

        public SavedLocationAdapter(List<String> savedNames) {    // Constructor to initialize the adapter with a list of savedNames

            this.savedNames = savedNames;
        }

        @NonNull
        @Override
        public SavedLocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Inflating the layout for each item in the RecyclerView

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_row, parent, false);
            return new SavedLocationViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SavedLocationViewHolder holder, int position) {
            // Binding data to the ViewHolder

            String savedLocation = savedNames.get(position);
            holder.bind(savedLocation);
        }

        @Override
        public int getItemCount() {
            // Returns the size of the savedNames list
            return savedNames.size();
        }

        public class SavedLocationViewHolder extends RecyclerView.ViewHolder {    // ViewHolder class to hold references to views for each item in the RecyclerView


            private ImageView studyAreaImage;
            private ImageView studyAreaSaveButton;
            private ImageView icon1;
            private ImageView icon2;
            private TextView savedLocationsTextView;
            private TextView savedNameTextView;
            public SavedLocationViewHolder(@NonNull View itemView) {
                super(itemView);
                savedNameTextView = itemView.findViewById(R.id.Saved_area_name);
                savedLocationsTextView = itemView.findViewById(R.id.Saved_area_location);
                studyAreaImage = itemView.findViewById(R.id.studyarea_image);
                studyAreaSaveButton = itemView.findViewById(R.id.studyarea_save_button);
                studyAreaSaveButton.setOnClickListener(onClickSavebutton);
                icon1 = itemView.findViewById(R.id.icon_1);
                icon2 = itemView.findViewById(R.id.icon_2);

            }

            private View.OnClickListener onClickSavebutton = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Get the position of the item clicked
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        String savedName = savedNames.get(position);
                        // Remove the item from Firestore and the list
                        removeItemFromFirestoreAndList(savedName, position);
                    }
                }
            };

            public void bind(String savedName) {
                savedNameTextView.setText(savedName);
                if(savedName.equals("Hilltop") ){
                    studyAreaImage.setImageResource(R.drawable.hilltop_image);
                    icon1.setImageResource(R.drawable.baseline_air_24);
                    icon2.setImageResource(R.drawable.baseline_charging_station_24);
                    savedLocationsTextView.setText("Near MAD");

                } else if (savedName.equals("Library_L1")) {
                    studyAreaImage.setImageResource(R.drawable.library1);
                    icon1.setImageResource(R.drawable.baseline_air_24);
                    icon2.setImageResource(R.drawable.baseline_invisible);

                } else if (savedName.equals("Library_L2")) {
                    studyAreaImage.setImageResource(R.drawable.library2);
                    icon1.setImageResource(R.drawable.baseline_air_24);
                    icon2.setImageResource(R.drawable.baseline_charging_station_24);

                }else if(savedName.equals("Spectrum")){
                    studyAreaImage.setImageResource(R.drawable.spectrum_image);
                    icon1.setImageResource(R.drawable.baseline_charging_station_24);
                    icon2.setImageResource(R.drawable.baseline_invisible);
                    savedLocationsTextView.setText("Near EEE");
                }else
                    studyAreaImage.setImageResource(R.drawable.noimage);
                }
            }
        }
    }
