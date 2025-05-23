package com.example.pearllogistics.Email.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:noreply@example.com}")
    private String senderEmail;

    @Value("${app.logo.path:static/images/logo.png}")
    private String logoPath;

    public void sendTicketReplyEmail(String to, String subject, String body) throws MessagingException {
        log.info("Preparing to send email to: {}", to);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(senderEmail);
        helper.setTo(to);
        helper.setSubject("Reply To Ticket: " + subject);

        String htmlContent =
                "<html>" +
                        "  <head>" +
                        "    <style>body{font-family:Arial,sans-serif;line-height:1.6;color:#333;max-width:600px;margin:0 auto;padding:20px}header{text-align:center;margin-bottom:20px}h1{color:#003366;margin-bottom:10px}main{background-color:#f9f9f9;padding:20px;border-radius:5px}footer{text-align:center;margin-top:20px;font-size:12px;color:#666}</style>" +
                        "  </head>" +
                        "  <body>" +
                        "    <header><img src=\"cid:logo\" alt=\"Pearl Logistics Logo\" style=\"max-width:200px;height:auto\"></header>" +
                        "    <h1>Ticket Reply</h1>" +
                        "    <main><p>Your ticket regarding '" + subject + "' has been answered.</p>" +
                        "      <div>" + body + "</div>" +
                        "    </main>" +
                        "    <footer><p>Thank you for contacting us. If you have any further questions, please don't hesitate to reach out.</p></footer>" +
                        "  </body>" +
                        "  <footer>" +
                        "    <p>© " + java.time.Year.now().getValue() + " Pearl Logistics. All rights reserved.</p>" +
                        "  </footer>" +
                        "</html>";

        helper.setText(htmlContent, true);
        addLogoToEmail(helper);

        log.info("Sending email to: {}", to);
        mailSender.send(message);
        log.info("Email sent successfully to: {}", to);
    }

    public void sendSignedInEmail(String to) throws MessagingException {
        log.info("Preparing to send sign-in notification to: {}", to);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        // Format current date and time
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy 'at' hh:mm a");
        String formattedDateTime = now.format(formatter);

        helper.setFrom(senderEmail);
        helper.setTo(to);
        helper.setSubject("New Sign-In to Your Pearl Logistics Account");

        String htmlContent =
                "<html>" +
                        "  <head>" +
                        "    <style>body{font-family:Arial,sans-serif;line-height:1.6;color:#333;max-width:600px;margin:0 auto;padding:20px}header{text-align:center;margin-bottom:20px}h1{color:#003366;margin-bottom:10px}main{background-color:#f9f9f9;padding:20px;border-radius:5px}footer{text-align:center;margin-top:20px;font-size:12px;color:#666}.btn{display:inline-block;background-color:#003366;color:#fff;padding:10px 20px;text-decoration:none;border-radius:5px;margin-top:15px}</style>" +
                        "  </head>" +
                        "  <body>" +
                        "    <header><img src=\"cid:logo\" alt=\"Pearl Logistics Logo\" style=\"max-width:200px;height:auto\"></header>" +
                        "    <h1>New Sign-In Detected</h1>" +
                        "    <main><p>We detected a new sign-in to your Pearl Logistics account.</p>" +
                        "      <div>" +
                        "      <h3>Sign-in details:</h3>" +
                        "      <ul>" +
                        "        <li>Account: " + to + "</li>" +
                        "        <li>Date & Time: " + formattedDateTime + "</li>" +
                        "      </ul>" +
                        "    </div>" +
                        "    <p>If this was you, no action is needed. If you didn't sign in to your account recently, please secure your account by changing your password immediately.</p>" +
                        "      <a href=\"#\" class=\"btn\">Review Account Activity</a>" +
                        "    </main>" +
                        "  </body>" +
                        "  <footer>" +
                        "    <p>© " + java.time.Year.now().getValue() + " Pearl Logistics. All rights reserved.</p>" +
                        "    <p>This is an automated message, please do not reply.</p>" +
                        "  </footer>" +
                        "</html>";

        helper.setText(htmlContent, true);
        addLogoToEmail(helper);

        log.info("Sending sign-in notification to: {}", to);
        mailSender.send(message);
        log.info("Sign-in notification sent successfully to: {}", to);
    }

    public void sendCustomOrderConfirmationEmail(String to, String firstName, String lastName,
                                                 String contactNumber, String street, String city,
                                                 String state, String zipCode, String country,
                                                 String productName, String productDescription,
                                                 String fromCountry, String productLink,
                                                 Boolean cashOnDelivery) throws MessagingException {
        log.info("Preparing to send custom order confirmation to: {}", to);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        // Format current date and time
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy 'at' hh:mm a");
        String formattedDateTime = now.format(formatter);

        helper.setFrom(senderEmail);
        helper.setTo(to);
        helper.setSubject("Custom Product Request Confirmation - Pearl Logistics");

        String htmlContent =
                "<html>" +
                        "  <head>" +
                        "    <style>body{font-family:Arial,sans-serif;line-height:1.6;color:#333;max-width:600px;margin:0 auto;padding:20px}header{text-align:center;margin-bottom:20px}h1{color:#003366;margin-bottom:10px}main{background-color:#f9f9f9;padding:20px;border-radius:5px}footer{text-align:center;margin-top:20px;font-size:12px;color:#666}.btn{display:inline-block;background-color:#003366;color:#fff;padding:10px 20px;text-decoration:none;border-radius:5px;margin-top:15px}table{width:100%;border-collapse:collapse}th,td{padding:8px;text-align:left;border-bottom:1px solid #ddd}</style>" +
                        "  </head>" +
                        "  <body>" +
                        "    <header><img src=\"cid:logo\" alt=\"Pearl Logistics Logo\" style=\"max-width:200px;height:auto\"></header>" +
                        "    <h1>Custom Product Request Received</h1>" +
                        "    <main><p>Thank you for your custom product request. We have received your details and our team will review your request shortly.</p>" +
                        "      <div>" +
                        "      <h3>Request Details:</h3>" +
                        "      <table>" +
                        "        <tr>" +
                        "          <th>Date & Time:</th>" +
                        "          <td>" + formattedDateTime + "</td>" +
                        "        </tr>" +
                        "        <tr>" +
                        "          <th>Customer Name:</th>" +
                        "          <td>" + firstName + " " + lastName + "</td>" +
                        "        </tr>" +
                        "        <tr>" +
                        "          <th>Email:</th>" +
                        "          <td>" + to + "</td>" +
                        "        </tr>" +
                        "        <tr>" +
                        "          <th>Contact Number:</th>" +
                        "          <td>" + contactNumber + "</td>" +
                        "        </tr>" +
                        "        <tr>" +
                        "          <th>Address:</th>" +
                        "          <td>" + street + ", " + city + ", " + state + ", " + zipCode + ", " + country + "</td>" +
                        "        </tr>" +
                        "        <tr>" +
                        "          <th colspan=\"2\">Product Information</th>" +
                        "        </tr>" +
                        "        <tr>" +
                        "          <th>Product Name:</th>" +
                        "          <td>" + (productName != null ? productName : "Not specified") + "</td>" +
                        "        </tr>" +
                        "        <tr>" +
                        "          <th>Description:</th>" +
                        "          <td>" + (productDescription != null ? productDescription : "Not specified") + "</td>" +
                        "        </tr>" +
                        "        <tr>" +
                        "          <th>From Country:</th>" +
                        "          <td>" + (fromCountry != null ? fromCountry : "Not specified") + "</td>" +
                        "        </tr>" +
                        "        <tr>" +
                        "          <th>Product Link:</th>" +
                        "          <td>" + (productLink != null ? productLink : "Not specified") + "</td>" +
                        "        </tr>" +
                        "        <tr>" +
                        "          <th>Payment Method:</th>" +
                        "          <td>" + (cashOnDelivery ? "Cash On Delivery" : "Not specified") + "</td>" +
                        "        </tr>" +
                        "      </table>" +
                        "    </div>" +
                        "    <p>Our team will evaluate your request and get back to you within 24-48 hours with more information. If you have any questions in the meantime, please don't hesitate to reach out to our customer service team.</p>" +
                        "      <a href=\"#\" class=\"btn\">Track Your Request</a>" +
                        "    </main>" +
                        "  </body>" +
                        "  <footer>" +
                        "    <p>© " + java.time.Year.now().getValue() + " Pearl Logistics. All rights reserved.</p>" +
                        "    <p>This is an automated message, please do not reply.</p>" +
                        "  </footer>" +
                        "</html>";

        helper.setText(htmlContent, true);
        addLogoToEmail(helper);

        log.info("Sending custom order confirmation to: {}", to);
        mailSender.send(message);
        log.info("Custom order confirmation sent successfully to: {}", to);
    }

    // New method to send notification when order is deleted by admin
    public void sendOrderDeletionNotificationEmail(String to, String customerName, String orderNumber, String productName) throws MessagingException {
        log.info("Preparing to send order deletion notification to: {}", to);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        // Format current date and time
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy 'at' hh:mm a");
        String formattedDateTime = now.format(formatter);

        helper.setFrom(senderEmail);
        helper.setTo(to);
        helper.setSubject("Important: Your Order Has Been Cancelled - Pearl Logistics");

        String htmlContent =
                "<html>" +
                        "  <head>" +
                        "    <style>body{font-family:Arial,sans-serif;line-height:1.6;color:#333;max-width:600px;margin:0 auto;padding:20px}header{text-align:center;margin-bottom:20px}h1{color:#003366;margin-bottom:10px}main{background-color:#f9f9f9;padding:20px;border-radius:5px}footer{text-align:center;margin-top:20px;font-size:12px;color:#666}.btn{display:inline-block;background-color:#fce803;color:#000000;padding:10px 20px;text-decoration:none;border-radius:5px;margin-top:15px}</style>" +
                        "  </head>" +
                        "  <body>" +
                        "    <header><img src=\"cid:logo\" alt=\"Pearl Logistics Logo\" style=\"max-width:200px;height:auto\"></header>" +
                        "    <h1>Order Cancellation Notice</h1>" +
                        "    <main>" +
                        "      <p>Dear " + customerName + ",</p>" +
                        "      <p>We regret to inform you that your order #" + orderNumber + " for the product \"" + productName + "\" has been cancelled.</p>" +
                        "      <p>This action was taken on " + formattedDateTime + ".</p>" +
                        "      <p>If you believe this was done in error or would like more information, please don't hesitate to contact our customer support team.</p>" +
                        "      <a href=\"#\" class=\"btn\">Contact Customer Support</a>" +
                        "    </main>" +
                        "  </body>" +
                        "  <footer>" +
                        "    <p>© " + java.time.Year.now().getValue() + " Pearl Logistics. All rights reserved.</p>" +
                        "    <p>This is an automated message, please do not reply.</p>" +
                        "  </footer>" +
                        "</html>";

        helper.setText(htmlContent, true);
        addLogoToEmail(helper);

        log.info("Sending order deletion notification to: {}", to);
        mailSender.send(message);
        log.info("Order deletion notification sent successfully to: {}", to);
    }

    // Extracted method to add logo to email - reduces code duplication
    private void addLogoToEmail(MimeMessageHelper helper) throws MessagingException {
        File customLogoFile = new File("C:\\Users\\Lenovo\\Desktop\\OOAD-Project\\group-project-group-3\\Backend\\pearllogistics\\pearllogistics\\src\\main\\resources\\static\\images\\logo.png");
        if (customLogoFile.exists()) {
            log.info("Using custom logo from absolute path");
            helper.addInline("logo", new FileSystemResource(customLogoFile));
        } else {
            try {
                Resource resource = new ClassPathResource(logoPath.replace("static/", ""));
                if (resource.exists()) {
                    log.info("Using logo from classpath");
                    helper.addInline("logo", resource);
                } else {
                    File logoFile = new File(logoPath);
                    if (logoFile.exists()) {
                        log.info("Using logo from configured path");
                        helper.addInline("logo", new FileSystemResource(logoFile));
                    } else {
                        log.warn("Logo not found in any location, email will be sent without logo");
                    }
                }
            } catch (Exception e) {
                log.error("Error adding logo to email: {}", e.getMessage());
            }
        }
    }
}