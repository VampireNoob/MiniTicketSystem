package com.vampirenoob.ticketsystem.model;

/**
* Repräsentiert den Bearbeitungsstatus eines Tickets
* im Workflow: OFFEN -> IN_ARBEIT -> ERLEDIGT.
*/
public enum TicketStatus {
    OFFEN,
    IN_ARBEIT,
    ERLEDIGT
}