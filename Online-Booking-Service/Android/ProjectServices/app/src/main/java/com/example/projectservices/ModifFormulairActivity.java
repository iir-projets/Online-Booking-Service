package com.example.projectservices;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ModifFormulairActivity extends AppCompatActivity {
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modif_formulair);
        token = getToken();

        // Récupérer les données du produit et afficher dans les champs de formulaire
        displayProductData();

        // Ajouter un écouteur de clic au bouton "modifier Product"
        Button buttonEditProduct = findViewById(R.id.buttoneditProduct);
        buttonEditProduct.setOnClickListener(view -> editProduct());
        
        Button addButton = findViewById(R.id.buttonCancel);
        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(ModifFormulairActivity.this, ProductAdminActivity.class);
            startActivity(intent);
        });
    }

    private void displayProductData() {
        EditText editTextProductName = findViewById(R.id.editTextProductName);
        EditText editTextDescription = findViewById(R.id.editTextDescription);
        EditText editTextCategory = findViewById(R.id.editTextCategory);
        EditText editTextAvailability = findViewById(R.id.editTextAvailability);
        EditText editTextPrice = findViewById(R.id.editTextPrice);
        EditText editTextLocation = findViewById(R.id.editTextLocation);

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

    private void editProduct() {
        EditText editTextProductName = findViewById(R.id.editTextProductName);
        EditText editTextDescription = findViewById(R.id.editTextDescription);
        EditText editTextCategory = findViewById(R.id.editTextCategory);
        EditText editTextAvailability = findViewById(R.id.editTextAvailability);
        EditText editTextPrice = findViewById(R.id.editTextPrice);
        EditText editTextLocation = findViewById(R.id.editTextLocation);

        String productName = editTextProductName.getText().toString();
        String description = editTextDescription.getText().toString();
        String category = editTextCategory.getText().toString();
        String availability = editTextAvailability.getText().toString();
        String priceText = editTextPrice.getText().toString();
        String location = editTextLocation.getText().toString();

        // Vérifier si le champ de prix est une valeur numérique
        if (!isNumeric(priceText)) {
            Toast.makeText(this, "Le prix doit être une valeur numérique", Toast.LENGTH_SHORT).show();
            return;
        }

        int price = Integer.parseInt(priceText);

        // Créer un objet Product avec les valeurs des champs de formulaire
        Product product = new Product(productName, description, category, availability, location, price);

        // Appeler la méthode pour éditer le produit
        editProductRequest(product);
    }

    private boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    private void editProductRequest(Product product) {
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

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "http://10.0.2.2:9085/services/edit?token=" + token, requestBody,
                response -> {
                    try {
                        int responseCode = response.getInt("response");
                        if (responseCode == 200) {
                            Toast.makeText(this, "Produit modifié avec succès", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ModifFormulairActivity.this, ProductAdminActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(this, "Erreur lors de la modification du produit: " + responseCode, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Erreur lors de la modification du produit", Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    Toast.makeText(this, "Échec de la modification du produit: " + error.getMessage(), Toast.LENGTH_LONG).show();
                });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);
    }

    private String getToken() {
        SharedPreferences sharedPref = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        return sharedPref.getString("token", "");
    }
}

