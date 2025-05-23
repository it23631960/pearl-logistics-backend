package com.example.pearllogistics.TicketManagement.Controller;

import com.example.pearllogistics.TicketManagement.DTO.TicketDTO;
import com.example.pearllogistics.TicketManagement.Service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/tickets")
@RequiredArgsConstructor
public class AdminTicketController {

    private final TicketService ticketService;

    @GetMapping
    public ResponseEntity<List<TicketDTO.TicketResponse>> getAllTickets() {
        return ResponseEntity.ok(ticketService.getAllTickets());
    }

    @GetMapping("/pending")
    public ResponseEntity<List<TicketDTO.TicketResponse>> getPendingTickets() {
        return ResponseEntity.ok(ticketService.getPendingTickets());
    }

    @GetMapping("/{ticketId}")
    public ResponseEntity<TicketDTO.TicketResponse> getTicketDetails(@PathVariable Long ticketId) {
        return ResponseEntity.ok(ticketService.getTicketById(ticketId));
    }

    @PutMapping("/{ticketId}/reply")
    public ResponseEntity<TicketDTO.TicketResponse> replyToTicket(
            @PathVariable Long ticketId,
            @RequestBody TicketDTO.AdminReplyRequest replyRequest) {
        return ResponseEntity.ok(ticketService.replyToTicket(ticketId, replyRequest));
    }
}
