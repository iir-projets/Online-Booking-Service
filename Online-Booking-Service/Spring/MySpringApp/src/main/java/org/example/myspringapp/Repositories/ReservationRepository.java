package org.example.myspringapp.Repositories;

import org.example.myspringapp.Model.Reservation;
import org.example.myspringapp.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation,Long> {
    public List<Reservation> findByUser(User user);
}