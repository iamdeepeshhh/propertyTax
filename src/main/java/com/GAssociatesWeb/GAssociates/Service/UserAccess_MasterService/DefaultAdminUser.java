package com.GAssociatesWeb.GAssociates.Service.UserAccess_MasterService;

import com.GAssociatesWeb.GAssociates.Entity.UserAccess_Entity.UserAccess;
import com.GAssociatesWeb.GAssociates.Repository.PropertySurveyRepository.UserAccessRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultAdminUser {

    @Autowired
    private UserAccessRepository userAccessRepository;

    @PostConstruct
    private void createDefaultAdminUser() {
        if (userAccessRepository.count() == 0) {
            UserAccess admin = new UserAccess();
            admin.setUsername("admin");
            admin.setPassword("admin0612"); // Storing password in plain text (not recommended for production)
            admin.setProfile("ITA");
            admin.setStatus("Active");
            admin.setFirstname("Admin");
            admin.setLastname("User");
            admin.setPhone("1234567890");
            admin.setEmail("admin@example.com");
            userAccessRepository.save(admin);
            System.out.println("Default admin user created.");
        }
    }
}