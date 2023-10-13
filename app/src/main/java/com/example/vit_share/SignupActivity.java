package com.example.vit_share;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.vit_share.databinding.ActivitySignupBinding;

public class SignupActivity extends AppCompatActivity {

    ActivitySignupBinding binding;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Instantiate the database helper
        databaseHelper = new DatabaseHelper(this);

        // Set up the click listener for the sign up button
        binding.signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the email, password, and confirm password values entered by the user
                String email = binding.signupEmail.getText().toString();
                String password = binding.signupPassword.getText().toString();
                String confirmPassword = binding.signupConfirm.getText().toString();

                // Check if any of the fields are empty, and show an error message if so
                if (email.equals("") || password.equals("") || confirmPassword.equals("")) {
                    Toast.makeText(SignupActivity.this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
                } else {
                    // Check if the password and confirm password fields match, and show an error message if they don't
                    if (password.equals(confirmPassword)) {
                        // Check if the email entered by the user already exists in the database
                        Boolean checkUserEmail = databaseHelper.checkUserEmail(email);

                        if (checkUserEmail == false) {
                            // If the email doesn't already exist in the database, insert the user's information into the database
                            Boolean insert = databaseHelper.insertUser(email, password);

                            if (insert == true) {
                                // If the data is successfully inserted into the database, show a success message and redirect the user to the login page
                                Toast.makeText(SignupActivity.this, "SignUp Success!", Toast.LENGTH_SHORT).show();
                                Intent intent =  new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                            } else {
                                // If the data is not successfully inserted into the database, show an error message
                                Toast.makeText(SignupActivity.this, "SignUp Failure!", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            // If the email already exists in the database, show an error message
                            Toast.makeText(SignupActivity.this, "User already exists", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // If the password and confirm password fields don't match, show an error message
                        Toast.makeText(SignupActivity.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // Set up the click listener for the "Already have an account? Log in" text
        binding.loginRedirectText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
