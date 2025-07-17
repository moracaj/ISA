package com.project.onlybuns.service;

import com.project.onlybuns.model.RegisteredUser;
import com.project.onlybuns.repository.RegisteredUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InactivityNotifier {
    @Autowired
    private RegisteredUserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserActivityService userActivityService;



  // @Scheduled(cron = "0 0/5 * * * ?") // Svakih 5 minuta
   @Scheduled(cron = "0 19 21 * * ?") // Svakog dana u 9:00
    public void notifyInactiveUsers() {
       try {
           //LocalDateTime oneWeekAgo = LocalDateTime.now().minusDays(7);
           //List<RegisteredUser> inactiveUsers = userRepository.findByLastActiveDateBefore(oneWeekAgo);

           LocalDateTime fiveMinutesAgo = LocalDateTime.now().minusMinutes(2);

           List<RegisteredUser> inactiveUsers = userRepository.findByLastActiveDateBefore(fiveMinutesAgo);

           for (RegisteredUser user : inactiveUsers) {
               String summary = userActivityService.generateWeeklySummary(user);
               emailService.sendSimpleEmail(user.getEmail(), "We miss you!", summary);

               System.out.println("Našao!");

           }
       }catch (Exception e) {
           System.err.println("⚠ Greška u zakazanoj metodi: " + e.getMessage());
           e.printStackTrace(); // dodaje ceo stack trace u konzolu
       }
    }

    public void testNotifyInactiveUsers() {
        LocalDateTime fiveMinutesAgo = LocalDateTime.now().minusMinutes(5);
        List<RegisteredUser> inactiveUsers = userRepository.findByLastActiveDateBefore(fiveMinutesAgo);

        for (RegisteredUser user : inactiveUsers) {
            String summary = userActivityService.generateWeeklySummary(user);
            emailService.sendSimpleEmail(user.getEmail(), "We miss you!", summary);
        }
    }
}
