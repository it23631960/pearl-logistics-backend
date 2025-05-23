package com.example.pearllogistics.TicketManagement.Service;

import com.example.pearllogistics.Email.Service.EmailService;
import com.example.pearllogistics.TicketManagement.DTO.TicketDTO;
import com.example.pearllogistics.TicketManagement.Entity.TicketEntity;
import com.example.pearllogistics.UserManagement.Entity.UserEntity;
import com.example.pearllogistics.TicketManagement.Repository.TicketRepository;
import com.example.pearllogistics.UserManagement.Repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    @Value("${app.email.enabled:false}")
    private boolean emailEnabled;

    @Transactional
    public TicketDTO.TicketResponse createTicket(TicketDTO.TicketRequest request) {
        UserEntity user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + request.getUserId()));

        TicketEntity ticket = TicketEntity.builder()
                .user(user)
                .name(request.getName())
                .subject(request.getSubject())
                .email(request.getEmail())
                .description(request.getDescription())
                .replied(false)
                .build();

        TicketEntity savedTicket = ticketRepository.save(ticket);
        return mapToTicketResponse(savedTicket);
    }

    @Transactional(readOnly = true)
    public List<TicketDTO.TicketResponse> getAllTickets() {
        return ticketRepository.findAll().stream()
                .map(this::mapToTicketResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TicketDTO.TicketResponse> getTicketsByUser(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        return ticketRepository.findByUser(user).stream()
                .map(this::mapToTicketResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TicketDTO.TicketResponse> getPendingTickets() {
        return ticketRepository.findByReplied(false).stream()
                .map(this::mapToTicketResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TicketDTO.TicketResponse getTicketById(Long ticketId) {
        TicketEntity ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found with id: " + ticketId));
        return mapToTicketResponse(ticket);
    }

    @Transactional
    public TicketDTO.TicketResponse replyToTicket(Long ticketId, TicketDTO.AdminReplyRequest replyRequest) {
        TicketEntity ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found with id: " + ticketId));

        ticket.setReply(replyRequest.getReply());
        ticket.setReplied(true);
        ticket.setRepliedAt(LocalDateTime.now());

        TicketEntity updatedTicket = ticketRepository.save(ticket);


        if (emailEnabled) {
            try {
                emailService.sendTicketReplyEmail(
                        ticket.getEmail(),
                        ticket.getSubject(),
                        replyRequest.getReply()
                );
            } catch (MessagingException e) {

                System.err.println("Failed to send email notification: " + e.getMessage());

            }
        }

        return mapToTicketResponse(updatedTicket);
    }

    private TicketDTO.TicketResponse mapToTicketResponse(TicketEntity ticket) {
        return TicketDTO.TicketResponse.builder()
                .id(ticket.getId())
                .userId(ticket.getUser().getId())
                .name(ticket.getName())
                .subject(ticket.getSubject())
                .email(ticket.getEmail())
                .description(ticket.getDescription())
                .reply(ticket.getReply())
                .replied(ticket.isReplied())
                .createdAt(ticket.getCreatedAt())
                .repliedAt(ticket.getRepliedAt())
                .build();
    }
}