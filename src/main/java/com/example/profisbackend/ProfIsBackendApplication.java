package com.example.profisbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jdbc.JdbcRepositoriesAutoConfiguration;

@SpringBootApplication(exclude = {JdbcRepositoriesAutoConfiguration.class})
public class ProfIsBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProfIsBackendApplication.class, args);
    }
}
