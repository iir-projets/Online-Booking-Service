package com.example.projectservices;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private EditText inputEmail;
    private EditText inputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);

        Button btnLogin = findViewById(R.id.btn);
        btnLogin.setOnClickListener(v -> authenticateUser());

        TextView btnSignUp = findViewById(R.id.SingUp);
        btnSignUp.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
    }

    private void authenticateUser() {
        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("email", email);
            jsonRequest.put("password", password);
        } catch (JSONException e) {
            Toast.makeText(LoginActivity.this, "Error creating JSON request.", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "http://10.0.2.2:9085/authenticate";
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonRequest,
                response -> {
                    try {
                        String token = response.getString("token");
                        Log.d("LoginActivity", "Token: " + token);
                        String role = response.getString("role");

                        SharedPreferences sharedPref = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("token", token);
                        editor.putString("role", role);
                        editor.apply();

                        Intent intent;
                        switch (role) {
                            case "admin":
                                intent = new Intent(LoginActivity.this, ProductAdminActivity.class);
                                break;
                            case "user":
                                intent = new Intent(LoginActivity.this, ProductActivity.class);
                                break;
                            default:
                                Toast.makeText(LoginActivity.this, "Role not recognized", Toast.LENGTH_SHORT).show();
                                return;
                        }
                        startActivity(intent);
                        finish();
                    } catch (JSONException e) {
                        Toast.makeText(LoginActivity.this, "Error processing login response.", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("LoginActivity", "Error: " + error.toString());
                    if (error.networkResponse != null) {
                        Log.e("LoginActivity", "Error Status Code: " + error.networkResponse.statusCode);
                        Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "Login failed. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        queue.add(jsonObjectRequest);
    }
}
