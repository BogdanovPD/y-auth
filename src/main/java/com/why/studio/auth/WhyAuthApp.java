package com.why.studio.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class WhyAuthApp {

    public static void main(String[] args) {
        SpringApplication.run(WhyAuthApp.class, args);
    }

}
