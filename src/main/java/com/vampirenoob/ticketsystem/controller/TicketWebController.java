package com.vampirenoob.ticketsystem.controller;

import com.vampirenoob.ticketsystem.service.TicketService;
import com.vampirenoob.ticketsystem.model.Ticket;
import com.vampirenoob.ticketsystem.model.TicketPriority;
import com.vampirenoob.ticketsystem.model.TicketStatus;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import java.util.List;
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
    public String showTicketList(@RequestParam(required = false) TicketStatus status, Model model) {
        List<Ticket> tickets = (status != null)
            ? ticketService.getAllTickets().stream().filter(t -> t.getStatus() == status).toList()
            : ticketService.getAllTickets();

        model.addAttribute("tickets", tickets);
        model.addAttribute("nextTicket", ticketService.getNextTicketToWorkOn().orElse(null));
        model.addAttribute("activeFilter", status);
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
                                @RequestParam TicketPriority priority,
                                @RequestParam(required = false) String assignee) {
        ticketService.createTicket(title, description, priority, assignee);
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

    /**
     * Zeigt das Formular zum Bearbeiten eines bestehenden Tickets,
     * vorausgefüllt mit den aktuellen Werten.
     * GET /tickets/{id}/edit
     */
    @GetMapping("/tickets/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Ticket ticket = ticketService.getTicketById(id)
            .orElseThrow(() -> new IllegalArgumentException("Ticket mit ID " + id + " nicht gefunden"));
        model.addAttribute("ticket", ticket);
        return "edit-ticket";
    }

    /**
     * Verarbeitet das abgeschickte Bearbeitungsformular.
     * POST /tickets/{id}/edit
     */
    @PostMapping("/tickets/{id}/edit")
    public String updateTicket(@PathVariable Long id,
                                @RequestParam String title,
                                @RequestParam String description,
                                @RequestParam TicketPriority priority,
                                @RequestParam(required = false) String assignee) {
        ticketService.updateTicketDetails(id, title, description, priority, assignee);
        return "redirect:/";
    }
}