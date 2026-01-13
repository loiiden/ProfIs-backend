package com.example.profisbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jdbc.JdbcRepositoriesAutoConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.awt.*;
import java.net.URI;

@SpringBootApplication(exclude = {JdbcRepositoriesAutoConfiguration.class})
public class ProfIsBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProfIsBackendApplication.class, args);
    }


    @EventListener(ApplicationReadyEvent.class)
    public void launchBrowser() {
        try {
            System.setProperty("java.awt.headless", "false");
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(new URI("http://localhost:8080"));
            }
        } catch (Exception e) {
            // Log the error or handle cases where no browser is available
            e.printStackTrace();
        }
    }
}
