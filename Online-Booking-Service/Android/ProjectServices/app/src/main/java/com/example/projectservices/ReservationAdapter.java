package com.example.projectservices;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ViewHolder> {
    private List<Reservation> reservations;

    public ReservationAdapter(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reservation_item, parent, false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Reservation reservation = reservations.get(position);
        holder.productName.setText(reservation.getProductName());
        holder.description.setText(reservation.getDescription());
        holder.category.setText(reservation.getCategory());
        holder.availability.setText(reservation.getAvailability());
        holder.price.setText(String.valueOf(reservation.getPrice()));
        holder.location.setText(reservation.getLocation());
        holder.reservationDate.setText(reservation.getReservationDate());
    }

    @Override
    public int getItemCount() {
        return reservations.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView productName, description, category, availability, price, location, reservationDate;

        public ViewHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            description = itemView.findViewById(R.id.description);
            category = itemView.findViewById(R.id.category);
            availability = itemView.findViewById(R.id.availability);
            price = itemView.findViewById(R.id.price);
            location = itemView.findViewById(R.id.location);
            reservationDate = itemView.findViewById(R.id.reservationDate);
        }
    }
}
