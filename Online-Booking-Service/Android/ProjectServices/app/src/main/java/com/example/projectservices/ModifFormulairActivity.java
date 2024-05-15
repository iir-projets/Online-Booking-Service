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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ModifFormulairActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int PICK_IMAGE_REQUEST = 2;

    private String token;
    private EditText editTextProductName, editTextDescription, editTextCategory, editTextAvailability, editTextPrice, editTextLocation;
    private Button buttonChooseImage;

    private byte[] image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modif_formulair);
        token = getToken();

        // Initialize text fields
        editTextProductName = findViewById(R.id.editTextProductName);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextCategory = findViewById(R.id.editTextCategory);
        editTextAvailability = findViewById(R.id.editTextAvailability);
        editTextPrice = findViewById(R.id.editTextPrice);
        editTextLocation = findViewById(R.id.editTextLocation);
        buttonChooseImage = findViewById(R.id.buttonChooseImage);

        // Retrieve product data and display in form fields
        displayProductData();

        // Add click listener to "Edit Product" button
        Button buttonEditProduct = findViewById(R.id.buttoneditProduct);
        buttonEditProduct.setOnClickListener(view -> editProduct());

        // Add click listener to "Cancel" button
        Button buttonCancel = findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener(v -> finish());

        // Add click listener to "Choose Image" button
        buttonChooseImage.setOnClickListener(v -> selectImage());
    }

    private void displayProductData() {
        Intent intent = getIntent();
        if (intent != null) {
            editTextProductName.setText(intent.getStringExtra("productName"));
            editTextDescription.setText(intent.getStringExtra("productDescription"));
            editTextCategory.setText(intent.getStringExtra("productCategory"));
            editTextAvailability.setText(intent.getStringExtra("productAvailability"));
            editTextPrice.setText(String.valueOf(intent.getIntExtra("productPrice", 0)));
            editTextLocation.setText(intent.getStringExtra("productLocation"));

        }
    }
    private String getToken() {
        SharedPreferences sharedPref = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        return sharedPref.getString("token", "");
    }


    private void editProduct() {
        String productName = editTextProductName.getText().toString();
        String description = editTextDescription.getText().toString();
        String category = editTextCategory.getText().toString();
        String availability = editTextAvailability.getText().toString();
        String priceText = editTextPrice.getText().toString();
        String location = editTextLocation.getText().toString();

        // Check if all fields are filled
        if (productName.isEmpty() || description.isEmpty() || category.isEmpty() || availability.isEmpty() || priceText.isEmpty() || location.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if price field is numeric
        if (!isNumeric(priceText)) {
            Toast.makeText(this, "Price must be a numeric value", Toast.LENGTH_SHORT).show();
            return;
        }

        int price = Integer.parseInt(priceText);

        // Create a Product object with form field values
        Product product = new Product(productName, description, category, availability, location, price, image);

        // Call method to edit product
        editProductRequest(productName, description, category, availability, location, price, image);
    }

    private boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    private void editProductRequest(String name, String description, String category, String availability, String location, int price, byte[] image) {
        String url = "http://10.0.2.2:9085/services/edit";

        // Prepare the parameters and the image data
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        JSONObject productDetails = new JSONObject();
        try {
            productDetails.put("name", name);
            productDetails.put("description", description);
            productDetails.put("category", category);
            productDetails.put("availability", availability);
            productDetails.put("price", price);
            productDetails.put("location", location);
            params.put("product", productDetails.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to prepare product details.", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, VolleyMultipartRequest.DataPart> dataParts = new HashMap<>();
        if (image != null) {
            dataParts.put("imageFile", new VolleyMultipartRequest.DataPart("file_name.jpg", image, "image/jpeg"));
        }

        // Create the custom request
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, null, params, dataParts,
                response -> {
                    Toast.makeText(this, "Product edited successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, ProductAdminActivity.class));
                    finish();
                },
                error -> {
                    Toast.makeText(this, "Failed to edit product: " + new String(error.networkResponse.data, StandardCharsets.UTF_8), Toast.LENGTH_LONG).show();
                });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(multipartRequest);
    }


    private void selectImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                requestPermission(android.Manifest.permission.READ_MEDIA_IMAGES);
            } else {
                openGallery();
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
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
