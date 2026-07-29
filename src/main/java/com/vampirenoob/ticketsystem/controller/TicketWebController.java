package com.vampirenoob.ticketsystem.controller;

import com.vampirenoob.ticketsystem.service.TicketService;
import com.vampirenoob.ticketsystem.model.TicketPriority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
}