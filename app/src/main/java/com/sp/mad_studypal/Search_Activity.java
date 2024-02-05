package com.sp.mad_studypal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Search_Activity extends AppCompatActivity {
    private BottomNavigationView bottom_menu;
    private String current_email;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference studyArea_coll_ref = db.collection("Study_Area");

    private RecyclerView StudyAreaList;
    private StudyAreaAdapter adapter;
    private List<StudyArea> studyAreas = new ArrayList<>();//array

    private SearchView searchView;
    private ImageButton mapbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        bottom_menu = findViewById(R.id.id_bottomNavigation);
        bottom_menu.setOnItemSelectedListener(bottom_menu_select);

        MenuItem item = bottom_menu.getMenu().findItem(R.id.bottom_item_search);   //Set the profile tab selected
        item.setChecked(true);

        holder object = new holder(getApplicationContext());
        current_email = object.getVariable();

        StudyAreaList = findViewById(R.id.recyclerview_StudyArea);
        StudyAreaList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StudyAreaAdapter(studyAreas);
        StudyAreaList.setAdapter(adapter);

        // Retrieve data from Firestore
        retrieveDataFromFirestore();

        searchView = findViewById(R.id.search_view);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return false;
            }
        });
        mapbtn = findViewById(R.id.id_mapbtn);
        mapbtn.setOnClickListener(onClickMapbtn);

    }


    private View.OnClickListener onClickMapbtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Search_Activity.this, Map_activity.class); // for the map button
            startActivity(intent);

        }
    };

    private void filterList(String Text) {
        List<StudyArea> filteredList = new ArrayList<>();
        for(StudyArea studyArea : studyAreas){
            if(studyArea.getName().toLowerCase().contains(Text.toLowerCase())){
                filteredList.add(studyArea);
            }
        }
        if(filteredList.isEmpty()){
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        }else{
            adapter.setFilteredList(filteredList);
        }
    }

    protected void onStart(){
        bottom_menu.setSelectedItemId(R.id.bottom_item_search);   //Default input
        super.onStart();
    }
    private void retrieveDataFromFirestore() {
        studyArea_coll_ref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    StudyArea studyArea = documentSnapshot.toObject(StudyArea.class);
                    studyAreas.add(studyArea);//add the data to the arraylist
                }
                adapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Search_Activity.this, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class StudyAreaAdapter extends RecyclerView.Adapter<StudyAreaAdapter.StudyAreaViewHolder> {
        private List<StudyArea> studyAreas;

        public StudyAreaAdapter(List<StudyArea> studyAreas) {
            this.studyAreas = studyAreas;
        }

        public void setFilteredList(List<StudyArea> filteredList){ //for the search
            this.studyAreas = filteredList;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public StudyAreaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { //Inflates the layout for each item in the RecyclerView.
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
            return new StudyAreaViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull StudyAreaViewHolder holder, int position) { //Binds data to the views within each item
            StudyArea studyArea = studyAreas.get(position);
            holder.bind(studyArea);
        }

        @Override
        public int getItemCount() {
            return studyAreas.size();
        } //Returns the total number of study areas.

        public class StudyAreaViewHolder extends RecyclerView.ViewHolder { //Holds references to the views within each item in the RecyclerView.
            private ImageView studyAreaImage;
            private TextView studyAreaName;
            private ImageView studyAreaSaveButton;
            private ImageView icon1;
            private ImageView icon2;
            private TextView studyAreaLocation;
            private boolean isSaved = false;

            public StudyAreaViewHolder(@NonNull View itemView) {
                super(itemView);
                studyAreaImage = itemView.findViewById(R.id.studyarea_image);
                studyAreaName = itemView.findViewById(R.id.studyarea_name);
                studyAreaSaveButton = itemView.findViewById(R.id.studyarea_save_button);
                studyAreaSaveButton.setOnClickListener(onClickSavebutton);
                icon1 = itemView.findViewById(R.id.icon_1);
                icon2 = itemView.findViewById(R.id.icon_2);
                studyAreaLocation = itemView.findViewById(R.id.studyarea_location);


                // Set OnClickListener for the entire item view
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // When the item is clicked, show a toast
                        Toast.makeText(itemView.getContext(), "Placeholder", Toast.LENGTH_SHORT).show();

                    }
                });

            }

            private View.OnClickListener onClickSavebutton = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isSaved) {
                        studyAreaSaveButton.setImageResource(R.drawable.baseline_favorite_24);
                        isSaved = true;
                        String studyAreaName = studyAreas.get(getAdapterPosition()).getName(); // Get the name of the study area associated with this item
                        DocumentReference userDocRef = db.collection("User_ID").document(current_email).collection("Saved_and_Reservation").document("Saved");
                        userDocRef.update("saved_location", FieldValue.arrayUnion(studyAreaName))
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // Update successful
                                        Toast.makeText(Search_Activity.this, "Saved Study Area !", Toast.LENGTH_SHORT).show();

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Handle failures
                                        Toast.makeText(Search_Activity.this, "Error adding !", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        // Change back to the original icon
                        studyAreaSaveButton.setImageResource(R.drawable.baseline_favorite_border_24);
                        isSaved = false;
                        String studyAreaName = studyAreas.get(getAdapterPosition()).getName(); // Get the name of the study area associated with this item

                        DocumentReference userDocRef = db.collection("User_ID").document(current_email).collection("Saved_and_Reservation").document("Saved");
                        userDocRef.update("saved_location",FieldValue.arrayRemove(studyAreaName))
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // Update successful
                                        Toast.makeText(Search_Activity.this, "Removed Study Area !", Toast.LENGTH_SHORT).show();

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Handle failures
                                        Toast.makeText(Search_Activity.this, "Error removing !", Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }
                }

            };

            public void bind(StudyArea studyArea) {
                // Bind data to your views here
                String name = studyArea.getName();
                String location = studyArea.getLocation();

                studyAreaName.setText(name);

                if(location.equals("null")){
                    studyAreaLocation.setText("");
                }else{
                    studyAreaLocation.setText("Near "+ location);
                }

                if(name.equals("Hilltop")){
                    studyAreaImage.setImageResource(R.drawable.hilltop_image);
                    icon1.setImageResource(R.drawable.baseline_air_24);
                    icon2.setImageResource(R.drawable.baseline_charging_station_24);

                } else if (name.equals("Library_L1")) {
                    studyAreaImage.setImageResource(R.drawable.library1);
                    icon1.setImageResource(R.drawable.baseline_air_24);
                    icon2.setImageResource(R.drawable.baseline_invisible);

                } else if (name.equals("Library_L2")) {
                    studyAreaImage.setImageResource(R.drawable.library2);
                    icon1.setImageResource(R.drawable.baseline_air_24);
                    icon2.setImageResource(R.drawable.baseline_charging_station_24);

                }else if(name.equals("Spectrum")){
                    studyAreaImage.setImageResource(R.drawable.spectrum_image);
                    icon1.setImageResource(R.drawable.baseline_charging_station_24);
                    icon2.setImageResource(R.drawable.baseline_invisible);
                }else
                    studyAreaImage.setImageResource(R.drawable.noimage);

                // Check if the study area name exists in the saved_location array
                DocumentReference userDocRef = db.collection("User_ID").document(current_email).collection("Saved_and_Reservation").document("Saved");
                userDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            List<String> savedLocations = (List<String>) documentSnapshot.get("saved_location");
                            if (savedLocations != null && savedLocations.contains(name)) {
                                studyAreaSaveButton.setImageResource(R.drawable.baseline_favorite_24);
                                isSaved = true;
                            } else {
                                studyAreaSaveButton.setImageResource(R.drawable.baseline_favorite_border_24);
                                isSaved = false;
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure
                        Toast.makeText(Search_Activity.this, "Failed to retrieve saved locations", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }
    }


    NavigationBarView.OnItemSelectedListener bottom_menu_select =  new NavigationBarView.OnItemSelectedListener() {      //Bottom Menu selected
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();

            if (id == R.id.bottom_item_scanner){    //Scanner Tab
                Intent intent = new Intent(Search_Activity.this, Scanner_Activity.class);
                startActivity(intent);
                return true;
            }
            else if (id == R.id.bottom_item_search){   //Search Tab
                return true;
            }
            else if (id == R.id.bottom_item_profile){   //Profile Tab
                Intent intent = new Intent(Search_Activity.this, Profile_Activity.class);
                startActivity(intent);
                return true;
            }

            return false;
            //true to display the item as the selected item.
            //false if the item should not be selected.
        }
    };
}