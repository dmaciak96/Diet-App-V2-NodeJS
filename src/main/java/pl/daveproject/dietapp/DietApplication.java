package pl.daveproject.dietapp;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@PWA(name = "Webdiet", shortName = "Webdiet")
public class DietApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(DietApplication.class, args);
    }
}
