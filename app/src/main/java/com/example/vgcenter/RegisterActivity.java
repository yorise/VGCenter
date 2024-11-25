package com.example.vgcenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        EditText fUsername = findViewById(R.id.usernameField);
        EditText fPassword = findViewById(R.id.passwordField);
        EditText fConPassword = findViewById(R.id.confirmPasswordField);

//      Register Akun
        findViewById(R.id.createAccountButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = fUsername.getText().toString();
                String password = fPassword.getText().toString();
                String conPassword = fConPassword.getText().toString();

                if (!username.isEmpty() && !password.isEmpty() && !conPassword.isEmpty()) {
                    if (conPassword.equals(password)) {
                        SharedPreferences sp = getSharedPreferences("userAccount", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("username", username);
                        editor.putString("password", password);
                        editor.putString("conPassword", conPassword);
                        editor.apply();

                        Toast.makeText(RegisterActivity.this, "Registration Successfull!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Password and Confirm Password not match!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

