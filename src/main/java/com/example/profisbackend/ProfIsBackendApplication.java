package com.example.profisbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jdbc.JdbcRepositoriesAutoConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.net.URI;

@SpringBootApplication(exclude = {JdbcRepositoriesAutoConfiguration.class})
public class ProfIsBackendApplication {

    public static void main(String[] args) {
        /*
        try {
            PrintStream out = new PrintStream(new FileOutputStream(System.getProperty("user.home") + "/Desktop/app_error.log"));
            System.setOut(out);
            System.setErr(out);

            String path = ProfIsBackendApplication.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            System.setProperty("user.dir", new File(path).getParent());

         */

            SpringApplication app = new SpringApplication(ProfIsBackendApplication.class);
            //I have tried to make it work as an native apple app. It didn't work out.

            //app.setHeadless(false);
            app.run(args);
            /*
        } catch (Exception e) {
            e.printStackTrace();
        }
             */
    }


    @EventListener(ApplicationReadyEvent.class)
    public void onReady() {
        launchBrowser();
    }

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
