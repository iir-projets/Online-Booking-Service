package com.example.projectservices;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private Context context;
    private List<Product> productList;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.nameTextView.setText(product.getName());
        holder.descriptionTextView.setText(product.getDescription());
        holder.categoryTextView.setText(product.getCategory());
        holder.priceTextView.setText(String.format("$%d", product.getPrice())); // Assuming price is an integer
        holder.availabilityTextView.setText(product.getAvailability());
        holder.locationTextView.setText(product.getLocation()); // Set the location TextView

        // Set onClickListener for the product image
        holder.productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProductDetailsDialog(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView nameTextView;
        TextView descriptionTextView;
        TextView categoryTextView;
        TextView priceTextView;
        TextView availabilityTextView;
        TextView locationTextView;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage); // Initialize ImageView
            nameTextView = itemView.findViewById(R.id.productName);
            descriptionTextView = itemView.findViewById(R.id.productDescription);
            categoryTextView = itemView.findViewById(R.id.productCategory);
            priceTextView = itemView.findViewById(R.id.productPrice);
            availabilityTextView = itemView.findViewById(R.id.productAvailability);
            locationTextView = itemView.findViewById(R.id.productLocation);
        }
    }


    private void showProductDetailsDialog(Product product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Product Details");
        String message =
                "Name: " + product.getName() + "\n" +
                "Description: " + product.getDescription() + "\n" +
                "Category: " + product.getCategory() + "\n" +
                "Price: $" + product.getPrice() + "\n" +
                "Availability: " + product.getAvailability() + "\n" +
                "Location: " + product.getLocation();
        builder.setMessage(message);
        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
