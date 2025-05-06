package com.kdob.jabs.task1;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CommandLIneRunnerImplementation implements CommandLineRunner {

    @Override
    public void run(String... args) {
        System.out.println("Hello World!");
    }
}
