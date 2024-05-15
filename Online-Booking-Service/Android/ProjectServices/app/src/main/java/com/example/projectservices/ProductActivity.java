package com.example.projectservices;

import android.content.ActivityNotFoundException;
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
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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

        SharedPreferences sharedPref = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        final String token = sharedPref.getString("token", "");

        adapter = new ProductAdapter(this, productList, token);
        recyclerView.setAdapter(adapter);

        Button historyButton = findViewById(R.id.historyButton);
        historyButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProductActivity.this, HistoryActivity.class);
            startActivity(intent);
        });
        Button logoutButton = findViewById(R.id.btnlout);
        logoutButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProductActivity.this, LoginActivity.class);
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

        // Append the token as a query parameter
        String url = "http://10.0.2.2:9085/services?token=" + Uri.encode(token);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null,
                response -> {
                    parseProducts(response);
                },
                error -> {
                    handleError(error);
                }) {
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

    public void generatePDF(String productName, double price, String token) {
        if (token.isEmpty()) {
            Toast.makeText(this, "Authentication token not found. Please login again.", Toast.LENGTH_LONG).show();
            return;
        }


        String url = "http://10.0.2.2:9085/reservation/PDF";
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("productName", productName);
            requestBody.put("price", price);
            requestBody.put("token", token);
            Log.d("generatePDF", "Sending PDF generation request: " + requestBody.toString());
        } catch (JSONException e) {
            Toast.makeText(this, "Error creating JSON data", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return;
        }

        Response.Listener<byte[]> responseListener = response -> {
            try {
                Log.d("generatePDF", "PDF generated successfully, response length: " + response.length);
                String filePath = savePdfToFile(response, "generated_pdf.pdf");
                if (filePath != null) {
                    Toast.makeText(this, "PDF generated successfully!", Toast.LENGTH_SHORT).show();
                    openPdfFile(filePath);  // Open the PDF file
                } else {
                    Toast.makeText(this, "Failed to save PDF", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(this, "Error handling PDF response", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        };

        Response.ErrorListener errorListener = error -> {
            Log.e("generatePDF", "Failed to generate PDF: " + error.toString());
            Toast.makeText(this, "Failed to generate PDF: " + error.toString(), Toast.LENGTH_LONG).show();
        };

        Request<byte[]> pdfRequest = new Request<byte[]>(Request.Method.POST, url, errorListener) {
            @Override
            protected Map<String, String> getParams() {
                return null;
            }

            @Override
            protected Response<byte[]> parseNetworkResponse(NetworkResponse response) {
                return Response.success(response.data, HttpHeaderParser.parseCacheHeaders(response));
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return requestBody.toString().getBytes(StandardCharsets.UTF_8);
            }

            @Override
            protected void deliverResponse(byte[] response) {
                responseListener.onResponse(response);
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(pdfRequest);
    }


    private String savePdfToFile(byte[] pdfBytes, String fileName) {
        try {
            File path = new File(getExternalFilesDir(null), fileName);
            FileOutputStream fos = new FileOutputStream(path);
            fos.write(pdfBytes);
            fos.close();
            Log.d("generatePDF", "PDF saved to: " + path.getAbsolutePath());
            return path.getAbsolutePath();
        } catch (IOException e) {
            Log.e("generatePDF", "Error saving PDF", e);
            return null;
        }
    }


    private void openPdfFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            Uri fileUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(fileUri, "application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "No application found to open PDF", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "PDF file not found", Toast.LENGTH_SHORT).show();
        }
    }



    private void handleError(VolleyError error) {
        Toast.makeText(this, "Failed to retrieve data: " + error.toString(), Toast.LENGTH_LONG).show();
    }
}
