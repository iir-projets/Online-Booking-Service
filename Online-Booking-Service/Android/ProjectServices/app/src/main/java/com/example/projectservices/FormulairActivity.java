package com.example.projectservices;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FormulairActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int PICK_IMAGE_REQUEST = 2;

    private EditText editTextProductName, editTextDescription, editTextCategory,
            editTextAvailability, editTextPrice, editTextLocation;
    private Button buttonAddProduct, buttonSelectImage, buttonCancel;
    private String token;
    private byte[] image;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulair);

        // Initialize views
        initViews();

        // Get token from SharedPreferences
        token = getToken();

        // Set click listeners
        setClickListeners();
    }

    private void initViews() {
        editTextProductName = findViewById(R.id.editTextProductName);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextCategory = findViewById(R.id.editTextCategory);
        editTextAvailability = findViewById(R.id.editTextAvailability);
        editTextPrice = findViewById(R.id.editTextPrice);
        editTextLocation = findViewById(R.id.editTextLocation);
        buttonAddProduct = findViewById(R.id.buttoneditProduct);
        buttonSelectImage = findViewById(R.id.buttonSelectImage);
        buttonCancel = findViewById(R.id.buttonCancel);
    }

    private void setClickListeners() {
        buttonAddProduct.setOnClickListener(v -> {
            try {
                addProduct();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });
        buttonSelectImage.setOnClickListener(v -> selectImage());
        buttonCancel.setOnClickListener(v -> finish());
    }

    private String getToken() {
        SharedPreferences sharedPref = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        return sharedPref.getString("token", "");
    }

    private void addProduct() throws JSONException {
        String productName = editTextProductName.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String category = editTextCategory.getText().toString().trim();
        String availability = editTextAvailability.getText().toString().trim();
        int price = Integer.parseInt(editTextPrice.getText().toString().trim());
        String location = editTextLocation.getText().toString().trim();

        if (productName.isEmpty() || description.isEmpty() || category.isEmpty() ||
                availability.isEmpty() || location.isEmpty() ) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        addProductToAPI(productName, description, category, availability, location, price);
    }

    private void addProductToAPI(String name, String description, String category, String availability, String location, int price) throws JSONException {
        String url = "http://10.0.2.2:9085/services/add";

        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("product", new JSONObject().put("name", name).put("description", description)
                .put("category", category).put("availability", availability)
                .put("price", price).put("location", location).toString());

        Map<String, VolleyMultipartRequest.DataPart> dataParts = new HashMap<>();
        if (image != null) {
            dataParts.put("imageFile", new VolleyMultipartRequest.DataPart("image.jpg", image, "image/jpeg"));
        }

        VolleyMultipartRequest request = new VolleyMultipartRequest(Request.Method.POST, url, null, params, dataParts,
                response -> {
                    Toast.makeText(this, "Product added successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, ProductAdminActivity.class));
                    finish();
                },
                error -> {
                    Toast.makeText(this, "Failed to add product: " + new String(error.networkResponse.data, StandardCharsets.UTF_8), Toast.LENGTH_LONG).show();
                });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }



    private void selectImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                requestPermission(Manifest.permission.READ_MEDIA_IMAGES);
            } else {
                openGallery();
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            } else {
                openGallery();
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void requestPermission(String permission) {
        ActivityCompat.requestPermissions(this, new String[]{permission}, PERMISSION_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                image = byteArrayOutputStream.toByteArray();
                Log.d("FormulairActivity", "Image converted to byte array.");
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to process image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(this, "Permission Denied. You can't use this feature without permission.", Toast.LENGTH_LONG).show();
            }
        }
    }
}