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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Signup_Activity extends AppCompatActivity {
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
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow); //Set the icon to back_arrow
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


                Toast.makeText(getApplicationContext(),"Good",Toast.LENGTH_SHORT).show();

                Map<String,Object> note = new HashMap<>();
                note.put(KEY_USERNAME, usernameStr);
                note.put(KEY_PASSWORD, passwordStr);


                db.collection("User_ID").document("User_1").set(note)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(),"Fail",Toast.LENGTH_SHORT).show();
                            }
                        });

            }




        }
    };

}