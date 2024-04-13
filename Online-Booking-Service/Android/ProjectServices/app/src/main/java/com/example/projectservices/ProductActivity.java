package com.example.projectservices;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
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
        adapter = new ProductAdapter(this, productList);
        recyclerView.setAdapter(adapter);

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
                    parseProducts(response.toString());
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



    private void parseProducts(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.has("products")) {
                JSONArray jsonArray = jsonObject.getJSONArray("products");
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
                Toast.makeText(this, "No products found in the response", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            Toast.makeText(this, "Error parsing JSON data", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void handleError(VolleyError error) {
        String message = null;
        if (error.networkResponse != null && error.networkResponse.data != null) {
            message = new String(error.networkResponse.data, StandardCharsets.UTF_8);
        }
        Toast.makeText(this, "Failed to retrieve data: " + (message != null ? message : error.toString()), Toast.LENGTH_LONG).show();
    }
}
