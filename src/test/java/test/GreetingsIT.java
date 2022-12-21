package test;

import com.example.jeemigrationdemo.Greeting;
import com.github.t1.testcontainers.jee.JeeContainer;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static org.assertj.core.api.BDDAssertions.then;

@Testcontainers
class GreetingsIT {
    @Container static JeeContainer CONTAINER = JeeContainer.create()
        .withDeployment("target/ROOT.war");

    @Path("/greetings")
    public interface Api {
        @GET @Produces(TEXT_PLAIN)
        String textGreeting();

        @GET @Produces(APPLICATION_JSON)
        JsonObject jsonObjectGreeting();

        @GET @Produces(APPLICATION_JSON)
        Greeting jsonGreeting();
    }

    Api api;

    @BeforeEach
    void setUp() {
        api = RestClientBuilder.newBuilder()
            .baseUri(CONTAINER.baseUri())
            .build(Api.class);
    }

    @Test
    void shouldGetTextGreeting() {
        var greeting = api.textGreeting();

        then(greeting).isEqualTo("Hello, World!");
    }

    @Test
    void shouldGetJsonObjectGreeting() {
        var greeting = api.jsonObjectGreeting();

        then(greeting.getString("hello")).isEqualTo("Hello");
        then(greeting.getString("who")).isEqualTo("World");
    }

    @Test
    void shouldGetJsonGreeting() {
        var greeting = api.jsonGreeting();

        then(greeting.getHello()).isEqualTo("Hello");
        then(greeting.getWho()).isEqualTo("World");
    }
}
