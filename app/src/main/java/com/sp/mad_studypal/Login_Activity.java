package com.sp.mad_studypal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class Login_Activity extends AppCompatActivity {
    private TextInputLayout login_username_layout;
    private TextInputLayout login_password_layout;
    private TextInputEditText login_username;
    private TextInputEditText login_password;
    private Button button_login;
    private TextView button_switch_to_signup;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference noteref  = db.document("User_ID/User_1");

    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow); //Set the icon to back_arrow
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
            startActivity(new Intent(Login_Activity.this, StartPage_Activity.class));
        }
    };

    private View.OnClickListener login = new View.OnClickListener() {    //Login button
        @Override
        public void onClick(View v) {
            String usernameStr = login_username.getText().toString().trim();
            String passwordStr = login_password.getText().toString().trim();

            if (usernameStr.isEmpty()){                                 //If username is empty
                login_username_layout.setHelperText("Username Required !");
                login_password_layout.setHelperText(" ");
                return;
            }
            if (passwordStr.isEmpty()){                                 //If Password is empty
                login_username_layout.setHelperText(" ");
                login_password_layout.setHelperText("Password Required !");
                return;
            }

            else {
                login_username_layout.setHelperText(" ");
                login_password_layout.setHelperText(" ");
                login_username.setText("");
                login_password.setText("");



            }

            noteref.get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if(documentSnapshot.exists()){

                                Map<String,Object> user = documentSnapshot.getData();

                                String storedUsername = user.get(KEY_USERNAME).toString().trim();
                                String storedPassword = user.get(KEY_PASSWORD).toString().trim();

                                String enteredUsername = login_username.getText().toString().trim();
                                String enteredPassword = login_password.getText().toString().trim();

                                if (storedUsername.equals(enteredUsername) && storedPassword.equals(enteredPassword)) {
                                    Toast.makeText(getApplicationContext(), "User does exist", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "User does not exist or invalid credentials", Toast.LENGTH_SHORT).show();
                                }

                            }else{
                                Toast.makeText(getApplicationContext(),"User does not exist",Toast.LENGTH_SHORT).show();

                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();

                        }
                    });


        }
    };


}