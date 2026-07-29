package com.vampirenoob.ticketsystem.repository;

import com.vampirenoob.ticketsystem.model.Ticket;
import com.vampirenoob.ticketsystem.model.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
* Repository-Schicht für den Datenbankzugriff auf Ticket-Entitäten.
* Erbt Standard-CRUD-Operationen von JpaRepository (save, findById,
* findAll, deleteById, ...) automatisch.
*/
public interface TicketRepository extends JpaRepository<Ticket, Long> {

/**
* Findet alle Tickets mit einem bestimmten Status.
* Spring Data JPA generiert die Query automatisch aus dem Methodennamen.
*/
    List<Ticket> findByStatus(TicketStatus status);
}