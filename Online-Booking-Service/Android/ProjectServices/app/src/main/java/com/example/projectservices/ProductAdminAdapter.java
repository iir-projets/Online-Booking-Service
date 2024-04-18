package com.example.projectservices;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.projectservices.Product;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ProductAdminAdapter extends RecyclerView.Adapter<ProductAdminAdapter.ViewHolder> {

    private Context context;
    private List<Product> productList;
    private String token;

    public ProductAdminAdapter(Context context, List<Product> productList, String token) {
        this.context = context;
        this.productList = productList;
        this.token = token;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_admin_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.productName.setText(product.getName());
        holder.productPrice.setText(String.format("$%s", product.getPrice()));
        holder.productCategory.setText(product.getCategory());

        holder.productImage.setOnClickListener(view -> {
            // Show product details dialog when image is clicked
            showProductDetails(product);
        });

        holder.btnEdit.setOnClickListener(view -> {
            // Perform action when edit button is clicked
            // You can open an activity for editing product details or perform any other action
            // Example: ((AdminProductActivity) context).editProduct(product);
        });

        holder.btnDelete.setOnClickListener(view -> {

            ((ProductAdminActivity) context).deleteProduct(product); });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView productName, productPrice, productCategory;
        public Button btnEdit, btnDelete;
        public ImageView productImage;

        public ViewHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productCategory = itemView.findViewById(R.id.productCategory);
            btnEdit = itemView.findViewById(R.id.btnModify);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            productImage = itemView.findViewById(R.id.productImage);
        }
    }

    private void showDeleteConfirmationDialog(Product product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to delete this product?");
        builder.setPositiveButton("Yes", (dialogInterface, i) -> {
            // Call API to delete the product
            ((ProductAdminActivity) context).deleteProduct(product);
        });
        builder.setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void showProductDetails(Product product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Product Details");
        builder.setMessage("Name: " + product.getName() +
                "\nDescription: " + product.getDescription() +
                "\nPrice: $" + product.getPrice() +
                "\nAvailability: " + product.getAvailability() +
                "\nCategory: " + product.getCategory() +
                "\nLocation: " + product.getLocation());
        builder.setPositiveButton("OK", (dialog, which) -> {
            dialog.dismiss();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
