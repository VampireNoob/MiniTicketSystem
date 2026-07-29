package com.vampirenoob.ticketsystem.service;

import com.vampirenoob.ticketsystem.model.Ticket;
import com.vampirenoob.ticketsystem.model.TicketPriority;
import com.vampirenoob.ticketsystem.model.TicketStatus;
import com.vampirenoob.ticketsystem.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
* Service-Schicht für die Business-Logik rund um Tickets.
* Kapselt Zugriffslogik und Regeln, die über reines CRUD hinausgehen
* (z. B. Prioritäts-basierte Auswahl des nächsten Tickets).
*/
@Service
public class TicketService {

    private final TicketRepository ticketRepository;

/**
* Konstruktor-Injection: Spring reicht das TicketRepository
* automatisch rein (Dependency Injection).
*/
    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

/**
* Erstellt ein neues Ticket mit Status OFFEN.
*/
    public Ticket createTicket(String title, String description, TicketPriority priority) {
        Ticket ticket = new Ticket(title, description, priority);
        return ticketRepository.save(ticket);
    }

/**
* Liefert alle Tickets.
*/
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

/**
* Liefert ein einzelnes Ticket per ID, falls vorhanden.
*/
    public Optional<Ticket> getTicketById(Long id) {
        return ticketRepository.findById(id);
    }

/**
* Liefert das nächste zu bearbeitende Ticket: das offene Ticket
* mit der höchsten Priorität. Wird bei jedem Aufruf frisch aus
* der Datenbank ermittelt, damit neu eingehende Tickets mit
* höherer Priorität sofort berücksichtigt werden (präemptiv).
*/
    public Optional<Ticket> getNextTicketToWorkOn() {
        return ticketRepository.findByStatus(TicketStatus.OFFEN)
            .stream()
            .max(Comparator.comparing(Ticket::getPriority));
    }

/**
* Ändert den Status eines Tickets (z. B. OFFEN -> IN_ARBEIT -> ERLEDIGT).
* Wirft eine Exception, falls das Ticket nicht existiert.
*/
    public Ticket updateStatus(Long id, TicketStatus newStatus) {
        Ticket ticket = ticketRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Ticket mit ID " + id + " nicht gefunden"));
        ticket.setStatus(newStatus);
        return ticketRepository.save(ticket);
    }

/**
* Löscht ein Ticket per ID.
*/
    public void deleteTicket(Long id) {
        ticketRepository.deleteById(id);
    }
}