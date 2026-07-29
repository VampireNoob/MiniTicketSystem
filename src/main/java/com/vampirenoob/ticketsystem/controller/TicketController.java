package com.vampirenoob.ticketsystem.controller;

import com.vampirenoob.ticketsystem.model.Ticket;
import com.vampirenoob.ticketsystem.model.TicketStatus;
import com.vampirenoob.ticketsystem.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST-Controller für Ticket-Operationen.
 * Stellt die HTTP-Schnittstelle bereit, delegiert die eigentliche
 * Logik komplett an die Service-Schicht.
 */
@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    /**
     * Erstellt ein neues Ticket.
     * POST /api/tickets
     */
    @PostMapping
    public ResponseEntity<Ticket> createTicket(@RequestBody CreateTicketRequest request) {
        Ticket ticket = ticketService.createTicket(
            request.title(),
            request.description(),
            request.priority()
        );
        return ResponseEntity.ok(ticket);
    }

    /**
     * Liefert alle Tickets.
     * GET /api/tickets
     */
    @GetMapping
    public ResponseEntity<List<Ticket>> getAllTickets() {
        return ResponseEntity.ok(ticketService.getAllTickets());
    }

    /**
     * Liefert ein einzelnes Ticket per ID.
     * GET /api/tickets/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Ticket> getTicketById(@PathVariable Long id) {
        return ticketService.getTicketById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Liefert das nächste zu bearbeitende Ticket (höchste Priorität, Status OFFEN).
     * GET /api/tickets/next
     */
    @GetMapping("/next")
    public ResponseEntity<Ticket> getNextTicket() {
        return ticketService.getNextTicketToWorkOn()
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.noContent().build());
    }

    /**
     * Ändert den Status eines Tickets.
     * PATCH /api/tickets/{id}/status
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<Ticket> updateStatus(@PathVariable Long id, @RequestBody Map<String, TicketStatus> body) {
        Ticket updated = ticketService.updateStatus(id, body.get("status"));
        return ResponseEntity.ok(updated);
    }

    /**
     * Löscht ein Ticket.
     * DELETE /api/tickets/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Record für den Request-Body beim Erstellen eines Tickets.
     * Java Records sind eine kompakte Möglichkeit, unveränderliche
     * Datenklassen ohne Boilerplate (Getter, equals, hashCode) zu definieren.
     */
    public record CreateTicketRequest(String title, String description, com.vampirenoob.ticketsystem.model.TicketPriority priority) {}
}