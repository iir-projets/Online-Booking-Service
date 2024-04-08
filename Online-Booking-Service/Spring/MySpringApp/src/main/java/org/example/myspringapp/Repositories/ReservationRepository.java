package org.example.myspringapp.Repositories;

import org.example.myspringapp.Model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation,Long> {
}
