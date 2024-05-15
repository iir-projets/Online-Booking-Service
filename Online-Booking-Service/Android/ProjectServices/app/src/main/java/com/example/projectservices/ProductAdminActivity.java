package com.example.projectservices;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.List;

public class ProductAdminActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProductAdminAdapter adapter;
    private List<Product> productList;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_admin);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productList = new ArrayList<>();

        SharedPreferences sharedPref = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        token = sharedPref.getString("token", "");

        adapter = new ProductAdminAdapter(this, productList, token);
        recyclerView.setAdapter(adapter);

        Button addButton = findViewById(R.id.addProductButton);
        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProductAdminActivity.this, FormulairActivity.class);
            startActivity(intent);
        });

        Button logoutButton = findViewById(R.id.Btnlogout);
        logoutButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProductAdminActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        fetchProductsFromApi();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchProductsFromApi(); // Refresh the product list whenever the activity comes to the foreground
    }

    private void fetchProductsFromApi() {
        Log.d("ProductAdminActivity", "Token: " + token);

        if (token.isEmpty()) {
            Toast.makeText(this, "Authentication token not found. Please login again.", Toast.LENGTH_LONG).show();
            return;
        }

        String url = "http://10.0.2.2:9085/services?token=" + Uri.encode(token);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null,
                this::parseProducts,
                this::handleError) {
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
                    byte[] image = Base64.decode(productObj.getString("image"), Base64.DEFAULT);

                    Product product = new Product(name, description, category, availability, location, price,image);
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

    public void deleteProduct(String token, Product product) {
        String url = "http://10.0.2.2:9085/services/delete?token=" + token;
        Log.d("FormulairActivity", "token 1111 : " + token);

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("name", product.getName());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                response -> {
                    Toast.makeText(this, "Product deleted successfully", Toast.LENGTH_SHORT).show();
                    fetchProductsFromApi(); // Rafraîchir la liste des produits après la suppression
                },
                error -> {
                    Toast.makeText(this, "Failed to delete product: " + error.getMessage(), Toast.LENGTH_LONG).show();
                });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);
    }



    private void handleError(VolleyError error) {
        Toast.makeText(this, "Failed to retrieve data: " + error.toString(), Toast.LENGTH_LONG).show();
    }
}
