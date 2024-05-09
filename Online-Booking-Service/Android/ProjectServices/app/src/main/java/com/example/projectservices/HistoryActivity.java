package com.example.projectservices;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ReservationAdapter adapter;
    private List<Reservation> reservations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        recyclerView = findViewById(R.id.historyRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        reservations = new ArrayList<>();
        adapter = new ReservationAdapter(reservations);
        recyclerView.setAdapter(adapter);
        Button historyButton = findViewById(R.id.btnReturn);
        historyButton.setOnClickListener(v -> {
            Intent intent = new Intent(HistoryActivity.this, ProductActivity.class);
            startActivity(intent);
        });

        fetchHistoryFromApi();  // Fetch history when activity starts
    }

    private void fetchHistoryFromApi() {
        SharedPreferences sharedPref = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", "");
        if (token.isEmpty()) {
            Toast.makeText(this, "No authentication token. Please log in again.", Toast.LENGTH_LONG).show();
            return;
        }

        String url = "http://10.0.2.2:9085/history?token=" + token;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                this::parseHistory,
                error -> Toast.makeText(this, "Failed to retrieve data: " + error.toString(), Toast.LENGTH_LONG).show()
        );

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);
    }

    private void parseHistory(JSONObject response) {
        try {
            int responseCode = response.getInt("response");
            if (responseCode == 200) {
                JSONArray jsonArray = response.getJSONArray("data");
                reservations.clear();
                DateTimeFormatter inputFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
                DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yy");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject reservationObj = jsonArray.getJSONObject(i);
                    JSONObject productObj = reservationObj.getJSONObject("product");

                    String reservationId = reservationObj.optString("id", "No ID");
                    String productName = productObj.optString("name", "No Product Name");
                    String description = productObj.optString("description", "No Description");
                    String category = productObj.optString("category", "No Category");
                    String availability = productObj.optString("availability", "Unavailable");
                    int price = productObj.optInt("price", 0);
                    String location = productObj.optString("location", "No Location");
                    String reservationDate = reservationObj.optString("reservationDate", "No Date");

                    // Log the original date string
                    Log.d("HistoryActivity", "Original Date: " + reservationDate);

                    // Convert and format the date
                    String formattedDate = "No Date";
                    try {
                        OffsetDateTime date = OffsetDateTime.parse(reservationDate, inputFormatter);
                        formattedDate = date.format(outputFormatter);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("HistoryActivity", "Exception: " + e.getMessage());
                    }

                    // Log the formatted date
                    Log.d("HistoryActivity", "Formatted Date: " + formattedDate);

                    reservations.add(new Reservation(reservationId, productName, description, category, availability, price, location, formattedDate));
                }
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "Failed to retrieve booking history. Response code: " + responseCode, Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            Toast.makeText(this, "Error parsing JSON data", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

}
