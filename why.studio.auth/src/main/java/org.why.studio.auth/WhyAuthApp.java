package org.why.studio.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class WhyAuthApp {

    public static void main(String[] args) {
        SpringApplication.run(WhyAuthApp.class, args);
    }

}
