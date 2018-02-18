package com.ITSMBroker;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LookupLauncher {
    public static void main(String[] args) { SpringApplication.run(LookupLauncher.class, args); }
    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) { return (String[] args) -> {}; }
}
