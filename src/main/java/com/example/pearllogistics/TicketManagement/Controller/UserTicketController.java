package com.example.pearllogistics.TicketManagement.Controller;

import com.example.pearllogistics.TicketManagement.DTO.TicketDTO;
import com.example.pearllogistics.TicketManagement.Service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/tickets")
@RequiredArgsConstructor
public class UserTicketController {

    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<TicketDTO.TicketResponse> createTicket(@RequestBody TicketDTO.TicketRequest request) {
        return new ResponseEntity<>(ticketService.createTicket(request), HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TicketDTO.TicketResponse>> getUserTickets(@PathVariable Long userId) {
        return ResponseEntity.ok(ticketService.getTicketsByUser(userId));
    }

    @GetMapping("/{ticketId}")
    public ResponseEntity<TicketDTO.TicketResponse> getTicketDetails(@PathVariable Long ticketId) {
        return ResponseEntity.ok(ticketService.getTicketById(ticketId));
    }
}