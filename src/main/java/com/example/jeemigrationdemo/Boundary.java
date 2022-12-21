package com.example.jeemigrationdemo;

import lombok.extern.slf4j.Slf4j;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Slf4j
@Path("/greetings")
public class Boundary {
    @Inject
    Repository repository;

    @GET
    public Greeting greeting() {
        log.info("GET greeting");
        return repository.greeting();
    }
}
