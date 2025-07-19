//package com.project.onlybuns.controller;
//
//import com.project.onlybuns.model.RegisteredUser;
//import com.project.onlybuns.repository.RegisteredUserRepository;
//import com.project.onlybuns.service.EmailService;
//import com.project.onlybuns.service.UserActivityService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.format.annotation.DateTimeFormat;
//import org.springframework.http.ResponseEntity;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@RestController
//@RequestMapping("/debug")
//public class DebugController {
//
//    @Autowired
//    private RegisteredUserRepository userRepository;
//
//    @Autowired
//    private UserActivityService userActivityService;
//
//    @Autowired
//    private EmailService emailService;
//
//    @GetMapping("/test-email")
//    public ResponseEntity<String> testSimpleMail() {
//        emailService.sendSimpleEmail("moracajelena1@gmail.com", "Test Notifikacija", "Ovo je test poruka iz aplikacije.");
//        return ResponseEntity.ok("✅ Poslata test poruka!");
//    }
//
//    @GetMapping("/test-inactive-summary")
//    public ResponseEntity<String> testInactiveSummary() {
//        LocalDateTime fiveMinutesAgo = LocalDateTime.now().minusMinutes(2);
//        List<RegisteredUser> inactiveUsers = userRepository.findByLastActiveDateBefore(fiveMinutesAgo);
//
//        if (inactiveUsers.isEmpty()) {
//            return ResponseEntity.ok("❌ Nema neaktivnih korisnika.");
//        }
//
//        for (RegisteredUser user : inactiveUsers) {
//            String summary = userActivityService.generateWeeklySummary(user);
//            emailService.sendSimpleEmail(user.getEmail(), "Dugo nisi aktivan!", summary);
//            System.out.println("📧 Poslato korisniku: " + user.getEmail());
//        }
//
//        return ResponseEntity.ok("📨 Notifikacije poslate za " + inactiveUsers.size() + " korisnika.");
//    }
//}
