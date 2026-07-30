package com.vampirenoob.ticketsystem.controller;

import com.vampirenoob.ticketsystem.service.TicketService;
import com.vampirenoob.ticketsystem.model.Ticket;
import com.vampirenoob.ticketsystem.model.TicketPriority;
import com.vampirenoob.ticketsystem.model.TicketStatus;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Web-Controller für die serverseitig gerenderten Thymeleaf-Ansichten.
 * Im Gegensatz zum TicketController (REST/JSON) liefert dieser
 * Controller vollständige HTML-Seiten aus.
 */
@Controller
public class TicketWebController {

    private final TicketService ticketService;

    public TicketWebController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    /**
     * Zeigt die Startseite mit allen Tickets an.
     * GET /
     */
    @GetMapping("/")
    public String showTicketList(Model model) {
        model.addAttribute("tickets", ticketService.getAllTickets());
        return "tickets";
    }

        /**
     * Zeigt das Formular zum Erstellen eines neuen Tickets.
     * GET /tickets/new
     */
    @GetMapping("/tickets/new")
    public String showCreateForm() {
        return "create-ticket";
    }

    /**
     * Verarbeitet das abgeschickte Formular und legt ein neues Ticket an.
     * POST /tickets/new
     */
    @PostMapping("/tickets/new")
    public String createTicket(@RequestParam String title,
                                @RequestParam String description,
                                @RequestParam TicketPriority priority) {
        ticketService.createTicket(title, description, priority);
        return "redirect:/";
    }

    /**
 * Setzt ein Ticket auf den nächsten Status im Workflow
 * (OFFEN -> IN_ARBEIT -> ERLEDIGT). Wird direkt aus der
 * Übersichtstabelle heraus aufgerufen.
 * POST /tickets/{id}/advance
 */
@PostMapping("/tickets/{id}/advance")
public String advanceStatus(@PathVariable Long id) {
    Ticket ticket = ticketService.getTicketById(id)
        .orElseThrow(() -> new IllegalArgumentException("Ticket mit ID " + id + " nicht gefunden"));
    TicketStatus nextStatus = switch (ticket.getStatus()) {
        case OFFEN -> TicketStatus.IN_ARBEIT;
        case IN_ARBEIT -> TicketStatus.ERLEDIGT;
        case ERLEDIGT -> TicketStatus.ERLEDIGT; // bleibt, kein weiterer Schritt
    };
    ticketService.updateStatus(id, nextStatus);
    return "redirect:/";
}

/**
 * Löscht ein Ticket direkt aus der Übersicht heraus.
 * POST /tickets/{id}/delete
 */
@PostMapping("/tickets/{id}/delete")
public String deleteTicket(@PathVariable Long id) {
    ticketService.deleteTicket(id);
    return "redirect:/";
}
}