package com.example.projectservices;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class FormulairActivity extends AppCompatActivity {

    private EditText editTextProductName, editTextDescription, editTextCategory,
            editTextAvailability, editTextPrice, editTextLocation;
    private Button buttonAddProduct;
    private String token;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulair);

        // Initialize views
        editTextProductName = findViewById(R.id.editTextProductName);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextCategory = findViewById(R.id.editTextCategory);
        editTextAvailability = findViewById(R.id.editTextAvailability);
        editTextPrice = findViewById(R.id.editTextPrice);
        editTextLocation = findViewById(R.id.editTextLocation);
        buttonAddProduct = findViewById(R.id.buttonAddProduct);

        // Get token from SharedPreferences
        token = getToken();

        // Set click listener for add product button
        buttonAddProduct.setOnClickListener(v -> addProduct());
    }

    private void addProduct() {
        // Get input values
        String productName = editTextProductName.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String category = editTextCategory.getText().toString().trim();
        String availability = editTextAvailability.getText().toString().trim();
        int price = Integer.parseInt(editTextPrice.getText().toString().trim());
        String location = editTextLocation.getText().toString().trim();

        // Check if any field is empty
        if (productName.isEmpty() || description.isEmpty() || category.isEmpty() ||
                availability.isEmpty() || location.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create Product object
        Product product = new Product( productName, description, category, availability, location, price);

        // Call method to add product
        addProductToAPI(product);
    }

    private void addProductToAPI(Product product) {
        // Call your API to add the product
        String url = "http://10.0.2.2:9085/services/add?token=" + token;
        Log.d("FormulairActivity","token 1111 : "+ token);

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("name", product.getName());
            requestBody.put("description", product.getDescription());
            requestBody.put("category", product.getCategory());
            requestBody.put("availability", product.getAvailability());
            requestBody.put("price", product.getPrice());
            requestBody.put("location", product.getLocation());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                response -> {
                    // Handle successful addition
                    Toast.makeText(this, "Product added successfully", Toast.LENGTH_SHORT).show();
                    // Redirect to ProductAdminActivity
                    Intent intent = new Intent(FormulairActivity.this, ProductAdminActivity.class);
                    startActivity(intent);
                    // Finish the current activity
                    finish();
                },
                error -> {
                    // Handle error
                    Toast.makeText(this, "Failed to add product: " + error.getMessage(), Toast.LENGTH_LONG).show();
                });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);
    }

    // Method to get the token from SharedPreferences
    private String getToken() {
        SharedPreferences sharedPref = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        return sharedPref.getString("token", "");
    }
}
