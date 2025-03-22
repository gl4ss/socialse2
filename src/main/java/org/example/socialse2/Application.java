package org.example.socialse2;

import org.example.socialse2.dto.RegistrationDto;
import org.example.socialse2.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner applicationStartupRunner(ConfigurableApplicationContext ctx) {
        return args -> {
            UserService userService = ctx.getBean(UserService.class);
            // Check if the admin user already exists
            if (userService.findByUsername("admin") == null) {
                RegistrationDto adminUser = new RegistrationDto();
                adminUser.setFirstName("Admin");
                adminUser.setLastName("User");
                adminUser.setEmail("admin@example.com");
                adminUser.setUsername("admin");
                adminUser.setPassword("admin");
                userService.createUser(adminUser);
                userService.makeUserAdmin(adminUser.getUsername());
                System.out.println("Admin user created!");
            } else {
                System.out.println("Admin user already exists.");
            }
        };
    }

}
