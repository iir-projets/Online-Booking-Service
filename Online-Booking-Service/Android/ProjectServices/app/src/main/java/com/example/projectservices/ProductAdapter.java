package com.example.projectservices;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private Context context;
    private List<Product> productList;
    private String token;  // Token for making reservations

    public ProductAdapter(Context context, List<Product> productList, String token) {
        this.context = context;
        this.productList = productList;
        this.token = token;  // Initialize token from constructor
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.productName.setText(product.getName());
        holder.productDescription.setText(product.getDescription());
        holder.productPrice.setText(String.format("$%s", product.getPrice()));
        holder.productAvailability.setText(product.getAvailability());
        holder.productCategory.setText(product.getCategory());
        holder.productLocation.setText(product.getLocation());

        // Set a click listener for the reserve button
        holder.btnReserve.setOnClickListener(view -> {
            if (context instanceof ProductActivity) {
                ((ProductActivity) context).makeReservation(product.getName(), token);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView productName, productDescription, productPrice, productAvailability, productCategory, productLocation;
        public Button btnReserve;

        public ViewHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productDescription = itemView.findViewById(R.id.productDescription);
            productPrice = itemView.findViewById(R.id.productPrice);
            productAvailability = itemView.findViewById(R.id.productAvailability);
            productCategory = itemView.findViewById(R.id.productCategory);
            productLocation = itemView.findViewById(R.id.productLocation);
            btnReserve = itemView.findViewById(R.id.btnReserve);
        }
    }
}
