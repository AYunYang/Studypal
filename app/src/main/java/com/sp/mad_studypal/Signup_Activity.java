package com.sp.mad_studypal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Signup_Activity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextInputLayout signup_username_layout;
    private TextInputLayout signup_email_layout;
    private TextInputLayout signup_password_layout;
    private TextInputLayout signup_cpassword_layout;
    private TextInputEditText signup_username;
    private TextInputEditText signup_email;
    private TextInputEditText signup_cpassword;
    private TextInputEditText signup_password;
    private Button button_signup;
    private TextView button_switch_to_login;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference user_coll_ref = db.collection("User_ID");    //Shortcut

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        toolbar = findViewById(R.id.toolbar_profile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        signup_username_layout = findViewById(R.id.id_signup_username_layout);
        signup_email_layout = findViewById(R.id.id_signup_email_layout);
        signup_password_layout = findViewById(R.id.id_signup_password_layout);
        signup_cpassword_layout = findViewById(R.id.id_signup_cpassword_layout);

        signup_username = findViewById(R.id.id_signup_username);
        signup_email = findViewById(R.id.id_signup_email);
        signup_password = findViewById(R.id.id_signup_password);
        signup_cpassword = findViewById(R.id.id_signup_cpassword);

        button_signup = findViewById(R.id.button_signup_id);
        button_signup.setOnClickListener(signup);

        button_switch_to_login = findViewById(R.id.button_switch4_id);
        button_switch_to_login.setOnClickListener(switch_login);


    }

    private View.OnClickListener switch_login = new View.OnClickListener() {     //Button, switch to login page
        @Override
        public void onClick(View v) {
            startActivity(new Intent(Signup_Activity.this, Login_Activity.class));
        }
    };

    private View.OnClickListener signup = new View.OnClickListener() {     // Signup button
        @Override
        public void onClick(View v) {

            String usernameStr = signup_username.getText().toString().trim();
            String emailStr = signup_email.getText().toString().trim();
            String passwordStr = signup_password.getText().toString().trim();
            String cpasswordStr = signup_cpassword.getText().toString().trim();


            if (usernameStr.isEmpty()){                                 //If username is empty
                signup_username_layout.setHelperText("Username Required !");
                signup_email_layout.setHelperText(" ");
                signup_password_layout.setHelperText(" ");
                signup_cpassword_layout.setHelperText(" ");
                return;
            }
            if (emailStr.isEmpty()){                                 //If Email is empty
                signup_username_layout.setHelperText(" ");
                signup_email_layout.setHelperText("Email Required !");
                signup_password_layout.setHelperText(" ");
                signup_cpassword_layout.setHelperText(" ");
                return;
            }

            if (passwordStr.isEmpty()){                                 //If Password is empty
                signup_username_layout.setHelperText(" ");
                signup_email_layout.setHelperText(" ");
                signup_password_layout.setHelperText("Password Required !");
                signup_cpassword_layout.setHelperText(" ");
                return;
            }
            if (!cpasswordStr.equals(passwordStr)){                                 //If Confirm Password is not equal to Password
                signup_username_layout.setHelperText(" ");
                signup_email_layout.setHelperText(" ");
                signup_password_layout.setHelperText(" ");
                signup_cpassword_layout.setHelperText("Password must be the same");
                return;
            }

            else {                                                                  //If all inputs are valid
                signup_username_layout.setHelperText(" ");
                signup_email_layout.setHelperText(" ");
                signup_password_layout.setHelperText(" ");
                signup_cpassword_layout.setHelperText(" ");

                signup_username.setText("");
                signup_email.setText("");
                signup_password.setText("");
                signup_cpassword.setText("");

                Map<String,Object> saved = new HashMap<>();
                List<String> savedLocations = new ArrayList<>();        //Create empty array
                saved.put("saved_location",savedLocations);
                user_coll_ref.document(emailStr).collection("Saved_and_Reservation").document("Saved").set(saved);

                Map<String,Object> note = new HashMap<>();
                note.put("username", usernameStr);
                note.put("password", passwordStr);

                user_coll_ref.document(emailStr).set(note)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(),"Account created ",Toast.LENGTH_SHORT).show();

                                holder object = new holder(getApplicationContext());
                                object.saveEmail(emailStr);

                                Intent intent = new Intent(Signup_Activity.this, Search_Activity.class);
                                startActivity(intent);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(),"Account not made !",Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        }
    };
}