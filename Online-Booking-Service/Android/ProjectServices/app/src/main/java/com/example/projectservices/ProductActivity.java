package com.example.projectservices;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productList = new ArrayList<>();

        SharedPreferences sharedPref = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        final String token = sharedPref.getString("token", "");

        adapter = new ProductAdapter(this, productList, token);
        recyclerView.setAdapter(adapter);

        Button historyButton = findViewById(R.id.historyButton);
        historyButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProductActivity.this, HistoryActivity.class);
            startActivity(intent);
        });

        fetchProductsFromApi();
    }

    private void fetchProductsFromApi() {
        SharedPreferences sharedPref = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        final String token = sharedPref.getString("token", "");
        Log.d("ProductActivity", "Token: " + token);

        if (token.isEmpty()) {
            Toast.makeText(this, "Authentication token not found. Please login again.", Toast.LENGTH_LONG).show();
            return;
        }

        String url = "http://10.0.2.2:9085/services";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null,
                response -> {
                    parseProducts(response);
                },
                error -> {
                    handleError(error);
                }) {
            @Override
            public byte[] getBody() {
                try {
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("token", token);
                    return jsonBody.toString().getBytes(StandardCharsets.UTF_8);
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);
    }

    private void parseProducts(JSONObject response) {
        try {
            int responseCode = response.getInt("response");
            if (responseCode == 200) {
                JSONArray jsonArray = response.getJSONArray("data");
                productList.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject productObj = jsonArray.getJSONObject(i);
                    long id = productObj.getLong("id");
                    String name = productObj.getString("name");
                    String description = productObj.getString("description");
                    String category = productObj.getString("category");
                    String availability = productObj.getString("availability");
                    String location = productObj.getString("location");
                    int price = productObj.getInt("price");

                    Product product = new Product(id, name, description, category, availability, location, price);
                    productList.add(product);
                }
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "Failed to retrieve products. Response code: " + responseCode, Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            Toast.makeText(this, "Error parsing JSON data", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void makeReservation(String productName, String token) {
        if (token.isEmpty()) {
            Toast.makeText(this, "Authentication token not found. Please login again.", Toast.LENGTH_LONG).show();
            return;
        }

        String url = "http://10.0.2.2:9085/reservation";
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("productName", productName);
            requestBody.put("token", token);
            Log.d("makeReservation", "Sending reservation request: " + requestBody.toString());  // Log the request body
        } catch (JSONException e) {
            Toast.makeText(this, "Error creating JSON data", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                response -> {
                    try {
                        int responseCode = response.getInt("response");
                        if (responseCode == 200) {
                            Toast.makeText(this, "Reservation successful!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Reservation failed with response code: " + responseCode, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(this, "Error parsing reservation response", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, "Failed to make reservation: " + error.toString(), Toast.LENGTH_LONG).show()
        ) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);
    }

    private void handleError(VolleyError error) {
        Toast.makeText(this, "Failed to retrieve data: " + error.toString(), Toast.LENGTH_LONG).show();
    }
}
