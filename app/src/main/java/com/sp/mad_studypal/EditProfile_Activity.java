package com.sp.mad_studypal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditProfile_Activity extends AppCompatActivity {
    private Toolbar toolbar;
    private String current_email;
    private EditText user_text;
    private EditText email_text;
    private EditText password_text;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference user_coll_ref = db.collection("User_ID");    //Shortcut

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);

        toolbar = findViewById(R.id.toolbar_profile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        password_text = findViewById(R.id.editText_password);
        user_text = findViewById(R.id.editText_username);
        email_text = findViewById(R.id.editText_email);
        email_text.setFocusable(false);
        email_text.setFocusableInTouchMode(false);
        email_text.setClickable(false);
        email_text.setOnClickListener(popup);

        holder object = new holder(getApplicationContext());
        current_email = object.getVariable();

        user_coll_ref.document(current_email).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {

                            String storedPassword = documentSnapshot.getString("password");  //Pull Password
                            String storedUsername = documentSnapshot.getString("username");
                            email_text.setText(current_email);
                            password_text.setText(storedPassword);
                            user_text.setText(storedUsername);
                        } else {
                            Toast.makeText(getApplicationContext(), "Error -No record", Toast.LENGTH_SHORT).show();
                        }
                }
        })
                .addOnFailureListener(new OnFailureListener() { //Failure
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error-Unable to pull", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_editprofile, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.save_button) {
            email_text.getText().toString().trim();
            password_text.getText().toString().trim();

            Map<String, Object> note = new HashMap<>();
            note.put("username", user_text.getText().toString().trim());
            note.put("password", password_text.getText().toString().trim());

            user_coll_ref.document(current_email).update(note)    //Pull document with same username from USER_ID collection
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(getApplicationContext(), "Changes saved", Toast.LENGTH_SHORT).show();
                }

            })
                    .addOnFailureListener(new OnFailureListener() { //Failure
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                        }
                    });


        }
        return super.onOptionsItemSelected(item);
    }

    private View.OnClickListener popup = new View.OnClickListener() {    //Reservation button, switch to reservation
        @Override
        public void onClick(View v) {
            Toast.makeText(getApplicationContext(), "Email cannot be changed", Toast.LENGTH_SHORT).show();
        }
    };


}