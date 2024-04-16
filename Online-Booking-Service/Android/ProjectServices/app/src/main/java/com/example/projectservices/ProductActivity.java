package com.example.projectservices;

import android.content.Context;
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
import com.example.projectservices.Product;
import com.example.projectservices.ProductAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        adapter = new ProductAdapter(this, productList);
        recyclerView.setAdapter(adapter);

        // Ajout du bouton "Historique"
        Button historyButton = findViewById(R.id.historyButton);
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchHistoryFromApi();
            }
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
                    // Handle response
                    parseProducts(response);
                },
                error -> {
                    // Handle error
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

                    // Vérifiez que les valeurs sont correctement extraites
                    Log.d("ProductActivity", "Product " + i + ": " + name + ", " + description + ", " + category + ", " + availability + ", " + location + ", " + price);

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

    private void handleError(VolleyError error) {
        Toast.makeText(this, "Failed to retrieve data: " + error.toString(), Toast.LENGTH_LONG).show();
    }

    // Méthode pour récupérer l'historique à partir de l'API
    private void fetchHistoryFromApi() {
        SharedPreferences sharedPref = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        final String token = sharedPref.getString("token", "");
        if (token.isEmpty()) {
            Toast.makeText(this, "No authentication token. Please log in again.", Toast.LENGTH_LONG).show();
            return;
        }

        // Append token as a query parameter
        String url = "http://10.0.2.2:9085/history?token=" + token;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> parseHistory(response),
                error -> handleError(error)
        );

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);
    }


    // Méthode pour analyser la réponse JSON contenant l'historique des réservations
    // Méthode pour analyser la réponse JSON contenant l'historique des réservations
    private void parseHistory(JSONObject response) {
        try {
            int responseCode = response.getInt("response");
            if (responseCode == 200) {
                JSONArray jsonArray = response.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject reservationObj = jsonArray.getJSONObject(i);

                    // Accessing nested product object
                    JSONObject productObj = reservationObj.getJSONObject("product");
                    // Accessing nested user object, if needed
                    JSONObject userObj = reservationObj.getJSONObject("user");

                    // Extracting information from the nested product object
                    String reservationId = reservationObj.optString("id", "No ID");
                    String productName = productObj.optString("name", "No Product Name");
                    String description = productObj.optString("description", "No Description");
                    String category = productObj.optString("category", "No Category");
                    String availability = productObj.optString("availability", "Unavailable");
                    int price = productObj.optInt("price", 0);
                    String location = productObj.optString("location", "No Location");
                    String reservationDate = reservationObj.optString("reservationDate", "No Date");

                    // Logging for verification
                    Log.d("Reservation", "ID: " + reservationId + ", Product Name: " + productName + ", Description: " + description +
                            ", Category: " + category + ", Available: " + availability + ", Price: " + price + ", Location: " + location +
                            ", Date: " + reservationDate);
                }
            } else {
                Toast.makeText(this, "Failed to retrieve booking history. Response code: " + responseCode, Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            Toast.makeText(this, "Error parsing JSON data", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

}
