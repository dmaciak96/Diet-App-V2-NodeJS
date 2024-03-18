package pl.daveproject.webdiet;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@PWA(name = "Webdiet", shortName = "Webdiet")
public class Webdiet implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(Webdiet.class, args);
    }
}
