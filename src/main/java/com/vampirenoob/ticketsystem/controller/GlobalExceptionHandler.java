package com.vampirenoob.ticketsystem.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Globaler Exception-Handler für alle Controller.
 * Fängt bekannte Fehlerfälle ab und wandelt sie in saubere,
 * einheitliche HTTP-Fehlerantworten um, statt rohe Stacktraces
 * mit Status 500 an den Client durchzureichen.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Wird ausgelöst, wenn z. B. ein Ticket mit einer nicht existierenden
     * ID angefragt/geändert wird. Ergebnis: 404 Not Found mit lesbarer
     * Fehlermeldung statt 500 Internal Server Error.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, Object> body = Map.of(
            "timestamp", LocalDateTime.now(),
            "status", HttpStatus.NOT_FOUND.value(),
            "error", "Not Found",
            "message", ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    /**
     * Auffangnetz für alle sonstigen, unerwarteten Fehler.
     * Verhindert, dass interne Details (Stacktrace, Klassennamen)
     * nach außen dringen.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception ex) {
        Map<String, Object> body = Map.of(
            "timestamp", LocalDateTime.now(),
            "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "error", "Internal Server Error",
            "message", "Ein unerwarteter Fehler ist aufgetreten"
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}